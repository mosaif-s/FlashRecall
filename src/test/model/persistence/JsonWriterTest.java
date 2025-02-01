package model.persistence;

import model.Flashcard;
import model.FlashcardDeck;
import model.FlashcardManager;
import persistence.JsonReader;
import persistence.JsonWriter;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// Referenced from the JsonSerialization Demo
// https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo


class JsonWriterTest extends JsonTest {
    //NOTE TO CPSC 210 STUDENTS: the strategy in designing tests for the JsonWriter is to
    //write data to a file and then use the reader to read it back in and check that we
    //read in a copy of what was written out.

    @Test
    void testWriterInvalidFile() {
        try {
            FlashcardManager fm = new FlashcardManager("My work room");
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyFlashcardManager() {
        try {
            FlashcardManager fm = new FlashcardManager("My work room");
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyFlashcardManager.json");
            writer.open();
            writer.write(fm);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyFlashcardManager.json");
            fm = reader.read();
            assertEquals("My work room", fm.getName());
            assertEquals(0, fm.numDecks());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralFlashcardManager() {
        try {
            FlashcardManager fm = new FlashcardManager("My work room");
            Flashcard fc1=new Flashcard("Q1","A1");
            Flashcard fc2=new Flashcard("Q2","A2");
            Flashcard fc3=new Flashcard("Q3","A3");
            FlashcardDeck fdeck=new FlashcardDeck("BIOL");
            FlashcardDeck fdeck2=new FlashcardDeck("CHEM");
            fdeck.addCard(fc1);
            fdeck.addCard(fc2);
            fdeck.addCard(fc3);
            fdeck2.addCard(fc1);
            fdeck2.addCard(fc2);
            fdeck2.addCard(fc3);

            
            fm.addDeck(fdeck);
            fm.addDeck(fdeck2);
            JsonWriter writer = new JsonWriter("./data/testWriterGeneralFlashcardManager.json");
            writer.open();
            writer.write(fm);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralFlashcardManager.json");
            fm = reader.read();
            assertEquals("My work room", fm.getName());
            List<FlashcardDeck> decks = fm.getDecks();
            assertEquals(2, decks.size());
            checkDeck("BIOL", fdeck.getCards(), decks.get(0));
            checkDeck("CHEM", fdeck2.getCards(), decks.get(1));

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}