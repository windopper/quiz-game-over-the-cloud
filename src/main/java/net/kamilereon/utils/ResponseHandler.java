package net.kamilereon.utils;

import net.kamilereon.response.Response;

/**
 * Utility class for handling responses.
 */
public class ResponseHandler {
    public static void handleErrorResponse(Response response) throws Exception {
        if (response.getStatus() == Response.Status.ERROR) {
            throw new Exception(response.getErrorMessage());
        }
    }
}
