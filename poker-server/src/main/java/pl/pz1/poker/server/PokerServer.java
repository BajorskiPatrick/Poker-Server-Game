package pl.pz1.poker.server;

import pl.pz1.poker.gameplay.Player;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.*;
import pl.pz1.poker.gameplay.*;
import pl.pz1.poker.moves.Exit;
import pl.pz1.poker.util.parser.Moves;
import pl.pz1.poker.util.parser.ServerMoveParser;
import pl.pz1.poker.util.parser.Token;


/**
 * The PokerServer class is responsible for handling a poker game server.
 * It manages player connections, game rounds, and processes player moves.
 */
public class PokerServer {

    /**
     * The port number on which the server listens for incoming connections.
     */
    private final int port;

    /**
     * The server socket channel used for accepting incoming connections.
     */
    private ServerSocketChannel serverSocketChannel;

    /**
     * The selector for handling multiple channels in a non-blocking manner.
     */
    private Selector selector;

    /**
     * A map of connected players associated with their respective socket channels.
     */
    final Map<SocketChannel, Player> players;

    /**
     * The current game instance being played on the server.
     */
    private Game game;

    /**
     * The parser used to interpret player moves.
     */
    private final ServerMoveParser moveParser = new ServerMoveParser();

    /**
     * The exit command handler for processing player exits.
     */
    private final Exit exit = new Exit();

    /**
     * Counter to track the number of rounds played.
     */
    private int roundCounter = 0;

    /**
     * Indicates if a player has been accepted before.
     */
    private boolean hasAcceptedSomebodyBefore = false;

    /**
     * Constructs a PokerServer with the specified port.
     *
     * @param port the port number on which the server will listen for connections.
     */
    public PokerServer(int port) {
        this.port = port;
        this.players = new HashMap<>();
    }

