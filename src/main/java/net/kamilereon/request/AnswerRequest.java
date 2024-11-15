package net.kamilereon.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class AnswerRequest extends Request {
    private static final String name = "ANSWER";

    public String getQuestionId() {
        return this.headers.get("questionId");
    }

    public AnswerRequest setQuestionId(String questionId) {
        this.headers.put("questionId", questionId);
        return this;
    }

    public int getAnswer() {
        return Integer.parseInt(this.headers.get("answer"));
    }

    public AnswerRequest setAnswer(int answer) {
        this.headers.put("answer", String.valueOf(answer));
        return this;
    }

    @Override
    public String method() {
        return name;
    }
}
