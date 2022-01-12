package ee.ria.riha.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.github.fge.jsonschema.core.report.ProcessingMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class JsonSecurityDetailsValidationService {

    private static final String ISKE_STANDARD = "ISKE";
    private static final String EITS_STANDARD = "EITS";
    private static final String SECURITY_DETAILS_VALIDATION_ERROR = "securityDetailsValidationError";

    private static final String SECURITY_DETAILS_KEY = "security";
    private static final String SECURITY_STANDARD_KEY = "standard";
    private static final String SECURITY_CLASS_KEY = "class";
    private static final String SECURITY_LEVEL_KEY = "level";
    private static final String LATEST_AUDIT_DATE_KEY = "latest_audit_date";
    private static final String LATEST_AUDIT_RESOLUTION_KEY = "latest_audit_resolution";

    private static final String SECURITY_CLASS_IS_NULL_KEY = "validation.system.json.securityClass.isNullWhenStandardIsISKEOrEITS";
    private static final String SECURITY_CLASS_IS_NOT_NULL_KEY = "validation.system.json.securityClass.isNotNullWhenStandardIsNotISKEOrEITS";
    private static final String SECURITY_LEVEL_IS_NULL_KEY = "validation.system.json.securityLevel.isNullWhenStandardIsISKEOrEITS";
    private static final String SECURITY_LEVEL_IS_NOT_NULL_KEY = "validation.system.json.securityLevel.isNotNullWhenStandardIsNotISKEOrEITS";
    private static final String LEVEL_DOES_NOT_MATCH_CLASS_KEY = "validation.system.json.securityLevel.doesNotMatchSecurityClass";
    private static final String LATEST_AUDIT_RESOLUTION_IS_NULL_KEY = "validation.system.json.latestAuditResolution.isNullWhenLatestAuditDateIsNotNull";
    private static final String LATEST_AUDIT_RESOLUTION_IS_NOT_NULL_KEY = "validation.system.json.latestAuditResolution.isNotNullWhenLatestAuditDateIsNull";
    private static final String LATEST_AUDIT_RESOLUTION_VALUE_IS_NOT_ALLOWED_KEY = "validation.system.json.latestAuditResolution.valueIsNotAllowed";

    private static final int AVAILABILITY_LEVEL_POSITION = 1;
    private static final int INTEGRITY_LEVEL_POSITION = 3;
    private static final int CONFIDENTIALITY_LEVEL_POSITION = 5;
    private static final String HIGH_SECURITY_LEVEL = "H";
    private static final String MEDIUM_SECURITY_LEVEL = "M";
    private static final String LOW_SECURITY_LEVEL = "L";
    private static final int HIGH_SECURITY_LEVEL_CLASS_VALUE = 3;
    private static final int MEDIUM_SECURITY_LEVEL_CLASS_VALUE = 2;
    private static final int LOW_SECURITY_LEVEL_CLASS_VALUE = 1;
    private static final int ALTERNATIVE_LOW_SECURITY_LEVEL_CLASS_VALUE = 0;

    private MessageSource messageSource;

    public List<ProcessingMessage> validate(JsonNode jsonNode) {
        return Stream.of(validateSecurityClass(jsonNode), validateSecurityLevel(jsonNode), validateLatestAuditResolution(jsonNode))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public boolean isNecessaryToValidateSecurityDetails(JsonNode jsonNode) {
        JsonNode securityDetails = jsonNode.get(SECURITY_DETAILS_KEY);
        return securityDetails != null && securityDetails.getClass() != NullNode.class;
    }

    public ProcessingMessage validateSecurityClass(JsonNode jsonNode) {
        ProcessingMessage validationResult = null;

        String securityStandard = getSecurityDetail(jsonNode, SECURITY_STANDARD_KEY);
        String securityClass = getSecurityDetail(jsonNode, SECURITY_CLASS_KEY);

        if ((ISKE_STANDARD.equals(securityStandard) || EITS_STANDARD.equals(securityStandard)) && securityClass == null) {
            validationResult = createProcessingMessage(SECURITY_CLASS_IS_NULL_KEY);
        } else if (!ISKE_STANDARD.equals(securityStandard) && !EITS_STANDARD.equals(securityStandard) && securityClass != null) {
            validationResult = createProcessingMessage(SECURITY_CLASS_IS_NOT_NULL_KEY);
        }

        return validationResult;
    }

    public ProcessingMessage validateSecurityLevel(JsonNode jsonNode) {
        ProcessingMessage validationResult = null;

        String securityStandard = getSecurityDetail(jsonNode, SECURITY_STANDARD_KEY);
        String securityLevel = getSecurityDetail(jsonNode, SECURITY_LEVEL_KEY);
        String securityClass = getSecurityDetail(jsonNode, SECURITY_CLASS_KEY);

        if ((ISKE_STANDARD.equals(securityStandard) || EITS_STANDARD.equals(securityStandard)) && securityLevel == null) {
            validationResult = createProcessingMessage(SECURITY_LEVEL_IS_NULL_KEY);
        } else if (!ISKE_STANDARD.equals(securityStandard) && !EITS_STANDARD.equals(securityStandard) && securityLevel != null) {
            validationResult = createProcessingMessage(SECURITY_LEVEL_IS_NOT_NULL_KEY);
        } else if (!isSecurityLevelMatchingSecurityClass(securityLevel, securityClass)) {
            validationResult = createProcessingMessage(LEVEL_DOES_NOT_MATCH_CLASS_KEY);
        }

        return validationResult;
    }

    public ProcessingMessage validateLatestAuditResolution(JsonNode jsonNode) {
        ProcessingMessage validationResult = null;

        String latestAuditDate = getSecurityDetail(jsonNode, LATEST_AUDIT_DATE_KEY);
        String latestAuditResolution = getSecurityDetail(jsonNode, LATEST_AUDIT_RESOLUTION_KEY);

        if (latestAuditDate == null && latestAuditResolution != null) {
            validationResult = createProcessingMessage(LATEST_AUDIT_RESOLUTION_IS_NOT_NULL_KEY);
        } else if (latestAuditDate != null && latestAuditResolution == null) {
            validationResult = createProcessingMessage(LATEST_AUDIT_RESOLUTION_IS_NULL_KEY);
        } else if (latestAuditDate != null && !isValidLatestAuditResolutionStatus(latestAuditResolution)) {
            validationResult = createProcessingMessage(LATEST_AUDIT_RESOLUTION_VALUE_IS_NOT_ALLOWED_KEY);
        }

        return validationResult;
    }

    private String getSecurityDetail(JsonNode jsonNode, String securityDetailPath) {
        return jsonNode.path(SECURITY_DETAILS_KEY).path(securityDetailPath).asText(null);
    }

    private String getErrorMessage(String messageKey) {
        return messageSource.getMessage(messageKey, null, Locale.getDefault());
    }

    private ProcessingMessage createProcessingMessage(String messageKey) {
        ProcessingMessage processingMessage = new ProcessingMessage();
        processingMessage.put(SECURITY_DETAILS_VALIDATION_ERROR, getErrorMessage(messageKey));
        return processingMessage;
    }

    private boolean isSecurityLevelMatchingSecurityClass(String securityLevel, String securityClass) {
        if (securityLevel == null && securityClass == null) {
            return true;
        } else if (securityLevel == null || securityClass == null) {
            return false;
        }

        int availabilityLevel = Character.getNumericValue(securityClass.charAt(AVAILABILITY_LEVEL_POSITION));
        int integrityLevel = Character.getNumericValue(securityClass.charAt(INTEGRITY_LEVEL_POSITION));
        int confidentialityLevel = Character.getNumericValue(securityClass.charAt(CONFIDENTIALITY_LEVEL_POSITION));

        int highestSecurityLevel = Stream.of(availabilityLevel, integrityLevel, confidentialityLevel)
                .max(Integer::compare)
                .orElse(Integer.MIN_VALUE);

        switch(securityLevel) {
            case HIGH_SECURITY_LEVEL: return highestSecurityLevel == HIGH_SECURITY_LEVEL_CLASS_VALUE;
            case MEDIUM_SECURITY_LEVEL: return highestSecurityLevel == MEDIUM_SECURITY_LEVEL_CLASS_VALUE;
            case LOW_SECURITY_LEVEL: return highestSecurityLevel == LOW_SECURITY_LEVEL_CLASS_VALUE
                    || highestSecurityLevel == ALTERNATIVE_LOW_SECURITY_LEVEL_CLASS_VALUE;
            default: return false;
        }
    }

    private boolean isValidLatestAuditResolutionStatus(String latestAuditResolution) {
        try {
            LatestAuditResolutionType.valueOf(latestAuditResolution);
            return true;
        } catch(IllegalArgumentException e) {
            return false;
        }
    }

    private enum LatestAuditResolutionType {

        PASSED_WITHOUT_REMARKS,
        PASSED_WITH_REMARKS,
        DID_NOT_PASS
    }

    @Autowired
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }
}
