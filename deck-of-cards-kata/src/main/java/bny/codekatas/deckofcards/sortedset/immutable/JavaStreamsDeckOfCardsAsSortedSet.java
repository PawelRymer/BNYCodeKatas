/*
 * Copyright 2024 The Bank of New York Mellon.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package bny.codekatas.deckofcards.sortedset.immutable;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import bny.codekatas.deckofcards.Card;
import bny.codekatas.deckofcards.Rank;
import bny.codekatas.deckofcards.Suit;

import static java.util.stream.Collectors.*;

public class JavaStreamsDeckOfCardsAsSortedSet
{
    private SortedSet<Card> cards;
    private Map<Suit, SortedSet<Card>> cardsBySuit;

    /**
     * TODO Replace the null values below with something more useful.
     * Use Java 8 Streams APIs with {@link Card#streamCards()} to create an "immutable" SortedSet and store that in cards.
     * Group all of the cards by {@link Card#suit()} into an "immutable" Map and stored that in cardsBySuit.
     *
     * Hint: Look at {@link Collectors#groupingBy(Function)}, {@link Collections#unmodifiableSortedSet(SortedSet)}
     * {@link Collectors#collectingAndThen(Collector, Function)}, {@link Collectors#mapping(Function, Collector)}
     */
    public JavaStreamsDeckOfCardsAsSortedSet()
    {
        this.cards = Collections.unmodifiableSortedSet(
                Card.streamCards().sorted().collect(Collectors.toCollection(TreeSet::new)));
        this.cardsBySuit = Map.copyOf(this.cards.stream()
                .collect(Collectors.groupingBy(
                        Card::suit,
                        collectingAndThen(
                                Collectors.toCollection(TreeSet::new),
                                Collections::unmodifiableSortedSet))));
    }

    public Deque<Card> shuffle(Random random)
    {
        // TODO Shuffle the deck 3 times with the Random parameter and push the shuffled cards onto an ArrayDeque
        // Hint: Look at IntStream.range() or IntStream.rangeClosed() and Collections.shuffle()
        var mutableCardsDeck = new ArrayList<>(this.cards);
        IntStream.rangeClosed(1, 3).forEach(i -> Collections.shuffle(mutableCardsDeck, random));
        var deque = new ArrayDeque<Card>();
        mutableCardsDeck.forEach(deque::push);
        return deque;
    }

    public Set<Card> deal(Deque<Card> deque, int count)
    {
        Set<Card> hand = new HashSet<>();
        IntStream.range(0, count).forEach(i -> hand.add(deque.pop()));
        return hand;
    }

    public List<Set<Card>> shuffleAndDeal(Random random, int hands, int cardsPerHand)
    {
        Deque<Card> shuffled = this.shuffle(random);
        return this.dealHands(shuffled, hands, cardsPerHand);
    }

    public List<Set<Card>> dealHands(
            Deque<Card> shuffled,
            int hands,
            int cardsPerHand)
    {
        // TODO Deal the number of hands with the cardsPerHand into an "immutable" List<Set<Card>>
        return IntStream.range(0, hands).mapToObj(i -> this.deal(shuffled, cardsPerHand)).toList();
    }

    public SortedSet<Card> diamonds()
    {
        // TODO return all diamonds as an "Immutable" SortedSet
        return this.cardsBySuit.get(Suit.DIAMONDS);
    }

    public SortedSet<Card> hearts()
    {
        // TODO return all hearts as an "Immutable" SortedSet
        return this.cardsBySuit.get(Suit.HEARTS);
    }

    public SortedSet<Card> spades()
    {
        // TODO return all spades as an "Immutable" SortedSet
        return this.cardsBySuit.get(Suit.SPADES);
    }

    public SortedSet<Card> clubs()
    {
        // TODO return all clubs as an "Immutable" SortedSet
        return this.cardsBySuit.get(Suit.CLUBS);
    }

    public Map<Suit, Long> countsBySuit()
    {
        // TODO return the count of cards by Suit
        // Hint: Look at Collectors.groupingBy() and Collectors.counting()
        return this.cards.stream().collect(Collectors.groupingBy(Card::suit, Collectors.counting()));
    }

    public Map<Rank, Long> countsByRank()
    {
        // TODO return the count of cards by Rank
        // Hint: Look at Collectors.groupingBy() and Collectors.counting()
        return this.cards.stream().collect(Collectors.groupingBy(Card::rank, Collectors.counting()));
    }

    public SortedSet<Card> getCards()
    {
        return this.cards;
    }

    public Map<Suit, SortedSet<Card>> getCardsBySuit()
    {
        return this.cardsBySuit;
    }
}
