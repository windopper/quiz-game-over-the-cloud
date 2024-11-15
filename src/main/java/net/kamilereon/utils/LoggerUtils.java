package net.kamilereon.utils;

import net.kamilereon.Method;

import java.util.logging.Level;
import java.util.logging.Logger;

public class LoggerUtils {
    public static void logProtocol(Logger logger, Level level, Method method, String ip, int port) {
        logger.log(level, ip + ":" + port + " / " + method);
    }
}
