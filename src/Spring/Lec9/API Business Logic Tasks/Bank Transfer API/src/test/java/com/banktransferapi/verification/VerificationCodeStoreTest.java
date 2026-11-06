package com.banktransferapi.verification;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

// The store is small enough to test directly: generation, single use,
// binding to (sender, receiver, amount) and the amount-scale normalization.
class VerificationCodeStoreTest {

    private VerificationCodeStore store;

    @BeforeEach
    void setUp() {
        store = new VerificationCodeStore();
    }

    @Test
    void generatedCodeIsSixDigitsAndVisible() {
        store.create(1L, 2L, new BigDecimal("15000.00"));

        assertThat(store.currentCode(1L, 2L, new BigDecimal("15000.00")).orElseThrow())
                .matches("\\d{6}");
    }

    @Test
    void correctCodeMatchesOnlyOnce() {
        store.create(1L, 2L, new BigDecimal("15000.00"));
        String code = store.currentCode(1L, 2L, new BigDecimal("15000.00")).orElseThrow();

        assertThat(store.matches(1L, 2L, new BigDecimal("15000.00"), code)).isTrue();
        assertThat(store.matches(1L, 2L, new BigDecimal("15000.00"), code)).isFalse();
        assertThat(store.currentCode(1L, 2L, new BigDecimal("15000.00"))).isEmpty();
    }

    @Test
    void wrongCodeIsRejectedAndConsumed() {
        store.create(1L, 2L, new BigDecimal("15000.00"));
        String code = store.currentCode(1L, 2L, new BigDecimal("15000.00")).orElseThrow();
        String wrong = code.equals("000000") ? "111111" : "000000";

        assertThat(store.matches(1L, 2L, new BigDecimal("15000.00"), wrong)).isFalse();

        // consumed on the failed attempt: the right code no longer works either
        assertThat(store.matches(1L, 2L, new BigDecimal("15000.00"), code)).isFalse();
    }

    @Test
    void codeIsBoundToTheExactOperation() {
        store.create(1L, 2L, new BigDecimal("15000.00"));
        String code = store.currentCode(1L, 2L, new BigDecimal("15000.00")).orElseThrow();

        assertThat(store.matches(1L, 3L, new BigDecimal("15000.00"), code)).isFalse();
        assertThat(store.matches(1L, 2L, new BigDecimal("16000.00"), code)).isFalse();
    }

    @Test
    void amountScaleDoesNotSplitTheKey() {
        store.create(1L, 2L, new BigDecimal("15000"));

        assertThat(store.currentCode(1L, 2L, new BigDecimal("15000.00"))).isPresent();
    }

    @Test
    void nullCodeNeverMatches() {
        store.create(1L, 2L, new BigDecimal("15000.00"));

        assertThat(store.matches(1L, 2L, new BigDecimal("15000.00"), null)).isFalse();
    }
}
