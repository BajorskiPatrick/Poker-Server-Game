package pl.pz1.poker.evaluator;

import pl.pz1.poker.gameplay.Card;
import pl.pz1.poker.gameplay.Player;
import pl.pz1.poker.util.parser.Hands;

import java.util.*;

/**
 * Class which is used to determine values of players hands and as a result,
 * who has the strongest cards and won the game
 */
public class HandEvaluator {

    /**
     * Main metod of class which evaluate every player hand value and return
     * list of ID's of players whose hand is the strongest.
     * @param players list of players to compare
     * @return result of comparison and evaluation
     */
    public List<Integer> evaluateHands(List<Player> players) {
        Map<Integer, Integer> results = new HashMap<>();

        for (Player player : players) {
            int result = evaluateHand(player);
            if (result != 0) {
                results.put(player.getPlayerID(), result);
            }
        }

        int winner;
        if (results.isEmpty()) {
            winner = compareCards(players, 0);
        }
        else {
            winner = determineWinner(players, results);
        }


        return prepareResult(players, winner);
    }


    private List<Integer> prepareResult(List<Player> players, int winner) {
        List<Integer> result = new ArrayList<>();
        if (winner != 0) {
            result.add(winner);
        } else {
            for (Player player : players) {
                result.add(player.getPlayerID());
            }
        }
        return result;
    }


    private int determineWinner(List<Player> players, Map<Integer, Integer> results) {
        int maxHand = 0;
        int maxID = 0;
        List<Integer> sameHandIDs = new ArrayList<>();

        for (Map.Entry<Integer, Integer> entry : results.entrySet()) {
            if (entry.getValue() > maxHand) {
                maxHand = entry.getValue();
                maxID = entry.getKey();
                sameHandIDs.clear();
                sameHandIDs.add(maxID);
            } else if (entry.getValue() == maxHand) {
                sameHandIDs.add(entry.getKey());
            }
        }

        if (sameHandIDs.size() == 1) return maxID;

        return resolveTie(players, sameHandIDs, maxHand);
    }


    private int resolveTie(List<Player> players, List<Integer> sameHandIDs, int maxHand) {
        List<Player> playersToCompare = new ArrayList<>();
        for (Player player : players) {
            if (sameHandIDs.contains(player.getPlayerID())) {
                playersToCompare.add(player);
            }
        }
        return compareCards(playersToCompare, maxHand);
    }


    protected int evaluateHand(Player player) {
        List<Card> hand = player.getCopyOfHand();
        List<Card.Rank> ranks = new ArrayList<>();
        List<Card.Suit> suits = new ArrayList<>();
        for (Card card : hand) {
            ranks.add(card.getRank());
            suits.add(card.getSuit());
        }
        Card.Rank.sortRanks(ranks);

        if (checkForRoyalFlush(ranks, suits)) return Hands.ROYAL_FLUSH.getValue();
        if (checkForStraightFlush(ranks, suits)) return Hands.STRAIGHT_FLUSH.getValue();
        if (checkForQuads(ranks)) return Hands.QUADS.getValue();
        if (checkForFullHouse(ranks)) return Hands.FULL_HOUSE.getValue();
        if (checkForFlush(ranks, suits)) return Hands.FLUSH.getValue();
        if (checkForStraight(ranks, suits)[0] && checkForStraight(ranks, suits)[1]) return Hands.STRAIGHT.getValue();
        if (checkForThreeOfAKind(ranks).size() == 3) return Hands.THREE_OF_A_KIND.getValue();
        if (checkForTwoPairs(ranks)) return Hands.TWO_PAIRS.getValue();
        if (checkForOnePair(ranks)) return Hands.ONE_PAIR.getValue();

        return 0; // Brak układu
    }



