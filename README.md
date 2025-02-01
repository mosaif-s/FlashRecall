# FlashRecall

## Efficient Learning Through Flashcards and Memory Science

## Project Proposal

The flashcard application will allow users to create, view, and manage sets of flashcards for studying various subjects. Users can add questions and answers, categorize them, and track their progress.
People who would utilize this project:

- Students
- Language learners
- Professionals studying for certifications

### Why This Project?

I’m passionate about education and learning methodologies. I used flashcards to teach my younger sibling sight words, and realized that the concept of **spaced repetition** and **memory retention** could be applied far beyond just early learning—it's an effective method for mastering virtually any subject across all age groups.Creating this flashcard application will not only allow me to enhance my programming skills in _Java_ but also provide a useful tool that can help others study more effectively.

## User Stories

- As a user, I want to be able to create a flashcard with a question and an answer.
- As a user, I want to create a deck of flashcards ie. a list of Flashcards
- As a user, I want to be able to view a list of all my flashcards.
- As a user, I want to be able to categorize my flashcards (e.g., by subject).
- As a user, I want to be able to delete a flashcard if I no longer need it.
- As a user, I want to be quizzed on my decks.
- As a user, I want to be quizzed on my unmastered decks.
- As a user, I want to be able to save my flashcard decks and corresponding flashcards
- As a user, I want to be able to load previously saved decks 

# Instructions for End User

- You can generate the first required action related to the user story "adding multiple Xs to a Y" by either adding multiple decks, or by adding multiple flashcards to a deck using respective add buttons.
- You can generate the second required action related to the user story "adding multiple Xs to a Y" by removing a flashcard from a deck if you no longer need it using the Remove flashcard button.
- You can locate my visual component by looking at any flashcard deck which displays an image relating to the progression of your learning.
- You can save the state of my application by clicking the More Options button in the corner of the screen which has a save option.
- You can reload the state of my application by by clicking the More Options button in the corner of the screen which has a load option.

## Phase 4: Task 2

Events logged:
- Biology deck added to deck manager
- Physics deck added to deck manager
- A Flashcard added to Biology deck.
- A Flashcard added to Biology deck.
- A Flashcard added to Physics deck.
- A Flashcard removed from Biology deck.


## Phase 4: Task 3

One of the refactorings would be to include StartQuiz as a method in the FlashCardDeck class, this will improve code structure and thus have all the relating functionalities in the same class. Different functionalities should ne divided into methods or classes as in the case of the MyFrame class, which includes fucntionals like making different panels, reacting to different buttons etc. These classes and methods should then be included in the MainUI class which further improves modularity and reduces redundancy. When complete implmentation of the UI is done, the main and main ui should be a singular class and the menu should be removed.

Associations Refactoring: 
Remove association from MyFrame and menu to FlashcardDeck as it already has access to the collection of decks using Flahcard manager.

