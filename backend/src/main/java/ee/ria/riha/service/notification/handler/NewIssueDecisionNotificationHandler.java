package ee.ria.riha.service.notification.handler;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import ee.ria.riha.domain.model.IssueResolutionType;
import ee.ria.riha.service.notification.model.EmailNotificationDataModel;
import ee.ria.riha.service.notification.model.NewIssueDecisionEmailNotification;

import static ee.ria.riha.domain.model.IssueResolutionType.DISMISSED;
import static ee.ria.riha.domain.model.IssueResolutionType.NEGATIVE;
import static ee.ria.riha.domain.model.IssueResolutionType.POSITIVE;

@Component
public class NewIssueDecisionNotificationHandler extends IssueNotificationHandler<NewIssueDecisionEmailNotification> {

	private static final Map<IssueResolutionType, String> DECISION_MESSAGE_CODES = Maps.newEnumMap(ImmutableMap.of(
			POSITIVE, "notifications.newIssueDecision.positive",
			NEGATIVE, "notifications.newIssueDecision.negative",
			DISMISSED, "notifications.newIssueDecision.dismissed"
	));

	@Override
	protected String getTemplateName() {
		return "new-issue-decision-notification-template.ftl";
	}

	@Override
	protected String getSubjectKey(NewIssueDecisionEmailNotification model) {
		return "notifications.newIssueDecision.subject";
	}

	@Override
	protected @Nullable Object[] getSubjectArgs(NewIssueDecisionEmailNotification model) {
		return new String[] { model.getInfoSystemShortName() };
	}

	@Override
	public boolean supports(EmailNotificationDataModel model) {
		return model.getClass() == NewIssueDecisionEmailNotification.class;
	}

	@Override
	protected Map<String, Object> createTemplateModel(NewIssueDecisionEmailNotification notification) {
		String decisionCode = DECISION_MESSAGE_CODES.get(notification.getDecision());
		Map<String, Object> model = new HashMap<>();
		model.put("baseUrl", notification.getBaseUrl());
		model.put("name", notification.getInfoSystemFullName());
		model.put("shortName", notification.getInfoSystemShortName());
		model.put("issueId", notification.getIssueId());
		model.put("issueTitle", notification.getIssueTitle());
		model.put("author", notification.getAuthorName());
		model.put("comment", notification.getComment());
		model.put("decision", messageSource.getMessage(decisionCode, null, Locale.getDefault()));
		return model;
	}
}
