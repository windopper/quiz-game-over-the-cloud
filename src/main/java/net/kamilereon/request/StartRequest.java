package net.kamilereon.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StartRequest extends Request {
    private static final String name = "START";

    public int getQuizLimit() {
        return Integer.parseInt(this.headers.get("quizLimit"));
    }

    public StartRequest setQuizLimit(int quizLimit) {
        this.headers.put("quizLimit", String.valueOf(quizLimit));
        return this;
    }

    @Override
    public String method() {
        return name;
    }
}
