package ee.ria.riha.service.notifications.handlers;

/**
 * Indicates exception during notification handler work
 *
 * @author Valentin Suhnjov
 */
public class NotificationHandlerException extends RuntimeException {

    public NotificationHandlerException() {
        super();
    }

    public NotificationHandlerException(String message) {
        super(message);
    }

    public NotificationHandlerException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotificationHandlerException(Throwable cause) {
        super(cause);
    }

}
