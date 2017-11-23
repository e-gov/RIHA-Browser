package ee.ria.riha.service.notifications.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Holds data for new issue email notification messages to RIHA assessors.
 */
@Getter
@Setter
public class NewIssueToAssessorsEmailNotification extends SimpleHtmlEmailNotification {

    private String infoSystemFullName;
    private String infoSystemShortName;
    private String issueTitle;
    private String baseUrl;

    @Builder
    public NewIssueToAssessorsEmailNotification(String from, String[] to, String subject, String[] cc, String[] bcc, String infoSystemFullName,
                                                String infoSystemShortName, String issueTitle, String baseUrl) {
        super(from, to, subject, cc, bcc);
        this.infoSystemFullName = infoSystemFullName;
        this.infoSystemShortName = infoSystemShortName;
        this.issueTitle = issueTitle;
        this.baseUrl = baseUrl;
    }
}
