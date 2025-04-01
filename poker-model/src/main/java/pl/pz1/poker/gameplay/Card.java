package pl.pz1.poker.gameplay;

import java.util.*;

/**
 * The Card class represents a playing card, which consists of a rank and a suit.
 * It provides methods for creating, manipulating, and comparing cards, as well as
 * utilities for working with decks of cards.
 */
public class Card {

    /**
     * Enum representing the suit of a card (Spades, Clubs, Diamonds, Hearts).
     */
    public enum Suit {
        SPADES("S"), CLUBS("C"), DIAMONDS("D"), HEARTS("H");

        private final String cardsSuit;

        /**
         * Constructor for Suit.
         *
         * @param suit the string representation of the suit.
         */
        Suit(String suit) { this.cardsSuit = suit; }

        /**
         * Gets the string representation of the suit.
         *
         * @return the string representation of the suit.
         */
        public String getSuit() { return this.cardsSuit; }

        /**
         * Checks if the given string matches any suit.
         *
         * @param suit the string to check.
         * @return true if the suit exists, false otherwise.
         */
        public static boolean contains(String suit) {
            for (Suit s : Suit.values()) {
                if (s.cardsSuit.equals(suit)) {
                    return true;
                }
            }
            return false;
        }
    }

    /**
     * Enum representing the rank of a card (Two, Three, ..., Ace).
     */
    public enum Rank {
        TWO("2"), THREE("3"), FOUR("4"), FIVE("5"), SIX("6"), SEVEN("7"), EIGHT("8"), NINE("9"),
        TEN("10"), JACK("J"), QUEEN("Q"), KING("K"), ACE("A");

        private final String cardsRank;

        /**
         * Constructor for Rank.
         *
         * @param rank the string representation of the rank.
         */
        Rank(String rank) { this.cardsRank = rank; }

        /**
         * Gets the string representation of the rank.
         *
         * @return the string representation of the rank.
         */
        public String getRank() { return this.cardsRank; }

        /**
         * Checks if the given string matches any rank.
         *
         * @param rank the string to check.
         * @return true if the rank exists, false otherwise.
         */
        public static boolean contains(String rank) {
            for (Rank r : Rank.values()) {
                if (r.cardsRank.equals(rank)) {
                    return true;
                }
            }
            return false;
        }

        /**
         * Gets the next rank in the sequence.
         *
         * @return the next rank.
         */
        public Rank next() {
            Rank[] ranks = Rank.values();
            int nextOrdinal = (this.ordinal() + 1) % ranks.length;
            return ranks[nextOrdinal];
        }

        /**
         * Sorts a list of ranks in ascending order.
         *
         * @param ranks the list of ranks to sort.
         */
        public static void sortRanks(List<Rank> ranks) {
            ranks.sort(Comparator.comparingInt(Enum::ordinal));
        }

        /**
         * Gets the highest rank from a list of ranks.
         *
         * @param ranks the list of ranks.
         * @return the highest rank.
         */
        public static Rank getHighestRank(List<Rank> ranks) {
            Rank highest = ranks.get(0);
            for (Rank r : ranks) {
                if (r.ordinal() > highest.ordinal()) {
                    highest = r;
                }
            }
            return highest;
        }

        /**
         * Gets the lowest rank from a list of ranks.
         *
         * @param ranks the list of ranks.
         * @return the lowest rank.
         */
        public static Rank getLowestRank(List<Rank> ranks) {
            Rank lowest = ranks.get(0);
            for (Rank r : ranks) {
                if (r.ordinal() < lowest.ordinal()) {
                    lowest = r;
                }
            }
            return lowest;
        }
    }

    private final Rank rank;
    private final Suit suit;

    /**
     * Constructs a Card with the specified rank and suit.
     *
     * @param r the rank of the card.
     * @param s the suit of the card.
     */
    public Card(Rank r, Suit s) {
        this.rank = r;
        this.suit = s;
    }

    /**
     * Gets the rank of the card.
     *
     * @return the rank of the card.
     */
    public Rank getRank() {
        return this.rank;
    }

    /**
     * Gets the suit of the card.
     *
     * @return the suit of the card.
     */
    public Suit getSuit() {
        return this.suit;
    }

    /**
     * Returns a string representation of the card.
     *
     * @return the string representation of the card.
     */
    @Override
    public String toString() {
        return this.rank.getRank() + "-" + this.suit.getSuit();
    }

    /**
     * Checks if this card is equal to another object.
     *
     * @param o the object to compare with.
     * @return true if the cards are equal, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return rank == card.rank && suit == card.suit;
    }

    /**
     * Computes the hash code for the card.
     *
     * @return the hash code of the card.
     */
    @Override
    public int hashCode() {
        return Objects.hash(rank, suit);
    }

    /**
     * Checks if a card exists based on its rank and suit.
     *
     * @param parts an array where the first element is the rank and the second is the suit.
     * @return true if the card exists, false otherwise.
     */
    public static boolean exists(String[] parts) {
        return Rank.contains(parts[0]) && Suit.contains(parts[1]);
    }

    /**
     * A prototype deck of all possible cards.
     */
    private static final List<Card> prototypeDeck = new ArrayList<>();

    static {
        for (Suit s : Suit.values()) {
            for (Rank r : Rank.values()) {
                prototypeDeck.add(new Card(r, s));
            }
        }
    }

    /**
     * Creates a new deck of cards based on the prototype deck.
     *
     * @return a copy of the prototype deck.
     */
    public static List<Card> newDeck() {
        return new ArrayList<>(prototypeDeck);
    }
}
