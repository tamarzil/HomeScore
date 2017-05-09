package homescore;

import org.joda.time.Interval;

/**
 * Created by Tamar on 5/5/2017.
 */
public class DateRange {

    private String words;
    private Interval interval;
    private boolean parseError;

    public DateRange() {}

    public DateRange(String words, Interval interval, boolean parseError) {
        this.words = words;
        this.interval = interval;
        this.parseError = parseError;
    }

    public DateRange(String words, Interval interval) {
        this.words = words;
        this.interval = interval;
        this.parseError = false;
    }

    public Interval getInterval() {
        return interval;
    }

    public String getWords() {
        return words;
    }

    public boolean isParseError() {
        return parseError;
    }
}
