package net.kamilereon.request;

public class NextRequest extends Request {
    private static final String name = "NEXT";

    @Override
    public String method() {
        return name;
    }
}
