package ee.ria.riha.web.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.databind.JsonNode;
import ee.ria.riha.domain.model.IssueType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Valentin Suhnjov
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
public class InfoSystemModel {

  private Long id;

  @JsonRawValue
  @JsonProperty("details")
  @Schema(type = "object", description = "Info system details")
  private JsonNode json;

  private IssueType lastPositiveApprovalRequestType;
  private Date lastPositiveApprovalRequestDate;
  private Date lastPositiveEstablishmentRequestDate;
  private Date lastPositiveTakeIntoUseRequestDate;
  private Date lastPositiveFinalizationRequestDate;
  private boolean hasUsedSystemTypeRelations;
}
