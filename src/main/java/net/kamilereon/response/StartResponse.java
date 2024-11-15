package net.kamilereon.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class StartResponse extends Response {
    private static final String name = "START";

    public String getQuestionText() {
        return this.headers.get("questionText");
    }

    public StartResponse setQuestionText(String questionText) {
        this.headers.put("questionText", questionText);
        return this;
    }

    public String[] getOptions() {
        String options = this.headers.get("options");
        if (options == null) {
            return null;
        }
        return options.split(",");
    }

    public StartResponse setOptions(String[] options) {
        this.headers.put("options", String.join(",", options));
        return this;
    }

    public String getQuestionId() {
        return this.headers.get("questionId");
    }

    public StartResponse setQuestionId(String questionId) {
        this.headers.put("questionId", questionId);
        return this;
    }

    public int getTotalQuestions() {
        String totalQuestions = this.headers.get("totalQuestions");
        if (totalQuestions == null) {
            return 0;
        }
        return Integer.parseInt(totalQuestions);
    }

    public StartResponse setTotalQuestions(int totalQuestions) {
        this.headers.put("totalQuestions", String.valueOf(totalQuestions));
        return this;
    }

    @Override
    public String method() {
        return name;
    }
}
