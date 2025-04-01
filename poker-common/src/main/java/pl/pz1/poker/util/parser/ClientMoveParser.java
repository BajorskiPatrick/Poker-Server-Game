package pl.pz1.poker.util.parser;

/**
 * The ClientMoveParser class is responsible for parsing client move commands.
 * It converts a raw input message into a formatted string representation of the move,
 * including game and player identifiers.
 */
public class ClientMoveParser {

    /**
     * Parses a client command message into a structured format.
     *
     * The resulting format includes:
     * - Game ID (ID_GRY)
     * - Player ID (ID_GRACZA)
     * - Move type (RODZAJ_RUCHU)
     * - Move parameters (PARAMETRY_RUCHU)
     *
     * If the input message is invalid, the method returns "Niepoprawny ruch!".
     *
     * @param message  the input message from the client.
     * @param gameID   the game ID to include in the parsed result.
     * @param playerID the player ID to include in the parsed result.
     * @return a structured string representation of the move, or an error message if the input is invalid.
     */
    public String parse(String message, int gameID, int playerID) {
        String result = Token.ID_GRY.getName() + ":" + gameID + "\n" +
                Token.ID_GRACZA.getName() + ":" + playerID + "\n";

        int checker = 1;

        String[] parts = message.split(" ", 2);
        if (parts.length == 1) {
            if (parts[0].equals(Moves.JOIN.getName())) {
                result += Token.RODZAJ_RUCHU.getName() + ":JOIN\n" +
                        Token.PARAMETRY_RUCHU.getName() + ":";
            }
            else if (parts[0].equals(Moves.FOLD.getName())) {
                result += Token.RODZAJ_RUCHU.getName() + ":FOLD\n" +
                        Token.PARAMETRY_RUCHU.getName() + ":";
            }
            else if (parts[0].equals(Moves.EXIT.getName())) {
                result += Token.RODZAJ_RUCHU.getName() + ":EXIT\n" +
                        Token.PARAMETRY_RUCHU.getName() + ":";
            }
            else if (parts[0].equals(Moves.CALL.getName())) {
                result += Token.RODZAJ_RUCHU.getName() + ":CALL\n" +
                        Token.PARAMETRY_RUCHU.getName() + ":";
            }
            else if (parts[0].equals(Moves.STATUS.getName())) {
                result += Token.RODZAJ_RUCHU.getName() + ":STATUS\n" +
                        Token.PARAMETRY_RUCHU.getName() + ":";
            }
            else if(parts[0].equals(Moves.EXCHANGE.getName())) {
                result += Token.RODZAJ_RUCHU.getName() + ":EXCHANGE\n"
                        + Token.PARAMETRY_RUCHU.getName() + ":";
            }
            else {
                checker = 0;
            }
        }
        else if (parts.length == 2) {
            parts[1] = parts[1].trim();
            if (parts[0].equals(Moves.EXCHANGE.getName())) {
                result += Token.RODZAJ_RUCHU.getName() + ":EXCHANGE\n" +
                        Token.PARAMETRY_RUCHU.getName() + ":" + parts[1];
            }
            else if (message.startsWith("RAISE")) {
                result += Token.RODZAJ_RUCHU.getName() + ":RAISE\n" +
                        Token.PARAMETRY_RUCHU.getName() + ":" + parts[1];
            }
            else if (message.startsWith("NEW_GAME")) {
                result += Token.RODZAJ_RUCHU.getName() + ":NEW_GAME\n" +
                        Token.PARAMETRY_RUCHU.getName() + ":" + parts[1];
            }
            else {
                checker = 0;
            }
        }
        else {
            checker = 0;
        }

        if (checker == 0) {
            result = "Niepoprawny ruch!";
        }

        return result;
    }
}
