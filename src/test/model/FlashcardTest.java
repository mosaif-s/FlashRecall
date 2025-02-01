package model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class FlashcardTest {
    private Flashcard flashcard;

    @Before
    public void setUp() {
        flashcard = new Flashcard("What is the capital of France?", "Paris");
    }

    // Test constructor
    @Test
    public void testConstructor() {
        assertEquals("What is the capital of France?", flashcard.getQuestion());
        assertEquals("Paris", flashcard.getAnswer());
        assertEquals(0, flashcard.getIncorrectTimes());
        assertEquals(0, flashcard.getCorrectTimes());
        assertFalse(flashcard.isMastered());  // Shouldn't be mastered yet
    }

    @Test
    public void testSetQuestion() {
        flashcard.setQuestion("What is the capital of Spain?");
        assertEquals("What is the capital of Spain?", flashcard.getQuestion());
    }

    @Test
    public void testSetAnswer() {
        flashcard.setAnswer("Madrid");
        assertEquals("Madrid", flashcard.getAnswer());
    }

    @Test
    public void testIsNotMasteredInitially() {
        flashcard.setCorrectTimes(0);
        flashcard.setIncorrectTimes(0);
        assertFalse(flashcard.isMastered());
    }

    @Test
    public void testIsMastered() {
        flashcard.setCorrectTimes(3);
        flashcard.setIncorrectTimes(1);
        assertTrue(flashcard.isMastered());
    }

    @Test
    public void testMarkAsMastered() {
        flashcard.markAsMastered();
        assertTrue(flashcard.isMastered());
    }

    @Test
    public void testIncrementIncorrectTimes() {
        flashcard.incrementIncorrectTimes();
        assertEquals(1, flashcard.getIncorrectTimes());
        flashcard.incrementIncorrectTimes();
        assertEquals(2, flashcard.getIncorrectTimes());
    }

    @Test
    public void testIncrementCorrectTimes() {
        flashcard.incrementCorrectTimes();
        assertEquals(1, flashcard.getCorrectTimes());
        flashcard.incrementCorrectTimes();
        assertEquals(2, flashcard.getCorrectTimes());
    }

    @Test
    public void testEstimateMasteryLevelNoAttempts() {
        assertEquals("No attempts yet.", flashcard.estimateMasteryLevel());
    }

    @Test
    public void testEstimateMasteryLevelNeedsImprovement() {
        flashcard.setCorrectTimes(1);
        flashcard.setIncorrectTimes(4);
        assertEquals("Needs Improvement", flashcard.estimateMasteryLevel());
    }

    @Test
    public void testEstimateMasteryLevelIntermediate() {
        flashcard.setCorrectTimes(3);
        flashcard.setIncorrectTimes(3);
        assertEquals("Intermediate", flashcard.estimateMasteryLevel());
    }

    @Test
    public void testEstimateMasteryLevelMastered() {
        flashcard.setCorrectTimes(8);
        flashcard.setIncorrectTimes(2);
        assertEquals("Mastered", flashcard.estimateMasteryLevel());
    }

    @Test
    public void testToString() {
        assertEquals("Q: What is the capital of France? A: Paris | Correct: 0 | Incorrect: 0", flashcard.toString());
        flashcard.incrementCorrectTimes();
        flashcard.incrementIncorrectTimes();
        assertEquals("Q: What is the capital of France? A: Paris | Correct: 1 | Incorrect: 1", flashcard.toString());
    }
}
