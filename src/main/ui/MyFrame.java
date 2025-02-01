package ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import java.util.Random;
import model.*;
import model.Event;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.util.ArrayList;
import java.util.HashMap;
import java.io.IOException;
import java.io.FileNotFoundException;

//A JFrame-based class that manages the flashcard application's GUI, flashcard decks,
// and persistent data storage via JSON.
public class MyFrame extends JFrame implements WindowListener {
    private FlashcardManager deckManager;
    private JsonReader jsonReader;
    private JsonWriter jsonWriter;
    private ArrayList<FlashcardDeck> decks;
    private static final String JSON_STORE = "./data/flashcardManager.json";

    static MyFrame frame = new MyFrame();

    // Initializes the frame, flashcard manager, JSON reader/writer, and sets up the
    // GUI components, including layout and panels.
    @SuppressWarnings("methodlength")
    public MyFrame() {
        // Initialize the deck manager and default deck
        deckManager = new FlashcardManager("Default");
        jsonReader = new JsonReader(JSON_STORE);
        jsonWriter = new JsonWriter(JSON_STORE);
        // FlashcardDeck defaultDeck = new FlashcardDeck("Default Deck");
        // deckManager.addDeck(defaultDeck);

        // Create the main panel with CardLayout
        JPanel mainPanel = new JPanel(new CardLayout());
        JPanel homePanel = new JPanel(new BorderLayout());
        JPanel containerPanel = new JPanel(new WrapLayout(10, 75, 20)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;

                // Define gradient colors
                Color color1 = new Color(128, 0, 128); // Purple
                Color color2 = new Color(255, 182, 193); // Light Pink

                // Create a gradient from top to bottom
                GradientPaint gradient = new GradientPaint(
                        0, 0, color1,
                        0, getHeight(), color2);

                // Apply the gradient
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        JPanel containerAndTitlePanel = new JPanel(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(128, 0, 160));
        topPanel.setLayout(new BorderLayout());
        homePanel.add(topPanel, BorderLayout.NORTH);

        JPanel myDecksPanel = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;

                // Define gradient colors
                Color color1 = new Color(128, 0, 128); // Purple
                Color color2 = new Color(128, 0, 128); // Purple

                // Create a gradient from top to bottom
                GradientPaint gradient = new GradientPaint(
                        0, 0, color1,
                        0, getHeight(), color2);

                // Apply the gradient
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        myDecksPanel.setPreferredSize(new Dimension(200, 80));
        // myDecksPanel.setBackground(Color.CYAN); // Optional: Color for visibility
        JLabel myDecksLabel = new JLabel("My Decks");
        myDecksLabel.setForeground(Color.WHITE);
        myDecksLabel.setBounds(100, 10, 600, 70);
        myDecksLabel.setFont(new Font("Segoe UI", Font.BOLD, 50)); // Optional: Styling the label
        myDecksPanel.add(myDecksLabel);

        containerAndTitlePanel.add(myDecksPanel, BorderLayout.NORTH);
        containerAndTitlePanel.add(containerPanel, BorderLayout.CENTER);

        // Button for loading decks
        JButton dotsButton = new JButton("⋮");
        dotsButton.setFont(new Font("Arial", Font.PLAIN, 18));
        dotsButton.setPreferredSize(new Dimension(20, 20));
        dotsButton.setFocusPainted(false);
        topPanel.add(dotsButton, BorderLayout.EAST);

        JPopupMenu optionsMenu = new JPopupMenu();
        JMenuItem loadOption = new JMenuItem("Load");
        JMenuItem saveOption = new JMenuItem("Save");
        optionsMenu.add(loadOption);
        optionsMenu.add(saveOption);

        // Button for adding a deck
        JPanel addDeckButtonPanel = new JPanel(null);
        addDeckButtonPanel.setOpaque(false);
        addDeckButtonPanel.setPreferredSize(new Dimension(230, 300));
        addDeckButtonPanel.setBackground(null);

        JButton addDeckButton = new JButton("+") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Draw the circular background
                g2.setColor(Color.LIGHT_GRAY);
                g2.fillOval(0, 0, getWidth(), getHeight());

                // Draw the "+" symbol
                g2.setFont(new Font("Arial", Font.BOLD, 20));
                FontMetrics fm = g2.getFontMetrics();
                String text = getText();
                int textWidth = fm.stringWidth(text);
                int textHeight = fm.getAscent();
                g2.setColor(Color.BLACK);
                g2.drawString(text, (getWidth() - textWidth) / 2, (getHeight() + textHeight) / 2 - 4);
            }

            @Override
            protected void paintBorder(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.DARK_GRAY);
                g2.drawOval(0, 0, getWidth() - 1, getHeight() - 1);
            }

