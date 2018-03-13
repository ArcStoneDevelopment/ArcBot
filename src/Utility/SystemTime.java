package Utility;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SystemTime {
    public static String getTime() {
        DateFormat dateFormat = new SimpleDateFormat("[MMM dd, yyyy] - [hh:mm:ss aa] ");
        Date date = new Date();
        return dateFormat.format(date);
    }
}
