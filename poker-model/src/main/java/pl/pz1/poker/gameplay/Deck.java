package pl.pz1.poker.gameplay;

import java.util.Collections;
import java.util.List;

/**
 * The Deck class provides utility methods for creating and managing a deck of cards.
 * It includes methods for generating a new deck and shuffling an existing deck.
 */
public class Deck {

    /**
     * Creates a new standard deck of cards.
     *
     * @return a list containing all cards in a standard deck.
     */
    public static List<Card> newDeck() {
        return Card.newDeck();
    }

    /**
     * Shuffles the given deck of cards randomly.
     *
     * @param toShuffle the deck of cards to shuffle.
     */
    public static void shuffleDeck(List<Card> toShuffle) {
        Collections.shuffle(toShuffle);
    }

    /**
     * Private constructor to prevent instantiation of the utility class.
     */
    private Deck() {}
}
