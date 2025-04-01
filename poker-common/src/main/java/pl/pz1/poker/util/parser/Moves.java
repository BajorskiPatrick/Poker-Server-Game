package pl.pz1.poker.util.parser;

/**
 * The Moves enum represents the various actions or commands that a player can make in the poker game.
 * Each move has an associated string name for identification.
 */
public enum Moves {

    /**
     * Represents the "CALL" move.
     */
    CALL("CALL"),

    /**
     * Represents the "EXCHANGE" move.
     */
    EXCHANGE("EXCHANGE"),

    /**
     * Represents the "EXIT" move.
     */
    EXIT("EXIT"),

    /**
     * Represents the "FOLD" move.
     */
    FOLD("FOLD"),

    /**
     * Represents the "JOIN" move.
     */
    JOIN("JOIN"),

    /**
     * Represents the "RAISE" move.
     */
    RAISE("RAISE"),

    /**
     * Represents the "STATUS" move.
     */
    STATUS("STATUS"),

    /**
     * Represents the "NEW_GAME" move.
     */
    NEW_GAME("NEW_GAME");

    /**
     * The string name of the move.
     */
    private final String name;

    /**
     * Constructs a Moves enum with the specified name.
     *
     * @param name the string name of the move.
     */
    Moves(String name) {
        this.name = name;
    }

    /**
     * Returns the string name of the move.
     *
     * @return the name of the move.
     */
    public String getName() {
        return name;
    }

    /**
     * Checks if a given string corresponds to a valid move.
     *
     * @param name the name to check.
     * @return true if the name matches a valid move, false otherwise.
     */
    public static boolean contains(String name) {
        for (Moves m : Moves.values()) {
            if (m.name.equals(name)) {
                return true;
            }
        }
        return false;
    }
}
