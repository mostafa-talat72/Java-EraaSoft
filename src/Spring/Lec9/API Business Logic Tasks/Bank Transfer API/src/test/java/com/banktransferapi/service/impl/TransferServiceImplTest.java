package com.banktransferapi.service.impl;

import com.banktransferapi.config.BankTransferProperties;
import com.banktransferapi.dto.TransferDTO;
import com.banktransferapi.dto.TransferRequestDTO;
import com.banktransferapi.exception.BadRequestException;
import com.banktransferapi.exception.VerificationRequiredException;
import com.banktransferapi.helper.AccountHelper;
import com.banktransferapi.mapper.TransferMapper;
import com.banktransferapi.model.Account;
import com.banktransferapi.model.AccountType;
import com.banktransferapi.model.Transfer;
import com.banktransferapi.repo.TransferRepo;
import com.banktransferapi.verification.VerificationCodeStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

// Unit tests for the money rules: every failure path must leave both balances
// untouched and persist nothing, and the happy path must move money exactly once.
@ExtendWith(MockitoExtension.class)
class TransferServiceImplTest {

    @Mock
    private TransferRepo transferRepo;

    @Mock
    private TransferMapper transferMapper;

    @Mock
    private AccountHelper accountHelper;

    @Mock
    private VerificationCodeStore verificationStore;

    private TransferServiceImpl service;
    private TransferRequestDTO request;
    private Account sender;
    private Account receiver;

    @BeforeEach
    void setUp() {
        BankTransferProperties properties = new BankTransferProperties();
        properties.setVerificationThreshold(new BigDecimal("10000"));

        service = new TransferServiceImpl(transferRepo, transferMapper, accountHelper,
                properties, verificationStore);

        sender = account(1L, "5000.00", true, type("BASIC", "5000.00"));
        receiver = account(2L, "0.00", true, type("BASIC", "5000.00"));

        request = new TransferRequestDTO(1L, 2L, new BigDecimal("100.00"), null);
    }

    // ---------- validation (rule 1 + defensive guards) ----------

    @Test
    void rejectsSameAccountBeforeLocking() {
        request.setReceiverAccountId(1L);

        assertThatThrownBy(() -> service.transfer(request))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("different accounts");

        verifyNoInteractions(accountHelper, transferRepo);
    }

    // ---------- rule 3: active accounts ----------

    @Test
    void rejectsInactiveSenderWithoutTouchingBalances() {
        sender.setActive(false);
        stubLocks();

        assertThatThrownBy(() -> service.transfer(request))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("Sender account is not active");

        assertBalancesUnchanged();
        verify(transferRepo, never()).save(any());
    }

    @Test
    void rejectsInactiveReceiverWithoutTouchingBalances() {
        receiver.setActive(false);
        stubLocks();

        assertThatThrownBy(() -> service.transfer(request))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("Receiver account is not active");

        assertBalancesUnchanged();
        verify(transferRepo, never()).save(any());
    }

    // ---------- rule 2: enough balance ----------

    @Test
    void rejectsInsufficientBalanceWithoutTouchingBalances() {
        sender.setBalance(new BigDecimal("50.00"));
        stubLocks();

        assertThatThrownBy(() -> service.transfer(request))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("Insufficient balance");

        assertThat(sender.getBalance()).isEqualByComparingTo("50.00");
        assertThat(receiver.getBalance()).isEqualByComparingTo("0.00");
        verify(transferRepo, never()).save(any());
    }

    // ---------- rule 4: daily limit ----------

    @Test
    void rejectsWhenDailyLimitWouldBeExceeded() {
        stubLocks();
        when(transferRepo.sumAmountBySenderAndPeriod(anyLong(), any(), any()))
                .thenReturn(new BigDecimal("4950.00"));

        assertThatThrownBy(() -> service.transfer(request))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Daily transfer limit exceeded");

        assertBalancesUnchanged();
        verify(transferRepo, never()).save(any());
    }

    @Test
    void allowsExactlyTheDailyLimit() {
        stubLocks();
        stubPersistence();
        when(transferRepo.sumAmountBySenderAndPeriod(anyLong(), any(), any()))
                .thenReturn(new BigDecimal("4900.00"));

        service.transfer(request);

        assertThat(sender.getBalance()).isEqualByComparingTo("4900.00");
        assertThat(receiver.getBalance()).isEqualByComparingTo("100.00");
        verify(transferRepo).save(any());
    }

    // ---------- successful transfer ----------

    @Test
    void successfulTransferMovesMoneyAndStampsOneTimestamp() {
        stubLocks();
        stubPersistence();
        stubZeroUsage();

        service.transfer(request);

        ArgumentCaptor<Transfer> transferCaptor = ArgumentCaptor.forClass(Transfer.class);
        verify(transferRepo).save(transferCaptor.capture());

        ArgumentCaptor<LocalDateTime> startCaptor = ArgumentCaptor.forClass(LocalDateTime.class);
        ArgumentCaptor<LocalDateTime> endCaptor = ArgumentCaptor.forClass(LocalDateTime.class);
        verify(transferRepo).sumAmountBySenderAndPeriod(eq(1L), startCaptor.capture(), endCaptor.capture());

        Transfer saved = transferCaptor.getValue();
        LocalDateTime start = startCaptor.getValue();
        LocalDateTime end = endCaptor.getValue();

        // the record and the daily window are derived from the same instant
        assertThat(start).isEqualTo(saved.getCreatedAt().toLocalDate().atStartOfDay());
        assertThat(end).isEqualTo(start.plusDays(1));
        assertThat(saved.getCreatedAt()).isBetween(start, end.minusNanos(1));

        assertThat(saved.getSender()).isSameAs(sender);
        assertThat(saved.getReceiver()).isSameAs(receiver);

        assertThat(sender.getBalance()).isEqualByComparingTo("4900.00");
        assertThat(receiver.getBalance()).isEqualByComparingTo("100.00");
    }

