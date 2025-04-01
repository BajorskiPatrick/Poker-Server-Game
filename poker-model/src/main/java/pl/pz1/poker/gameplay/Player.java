package pl.pz1.poker.gameplay;

import pl.pz1.poker.util.parser.Moves;

import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * The Player class represents a player in the poker game. It manages player-specific data,
 * such as the player's hand, bets, moves, and game-related information.
 */
public class Player {

    /**
     * The player's hand of cards, with a fixed size defined by HAND_SIZE.
     */
    private final ArrayList<Card> hand = new ArrayList<>(HAND_SIZE);

    /**
     * The network channel associated with the player.
     */
    SocketChannel channel;

    /**
     * The player's unique identifier.
     */
    private int playerID = 0;

    /**
     * The identifier of the game the player is participating in.
     */
    private int gameID = 0;

    /**
     * The player's current bet amount.
     */
    private int bet = 0;

    /**
     * The player's last move.
     */
    private String lastMove = "";

    /**
     * Indicates whether the player has passed.
     */
    private boolean hasPassed = false;

    /**
     * The list of moves available to the player.
     */
    private final List<String> availableMoves = new ArrayList<>();

    /**
     * The size of a player's hand.
     */
    public static final int HAND_SIZE = 5;

    /**
     * Constructs a Player object and initializes default moves.
     *
     * @param channel the network channel associated with the player.
     */
    public Player(SocketChannel channel) {
        this.channel = channel;
        setDefaultMoves();
    }

    /**
     * Gets the player's unique identifier.
     *
     * @return the player's ID.
     */
    public int getPlayerID() {
        return this.playerID;
    }

    /**
     * Sets the player's unique identifier.
     *
     * @param playerID the player's ID.
     */
    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    /**
     * Gets the identifier of the game the player is participating in.
     *
     * @return the game ID.
     */
    public int getGameID() {
        return this.gameID;
    }

    /**
     * Sets the identifier of the game the player is participating in.
     *
     * @param gameID the game ID.
     */
    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    /**
     * Updates the player's bet amount.
     *
     * @param bet the new bet amount.
     */
    public void updateBet(int bet) {
        this.bet = bet;
    }

    /**
     * Gets the player's current bet amount.
     *
     * @return the bet amount.
     */
    public int getBet() {
        return this.bet;
    }

    /**
     * Gets the list of moves available to the player as a string.
     *
     * @return a string representation of available moves.
     */
    public String getAvailableMoves() {
        return this.availableMoves.toString();
    }

    /**
     * Checks if a specific move is available to the player.
     *
     * @param move the move to check.
     * @return true if the move is available, false otherwise.
     */
    public boolean isMoveAvailable(String move) {
        return this.availableMoves.contains(move);
    }

    /**
     * Checks if the player has passed.
     *
     * @return true if the player has passed, false otherwise.
     */
    public boolean hasPlayerPassed() {
        return this.hasPassed;
    }

    /**
     * Marks the player as having passed.
     */
    public void setAsPassed() {
        this.hasPassed = true;
    }

    /**
     * Gets the player's last move.
     *
     * @return the player's last move.
     */
    public String getLastMove() {
        return this.lastMove;
    }

    /**
     * Sets the player's last move.
     *
     * @param lastMove the last move made by the player.
     */
    public void setLastMove(String lastMove) {
        this.lastMove = lastMove;
    }

    /**
     * Updates the list of moves available to the player.
     *
     * @param moves the new list of moves.
     */
    public void updateAvailableMoves(List<String> moves) {
        this.availableMoves.clear();
        this.availableMoves.addAll(moves);
    }

    /**
     * Sets the default moves available to the player.
     */
    public void setDefaultMoves() {
        this.availableMoves.clear();
        this.availableMoves.add(Moves.NEW_GAME.getName());
        this.availableMoves.add(Moves.STATUS.getName());
        this.availableMoves.add(Moves.EXIT.getName());
    }

    /**
     * Adds a card to the player's hand.
     *
     * @param card the card to add.
     */
    public void giveCard(Card card) {
        this.hand.add(card);
    }

    /**
     * Updates the player's hand with a new list of cards.
     *
     * @param hand the new list of cards.
     */
    public void updateHand(List<Card> hand) {
        this.hand.clear();
        this.hand.addAll(hand);
    }

    /**
     * Gets a copy of the player's hand.
     *
     * @return a copy of the hand.
     */
    public List<Card> getCopyOfHand() {
        return new ArrayList<>(this.hand);
    }

    /**
     * Gets the player's hand.
     *
     * @return the hand.
     */
    public List<Card> getHand() {
        return this.hand;
    }

    /**
     * Resets the player's data to its default state.
     */
    public void resetPlayer() {
        this.playerID = 0;
        this.gameID = 0;
        this.bet = 0;
        this.hasPassed = false;
        setDefaultMoves();
        this.lastMove = "";
        this.hand.clear();
    }
}
