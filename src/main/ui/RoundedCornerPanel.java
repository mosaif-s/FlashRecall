package ui;

import javax.swing.*;
import java.awt.*;

// Represents a JPanel with rounded corners, used for custom panel shapes in the UI.
class RoundedCornerPanel extends JPanel {
    private int arcWidth;
    private int arcHeight;

    // Constructs a rounded corner panel with given arcWidth and height
    public RoundedCornerPanel(int arcWidth, int arcHeight) {
        this.arcWidth = arcWidth;
        this.arcHeight = arcHeight;
        setOpaque(false); // Ensure transparency around the corners
    }

    // EFFECTS: Paints the panel with rounded corners by overriding the default
    // paint behavior.
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();

        // Enable anti-aliasing for smoother corners
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Set background color
        g2.setColor(getBackground());

        // Draw a rounded rectangle
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), arcWidth, arcHeight);

        g2.dispose();
    }
}
