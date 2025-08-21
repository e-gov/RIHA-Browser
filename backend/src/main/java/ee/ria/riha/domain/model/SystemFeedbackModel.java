package ee.ria.riha.domain.model;

import com.fasterxml.jackson.annotation.*;
import java.util.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SystemFeedbackModel {

  private int rating;

  private int nps;

  private String comment;

  @JsonProperty("feedback_time")
  private Date feedbackTime;

  @JsonProperty("user_agent")
  private String userAgent;

  @JsonProperty("user_ip")
  private String userIp;
}
