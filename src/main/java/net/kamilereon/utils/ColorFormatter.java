package net.kamilereon.utils;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;

/**
 * Custom formatter for logging with colors.
 */
public class ColorFormatter extends SimpleFormatter {
    public static String RESET = "\u001B[0m";
    public static String GREEN = "\u001B[32m";
    public static String WHITE = "\u001B[37m";
    public static String YELLOW = "\u001B[33m";
    public static String RED = "\u001B[31m";

    public String format(LogRecord logRecord) {
        String color;

        if (logRecord.getLevel() == Level.SEVERE) {
            color = RED;
        } else if (logRecord.getLevel() == Level.WARNING) {
            color = YELLOW;
        } else {
            color = GREEN;
        }

        // format log record with color
        // %1$tH:%1$tM:%1$tS.%1$tL - time
        // %4$-1s - log level
        // %5$s - message
        return String.format(WHITE + "%1$tH:%1$tM:%1$tS.%1$tL" + color + " [%4$-1s]" + WHITE + " %5$s %n" + RESET,
                logRecord.getMillis(), "", "", logRecord.getLevel().getLocalizedName(), logRecord.getMessage());
    }
}
