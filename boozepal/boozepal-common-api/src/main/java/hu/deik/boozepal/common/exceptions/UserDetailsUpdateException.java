package hu.deik.boozepal.common.exceptions;

public class UserDetailsUpdateException extends Exception {

    private static final long serialVersionUID = 1L;

    public UserDetailsUpdateException() {
        super();
    }

    public UserDetailsUpdateException(String message) {
        super(message);
    }

    public UserDetailsUpdateException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserDetailsUpdateException(Throwable cause) {
        super(cause);
    }

    protected UserDetailsUpdateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
