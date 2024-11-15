# Quiz Game Over The Cloud
This is a simple quiz game that can be played over the network. The game consists of multiple-choice questions and the player has to answer the questions to score points. The game is implemented using a client-server architecture where the server maintains the game state and the client interacts with the server to play the game.

> This project was carried out as an assignment for the Computer Network & Programming class.

# Installation
1. Clone the repository
```bash
git clone 
cd quiz-game-over-the-cloud 
```

2. Run `./gradlew serverJar clientJar` to build the server and client jar files. The jar file will be created in the root directory of the project.

# Run with gradle task
> In this case, you don't need to build the jar files.
## Running the server
1. Run `./gradlew runServer` to start the server

## Running the client
1. Run `./gradlew runClient` to start the client

# Run with jar files
> You need to build the jar files before running the server and client.
> 
> `./gradlew serverJar clientJar` to build the jar files.
## Running the server
1. Run `java -jar quiz-server.jar` to start the server

## Running the client
1. Run `java -jar quiz-client.jar` to start the client

# Protocol
This protocol defines the communication between client and server for implementing a quiz game over network communication. The protocol follows a request-response pattern and maintains game state on the server side.

## START
Initiates a new quiz game session.
### request
```
START
quizLimit=<number> // Optional: Number of questions (1~10)
```
### response
```
START
status= // OK | ERROR
questionText=<string> // First question text (if status=OK)
options=<array> // Array of 4 questions (if status=OK)
questionId=<string> // Unique identifier for the question (if status=OK)
totalQuestions=<number> // Total number of questions in the quiz (if status=OK)
errorMessage=<string> // Error description (if status!=OK)
```

## NEXT
Requests the next question in the quiz.
### request
```
NEXT
```

### response
```
NEXT
status= // OK | ERROR
questionText=<string> // First question text (if status=OK)
options=<array> // Array of 4 questions (if status=OK)
questionId=<string> // Unique identifier for the question (if status=OK)
errorMessage= // Error description (if status!=OK)
```

## ANSWER
Submits an answer for the current question.
### request
```
ANSWER
questionId=<string> // Required: Unique identifier for the question
answer=<number> // Required: Answer selected by the user (0~3)
```

### response
```
ANSWER
status= // OK | ERROR
isCorrect= // Whether the answer was correct (if status=OK)
correctAnswer=<number> // The correct answer (0~3) (if status=OK)
explanation=<string>   // Explanation of the correct answer (if status=OK)
currentScore=<number>  // Current total score (if status=OK)
isLastQuestion=<boolean> // Whether the current question is the last question
errorMessage=<string>  // Error description (if status!=OK)
```

## END
End the quiz game.

### request
```
END
```

### response
```
END
status= // OK | ERROR
score= // Total score (if status=OK)
errorMessage= // Error description (if status!=OK)
```

