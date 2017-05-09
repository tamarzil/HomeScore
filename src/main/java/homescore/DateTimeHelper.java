package homescore;


import org.joda.time.*;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Created by Tamar on 5/3/2017.
 */
public class DateTimeHelper {

    public static String getTimePhrase(DateTime datetime, DateTime nowDateTime) {

        DateTime dateStartOfDay = datetime.withTimeAtStartOfDay();
        DateTime nowStartOfDay = nowDateTime.withTimeAtStartOfDay();
        int daysBetween = Days.daysBetween(dateStartOfDay, nowStartOfDay).getDays();

        if (daysBetween < 0)
            return "today";
        else if (daysBetween == 0)
            return "today";
        else if (daysBetween == 1)
            return "yesterday";
        else if (daysBetween <= 7)
            return String.format("last %s", datetime.dayOfWeek().getAsText(Locale.ENGLISH));
        else {
            return String.format("on %s %s", datetime.monthOfYear().getAsText(), TextHelper.dayOfMonthToWords(datetime.getDayOfMonth()));
            // TODO: return date in SSML format so alexa says "March 2nd", no year
        }
    }

    public static String getTimePhrase(DateTime datetime) {
        return getTimePhrase(datetime, DateTime.now());
    }

    public static DateRange parseDateRange(String value) {
        return parseDateRange(value, DateTime.now());
    }

    public static DateRange parseDateRange(String value, DateTime now) {

        // TODO: add support for ranges that don't end today - last month / last week / etc.
        // for now only "this week / month / year" + "today / yesterday" is supported

        if (value == null)
            return getDefaultRange(now);

        DateTime endTime = now;
        DateTime startTime;
        String words;
        if (Pattern.matches("^\\d{4}$", value)) {   // year
            int year = Integer.parseInt(value);
            startTime = new DateTime(year, 1, 1, 0, 0);
            int yearsBetween = Years.yearsBetween(startTime, now.withTimeAtStartOfDay()).getYears();
            if (yearsBetween != 0 || startTime.isAfter(now)) // another year - we don't support
                return getDefaultRangeWithError(now);
            else
                words = "this year";
        } else if (Pattern.matches("^\\d{4}-\\d{2}$", value)) { // month
            String strWithDay = String.format("%s%S", value, "-01");
            startTime = DateTime.parse(strWithDay, DateTimeFormat.forPattern("yyyy-MM-dd"));
            int monthsBetween = Months.monthsBetween(startTime, now.withTimeAtStartOfDay()).getMonths();
            if (monthsBetween != 0 || startTime.isAfter(now)) // another month - we don't support
                return getDefaultRangeWithError(now);
            else
                words = "this month";
        } else if (Pattern.matches("^\\d{4}-W\\d{2}$", value)) {    // week
            String strWithDay = String.format("%s%S", value, "-1");
            startTime = ISODateTimeFormat.weekyearWeekDay().parseDateTime(strWithDay);
            int daysBetween = Days.daysBetween(startTime, now.withTimeAtStartOfDay()).getDays();
            if (daysBetween < 0 || daysBetween > 7) // another week - we don't support
                return getDefaultRangeWithError(now);
            else
                words = "this week";
        } else if (Pattern.matches("^\\d{4}-\\d{2}-\\d{2}$", value)) {  // day
            startTime = DateTime.parse(value, DateTimeFormat.forPattern("yyyy-MM-dd"));
            int daysBetween = Days.daysBetween(startTime, now.withTimeAtStartOfDay()).getDays();
            if (daysBetween == 0)
                words = "today";
            else if (daysBetween == 1) {
                words = "yesterday";
                endTime = startTime.withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59).withMillisOfSecond(999);
            }
            else
                return getDefaultRangeWithError(now);
        } else {
            return getDefaultRangeWithError(now);
        }

        return new DateRange(words, new Interval(startTime, endTime));
    }

    private static DateRange getDefaultRange(DateTime now) {
        return new DateRange("so far", new Interval(new DateTime(2000, 1, 1, 0, 0), now));
    }

    private static DateRange getDefaultRangeWithError(DateTime now) {
        return new DateRange("so far", new Interval(new DateTime(2000, 1, 1, 0, 0), now), true);
    }
}
