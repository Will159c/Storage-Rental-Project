import javax.swing.*;
import java.awt.*;

public class BackgroundSetter extends JPanel {
    private final Image bgImg;

    public BackgroundSetter(String path, LayoutManager layout) {
        super(layout);
        bgImg = new ImageIcon(getClass().getResource(path)).getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(bgImg, 0, 0, getWidth(), getHeight(), this);
    }
}
