package ui;

import model.Flashcard;
import model.FlashcardDeck;
import model.FlashcardManager;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

// Represents the main menu for interacting with flashcard decks, allowing users
// to create decks, add/remove cards, start quizzes, and manage saved progress.
public class Menu {
    private ArrayList<FlashcardDeck> decks; // List of flashcard decks
    private Scanner scanner;
    private FlashcardManager flashcardManager;
    private static final String JSON_STORE = "./data/flashcardManager.json";
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    // EFFECTS: Initializes an empty list of flashcard decks and a scanner object
    public Menu() {
        decks = new ArrayList<>();
        scanner = new Scanner(System.in);
        flashcardManager = new FlashcardManager("Mohammad's workroom");
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);

    }

    // MODIFIES: this
    // EFFECTS: Displays the menu, prompts the user for a choice, and invokes the
    // respective method based on the user's selection
    @SuppressWarnings("methodlength")
    public void showMenu() {
        int choice;
        do {
            printMenu();
            choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1:
                    createDeck();
                    break;
                case 2:
                    addCardToDeck();
                    break;
                case 3:
                    removeCardFromDeck();
                    break;
                case 4:
                    startQuiz();
                    break;
                case 5:
                    startQuizUnmastered();
                    break;
                case 6:
                    saveFlashcardManager();
                    break;
                case 7:
                    loadFlashcardManager();
                    break;
                case 8:
                    listFlashcards();
                    break;
                case 0:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 0);
    }

    // MODIFIES: this
    // EFFECTS: Prompts the user to enter a subject and creates a new flashcard deck
    // with that subject
    private void createDeck() {
        System.out.print("Enter the subject for the new deck: ");
        String subject = scanner.nextLine();
        FlashcardDeck fd = new FlashcardDeck(subject);
        decks.add(fd);
        flashcardManager.addDeck(fd);
        System.out.println("Deck for " + subject + " created.");
    }

    // MODIFIES: this, FlashcardDeck
    // EFFECTS: Prompts the user to select a deck and enter a new flashcard
    // (question and answer),
    // then adds the flashcard to the selected deck
    private void addCardToDeck() {
        selectTriggered();
        int deckIndex = scanner.nextInt() - 1;
        scanner.nextLine(); // Consume newline

        if (deckIndex < 0 || deckIndex >= decks.size()) {
            System.out.println("Invalid deck selection.");
            return;
        }
        System.out.print("Enter the question for the flashcard: ");
        String question = scanner.nextLine();
        System.out.print("Enter the answer for the flashcard: ");
        String answer = scanner.nextLine();

        Flashcard card = new Flashcard(question, answer);
        decks.get(deckIndex).addCard(card);
        System.out.println("Card added to deck.");
    }

    // MODIFIES: this, FlashcardDeck
    // EFFECTS: Prompts the user to select a deck and enter the question of the card
    // to remove,
    // then removes the matching flashcard from the selected deck
    private void removeCardFromDeck() {
        selectTriggered();
        int deckIndex = scanner.nextInt() - 1;
        scanner.nextLine(); // Consume newline

        if (deckIndex < 0 || deckIndex >= decks.size()) {
            System.out.println("Invalid deck selection.");
            return;
        }
        FlashcardDeck selectedDeck = decks.get(deckIndex);
        System.out.print("Enter the question of the card to remove: ");
        String questionToRemove = scanner.nextLine();

        Flashcard cardToRemove = null;
        for (Flashcard card : selectedDeck) {
            if (card.getQuestion().equalsIgnoreCase(questionToRemove)) {
                cardToRemove = card;
                break;
            }
        }

        if (cardToRemove != null) {
            selectedDeck.removeCard(cardToRemove);
            System.out.println("Card removed.");
        } else {
            System.out.println("Card not found.");
        }
    }

    // MODIFIES: this
    // EFFECTS: Prompts the user to select a deck and starts a quiz using that deck.
    // If the deck is empty, notifies the user.
    private void startQuiz() {
        selectTriggered();
        int deckIndex = scanner.nextInt() - 1;
        scanner.nextLine(); // Consume newline

        if (deckIndex < 0 || deckIndex >= decks.size()) {
            System.out.println("Invalid deck selection.");
            return;
        }
        FlashcardDeck selectedDeck = decks.get(deckIndex);
        if (selectedDeck.getSize() != 0) {
            StartQuiz quiz = new StartQuiz(selectedDeck);
            quiz.startQuiz();
        } else {
            System.out.println("The " + selectedDeck.getSubject() + "deck is empty!");
        }

    }

    // MODIFIES: this
    // EFFECTS: Prompts the user to select a deck and starts a quiz with unmastered
    // cards from that deck.
    // If no unmastered cards are found, notifies the user.
    private void startQuizUnmastered() {
        selectTriggered();
        int deckIndex = scanner.nextInt() - 1;
        scanner.nextLine(); // Consume newline

        if (deckIndex < 0 || deckIndex >= decks.size()) {
            System.out.println("Invalid deck selection.");
            return;
        }
        FlashcardDeck selectedDeck = decks.get(deckIndex);
        FlashcardDeck unmasteredDeck = selectedDeck.getUnmasteredCards();
        if (unmasteredDeck.getSize() > 0) {
            StartQuiz quiz = new StartQuiz(unmasteredDeck);
            quiz.startQuiz();
        } else {
            System.out.println("Unmastered deck is empty! Good Job!");
        }
    }

    // EFFECTS: saves the workroom to file
    private void saveFlashcardManager() {
        try {
            jsonWriter.open();
            jsonWriter.write(flashcardManager);
            jsonWriter.close();
            System.out.println("Saved " + flashcardManager.getName() + " to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

    // MODIFIES: this
    // EFFECTS: loads workroom from file
    private void loadFlashcardManager() {
        try {
            flashcardManager = jsonReader.read();
            decks = (ArrayList) flashcardManager.getDecks();
            System.out.println("Loaded " + flashcardManager.getName() + " from " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }

    // EFFECTS: prints the menu for the program
    private void printMenu() {
        System.out.println("Menu:");
        System.out.println("1. Create a new deck");
        System.out.println("2. Add a card to a deck");
        System.out.println("3. Remove a card from a deck");
        System.out.println("4. Start a quiz");
        System.out.println("5. Start quiz of unmastered cards");
        System.out.println("6. Save your decks and learning progress");
        System.out.println("7. Load your decks and learning progress");
        System.out.println("8. Print all flashcards in a deck");
        System.out.println("0. Exit");
        System.out.print("Enter your choice: ");
    }

    // EFFECTS: Prints all cards of a deck
    private void listFlashcards() {
        selectTriggered();
        System.out.print("Select a deck by number: ");
        int deckIndex = scanner.nextInt() - 1;
        scanner.nextLine(); // Consume newline

        if (deckIndex < 0 || deckIndex >= decks.size()) {
            System.out.println("Invalid deck selection.");
            return;
        }
        FlashcardDeck selectedDeck = decks.get(deckIndex);
        System.out.println(selectedDeck.listAllCards());

    }

    // EFFECTS: Prints a deck menu when a feature is triggered
    private void selectTriggered() {
        if (decks.isEmpty()) {
            System.out.println("No decks available. Create a deck first.");
            return;
        }

        System.out.println("Available decks:");
        for (int i = 0; i < decks.size(); i++) {
            System.out.println((i + 1) + ". " + decks.get(i).getSubject());
        }
        System.out.print("Select a deck by number: ");

    }
}
