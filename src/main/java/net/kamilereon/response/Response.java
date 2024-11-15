package net.kamilereon.response;

import net.kamilereon.Payload;

/**
 * Abstract class for response.
 * <p/>
 * Response is a message that is sent from server to client.
 */
public abstract class Response extends Payload {
    public Status getStatus() {
        String status = this.headers.get("status");
        if (status == null) {
            return null;
        }
        return Status.valueOf(status);
    }

    public Response setStatus(Status status) {
        this.headers.put("status", status.name());
        return this;
    }

    public String getErrorMessage() {
        return this.headers.get("errorMessage");
    }

    public Response setErrorMessage(String errorMessage) {
        this.headers.put("errorMessage", errorMessage);
        return this;
    }

    public enum Status {
        OK, ERROR
    }
}
