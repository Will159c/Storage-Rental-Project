import javax.swing.*;
import java.awt.*;

/**
 * a) Class Name: LoginScreenGUI
 * b) Date of the Code: March 2, 2025
 * c) Programmers Names: Miguel Sicaja
 * d) Brief description: The login screen panel that allows the user to login as the admin or regular
 * user, provided they give the correct credentials. Handles the user input, basic authentication,
 * and navigation to the admin page, user page, or welcome screen.
 * e) Brief explanation of important functions:
 * - loginHandler: Verifies if user input is valid. If valid, logs user in as a user or admin. Otherwise,
 * gives an error message.
 * f) Important Data Structures:
 * - MySQL: Provides means of checking if user credentials are valid or not
 * g) Algorithms used:
 * - Uses basic logical algorithm to validate user credentials and handle login.
 */
public class LoginScreenGUI extends JPanel {

    private MyGUI myGui;
    JTextField registerUserTxt;
    JPasswordField passTxt;
    JLabel userValid;

    /**
     * Constructs login screen with the username and password
     * labels and fields. Also provides buttons and key listener for
     * logging in or returning to home screen.
     * @param myGui reference to the main GUI controller
     */
    public LoginScreenGUI(MyGUI myGui) {
        this.myGui = myGui; // Save reference
        setLayout(new BorderLayout());

        // Create a panel with GridBagLayout
        JPanel panel = new BackgroundSetter("/background_blur.jpg", new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints(); // Allows for ordered positioning

        // Initial settings
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.anchor = GridBagConstraints.CENTER;

        // Title text
        JLabel titleTxt = new JLabel("Log-in", SwingConstants.CENTER);
        titleTxt.setFont(new Font("SansSerif", Font.BOLD, 40));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // Ensure title spans both columns
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(titleTxt, gbc);

        gbc.gridwidth = 1; // Reset width

        // Username label
        JLabel userNameTxt = new JLabel("Enter Username:");
        userNameTxt.setFont(new Font("SansSerif", Font.PLAIN, 12));
        userNameTxt.setOpaque(true); // Allow for background of border to be colored
        userNameTxt.setBackground(Color.BLACK); // Set background border color
        userNameTxt.setForeground(Color.WHITE); // Set text color
        userNameTxt.setBorder(BorderFactory.createLineBorder(Color.pink)); // Create the border
        gbc.gridy = 1;
        gbc.insets = new Insets(20, 15, 5, 0);
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(userNameTxt, gbc);

        // Username text field
        registerUserTxt = new JTextField(15);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(registerUserTxt, gbc);

        // Username validation message
        userValid = new JLabel("");
        userValid.setFont(new Font("SansSerif", Font.ITALIC, 10));
        userValid.setOpaque(true); // Allow for background of border to be colored
        userValid.setBackground(Color.BLACK); // Set background border color
        userValid.setForeground(Color.red); // Set text color
        userValid.setBorder(BorderFactory.createLineBorder(Color.pink)); // Create the border
        gbc.gridy = 2;
        gbc.gridx = 1;
        gbc.insets = new Insets(0, 40, 5, 0);
        panel.add(userValid, gbc);

        // Password label
        JLabel passLabel = new JLabel("Enter Password:");
        passLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        passLabel.setOpaque(true); // Allow for background of border to be colored
        passLabel.setBackground(Color.BLACK); // Set background border color
        passLabel.setForeground(Color.WHITE); // Set text color
        passLabel.setBorder(BorderFactory.createLineBorder(Color.pink)); // Create the border
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.insets = new Insets(20, 15, 5, 0);
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(passLabel, gbc);

        // Password text field
        passTxt = new JPasswordField(15);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(passTxt, gbc);


        // Back Button
        JButton rBack = new JButton("Back");
        rBack.setPreferredSize(new Dimension(120, 30));
        gbc.gridy = 4;
        gbc.gridx = 0;
        gbc.insets = new Insets(20, 0, 10, 0);
        panel.add(rBack, gbc);

        // Back button function
        rBack.addActionListener(e -> myGui.showMain("Welcome Screen"));

        // Confirm Button
        JButton rConfirm = new JButton("Confirm");
        rConfirm.setPreferredSize(new Dimension(120, 30));
        gbc.gridx = 1;
        gbc.insets = new Insets(20, 80, 10, 0);
        panel.add(rConfirm, gbc);

        // Confirm button function
        rConfirm.addActionListener(e -> loginHandler());

        // Enter button event listener
        registerUserTxt.addActionListener(e -> loginHandler());
        passTxt.addActionListener(e -> loginHandler());

        // Add panel to LoginScreenGUI
        add(panel, BorderLayout.CENTER);
    }

    /**
     * Handles the login logic: validates user credentials, clears fields after login,
     * and navigates user to correct page based on whether they are an admin or user.
     */
    private void loginHandler() {
        String getUser = registerUserTxt.getText();
        String getPass = String.valueOf(passTxt.getPassword());

        // Clear error messages
        userValid.setText("");

        if (getUser.equals("admin") && getPass.equals("password")) { // Log in to Admin Screen
            // Clear text fields
            registerUserTxt.setText("");
            passTxt.setText("");

            myGui.showMain("Admin Screen"); // Redirect to Admin Screen
        }
        else if (MySQL.isUsernameAndPassword(getUser, getPass)) { // Log in to user screen

            // Clear text fields
            registerUserTxt.setText("");
            passTxt.setText("");
            myGui.setUsername(getUser);

            myGui.loginUser(getUser); // Redirect to User Screen by passing to MyGUI to save user info
        }
        else {
            userValid.setText("Invalid Credentials");
        }
    }
}
