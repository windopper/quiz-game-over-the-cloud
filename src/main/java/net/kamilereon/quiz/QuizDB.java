package net.kamilereon.quiz;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

public class QuizDB {
    private static final List<QuestionAnswer> questions = List.of(
            new QuestionAnswer(
                    "Which planet is known as the Red Planet?",
                    new String[]{"Venus", "Mars", "Jupiter", "Saturn"},
                    1
            ),
            new QuestionAnswer(
                    "Who painted the Mona Lisa?",
                    new String[]{"Vincent van Gogh", "Pablo Picasso", "Leonardo da Vinci", "Michelangelo"},
                    2
            ),
            new QuestionAnswer(
                    "What is the chemical symbol for gold?",
                    new String[]{"Ag", "Fe", "Cu", "Au"},
                    3
            ),
            new QuestionAnswer(
                    "Which famous scientist developed the theory of relativity?",
                    new String[]{"Albert Einstein", "Isaac Newton", "Niels Bohr", "Marie Curie"},
                    0
            ),
            new QuestionAnswer(
                    "What is the largest ocean on Earth?",
                    new String[]{"Atlantic Ocean", "Indian Ocean", "Arctic Ocean", "Pacific Ocean"},
                    3
            ),
            new QuestionAnswer(
                    "Who wrote 'Romeo and Juliet'?",
                    new String[]{"Charles Dickens", "William Shakespeare", "Jane Austen", "Mark Twain"},
                    1
            ),
            new QuestionAnswer(
                    "What is the primary language spoken in Brazil?",
                    new String[]{"Spanish", "English", "Portuguese", "French"},
                    2
            ),
            new QuestionAnswer(
                    "Which element is the most abundant in Earth's atmosphere?",
                    new String[]{"Oxygen", "Carbon", "Nitrogen", "Hydrogen"},
                    2
            ),
            new QuestionAnswer(
                    "What is the capital city of Japan?",
                    new String[]{"Seoul", "Beijing", "Tokyo", "Bangkok"},
                    2
            ),
            new QuestionAnswer(
                    "Who invented the telephone?",
                    new String[]{"Thomas Edison", "Alexander Graham Bell", "Nikola Tesla", "George Eastman"},
                    1
            )
    );

    // Get a random question that is not in the excludeIds list
    public static QuestionAnswer getRandomQuestion(List<String> excludeIds) {
        List<QuestionAnswer> availableQuestions = questions.stream()
                .filter(question -> !excludeIds.contains(question.getId()))
                .toList();

        return availableQuestions.get((int) (Math.random() * availableQuestions.size()));
    }

    @Getter
    @Setter
    public static class QuestionAnswer {
        private final String id = UUID.randomUUID().toString();
        private int score = 1;
        private String question;
        private String[] options;
        private int correctAnswerIndex;

        public QuestionAnswer(String question, String[] options, int correctAnswerIndex) {
            this.question = question;
            this.options = options;
            this.correctAnswerIndex = correctAnswerIndex;
        }

        public boolean isCorrectAnswer(int selectedAnswerIndex) {
            return selectedAnswerIndex == correctAnswerIndex;
        }
    }
}