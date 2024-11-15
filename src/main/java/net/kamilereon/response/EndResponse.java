package net.kamilereon.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class EndResponse extends Response {
    private static final String name = "END";

    public int getScore() {
        return Integer.parseInt(this.headers.get("score"));
    }

    public EndResponse setScore(int score) {
        this.headers.put("score", String.valueOf(score));
        return this;
    }

    @Override
    public String method() {
        return name;
    }
}
