package persistence;

import model.Event;
import model.EventLog;
import model.Flashcard;
import model.FlashcardDeck;
import model.FlashcardManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;

import org.json.*;

// Referenced from the JsonSerialization Demo
// https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo

// Represents a reader that reads FlashcardManager from JSON data stored in file
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads FlashcardManager from file and returns it;
    // throws IOException if an error occurs reading data from file
    public FlashcardManager read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        EventLog.getInstance().logEvent(new Event("Loaded data."));
        return parseFlashcardManager(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses FlashcardManager from JSON object and returns it
    private FlashcardManager parseFlashcardManager(JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        FlashcardManager fm = new FlashcardManager(name);
        addDecks(fm, jsonObject);
        return fm;
    }

    // MODIFIES: fm
    // EFFECTS: parses decks from JSON object and adds them to FlashcardManager
    private void addDecks(FlashcardManager fm, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("decks");
        for (Object json : jsonArray) {
            JSONObject deck = (JSONObject) json;
            addDeck(fm, deck);
        }
    }

    // MODIFIES: fm
    // EFFECTS: parses deck from JSON object and adds it to FlashcardManager
    private void addDeck(FlashcardManager fm, JSONObject jsonObject) {
        String subject = jsonObject.getString("subject");
        JSONArray cardsArray = jsonObject.getJSONArray("cards");

        ArrayList<Flashcard> cards = new ArrayList<>();

        // Loop through each flashcard in the JSON array
        for (Object cardObj : cardsArray) {
            JSONObject cardJson = (JSONObject) cardObj;
            Flashcard card = parseFlashcard(cardJson); // Parse flashcard
            cards.add(card); // Add parsed card to list
        }

        FlashcardDeck deck = new FlashcardDeck(subject, cards); // Create deck with cards
        fm.addDeck(deck); // Add deck to FlashcardManager
    }

    // MODIFIES: fm
    // EFFECTS: parses a single Flashcard from JSON object and returns the card
    private Flashcard parseFlashcard(JSONObject jsonObject) {
        String question = jsonObject.getString("question");
        String answer = jsonObject.getString("answer");
        int correctTimes = jsonObject.getInt("correctTimes");
        int incorrectTimes = jsonObject.getInt("incorrectTimes");
        boolean mastery = jsonObject.getBoolean("mastery");

        Flashcard card = new Flashcard(question, answer);
        card.setCorrectTimes(correctTimes); // Set correct attempts
        card.setIncorrectTimes(incorrectTimes); // Set incorrect attempts
        if (mastery) {
            card.markAsMastered(); // Set the mastery if marked as mastered in the JSON
        }
        return card;
    }
}
