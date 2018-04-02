package Utility;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Static class for unified time formatting.
 *
 * @author ArcStone Development LLC
 * @version v1.0
 * @since v1.0
 */
public class SystemTime {

    /**
     * Access the current system time with the standard format.
     * <br> <strong>Format:</strong>
     * <br> [MMM dd, yyyy] - [hh:mm:ss aa]
     * <br> EX: [Jan 25, 2018] - [05:05:25 PM]
     * @return String - Current system time in the specified format.
     */
    public static String getTime() {
        DateFormat dateFormat = new SimpleDateFormat("[MMM dd, yyyy] - [hh:mm:ss aa] ");
        Date date = new Date();
        return dateFormat.format(date);
    }
}
