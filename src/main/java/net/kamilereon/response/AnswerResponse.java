package net.kamilereon.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Setter
@Getter
public class AnswerResponse extends Response {
    private static final String name = "ANSWER";

    public boolean getIsCorrect() {
        return Boolean.parseBoolean(this.headers.get("isCorrect"));
    }

    public AnswerResponse setIsCorrect(boolean isCorrect) {
        this.headers.put("isCorrect", String.valueOf(isCorrect));
        return this;
    }

    public int getCorrectAnswer() {
        return Integer.parseInt(this.headers.get("correctAnswer"));
    }

    public AnswerResponse setCorrectAnswer(int correctAnswer) {
        this.headers.put("correctAnswer", String.valueOf(correctAnswer));
        return this;
    }

    public String getExplanation() {
        return this.headers.get("explanation");
    }

    public AnswerResponse setExplanation(String explanation) {
        this.headers.put("explanation", explanation);
        return this;
    }

    public int getCurrentScore() {
        return Integer.parseInt(this.headers.get("currentScore"));
    }

    public AnswerResponse setCurrentScore(int currentScore) {
        this.headers.put("currentScore", String.valueOf(currentScore));
        return this;
    }

    public boolean getIsLastQuestion() {
        return Boolean.parseBoolean(this.headers.get("isLastQuestion"));
    }

    public AnswerResponse setIsLastQuestion(boolean isLastQuestion) {
        this.headers.put("isLastQuestion", String.valueOf(isLastQuestion));
        return this;
    }

    @Override
    public String method() {
        return name;
    }
}
