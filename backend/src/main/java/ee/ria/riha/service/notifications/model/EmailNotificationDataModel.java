package ee.ria.riha.service.notifications.model;

/**
 * Interface for classes that hold email notification messages data.
 */
public interface EmailNotificationDataModel {

    String getFrom();

    String[] getTo();

    String[] getCc();

    String[] getBcc();
}
