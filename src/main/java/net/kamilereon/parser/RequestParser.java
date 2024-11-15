package net.kamilereon.parser;

import net.kamilereon.Method;
import net.kamilereon.request.*;

public class RequestParser {
    public static Request parse(String request) {
        String[] parts = request.split("\n");
        String method = parts[0].toUpperCase();
        Method methodEnum = Method.valueOf(method);

        switch (methodEnum) {
            case START:
                StartRequest startRequest = new StartRequest();
                startRequest.loadFromMessage(request);
                return startRequest;
            case NEXT:
                NextRequest nextRequest = new NextRequest();
                nextRequest.loadFromMessage(request);
                return nextRequest;
            case ANSWER:
                AnswerRequest answerRequest = new AnswerRequest();
                answerRequest.loadFromMessage(request);
                return answerRequest;
            case END:
                EndRequest endRequest = new EndRequest();
                endRequest.loadFromMessage(request);
                return endRequest;
        }

        return null;
    }
}
