package net.kamilereon.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for loading server configuration.
 */
public class ServerConfigLoader {
    public static Map<String, String> loadServerInfo(String fileName) throws Exception {
        Map<String, String> serverInfo = new HashMap<>();
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String line;
        // read file line by line
        // if split line has 2 parts, add it to the map
        while ((line = reader.readLine()) != null) {
            if (line.isEmpty()) continue;
            String[] parts = line.split("=");
            if (parts.length == 2) {
                serverInfo.put(parts[0], parts[1]);
            }
        }
        return serverInfo;
    }
}