    private int compareWithoutThreesAndPairs(int handNumber, Map<Integer, List<Card.Rank>> playersRanks, List<Player> players) {
        int winner = -1;

        for (int i = 0; i < players.size(); i++) {
            Map<Integer, Card.Rank> highest = new HashMap<>();
            for (Map.Entry<Integer, List<Card.Rank>> entry : playersRanks.entrySet()) {
                highest.put(entry.getKey(), Card.Rank.getHighestRank(entry.getValue()));
            }
            Card.Rank theHighestOne = Card.Rank.getHighestRank(new ArrayList<>(highest.values()));
            Card.Rank theLowestOne = Card.Rank.getLowestRank(new ArrayList<>(highest.values()));

            if (theHighestOne.equals(theLowestOne) && ((playersRanks.size() == 2)
                    || handNumber == Hands.STRAIGHT_FLUSH.getValue() || handNumber == Hands.STRAIGHT.getValue())) {
                winner = 0;
                break;
            }
            else {
                List<Integer> toRemove = new ArrayList<>();
                for (Map.Entry<Integer, Card.Rank> map : highest.entrySet()) {
                    if (!map.getValue().equals(theHighestOne)) {
                        toRemove.add(map.getKey());
                    }
                }
                toRemove.forEach(playersRanks::remove);


                if (playersRanks.size() == 1) {
                    for (Player player : players) {
                        if (playersRanks.containsKey(player.getPlayerID())) {
                            winner = player.getPlayerID();
                            return winner;
                        }
                    }
                }
                else {
                    for (List<Card.Rank> list : playersRanks.values()) {
                        list.removeIf(rank -> rank.equals(theHighestOne));
                    }
                }

            }
        }

        return winner;
    }


