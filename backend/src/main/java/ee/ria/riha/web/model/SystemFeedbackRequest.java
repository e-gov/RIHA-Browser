package ee.ria.riha.web.model;

import lombok.*;

import java.sql.*;
import javax.validation.constraints.*;

@Getter
@Setter
@NoArgsConstructor
public class SystemFeedbackRequest {

    @NotNull(message = "validation.feedback.invalidInput")
    private Integer grade;

    @Size(max = 4000, message = "validation.feedback.invalidInput")
    private String comment;

    @Setter(AccessLevel.NONE)
    private Integer NPS;

    private Timestamp timestamp = new Timestamp(System.currentTimeMillis());


    public void setGrade(Integer grade) {
        this.grade = grade;
        if (this.grade >= 9) {
            NPS = 100;
        } else if (this.grade <= 6) {
            NPS = -100;
        } else {
            NPS = 0;
        }
    }

}
