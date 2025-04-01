package pl.pz1.poker;

import org.junit.jupiter.api.Test;
import pl.pz1.poker.gameplay.*;
import pl.pz1.poker.moves.Exit;
import pl.pz1.poker.util.parser.Moves;

import java.nio.channels.SocketChannel;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


class GameplayAndMovesTest {
    @Test
    void CroupierDealCardsTest() {
        List<Player> players = new ArrayList<>(List.of(new Player(mock(SocketChannel.class))));
        List<Card> deck = Deck.newDeck();
        Deck.shuffleDeck(deck);
        List<Card> toCheck = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            toCheck.add(deck.get(i));
        }
        Croupier.dealCards(deck, players);
        assertEquals(toCheck, players.get(0).getHand());
        assertFalse(deck.containsAll(toCheck));
    }



    @Test
    void resetPlayerTest() {
        Player player = new Player(mock(SocketChannel.class));
        player.setPlayerID(2);
        player.setGameID(1);
        player.updateBet(10);
        player.updateAvailableMoves(List.of(Moves.EXCHANGE.getName(), Moves.RAISE.getName()));
        player.setLastMove(Moves.EXCHANGE.getName());
        player.setAsPassed();

        List<Card> deck = Deck.newDeck();
        Deck.shuffleDeck(deck);
        List<Card> toCheck = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            toCheck.add(deck.get(i));
        }
        player.updateHand(toCheck);

        player.resetPlayer();

        assertEquals(0, player.getPlayerID());
        assertEquals(0, player.getGameID());
        assertEquals("", player.getLastMove());
        assertEquals(0, player.getBet());
        assertEquals(0, player.getHand().size());
        assertFalse(player.hasPlayerPassed());
        assertEquals(List.of(Moves.NEW_GAME.getName(), Moves.STATUS.getName(), Moves.EXIT.getName()).toString(), player.getAvailableMoves());
    }




    @org.junit.jupiter.api.Nested
    class MovesAndItsProcessingTests {
        @Test
        void testProcessMoveJOIN() {
            Game game = new Game();
            Map<String, String> move = new HashMap<>();
            Player player1 = new Player(mock(SocketChannel.class));
            player1.updateAvailableMoves(List.of("JOIN"));

            Player player2 = new Player(mock(SocketChannel.class));
            player2.updateAvailableMoves(List.of("JOIN"));

            game.startGame(1, 50);

            move.put("ID_GRY", "0");
            move.put("ID_GRACZA", "0");
            move.put("RODZAJ_RUCHU", "JOIN");
            move.put("PARAMETRY_RUCHU", "");

            String result = game.processMove(player1, move);
            assertTrue(result.contains("Dołączono do gry o ID:"));
            assertEquals(1, player1.getPlayerID());
            assertEquals(game.getGameID(), player1.getGameID());
            assertEquals(1, game.getNumberOfPlayersInGame());

            result = game.processMove(player2, move);
            assertTrue(result.contains("W grze jest już maksymalna liczba graczy!"));
        }


        @Test
        void testProcessMoveCALL() {
            Game game = new Game();
            game.startGame(1, 50);

            Map<String, String> move = new HashMap<>();
            Player player1 = new Player(mock(SocketChannel.class));
            player1.updateAvailableMoves(List.of("CALL"));

            move.put("ID_GRY", "0");
            move.put("ID_GRACZA", "0");
            move.put("RODZAJ_RUCHU", "CALL");
            move.put("PARAMETRY_RUCHU", "");

            String result = game.processMove(player1, move);
            String expected = "Ruch wykonany prawidłowo!";
            assertEquals(expected, result);
            assertEquals(player1.getBet(), game.getHighestBet());
            assertEquals("CALL", player1.getLastMove());
        }


        @Test
        void testProcessMoveFOLD() {
            Game game = new Game();
            game.startGame(1, 50);

            Map<String, String> move = new HashMap<>();
            Player player1 = new Player(mock(SocketChannel.class));
            player1.updateAvailableMoves(List.of("FOLD"));

            move.put("ID_GRY", "0");
            move.put("ID_GRACZA", "0");
            move.put("RODZAJ_RUCHU", "FOLD");
            move.put("PARAMETRY_RUCHU", "");

            String result = game.processMove(player1, move);
            String expected = "Ruch wykonany prawidłowo!";
            assertEquals(expected, result);
            assertTrue(player1.hasPlayerPassed());
            assertEquals("FOLD", player1.getLastMove());
        }


        @Test
        void testProcessMoveRAISE() {
            Game game = new Game();
            game.startGame(1, 50);

            Map<String, String> move = new HashMap<>();
            Player player1 = new Player(mock(SocketChannel.class));
            player1.updateAvailableMoves(List.of("RAISE"));

            move.put("ID_GRY", "0");
            move.put("ID_GRACZA", "0");
            move.put("RODZAJ_RUCHU", "RAISE");
            move.put("PARAMETRY_RUCHU", "50");

            String result = game.processMove(player1, move);
            String expected = "Ruch wykonany prawidłowo!";
            assertEquals(expected, result);
            assertEquals(game.getHighestBet(), player1.getBet());
            assertEquals(100, game.getHighestBet());
            assertEquals("RAISE", player1.getLastMove());
        }


        @Test
        void testProcessMoveNEW_GAME() {
            Game game = new Game();

            Map<String, String> move = new HashMap<>();
            Player player1 = new Player(mock(SocketChannel.class));
            player1.updateAvailableMoves(List.of("NEW_GAME"));

            move.put("ID_GRY", "0");
            move.put("ID_GRACZA", "0");
            move.put("RODZAJ_RUCHU", "NEW_GAME");
            move.put("PARAMETRY_RUCHU", "2, 50");

            String result = game.processMove(player1, move);
            String expected = "Nowa gra została utworzona!";
            assertEquals(expected, result);
            assertEquals(50, game.getAnte());
            assertEquals(2, game.getPlayerCount());
        }



        @Test
        void testCroupierAndProcessMoveEXCHANGE() {
            Game game = new Game();
            game.startGame(2, 50);
            Map<String, String> move = new HashMap<>();
            Player player1 = new Player(mock(SocketChannel.class));
            player1.updateAvailableMoves(List.of("EXCHANGE"));

            List<Card> deck = Deck.newDeck();
            List<Card> toCheck = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                player1.giveCard(deck.get(i));
                toCheck.add(deck.get(i));
            }
            deck.removeAll(toCheck);
            game.setDeck(deck);

            move.put("ID_GRY", "0");
            move.put("ID_GRACZA", "0");
            move.put("RODZAJ_RUCHU", "EXCHANGE");
            move.put("PARAMETRY_RUCHU", "");

            String result = game.processMove(player1, move);
            String expected = "Ruch wykonany prawidłowo!";
            assertEquals(expected, result);
            assertEquals(toCheck, player1.getHand());
            assertEquals("EXCHANGE", player1.getLastMove());


            move.put("PARAMETRY_RUCHU", toCheck.get(0).toString());
            toCheck.remove(0);
            toCheck.add(deck.get(0));
            int deckPreviouseSize = deck.size();

            result = game.processMove(player1, move);
            expected = "Ruch wykonany prawidłowo!";
            assertEquals(expected, result);
            assertEquals(toCheck, player1.getHand());
            assertEquals("EXCHANGE", player1.getLastMove());
            assertEquals(deckPreviouseSize, game.getDeck().size() + 1);
        }



        @Test
        void testProcessMoveSTATUS() {
            Game game = new Game();
            Map<String, String> move1 = new HashMap<>();
            Map<String, String> move2 = new HashMap<>();
            Map<String, String> move3 = new HashMap<>();

            move1.put("ID_GRY", "0");
            move1.put("ID_GRACZA", "0");
            move1.put("RODZAJ_RUCHU", "STATUS");
            move1.put("PARAMETRY_RUCHU", "");

            move2.put("ID_GRY", "0");
            move2.put("ID_GRACZA", "0");
            move2.put("RODZAJ_RUCHU", "STATUS");
            move2.put("PARAMETRY_RUCHU", "");

            move3.put("ID_GRY", "0");
            move3.put("ID_GRACZA", "0");
            move3.put("RODZAJ_RUCHU", "JOIN");
            move3.put("PARAMETRY_RUCHU", "");

            Player player1 = new Player(mock(SocketChannel.class));
            player1.updateAvailableMoves(List.of("JOIN", "STATUS"));

            Player player2 = new Player(mock(SocketChannel.class));
            player2.updateAvailableMoves(List.of("JOIN", "STATUS"));

            Player player3 = new Player(mock(SocketChannel.class));
            player3.updateAvailableMoves(List.of("JOIN", "STATUS"));


            String result = game.processMove(player1, move1);
            String expected = "Możliwe ruchy: " + player1.getAvailableMoves();
            assertEquals(expected, result);


            game.startGame(3, 50);
            move1.put("RODZAJ_RUCHU", "JOIN");
            game.processMove(player1, move1);

            move1.put("RODZAJ_RUCHU", "STATUS");
            move1.put("ID_GRY", "" + game.getGameID());
            move1.put("ID_GRACZA", "1");
            result = game.processMove(player1, move1);
            expected = "Oczekiwanie na dołączenie do gry wymaganej liczby graczy...";
            assertTrue(result.contains(expected));

            result = game.processMove(player2, move2);
            expected = "Dołączenie do gry wymaga wpłaty";
            assertTrue(result.contains(expected));
            move2.put("RODZAJ_RUCHU", "JOIN");
            game.processMove(player2, move2);
            game.processMove(player3, move3);


            move2.put("ID_GRY", "" + game.getGameID());
            move2.put("ID_GRACZA", "2");
            move3.put("ID_GRY", "" + game.getGameID());
            move3.put("ID_GRACZA", "3");

            move1.put("RODZAJ_RUCHU", "STATUS");
            move2.put("RODZAJ_RUCHU", "STATUS");
            result = game.processMove(player1, move1);
            expected = "Kolejka: Twoja kolej!";
            assertTrue(result.contains(expected));
            result = game.processMove(player2, move2);
            expected = "Kolejka: kolej gracza ";
            assertTrue(result.contains(expected));


            move1.put("RODZAJ_RUCHU", "RAISE");
            move1.put("PARAMETRY_RUCHU", "20");
            game.processMove(player1, move1);

            move2.put("RODZAJ_RUCHU", "FOLD");
            game.processMove(player2, move2);
            move2.put("RODZAJ_RUCHU", "STATUS");
            result = game.processMove(player2, move2);
            expected = "Spasowałeś w tej rozgrywce! Zaczekaj na jej zakończenie";
            assertTrue(result.contains(expected));

            move3.put("RODZAJ_RUCHU", "FOLD");
            game.processMove(player3, move3);

            result = game.processMove(player2, move2);
            expected = "Wygrał gracz o ID";
            assertTrue(result.contains(expected));

            move1.put("RODZAJ_RUCHU", "STATUS");
            move1.put("PARAMETRY_RUCHU", "");
            result = game.processMove(player1, move1);

            expected = "Twoja wygrana to:";
            assertTrue(result.contains(expected));
        }


        @Test
        void simpleExitTest() {
            Exit exit = new Exit();
            assertEquals("Do zobaczenia!", exit.execute(null, null, null));
        }
    }



    @org.junit.jupiter.api.Nested
    class GameTests {
        @Test
        void testStartGame() {
            Game game = new Game();
            game.startGame(2, 50);

            assertEquals(2, game.getPlayerCount());
            assertEquals(50, game.getAnte());
            assertEquals(1, game.getRound());
        }
    }
}
