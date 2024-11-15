package net.kamilereon.request;

public class EndRequest extends Request {
    private static final String name = "END";

    @Override
    public String method() {
        return name;
    }
}