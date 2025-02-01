package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//The FlashcardManager class organizes multiple flashcard decks under a single manager name, 
//allowing addition, retrieval, and JSON serialization of decks for persistent storage.
public class FlashcardManager implements Writable {
    private String name;
    private ArrayList<FlashcardDeck> decks;

    // EFFECTS: constructs FlashcardManager with a name and empty list of decks
    public FlashcardManager(String name) {
        this.name = name;
        decks = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    // MODIFIES: this
    // EFFECTS: adds deck to this FlashcardManager
    public void addDeck(FlashcardDeck deck) {
        decks.add(deck);
        EventLog.getInstance().logEvent(new Event(deck.getSubject() + " deck added to deck manager"));

    }

    // EFFECTS: returns an unmodifiable list of decks in this FlashcardManager
    public ArrayList<FlashcardDeck> getDecks() {
        return decks;
        // return Collections.unmodifiableList(decks);
    }

    // EFFECTS: returns number of decks in this Manager
    public int numDecks() {
        return decks.size();
    }

    // EFFECTS: returns this Manager in this deck as a JSON object
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("decks", decksToJson());
        return json;
    }

    // EFFECTS: returns decks in this FlashcardManager as a JSON array
    private JSONArray decksToJson() {
        JSONArray jsonArray = new JSONArray();

        for (FlashcardDeck d : decks) {
            jsonArray.put(d.toJson());
        }

        return jsonArray;
    }

}
