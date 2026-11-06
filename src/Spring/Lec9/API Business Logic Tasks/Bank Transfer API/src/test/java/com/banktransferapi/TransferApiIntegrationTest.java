package com.banktransferapi;

import com.banktransferapi.dto.TransferRequestDTO;
import com.banktransferapi.model.Account;
import com.banktransferapi.model.AccountType;
import com.banktransferapi.repo.AccountRepo;
import com.banktransferapi.repo.AccountTypeRepo;
import com.banktransferapi.repo.TransferRepo;
import com.banktransferapi.service.TransferService;
import com.banktransferapi.verification.VerificationCodeStore;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// End-to-end tests over the real HTTP layer, service, transactions and database
// (H2 in place of Oracle). Every test asserts the resulting account balances,
// because "failed transfer must not change balances" is the core rule.
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class TransferApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountRepo accountRepo;

    @Autowired
    private AccountTypeRepo accountTypeRepo;

    @Autowired
    private TransferRepo transferRepo;

    @Autowired
    private VerificationCodeStore verificationStore;

    @Autowired
    private TransferService transferService;

    private AccountType basic;
    private AccountType premium;

    @BeforeEach
    void setUp() {
        // Transfers reference accounts, so they must go first.
        transferRepo.deleteAll();
        accountRepo.deleteAll();

        // Seeded by DataInitializer: BASIC 5000, STANDARD 20000, PREMIUM 100000.
        basic = accountTypeRepo.findByName("BASIC").orElseThrow();
        premium = accountTypeRepo.findByName("PREMIUM").orElseThrow();
    }

    // ---------- successful transfers ----------

    @Test
    void successfulTransferUpdatesBothBalances() throws Exception {
        Account alice = account("ACC-1", "1000.00", true, basic);
        Account bob = account("ACC-2", "500.00", true, basic);

        postTransfer(alice.getId(), bob.getId(), "250.00", null)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.amount").value(250.0));

        assertBalance(alice, "750.00");
        assertBalance(bob, "750.00");
        assertThat(transferRepo.count()).isEqualTo(1);
    }

    @Test
    void transferOfTheWholeBalanceIsAllowed() throws Exception {
        Account alice = account("ACC-1", "300.00", true, basic);
        Account bob = account("ACC-2", "0.00", true, basic);

        postTransfer(alice.getId(), bob.getId(), "300.00", null)
                .andExpect(status().isCreated());

        assertBalance(alice, "0.00");
        assertBalance(bob, "300.00");
    }

    // ---------- validation happens on the DTO (Bean Validation) ----------

    @Test
    void invalidAmountsAreRejectedBeforeAnyAccountIsTouched() throws Exception {
        Account alice = account("ACC-1", "1000.00", true, basic);
        Account bob = account("ACC-2", "0.00", true, basic);

        postTransfer(alice.getId(), bob.getId(), "0.00", null)
                .andExpect(status().isBadRequest());

        postTransfer(alice.getId(), bob.getId(), "-50.00", null)
                .andExpect(status().isBadRequest());

        postTransfer(alice.getId(), bob.getId(), "10.001", null)
                .andExpect(status().isBadRequest());

        assertBalance(alice, "1000.00");
        assertBalance(bob, "0.00");
        assertThat(transferRepo.count()).isZero();
    }

    @Test
    void missingFieldsAndMalformedCodeAreRejected() throws Exception {
        Account alice = account("ACC-1", "1000.00", true, basic);
        Account bob = account("ACC-2", "0.00", true, basic);

        // missing senderAccountId
        mockMvc.perform(post("/transfers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"receiverAccountId\":" + bob.getId() + ",\"amount\":100}"))
                .andExpect(status().isBadRequest());

        // verificationCode that is not 6 digits
        postTransfer(alice.getId(), bob.getId(), "100.00", "12ab")
                .andExpect(status().isBadRequest());

        assertBalance(alice, "1000.00");
        assertBalance(bob, "0.00");
        assertThat(transferRepo.count()).isZero();
    }

    // The same DTO rules apply when the service is called directly (not through
    // the controller): @Validated on the bean + @Valid on the parameter.
    @Test
    void directServiceCallIsValidatedWithTheSameDtoRules() {
        Account alice = account("ACC-1", "1000.00", true, basic);
        Account bob = account("ACC-2", "0.00", true, basic);

        assertThatThrownBy(() -> transferService.transfer(
                new TransferRequestDTO(alice.getId(), bob.getId(), new BigDecimal("-5.00"), null)))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("amount");

        assertBalance(alice, "1000.00");
        assertBalance(bob, "0.00");
        assertThat(transferRepo.count()).isZero();
    }

    // ---------- rule 1: different accounts ----------

    @Test
    void sameAccountIsRejected() throws Exception {
        Account alice = account("ACC-1", "1000.00", true, basic);

        postTransfer(alice.getId(), alice.getId(), "100.00", null)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("different accounts")));

        assertBalance(alice, "1000.00");
        assertThat(transferRepo.count()).isZero();
    }

    // ---------- rule 3: active accounts ----------

    @Test
    void inactiveAccountsAreRejected() throws Exception {
        Account inactive = account("ACC-1", "1000.00", false, basic);
        Account active = account("ACC-2", "1000.00", true, basic);

        postTransfer(inactive.getId(), active.getId(), "100.00", null)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("Sender account is not active")));

        postTransfer(active.getId(), inactive.getId(), "100.00", null)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("Receiver account is not active")));

        assertBalance(inactive, "1000.00");
        assertBalance(active, "1000.00");
        assertThat(transferRepo.count()).isZero();
    }

    // ---------- rule 2: enough balance ----------

    @Test
    void insufficientBalanceIsRejected() throws Exception {
        Account alice = account("ACC-1", "100.00", true, basic);
        Account bob = account("ACC-2", "0.00", true, basic);

        postTransfer(alice.getId(), bob.getId(), "100.01", null)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("Insufficient balance")));

        assertBalance(alice, "100.00");
        assertBalance(bob, "0.00");
        assertThat(transferRepo.count()).isZero();
    }

    // ---------- rule 4: daily limit per account type ----------

    @Test
    void dailyLimitIsEnforcedForTheSenderAccountType() throws Exception {
        Account alice = account("ACC-1", "20000.00", true, basic); // BASIC daily limit 5000
        Account bob = account("ACC-2", "0.00", true, basic);

        postTransfer(alice.getId(), bob.getId(), "4000.00", null)
                .andExpect(status().isCreated());

        postTransfer(alice.getId(), bob.getId(), "1500.00", null)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("Daily transfer limit exceeded")));

        assertBalance(alice, "16000.00");
        assertBalance(bob, "4000.00");
        assertThat(transferRepo.count()).isEqualTo(1);
    }

    // ---------- rule 5: two-step verification ----------

    @Test
    void transferAtTheThresholdNeedsNoCode() throws Exception {
        Account alice = account("ACC-1", "50000.00", true, premium);
        Account bob = account("ACC-2", "0.00", true, premium);

        postTransfer(alice.getId(), bob.getId(), "10000.00", null)
                .andExpect(status().isCreated());

        assertBalance(alice, "40000.00");
        assertBalance(bob, "10000.00");
    }

    @Test
    void largeTransferNeedsAValidCodeAndMovesMoneyOnlyOnce() throws Exception {
        Account alice = account("ACC-1", "50000.00", true, premium);
        Account bob = account("ACC-2", "0.00", true, premium);

        // Step 1: no code -> rejected, nothing persisted, balances untouched.
        postTransfer(alice.getId(), bob.getId(), "20000.00", null)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("verification code")));

        assertBalance(alice, "50000.00");
        assertBalance(bob, "0.00");
        assertThat(transferRepo.count()).isZero();

        String code = verificationStore
                .currentCode(alice.getId(), bob.getId(), new BigDecimal("20000.00"))
                .orElseThrow();
        assertThat(code).matches("\\d{6}");

        // Step 2: the same request with the code goes through.
        postTransfer(alice.getId(), bob.getId(), "20000.00", code)
                .andExpect(status().isCreated());

        assertBalance(alice, "30000.00");
        assertBalance(bob, "20000.00");
        assertThat(transferRepo.count()).isEqualTo(1);

        // The code is single use: replaying it cannot debit the account twice.
        assertThat(verificationStore.currentCode(alice.getId(), bob.getId(), new BigDecimal("20000.00")))
                .isEmpty();

        postTransfer(alice.getId(), bob.getId(), "20000.00", code)
                .andExpect(status().isBadRequest());

        assertBalance(alice, "30000.00");
        assertBalance(bob, "20000.00");
        assertThat(transferRepo.count()).isEqualTo(1);
    }

    @Test
    void invalidVerificationCodeDoesNotMoveMoney() throws Exception {
        Account alice = account("ACC-1", "50000.00", true, premium);
        Account bob = account("ACC-2", "0.00", true, premium);

        postTransfer(alice.getId(), bob.getId(), "20000.00", null)
                .andExpect(status().isBadRequest());

        String wrong = verificationStore
                .currentCode(alice.getId(), bob.getId(), new BigDecimal("20000.00"))
                .orElse("")
                .equals("000000") ? "111111" : "000000";

        postTransfer(alice.getId(), bob.getId(), "20000.00", wrong)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("Invalid or already used verification code")));

        assertBalance(alice, "50000.00");
        assertBalance(bob, "0.00");
        assertThat(transferRepo.count()).isZero();
    }

    // ---------- edge cases ----------

    @Test
    void unknownAccountReturns404() throws Exception {
        Account alice = account("ACC-1", "1000.00", true, basic);

        postTransfer(alice.getId(), 999_999L, "100.00", null)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(containsString("not found")));

        assertBalance(alice, "1000.00");
    }

    @Test
    void accountTypeInUseCannotBeDeleted() throws Exception {
        account("ACC-1", "1000.00", true, basic);

        mockMvc.perform(delete("/account-types/{id}", basic.getId()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("cannot be deleted")));
    }

    // ---------- helpers ----------

    private Account account(String number, String balance, boolean active, AccountType type) {
        Account account = new Account();
        account.setAccountNumber(number);
        account.setHolderName("Holder " + number);
        account.setBalance(new BigDecimal(balance));
        account.setActive(active);
        account.setAccountType(type);
        return accountRepo.save(account);
    }

    private ResultActions postTransfer(Long senderId, Long receiverId, String amount, String verificationCode)
            throws Exception {
        StringBuilder body = new StringBuilder()
                .append("{\"senderAccountId\":").append(senderId)
                .append(",\"receiverAccountId\":").append(receiverId)
                .append(",\"amount\":").append(amount);

        if (verificationCode != null) {
            body.append(",\"verificationCode\":\"").append(verificationCode).append('"');
        }

        body.append('}');

        return mockMvc.perform(post("/transfers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body.toString()));
    }

    private void assertBalance(Account account, String expected) {
        assertThat(accountRepo.findById(account.getId()).orElseThrow().getBalance())
                .isEqualByComparingTo(expected);
    }
}
