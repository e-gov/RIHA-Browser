package ee.ria.riha.service.util;

import ee.ria.riha.service.IssueService;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class DateUtilsTest {

    @Test
    public void getDecisionDeadline() throws ParseException {

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        LocalDate decisionDeadline = DateUtils.getDecisionDeadline(dateFormat.parse("28-01-2019"), IssueService.WORK_DAYS_UNTIL_DEADLINE);
        assertThat(decisionDeadline.format(dateTimeFormatter), is("25-02-2019"));

        decisionDeadline = DateUtils.getDecisionDeadline(dateFormat.parse("11-03-2019"), IssueService.WORK_DAYS_UNTIL_DEADLINE);
        assertThat(decisionDeadline.format(dateTimeFormatter), is("08-04-2019"));

    }
}