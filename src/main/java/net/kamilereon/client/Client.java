package net.kamilereon.client;

import net.kamilereon.utils.ServerConfigLoader;
import net.kamilereon.parser.ResponseParser;
import net.kamilereon.request.*;
import net.kamilereon.response.*;
import net.kamilereon.utils.MessageUtils;
import net.kamilereon.utils.OutputFormatter;
import net.kamilereon.utils.ResponseHandler;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * Client class for quiz game.
 */
public class Client {
    private Socket clientSocket;
    private OutputStreamWriter outToServer;
    private BufferedReader inFromServer;
    BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));

    public static Client createInstance() {
        return new Client();
    }

    public void start() {
        try {
            establishConnection();

            System.out.println("Loading Quiz...\n");

            // Load quiz
            StartResponse startResponse = loadQuiz(5);

            // handle error response
            // if error, throw exception
            ResponseHandler.handleErrorResponse(startResponse);

            // store question text, options, question id
            // this variable will be reused in the loop
            String questionText = startResponse.getQuestionText();
            String[] options = startResponse.getOptions();
            String questionId = startResponse.getQuestionId();
            System.out.println(OutputFormatter.formatQuiz(questionText, options));

            while (true) {
                // Answer the question
                int answer = selectAnswer();

                AnswerResponse answerResponse = answerQuestion(
                        answer,
                        questionId
                );
                ResponseHandler.handleErrorResponse(answerResponse);

                // Check if answer is correct
                if (!answerResponse.getIsCorrect()) {
                    System.out.println("\nIncorrect!");
                    System.out.println(OutputFormatter.formatCorrectAnswer(answerResponse.getCorrectAnswer(), options));
                } else {
                    System.out.println("\nCorrect!");
                }

                // Check if it's the last question
                if (answerResponse.getIsLastQuestion()) {
                    System.out.println("Quiz finished.");
                    break;
                }

                // Get next question
                NextResponse nextResponse = getNextQuestion();
                ResponseHandler.handleErrorResponse(nextResponse);

                questionText = nextResponse.getQuestionText();
                options = nextResponse.getOptions();
                questionId = nextResponse.getQuestionId();

                System.out.println(OutputFormatter.formatQuiz(questionText, options));
            }

            // End quiz
            // Print final score
            EndResponse endResponse = endQuiz();
            ResponseHandler.handleErrorResponse(endResponse);
            String finalScore = OutputFormatter.formatFinalScore(endResponse.getScore(), 5);
            System.out.println(finalScore);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            closeConnection();
        }
    }

    private Map<String, String> loadServerInfo(String path) {
        Map<String, String> serverInfo = new HashMap<>();
        try {
            serverInfo = ServerConfigLoader.loadServerInfo(path);
            System.out.println("Loaded server_info.dat.");
        } catch (Exception e) {
            System.out.println("Failed to load server_info.dat. Defaulting to localhost:6789");
        }
        return serverInfo;
    }

    private void establishConnection() {
        // load server info
        Map<String, String> serverInfo = loadServerInfo("src/main/resources/server_info.dat");
        String serverIP = serverInfo.getOrDefault("SERVER_IP", "localhost");
        int nPort = Integer.parseInt(serverInfo.getOrDefault("PORT", "6789"));

        // establish connection
        try {
            System.out.println("Connecting to server... serverIP: " + serverIP + ", port: " + nPort);
            clientSocket = new Socket(serverIP, nPort);
            outToServer = new OutputStreamWriter(clientSocket.getOutputStream(), StandardCharsets.UTF_8);
            inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8));
            System.out.println("Connected to server.");
        } catch (Exception e) {
            System.out.println("Failed to establish connection to server.");
        }
        System.out.println();
    }

    public StartResponse loadQuiz(int quizLimit) {
        StartRequest startRequest = new StartRequest();
        startRequest.setQuizLimit(quizLimit);
        return (StartResponse) sendRequest(startRequest);
    }

    public int selectAnswer() throws IOException {
        System.out.println("Enter your answer (1-4):");
        int answer;
        while (true) {
            String input = inFromUser.readLine();
            // validate answer 1 ~ 4
            if (input == null || input.length() != 1 || input.charAt(0) < '1' || input.charAt(0) > '4') {
                System.out.println("Invalid answer. Please enter a number between 1 and 4.");
            }
            else {
                answer = Integer.parseInt(input);
                break;
            }
        }

        return answer;
    }

    public AnswerResponse answerQuestion(int answer, String questionId) {
        AnswerRequest answerRequest = new AnswerRequest();
        answerRequest.setAnswer(answer - 1); // convert to 0-based index
        answerRequest.setQuestionId(questionId);
        return (AnswerResponse) sendRequest(answerRequest);
    }

    public NextResponse getNextQuestion() {
        NextRequest nextRequest = new NextRequest();
        return (NextResponse) sendRequest(nextRequest);
    }

    public EndResponse endQuiz() {
        EndRequest endRequest = new EndRequest();
        return (EndResponse) sendRequest(endRequest);
    }

    private void closeConnection() {
        try {
            if (outToServer != null) {
                outToServer.close();
            }
            if (inFromServer != null) {
                inFromServer.close();
            }
            if (clientSocket != null) {
                clientSocket.close();
            }
        } catch (Exception e) {
            System.out.println("Failed to close connection to server.");
        }
    }

    private Response sendRequest(Request request) {
        try {
            outToServer.write(request.getMessage() + "\n");
            outToServer.flush();
            String responseMessage = MessageUtils.readMessageWithDelimiter(inFromServer, "\n");
            return ResponseParser.parse(responseMessage);
        } catch (Exception e) {
            System.out.println("Failed to send request to server.");
        }

        return null;
    }

    public static void main(String[] args) {
        Client client = Client.createInstance();
        client.start();
    }
}
