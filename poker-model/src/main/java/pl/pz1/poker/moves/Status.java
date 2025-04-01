package pl.pz1.poker.moves;

import pl.pz1.poker.gameplay.Game;
import pl.pz1.poker.gameplay.Player;
import pl.pz1.poker.util.parser.Moves;

/**
 * The Status class represents the "STATUS" move in the poker game.
 * A player uses this move to get detailed information about their current game status.
 */
public class Status implements Move {

    /**
     * Executes the "STATUS" move by generating a message containing the player's status,
     * including available moves, hand, bet amounts, and game-specific details.
     *
     * @param game       the current game instance.
     * @param player     the player making the move.
     * @param parameters additional parameters for the move (unused in this implementation).
     * @return a status message describing the player's current situation in the game.
     */
    @Override
    public String execute(Game game, Player player, String parameters) {
        String status = "";
        String constant = "Możliwe ruchy: ";

        if (player.getPlayerID() == 0 && game.getRound() == 0) {
            status = constant + player.getAvailableMoves();
        } else if (player.getPlayerID() == 0 && game.getRound() == 1) {
            status = constant + player.getAvailableMoves() + "\n"
                    + "Dołączenie do gry wymaga wpłaty ante w wysokości: " + game.getAnte() + "zł";
        } else if (player.hasPlayerPassed() && game.getRound() < 5) {
            status = constant + "[" + Moves.STATUS.getName() + "]\n"
                    + "Spasowałeś w tej rozgrywce! Zaczekaj na jej zakończenie";
        } else if (game.getRound() > 1 && game.getRound() < 5 && player.getPlayerID() != 0 && game.whoseTurn() == player.getPlayerID()) {
            if ((game.getRound() == 2 || game.getRound() == 4) && player.getBet() == game.getHighestBet()) {
                status = constant + "[" + Moves.FOLD.getName() + ", " + Moves.RAISE.getName() + ", " + Moves.STATUS.getName() + "]\n";
            } else {
                status = constant + player.getAvailableMoves() + "\n";
            }

            status += "Twoje karty: " + player.getCopyOfHand().toString() + "\n"
                    + "Twój BET: " + player.getBet() + "zł, najwyższy BET w grze: " + game.getHighestBet() + "zł\n"
                    + "Kolejka: Twoja kolej!";
        } else if (game.getRound() > 1 && game.getRound() < 5 && player.getPlayerID() != 0 && game.whoseTurn() != player.getPlayerID()) {
            status += constant + "[" + Moves.STATUS.getName() + "]\n"
                    + "Twoje karty: " + player.getCopyOfHand().toString() + "\n"
                    + "Twój BET: " + player.getBet() + "zł, najwyższy BET w grze: " + game.getHighestBet() + "zł\n"
                    + "Kolejka: kolej gracza " + game.whoseTurn();
        } else if (game.getRound() == 1 && player.getPlayerID() > 0) {
            status = constant + player.getAvailableMoves() + "\n"
                    + "Oczekiwanie na dołączenie do gry wymaganej liczby graczy...";
        } else if (game.getRound() == 5 && player.getPlayerID() > 0 && !game.getWinnersID().contains(player.getPlayerID())) {
            status = constant + "[" + Moves.STATUS.getName() + "]\n"
                    + "Gra zakończyła się! Wygrał gracz o ID: " + game.getWinnersID();
        } else if (game.getRound() == 5 && player.getPlayerID() > 0 && game.getWinnersID().contains(player.getPlayerID())) {
            status = constant + "[" + Moves.STATUS.getName() + "]\n"
                    + "Gra zakończyła się! Wygrałeś! Twoja wygrana to: " + game.getWinAmount() + "zł";
        }

        if (game.getRound() == 5) {
            player.setLastMove(Moves.STATUS.getName());
        }

        return status;
    }
}
