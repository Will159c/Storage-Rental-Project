import javax.swing.*;
import java.awt.*;

public class StorageGUI extends JPanel {

    private MyGUI myGui;

    public StorageGUI(MyGUI myGui) {
        this.myGui = myGui;
        JPanel panel = new JPanel(new GridBagLayout());
        JLabel titleTxt = new JLabel("Test", SwingConstants.CENTER);
        panel.add(titleTxt);

        add(panel);
    }

}
