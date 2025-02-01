package ui;

import java.util.Scanner;
import model.Flashcard;
import model.FlashcardDeck;

// Represent a quiz class which takes user's quiz on a deck
public class StartQuiz {
    private FlashcardDeck deck;
    private int correctAnswers;
    private int totalQuestions;

    // REQUIRES: deckOfficial is non-null
    // EFFECTS: Initializes a new StartQuiz object, copying the flashcards from
    // deckOfficial into a new FlashcardDeck
    public StartQuiz(FlashcardDeck deckOfficial) {
        this.deck = new FlashcardDeck("QuizDeck");
        this.correctAnswers = 0;

        for (Flashcard card : deckOfficial) {
            this.deck.addCard(card);
        }

    }

    // MODIFIES: this
    // EFFECTS: Starts the quiz, prompts the user with flashcard questions, records
    // correct/incorrect answers,
    // reinserts cards that are answered incorrectly or marked as "Maybe" at a
    // random position in the deck,
    // and displays the final score at the end of the quiz.
    @SuppressWarnings("methodlength")
    public void startQuiz() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Starting the quiz. Answer the following questions:");

        int currentIndex = 0; // Use an index to track position
        while (currentIndex < deck.getSize()) {
            Flashcard card = deck.getCard(currentIndex); // Get card by index

            System.out.println("Q: " + card.getQuestion());
            System.out.print("Your answer: ");
            String userAnswer = scanner.nextLine();
            totalQuestions++;
            if (userAnswer.equalsIgnoreCase(card.getAnswer())) {
                card.incrementCorrectTimes();
                System.out.println("Correct!");
                correctAnswers++;
                System.out.print("How did you feel about this card? ");
                String mastering = scanner.nextLine();

                if (mastering.equals("Maybe")) {
                    deck.reinsertCardAtRandomPosition(card); // Move card reinsert logic to FlashcardDeck
                    System.out.println("This card will be asked again later.");
                }
            } else {
                card.incrementIncorrectTimes();
                System.out.println("Wrong! The correct answer was: " + card.getAnswer());
                deck.reinsertCardAtRandomPosition(card); // Move card reinsert logic to FlashcardDeck
                System.out.println("This card will be asked again later.");
            }

            currentIndex++;
        }

        System.out.println("\nQuiz completed!");
        System.out.println("You got " + correctAnswers + " out of " + totalQuestions + " correct.");
        // scanner.close();
    }
}
