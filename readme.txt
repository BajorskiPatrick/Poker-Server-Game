# Poker Game Project - README

## Overview
This project is a server-client implementation of a five-card draw poker game, designed for multiplayer interactions using Java. It consists of the following main components:

1. **Server**: Handles game logic, player connections, and communication.
2. **Client**: Allows players to connect to the server, send moves, and interact with the game.
3. **Gameplay**: Manages the game state, player actions, card dealing, and game progression.

## Rules of the Game
The implemented poker game follows the rules of five-card draw poker:

1. **Objective**: Players aim to create the best poker hand using five cards.
2. **Game Flow**:
   - **Join Phase**: Players join the game and place an ante.
   - **First Betting Round**: Players bet, raise, call, or fold.
   - **Card Exchange**: Players can exchange up to three cards.
   - **Second Betting Round**: Another round of betting occurs.
   - **Showdown**: Players reveal their hands, and the winner(s) are determined based on standard poker hand rankings.

## How to Run

### Prerequisites
- Java Development Kit (JDK) 17 or later
- Maven build tool

### Steps to Run
1. Clone the repository and navigate to the project root.
2. Build the project using Maven:
   ```bash
   mvn clean install
   ```
3. Start the server:
   ```bash
   java -cp poker-server/target/poker-server-1.0-jar-with-dependencies.jar
   ```
4. Start a client:
   ```bash
   java -cp poker-client/target/poker-client-1.0-jar-with-dependencies.jar
   ```
   Use multiple terminal windows to connect multiple clients to the server.

## Communication Protocol

The communication protocol between the server and client is text-based and consists of specific commands. Below are the supported commands and their descriptions:

### Commands Sent by the Client

1. **JOIN**:
   - **Description**: Allows a player to join an active game.
   - **Format**: `JOIN`
   - **Response**: `Dołączono do gry! ID: [gameID], ID gracza: [playerID]` if successful, or an error message.

2. **EXCHANGE**:
   - **Description**: Requests a card exchange during the exchange phase.
   - **Format**: `EXCHANGE card1,card2,...`
   - **Response**: `Ruch wykonany prawidłowo!` or an error message if invalid.

3. **RAISE**:
   - **Description**: Places a raise during the betting round.
   - **Format**: `RAISE amount`
   - **Response**: `Ruch wykonany prawidłowo!` or an error message.

4. **CALL**:
   - **Description**: Matches the highest bet.
   - **Format**: `CALL`
   - **Response**: `Ruch wykonany prawidłowo!`.

5. **FOLD**:
   - **Description**: Exits the current round.
   - **Format**: `FOLD`
   - **Response**: `Ruch wykonany prawidłowo!`.

6. **STATUS**:
   - **Description**: Requests the current game status.
   - **Format**: `STATUS`
   - **Response**: Provides the current game status and player information.

7. **EXIT**:
   - **Description**: Disconnects from the game.
   - **Format**: `EXIT`
   - **Response**: `Do zobaczenia!` and disconnects the client.

### Commands Sent by the Server

1. **Welcome Message**:
   - **Description**: Sent upon a successful client connection.
   - **Message**: `Witamy w serwerze Pokera!`

2. **Available Moves**:
   - **Description**: Lists the moves available to the player at the current phase.
   - **Message**: `Możliwe ruchy: [MOVE1, MOVE2, ...]`

3. **Game Updates**:
   - **Description**: Updates the player on game progression, including round changes, turn notifications, or errors.
   - **Examples**:
     - `Gra rozpoczyna się!`
     - `Twój ruch!`

4. **Game Results**:
   - **Description**: Announces the winners and their winnings.
   - **Message**: `Gra zakończyła się! Zwycięzcy: [PlayerID1, PlayerID2]. Wygrana: [amount].`

## Key Features

- **Scalable Server**: Uses non-blocking IO with `Selector` for handling multiple clients.
- **Game Logic**: Fully implemented poker rules with phases and hand evaluation.
- **Custom Commands**: Expandable communication protocol.

## Additional Notes

- The server enforces a minimum of two players and a maximum of four players per game.
- Error handling ensures that invalid commands are rejected with descriptive messages.
- All monetary amounts are handled as integers to simplify calculations.

## Contact
For issues or improvements, feel free to contribute to the repository or contact the project maintainer.

