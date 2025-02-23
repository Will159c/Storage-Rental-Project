import javax.swing.*;
import java.awt.*;
public class MyGUI {
    public MyGUI() {
        JFrame frame = new JFrame();
        frame.setSize(600, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        frame.add(panel);

        panel.setLayout(null);

        frame.setVisible(true);
    }
}
