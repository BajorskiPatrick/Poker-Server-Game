package pl.pz1.poker.util.parser;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CommonsTest {
    @Test
    void clientMoveParserTest() {
        ClientMoveParser clientMoveParser = new ClientMoveParser();

        String result = clientMoveParser.parse("JOIN", 0, 0);
        String expected = """
                ID_GRY:0
                ID_GRACZA:0
                RODZAJ_RUCHU:JOIN
                PARAMETRY_RUCHU:""";
        assertEquals(expected, result);  // Możesz dostosować wynik w zależności od implementacji

        result = clientMoveParser.parse("CALL", 0, 0);
        expected = """
                ID_GRY:0
                ID_GRACZA:0
                RODZAJ_RUCHU:CALL
                PARAMETRY_RUCHU:""";
        assertEquals(expected, result);

        result = clientMoveParser.parse("EXIT", 0, 0);
        expected = """
                ID_GRY:0
                ID_GRACZA:0
                RODZAJ_RUCHU:EXIT
                PARAMETRY_RUCHU:""";
        assertEquals(expected, result);

        result = clientMoveParser.parse("STATUS", 0, 0);
        expected = """
                ID_GRY:0
                ID_GRACZA:0
                RODZAJ_RUCHU:STATUS
                PARAMETRY_RUCHU:""";
        assertEquals(expected, result);

        result = clientMoveParser.parse("FOLD", 0, 0);
        expected = """
                ID_GRY:0
                ID_GRACZA:0
                RODZAJ_RUCHU:FOLD
                PARAMETRY_RUCHU:""";
        assertEquals(expected, result);

        result = clientMoveParser.parse("EXCHANGE", 0, 0);
        expected = """
                ID_GRY:0
                ID_GRACZA:0
                RODZAJ_RUCHU:EXCHANGE
                PARAMETRY_RUCHU:""";
        assertEquals(expected, result);

        result = clientMoveParser.parse("EXCHANGE J-C", 0, 0);
        expected = """
                ID_GRY:0
                ID_GRACZA:0
                RODZAJ_RUCHU:EXCHANGE
                PARAMETRY_RUCHU:J-C""";
        assertEquals(expected, result);

        result = clientMoveParser.parse("RAISE 20", 0, 0);
        expected = """
                ID_GRY:0
                ID_GRACZA:0
                RODZAJ_RUCHU:RAISE
                PARAMETRY_RUCHU:20""";
        assertEquals(expected, result);

        result = clientMoveParser.parse("NEW_GAME 2, 10", 0, 0);
        expected = """
                ID_GRY:0
                ID_GRACZA:0
                RODZAJ_RUCHU:NEW_GAME
                PARAMETRY_RUCHU:2, 10""";
        assertEquals(expected, result);

        result = clientMoveParser.parse("TEST", 0, 0);
        expected = "Niepoprawny ruch!";
        assertEquals(expected, result);
    }


    @Test
    void ServerMoveParserTest() {
        ServerMoveParser serverMoveParser = new ServerMoveParser();
        Map<String, String> result = serverMoveParser.parse("""
                ID_GRY:0
                ID_GRACZA:0
                RODZAJ_RUCHU:RAISE
                PARAMETRY_RUCHU:20""");
        Map<String, String> expected = new HashMap<>();
        expected.put("ID_GRY", "0");
        expected.put("ID_GRACZA", "0");
        expected.put("RODZAJ_RUCHU", "RAISE");
        expected.put("PARAMETRY_RUCHU", "20");

        assertEquals(expected, result);
    }



    @Test
    void MovesEnumTest() {
        assertFalse(Moves.contains("Test"));
        assertTrue(Moves.contains("CALL"));
        assertTrue(Moves.contains("NEW_GAME"));
    }



    @Test
    void HandsEnumTest() {
        assertEquals(9, Hands.ROYAL_FLUSH.getValue());
        assertEquals(8, Hands.STRAIGHT_FLUSH.getValue());
        assertEquals(7, Hands.QUADS.getValue());
        assertEquals(6, Hands.FULL_HOUSE.getValue());
        assertEquals(5, Hands.FLUSH.getValue());
        assertEquals(4, Hands.STRAIGHT.getValue());
        assertEquals(3, Hands.THREE_OF_A_KIND.getValue());
        assertEquals(2, Hands.TWO_PAIRS.getValue());
        assertEquals(1, Hands.ONE_PAIR.getValue());
    }
}
