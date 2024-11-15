package net.kamilereon.parser;

import net.kamilereon.Method;
import net.kamilereon.response.*;

public class ResponseParser {
    public static Response parse(String response) {
        String[] parts = response.split("\n");
        String method = parts[0].toUpperCase();
        Method methodEnum = Method.valueOf(method);
        // slice parts[1] to parts[parts.length - 1]

        switch (methodEnum) {
            case START:
                StartResponse startResponse = new StartResponse();
                startResponse.loadFromMessage(response);
                return startResponse;
            case NEXT:
                NextResponse nextResponse = new NextResponse();
                nextResponse.loadFromMessage(response);
                return nextResponse;
            case ANSWER:
                AnswerResponse answerResponse = new AnswerResponse();
                answerResponse.loadFromMessage(response);
                return answerResponse;
            case END:
                EndResponse endResponse = new EndResponse();
                endResponse.loadFromMessage(response);
                return endResponse;
            default:
                System.out.println("Unknown method: " + method);
                return null;
        }
    }
}
