package ee.ria.riha.domain.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties("kind_id")
public class NationalHoliday {

	private String title;
	private String notes;
	private LocalDate date;
	private HolidayType kind;
}
