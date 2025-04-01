package pl.pz1.poker.util.parser;

import java.util.HashMap;
import java.util.Map;

/**
 * The ServerMoveParser class is responsible for parsing messages received by the server.
 * It converts a formatted string message into a map of key-value pairs.
 */
public class ServerMoveParser {

    /**
     * Parses a string message into a map of key-value pairs.
     *
     * The message is expected to have lines in the format "key:value".
     *
     * @param message the message to be parsed.
     * @return a map containing key-value pairs extracted from the message.
     * @throws ArrayIndexOutOfBoundsException if a line does not contain a colon.
     */
    public Map<String, String> parse(String message) {
        Map<String, String> map = new HashMap<>();
        String[] lines = message.split("\n");
        for (String line : lines) {
            String[] tokens = line.split(":", 2);
            map.put(tokens[0], tokens[1]);
        }
        return map;
    }
}

