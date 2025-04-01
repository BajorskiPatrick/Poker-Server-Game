package pl.pz1.poker.moves;

import pl.pz1.poker.gameplay.Game;
import pl.pz1.poker.gameplay.Player;

/**
 * The Exit class represents the "EXIT" move in the poker game.
 * A player uses this move to leave the game.
 */
public class Exit implements Move {

    /**
     * Executes the "EXIT" move by removing the player from the game.
     *
     * @param game       the current game instance, or null if no game is active.
     * @param player     the player making the move.
     * @param parameters additional parameters for the move (unused in this implementation).
     * @return a message indicating the player has exited the game.
     */
    @Override
    public String execute(Game game, Player player, String parameters) {
        if (game != null) {
            game.getPlayers().remove(player);
        }

        return "Do zobaczenia!";
    }
}

