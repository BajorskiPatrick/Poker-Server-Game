package pl.pz1.poker.moves;

import pl.pz1.poker.gameplay.Game;
import pl.pz1.poker.gameplay.Player;
import pl.pz1.poker.util.parser.Moves;

/**
 * The Call class represents the "CALL" move in the poker game.
 * A player matches the current highest bet in the game.
 */
public class Call implements Move {

    /**
     * Executes the "CALL" move by updating the player's bet to match the highest bet in the game.
     *
     * @param game       the current game instance.
     * @param player     the player making the move.
     * @param parameters additional parameters for the move (unused in this implementation).
     * @return a message indicating the move was successfully executed.
     */
    @Override
    public String execute(Game game, Player player, String parameters) {
        player.updateBet(game.getHighestBet());
        player.setLastMove(Moves.CALL.getName());
        return "Ruch wykonany prawidłowo!";
    }
}

