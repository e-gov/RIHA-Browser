package ee.ria.riha.web;

import com.fasterxml.jackson.annotation.JsonAlias;
import ee.ria.riha.conf.ApplicationProperties;
import ee.ria.riha.domain.model.SystemFeedbackModel;
import ee.ria.riha.service.SystemFeedbackService;
import ee.ria.riha.web.model.SystemFeedbackRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static ee.ria.riha.conf.ApplicationProperties.API_V1_PREFIX;

@RestController
@RequestMapping(API_V1_PREFIX + "/system-feedback")
@Api("Feedback")
@RequiredArgsConstructor
@Slf4j
public class SystemFeedbackController {

    public static final String RECAPTCHA_VERIFICATION_ENDPOINT = "https://www.google.com/recaptcha/api/siteverify?secret={secret}&remoteip={remoteip}&response={response}";

    private final SystemFeedbackService systemFeedbackService;
    private final ApplicationProperties applicationProperties;
    private final RestTemplate restTemplate;

    @PostMapping
    @ApiOperation("Save user feedback")
    public ResponseEntity<Void> leaveFeedback(@RequestBody @Valid SystemFeedbackRequest systemFeedback, HttpServletRequest request) {

        if (applicationProperties.getFeedbackRecaptcha().isEnabled()) {

            Map<String, String> verificationRequestParams = new HashMap<>();
            verificationRequestParams.put("secret", applicationProperties.getFeedbackRecaptcha().getServerKey());
            verificationRequestParams.put("remoteip", request.getRemoteAddr());
            verificationRequestParams.put("response", systemFeedback.getRecaptchaToken());

            ResponseEntity<RecaptchaVerificationResponse> responseEntity = restTemplate
                    .getForEntity(
                            RECAPTCHA_VERIFICATION_ENDPOINT,
                            RecaptchaVerificationResponse.class,
                            verificationRequestParams);

            if (!responseEntity.hasBody()) {
                log.error("Verification response is missing, cannot proceed");
                return reportRecaptchaResultsToClient(applicationProperties.getFeedbackRecaptcha().isReportErrorsToClient());
            }

            RecaptchaVerificationResponse recaptchaVerificationResponse = responseEntity.getBody();

            if (!recaptchaVerificationResponse.isSuccess()) {
                log.error("There are problems with recaptcha integration. Verification endpoint responsed with: {}", recaptchaVerificationResponse);
                return reportRecaptchaResultsToClient(applicationProperties.getFeedbackRecaptcha().isReportErrorsToClient());
            }


            if (recaptchaVerificationResponse.getScore() < applicationProperties.getFeedbackRecaptcha().getPassingScore()) {
                log.debug("recaptcha score did not pass for feedback: {}, recaptcha response: {}", systemFeedback, recaptchaVerificationResponse);
                return reportRecaptchaResultsToClient(applicationProperties.getFeedbackRecaptcha().isReportErrorsToClient());
            }


            log.debug("recaptcha response for feedback {} is {}", systemFeedback, recaptchaVerificationResponse);
        }

        return sendFeedbackToBackend(systemFeedback, request);
    }

    private ResponseEntity<Void> reportRecaptchaResultsToClient(boolean reportAsError) {
        return reportAsError ? ResponseEntity.badRequest().build() : ResponseEntity.ok().build();
    }

    private ResponseEntity<Void> sendFeedbackToBackend(@RequestBody @Valid SystemFeedbackRequest systemFeedback, HttpServletRequest request) {
        SystemFeedbackModel modelToSend = SystemFeedbackModel
                .builder()
                .rating(systemFeedback.getGrade())
                .nps(systemFeedback.getNPS())
                .comment(systemFeedback.getComment())
                .feedbackTime(new Date())
                .userAgent(request.getHeader("User-Agent"))
                .userIp(request.getRemoteAddr())
                .build();

        try {
            systemFeedbackService.sendFeedback(modelToSend);
            return ResponseEntity.ok().build();
        } catch (Exception fatal) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Data
    @NoArgsConstructor
    @ToString
    private static class RecaptchaVerificationResponse {
        private boolean success;
        @JsonAlias("challenge_ts")
        private String challengeTimestamp;
        private String hostname;
        @JsonAlias("error-codes")
        private String[] errorCodes;
        private float score;
    }

}
