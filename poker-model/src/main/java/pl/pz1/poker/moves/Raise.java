package pl.pz1.poker.moves;

import pl.pz1.poker.gameplay.Game;
import pl.pz1.poker.gameplay.Player;
import pl.pz1.poker.util.parser.Moves;

/**
 * The Raise class represents the "RAISE" move in the poker game.
 * A player uses this move to increase the current highest bet.
 */
public class Raise implements Move {

    /**
     * Executes the "RAISE" move by updating the game's highest bet and the player's bet.
     *
     * @param game       the current game instance.
     * @param player     the player making the move.
     * @param parameters the new bet amount as a string.
     * @return a message indicating the move was successfully executed.
     */
    @Override
    public String execute(Game game, Player player, String parameters) {
        game.updateHighestBet(Integer.parseInt(parameters));
        player.updateBet(game.getHighestBet());
        player.setLastMove(Moves.RAISE.getName());
        return "Ruch wykonany prawidłowo!";
    }
}
