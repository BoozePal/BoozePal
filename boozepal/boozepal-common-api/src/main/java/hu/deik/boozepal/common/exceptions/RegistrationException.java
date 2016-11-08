package hu.deik.boozepal.common.exceptions;

public class RegistrationException extends Exception {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public RegistrationException(String message) {
        super(message);
    }

    public RegistrationException(String message, Throwable cause) {
        super(message, cause);
    }

    public RegistrationException(Throwable cause) {
        super(cause);
    }

    protected RegistrationException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
