package net.kamilereon.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class NextResponse extends Response {
    private static final String name = "NEXT";

    public String getQuestionText() {
        return this.headers.get("questionText");
    }

    public NextResponse setQuestionText(String questionText) {
        this.headers.put("questionText", questionText);
        return this;
    }

    public String[] getOptions() {
        return this.headers.get("options").split(",");
    }

    public NextResponse setOptions(String[] options) {
        this.headers.put("options", String.join(",", options));
        return this;
    }

    public String getQuestionId() {
        return this.headers.get("questionId");
    }

    public NextResponse setQuestionId(String questionId) {
        this.headers.put("questionId", questionId);
        return this;
    }

    @Override
    public String method() {
        return name;
    }
}
