package ui;

import java.awt.*;
import javax.swing.JPanel;

// Custom layout manager that wraps components when there isn't enough horizontal space.
class WrapLayout extends FlowLayout {

    // Constructs a WrapLayout with the default alignment, horizontal gap, and
    // vertical gap.
    public WrapLayout() {
        super();
    }

    // EFFECTS: Sets the alignment of components within the layout.
    public WrapLayout(int align) {
        super(align);
    }

    // EFFECTS: Sets the alignment, horizontal gap, and vertical gap between
    // components.
    public WrapLayout(int align, int hgap, int vgap) {
        super(align, hgap, vgap);
    }

    // EFFECTS: Returns the preferred dimensions for the container to arrange its
    // components.
    @Override
    public Dimension preferredLayoutSize(Container target) {
        return layoutSize(target, true);
    }

    // EFFECTS: Returns the minimum dimensions for the container to arrange its
    // components.
    @Override
    public Dimension minimumLayoutSize(Container target) {
        return layoutSize(target, false);
    }

    // EFFECTS: Computes the total width and height required to lay out the
    // components.
    private Dimension layoutSize(Container target, boolean preferred) {
        synchronized (target.getTreeLock()) {
            int targetWidth = target.getSize().width;
            int hgap = getHgap();
            int vgap = getVgap();
            Insets insets = target.getInsets();

            int width = targetWidth - insets.left - insets.right - hgap * 2;
            int x = 0;
            int y = insets.top + vgap; 
            int rowHeight = 0;

            for (Component comp : target.getComponents()) {
                Dimension d = preferred ? comp.getPreferredSize() : comp.getMinimumSize();
                if (x + d.width > width) {
                    x = 0;
                    y += rowHeight + vgap;
                    rowHeight = 0;
                }
                x += d.width + hgap;
                rowHeight = Math.max(rowHeight, d.height);
            }

            y += rowHeight + vgap;
            return new Dimension(targetWidth, y);
        }
    }
}