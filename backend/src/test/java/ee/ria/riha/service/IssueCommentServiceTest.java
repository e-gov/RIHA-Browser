package ee.ria.riha.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ee.ria.riha.TestUtils;
import ee.ria.riha.domain.CommentRepository;
import ee.ria.riha.domain.model.Comment;
import ee.ria.riha.rules.CleanAuthentication;
import ee.ria.riha.web.model.IssueCommentModel;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author Valentin Suhnjov
 */
@MockitoSettings(strictness = Strictness.WARN)
@ExtendWith({MockitoExtension.class, CleanAuthentication.class})
public class IssueCommentServiceTest {

  private static final long EXISTING_ISSUE_ID = 15503L;
  private static final Long CREATED_COMMENT_ENTITY_ID = 42L;

  private final Authentication authenticationToken = TestUtils.getOAuth2LoginToken(null, null);

  @Mock private CommentRepository commentRepository;

  @Mock private NotificationService notificationService;

  @InjectMocks private IssueCommentService issueCommentService;

  @BeforeEach
  public void setUp() {
    // Reset authentication
    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    TestUtils.setActiveOrganisation(
        authenticationToken, JaneAuthenticationTokenBuilder.ORGANIZATION_CODE);

    when(commentRepository.add(any(Comment.class)))
        .thenReturn(Collections.singletonList(CREATED_COMMENT_ENTITY_ID));
  }

  @Test
  public void populatesAuthorAndOrganizationFromUserDetailsDuringCommentCreation() {
    issueCommentService.createIssueComment(
        EXISTING_ISSUE_ID, IssueCommentModel.builder().comment("").build());

    ArgumentCaptor<Comment> commentArgumentCaptor = ArgumentCaptor.forClass(Comment.class);
    verify(commentRepository).add(commentArgumentCaptor.capture());

    Comment comment = commentArgumentCaptor.getValue();
    assertThat(comment.getAuthor_name(), is(equalTo("Jane Doe")));
    assertThat(comment.getAuthor_personal_code(), is(equalTo("EE40102031234")));
    assertThat(comment.getOrganization_name(), is(equalTo("Acme org")));
    assertThat(comment.getOrganization_code(), is(equalTo("555010203")));
  }

  @Test
  public void throwsExceptionWhenActiveOrganizationIsNotSetDuringCommentCreation() {
    assertThrows(
        IllegalBrowserStateException.class,
        () -> {
          TestUtils.setActiveOrganisation(authenticationToken, null);

          issueCommentService.createIssueComment(
              EXISTING_ISSUE_ID, IssueCommentModel.builder().comment("comment").build());
        });
  }
}
