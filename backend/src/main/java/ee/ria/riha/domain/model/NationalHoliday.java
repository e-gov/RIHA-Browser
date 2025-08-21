package ee.ria.riha.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDate;
import lombok.Data;

@Data
@JsonIgnoreProperties("kind_id")
public class NationalHoliday {

  private String title;
  private String notes;
  private LocalDate date;
  private HolidayType kind;
}
