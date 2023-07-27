package webserver.utils;

import java.text.SimpleDateFormat;

public class StringUtils {
//    public static final String NEW_LINE = System.getProperty("line.separator");
    public static final String NEW_LINE = "\r\n";

    public static final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm");

    public static String getKeyString(String line, int splitIndex) {
        return line.substring(0, splitIndex).trim();
    }

    public static String getValueString(String line, int splitIndex) {
        return line.substring(splitIndex+1).trim();
    }

}
