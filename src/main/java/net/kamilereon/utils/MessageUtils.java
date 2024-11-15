package net.kamilereon.utils;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Utility class for reading messages from BufferedReader.
 */
public class MessageUtils {
    // Read message from BufferedReader until empty line
    public static String readMessageWithDelimiter(BufferedReader reader, String delimiter) throws IOException {
        StringBuilder message = new StringBuilder();
        while (true) {
            String line = reader.readLine();
            if (line == null || line.isEmpty() || line.isBlank()) {
                break;
            }
            message.append(line).append(delimiter);
        }

        return message.toString();
    }
}
