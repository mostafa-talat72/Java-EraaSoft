package com.banktransferapi.exception;

// Raised on step 1 of a large transfer: a code was generated and must be sent back.
// It extends BadRequestException, so the existing handler already answers 400;
// the subclass exists to make the intent explicit and to keep it testable.
public class VerificationRequiredException extends BadRequestException {

    public VerificationRequiredException(String message) {
        super(message);
    }
}
