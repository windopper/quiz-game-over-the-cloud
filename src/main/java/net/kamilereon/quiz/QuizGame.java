package net.kamilereon.quiz;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class QuizGame {
    // The current score of the user
    private int currentScore = 0;
    // The number of questions that the user can solve
    private int quizLimit;
    // The number of questions that have been processed
    private int processedQuestionCount = 0;
    // The number of correct answers
    private int correctAnswerCount = 0;
    // Whether the user has already answered the current question
    private boolean alreadyAnsweredCurrentQuestion = false;
    // The list of questions that have been answered
    private List<QuizDB.QuestionAnswer> questions = new ArrayList<>();

    public QuizGame(int quizLimit) {
        this.quizLimit = quizLimit;
    }

    /**
     * Get the next question
     * @return the next question
     */
    public QuizDB.QuestionAnswer getNextQuestion() {
        QuizDB.QuestionAnswer questionAnswer = QuizDB.getRandomQuestion(questions.stream().map(QuizDB.QuestionAnswer::getId).toList());
        questions.add(questionAnswer);
        alreadyAnsweredCurrentQuestion = false;
        return questionAnswer;
    }

    /**
     * Select the answer of the last question
     * @param selectedAnswerIndex the index of the selected answer
     * @return true if the answer is correct
     */
    public boolean selectAnswer(int selectedAnswerIndex) {
        QuizDB.QuestionAnswer questionAnswer = questions.get(questions.size() - 1);
        processedQuestionCount++;
        alreadyAnsweredCurrentQuestion = true;
        if (questionAnswer.isCorrectAnswer(selectedAnswerIndex)) {
            correctAnswerCount++;
            currentScore += questionAnswer.getScore();
            return true;
        }
        return false;
    }

    /**
     * Get the correct answer of the last question
     * @return the index of the correct answer
     *        -1 if there is no question
     */
    public int getCorrectAnswer() {
        if (questions.isEmpty()) {
            return -1;
        }
        return questions.get(questions.size() - 1).getCorrectAnswerIndex();
    }

    public boolean isLastQuestion() {
        return questions.size() == quizLimit;
    }

    public boolean isSolveAllQuiz() {
        return processedQuestionCount == quizLimit;
    }
}
