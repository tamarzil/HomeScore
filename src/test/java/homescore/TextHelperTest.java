package homescore;

import org.joda.time.DateTime;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Tamar on 5/5/2017.
 */
public class TextHelperTest {

    @Test
    public void dayOfMonthToWords_minus3_returnsEmpty() {
        assertEquals("", TextHelper.dayOfMonthToWords(-3));
    }

    @Test
    public void dayOfMonthToWords_0_returnsEmpty() {
        assertEquals("", TextHelper.dayOfMonthToWords(0));
    }

    @Test
    public void dayOfMonthToWords_1_returnsFirst() {
        assertEquals("first", TextHelper.dayOfMonthToWords(1));
    }
    @Test
    public void dayOfMonthToWords_5_returnsFifth() {
        assertEquals("fifth", TextHelper.dayOfMonthToWords(5));
    }

    @Test
    public void dayOfMonthToWords_10_returnsTenth() {
        assertEquals("tenth", TextHelper.dayOfMonthToWords(10));
    }

    @Test
    public void dayOfMonthToWords_14_returnsFourteenth() {
        assertEquals("fourteenth", TextHelper.dayOfMonthToWords(14));
    }

    @Test
    public void dayOfMonthToWords_20_returnsTwentieth() {
        assertEquals("twentieth", TextHelper.dayOfMonthToWords(20));
    }

    @Test
    public void dayOfMonthToWords_22_returnsTwentySecond() {
        assertEquals("twenty second", TextHelper.dayOfMonthToWords(22));
    }

    @Test
    public void dayOfMonthToWords_30_returnsThirtieth() {
        assertEquals("thirtieth", TextHelper.dayOfMonthToWords(30));
    }

    @Test
    public void dayOfMonthToWords_31_returnsThirtyFirst() {
        assertEquals("thirty first", TextHelper.dayOfMonthToWords(31));
    }
}
