package com.banktransferapi.verification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

// Two-step verification for transfers above bank.transfer.verification-threshold.
//
// Step 1: POST /transfers without a code -> a 6-digit code is stored for this
//         exact (sender, receiver, amount), "sent" (logged, simulating an SMS)
//         and the request is rejected with 400.
// Step 2: the same POST with verificationCode -> the code is checked and consumed,
//         so it can never be replayed.
//
// In memory on purpose: losing the codes on a restart just means the client asks
// for a new one, and no money data is kept here.
@Component
public class VerificationCodeStore {

    private static final Logger log = LoggerFactory.getLogger(VerificationCodeStore.class);

    private final Map<String, String> codes = new ConcurrentHashMap<>();

    public void create(Long senderId, Long receiverId, BigDecimal amount) {
        String code = String.format("%06d", ThreadLocalRandom.current().nextInt(1_000_000));

        codes.put(key(senderId, receiverId, amount), code);

        log.info("Simulated SMS: verification code {} for sender {} -> receiver {} amount {}",
                code, senderId, receiverId, amount);
    }

    // Checks and consumes in one step. A wrong code is consumed too, so a client
    // must ask for a new one instead of guessing repeatedly.
    public boolean matches(Long senderId, Long receiverId, BigDecimal amount, String code) {
        if (code == null) {
            return false;
        }

        return code.equals(codes.remove(key(senderId, receiverId, amount)));
    }

    // Read-only view used by tests (and handy when debugging the simulated SMS).
    public Optional<String> currentCode(Long senderId, Long receiverId, BigDecimal amount) {
        return Optional.ofNullable(codes.get(key(senderId, receiverId, amount)));
    }

    // "1:2:20000" — stripTrailingZeros keeps 20000 and 20000.00 on the same key,
    // because BigDecimal.equals() also compares the scale.
    private String key(Long senderId, Long receiverId, BigDecimal amount) {
        return senderId + ":" + receiverId + ":" + amount.stripTrailingZeros().toPlainString();
    }
}
