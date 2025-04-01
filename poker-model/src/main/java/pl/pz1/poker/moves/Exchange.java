package pl.pz1.poker.moves;

import pl.pz1.poker.gameplay.Card;
import pl.pz1.poker.gameplay.Croupier;
import pl.pz1.poker.gameplay.Game;
import pl.pz1.poker.gameplay.Player;
import pl.pz1.poker.util.parser.Moves;

import java.util.Iterator;
import java.util.List;

/**
 * The Exchange class represents the "EXCHANGE" move in the poker game.
 * A player can exchange selected cards from their hand for new ones from the deck.
 */
public class Exchange implements Move {

    /**
     * Executes the "EXCHANGE" move by removing specified cards from the player's hand
     * and replacing them with new cards from the game's deck.
     *
     * @param game       the current game instance.
     * @param player     the player making the move.
     * @param parameters a comma-separated string of cards to exchange.
     * @return a message indicating the move was successfully executed.
     */
    @Override
    public String execute(Game game, Player player, String parameters) {
        List<Card> playersCards = player.getHand();
        String[] cardsToRemove = parameters.split(",");

        if (cardsToRemove.length == 1 && cardsToRemove[0].isEmpty()) {
            player.setLastMove(Moves.EXCHANGE.getName());
            return "Ruch wykonany prawidłowo!";
        }

        for (int i = 0; i < cardsToRemove.length; i++) {
            cardsToRemove[i] = cardsToRemove[i].trim();
        }

        Iterator<Card> cardIterator = playersCards.iterator();
        while (cardIterator.hasNext()) {
            Card card = cardIterator.next();
            for (String s : cardsToRemove) {
                if (card.toString().equals(s)) {
                    cardIterator.remove();
                    break;
                }
            }
        }

        Croupier.addNewCards(game.getDeck(), player, cardsToRemove.length);
        player.setLastMove(Moves.EXCHANGE.getName());

        return "Ruch wykonany prawidłowo!";
    }
}

