package ee.ria.riha.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author tristan on 01-Mar-19.
 */
public enum HolidayType {

	@JsonProperty("Riigipüha")
	NATIONAL_HOLIDAY	(1),

	@JsonProperty("Rahvuspüha")
	PUBLIC_HOLIDAY		(2),

	@JsonProperty("Riiklik tähtpäev")
	NATIONAL_ANNIVERSARY(3),

	@JsonProperty("Lühendatud tööpäev")
	SHORTENED_WORKDAY 	(4);

	private final int id;

	HolidayType(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}
}
