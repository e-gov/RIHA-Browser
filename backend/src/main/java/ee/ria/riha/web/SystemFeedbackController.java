package ee.ria.riha.web;

import ee.ria.riha.domain.model.*;
import ee.ria.riha.service.*;
import ee.ria.riha.web.model.*;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import javax.servlet.http.*;
import javax.validation.*;

import static ee.ria.riha.conf.ApplicationProperties.*;

@RestController
@RequestMapping(API_V1_PREFIX + "/system-feedback")
@Api("Feedback")
public class SystemFeedbackController {

    @Autowired
    SystemFeedbackService systemFeedbackService;

    @PostMapping
    @ApiOperation("Save user feedback")
    public ResponseEntity<Void> leaveFeedback(@RequestBody @Valid SystemFeedbackRequest systemFeedback, HttpServletRequest request) {

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

}
