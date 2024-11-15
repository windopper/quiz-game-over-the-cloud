package net.kamilereon.utils;

import java.util.List;

/**
 * Utility class for formatting output.
 */
public class OutputFormatter {
    public static String formatQuiz(String question, String[] options) {
        StringBuilder formatted = new StringBuilder();
        formatted.append(question).append("\n");
        for (int i = 0; i < options.length; i++) {
            formatted.append(i + 1).append(". ").append(options[i]).append("\n");
        }
        return formatted.toString();
    }

    public static String formatFinalScore(int score, int totalQuestions) {
        return String.format("\nYour final score is %d out of %d.\n", score, totalQuestions);
    }

    public static String formatCorrectAnswer(int correctAnswerIndex, String[] options) {
        return String.format("Correct answer is %s.\n", options[correctAnswerIndex]);
    }
}
