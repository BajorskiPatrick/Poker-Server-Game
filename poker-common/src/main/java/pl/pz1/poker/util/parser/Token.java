package pl.pz1.poker.util.parser;

/**
 * The Token enum represents various tokens used in parsing game-related data.
 * Each token has an associated string name for identification.
 */
public enum Token {

    /**
     * Token representing the game ID.
     */
    ID_GRY("ID_GRY"),

    /**
     * Token representing the player ID.
     */
    ID_GRACZA("ID_GRACZA"),

    /**
     * Token representing the type of move.
     */
    RODZAJ_RUCHU("RODZAJ_RUCHU"),

    /**
     * Token representing the parameters of a move.
     */
    PARAMETRY_RUCHU("PARAMETRY_RUCHU");

    /**
     * The string name of the token.
     */
    private final String name;

    /**
     * Constructs a Token with the specified name.
     *
     * @param name the string name of the token.
     */
    Token(String name) {
        this.name = name;
    }

    /**
     * Returns the string name of the token.
     *
     * @return the name of the token.
     */
    public String getName() {
        return name;
    }
}

