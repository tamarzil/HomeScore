package homescore;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Months;
import org.joda.time.Years;
import org.joda.time.format.DateTimeFormat;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by Tamar on 5/4/2017.
 */
public class DateTimeHelperTest {

    @Test
    public void getTimePhrase_futureDate_returnsToday() {
        DateTime date = DateTime.now().plusDays(3);
        String result = DateTimeHelper.getTimePhrase(date);
        assertEquals("today", result);
    }

    @Test
    public void getTimePhrase_2hoursAgoToday_returnsToday() {
        DateTime testNow = DateTime.parse("2017-05-04 22:00:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
        DateTime date = testNow.minusHours(2);
        String result = DateTimeHelper.getTimePhrase(date, testNow);
        assertEquals("today", result);
    }

    @Test
    public void getTimePhrase_2hoursAgoYesterday_returnsToday() {
        DateTime testNow = DateTime.parse("2017-05-04 01:00:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
        DateTime date = testNow.minusHours(2);
        String result = DateTimeHelper.getTimePhrase(date, testNow);
        assertEquals("yesterday", result);
    }

    @Test
    public void getTimePhrase_yesterday_returnsYesterday() {
        DateTime testNow = DateTime.parse("2017-05-04 22:00:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
        DateTime date = testNow.minusDays(1);
        String result = DateTimeHelper.getTimePhrase(date, testNow);
        assertEquals("yesterday", result);
    }

    @Test
    public void getTimePhrase_34hoursAgoDayBeforeYesterday_returnsLastTuesday() {
        DateTime testNow = DateTime.parse("2017-05-04 06:00:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
        DateTime date = DateTime.parse("2017-05-02 20:00:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
        String result = DateTimeHelper.getTimePhrase(date, testNow);
        assertEquals("last Tuesday", result);
    }

    @Test
    public void getTimePhrase_5daysAgo_returnsLastSaturday() {
        DateTime testNow = DateTime.parse("2017-05-04 22:00:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
        DateTime date = testNow.minusDays(5);
        String result = DateTimeHelper.getTimePhrase(date, testNow);
        assertEquals("last Saturday", result);
    }

    @Test
    public void getTimePhrase_7daysAgo_returnsLastThursday() {
        DateTime testNow = DateTime.parse("2017-05-04 22:00:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
        DateTime date = testNow.minusDays(7);
        String result = DateTimeHelper.getTimePhrase(date, testNow);
        assertEquals("last Thursday", result);
    }

    @Test
    public void getTimePhrase_20daysAgo_returnsOnAprilFourteenth() {
        DateTime testNow = DateTime.parse("2017-05-04 22:00:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
        DateTime date = testNow.minusDays(20);
        String result = DateTimeHelper.getTimePhrase(date, testNow);
        assertEquals("on April fourteenth", result);
    }

    @Test
    public void parseDateRange_null_returnsSoFar() {
        DateTime testNow = DateTime.parse("2017-05-04 22:00:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
        String input = null;
        DateRange result = DateTimeHelper.parseDateRange(input, testNow);
        assertNotNull(result);
        assertEquals("so far", result.getWords());
        assertEquals(17, Years.yearsIn(result.getInterval()).getYears());
    }

    @Test
    public void parseDateRange_today_returnsToday() {
        DateTime testNow = DateTime.parse("2017-05-04 22:00:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
        String input = "2017-05-04";
        DateRange result = DateTimeHelper.parseDateRange(input, testNow);
        assertNotNull(result);
        assertEquals("today", result.getWords());
        assertEquals(0, Days.daysIn(result.getInterval()).getDays());
    }

    @Test
    public void parseDateRange_yesterday_returnsYesterday() {
        DateTime testNow = DateTime.parse("2017-05-04 22:00:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
        String input = "2017-05-03";
        DateRange result = DateTimeHelper.parseDateRange(input, testNow);
        assertNotNull(result);
        assertEquals("yesterday", result.getWords());
        assertEquals(0, Days.daysIn(result.getInterval()).getDays());
    }

    @Test
    public void parseDateRange_someOtherDay_returnsSoFar() {
        DateTime testNow = DateTime.parse("2017-05-04 22:00:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
        String input = "2017-05-01";
        DateRange result = DateTimeHelper.parseDateRange(input, testNow);
        assertNotNull(result);
        assertEquals("so far", result.getWords());
        assertEquals(17, Years.yearsIn(result.getInterval()).getYears());
    }

    @Test
    public void parseDateRange_thisWeek_returnsThisWeek() {
        DateTime testNow = DateTime.parse("2017-05-04 22:00:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
        String input = "2017-W18";  // the week that starts on 2017-05-01
        DateRange result = DateTimeHelper.parseDateRange(input, testNow);
        assertNotNull(result);
        assertEquals("this week", result.getWords());
        assertEquals(3, Days.daysIn(result.getInterval()).getDays());
    }

    @Test
    public void parseDateRange_nextWeek_returnsSoFar() {
        DateTime testNow = DateTime.parse("2017-05-04 22:00:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
        String input = "2017-W19";
        DateRange result = DateTimeHelper.parseDateRange(input, testNow);
        assertNotNull(result);
        assertEquals("so far", result.getWords());
        assertEquals(17, Years.yearsIn(result.getInterval()).getYears());
    }

    @Test
    public void parseDateRange_lastWeek_returnsSoFar() { // for now, this is the supported behaviour
        DateTime testNow = DateTime.parse("2017-05-04 22:00:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
        String input = "2017-W17";
        DateRange result = DateTimeHelper.parseDateRange(input, testNow);
        assertNotNull(result);
        assertEquals("so far", result.getWords());
        assertEquals(17, Years.yearsIn(result.getInterval()).getYears());
    }

    @Test
    public void parseDateRange_thisMonth_returnsThisMonth() {
        DateTime testNow = DateTime.parse("2017-05-04 22:00:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
        String input = "2017-05";
        DateRange result = DateTimeHelper.parseDateRange(input, testNow);
        assertNotNull(result);
        assertEquals("this month", result.getWords());
        assertEquals(0, Months.monthsIn(result.getInterval()).getMonths());
    }

    @Test
    public void parseDateRange_nextMonth_returnsSoFar() {
        DateTime testNow = DateTime.parse("2017-05-04 22:00:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
        String input = "2017-06";
        DateRange result = DateTimeHelper.parseDateRange(input, testNow);
        assertNotNull(result);
        assertEquals("so far", result.getWords());
        assertEquals(17, Years.yearsIn(result.getInterval()).getYears());
    }

    @Test
    public void parseDateRange_lastMonth_returnsSoFar() { // for now, this is the supported behaviour
        DateTime testNow = DateTime.parse("2017-05-04 22:00:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
        String input = "2017-04";
        DateRange result = DateTimeHelper.parseDateRange(input, testNow);
        assertNotNull(result);
        assertEquals("so far", result.getWords());
        assertEquals(17, Years.yearsIn(result.getInterval()).getYears());
    }

    @Test
    public void parseDateRange_thisYear_returnsThisYear() {
        DateTime testNow = DateTime.parse("2017-05-04 22:00:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
        String input = "2017";
        DateRange result = DateTimeHelper.parseDateRange(input, testNow);
        assertNotNull(result);
        assertEquals("this year", result.getWords());
        assertEquals(0, Years.yearsIn(result.getInterval()).getYears());
    }

    @Test
    public void parseDateRange_nextYear_returnsSoFar() {
        DateTime testNow = DateTime.parse("2017-05-04 22:00:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
        String input = "2018";
        DateRange result = DateTimeHelper.parseDateRange(input, testNow);
        assertNotNull(result);
        assertEquals("so far", result.getWords());
        assertEquals(17, Years.yearsIn(result.getInterval()).getYears());
    }

    @Test
    public void parseDateRange_lastYear_returnsSoFar() { // for now, this is the supported behaviour
        DateTime testNow = DateTime.parse("2017-05-04 22:00:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
        String input = "2016";
        DateRange result = DateTimeHelper.parseDateRange(input, testNow);
        assertNotNull(result);
        assertEquals("so far", result.getWords());
        assertEquals(17, Years.yearsIn(result.getInterval()).getYears());
    }
}
