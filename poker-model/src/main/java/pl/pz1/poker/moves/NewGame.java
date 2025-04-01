package pl.pz1.poker.moves;

import pl.pz1.poker.gameplay.Game;
import pl.pz1.poker.gameplay.Player;

/**
 * The NewGame class represents the "NEW_GAME" move in the poker game.
 * A player uses this move to start a new game with specified parameters.
 */
public class NewGame implements Move {

    /**
     * Executes the "NEW_GAME" move by initializing a new game with the specified parameters.
     *
     * @param game       the current game instance.
     * @param player     the player making the move.
     * @param parameters a comma-separated string containing the number of players and the initial bet.
     * @return a message indicating the new game was successfully created.
     */
    @Override
    public String execute(Game game, Player player, String parameters) {
        String[] params = parameters.split(",");

        Integer[] parsedParams = new Integer[2];
        for (int i = 0; i < params.length; i++) {
            params[i] = params[i].trim();
            parsedParams[i] = Integer.parseInt(params[i]);
        }

        game.startGame(parsedParams[0], parsedParams[1]);

        return "Nowa gra została utworzona!";
    }
}