    private int compareCards(List<Player> players, int handNumber) {
        Map<Integer, List<Card.Rank>> playersRanks = getPlayersRanks(players);
        int winner = -1;

        if (handNumber == Hands.ROYAL_FLUSH.getValue()) {
            winner = 0; // Podział wygranej między graczy z tym układem
        }
        else if (handNumber == Hands.STRAIGHT_FLUSH.getValue() || handNumber == Hands.FLUSH.getValue()
                || handNumber == Hands.STRAIGHT.getValue()) {

            winner = compareWithoutThreesAndPairs(handNumber, playersRanks, players);
        }
        else if (handNumber == Hands.QUADS.getValue()) {
            Map<Integer, Card.Rank> quadsRank = new HashMap<>();
            Map<Integer, Card.Rank> kickersRank = new HashMap<>();
            for (Map.Entry<Integer, List<Card.Rank>> entry : playersRanks.entrySet()) {
                quadsRank.put(entry.getKey(), entry.getValue().get(1));// pod indeksem 1 na pewno jest wartość z karety
                if (entry.getValue().get(1).compareTo(entry.getValue().get(0)) > 0) {
                    kickersRank.put(entry.getKey(), entry.getValue().get(0));
                }
                else {
                    kickersRank.put(entry.getKey(), entry.getValue().get(4));
                }
            }

            Card.Rank theHighestQuad = Card.Rank.getHighestRank(new ArrayList<>(quadsRank.values()));
            for (Map.Entry<Integer, Card.Rank> map : quadsRank.entrySet()) {
                if (!map.getValue().equals(theHighestQuad)) {
                    kickersRank.remove(map.getKey());
                    playersRanks.remove(map.getKey());
                }
            }

            if (kickersRank.size() == 1) {
                for (Player player : players) {
                    if (kickersRank.containsKey(player.getPlayerID())) {
                        winner = player.getPlayerID();
                        break;
                    }
                }
            }
            else {
                Card.Rank theHighestKicker = Card.Rank.getHighestRank(new ArrayList<>(kickersRank.values()));
                Card.Rank theLowestKicker = Card.Rank.getLowestRank(new ArrayList<>(kickersRank.values()));
                if (theHighestKicker.equals(theLowestKicker)) {
                    winner = 0;
                }
                else {
                    for (Map.Entry<Integer, Card.Rank> map : kickersRank.entrySet()) {
                        if (!map.getValue().equals(theHighestKicker)) {
                            playersRanks.remove(map.getKey());
                        }
                    }
                }
            }
        }
        else if (handNumber == Hands.FULL_HOUSE.getValue()) {
            Map<Integer, Card.Rank> threeRank = new HashMap<>();
            Map<Integer, Card.Rank> pairRank = new HashMap<>();
            for (Map.Entry<Integer, List<Card.Rank>> entry : playersRanks.entrySet()) {
                threeRank.put(entry.getKey(), entry.getValue().get(2));// pod indeksem 1 na pewno jest wartość z karety
                if (entry.getValue().get(2).compareTo(entry.getValue().get(0)) != 0) {
                    pairRank.put(entry.getKey(), entry.getValue().get(0));
                }
                else if (entry.getValue().get(2).compareTo(entry.getValue().get(4)) != 0) {
                    pairRank.put(entry.getKey(), entry.getValue().get(4));
                }
            }

            Card.Rank theHighestThree = Card.Rank.getHighestRank(new ArrayList<>(threeRank.values()));
            for (Map.Entry<Integer, Card.Rank> map : threeRank.entrySet()) {
                if (!map.getValue().equals(theHighestThree)) {
                    pairRank.remove(map.getKey());
                    playersRanks.remove(map.getKey());
                }
            }

            if (pairRank.size() == 1) {
                for (Player player : players) {
                    if (pairRank.containsKey(player.getPlayerID())) {
                        winner = player.getPlayerID();
                        break;
                    }
                }
            }
            else {
                Card.Rank theHighestPair = Card.Rank.getHighestRank(new ArrayList<>(pairRank.values()));
                Card.Rank theLowestPair = Card.Rank.getLowestRank(new ArrayList<>(pairRank.values()));
                if (theHighestPair.equals(theLowestPair)) {
                    winner = 0;
                }
                else {
                    for (Map.Entry<Integer, Card.Rank> map : pairRank.entrySet()) {
                        if (!map.getValue().equals(theHighestPair)) {
                            playersRanks.remove(map.getKey());
                        }
                    }
                }
            }
        }
        else if (handNumber == Hands.THREE_OF_A_KIND.getValue()) {
            Map<Integer, Card.Rank> threeRank = new HashMap<>();
            Map<Integer, List<Card.Rank>> kickersRanks = new HashMap<>();
            for (Map.Entry<Integer, List<Card.Rank>> entry : playersRanks.entrySet()) {
                threeRank.put(entry.getKey(), entry.getValue().get(2));
                List<Card.Rank> ranks = new ArrayList<>();
                for (int i = 0; i < players.size(); i++) {
                    if (entry.getValue().get(2).compareTo(entry.getValue().get(i)) != 0) {
                        ranks.add(entry.getValue().get(i));
                    }
                }
                kickersRanks.put(entry.getKey(), ranks);
            }

            Card.Rank theHighestThree = Card.Rank.getHighestRank(new ArrayList<>(threeRank.values()));
            for (Map.Entry<Integer, Card.Rank> map : threeRank.entrySet()) {
                if (!map.getValue().equals(theHighestThree)) {
                    kickersRanks.remove(map.getKey());
                    playersRanks.remove(map.getKey());
                }
            }

            if (playersRanks.size() == 1) {
                for (Player player : players) {
                    if (playersRanks.containsKey(player.getPlayerID())) {
                        winner = player.getPlayerID();
                        break;
                    }
                }
            }
            else {
                Set<Integer> keys = playersRanks.keySet();
                for (Integer key : keys) {
                    playersRanks.put(key, kickersRanks.get(key));
                }
                winner = compareWithoutThreesAndPairs(0, playersRanks, players);
            }
        }
        else if (handNumber == Hands.TWO_PAIRS.getValue()) {
            Map<Integer, Card.Rank> lowerPairRank = new HashMap<>();
            Map<Integer, Card.Rank> higherPairRank = new HashMap<>();
            Map<Integer, Card.Rank> kickerRank = new HashMap<>();

            for (Map.Entry<Integer, List<Card.Rank>> entry : playersRanks.entrySet()) {
                List<Card.Rank> ranks = entry.getValue();

                List<Card.Rank> pairs = new ArrayList<>();
                Card.Rank kicker = null;

                for (int i = 0; i < ranks.size() - 1; i++) {
                    if (ranks.get(i).equals(ranks.get(i + 1))) {
                        pairs.add(ranks.get(i));
                        i++;
                    } else {
                        kicker = ranks.get(i);
                    }
                }

                if (!pairs.isEmpty()) {
                    lowerPairRank.put(entry.getKey(), pairs.get(0));
                    higherPairRank.put(entry.getKey(), pairs.get(1));
                }
                if (kicker != null) {
                    kickerRank.put(entry.getKey(), kicker);
                }
            }

            Card.Rank theHighestOfHigherPair = Card.Rank.getHighestRank(new ArrayList<>(higherPairRank.values()));
            playersRanks.keySet().removeIf(key -> !theHighestOfHigherPair.equals(higherPairRank.get(key)));

            if (playersRanks.size() == 1) {
                winner = playersRanks.keySet().iterator().next();
                return winner;
            }

            Card.Rank theHighestOfLowerPair = Card.Rank.getHighestRank(new ArrayList<>(lowerPairRank.values()));
            playersRanks.keySet().removeIf(key -> !theHighestOfLowerPair.equals(lowerPairRank.get(key)));

            if (playersRanks.size() == 1) {
                winner = playersRanks.keySet().iterator().next();
                return winner;
            }

            Card.Rank theHighestKicker = Card.Rank.getHighestRank(new ArrayList<>(kickerRank.values()));
            playersRanks.keySet().removeIf(key -> !theHighestKicker.equals(kickerRank.get(key)));

            if (playersRanks.size() == 1) {
                winner = playersRanks.keySet().iterator().next();
            } else {
                winner = 0;
            }
        }
        else if (handNumber == Hands.ONE_PAIR.getValue()) {
            Map<Integer, Card.Rank> pairRank = new HashMap<>();
            Map<Integer, List<Card.Rank>> kickersRanks = new HashMap<>();
            for (Map.Entry<Integer, List<Card.Rank>> entry : playersRanks.entrySet()) {
                Set<Card.Rank> set = new HashSet<>(entry.getValue());
                List<Card.Rank> ranks = new ArrayList<>();
                for (Card.Rank rank : set) {
                    if (entry.getValue().indexOf(rank) == entry.getValue().lastIndexOf(rank)) {
                        ranks.add(rank);
                    }
                    else {
                        pairRank.put(entry.getKey(), rank);
                    }
                }
                Card.Rank.sortRanks(ranks);
                kickersRanks.put(entry.getKey(), ranks);
            }

            Card.Rank theHighestPair = Card.Rank.getHighestRank(new ArrayList<>(pairRank.values()));
            for (Map.Entry<Integer, Card.Rank> map : pairRank.entrySet()) {
                if (!map.getValue().equals(theHighestPair)) {
                    kickersRanks.remove(map.getKey());
                    playersRanks.remove(map.getKey());
                }
            }

            if (playersRanks.size() == 1) {
                for (Player player : players) {
                    if (playersRanks.containsKey(player.getPlayerID())) {
                        winner = player.getPlayerID();
                        break;
                    }
                }
            }
            else {
                Set<Integer> keys = playersRanks.keySet();
                for (Integer key : keys) {
                    playersRanks.put(key, kickersRanks.get(key));
                }
                winner = compareWithoutThreesAndPairs(0, playersRanks, players);
            }
        }
        else if (handNumber == 0) {
            for (int i = 0; i < 5; i++) {
                Card.Rank theHighestRank = null;
                Set<Integer> candidates = new HashSet<>(playersRanks.keySet());

                for (Integer playerID : playersRanks.keySet()) {
                    List<Card.Rank> ranks = playersRanks.get(playerID);

                    Card.Rank currentRank = ranks.get(ranks.size() - 1 - i);

                    if (theHighestRank == null || currentRank.compareTo(theHighestRank) > 0) {
                        theHighestRank = currentRank;
                        candidates.clear();
                        candidates.add(playerID);
                    } else if (currentRank.compareTo(theHighestRank) < 0) {
                        candidates.remove(playerID);
                    }
                }

                if (candidates.size() == 1) {
                    return candidates.iterator().next();
                }

                playersRanks.keySet().retainAll(candidates);
            }

            return 0;
        }


        Iterator<Player> it = players.iterator();
        Set<Integer> keys = playersRanks.keySet();
        while (it.hasNext()) {
            Player p = it.next();
            if (!keys.contains(p.getPlayerID())) {
                it.remove();
            }
        }

        return winner;
    }