    /**
     * Starts the poker server and initializes the game loop.
     */
    public void start() {
        try {
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress(port));
            serverSocketChannel.configureBlocking(false);

            selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            System.out.println("Serwer rozpoczął pracę na porcie: " + port);
            System.out.println();

            boolean gameEndChecker = false;

            while (true) {
                this.roundCounter++;
                this.game = new Game();

                while (game.getRound() <= 1) {
                    selector.select();
                    Set<SelectionKey> keys = selector.selectedKeys();
                    Iterator<SelectionKey> iterator = keys.iterator();

                    while (iterator.hasNext()) {
                        SelectionKey key = iterator.next();
                        iterator.remove();

                        if (key.isAcceptable()) {
                            handleAccept();
                        } else if (key.isReadable()) {
                            handleRead(key);
                        }

                        if (players.isEmpty() && (roundCounter > 1 || hasAcceptedSomebodyBefore)) {
                            gameEndChecker = true;
                            break;
                        }
                    }

                    if (gameEndChecker) {
                        break;
                    }
                }

                if (gameEndChecker) {
                    break;
                }

                while (game.getRound() < 5) {
                    selector.select();
                    Set<SelectionKey> keys = selector.selectedKeys();
                    Iterator<SelectionKey> iterator = keys.iterator();

                    while (iterator.hasNext()) {
                        SelectionKey key = iterator.next();
                        iterator.remove();

                        if (key.isAcceptable()) {
                            handleAccept();
                        } else if (key.isReadable()) {
                            handleRead(key);
                        }
                    }
                }

                while (!game.hasAllCheckedResults()) {
                    selector.select();
                    Set<SelectionKey> keys = selector.selectedKeys();
                    Iterator<SelectionKey> iterator = keys.iterator();

                    while (iterator.hasNext()) {
                        SelectionKey key = iterator.next();
                        iterator.remove();

                        if (key.isAcceptable()) {
                            handleAccept();
                        } else if (key.isReadable()) {
                            handleRead(key);
                        }
                    }
                }

                game.endGame(new ArrayList<>(players.values()));
            }
        } catch (IOException e) {
            System.err.println("Błąd serwera: " + e.getMessage());
        } finally {
            close();
        }
    }

    /**
     * Handles a new player connection.
     *
     * @throws IOException if an I/O error occurs during the operation.
     */
    protected void handleAccept() throws IOException {
        SocketChannel clientChannel = serverSocketChannel.accept();
        clientChannel.configureBlocking(false);
        clientChannel.register(selector, SelectionKey.OP_READ);

        Player player = new Player(clientChannel);
        players.put(clientChannel, player);

        System.out.println("Nowy gracz został połączony: " + clientChannel.getRemoteAddress());
        hasAcceptedSomebodyBefore = true;
        if (game != null && game.getRound() > 0) {
            List<String> moves = new ArrayList<>(Arrays.asList(Moves.JOIN.getName(), Moves.STATUS.getName(), Moves.EXIT.getName()));
            player.updateAvailableMoves(moves);
        }
        sendMessage(clientChannel, "Możliwe ruchy: " + player.getAvailableMoves());
    }

    /**
     * Handles incoming messages from a player.
     *
     * @param key the selection key representing the player connection.
     * @throws IOException if an I/O error occurs during the operation.
     */
    protected void handleRead(SelectionKey key) throws IOException {
        SocketChannel clientChannel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int bytesRead = clientChannel.read(buffer);

        if (bytesRead == -1) {
            players.remove(clientChannel);
            clientChannel.close();
            System.out.println("Gracz został rozłączony");
            return;
        }

        String message = new String(buffer.array(), 0, bytesRead).trim();
        String response = processMessage(clientChannel, message);
        sendMessage(clientChannel, response);
    }

    /**
     * Processes a message from a player and generates an appropriate response.
     *
     * @param clientChannel the player's socket channel.
     * @param message        the message received from the player.
     * @return the response to be sent to the player.
     */
    String processMessage(SocketChannel clientChannel, String message) {
        Player player = players.get(clientChannel);
        Map<String, String> parsedMessage = moveParser.parse(message);

        if (parsedMessage.get(Token.RODZAJ_RUCHU.getName()).equals(Moves.EXIT.getName()) && player.isMoveAvailable(Moves.EXIT.getName())) {
            players.remove(clientChannel);
            if (players.isEmpty() && this.roundCounter == 1) {
                this.game = new Game();
            }
            return exit.execute(this.game, player, parsedMessage.get(Token.PARAMETRY_RUCHU.getName()));
        }

        if (this.game == null) {
            return "Brak gry do procesowania wiadomości!";
        }

        String result = game.processMove(player, parsedMessage);
        if (result.equals("Nowa gra została utworzona!")) {
            List<String> moves = new ArrayList<>(Arrays.asList(Moves.JOIN.getName(), Moves.STATUS.getName(), Moves.EXIT.getName()));
            for (Player player_ : players.values()) {
                player_.updateAvailableMoves(moves);
            }
        }

        return result;
    }

    /**
     * Sends a message to a player.
     *
     * @param clientChannel the player's socket channel.
     * @param message        the message to be sent.
     * @throws IOException if an I/O error occurs during the operation.
     */
    protected void sendMessage(SocketChannel clientChannel, String message) throws IOException {
        ByteBuffer buffer = ByteBuffer.wrap(message.getBytes());
        clientChannel.write(buffer);
    }

    /**
     * Sets the server socket channel, primarily used for testing purposes.
     *
     * @param serverSocketChannel the server socket channel to be set.
     */
    public void setServerSocketChannel(ServerSocketChannel serverSocketChannel) {
        this.serverSocketChannel = serverSocketChannel;
    }

    /**
     * Closes the server and releases resources.
     */
    public void close() {
        try {
            if (serverSocketChannel != null) {
                serverSocketChannel.close();
            }
            if (selector != null) {
                selector.close();
            }
        } catch (IOException e) {
            System.err.println("Error closing server: " + e.getMessage());
        }
    }

    /**
     * The main method to start the PokerServer.
     *
     * @param args command-line arguments.
     */
    public static void main(String[] args) {
        PokerServer server = new PokerServer(8080);
        server.start();
    }
}

