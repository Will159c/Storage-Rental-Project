import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class MyGUI {

    private static JLabel titleTxt;
    private static JButton registerButton;
    private static JButton loginButton;
    private static JPanel cardpanel;
    private static CardLayout cardLayout;
    public MyGUI() {
        /////////////// Initial GUI Settings /////////////
        JFrame frame = new JFrame();
        frame.setSize(1000, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null); // Centers the window

        cardLayout = new CardLayout();
        cardpanel = new JPanel(cardLayout);

        cardpanel.add(welcomeScreen(), "Welcome Screen");
        cardpanel.add(registerScreen(), "Registration");

        frame.add(cardpanel);
        cardLayout.show(cardpanel, "Welcome Screen"); // Show the first page

        frame.setVisible(true);
    }

    private JPanel welcomeScreen() {
        ///////////// Initial Settings ///////////
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints(); // Allows for ordered positioning

        ///////////// Text ///////////

        // Title text
        titleTxt = new JLabel("Welcome to Sotrage Rental", SwingConstants.CENTER);
        titleTxt.setFont(new Font("SansSerif", Font.BOLD, 40));
        titleTxt.setMinimumSize(new Dimension(400, 400));
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(titleTxt, gbc);

        ////////////// Buttons ////////////

        /////// Register Button
        // Button Design
        registerButton = new JButton("Register");
        registerButton.setPreferredSize(new Dimension(180, 50));
        gbc.gridy = 1; // Move button to next row
        gbc.insets = new Insets(20, 0, 10, 0); // Add spacing in between buttons and labels
        panel.add(registerButton, gbc);

        // Button action
        registerButton.addActionListener(e -> cardLayout.show(cardpanel, "Registration"));

        // Login Button
        loginButton = new JButton("Log-in");
        loginButton.setPreferredSize(new Dimension(180, 50));
        gbc.gridy = 2; // Move button to next row
        gbc.insets = new Insets(20, 0, 10, 0); // Add spacing in between buttons and labels
        panel.add(loginButton, gbc);

        return panel;
    }

    private JPanel registerScreen() {
        ///////////// Initial Settings ///////////
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints(); // Allows for ordered positioning

        return panel;
    }
}