    private Map<Integer, List<Card.Rank>> getPlayersRanks(List<Player> players) {
        Map<Integer, List<Card.Rank>> playersRanks = new HashMap<>();

        for (Player player : players) {
            List<Card> hand = player.getCopyOfHand();
            List<Card.Rank> ranks = new ArrayList<>();
            for (Card card : hand) {
                ranks.add(card.getRank());
            }
            Card.Rank.sortRanks(ranks);
            playersRanks.put(player.getPlayerID(), ranks);
        }
        return playersRanks;
    }


    private boolean hasOnlyOneColor(List<Card.Suit> suits) {
        Set<Card.Suit> set = new HashSet<>(suits);
        return set.size() == 1;
    }


    private boolean isFigure(Card.Rank rank) {
        return rank == Card.Rank.ACE || rank == Card.Rank.KING || rank == Card.Rank.QUEEN || rank == Card.Rank.JACK;
    }


    protected boolean checkForRoyalFlush(List<Card.Rank> ranks, List<Card.Suit> suits) {
        boolean[] checker = checkForStraight(ranks, suits);

        return !checker[0] && checker[1] && (ranks.get(0) == Card.Rank.TEN);
    }

    protected boolean checkForStraightFlush(List<Card.Rank> ranks, List<Card.Suit> suits) {
        boolean[] checker = checkForStraight(ranks, suits);

        return !checker[0] && checker[1] && (ranks.get(ranks.size() - 1) != Card.Rank.ACE);
    }

