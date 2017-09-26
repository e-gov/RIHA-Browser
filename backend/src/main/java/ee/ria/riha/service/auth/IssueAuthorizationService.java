package ee.ria.riha.service.auth;

import ee.ria.riha.domain.model.InfoSystem;
import ee.ria.riha.domain.model.Issue;
import ee.ria.riha.service.InfoSystemService;
import ee.ria.riha.service.IssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author Valentin Suhnjov
 */
@Service
public class IssueAuthorizationService {

    @Autowired
    private IssueService issueService;

    @Autowired
    private InfoSystemService infoSystemService;

    @Autowired
    private InfoSystemAuthorizationService infoSystemAuthorizationService;

    public boolean isIssueOwner(Long issueId) {
        Issue issue = issueService.getIssueById(issueId);
        UUID infoSystemUuid = issue.getInfoSystemUuid();

        InfoSystem infoSystem = infoSystemService.get(infoSystemUuid);

        return infoSystemAuthorizationService.isOwner(infoSystem);
    }

}
