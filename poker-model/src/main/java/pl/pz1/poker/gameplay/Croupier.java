package pl.pz1.poker.gameplay;

import java.util.ArrayList;
import java.util.List;

/**
 * The Croupier class provides utility methods for managing card distribution during a poker game.
 * It includes methods to deal cards to players and add new cards to a player's hand.
 */
public class Croupier {

    /**
     * Deals cards from the deck to all players.
     *
     * @param deck    the deck of cards to deal from.
     * @param players the list of players who will receive the cards.
     */
    public static void dealCards(List<Card> deck, List<Player> players) {
        List<Card> toRemove = new ArrayList<>();
        for (int i = 0; i < Player.HAND_SIZE; i++) {
            for (int j = 0; j < players.size(); j++) {
                players.get(j).giveCard(deck.get(i * players.size() + j));
                toRemove.add(deck.get(i * players.size() + j));
            }
        }
        deck.removeAll(toRemove);
    }

    /**
     * Adds new cards to a player's hand from the deck.
     *
     * @param deck   the deck of cards to draw from.
     * @param player the player who will receive the cards.
     * @param amount the number of cards to give to the player.
     */
    public static void addNewCards(List<Card> deck, Player player, int amount) {
        List<Card> toRemove = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            player.giveCard(deck.get(i));
            toRemove.add(deck.get(i));
        }
        deck.removeAll(toRemove);
    }

    /**
     * Private constructor to prevent instantiation of the utility class.
     */
    private Croupier() {}
}

