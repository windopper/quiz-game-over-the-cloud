package net.kamilereon;

import java.util.HashMap;
import java.util.Map;

/**
 * Abstract class for payload.
 */
public abstract class Payload {
    protected Map<String, String> headers = new HashMap<>();

    public abstract String method();

    public String getMessage() {
        StringBuilder message = new StringBuilder();
        message.append(method()).append("\n");
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            message.append(entry.getKey()).append("=").append(entry.getValue()).append("\n");
        }
        return message.toString();
    }

    public void loadFromMessage(String message) {
        String[] parts = message.split("\n");
        // parts[0] is method
        for (int i = 1; i < parts.length; i++) {
            String[] keyValue = parts[i].split("=");
            if (keyValue.length == 2) {
                headers.put(keyValue[0], keyValue[1]);
            }
        }
    }
}
