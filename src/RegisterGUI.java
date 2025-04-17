import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * a) Class Name: RegisterGUI
 * b) Date of the Code: March 2, 2025
 * c) Programmers Names: Miguel Sicaja
 * d) Brief description: Displays the labels for the username, email, password,
 * and confirmation password along with their designated
 * text fields. Performs a validation check and inserts
 * the new user into the database if all checks passes. Returns
 * to the main menu if user is successfully created, or shows
 * error messages if one or more checks fail.
 * e) Brief explanation of important functions:
 * - handleRegistration: Registers new user to database if all validation checks pass,
 *   otherwise gives error messages
 * - validate: checks whether given user input is valid or not
 * f) Important Data Structures:
 * - MySQL: Provides means of checking if input is valid to enter into the database. Also
 *   adds new user to the database
 * g) Algorithms used:
 * - Uses basic logic algorithm to validate user input, insert new user into database, or
 *   send error messages
 */
public class RegisterGUI extends JPanel {

    private MyGUI myGui;
    private JLabel userValid;
    private JLabel emailValid;
    private JLabel passValid;
    private JLabel cpassValid;
    private JTextField registerUserTxt;
    private JTextField emailTxt;
    private JPasswordField passTxt;
    private JPasswordField cpassTxt;

    /**
     * Constructs the RegisterGUI page and sets up the
     * labels, text fields, and buttons.
     *
     * @param myGui Reference to the main GUI controller for navigation
     */
    public RegisterGUI(MyGUI myGui) {
        this.myGui = myGui;
        setLayout(new BorderLayout());

        // Create a panel with GridBagLayout
        JPanel panel = new BackgroundSetter("/background_blur.jpg", new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints(); // Allows for ordered positioning

        // Initial settings
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.anchor = GridBagConstraints.CENTER;

        // Register Text
        JLabel registerTitle = new JLabel("Registration", SwingConstants.CENTER);
        registerTitle.setFont(new Font("SansSerif", Font.BOLD, 40));
        registerTitle.setMinimumSize(new Dimension(400, 400));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // Both this line and the line below ensures title is centered
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(registerTitle, gbc);

        // Reset constraints for next components
        gbc.gridwidth = 1;

        ////// Username Items //////
        // Username text
        JLabel userNameTxt = new JLabel("Enter Username");
        userNameTxt.setFont(new Font("SansSerif", Font.PLAIN, 12));
        userNameTxt.setOpaque(true); // Allow for background of border to be colored
        userNameTxt.setBackground(Color.BLACK); // Set background border color
        userNameTxt.setForeground(Color.WHITE); // Set text color
        userNameTxt.setBorder(BorderFactory.createLineBorder(Color.pink)); // Create the border
        gbc.gridy = 1;
        gbc.insets = new Insets(20, 15, 5, 0); // Add spacing
        gbc.anchor = GridBagConstraints.EAST; // Keep this to the left
        panel.add(userNameTxt, gbc);

        // Username text field
        registerUserTxt = new JTextField(15);
        gbc.gridy = 1;
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST; // Keep this to the right
        panel.add(registerUserTxt, gbc);

        // Confirm username is valid
        userValid = new JLabel("");
        userValid.setFont(new Font("SansSerif", Font.ITALIC, 10));
        userValid.setOpaque(true);
        userValid.setBackground(Color.BLACK); // Set background border color
        userValid.setForeground(Color.red);
        userValid.setBorder(BorderFactory.createLineBorder(Color.pink));
        gbc.gridy = 2;
        gbc.gridx = 1;
        gbc.insets = new Insets(0, 40, 5, 0);
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(userValid, gbc);

        ///////// Email Items //////////
        // Email text
        JLabel emailLabel = new JLabel("Enter email");
        emailLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        emailLabel.setOpaque(true); // Allow for background of border to be colored
        emailLabel.setBackground(Color.BLACK); // Set background border color
        emailLabel.setForeground(Color.WHITE); // Set text color
        emailLabel.setBorder(BorderFactory.createLineBorder(Color.pink)); // Create the border
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.insets = new Insets(20, 15, 5, 0); // Add spacing
        gbc.anchor = GridBagConstraints.EAST; // Keep this to the left
        panel.add(emailLabel, gbc);

        // Email text field
        emailTxt = new JTextField(15);
        gbc.gridy = 3;
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST; // Keep this to the right
        panel.add(emailTxt, gbc);

        // Confirm email is valid
        emailValid = new JLabel("");
        emailValid.setFont(new Font("SansSerif", Font.ITALIC, 10));
        emailValid.setOpaque(true); // Allow for background of border to be colored
        emailValid.setBackground(Color.BLACK); // Set background border color
        emailValid.setForeground(Color.red); // Set text color
        emailValid.setBorder(BorderFactory.createLineBorder(Color.pink)); // Create the border
        gbc.gridy = 4;
        gbc.gridx = 1;
        gbc.insets = new Insets(0, 40, 5, 0);
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(emailValid, gbc);

        ////////// Password Items //////////
        // Password text
        JLabel passLabel = new JLabel("Enter password");
        passLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        passLabel.setOpaque(true); // Allow for background of border to be colored
        passLabel.setBackground(Color.BLACK); // Set background border color
        passLabel.setForeground(Color.WHITE); // Set text color
        passLabel.setBorder(BorderFactory.createLineBorder(Color.pink)); // Create the border
        gbc.gridy = 5;
        gbc.gridx = 0;
        gbc.insets = new Insets(20, 15, 5, 0); // Add spacing
        gbc.anchor = GridBagConstraints.EAST; // Keep this to the left
        panel.add(passLabel, gbc);

        // Password text field
        passTxt = new JPasswordField(15);
        gbc.gridy = 5;
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST; // Keep this to the right
        panel.add(passTxt, gbc);

        // Confirm password is valid
        passValid = new JLabel("");
        passValid.setFont(new Font("SansSerif", Font.ITALIC, 10));
        passValid.setOpaque(true); // Allow for background of border to be colored
        passValid.setBackground(Color.BLACK); // Set background border color
        passValid.setForeground(Color.red); // Set text color
        passValid.setBorder(BorderFactory.createLineBorder(Color.pink)); // Create the border
        gbc.gridy = 6;
        gbc.gridx = 1;
        gbc.insets = new Insets(0, 0, 5, 0);
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(passValid, gbc);

        /////////// Confirm password stuff /////////////
        // Confirm Password text
        JLabel cpassLabel = new JLabel("Confirm password");
        cpassLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        cpassLabel.setOpaque(true); // Allow for background of border to be colored
        cpassLabel.setBackground(Color.BLACK); // Set background border color
        cpassLabel.setForeground(Color.WHITE); // Set text color
        cpassLabel.setBorder(BorderFactory.createLineBorder(Color.pink)); // Create the border
        gbc.gridy = 7;
        gbc.gridx = 0;
        gbc.insets = new Insets(20, 15, 5, 0); // Add spacing
        gbc.anchor = GridBagConstraints.EAST; // Keep this to the left
        panel.add(cpassLabel, gbc);

        // Confirm Password text field
        cpassTxt = new JPasswordField(15);
        gbc.gridy = 7;
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST; // Keep this to the right
        panel.add(cpassTxt, gbc);

        // Confirm password confirmation is valid
        cpassValid = new JLabel("");
        cpassValid.setFont(new Font("SansSerif", Font.ITALIC, 10));
        cpassValid.setOpaque(true); // Allow for background of border to be colored
        cpassValid.setBackground(Color.BLACK); // Set background border color
        cpassValid.setForeground(Color.red); // Set text color
        cpassValid.setBorder(BorderFactory.createLineBorder(Color.pink)); // Create the border
        gbc.gridy = 8;
        gbc.gridx = 1;
        gbc.insets = new Insets(0, 40, 5, 0);
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(cpassValid, gbc);


        //////// Buttons /////////

        /// Back button
        // Back button Design
        JButton rBack = new JButton("Back");
        rBack.setPreferredSize(new Dimension(120, 30));
        gbc.gridy = 9;
        gbc.gridx = 0;
        gbc.insets = new Insets(20, 0, 10, 0); // Add spacing in between buttons
        panel.add(rBack, gbc);

        // Back Button Function
        rBack.addActionListener(e -> myGui.showMain("Welcome Screen"));

        /// Confirmation Button

        // Confirm Button design
        JButton rConfirm = new JButton("Confirm");
        rConfirm.setPreferredSize(new Dimension(120, 30));
        gbc.gridy = 9;
        gbc.gridx = 1;
        gbc.insets = new Insets(20, 80, 10, 0); // Add spacing in between buttons
        panel.add(rConfirm, gbc);

        // Confirm button functionality
        rConfirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String getUser = registerUserTxt.getText();
                String getEmail = emailTxt.getText();
                String getPass = String.valueOf(passTxt.getPassword());
                String getCPass = String.valueOf(cpassTxt.getPassword());

                handleRegistration(getUser, getEmail, getPass, getCPass);
            }
        });

        add(panel, BorderLayout.CENTER);
    }

    /**
     * Handles registration logic.
     * Validates user inputs, shows error messages,
     * inserts user into the database, and resets page
     * after validation.
     *
     * @param getUser Username entered
     * @param getEmail Email entered
     * @param getPass Password entered
     * @param getCPass Confirm password entered
     */
    private void handleRegistration(String getUser, String getEmail, String getPass, String getCPass) {
        // Clear error messages
        userValid.setText("");
        passValid.setText("");
        cpassValid.setText("");
        emailValid.setText("");

        // Check registration validity
        if (validate(getUser, getEmail, getPass, getCPass)) {
            MySQL.insertUser(getUser, getPass, getEmail); // Create account into database with email

            // Dialog box
            JOptionPane.showMessageDialog(null, "Account Successfully Created. Please Log-in.", "", JOptionPane.INFORMATION_MESSAGE);

            // Clear text fields
            registerUserTxt.setText("");
            emailTxt.setText("");
            passTxt.setText("");
            cpassTxt.setText("");

            // Return to main menu
            myGui.showMain("Welcome Screen");
        }
        else {
            if (MySQL.isUser(getUser)) { // Check if username is already used, if so reject registration
                userValid.setText("Invalid Username");
            }
            if (getPass.length() < 4) { // Check if password is less than 4 chara
                passValid.setText("Password must be at least 4 characters");
            }
            if (!getPass.equals(getCPass)) {
                cpassValid.setText("Passwords don't match");
            }
            if (!getEmail.contains("@")) {
                emailValid.setText("Invalid format");
            }
            if (MySQL.isEmail(getEmail)) {
                emailValid.setText("Email already in use");
            }
        }
    }

    /**
     * Checks if all user input is valid
     * @param userTxt Username entered
     * @param emailTxt Email entered
     * @param password Password entered
     * @param cPassword Confirm password entered
     * @return true if all fields are valid, otherwise false
     */
    private boolean validate(String userTxt, String emailTxt, String password, String cPassword) {

        if (MySQL.isUser(userTxt)) { // Check if username is already used, if so reject registration
            return false;
        }
        else if (password.length() < 4) { // Check if password is less than 4 chara
            return false;
        }
        else if (!password.equals(cPassword)) { // Check if passwords match
            return false;
        }
        else if (!emailTxt.contains("@")) {
            return false;
        }
        else if (MySQL.isEmail(emailTxt)) {
            return false;
        }

        return true;
    }
}
