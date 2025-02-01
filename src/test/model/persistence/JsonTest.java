package model.persistence;


import model.Flashcard;
import model.FlashcardDeck;
import model.FlashcardManager;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

// Referenced from the JsonSerialization Demo
// https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo

public class JsonTest {
    protected void checkDeck(String subject, ArrayList<Flashcard> arraydeck, FlashcardDeck deck) {
        assertEquals(subject, deck.getSubject());
        assertEquals(arraydeck.toString(), deck.getCards().toString());
    }
}
