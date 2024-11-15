package net.kamilereon.server;

import net.kamilereon.Method;
import net.kamilereon.parser.RequestParser;
import net.kamilereon.quiz.QuizDB;
import net.kamilereon.quiz.QuizGame;
import net.kamilereon.request.*;
import net.kamilereon.response.*;
import net.kamilereon.utils.LoggerUtils;
import net.kamilereon.utils.MessageUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.logging.Level;

/**
 * Server thread class.
 * <p/>
 * Server thread is a class that processes client requests.
 * It reads messages from client, processes them and sends responses.
 */
public class ServerThread extends Thread {
    private final Socket socket;
    private final Server server;
    private final String ip;
    private final int port;

    private QuizGame quizGame;
    private BufferedReader inFromClient;
    private OutputStreamWriter outToClient;

    public ServerThread(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
        this.ip = socket.getInetAddress().getHostAddress();
        this.port = socket.getPort();
    }

    @Override
    public void run() {
        try {
            Server.logger.info("Client connected: " + ip + ":" + port);
            inFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            outToClient = new OutputStreamWriter(socket.getOutputStream());

            while (true) {
                String message = MessageUtils.readMessageWithDelimiter(inFromClient, "\n");
                if (message.isEmpty()) break;
                Request request = RequestParser.parse(message);

                if (request instanceof StartRequest startRequest) {
                    if (isQuizInitialized()) {
                        sendResponse(getErrorResponse("Quiz is already initialized.", StartResponse.class));
                    } else {
                        sendResponse(getStartResponse(startRequest));
                    }
                } else if (request instanceof NextRequest nextRequest) {
                    if (!isQuizInitialized()) {
                        sendResponse(getErrorResponse("Quiz is not initialized.", NextResponse.class));
                    } else if (isSolveAllQuiz()) {
                        sendResponse(getErrorResponse("Quiz is finished.", NextResponse.class));
                    } else {
                        sendResponse(getNextResponse());
                    }
                } else if (request instanceof AnswerRequest answerRequest) {
                    if (!isQuizInitialized()) {
                        sendResponse(getErrorResponse("Quiz is not initialized.", AnswerResponse.class));
                    } else if (isSolveAllQuiz()) {
                        sendResponse(getErrorResponse("Quiz is finished.", AnswerResponse.class));
                    } else if (isAlreadyAnsweredCurrentQuestion()) {
                        sendResponse(getErrorResponse("Question is already answered.", AnswerResponse.class));
                    } else {
                        sendResponse(getAnswerResponse(answerRequest));
                    }
                } else if (request instanceof EndRequest endRequest) {
                    if (!isQuizInitialized()) {
                        sendResponse(getErrorResponse("Quiz is not initialized.", EndResponse.class));
                    } else if (!isQuizLastQuestion() || !isSolveAllQuiz()) {
                        sendResponse(getErrorResponse("Quiz is not finished yet.", EndResponse.class));
                    } else {
                        sendResponse(getEndResponse());
                    }
                }
            }
        } catch (Exception e) {
            Server.logger.severe("Failed to process client request.");
        } finally {
            try {
                inFromClient.close();
                outToClient.close();
                socket.close();
            } catch (Exception e) {
                Server.logger.severe("Failed to close connection.");
            }
        }

        Server.logger.info("Client disconnected: " + ip + ":" + port);
    }

    private EndResponse getEndResponse() {
        EndResponse endResponse = new EndResponse();
        endResponse.setStatus(EndResponse.Status.OK);
        endResponse.setScore(quizGame.getCurrentScore());
        return endResponse;
    }

    private AnswerResponse getAnswerResponse(AnswerRequest answerRequest) {
        int answerIndex = answerRequest.getAnswer();
        boolean isCorrect = quizGame.selectAnswer(answerIndex);
        AnswerResponse answerResponse = new AnswerResponse();

        answerResponse.setStatus(AnswerResponse.Status.OK);
        answerResponse.setIsCorrect(isCorrect);
        answerResponse.setCorrectAnswer(quizGame.getCorrectAnswer());
        answerResponse.setExplanation("");
        answerResponse.setIsLastQuestion(quizGame.isLastQuestion());
        return answerResponse;
    }

    private NextResponse getNextResponse() {
        NextResponse nextResponse = new NextResponse();

        QuizDB.QuestionAnswer nextQuestion = quizGame.getNextQuestion();
        nextResponse.setStatus(NextResponse.Status.OK);
        nextResponse.setQuestionText(nextQuestion.getQuestion());
        nextResponse.setOptions(nextQuestion.getOptions());
        nextResponse.setQuestionId(nextQuestion.getId());
        return nextResponse;
    }

    private StartResponse getStartResponse(StartRequest startRequest) {
        int quizLimit = startRequest.getQuizLimit();
        quizGame = new QuizGame(quizLimit);
        QuizDB.QuestionAnswer question = quizGame.getNextQuestion();

        StartResponse startResponse = new StartResponse();
        startResponse.setStatus(StartResponse.Status.OK);
        startResponse.setQuestionText(question.getQuestion());
        startResponse.setOptions(question.getOptions());
        startResponse.setQuestionId(question.getId());
        startResponse.setTotalQuestions(quizGame.getQuizLimit());
        return startResponse;
    }

    private void sendResponse(Response response) {
        try {
            if (response == null) {
                throw new Exception("Response is null.");
            }
            String message = response.getMessage();
            outToClient.write(message + "\n");
            outToClient.flush();

            boolean isError = response.getStatus() == Response.Status.ERROR;
            LoggerUtils.logProtocol(Server.logger,
                    isError ? Level.SEVERE : Level.INFO, Method.valueOf(response.method()), ip, port);
        } catch (Exception e) {
            Server.logger.severe("Failed to send response." + e.getMessage());
        }
    }

    private boolean isQuizInitialized() {
        return quizGame != null;
    }

    private boolean isSolveAllQuiz() {
        return quizGame.isSolveAllQuiz();
    }

    private boolean isAlreadyAnsweredCurrentQuestion() {
        return quizGame.isAlreadyAnsweredCurrentQuestion();
    }

    private boolean isQuizLastQuestion() {
        return quizGame.isLastQuestion();
    }

    private <T extends Response> Response getErrorResponse(String message, Class<T> clazz) {
        try {
            T response = clazz.getDeclaredConstructor().newInstance();
            response.setStatus(Response.Status.ERROR);
            response.setErrorMessage(message);
            return response;
        } catch (Exception e) {
            Server.logger.severe("Failed to create error response.");
            return null;
        }
    }
}
