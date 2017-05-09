package homescore;

/**
 * Created by Tamar on 5/5/2017.
 */
public class TextHelper {
    public static String dayOfMonthToWords(int dayOfMonth) {
        if (dayOfMonth < 1 || dayOfMonth > 31)
            return "";

        String result = "";
        if (dayOfMonth < 20) {
            result = numNames[dayOfMonth];
        } else if (dayOfMonth % 10 == 0) {
            int tens = dayOfMonth / 10;
            result = tensNamesOrdinal[tens];
        } else {
            int tens = dayOfMonth / 10;
            int ones = dayOfMonth % 10;
            result = String.format("%s %s", tensNames[tens], numNames[ones]);
        }

        return result;
    }

    private static final String[] tensNames = {
            "",
            "ten",
            "twenty",
            "thirty"
    };

    private static final String[] tensNamesOrdinal = {
            "",
            "tenth",
            "twentieth",
            "thirtieth"
    };

    private static final String[] numNames = {
            "",
            "first",
            "second",
            "third",
            "fourth",
            "fifth",
            "sixth",
            "seventh",
            "eighth",
            "ninth",
            "tenth",
            "eleventh",
            "twelfth",
            "thirteenth",
            "fourteenth",
            "fifteenth",
            "sixteenth",
            "seventeenth",
            "eighteenth",
            "nineteenth"
    };
}
