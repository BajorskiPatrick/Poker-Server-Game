package pl.pz1.poker.moves;

import pl.pz1.poker.gameplay.Game;
import pl.pz1.poker.gameplay.Player;

/**
 * The Move interface defines the contract for all poker game moves.
 * Each move must implement the execute method to define its behavior.
 */
public interface Move {

    /**
     * Executes the move in the context of the game and player.
     *
     * @param game       the current game instance.
     * @param player     the player making the move.
     * @param parameters additional parameters required for the move.
     * @return a message indicating the outcome of the move.
     */
    String execute(Game game, Player player, String parameters);
}

