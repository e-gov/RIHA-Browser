package ee.ria.riha.service.notification.model;

/**
 * Interface for classes that hold email notification messages data.
 */
public interface EmailNotificationDataModel {

    String getFrom();

    String getTo();

    String[] getCc();

    String[] getBcc();
}
