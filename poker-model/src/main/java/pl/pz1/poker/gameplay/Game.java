package pl.pz1.poker.gameplay;

import pl.pz1.poker.evaluator.HandEvaluator;
import pl.pz1.poker.moves.*;
import pl.pz1.poker.util.parser.Moves;
import pl.pz1.poker.util.parser.Token;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * The Game class represents a poker game instance. It manages the game's state,
 * players, deck, and moves. It provides methods to process player moves, update game parameters,
 * deal cards, and determine the results of the game.
 */
public class Game {
    /**
     * Counter for tracking the number of games created.
     */
    private static int gameCounter = 0;

    /**
     * List of players participating in the game.
     */
    private final List<Player> players = new ArrayList<>();

    /**
     * Map of available moves in the game.
     */
    private final Map<String, Move> moves = new HashMap<>();

    /**
     * The deck of cards used in the game.
     */
    private List<Card> deck = new ArrayList<>();

    /**
     * Hand evaluator for determining the winners.
     */
    private final HandEvaluator evaluator = new HandEvaluator();

    /**
     * Unique identifier for the game.
     */
    private int gameID = 0;

    /**
     * The number of players required to start the game.
     */
    private int playerCount = 0;

    /**
     * The ante amount required for players to join the game.
     */
    private int ante = 0;

    /**
     * The ID of the player whose turn it is.
     */
    private int whoseTurnID = 0;

    /**
     * List of player IDs who have won the game.
     */
    private List<Integer> winnersID = new ArrayList<>();

    /**
     * Total winnings in the game.
     */
    private int win = 0;

    /**
     * The highest bet placed in the game.
     */
    private int highestBet = 0;

    /**
     * The current round of the game.
     * 0 - Game created but not started
     * 1 - Accepting players
     * 2 - First betting round
     * 3 - Card exchange
     * 4 - Second betting round
     * 5 - Showdown
     */
    private int round = 0;

    /**
     * Constructs a new Game instance and initializes the available moves.
     */
    public Game() {
        generateMoves();
        gameCounter++;
    }

    /**
     * Starts a new game with the specified player count and ante amount.
     *
     * @param playerCount the number of players required for the game.
     * @param ante        the ante amount.
     */
    public void startGame(int playerCount, int ante) {
        this.playerCount = playerCount;
        this.ante = ante;
        this.highestBet = ante;
        this.gameID = gameCounter;
        this.round = 1;
    }

    /**
     * Processes a move made by a player.
     *
     * @param player the player making the move.
     * @param move   the move details.
     * @return the result of the move execution.
     */
    public String processMove(Player player, Map<String, String> move) {
        String result = validateMove(player, move);
        if (result.isEmpty()) {
            result = moves.get(move.get(Token.RODZAJ_RUCHU.getName())).
                    execute(this, player, move.get(Token.PARAMETRY_RUCHU.getName()));

            adjustGameParameters(move.get(Token.RODZAJ_RUCHU.getName()));
        }
        return result;
    }

    /**
     * Adjusts game parameters based on the most recent move.
     *
     * @param moveName the name of the move.
     */
    public void adjustGameParameters(String moveName) {
        if ((moveName.equals(Moves.JOIN.getName()) && this.round > 1) || (moveName.equals(Moves.STATUS.getName()))
            || moveName.equals(Moves.NEW_GAME.getName())) {
            return;
        }

        boolean hasRoundIncreased = false;

        if (this.round == 1 && this.playerCount == getNumberOfPlayersInGame()) {
            increaseRound(1);
            hasRoundIncreased = true;
            this.whoseTurnID = 1;
            dealCards();
        }
        else if (this.round == 2) {
            if (hasAllExceptOnePassed()) {
                increaseRound(3);
                hasRoundIncreased = true;
                checkResults();
            }
            else if (hasAllInGameEqualBet()) {
                increaseRound(1);
                hasRoundIncreased = true;
            }
        }
        else if ((this.round == 4 && (hasAllInGameEqualBet() || hasAllExceptOnePassed()))
                || (this.round == 3 && hasAllExchanged())) {
            increaseRound(1);
            hasRoundIncreased = true;
            if (round == 5) {
                checkResults();
            }
        }

        updateAllMoves();

        if (!hasRoundIncreased) {
            updateTurn();
        }
        else {
            this.whoseTurnID = 1;
        }
    }

