package pl.pz1.poker.moves;

import pl.pz1.poker.gameplay.Game;
import pl.pz1.poker.gameplay.Player;

/**
 * The Join class represents the "JOIN" move in the poker game.
 * A player uses this move to join an active game if there is room for additional players.
 */
public class Join implements Move {

    /**
     * Executes the "JOIN" move by attempting to add the player to the current game.
     *
     * @param game       the current game instance.
     * @param player     the player making the move.
     * @param parameters additional parameters for the move (unused in this implementation).
     * @return a message indicating whether the player successfully joined the game or if the game is full.
     */
    @Override
    public String execute(Game game, Player player, String parameters) {
        String response;
        if (game.getNumberOfPlayersInGame() < game.getPlayerCount()) {
            game.addPlayer(player);
            response = "Dołączono do gry o ID: " + game.getGameID() + "! Twoje ID: " + player.getPlayerID() + "\n";
        } else {
            response = "W grze jest już maksymalna liczba graczy! Zaczekaj na kolejną grę lub opuść serwer wpisując 'exit\n";
        }

        return response;
    }
}