            @Override
            public boolean contains(int x, int y) {
                int radius = getWidth() / 2;
                int centerX = getWidth() / 2;
                int centerY = getHeight() / 2;
                // Check if the point (x, y) is within the circle
                return Math.pow(x - centerX, 2) + Math.pow(y - centerY, 2) <= Math.pow(radius, 2);
            }
        };
        addDeckButton.setPreferredSize(new Dimension(30, 20));
        addDeckButton.setFocusPainted(false);
        addDeckButton.setBounds(27, 125, 80, 80);
        addDeckButtonPanel.add(addDeckButton);
        containerPanel.add(addDeckButtonPanel);

        JScrollPane scrollPane = new JScrollPane(containerAndTitlePanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        homePanel.add(scrollPane, BorderLayout.CENTER);

        // Map to store unique panels for each deck
        HashMap<String, JPanel> normalDeckPanels = new HashMap<>();

        // Add panels to the main panel
        mainPanel.add(homePanel, "Home");

        // Action Listeners
        loadOption.addActionListener(e -> {
            try {
                Map<FlashcardDeck, JLabel> sizeDeckLabels = new HashMap<>();
                deckManager = jsonReader.read();
                decks = (ArrayList) deckManager.getDecks();
                System.out.println("Loaded " + deckManager.getName() + " from " + JSON_STORE);
                containerPanel.removeAll();

                for (FlashcardDeck currentDeck : decks) {
                    ImageIcon ic;
                    if (currentDeck.getRatio() < 33.0) {
                        //System.out.println(currentDeck.getRatio());
                        ic = new ImageIcon("./data/candle.png");
                    } else if (currentDeck.getRatio() > 33.0 && currentDeck.getRatio() < 66.0) {
                        //System.out.println(currentDeck.getRatio());
                        ic = new ImageIcon("./data/bulb.png");
                    } else {
                        //System.out.println(currentDeck.getRatio());
                        ic = new ImageIcon("./data/star.png");
                    }
                    JPanel removePanel = new JPanel();
                    JPanel deck = createColorPanel(Color.RED, ic);
                    JButton viewDeckButton = new JButton("View " + currentDeck.getSubject() + " Deck");
                    JTextField questionFlashcard = new JTextField();
                    JTextField answerFlashcard = new JTextField();
                    JPanel sideDeckPanel = createSideDeckPanel(questionFlashcard, answerFlashcard);

                    // Unique normalDeckPanel for this deck
                    JPanel normalDeckPanel = new JPanel(new BorderLayout());
                    JPanel mainDeckPanel = new JPanel();
                    mainDeckPanel.setBackground(Color.GRAY);
                    mainDeckPanel.setPreferredSize(new Dimension(350, 400));
                    normalDeckPanel.add(new JScrollPane(mainDeckPanel), BorderLayout.CENTER);

                    JButton backButton = new JButton("Back to Home");
                    normalDeckPanel.add(sideDeckPanel, BorderLayout.EAST);
                    normalDeckPanel.add(backButton, BorderLayout.SOUTH);

                    // Save this deck's panel in the map
                    normalDeckPanels.put(currentDeck.getSubject(), normalDeckPanel);

                    // Add this deck's panel to the main panel
                    mainPanel.add(normalDeckPanel, currentDeck.getSubject());
                    mainPanel.add(removePanel, "Remove" + currentDeck.getSubject());

                    // Populate the flashcards for this deck
                    backButton.addActionListener(evt -> switchPanel(mainPanel, "Home"));
                    viewDeckButton.addActionListener(evt -> {
                        switchPanel(mainPanel, currentDeck.getSubject());
                        mainDeckPanel.removeAll();
                        for (Flashcard flashcard : currentDeck.getCards()) {
                            JPanel flashcardPanel = createFlashcardPanel(flashcard.getQuestion());
                            mainDeckPanel.add(flashcardPanel);
                        }
                        mainDeckPanel.revalidate();
                        mainDeckPanel.repaint();
                    });

                    // Add a new flashcard to the deck
                    JButton addButton = new JButton("Add Flashcard");
                    addButton.addActionListener(evt -> {
                        String question = questionFlashcard.getText().trim();
                        String answer = answerFlashcard.getText().trim();
                        if (!question.isEmpty() && !answer.isEmpty()) {
                            Flashcard flashcard = new Flashcard(question, answer);
                            currentDeck.addCard(flashcard);
                            JLabel deckSizeLabel = sizeDeckLabels.get(currentDeck);
                            if (deckSizeLabel != null) {
                                deckSizeLabel.setText(currentDeck.getSize() + " cards.");
                            }
                            JPanel flashcardPanel = createFlashcardPanel(flashcard.getQuestion());
                            mainDeckPanel.add(flashcardPanel);
                            mainDeckPanel.revalidate();
                            mainDeckPanel.repaint();
                            questionFlashcard.setText("");
                            answerFlashcard.setText("");
                        }
                    });

                    sideDeckPanel.add(addButton);
                    sideDeckPanel.add(new JLabel(currentDeck.getSubject()));

                    JButton removeButton = new JButton("Remove a Flashcard");
                    JButton backButtonRemove = new JButton("Back to Flashcard Deck");

                    removeButton.addActionListener(event -> {
                        removePanel.removeAll();
                        // FlashcardDeck currentDeck = newDeck;
                        for (int i = 0; i < currentDeck.getSize(); i++) {
                            Flashcard flashcard = currentDeck.getCard(i);
                            JButton cardButton = new JButton(flashcard.getQuestion());
                            int index = i;
                            cardButton.addActionListener(evt -> {
                                int confirmed = JOptionPane.showConfirmDialog(
                                        null,
                                        "Are you sure you want to delete this card?",
                                        "Confirm Deletion",
                                        JOptionPane.YES_NO_OPTION);
                                if (confirmed == JOptionPane.YES_OPTION) {
                                    currentDeck.removeCard(currentDeck.getCard(index));
                                    // Update UI
                                    mainDeckPanel.removeAll();
                                    for (int j = 0; j < currentDeck.getSize(); j++) {
                                        Flashcard updatedFlashcard = currentDeck.getCard(j);
                                        //System.out.println(currentDeck.getSize());
                                        //System.out.println(updatedFlashcard.getAnswer());
                                        //System.out.println(updatedFlashcard.getQuestion());
                                        mainDeckPanel.add(createFlashcardPanel(updatedFlashcard.getQuestion()));
                                    }
                                    JLabel deckSizeLabel = sizeDeckLabels.get(currentDeck);
                                    if (deckSizeLabel != null) {
                                        deckSizeLabel.setText(currentDeck.getSize() + " cards.");
                                    }
                                    mainDeckPanel.revalidate();
                                    mainDeckPanel.repaint();
                                    removePanel.remove(cardButton);
                                    removePanel.revalidate();
                                    removePanel.repaint();
                                }
                            });
                            removePanel.add(cardButton);

                        }
                        removePanel.add(backButtonRemove);
                        removePanel.revalidate();
                        removePanel.repaint();
                        switchPanel(mainPanel, "Remove" + currentDeck.getSubject());

                        backButtonRemove.addActionListener(evt -> switchPanel(mainPanel, currentDeck.getSubject()));

                    });
                    sideDeckPanel.add(removeButton);

                    deck.setLayout(null);

                    // deck.add();
                    viewDeckButton.setFont(new Font("Arial", Font.BOLD, 13)); // Set font style and size
                    viewDeckButton.setForeground(Color.BLACK);
                    viewDeckButton.setBounds(27, 270, 180, 30);
                    Color panelColor = deck.getBackground();
                    int r = panelColor.getRed() - 18;
                    int g = panelColor.getGreen() - 18;
                    int b = panelColor.getBlue() - 18;
                    r = Math.max(0, Math.min(255, r));
                    g = Math.max(0, Math.min(255, g));
                    b = Math.max(0, Math.min(255, b));
                    Color darkerColor = new Color(r, g, b);
                    viewDeckButton.setBackground(darkerColor);

                    JLabel titleDeck = new JLabel(currentDeck.getSubject());
                    titleDeck.setFont(new Font("Arial", Font.BOLD, 40)); // Set font style and size
                    titleDeck.setForeground(new Color(r - 25, g - 25, b - 25));
                    titleDeck.setBounds(27, 150, 180, 60);
                    deck.add(titleDeck);

                    JLabel sizeDeck = new JLabel((currentDeck.getSize() + " cards."));
                    sizeDeck.setFont(new Font("Arial", Font.BOLD, 15)); // Set font style and size
                    sizeDeck.setForeground(new Color(r - 25, g - 25, b - 25));
                    sizeDeck.setBounds(27, 185, 180, 60);
                    deck.add(sizeDeck);
                    // deck.setIcon(new ImageIcon("path_to_image.jpg"));
                    sizeDeckLabels.put(currentDeck, sizeDeck);

                    deck.add(viewDeckButton);
                    containerPanel.add(deck);
                }
                containerPanel.add(addDeckButton);
                containerPanel.revalidate();
                containerPanel.repaint();
            } catch (IOException ex) {
                System.out.println("Unable to read from file: " + JSON_STORE);
            }
        });

        saveOption.addActionListener(e -> {
            try {
                jsonWriter.open();
                jsonWriter.write(deckManager);
                jsonWriter.close();
                System.out.println("Saved " + deckManager.getName() + " to " + JSON_STORE);
            } catch (FileNotFoundException exc) {
                System.out.println("Unable to write to file: " + JSON_STORE);
            }
        });

        dotsButton.addActionListener(
                e -> optionsMenu.show(dotsButton, dotsButton.getWidth() / 2, dotsButton.getHeight() / 2));

        addDeckButton.addActionListener(e -> {
            // Show a dialog to get the subject for the new deck
            String subject = JOptionPane.showInputDialog(this, "Enter the subject for the new deck:");
            Map<FlashcardDeck, JLabel> sizeDeckLabels = new HashMap<>();

            // Ensure the subject is not null or empty
            if (subject != null && !subject.trim().isEmpty()) {
                // Create a new deck with the provided subject
                FlashcardDeck newDeck = new FlashcardDeck(subject);

                // Create a panel for the new deck
                ImageIcon ic;
                if (newDeck.getRatio() < 33.0) {
                    ic = new ImageIcon("./data/candle.png");
                } else if (newDeck.getRatio() > 33.0 && newDeck.getRatio() < 66.0) {
                    ic = new ImageIcon("./data/bulb.pngn");
                } else {
                    ic = new ImageIcon("./data/star.png");
                }
                JPanel deck = createColorPanel(Color.RED, ic);
                JButton viewDeckButton = new JButton("View " + newDeck.getSubject() + " Deck");
                JTextField questionFlashcard = new JTextField();
                JTextField answerFlashcard = new JTextField();
                JPanel sideDeckPanel = createSideDeckPanel(questionFlashcard, answerFlashcard);

                // Create a panel to hold the flashcards
                JPanel normalDeckPanel = new JPanel(new BorderLayout());

                JPanel removePanel = new JPanel();
                JPanel mainDeckPanel = new JPanel();
                mainDeckPanel.setBackground(Color.GRAY);
                mainDeckPanel.setPreferredSize(new Dimension(350, 400));
                normalDeckPanel.add(new JScrollPane(mainDeckPanel), BorderLayout.CENTER);

                JButton backButton = new JButton("Back to Home");
                normalDeckPanel.add(sideDeckPanel, BorderLayout.EAST);
                normalDeckPanel.add(backButton, BorderLayout.SOUTH);

                // Save this deck's panel in the map
                normalDeckPanels.put(newDeck.getSubject(), normalDeckPanel);

                // Add this deck's panel to the main panel
                mainPanel.add(normalDeckPanel, newDeck.getSubject());
                mainPanel.add(removePanel, "Remove" + newDeck.getSubject());

                // Populate the flashcards for this deck
                backButton.addActionListener(evt -> switchPanel(mainPanel, "Home"));

                viewDeckButton.addActionListener(evt -> {
                    switchPanel(mainPanel, newDeck.getSubject());
                    mainDeckPanel.removeAll();
                    for (Flashcard flashcard : newDeck.getCards()) {
                        JPanel flashcardPanel = createFlashcardPanel(flashcard.getQuestion());
                        mainDeckPanel.add(flashcardPanel);
                    }
                    mainDeckPanel.revalidate();
                    mainDeckPanel.repaint();
                });

                // Add a new flashcard to the deck
                JButton addButton = new JButton("Add Flashcard");
                addButton.addActionListener(evt -> {
                    String question = questionFlashcard.getText().trim();
                    String answer = answerFlashcard.getText().trim();
                    if (!question.isEmpty() && !answer.isEmpty()) {
                        Flashcard flashcard = new Flashcard(question, answer);
                        newDeck.addCard(flashcard);

                        JLabel deckSizeLabel = sizeDeckLabels.get(newDeck);
                        if (deckSizeLabel != null) {
                            deckSizeLabel.setText(newDeck.getSize() + " cards.");
                        }
                        JPanel flashcardPanel = createFlashcardPanel(flashcard.getQuestion());
                        mainDeckPanel.add(flashcardPanel);
                        mainDeckPanel.revalidate();
                        mainDeckPanel.repaint();
                        deck.revalidate();
                        deck.repaint();
                        questionFlashcard.setText("");
                        answerFlashcard.setText("");
                    }
                });

                sideDeckPanel.add(addButton);
                sideDeckPanel.add(new JLabel(newDeck.getSubject()));

                JButton removeButton = new JButton("Remove a Flashcard");
                JButton backButtonRemove = new JButton("Back to Flashcard Deck");

                removeButton.addActionListener(event -> {
                    removePanel.removeAll();
                    FlashcardDeck currentDeck = newDeck;
                    for (int i = 0; i < currentDeck.getSize(); i++) {
                        Flashcard flashcard = currentDeck.getCard(i);
                        JButton cardButton = new JButton(flashcard.getQuestion());
                        int index = i;
                        cardButton.addActionListener(evt -> {
                            int confirmed = JOptionPane.showConfirmDialog(
                                    null,
                                    "Are you sure you want to delete this card?",
                                    "Confirm Deletion",
                                    JOptionPane.YES_NO_OPTION);
                            if (confirmed == JOptionPane.YES_OPTION) {
                                currentDeck.removeCard(currentDeck.getCard(index));
                                // Update UI
                                mainDeckPanel.removeAll();
                                for (int j = 0; j < currentDeck.getSize(); j++) {
                                    Flashcard updatedFlashcard = currentDeck.getCard(j);
                                    //System.out.println(currentDeck.getSize());
                                    //System.out.println(updatedFlashcard.getAnswer());
                                    //System.out.println(updatedFlashcard.getQuestion());
                                    mainDeckPanel.add(createFlashcardPanel(updatedFlashcard.getQuestion()));
                                }
                                JLabel deckSizeLabel = sizeDeckLabels.get(currentDeck);
                                if (deckSizeLabel != null) {
                                    deckSizeLabel.setText(currentDeck.getSize() + " cards.");
                                }
                                mainDeckPanel.revalidate();
                                mainDeckPanel.repaint();
                                removePanel.remove(cardButton);
                                removePanel.revalidate();
                                removePanel.repaint();
                            }
                        });
                        removePanel.add(cardButton);

                    }
                    removePanel.add(backButtonRemove);
                    removePanel.revalidate();
                    removePanel.repaint();
                    switchPanel(mainPanel, "Remove" + newDeck.getSubject());

                    backButtonRemove.addActionListener(evt -> switchPanel(mainPanel, newDeck.getSubject()));

                });
                sideDeckPanel.add(removeButton);

                deck.setLayout(null);

                // deck.add();
                viewDeckButton.setFont(new Font("Arial", Font.BOLD, 13)); // Set font style and size
                viewDeckButton.setForeground(Color.BLACK);
                viewDeckButton.setBounds(27, 270, 180, 30);
                Color panelColor = deck.getBackground();
                int r = panelColor.getRed() - 18;
                int g = panelColor.getGreen() - 18;
                int b = panelColor.getBlue() - 18;
                r = Math.max(0, Math.min(255, r));
                g = Math.max(0, Math.min(255, g));
                b = Math.max(0, Math.min(255, b));
                Color darkerColor = new Color(r, g, b);
                viewDeckButton.setBackground(darkerColor);

                JLabel titleDeck = new JLabel(newDeck.getSubject());
                titleDeck.setFont(new Font("Arial", Font.BOLD, 40)); // Set font style and size
                titleDeck.setForeground(new Color(r - 25, g - 25, b - 25));
                titleDeck.setBounds(27, 150, 180, 60);
                deck.add(titleDeck);

                JLabel sizeDeck = new JLabel((newDeck.getSize() + " cards."));
                sizeDeck.setFont(new Font("Arial", Font.BOLD, 15)); // Set font style and size
                sizeDeck.setForeground(new Color(r - 25, g - 25, b - 25));
                sizeDeck.setBounds(27, 185, 180, 60);
                deck.add(sizeDeck);
                sizeDeckLabels.put(newDeck, sizeDeck);
                deck.add(viewDeckButton);
                containerPanel.add(deck);
                deckManager.addDeck(newDeck);
                containerPanel.revalidate();
                containerPanel.repaint();
            } else {
                JOptionPane.showMessageDialog(this, "Subject cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Add the main panel to the frame
        this.add(mainPanel);

        // Configure the frame
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1000, 500);
        this.setVisible(true);

        addWindowListener(this);
    }

    // EFFECTS: Creates and returns a RoundedCornerPanel with a randomly generated
    // background color and an optional image drawn on it.
    private JPanel createColorPanel(Color color, ImageIcon imgicon) {
        RoundedCornerPanel panel = new RoundedCornerPanel(30, 30) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Load and draw the image
                Image img = imgicon.getImage();
                g.drawImage(img, 50, 20, 135, 135, this);
            }
        };
        Random random = new Random();
        // Generate a bright color
        int red = random.nextInt(56) + 200; // 200 to 255
        int green = random.nextInt(56) + 200; // 200 to 255
        int blue = random.nextInt(56) + 200;
        int[] rgb = { red, green, blue };
        for (int i = 0; i < rgb.length; i++) {
            if (random.nextBoolean()) { // Randomize to make a mix
                rgb[i] = random.nextInt(128) + 127; // 127 to 255
            }
        }
        color = new Color(rgb[0], rgb[1], rgb[2]);
        panel.setBackground(color);
        panel.setPreferredSize(new Dimension(230, 300));
        return panel;
    }

    // EFFECTS: Creates and returns a JPanel displaying text fields for entering a
    // question and an answer for a flashcard.
    private JPanel createSideDeckPanel(JTextField questionFlashcard, JTextField answerFlashcard) {
        JPanel sideDeckPanel = new JPanel();
        sideDeckPanel.setLayout(new BoxLayout(sideDeckPanel, BoxLayout.Y_AXIS));
        sideDeckPanel.setBackground(Color.LIGHT_GRAY);
        sideDeckPanel.setPreferredSize(new Dimension(200, 400));
        questionFlashcard.setMaximumSize(new Dimension(180, 30));
        answerFlashcard.setMaximumSize(new Dimension(180, 30));
        sideDeckPanel.add(new JLabel("Question:"));
        sideDeckPanel.add(questionFlashcard);
        sideDeckPanel.add(new JLabel("Answer:"));
        sideDeckPanel.add(answerFlashcard);
        return sideDeckPanel;
    }

    // EFFECTS: Creates and returns a JPanel displaying a flashcard with the
    // provided text.
    private JPanel createFlashcardPanel(String text) {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setPreferredSize(new Dimension(300, 50));
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        panel.add(new JLabel(text));
        return panel;
    }

    // EFFECTS: Switches the displayed panel in the CardLayout of the given panel to
    // the specified card name.
    private void switchPanel(JPanel panel, String cardName) {
        CardLayout cl = (CardLayout) panel.getLayout();
        cl.show(panel, cardName);
    }

    public static void main(String[] args) {
        new MyFrame();
    }

    // EFFECTS: Does nothing when the window is opened.
    @Override
    public void windowOpened(WindowEvent e) {
        
    }

    // EFFECTS: Logs all events from EventLog, 
    //  and disposes of the frame with a 2-second delay.
    @Override
    public void windowClosing(WindowEvent e) {
        //System.out.println("WINDOW CLOSEEEED");

        Iterator<Event> iterator = EventLog.getInstance().iterator();

        // Use the iterator to loop through the list
        System.out.println("Event logged:\n");
        while (iterator.hasNext()) {  // Check if there are more elements
            String desc = iterator.next().getDescription();  // Get the next element
            System.out.println(desc);  // Print the element
        }
        ActionListener task = new ActionListener() {
            boolean alreadyDisposed = false;
            public void actionPerformed(ActionEvent e) {
                if (frame.isDisplayable()) {
                    alreadyDisposed = true;
                    frame.dispose();
                }
            }
        };
        Timer timer = new Timer(500, task); //fire every half second
        timer.setInitialDelay(2000);        //first delay 2 seconds
        timer.setRepeats(false);
        timer.start();
    }

    // EFFECTS: Does nothing when the window is closed.
    @Override
    public void windowClosed(WindowEvent e) {
    }

    // EFFECTS: Does nothing when the window is minimized. 
    @Override
    public void windowIconified(WindowEvent e) {
        
    }

    // EFFECTS: Does nothing when the window is restored from a minimized state.
    @Override
    public void windowDeiconified(WindowEvent e) {
        
    }
    
    // EFFECTS: Does nothing when the window is activated.
    @Override
    public void windowActivated(WindowEvent e) {
        
    }

    // EFFECTS: Does nothing when the window is deactivated.
    @Override
    public void windowDeactivated(WindowEvent e) {
        
    }
}
