package model.persistence;

import model.Flashcard;
import model.FlashcardDeck;
import model.FlashcardManager;
import persistence.JsonReader;
import persistence.JsonWriter;
import org.junit.jupiter.api.Test;

import persistence.JsonReader;
import persistence.JsonWriter;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// Referenced from the JsonSerialization Demo
// https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo


class JsonReaderTest extends JsonTest {

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            FlashcardManager fm = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyFlashcardManager() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyFlashcardManager.json");
        try {
            FlashcardManager fm = reader.read();
            assertEquals("My work room", fm.getName());
            assertEquals(0, fm.numDecks());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderGeneralFlashcardManager() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralFlashcardManager.json");
        try {
            FlashcardManager fm = reader.read();
            assertEquals("My work room", fm.getName());
            List<FlashcardDeck> decks = fm.getDecks();
            assertEquals(2, decks.size());

            Flashcard flashcard = new Flashcard("Q1", "A1");
            Flashcard flashcard2 = new Flashcard("Q2", "A2");
            FlashcardDeck biolDeck = new FlashcardDeck("BIOL");
            biolDeck.addCard(flashcard);
            biolDeck.addCard(flashcard2);

            Flashcard flashcardA = new Flashcard("Q1", "A1");
            FlashcardDeck chemDeck = new FlashcardDeck("CHEM");
            chemDeck.addCard(flashcardA);
           

            checkDeck("BIOL", biolDeck.getCards(), decks.get(0));
            checkDeck("CHEM", chemDeck.getCards(), decks.get(1));
            
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
}