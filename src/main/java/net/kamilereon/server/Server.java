package net.kamilereon.server;

import net.kamilereon.utils.ColorFormatter;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Server class.
 * <p/>
 * Server is a class that listens for connections and creates a new thread for each connection.
 * It uses a thread pool to manage threads.
 */
public class Server {
    public static final Logger logger = Logger.getLogger(Server.class.getName());
    private static final int port = 6789;
    private ServerSocket serverSocket;
    private ExecutorService executorService;

    public static Server getInstance() {
        return new Server();
    }
    public void start() {
        configureLogging();
        try {
            load();
            logger.info("Server started on port " + port);
        } catch (Exception e) {
            logger.severe("Failed to start server.");
            return;
        }

        logger.info("Listening for connections...");

        while (true) {
            try {
                // accept connection and create a new thread
                Socket connection = serverSocket.accept();
                ServerThread serverThread = new ServerThread(connection, this);
                executorService.execute(serverThread);
            } catch (Exception e) {
                logger.severe("Failed to accept connection.");
            }
        }
    }

    private void configureLogging() {
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(Level.ALL);
        // set custom formatter
        consoleHandler.setFormatter(new ColorFormatter());
        logger.addHandler(consoleHandler);
        logger.setLevel(Level.ALL);
        logger.setUseParentHandlers(false);
    }

    private void load() throws Exception {
        serverSocket = new ServerSocket(port);
        executorService = Executors.newCachedThreadPool();
    }

    public static void main(String[] args) {
        Server server = Server.getInstance();
        server.start();
    }
}
