package pl.pz1.poker.server;


import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.ByteBuffer;
import org.mockito.ArgumentCaptor;

import pl.pz1.poker.gameplay.Player;
import pl.pz1.poker.util.parser.ClientMoveParser;
import java.io.IOException;
import java.nio.channels.ServerSocketChannel;



class Main1Test {
    @org.junit.jupiter.api.Nested
    class PokerServerTests {
        @Test
        void testHandleRead() throws IOException {
            PokerServer server = new PokerServer(8080);
            SocketChannel mockClientChannel = mock(SocketChannel.class);
            SelectionKey mockSelectionKey = mock(SelectionKey.class);
            ClientMoveParser clientMoveParser = new ClientMoveParser();

            // Mockowanie kanału do odczytu
            when(mockSelectionKey.channel()).thenReturn(mockClientChannel);

            String mockMessage = clientMoveParser.parse("JOIN", 0, 0);
            ByteBuffer buffer = ByteBuffer.wrap(mockMessage.getBytes());

            when(mockClientChannel.read(any(ByteBuffer.class))).thenAnswer(invocation -> {
                ByteBuffer argBuffer = invocation.getArgument(0);
                argBuffer.put(buffer);
                return mockMessage.length();
            });

            // Wywołanie metody
            server.handleRead(mockSelectionKey);

            // Weryfikacja odczytu danych z kanału
            verify(mockClientChannel).read(any(ByteBuffer.class));
        }

        @Test
        void testProcessingMessages() throws IOException {
            PokerServer server = new PokerServer(8080);
            SocketChannel mockClientChannel = mock(SocketChannel.class);

            String testMessage = "Test message";

            // Wywołanie metody
            server.sendMessage(mockClientChannel, testMessage);

            // Weryfikacja, czy wiadomość została wysłana
            ArgumentCaptor<ByteBuffer> bufferCaptor = ArgumentCaptor.forClass(ByteBuffer.class);
            verify(mockClientChannel).write(bufferCaptor.capture());

            ByteBuffer capturedBuffer = bufferCaptor.getValue();
            String sentMessage = new String(capturedBuffer.array()).trim();

            assertEquals(testMessage, sentMessage);
        }

        @Test
        void testHandleAccept() throws IOException {
            PokerServer server = new PokerServer(8080);
            ServerSocketChannel mockServerSocketChannel = mock(ServerSocketChannel.class);
            SocketChannel mockClientChannel = mock(SocketChannel.class);

            // Mockowanie akceptacji klienta
            when(mockServerSocketChannel.accept()).thenReturn(mockClientChannel);
            when(mockClientChannel.getRemoteAddress()).thenReturn(new java.net.InetSocketAddress("localhost", 12345));

            server.setServerSocketChannel(mockServerSocketChannel);

            // Wywołanie
            server.handleAccept();

            // Weryfikacja
            verify(mockClientChannel).configureBlocking(false);
            verify(mockClientChannel).register(any(), eq(SelectionKey.OP_READ));
        }

        @Test
        void testHandleRead_ClientDisconnects() throws IOException {
            PokerServer server = new PokerServer(8080);
            SocketChannel mockClientChannel = mock(SocketChannel.class);
            SelectionKey mockKey = mock(SelectionKey.class);

            when(mockKey.channel()).thenReturn(mockClientChannel);
            when(mockClientChannel.read(any(ByteBuffer.class))).thenReturn(-1); // Klient się rozłącza

            server.players.put(mockClientChannel, new Player(mockClientChannel));

            server.handleRead(mockKey);

            // Sprawdzamy, że gracz został usunięty
            assertTrue(server.players.isEmpty());
            verify(mockClientChannel).close();
        }


        @Test
        void testServerClose() throws IOException {
            PokerServer server = new PokerServer(8080);
            ServerSocketChannel mockServerSocketChannel = mock(ServerSocketChannel.class);
            Selector mockSelector = mock(Selector.class);

            server.setServerSocketChannel(mockServerSocketChannel);

            server.close();

            verify(mockServerSocketChannel, times(1)).close();
            verify(mockSelector, never()).close(); // Selector był null i nie został ustawiony
        }
    }
}





