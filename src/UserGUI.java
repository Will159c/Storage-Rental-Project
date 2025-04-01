import javax.swing.*;
import java.awt.*;

public class UserGUI extends JPanel {

    private MyGUI myGui;
    private JLabel titleTxt;

    public UserGUI(MyGUI myGUI) {
        this.myGui = myGUI;
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints(); // Allows for ordered positioning

        // Create a panel with GridBagLayout
        JPanel panel = new JPanel(new GridBagLayout());
        gbc.fill = GridBagConstraints.HORIZONTAL; // Stretch components horizontally

        // Title text
        titleTxt = new JLabel("Welcome", SwingConstants.CENTER);
        titleTxt.setFont(new Font("SansSerif", Font.BOLD, 40));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1; // Ensure title spans both columns
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(titleTxt, gbc);

        ////////// Buttons ///////////
        // User Button
        JButton manageButton = new JButton("Manage Storage Units");
        manageButton.setPreferredSize(new Dimension(100, 50));
        gbc.gridy = 1;
        gbc.gridx = 0;
        gbc.insets = new Insets(20, 0, 10, 0);
        panel.add(manageButton, gbc);

        // Storage Button
        JButton reserveButton = new JButton("Reserve Storage Units");
        reserveButton.setPreferredSize(new Dimension(100, 50));
        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.insets = new Insets(20, 0, 10, 0);
        panel.add(reserveButton, gbc);

        // Log Out Button
        JButton rBack = new JButton("Log Out");
        rBack.setPreferredSize(new Dimension(100, 50));
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.insets = new Insets(20, 0, 10, 0);
        panel.add(rBack, gbc);

        add(panel);
    }

    public void setUsername(String username) { // Set up
        titleTxt.setText("Welcome " + username);
    }
}
