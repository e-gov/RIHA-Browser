package ee.ria.riha.service.notification.handler;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.lang.Nullable;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import ee.ria.riha.service.notification.model.SimpleHtmlEmailNotification;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;

public abstract class IssueNotificationHandler<T extends SimpleHtmlEmailNotification> extends SimpleHtmlEmailNotificationHandler<T> {

	@Autowired
	protected MessageSource messageSource;

	@Autowired
	protected Configuration freeMarkerConfiguration;

	protected abstract String getTemplateName();
	protected abstract String getSubjectKey(T notification);
	protected abstract @Nullable Object[] getSubjectArgs(T notification);
	protected abstract Map<String, Object> createTemplateModel(T notification);

	@Override
	protected String getSubject(T notification) {
		return messageSource.getMessage(getSubjectKey(notification), getSubjectArgs(notification), Locale.getDefault());
	}

	@Override
	protected String getText(T notification) {
		String templateName = getTemplateName();
		try {
			return FreeMarkerTemplateUtils.processTemplateIntoString(
					freeMarkerConfiguration.getTemplate(templateName), createTemplateModel(notification));
		} catch (IOException | TemplateException e) {
			throw new NotificationHandlerException("Error generating notification message text template " + templateName, e);
		}
	}
}
