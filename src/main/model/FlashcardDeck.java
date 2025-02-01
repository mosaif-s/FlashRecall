package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

//Represents a list of Flashcards as a Flashcard Deck with methods for manipulation including
// shuffling, returning unmastered cards and JSON serialization
public class FlashcardDeck implements Iterable<Flashcard>, Writable {
    private ArrayList<Flashcard> cards;
    private FlashcardDeck unmasteredCards;
    private String subject;

    // EFFECTS: Constructs a new FlashcardDeck object with the specified subject and
    // an empty card list.
    public FlashcardDeck(String subject) {
        this.subject = subject;
        cards = new ArrayList<>();
    }

    // EFFECTS: Constructs a new FlashcardDeck object with the specified subject and
    // card list.
    public FlashcardDeck(String subject, ArrayList<Flashcard> cards) {
        this.subject = subject;
        this.cards = cards;
    }

    // REQUIRES: card is non-null, index is non-negative and less than or equal to
    // the size of the deck.
    // MODIFIES: this
    // EFFECTS: Inserts card into the deck at the specified index.
    public void addCard(Flashcard card, int index) {
        cards.add(index, card);
        EventLog.getInstance().logEvent(new Event("A Flashcard added to " + subject + " deck."));
    }

    // REQUIRES: card is non-null.
    // MODIFIES: this
    // EFFECTS: Appends card to the end of the deck.
    public void addCard(Flashcard card) {
        cards.add(card);
        EventLog.getInstance().logEvent(new Event("A Flashcard added to " + subject + " deck."));

    }

    // REQUIRES: card is non-null and exists in the deck.
    // MODIFIES: this
    // EFFECTS: Removes the specified card from the deck.
    public void removeCard(Flashcard card) {
        if (!cards.isEmpty()) {
            cards.remove(card);
            EventLog.getInstance().logEvent(new Event("A Flashcard removed from " + subject + " deck."));
        }
    }

    // REQUIRES: The deck is not empty.
    // EFFECTS: Returns a random flashcard from the deck.
    public Flashcard getRandomCard() {
        Random random = new Random();
        return cards.get(random.nextInt(cards.size()));
    }

    // MODIFIES: this
    // EFFECTS: Shuffles the order of flashcards in the deck.
    public void shuffle() {
        Collections.shuffle(cards);
    }

    // MODIFIES: this
    // EFFECTS: Adds the flashcard at a random position near the end of the deck
    public void reinsertCardAtRandomPosition(Flashcard card) {
        Random random = new Random();
        int insertPosition = cards.size() - random.nextInt(Math.min(5, cards.size()));
        cards.add(insertPosition, card);
    }

    // MODIFIES: this
    // EFFECTS: Creates a new FlashcardDeck containing unmastered cards
    public void unmasteredCards() {
        unmasteredCards = new FlashcardDeck("Unmastered " + subject); // Reinitialize to avoid retaining previous data
        for (Flashcard card : cards) {
            if (!card.isMastered()) {
                unmasteredCards.addCard(card); // Add unmastered cards
            }
        }
    }

    // EFFECTS: Returns a list of all flashcards in the deck as a formatted string.
    public String listAllCards() {
        String allCards = "";
        for (Flashcard card : cards) {
            allCards += card.toString() + "\n";
        }
        EventLog.getInstance().logEvent(new Event(" Flashcards returned for printing the " + subject + " deck."));
        return allCards.trim();
    }

    // EFFECTS: Returns a FlashcardDeck containing unmastered cards.
    public FlashcardDeck getUnmasteredCards() {
        unmasteredCards();
        return unmasteredCards;
    }

    // EFFECTS: Returns the size of the deck.
    public int getSize() {
        return cards.size();
    }

    // REQUIRES: index is non-negative and less than the size of the deck.
    // EFFECTS: Returns the flashcard at the specified index.
    public Flashcard getCard(int index) {
        if (index < 0 || index >= cards.size()) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + cards.size());
        }
        return cards.get(index);
    }

    // EFFECTS: Returns a string representation of the deck (the list of cards).
    @Override
    public String toString() {
        return cards.toString();
    }

    // EFFECTS: Returns an iterator over the flashcards in the deck.
    @Override
    public Iterator<Flashcard> iterator() {
        return cards.iterator();
    }

    // EFFECTS: Returns the subject of the deck.
    public String getSubject() {
        return subject;
    }

    // EFFECTS: Returns the cards of the deck.
    public ArrayList<Flashcard> getCards() {
        return cards;
    }

    // EFFECTS: Returns the incorrect times and correct times ratio
    public double getRatio() {
        int ctimes = 0;
        int itimes = 0;
        for (Flashcard card : cards) {
            ctimes += card.getCorrectTimes();
            itimes += card.getIncorrectTimes();

        }
        int totalAttempts = ctimes + itimes;
        if (!(totalAttempts == 0)) {
            return (double) ctimes / totalAttempts * 100;
        } else {
            return 0;
        }

    }

    // MODIFIES: this
    // EFFECTS: Sets the subject of the deck.
    public void setSubject(String subject) {
        this.subject = subject;
    }

    // EFFECTS: Returns a JSON object of the corresponding deck.
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("subject", subject);
        json.put("cards", cardsToJson()); // Cards saved as a JSONArray
        return json;
    }

    // EFFECTS: returns cards in this deck as a JSON array
    private JSONArray cardsToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Flashcard card : cards) {
            jsonArray.put(card.toJson()); // Add each card's JSON representation to array
        }

        return jsonArray;
    }
}
