package ee.ria.riha.service.notifications.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Holds data for new issue comment email notification messages.
 */
@Getter
@Setter
public class NewIssueCommentEmailNotification extends SimpleHtmlEmailNotification {

    private String infoSystemFullName;
    private String infoSystemShortName;
    private String baseUrl;

    @Builder
    public NewIssueCommentEmailNotification(String from, String[] to, String subject, String[] cc, String[] bcc, String infoSystemFullName,
                                            String infoSystemShortName, String baseUrl) {
        super(from, to, subject, cc, bcc);
        this.infoSystemFullName = infoSystemFullName;
        this.infoSystemShortName = infoSystemShortName;
        this.baseUrl = baseUrl;
    }
}