    private boolean checkForQuads(List<Card.Rank> ranks) {
        boolean checker = false;

        int counter = 1;
        for (int i = 1; i < ranks.size(); i++) {
            if (ranks.get(i) == ranks.get(i - 1)) {
                counter += 1;
            }
            else {
                if (counter == 4) {
                    checker = true;
                    break;
                }
                counter = 1;
            }
        }

        if (counter == 4) {
            checker = true;
        }

        return checker;
    }

    private boolean checkForFullHouse(List<Card.Rank> ranks) {
        boolean checker = true;

        ArrayList<Integer> indexes = checkForThreeOfAKind(ranks);
        if (indexes.size() != 3) {
            checker =  false;
        }
        else if (!isFigure(ranks.get(indexes.get(0)))) {
            checker = false;
        }
        else {
            ArrayList<Integer> allIndexes = new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4));
            allIndexes.removeAll(indexes);
            if (ranks.get(allIndexes.get(0)) != ranks.get(allIndexes.get(1))) {
                checker = false;
            }
        }

        return checker;
    }

    private boolean checkForFlush(List<Card.Rank> ranks, List<Card.Suit> suits) {
        boolean[] checker = checkForStraight(ranks, suits);
        return !checker[0] && !checker[1];
    }

    private boolean[] checkForStraight(List<Card.Rank> ranks, List<Card.Suit> suits) {
        boolean[] checker = {true, true}; // Jest więcej niż 1 kolor i karty są po kolei

        if (hasOnlyOneColor(suits)) {
            checker[0] = false;
        }

        for (int i = 0; i < ranks.size() - 1; i++) {
            if (ranks.get(i+1) != ranks.get(i).next()) {
                checker[1] = false;
                break;
            }
        }

        return checker;
    }

    private ArrayList<Integer> checkForThreeOfAKind(List<Card.Rank> ranks) {
        int counter = 1;
        ArrayList<Integer> indexes = new ArrayList<>(3);
        indexes.add(0);
        for (int i = 1; i < ranks.size(); i++) {
            if (ranks.get(i) == ranks.get(i - 1)) {
                counter += 1;
                indexes.add(i);
            }
            else {
                if (counter == 3) {
                    break;
                }
                counter = 1;
                indexes.clear();
                indexes.add(i);
            }
        }

        return indexes;
    }

    private boolean checkForTwoPairs(List<Card.Rank> ranks) {
        boolean checker = false;

        int counter = 1;
        int pairsCounter = 0;
        for (int i = 1; i < ranks.size(); i++) {
            if (ranks.get(i) == ranks.get(i - 1)) {
                counter += 1;
            }
            else {
                if (counter == 2) {
                    pairsCounter++;
                }
                counter = 1;
            }
        }

        if (counter == 2) {
            pairsCounter++;
        }

        if (pairsCounter == 2) {
            checker = true;
        }

        return checker;
    }

    private boolean checkForOnePair(List<Card.Rank> ranks) {
        boolean checker = false;

        Set<Card.Rank> set = new HashSet<>(ranks);
        if (set.size() == 4) {
            checker = true;
        }
        return checker;
    }
}
