package pl.pz1.poker.moves;

import pl.pz1.poker.gameplay.Game;
import pl.pz1.poker.gameplay.Player;
import pl.pz1.poker.util.parser.Moves;

/**
 * The Fold class represents the "FOLD" move in the poker game.
 * A player uses this move to pass and forfeit their participation in the current round.
 */
public class Fold implements Move {

    /**
     * Executes the "FOLD" move by marking the player as having passed.
     *
     * @param game       the current game instance.
     * @param player     the player making the move.
     * @param parameters additional parameters for the move (unused in this implementation).
     * @return a message indicating the move was successfully executed.
     */
    @Override
    public String execute(Game game, Player player, String parameters) {
        player.setAsPassed();
        player.setLastMove(Moves.FOLD.getName());
        return "Ruch wykonany prawidłowo!";
    }
}

