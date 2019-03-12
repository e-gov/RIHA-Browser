package ee.ria.riha.service.util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.EnumSet;
import java.util.Set;
import java.util.stream.Collectors;

import ee.ria.riha.domain.model.NationalHoliday;
import ee.ria.riha.domain.model.NationalHolidays;

public class DateUtils {

	public static final Set<LocalDate> daysOffWork = NationalHolidays.get().stream()
			.filter(DateUtils::isDayOffFromWork)
			.map(NationalHoliday::getDate)
			.collect(Collectors.toSet());

	public static LocalDate getDecisionDeadline(Date from, int workDaysToAdd) {
		if (from == null) {
			return null;
		}

		LocalDate date = from.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		int dayCount = 0;
		while (dayCount <= workDaysToAdd) {
			date = date.plusDays(1);
			if (isWorkDay(date)) {
				dayCount++;
			}
		}
		return date;
	}

	public static boolean isWorkDay(LocalDate date) {
		return !isWeekend(date) && !isHoliday(date);
	}

	public static boolean isWeekend(LocalDate date) {
		return EnumSet.of(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY).contains(date.getDayOfWeek());
	}

	public static boolean isHoliday(LocalDate date) {
		return daysOffWork.contains(date);
	}

	private static boolean isDayOffFromWork(NationalHoliday holiday) {
		switch (holiday.getKind()) {
		case NATIONAL_HOLIDAY:
		case PUBLIC_HOLIDAY:
			return true;
		}
		return false;
	}
}
