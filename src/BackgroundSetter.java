import javax.swing.*;
import java.awt.*;

/**
 * Class is used to apply a background to the JPanel without changing how
 * the GUI uses the panel.
 */
public class BackgroundSetter extends JPanel {
    private final Image bgImg;

    /**
     * Constructs a BackgroundSetter with the specified background image path and layout manager.
     * @param path path to the background image
     * @param layout layout manager to use for the panel
     */
    public BackgroundSetter(String path, LayoutManager layout) {
        super(layout);
        bgImg = new ImageIcon(getClass().getResource(path)).getImage();
    }

    /**
     * Paints the background to include new image in the panel.
     * @param g Graphics content to use for painting
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(bgImg, 0, 0, getWidth(), getHeight(), this);
    }
}