    @Test
    void locksAccountsInAscendingIdOrder() {
        Account low = account(3L, "1000.00", true, type("BASIC", "5000.00"));
        Account high = account(7L, "1000.00", true, type("BASIC", "5000.00"));

        request.setSenderAccountId(7L);
        request.setReceiverAccountId(3L);

        when(accountHelper.lockOrThrow(3L)).thenReturn(low);
        when(accountHelper.lockOrThrow(7L)).thenReturn(high);
        stubPersistence();
        stubZeroUsage();

        service.transfer(request);

        InOrder inOrder = inOrder(accountHelper);
        inOrder.verify(accountHelper).lockOrThrow(3L);
        inOrder.verify(accountHelper).lockOrThrow(7L);
    }

    // ---------- rule 5: verification for large transfers ----------

    @Test
    void transferAtTheThresholdNeedsNoCode() {
        sender.setBalance(new BigDecimal("50000.00"));
        sender.setAccountType(type("PREMIUM", "100000.00"));
        request.setAmount(new BigDecimal("10000.00"));

        stubLocks();
        stubPersistence();
        stubZeroUsage();

        service.transfer(request);

        verifyNoInteractions(verificationStore);
        assertThat(sender.getBalance()).isEqualByComparingTo("40000.00");
    }

    @Test
    void largeTransferWithoutCodeIssuesCodeAndSavesNothing() {
        sender.setBalance(new BigDecimal("50000.00"));
        sender.setAccountType(type("PREMIUM", "100000.00"));
        request.setAmount(new BigDecimal("20000.00"));

        stubLocks();
        stubZeroUsage();

        assertThatThrownBy(() -> service.transfer(request))
                .isInstanceOf(VerificationRequiredException.class)
                .hasMessageContaining("verification code");

        verify(verificationStore).create(eq(1L), eq(2L), any());
        verify(transferRepo, never()).save(any());
        assertThat(sender.getBalance()).isEqualByComparingTo("50000.00");
        assertThat(receiver.getBalance()).isEqualByComparingTo("0.00");
    }

    @Test
    void largeTransferWithInvalidCodeSavesNothing() {
        sender.setBalance(new BigDecimal("50000.00"));
        sender.setAccountType(type("PREMIUM", "100000.00"));
        request.setAmount(new BigDecimal("20000.00"));
        request.setVerificationCode("000000");

        stubLocks();
        stubZeroUsage();
        when(verificationStore.matches(any(), any(), any(), eq("000000"))).thenReturn(false);

        assertThatThrownBy(() -> service.transfer(request))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("Invalid or already used verification code");

        verify(transferRepo, never()).save(any());
        assertThat(sender.getBalance()).isEqualByComparingTo("50000.00");
        assertThat(receiver.getBalance()).isEqualByComparingTo("0.00");
    }

    @Test
    void largeTransferWithValidCodeMovesMoney() {
        sender.setBalance(new BigDecimal("50000.00"));
        sender.setAccountType(type("PREMIUM", "100000.00"));
        request.setAmount(new BigDecimal("20000.00"));
        request.setVerificationCode("123456");

        stubLocks();
        stubPersistence();
        stubZeroUsage();
        when(verificationStore.matches(any(), any(), any(), eq("123456"))).thenReturn(true);

        service.transfer(request);

        verify(verificationStore).matches(any(), any(), any(), eq("123456"));
        verify(transferRepo).save(any());

        assertThat(sender.getBalance()).isEqualByComparingTo("30000.00");
        assertThat(receiver.getBalance()).isEqualByComparingTo("20000.00");
    }

    // ---------- helpers ----------

    private void stubLocks() {
        when(accountHelper.lockOrThrow(1L)).thenReturn(sender);
        when(accountHelper.lockOrThrow(2L)).thenReturn(receiver);
    }

    private void stubZeroUsage() {
        when(transferRepo.sumAmountBySenderAndPeriod(anyLong(), any(), any()))
                .thenReturn(BigDecimal.ZERO);
    }

    private void stubPersistence() {
        when(transferMapper.convertFromTransferRequestDtoToTransfer(any()))
                .thenAnswer(invocation -> new Transfer());
        when(transferRepo.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(transferMapper.convertFromTransferToTransferDto(any()))
                .thenReturn(new TransferDTO());
    }

    private void assertBalancesUnchanged() {
        assertThat(sender.getBalance()).isEqualByComparingTo("5000.00");
        assertThat(receiver.getBalance()).isEqualByComparingTo("0.00");
    }

    private static AccountType type(String name, String dailyLimit) {
        AccountType type = new AccountType();
        type.setId(10L);
        type.setName(name);
        type.setDailyLimit(new BigDecimal(dailyLimit));
        return type;
    }

    private static Account account(Long id, String balance, boolean active, AccountType type) {
        Account account = new Account();
        account.setId(id);
        account.setAccountNumber("ACC-" + id);
        account.setHolderName("Holder " + id);
        account.setBalance(new BigDecimal(balance));
        account.setActive(active);
        account.setAccountType(type);
        return account;
    }
}
