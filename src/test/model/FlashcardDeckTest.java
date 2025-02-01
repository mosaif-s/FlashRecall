package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class FlashcardDeckTest {
    private FlashcardDeck deck;
    private Flashcard card1;
    private Flashcard card2;
    private Flashcard card3;

    @BeforeEach
    public void setUp() {
        deck = new FlashcardDeck("Math");
        card1 = new Flashcard("What is 2 + 2?", "4");
        card2 = new Flashcard("What is 3 + 5?", "8");
        card3 = new Flashcard("What is 5 + 7?", "12");
    }

    @Test
    public void testConstructor() {
        assertEquals("Math", deck.getSubject());
        assertEquals(0, deck.getSize());
    }

    @Test
    public void testAddCard() {
        deck.addCard(card1);
        assertEquals(1, deck.getSize());
        assertEquals(card1, deck.getCard(0));

        deck.addCard(card2, 0); // Add at index 0
        assertEquals(2, deck.getSize());
        assertEquals(card2, deck.getCard(0));
    }

    @Test
    public void testRemoveCard() {
        deck.addCard(card1);
        deck.addCard(card2);
        deck.removeCard(card1);
        assertEquals(1, deck.getSize());
        assertThrows(IndexOutOfBoundsException.class, () -> deck.getCard(1));
    }

    @Test
    public void testGetRandomCard() {
        deck.addCard(card1);
        deck.addCard(card2);
        Flashcard randomCard = deck.getRandomCard();
        assertTrue(randomCard.equals(card1) || randomCard.equals(card2));
    }

    @Test
    public void testShuffle() {
        deck.addCard(card1);
        deck.addCard(card2);
        deck.addCard(card3);
        List<Flashcard> originalOrder = List.of(deck.getCard(0), deck.getCard(1), deck.getCard(2));
        deck.shuffle();
        // Since shuffle is random, we cannot assert for specific order
        assertNotEquals(originalOrder, List.of(deck.getCard(0), deck.getCard(1), deck.getCard(2)));
    }

    @Test
    public void testReinsertCardAtRandomPosition() {
        deck.addCard(card1);
        deck.addCard(card2);
        deck.reinsertCardAtRandomPosition(card3);
        assertTrue(deck.getSize() == 3);
        assertTrue(deck.getCard(0).equals(card1) || deck.getCard(0).equals(card2) || deck.getCard(0).equals(card3));
    }

    @Test
    public void testGetUnmasteredCards() {
        card1.markAsMastered(); // Assuming markAsMastered method exists in Flashcard
        deck.addCard(card1);
        deck.addCard(card2);
        FlashcardDeck unmasteredDeck = deck.getUnmasteredCards();
        assertEquals(1, unmasteredDeck.getSize());
        assertEquals(card2, unmasteredDeck.getCard(0));
    }

    @Test
    public void testGetSize() {
        assertEquals(0, deck.getSize());
        deck.addCard(card1);
        assertEquals(1, deck.getSize());
        deck.addCard(card2);
        assertEquals(2, deck.getSize());
    }

    @Test
    public void testGetCard() {
        deck.addCard(card1);
        deck.addCard(card2);
        assertEquals(card1, deck.getCard(0));
        assertEquals(card2, deck.getCard(1));
        assertThrows(IndexOutOfBoundsException.class, () -> deck.getCard(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> deck.getCard(2));
    }

    @Test
    public void testToString() {
        deck.addCard(card1);
        assertEquals("[Q: What is 2 + 2? A: 4 | Correct: 0 | Incorrect: 0]", deck.toString());
    }

    @Test
    public void testIterator() {
        deck.addCard(card1);
        deck.addCard(card2);
        int count = 0;
        for (Flashcard card : deck) {
            count++;
            assertTrue(card.equals(card1) || card.equals(card2));
        }
        assertEquals(2, count);
    }

    @Test
    public void testSetSubject() {
        deck.setSubject("Science");
        assertEquals("Science", deck.getSubject());
    }
    @Test
    public void testListAllCardsMultipleCards() {
        deck.addCard(card1);
        deck.addCard(card2);
        
        String expected = "Q: What is 2 + 2? A: 4 | Correct: 0 | Incorrect: 0\n" +
                          "Q: What is 3 + 5? A: 8 | Correct: 0 | Incorrect: 0";

        assertEquals(expected, deck.listAllCards());
    }

    @Test
    public void testListAllCardsSingleCard() {
        deck.addCard(card1);
        
        String expected = "Q: What is 2 + 2? A: 4 | Correct: 0 | Incorrect: 0";

        assertEquals(expected, deck.listAllCards());
    }

    @Test
    public void testListAllCardsEmptyDeck() {
        String expected = "";  // For an empty deck, the result should be an empty string

        assertEquals(expected, deck.listAllCards());
    }
}
