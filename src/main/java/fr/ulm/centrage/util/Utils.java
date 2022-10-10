package fr.ulm.centrage.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class Utils {

    public static float parseFloat(String v) {
        try {
            return Float.parseFloat(v);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static int parseInt(String v) {
        try {
            return Integer.parseInt(v);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static String getDate() {
        Date date = new Date();
        DateFormat format = SimpleDateFormat.getDateInstance();
        return format.format(date);
    }
}
