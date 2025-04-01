package pl.pz1.poker.evaluator;

import org.junit.jupiter.api.Test;
import pl.pz1.poker.gameplay.*;
import pl.pz1.poker.util.parser.Hands;

import java.nio.channels.SocketChannel;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class HandEvaluatorTest {
    HandEvaluator handEvaluator = new HandEvaluator();


    @Test
    void complexRoyalFlushTest() {
        List<Card.Rank> ranks = new ArrayList<>();
        List<Card.Suit> suits1 = new ArrayList<>();
        List<Card.Suit> suits2 = new ArrayList<>();
        List<Card> cards1 = new ArrayList<>();
        List<Card> cards2 = new ArrayList<>();
        Player player1 = new Player(mock(SocketChannel.class));
        Player player2 = new Player(mock(SocketChannel.class));

        ranks.addAll(Arrays.asList(Card.Rank.TEN, Card.Rank.JACK, Card.Rank.QUEEN, Card.Rank.KING, Card.Rank.ACE));
        suits1.addAll(Arrays.asList(Card.Suit.CLUBS, Card.Suit.CLUBS, Card.Suit.CLUBS, Card.Suit.CLUBS, Card.Suit.CLUBS));
        suits2.addAll(Arrays.asList(Card.Suit.HEARTS, Card.Suit.HEARTS, Card.Suit.HEARTS, Card.Suit.HEARTS, Card.Suit.HEARTS));
        for (int i = 0; i < 5; i++) {
            cards1.add(new Card(ranks.get(i), suits1.get(i)));
            cards2.add(new Card(ranks.get(i), suits2.get(i)));
        }
        player1.updateHand(cards1);
        player1.setPlayerID(1);
        player2.updateHand(cards2);
        player2.setPlayerID(2);

        assertEquals(Hands.ROYAL_FLUSH.getValue(), handEvaluator.evaluateHand(player1));
        List<Integer> result = handEvaluator.evaluateHands(new ArrayList<>(Arrays.asList(player1, player2)));
        assertEquals(2, result.size());
    }


    @Test
    void complexStraightFlushTest() {
        List<Card.Rank> ranks1 = new ArrayList<>();
        List<Card.Rank> ranks2 = new ArrayList<>();
        List<Card.Suit> suits1 = new ArrayList<>();
        List<Card.Suit> suits2 = new ArrayList<>();
        List<Card> cards1 = new ArrayList<>();
        List<Card> cards2 = new ArrayList<>();
        Player player1 = new Player(mock(SocketChannel.class));
        Player player2 = new Player(mock(SocketChannel.class));

        ranks1.addAll(Arrays.asList(Card.Rank.TEN, Card.Rank.JACK, Card.Rank.QUEEN, Card.Rank.KING, Card.Rank.NINE));
        suits1.addAll(Arrays.asList(Card.Suit.CLUBS, Card.Suit.CLUBS, Card.Suit.CLUBS, Card.Suit.CLUBS, Card.Suit.CLUBS));
        ranks2.addAll(Arrays.asList(Card.Rank.TEN, Card.Rank.JACK, Card.Rank.QUEEN, Card.Rank.EIGHT, Card.Rank.NINE));
        suits2.addAll(Arrays.asList(Card.Suit.HEARTS, Card.Suit.HEARTS, Card.Suit.HEARTS, Card.Suit.HEARTS, Card.Suit.HEARTS));
        for (int i = 0; i < 5; i++) {
            cards1.add(new Card(ranks1.get(i), suits1.get(i)));
            cards2.add(new Card(ranks2.get(i), suits2.get(i)));
        }
        player1.updateHand(cards1);
        player1.setPlayerID(1);
        player2.updateHand(cards2);
        player2.setPlayerID(2);

        assertEquals(Hands.STRAIGHT_FLUSH.getValue(), handEvaluator.evaluateHand(player1));
        List<Integer> result = handEvaluator.evaluateHands(new ArrayList<>(Arrays.asList(player1, player2)));
        assertEquals(1, result.size());
        assertEquals(1, result.get(0));
    }



    @Test
    void complexQuadsTest() {
        List<Card.Rank> ranks1 = new ArrayList<>();
        List<Card.Rank> ranks2 = new ArrayList<>();
        List<Card.Suit> suits1 = new ArrayList<>();
        List<Card.Suit> suits2 = new ArrayList<>();
        List<Card> cards1 = new ArrayList<>();
        List<Card> cards2 = new ArrayList<>();
        Player player1 = new Player(mock(SocketChannel.class));
        Player player2 = new Player(mock(SocketChannel.class));

        ranks1.addAll(Arrays.asList(Card.Rank.TEN, Card.Rank.TEN, Card.Rank.TEN, Card.Rank.TEN, Card.Rank.ACE));
        suits1.addAll(Arrays.asList(Card.Suit.CLUBS, Card.Suit.SPADES, Card.Suit.DIAMONDS, Card.Suit.HEARTS, Card.Suit.CLUBS));
        ranks2.addAll(Arrays.asList(Card.Rank.JACK, Card.Rank.JACK, Card.Rank.JACK, Card.Rank.JACK, Card.Rank.NINE));
        suits2.addAll(Arrays.asList(Card.Suit.HEARTS, Card.Suit.CLUBS, Card.Suit.DIAMONDS, Card.Suit.SPADES, Card.Suit.HEARTS));
        for (int i = 0; i < 5; i++) {
            cards1.add(new Card(ranks1.get(i), suits1.get(i)));
            cards2.add(new Card(ranks2.get(i), suits2.get(i)));
        }
        player1.updateHand(cards1);
        player1.setPlayerID(1);
        player2.updateHand(cards2);
        player2.setPlayerID(2);

        assertEquals(Hands.QUADS.getValue(), handEvaluator.evaluateHand(player1));
        List<Integer> result = handEvaluator.evaluateHands(new ArrayList<>(Arrays.asList(player1, player2)));
        assertEquals(1, result.size());
        assertEquals(2, result.get(0));
    }




    @Test
    void complexFullHouseTest() {
        List<Card.Rank> ranks1 = new ArrayList<>();
        List<Card.Rank> ranks2 = new ArrayList<>();
        List<Card.Suit> suits1 = new ArrayList<>();
        List<Card.Suit> suits2 = new ArrayList<>();
        List<Card> cards1 = new ArrayList<>();
        List<Card> cards2 = new ArrayList<>();
        Player player1 = new Player(mock(SocketChannel.class));
        Player player2 = new Player(mock(SocketChannel.class));

        ranks1.addAll(Arrays.asList(Card.Rank.TEN, Card.Rank.ACE, Card.Rank.ACE, Card.Rank.TEN, Card.Rank.ACE));
        suits1.addAll(Arrays.asList(Card.Suit.CLUBS, Card.Suit.SPADES, Card.Suit.DIAMONDS, Card.Suit.HEARTS, Card.Suit.CLUBS));
        ranks2.addAll(Arrays.asList(Card.Rank.JACK, Card.Rank.JACK, Card.Rank.JACK, Card.Rank.NINE, Card.Rank.NINE));
        suits2.addAll(Arrays.asList(Card.Suit.HEARTS, Card.Suit.CLUBS, Card.Suit.DIAMONDS, Card.Suit.SPADES, Card.Suit.HEARTS));
        for (int i = 0; i < 5; i++) {
            cards1.add(new Card(ranks1.get(i), suits1.get(i)));
            cards2.add(new Card(ranks2.get(i), suits2.get(i)));
        }
        player1.updateHand(cards1);
        player1.setPlayerID(1);
        player2.updateHand(cards2);
        player2.setPlayerID(2);

        assertEquals(Hands.FULL_HOUSE.getValue(), handEvaluator.evaluateHand(player1));
        List<Integer> result = handEvaluator.evaluateHands(new ArrayList<>(Arrays.asList(player1, player2)));
        assertEquals(1, result.size());
        assertEquals(1, result.get(0));
    }



    @Test
    void complexFlushTest() {
        List<Card.Rank> ranks1 = new ArrayList<>();
        List<Card.Rank> ranks2 = new ArrayList<>();
        List<Card.Suit> suits1 = new ArrayList<>();
        List<Card.Suit> suits2 = new ArrayList<>();
        List<Card> cards1 = new ArrayList<>();
        List<Card> cards2 = new ArrayList<>();
        Player player1 = new Player(mock(SocketChannel.class));
        Player player2 = new Player(mock(SocketChannel.class));

        ranks1.addAll(Arrays.asList(Card.Rank.TWO, Card.Rank.TEN, Card.Rank.EIGHT, Card.Rank.KING, Card.Rank.SIX));
        suits1.addAll(Arrays.asList(Card.Suit.CLUBS, Card.Suit.CLUBS, Card.Suit.CLUBS, Card.Suit.CLUBS, Card.Suit.CLUBS));
        ranks2.addAll(Arrays.asList(Card.Rank.JACK, Card.Rank.TWO, Card.Rank.TEN, Card.Rank.SEVEN, Card.Rank.ACE));
        suits2.addAll(Arrays.asList(Card.Suit.HEARTS, Card.Suit.HEARTS, Card.Suit.HEARTS, Card.Suit.HEARTS, Card.Suit.HEARTS));
        for (int i = 0; i < 5; i++) {
            cards1.add(new Card(ranks1.get(i), suits1.get(i)));
            cards2.add(new Card(ranks2.get(i), suits2.get(i)));
        }
        player1.updateHand(cards1);
        player1.setPlayerID(1);
        player2.updateHand(cards2);
        player2.setPlayerID(2);

        assertEquals(Hands.FLUSH.getValue(), handEvaluator.evaluateHand(player1));
        List<Integer> result = handEvaluator.evaluateHands(new ArrayList<>(Arrays.asList(player1, player2)));
        assertEquals(1, result.size());
        assertEquals(2, result.get(0));


        ranks1.clear();
        cards1.clear();
        ranks1.addAll(Arrays.asList(Card.Rank.ACE, Card.Rank.JACK, Card.Rank.TEN, Card.Rank.TWO, Card.Rank.SEVEN));
        for (int i = 0; i < 5; i++) {
            cards1.add(new Card(ranks1.get(i), suits1.get(i)));
        }
        player1.updateHand(cards1);
        result = handEvaluator.evaluateHands(new ArrayList<>(Arrays.asList(player1, player2)));
        assertEquals(2, result.size());
    }



    @Test
    void complexStraightTest() {
        List<Card.Rank> ranks1 = new ArrayList<>();
        List<Card.Rank> ranks2 = new ArrayList<>();
        List<Card.Suit> suits1 = new ArrayList<>();
        List<Card.Suit> suits2 = new ArrayList<>();
        List<Card> cards1 = new ArrayList<>();
        List<Card> cards2 = new ArrayList<>();
        Player player1 = new Player(mock(SocketChannel.class));
        Player player2 = new Player(mock(SocketChannel.class));

        ranks1.addAll(Arrays.asList(Card.Rank.TWO, Card.Rank.THREE, Card.Rank.FOUR, Card.Rank.FIVE, Card.Rank.SIX));
        suits1.addAll(Arrays.asList(Card.Suit.CLUBS, Card.Suit.SPADES, Card.Suit.CLUBS, Card.Suit.CLUBS, Card.Suit.DIAMONDS));
        ranks2.addAll(Arrays.asList(Card.Rank.JACK, Card.Rank.QUEEN, Card.Rank.TEN, Card.Rank.NINE, Card.Rank.KING));
        suits2.addAll(Arrays.asList(Card.Suit.SPADES, Card.Suit.HEARTS, Card.Suit.DIAMONDS, Card.Suit.HEARTS, Card.Suit.SPADES));
        for (int i = 0; i < 5; i++) {
            cards1.add(new Card(ranks1.get(i), suits1.get(i)));
            cards2.add(new Card(ranks2.get(i), suits2.get(i)));
        }
        player1.updateHand(cards1);
        player1.setPlayerID(1);
        player2.updateHand(cards2);
        player2.setPlayerID(2);

        assertEquals(Hands.STRAIGHT.getValue(), handEvaluator.evaluateHand(player1));
        List<Integer> result = handEvaluator.evaluateHands(new ArrayList<>(Arrays.asList(player1, player2)));
        assertEquals(1, result.size());
        assertEquals(2, result.get(0));


        ranks1.clear();
        cards1.clear();
        ranks1.addAll(Arrays.asList(Card.Rank.JACK, Card.Rank.QUEEN, Card.Rank.TEN, Card.Rank.NINE, Card.Rank.KING));
        for (int i = 0; i < 5; i++) {
            cards1.add(new Card(ranks1.get(i), suits1.get(i)));
        }
        player1.updateHand(cards1);
        result = handEvaluator.evaluateHands(new ArrayList<>(Arrays.asList(player1, player2)));
        assertEquals(2, result.size());
    }



    @Test
    void complexThreeOfAKindTest() {
        List<Card.Rank> ranks1 = new ArrayList<>();
        List<Card.Rank> ranks2 = new ArrayList<>();
        List<Card.Suit> suits1 = new ArrayList<>();
        List<Card.Suit> suits2 = new ArrayList<>();
        List<Card> cards1 = new ArrayList<>();
        List<Card> cards2 = new ArrayList<>();
        Player player1 = new Player(mock(SocketChannel.class));
        Player player2 = new Player(mock(SocketChannel.class));

        ranks1.addAll(Arrays.asList(Card.Rank.KING, Card.Rank.TEN, Card.Rank.KING, Card.Rank.KING, Card.Rank.SIX));
        suits1.addAll(Arrays.asList(Card.Suit.CLUBS, Card.Suit.HEARTS, Card.Suit.HEARTS, Card.Suit.DIAMONDS, Card.Suit.HEARTS));
        ranks2.addAll(Arrays.asList(Card.Rank.JACK, Card.Rank.JACK, Card.Rank.JACK, Card.Rank.NINE, Card.Rank.EIGHT));
        suits2.addAll(Arrays.asList(Card.Suit.SPADES, Card.Suit.HEARTS, Card.Suit.DIAMONDS, Card.Suit.HEARTS, Card.Suit.SPADES));
        for (int i = 0; i < 5; i++) {
            cards1.add(new Card(ranks1.get(i), suits1.get(i)));
            cards2.add(new Card(ranks2.get(i), suits2.get(i)));
        }
        player1.updateHand(cards1);
        player1.setPlayerID(1);
        player2.updateHand(cards2);
        player2.setPlayerID(2);

        assertEquals(Hands.THREE_OF_A_KIND.getValue(), handEvaluator.evaluateHand(player1));
        List<Integer> result = handEvaluator.evaluateHands(new ArrayList<>(Arrays.asList(player1, player2)));
        assertEquals(1, result.size());
        assertEquals(1, result.get(0));
    }



    @Test
    void complexTwoPairsTest() {
        List<Card.Rank> ranks1 = new ArrayList<>();
        List<Card.Rank> ranks2 = new ArrayList<>();
        List<Card.Suit> suits1 = new ArrayList<>();
        List<Card.Suit> suits2 = new ArrayList<>();
        List<Card> cards1 = new ArrayList<>();
        List<Card> cards2 = new ArrayList<>();
        Player player1 = new Player(mock(SocketChannel.class));
        Player player2 = new Player(mock(SocketChannel.class));

        ranks1.addAll(Arrays.asList(Card.Rank.TEN, Card.Rank.TEN, Card.Rank.EIGHT, Card.Rank.KING, Card.Rank.KING));
        suits1.addAll(Arrays.asList(Card.Suit.CLUBS, Card.Suit.HEARTS, Card.Suit.SPADES, Card.Suit.CLUBS, Card.Suit.DIAMONDS));
        ranks2.addAll(Arrays.asList(Card.Rank.JACK, Card.Rank.JACK, Card.Rank.NINE, Card.Rank.NINE, Card.Rank.SEVEN));
        suits2.addAll(Arrays.asList(Card.Suit.SPADES, Card.Suit.HEARTS, Card.Suit.DIAMONDS, Card.Suit.HEARTS, Card.Suit.SPADES));
        for (int i = 0; i < 5; i++) {
            cards1.add(new Card(ranks1.get(i), suits1.get(i)));
            cards2.add(new Card(ranks2.get(i), suits2.get(i)));
        }
        player1.updateHand(cards1);
        player1.setPlayerID(1);
        player2.updateHand(cards2);
        player2.setPlayerID(2);

        assertEquals(Hands.TWO_PAIRS.getValue(), handEvaluator.evaluateHand(player1));
        List<Integer> result = handEvaluator.evaluateHands(new ArrayList<>(Arrays.asList(player1, player2)));
        assertEquals(1, result.size());
        assertEquals(1, result.get(0));


        ranks1.clear();
        suits1.clear();
        cards1.clear();
        ranks1.addAll(Arrays.asList(Card.Rank.JACK, Card.Rank.JACK, Card.Rank.NINE, Card.Rank.NINE, Card.Rank.TWO));
        suits1.addAll(Arrays.asList(Card.Suit.CLUBS, Card.Suit.DIAMONDS, Card.Suit.SPADES, Card.Suit.CLUBS, Card.Suit.DIAMONDS));
        for (int i = 0; i < 5; i++) {
            cards1.add(new Card(ranks1.get(i), suits1.get(i)));
        }
        player1.updateHand(cards1);
        result = handEvaluator.evaluateHands(new ArrayList<>(Arrays.asList(player1, player2)));
        assertEquals(1, result.size());
        assertEquals(2, result.get(0));
    }



    @Test
    void complexOnePairTest() {
        List<Card.Rank> ranks1 = new ArrayList<>();
        List<Card.Rank> ranks2 = new ArrayList<>();
        List<Card.Suit> suits1 = new ArrayList<>();
        List<Card.Suit> suits2 = new ArrayList<>();
        List<Card> cards1 = new ArrayList<>();
        List<Card> cards2 = new ArrayList<>();
        Player player1 = new Player(mock(SocketChannel.class));
        Player player2 = new Player(mock(SocketChannel.class));

        ranks1.addAll(Arrays.asList(Card.Rank.TWO, Card.Rank.TEN, Card.Rank.EIGHT, Card.Rank.KING, Card.Rank.KING));
        suits1.addAll(Arrays.asList(Card.Suit.SPADES, Card.Suit.HEARTS, Card.Suit.CLUBS, Card.Suit.CLUBS, Card.Suit.DIAMONDS));
        ranks2.addAll(Arrays.asList(Card.Rank.JACK, Card.Rank.JACK, Card.Rank.NINE, Card.Rank.TWO, Card.Rank.SEVEN));
        suits2.addAll(Arrays.asList(Card.Suit.SPADES, Card.Suit.HEARTS, Card.Suit.DIAMONDS, Card.Suit.HEARTS, Card.Suit.SPADES));
        for (int i = 0; i < 5; i++) {
            cards1.add(new Card(ranks1.get(i), suits1.get(i)));
            cards2.add(new Card(ranks2.get(i), suits2.get(i)));
        }
        player1.updateHand(cards1);
        player1.setPlayerID(1);
        player2.updateHand(cards2);
        player2.setPlayerID(2);

        assertEquals(Hands.ONE_PAIR.getValue(), handEvaluator.evaluateHand(player1));
        List<Integer> result = handEvaluator.evaluateHands(new ArrayList<>(Arrays.asList(player1, player2)));
        assertEquals(1, result.size());
        assertEquals(1, result.get(0));


        ranks1.clear();
        cards1.clear();
        ranks1.addAll(Arrays.asList(Card.Rank.TWO, Card.Rank.TEN, Card.Rank.EIGHT, Card.Rank.JACK, Card.Rank.JACK));
        for (int i = 0; i < 5; i++) {
            cards1.add(new Card(ranks1.get(i), suits1.get(i)));
        }
        player1.updateHand(cards1);
        result = handEvaluator.evaluateHands(new ArrayList<>(Arrays.asList(player1, player2)));
        assertEquals(1, result.size());
        assertEquals(1, result.get(0));
    }



    @Test
    void complexWithoutAnyRankTest() {
        List<Card.Rank> ranks1 = new ArrayList<>();
        List<Card.Rank> ranks2 = new ArrayList<>();
        List<Card.Suit> suits1 = new ArrayList<>();
        List<Card.Suit> suits2 = new ArrayList<>();
        List<Card> cards1 = new ArrayList<>();
        List<Card> cards2 = new ArrayList<>();
        Player player1 = new Player(mock(SocketChannel.class));
        Player player2 = new Player(mock(SocketChannel.class));

        ranks1.addAll(Arrays.asList(Card.Rank.TWO, Card.Rank.TEN, Card.Rank.EIGHT, Card.Rank.KING, Card.Rank.FIVE));
        suits1.addAll(Arrays.asList(Card.Suit.SPADES, Card.Suit.HEARTS, Card.Suit.CLUBS, Card.Suit.CLUBS, Card.Suit.DIAMONDS));
        ranks2.addAll(Arrays.asList(Card.Rank.JACK, Card.Rank.FOUR, Card.Rank.NINE, Card.Rank.TWO, Card.Rank.SEVEN));
        suits2.addAll(Arrays.asList(Card.Suit.SPADES, Card.Suit.HEARTS, Card.Suit.DIAMONDS, Card.Suit.HEARTS, Card.Suit.SPADES));
        for (int i = 0; i < 5; i++) {
            cards1.add(new Card(ranks1.get(i), suits1.get(i)));
            cards2.add(new Card(ranks2.get(i), suits2.get(i)));
        }
        player1.updateHand(cards1);
        player1.setPlayerID(1);
        player2.updateHand(cards2);
        player2.setPlayerID(2);

        assertEquals(0, handEvaluator.evaluateHand(player1));
        List<Integer> result = handEvaluator.evaluateHands(new ArrayList<>(Arrays.asList(player1, player2)));
        assertEquals(1, result.size());
        assertEquals(1, result.get(0));


        ranks1.clear();
        cards1.clear();
        ranks1.addAll(Arrays.asList(Card.Rank.TWO, Card.Rank.TEN, Card.Rank.EIGHT, Card.Rank.JACK, Card.Rank.FIVE));
        for (int i = 0; i < 5; i++) {
            cards1.add(new Card(ranks1.get(i), suits1.get(i)));
        }
        player1.updateHand(cards1);
        result = handEvaluator.evaluateHands(new ArrayList<>(Arrays.asList(player1, player2)));
        assertEquals(1, result.size());
        assertEquals(1, result.get(0));
    }
}
