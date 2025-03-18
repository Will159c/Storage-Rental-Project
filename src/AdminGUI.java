import javax.swing.*;
import java.awt.*;

public class AdminGUI extends JPanel {
    private MyGUI myGui;

    public AdminGUI(MyGUI myGui) {
        this.myGui = myGui; // Save reference
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints(); // Allows for ordered positioning

        // Create a panel with GridBagLayout
        JPanel panel = new JPanel(new GridBagLayout());
        gbc.fill = GridBagConstraints.HORIZONTAL; // Stretch components horizontally

        // Title text
        JLabel titleTxt = new JLabel("Welcome Admin", SwingConstants.CENTER);
        titleTxt.setFont(new Font("SansSerif", Font.BOLD, 40));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1; // Ensure title spans both columns
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(titleTxt, gbc);

        ////////// Buttons ///////////
        // User Button
        JButton userButton = new JButton("Manage Users");
        userButton.setPreferredSize(new Dimension(100, 50));
        gbc.gridy = 1;
        gbc.gridx = 0;
        gbc.insets = new Insets(20, 0, 10, 0);
        panel.add(userButton, gbc);

        // Storage Button
        JButton storageButton = new JButton("Manage Storage Units");
        storageButton.setPreferredSize(new Dimension(100, 50));
        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.insets = new Insets(20, 0, 10, 0);
        panel.add(storageButton, gbc);

        // Log Out Button
        JButton rBack = new JButton("Log Out");
        rBack.setPreferredSize(new Dimension(100, 50));
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.insets = new Insets(20, 0, 10, 0);
        panel.add(rBack, gbc);

        /////// Button functionalities //////////

        // Log out and return to home screen!!!!!!!11!!
        rBack.addActionListener(e -> myGui.showMain("Welcome Screen"));

        // Go to manage storage screen

        // Add panel to AdminGUI
        add(panel);
    }
}
