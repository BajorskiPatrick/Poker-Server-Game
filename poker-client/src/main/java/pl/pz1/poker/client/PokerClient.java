package pl.pz1.poker.client;

import pl.pz1.poker.util.parser.ClientMoveParser;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.net.InetSocketAddress;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * The PokerClient class represents the client-side implementation of the poker game.
 * It handles communication with the server, processes player moves, and manages game state.
 */
public class PokerClient {

    /**
     * The socket channel used to connect to the server.
     */
    private SocketChannel client;

    /**
     * The buffer used for reading and writing data to the server.
     */
    private final ByteBuffer buffer;

    /**
     * The server address to which the client connects.
     */
    private final String serverAddress;

    /**
     * The port number of the server.
     */
    private final int port;

    /**
     * The parser used to validate and format client moves.
     */
    private final ClientMoveParser clientMoveParser = new ClientMoveParser();

    /**
     * The ID of the current game session.
     */
    private int gameID = 0;

    /**
     * The ID of the player within the game.
     */
    private int playerID = 0;

    /**
     * Constructs a PokerClient with the specified server address and port.
     *
     * @param serverAddress the server's address.
     * @param port          the server's port number.
     */
    public PokerClient(String serverAddress, int port) {
        this.serverAddress = serverAddress;
        this.port = port;
        this.buffer = ByteBuffer.allocate(1024);
    }

    /**
     * Starts the client by connecting to the server and initiating communication.
     */
    public void start() {
        try {
            client = SocketChannel.open(new InetSocketAddress(serverAddress, port));
            System.out.println("Witamy w serwerze Pokera!");
            communicateWithServer();
        } catch (IOException e) {
            System.err.println("Połaczenie z serwerem nie powiodło się: " + e.getMessage());
        }
    }

    /**
     * Handles communication with the server by sending and receiving messages.
     */
    public void communicateWithServer() {
        try (Scanner scanner = new Scanner(System.in)) {
            buffer.clear();
            client.read(buffer);
            String initialResponse = new String(buffer.array(), 0, buffer.position()).trim();
            System.out.println(initialResponse);

            while (true) {
                String move;
                while (true) {
                    System.out.println("Wprowadź swój ruch: ");
                    move = scanner.nextLine().toUpperCase().trim();
                    move = clientMoveParser.parse(move, this.gameID, this.playerID);

                    if (!move.equals("Niepoprawny ruch!")) {
                        break;
                    }
                    System.out.println(move);
                }
                buffer.clear();
                buffer.put(move.getBytes());
                buffer.flip();
                client.write(buffer);

                buffer.clear();
                client.read(buffer);
                String moveResponse = new String(buffer.array(), 0, buffer.position()).trim();

                System.out.println(moveResponse);

                if (moveResponse.equals("Do zobaczenia!")) {
                    break;
                } else if (moveResponse.startsWith("Dołączono")) {
                    String regex = "ID: (\\d+)";
                    Pattern pattern = Pattern.compile(regex);
                    Matcher matcher = pattern.matcher(moveResponse);

                    String[] ids = new String[2];
                    int index = 0;

                    while (matcher.find() && index < 2) {
                        ids[index] = matcher.group(1);
                        index++;
                    }

                    this.gameID = Integer.parseInt(ids[0]);
                    this.playerID = Integer.parseInt(ids[1]);
                } else if (moveResponse.contains("Gra zakończyła się!")) {
                    this.gameID = 0;
                    this.playerID = 0;
                }
            }
        } catch (IOException e) {
            System.err.println("Wystąpił błąd w komunikacji z serwerem: " + e.getMessage());
        } finally {
            close();
        }
    }

    /**
     * Closes the connection to the server and releases resources.
     */
    protected void close() {
        try {
            if (client != null) {
                client.close();
            }
        } catch (IOException e) {
            System.out.println("Błąd przy zamykaniu połączenia klienta: " + e.getMessage());
        }
    }

    /**
     * The main method to start the PokerClient.
     *
     * @param args command-line arguments.
     */
    public static void main(String[] args) {
        PokerClient client = new PokerClient("localhost", 8080);
        client.start();
    }
}