    /**
     * Evaluates the game results and determines the winners.
     */
    public void checkResults() {
        evaluateWinAmount();

        if (hasAllExceptOnePassed()) {
            for (Player player : players) {
                if (!player.hasPlayerPassed()) {
                    this.winnersID.add(player.getPlayerID());
                }
            }
        }
        else {
            List<Player> toCheck = new ArrayList<>();
            for (Player player : players) {
                if (!player.hasPlayerPassed()) {
                    toCheck.add(player);
                }
            }

            this.winnersID = evaluator.evaluateHands(new ArrayList<>(toCheck));
        }
    }

    /**
     * Ends the game and resets the players.
     *
     * @param playersToReset the list of players to reset.
     */
    public void endGame(List<Player> playersToReset) {
        for (Player player : playersToReset) {
            player.resetPlayer();
        }
    }

    /**
     * Check if every player in game has already exchanged their cards
     * @return boolean answer to the question
     */
    private boolean hasAllExchanged() {
        boolean checker = true;
        for (Player player : players) {
            if (!player.hasPlayerPassed() && !player.getLastMove().equals(Moves.EXCHANGE.getName())) {
                checker = false;
                break;
            }
        }
        return checker;
    }

    /**
     * Check if every player in game has already checked for game results
     * @return boolean answer to the question
     */
    public boolean hasAllCheckedResults() {
        boolean checker = true;
        for (Player player : players) {
            if (!player.getLastMove().equals(Moves.STATUS.getName())) {
                checker = false;
                break;
            }
        }
        return checker;
    }

    /**
     * Check if every player in game, except one, has already passed
     * @return boolean answer to the question
     */
    private boolean hasAllExceptOnePassed() {
        int notPassedCounter = 0;
        for (Player player : players) {
            if (!player.hasPlayerPassed()) {
                notPassedCounter++;
            }
        }
        return notPassedCounter == 1;
    }

    /**
     * Check if every player in game has equal bet
     * @return boolean answer to the question
     */
    private boolean hasAllInGameEqualBet() {
        boolean checker = true;
        if (this.highestBet != this.ante) {
            for (Player player : players) {
                if (!player.hasPlayerPassed() && player.getBet() != this.highestBet) {
                    checker = false;
                }
            }
        }
        else {
            checker = false;
        }

        return checker;
    }

    /**
     * Update turn in game. Determines whose turn is now
     */
    private void updateTurn() {
        int checker = 0;
        if (players.isEmpty()) {
            return;
        }

        while (checker == 0) {
            if (whoseTurnID < getNumberOfPlayersInGame()) {
                whoseTurnID++;
            } else {
                whoseTurnID = 1;
            }

            for (Player player : players) {
                if (player.getPlayerID() == this.whoseTurnID && !player.hasPlayerPassed()) {
                    checker = 1;
                    break;
                }
            }
        }
    }

    /**
     * Updates all players moves. Available moves depends on game round
     */
    private void updateAllMoves() {
        if (this.round == 2 || this.round == 4) {
            List<String> newMoves = new ArrayList<>(Arrays.asList(Moves.FOLD.getName(), Moves.CALL.getName(),
                    Moves.RAISE.getName(), Moves.STATUS.getName()));
            for (Player player : players) {
                player.updateAvailableMoves(newMoves);
            }
        }
        else if (this.round == 3) {
            List<String> newMoves = new ArrayList<>(Arrays.asList(Moves.EXCHANGE.getName(), Moves.STATUS.getName()));
            for (Player player : players) {
                player.updateAvailableMoves(newMoves);
            }
        }
        else if (this.round == 5) {
            List<String> newMoves = new ArrayList<>(Collections.singletonList(Moves.STATUS.getName()));
            for (Player player : players) {
                player.updateAvailableMoves(newMoves);
            }
        }
    }

    /**
     * Deals cards to all players and shuffles the deck.
     */
    public void dealCards() {
        this.deck = Deck.newDeck();
        Deck.shuffleDeck(this.deck);
        Croupier.dealCards(this.deck, this.players);
    }


    public double getWinAmount() {
        BigDecimal winAmount = new BigDecimal(this.win)
                .divide(new BigDecimal(winnersID.size()), 2, RoundingMode.HALF_UP);
        return winAmount.doubleValue();
    }

    /**
     * Evaluates win amount in this game
     */
    private void evaluateWinAmount() {
        for (Player player : players) {
            this.win += player.getBet();
        }
    }


    public int getRound() { return this.round; }

    public List<Integer> getWinnersID() { return this.winnersID; }

