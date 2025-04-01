# Poker Game Project

## Overview

This project is a server-client implementation of a five-card draw poker game, designed for multiplayer interactions using Java. It consists of the following main components:

1. **Server** – Handles game logic, player connections, and communication.
2. **Client** – Allows players to connect to the server, send moves, and interact with the game.
3. **Gameplay** – Manages the game state, player actions, card dealing, and game progression.

---

## Rules of the Game

The implemented poker game follows the rules of five-card draw poker:

1. **Objective**: Players aim to create the best poker hand using five cards.
2. **Game Flow**:
    - **Join Phase** – Players join the game and place an ante.
    - **First Betting Round** – Players bet, raise, call, or fold.
    - **Card Exchange** – Players can exchange up to three cards.
    - **Second Betting Round** – Another round of betting occurs.
    - **Showdown** – Players reveal their hands, and the winner(s) are determined based on standard poker hand rankings.

---

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

---

## Communication Protocol

The communication protocol between the server and client is text-based and consists of specific commands.

### Commands Sent by the Client

- **NEW GAME**
  - Format: `NEW_GAME numberOfPlayers, initialBet`
  - Response: `Nowa gra została utworzona!`

- **JOIN**
    - Format: `JOIN`
    - Response: `Dołączono do gry! ID: [gameID], ID gracza: [playerID]`

- **EXCHANGE**
    - Format: `EXCHANGE card1,card2,...`
    - Response: `Ruch wykonany prawidłowo!`

- **RAISE**
    - Format: `RAISE amount`
    - Response: `Ruch wykonany prawidłowo!`

- **CALL**
    - Format: `CALL`
    - Response: `Ruch wykonany prawidłowo!`

- **FOLD**
    - Format: `FOLD`
    - Response: `Ruch wykonany prawidłowo!`

- **STATUS**
    - Format: `STATUS`
    - Response: Current game status and player information.

- **EXIT**
    - Format: `EXIT`
    - Response: `Do zobaczenia!`

### Commands Sent by the Server

- **Welcome Message**
    - Message: `Witamy w serwerze Pokera!`

- **Available Moves**
    - Message: `Możliwe ruchy: [MOVE1, MOVE2, ...]`

- **Game Updates**
    - Examples:
        - `Gra rozpoczyna się!`
        - `Twój ruch!`

- **Game Results**
    - Message: `Gra zakończyła się! Zwycięzcy: [PlayerID1, PlayerID2]. Wygrana: [amount].`

---

## Key Features

- **Scalable Server**: Uses non-blocking IO with `Selector` for handling multiple clients.
- **Game Logic**: Fully implemented poker rules with phases and hand evaluation.
- **Custom Commands**: Expandable communication protocol.

---

## Additional Notes

- The server supports 2 to 4 players per game.
- Invalid commands return detailed error messages.
- All monetary values are managed as integers.

---

## Contact

For issues or contributions, feel free to open a pull request or contact the project maintainer.