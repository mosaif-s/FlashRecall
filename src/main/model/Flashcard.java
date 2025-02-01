package model;

import org.json.JSONObject;

//The Flashcard class represents a study flashcard, storing a question, answer, mastery state, and correct/incorrect
// attempt counts, with methods for manipulation and JSON serialization.
public class Flashcard {
    private String question;
    private String answer;
    private boolean mastery;
    private int incorrectTimes;
    private int correctTimes;

    // EFFECTS: Constructs a flashcard with the given question and answer.
    // Initializes mastery to false and sets correct and incorrect times to 0.
    public Flashcard(String question, String answer) {
        this.question = question;
        this.answer = answer;
        this.incorrectTimes = 0; // Initialize incorrect and correct times
        this.correctTimes = 0;
        this.mastery = false;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    // EFFECTS: Returns true if the card is mastered (more correct answers than
    // incorrect), false otherwise.
    public boolean isMastered() {
        return mastery || (correctTimes > incorrectTimes);
    }

    public int getIncorrectTimes() {
        return incorrectTimes;
    }

    public int getCorrectTimes() {
        return correctTimes;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    // MODIFIES: this
    // EFFECTS: Manually sets the mastery state of the flashcard.
    // public void setMastery(boolean mastery) {
    // this.mastery = mastery;
    // }

    public void setIncorrectTimes(int incorrectTimes) {
        this.incorrectTimes = incorrectTimes;
    }

    public void setCorrectTimes(int correctTimes) {
        this.correctTimes = correctTimes;
    }

    // MODIFIES: this
    // EFFECTS: Increments the incorrectTimes by 1.
    public void incrementIncorrectTimes() {
        this.incorrectTimes++;
    }

    // MODIFIES: this
    // EFFECTS: Increments the correctTimes by 1.
    public void incrementCorrectTimes() {
        this.correctTimes++;
    }

    // MODIFIES: this
    // EFFECTS: Marks the flashcard as mastered by setting mastery to true.
    public void markAsMastered() {
        this.mastery = true;
    }

    // MODIFIES: this
    // EFFECTS: Returns a string representing the flashcard's mastery level based on
    // the ratio of correct answers to total attempts.
    public String estimateMasteryLevel() {
        int totalAttempts = correctTimes + incorrectTimes;

        if (totalAttempts == 0) {
            return "No attempts yet.";
        }

        double accuracy = (double) correctTimes / totalAttempts * 100;

        if (accuracy >= 80) {
            return "Mastered";
        } else if (accuracy >= 50) {
            return "Intermediate";
        } else {
            return "Needs Improvement";
        }
    }

    // EFFECTS: Returns a string representation of the flashcard.
    @Override
    public String toString() {
        return "Q: " + question + " A: " + answer + " | Correct: " + correctTimes + " | Incorrect: " + incorrectTimes;
    }

    // EFFECTS: Returns a jsonObject of the corresponding flashcard.
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("question", question);
        json.put("answer", answer);
        json.put("correctTimes", correctTimes);
        json.put("incorrectTimes", incorrectTimes);
        json.put("mastery", mastery);
        return json;
    }
}