    public void increaseRound(int a) { this.round += a; }

    public int whoseTurn() { return whoseTurnID; }

    public int getPlayerCount() { return playerCount; }

    public int getNumberOfPlayersInGame() { return players.size(); }

    public int getAnte() { return ante; }

    public int getGameID() { return gameID; }

    public void updateHighestBet(int bet) { this.highestBet += bet; }

    public int getHighestBet() { return this.highestBet; }

    public List<Card> getDeck() { return this.deck; }

    public void setDeck(List<Card> deck) { this.deck = deck; }

    public List<Player> getPlayers() { return this.players; }

    public void addPlayer(Player player) {
        player.setPlayerID(this.players.size() + 1);
        player.setGameID(this.gameID);
        player.updateBet(this.ante);
        List<String> movesToUpdate = new ArrayList<>(Arrays.asList(Moves.STATUS.getName(), Moves.EXIT.getName()));
        player.updateAvailableMoves(movesToUpdate);

        this.players.add(player);
    }


    private String validateMove(Player player, Map<String, String> move) {
        String result = "";
        String wrongParams = "Niepoprawne parametry!";

        if ((Integer.parseInt(move.get(Token.ID_GRY.getName())) != this.gameID && player.getPlayerID() != 0)
                || Integer.parseInt(move.get(Token.ID_GRACZA.getName())) != player.getPlayerID()) {
            result = "Ruch wykonany został przez gracza o niepoprawnych danych ID!";
        }
        else if (!player.isMoveAvailable(move.get(Token.RODZAJ_RUCHU.getName()))
                || (move.get(Token.RODZAJ_RUCHU.getName()).equals(Moves.CALL.getName()) && player.getBet() == this.highestBet)) {
            result = "Ruch jest niedostępny!";
        }
        else if (player.getPlayerID() != 0 && this.whoseTurnID != player.getPlayerID()
                && this.round > 1 && !move.get(Token.RODZAJ_RUCHU.getName()).equals(Moves.STATUS.getName())) {
            result = "Nie twoja kolej!";
        }

        if (move.get(Token.RODZAJ_RUCHU.getName()).equals(Moves.EXCHANGE.getName())) {
            String parameters = move.get(Token.PARAMETRY_RUCHU.getName());
            if (!parameters.isEmpty()) {
                String[] params = parameters.split(",");
                if (params.length > 4) {
                    result = wrongParams;
                }

                String playerCards = player.getCopyOfHand().toString();
                for (int i = 0; i < params.length; i++) {
                    params[i] = params[i].trim();
                    String[] parts = params[i].split("-");
                    if (parts.length != 2 || !Card.exists(parts) || !playerCards.contains(params[i])) {
                        result = wrongParams;
                    }
                }
            }
        }
        else if (move.get(Token.RODZAJ_RUCHU.getName()).equals(Moves.RAISE.getName())) {
            String parameters = move.get(Token.PARAMETRY_RUCHU.getName());
            try {
                int bet = Integer.parseInt(parameters);
                if (bet <= 0) {
                    result = wrongParams;
                }
            }
            catch (NumberFormatException e) {
                result = wrongParams;
            }
        }
        else if (move.get(Token.RODZAJ_RUCHU.getName()).equals(Moves.NEW_GAME.getName())) {
            String parameters = move.get(Token.PARAMETRY_RUCHU.getName());
            String[] params = parameters.split(",");
            if (params.length != 2) {
                result = wrongParams;
            }

            Integer[] parsedParams = new Integer[2];
            for (int i = 0; i < params.length; i++) {
                params[i] = params[i].trim();
                try {
                    parsedParams[i] = Integer.parseInt(params[i]);
                }
                catch (NumberFormatException e) {
                    result = wrongParams;
                }
            }

            if (parsedParams[0] < 2 || parsedParams[0] > 4 || parsedParams[1] < 0) {
                result = wrongParams;
            }
        }

        return result;
    }


    private void generateMoves() {
        moves.put(Moves.EXCHANGE.getName(), new Exchange());
        moves.put(Moves.JOIN.getName(), new Join());
        moves.put(Moves.RAISE.getName(), new Raise());
        moves.put(Moves.FOLD.getName(), new Fold());
        moves.put(Moves.CALL.getName(), new Call());
        moves.put(Moves.STATUS.getName(), new Status());
        moves.put(Moves.NEW_GAME.getName(), new NewGame());
    }
}
