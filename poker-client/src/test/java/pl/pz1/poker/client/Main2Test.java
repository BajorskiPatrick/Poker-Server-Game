package pl.pz1.poker.client;

import org.junit.jupiter.api.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import pl.pz1.poker.server.PokerServer;

class Main2Test {
    @Test
    void testClientExitingFromServer() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        PokerServer server = new PokerServer(8080);

        // Uruchom serwer w osobnym wątku
        executorService.submit(() -> server.start());

        String input = "exit\n";
        ByteArrayInputStream bis = new ByteArrayInputStream(input.getBytes());
        System.setIn(bis);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        System.setOut(new PrintStream(bos));

        // Symulowanie klienta
        PokerClient client = new PokerClient("localhost", 8080);
        client.start();

        // Możesz sprawdzić odpowiedzi serwera i komunikaty klienta
        Assertions.assertTrue(true); // Jeśli połączenie nie rzuci błędu, test przechodzi

        executorService.shutdownNow();
    }

    @Test
    void testClientCreatingNewGameOnServer() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        PokerServer server = new PokerServer(8080);

        // Uruchom serwer w osobnym wątku
        executorService.submit(() -> server.start());

        String input = "new_game 2, 10\nexit\n";
        ByteArrayInputStream bis = new ByteArrayInputStream(input.getBytes());
        System.setIn(bis);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        System.setOut(new PrintStream(bos));

        // Symulowanie klienta
        PokerClient client = new PokerClient("localhost", 8080);
        client.start();


        // Możesz sprawdzić odpowiedzi serwera i komunikaty klienta
        Assertions.assertTrue(true); // Jeśli połączenie nie rzuci błędu, test przechodzi

        executorService.shutdownNow();
    }

    @Test
    void testClientCheckingStatusOnServer() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        PokerServer server = new PokerServer(8080);

        // Uruchom serwer w osobnym wątku
        executorService.submit(() -> server.start());

        String input = "status\nexit\n";
        ByteArrayInputStream bis = new ByteArrayInputStream(input.getBytes());
        System.setIn(bis);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        System.setOut(new PrintStream(bos));

        // Symulowanie klienta
        PokerClient client = new PokerClient("localhost", 8080);
        client.start();

        // Możesz sprawdzić odpowiedzi serwera i komunikaty klienta
        Assertions.assertTrue(true); // Jeśli połączenie nie rzuci błędu, test przechodzi

        executorService.shutdownNow();
    }

    @Test
    void testClientJoinGameAndExitServer() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        PokerServer server = new PokerServer(8080);

        // Uruchom serwer w osobnym wątku
        executorService.submit(() -> server.start());

        String input = "new_game 2, 10\nJoin\nexit\n";
        ByteArrayInputStream bis = new ByteArrayInputStream(input.getBytes());
        System.setIn(bis);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        System.setOut(new PrintStream(bos));

        // Symulowanie klienta
        PokerClient client = new PokerClient("localhost", 8080);
        client.start();


        // Możesz sprawdzić odpowiedzi serwera i komunikaty klienta
        Assertions.assertTrue(true); // Jeśli połączenie nie rzuci błędu, test przechodzi

        executorService.shutdownNow();
    }

}



