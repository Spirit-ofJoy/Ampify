package utility;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Time {

    private static final DateFormat dateFormat = new SimpleDateFormat("HH:mm");

    public static String getCurrentTimeStamp() {
        Date date = new Date();
        String s = "[" +dateFormat.format(date) +"]";
        return s;
    }


}
