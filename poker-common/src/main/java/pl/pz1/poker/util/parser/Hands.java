package pl.pz1.poker.util.parser;

/**
 * The Hands enum represents the hierarchy of poker hands, each assigned a value to indicate its rank.
 * Higher values represent stronger hands.
 */
public enum Hands {

    /**
     * Represents the "ROYAL FLUSH" hand, the strongest poker hand.
     */
    ROYAL_FLUSH(9),

    /**
     * Represents the "STRAIGHT FLUSH" hand.
     */
    STRAIGHT_FLUSH(8),

    /**
     * Represents the "FOUR OF A KIND" hand (also known as "QUADS").
     */
    QUADS(7),

    /**
     * Represents the "FULL HOUSE" hand.
     */
    FULL_HOUSE(6),

    /**
     * Represents the "FLUSH" hand.
     */
    FLUSH(5),

    /**
     * Represents the "STRAIGHT" hand.
     */
    STRAIGHT(4),

    /**
     * Represents the "THREE OF A KIND" hand.
     */
    THREE_OF_A_KIND(3),

    /**
     * Represents the "TWO PAIRS" hand.
     */
    TWO_PAIRS(2),

    /**
     * Represents the "ONE PAIR" hand.
     */
    ONE_PAIR(1);

    /**
     * The value representing the strength of the hand.
     */
    private final int value;

    /**
     * Constructs a Hands enum with the specified value.
     *
     * @param value the value representing the strength of the hand.
     */
    Hands(int value) {
        this.value = value;
    }

    /**
     * Returns the value representing the strength of the hand.
     *
     * @return the value of the hand.
     */
    public int getValue() {
        return value;
    }
}

