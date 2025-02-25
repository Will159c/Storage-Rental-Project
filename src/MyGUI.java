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
    private static JLabel userNameTxt;
    private static JLabel registerTitle;
    private static JTextField registerUserTxt;
    private static JLabel userValid;
    private static JLabel emailLabel;
    private static JTextField emailTxt;
    private static JLabel emailValid;
    private static JLabel passLabel;
    private static JTextField passTxt;
    private static JLabel passValid;
    private static JLabel cpassLabel;
    private static JTextField cpassTxt;
    private static JLabel cpassValid;
    private static JButton rConfirm;
    private static JButton rBack;
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
        cardpanel.add(loginScreen(), "Login");

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

        // Register Button action
        registerButton.addActionListener(e -> cardLayout.show(cardpanel, "Registration"));

        // Login Button
        loginButton = new JButton("Log-in");
        loginButton.setPreferredSize(new Dimension(180, 50));
        gbc.gridy = 2; // Move button to next row
        gbc.insets = new Insets(20, 0, 10, 0); // Add spacing in between buttons and labels
        panel.add(loginButton, gbc);

        // Login button action
        loginButton.addActionListener(e -> cardLayout.show(cardpanel, "Login"));


        return panel;
    }

    private JPanel registerScreen() {
        ///////////// Initial Settings ///////////
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints(); // Allows for ordered positioning

        // Register Text
        registerTitle = new JLabel("Registration", SwingConstants.CENTER);
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
        userNameTxt = new JLabel("Enter Username");
        userNameTxt.setFont(new Font("SansSerif", Font.PLAIN, 12));
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
        userValid = new JLabel("Invalid username");
        userValid.setFont(new Font("SansSerif", Font.ITALIC, 10));
        userValid.setForeground(Color.red);
        gbc.gridy = 2;
        gbc.gridx = 1;
        gbc.insets = new Insets(0, 40, 5, 0);
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(userValid, gbc);

        ///////// Email Items //////////
        // Email text
        emailLabel = new JLabel("Enter email");
        emailLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
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
        emailValid = new JLabel("Email already in use");
        emailValid.setFont(new Font("SansSerif", Font.ITALIC, 10));
        emailValid.setForeground(Color.red);
        gbc.gridy = 4;
        gbc.gridx = 1;
        gbc.insets = new Insets(0, 40, 5, 0);
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(emailValid, gbc);

        ////////// Password Items //////////
        // Password text
        passLabel = new JLabel("Enter password");
        passLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        gbc.gridy = 5;
        gbc.gridx = 0;
        gbc.insets = new Insets(20, 15, 5, 0); // Add spacing
        gbc.anchor = GridBagConstraints.EAST; // Keep this to the left
        panel.add(passLabel, gbc);

        // Password text field
        passTxt = new JTextField(15);
        gbc.gridy = 5;
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST; // Keep this to the right
        panel.add(passTxt, gbc);

        // Confirm password is valid
        passValid = new JLabel("Password must be at least 4 characters");
        passValid.setFont(new Font("SansSerif", Font.ITALIC, 10));
        passValid.setForeground(Color.red);
        gbc.gridy = 6;
        gbc.gridx = 1;
        gbc.insets = new Insets(0, 0, 5, 0);
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(passValid, gbc);

        /////////// Confirm password stuff /////////////
        // Confirm Password text
        cpassLabel = new JLabel("Confirm password");
        cpassLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        gbc.gridy = 7;
        gbc.gridx = 0;
        gbc.insets = new Insets(20, 15, 5, 0); // Add spacing
        gbc.anchor = GridBagConstraints.EAST; // Keep this to the left
        panel.add(cpassLabel, gbc);

        // Confirm Password text field
        cpassTxt = new JTextField(15);
        gbc.gridy = 7;
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST; // Keep this to the right
        panel.add(cpassTxt, gbc);

        // Confirm password confirmation is valid
        cpassValid = new JLabel("Passwords don't match");
        cpassValid.setFont(new Font("SansSerif", Font.ITALIC, 10));
        cpassValid.setForeground(Color.red);
        gbc.gridy = 8;
        gbc.gridx = 1;
        gbc.insets = new Insets(0, 40, 5, 0);
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(cpassValid, gbc);


        //////// Buttons /////////

        /// Back button
        // Back button Design
        rBack = new JButton("Back");
        rBack.setPreferredSize(new Dimension(120, 30));
        gbc.gridy = 9;
        gbc.gridx = 0;
        gbc.insets = new Insets(20, 0, 10, 0); // Add spacing in between buttons
        panel.add(rBack, gbc);

        // Back Button Function
        rBack.addActionListener(e -> cardLayout.show(cardpanel, "Welcome Screen"));

        /// Confirmation Button

        // Confirm Button design
        rConfirm = new JButton("Confirm");
        rConfirm.setPreferredSize(new Dimension(120, 30));
        gbc.gridy = 9;
        gbc.gridx = 1;
        gbc.insets = new Insets(20, 80, 10, 0); // Add spacing in between buttons
        panel.add(rConfirm, gbc);

        // Confirm button functionality
        rConfirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("I have to go to work now lololol");
            }
        });

        return panel;
    }

    private JPanel loginScreen() {
        ///////////// Initial Settings ///////////
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints(); // Allows for ordered positioning

        // Register Text
        titleTxt = new JLabel("Log-in", SwingConstants.CENTER);
        titleTxt.setFont(new Font("SansSerif", Font.BOLD, 40));
        titleTxt.setMinimumSize(new Dimension(400, 400));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // Both this line and the line below ensures title is centered
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(titleTxt, gbc);

        // Reset constraints for next components
        gbc.gridwidth = 1;

        ////// Username Items //////
        // Username text
        userNameTxt = new JLabel("Enter Username");
        userNameTxt.setFont(new Font("SansSerif", Font.PLAIN, 12));
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
        userValid = new JLabel("Invalid username");
        userValid.setFont(new Font("SansSerif", Font.ITALIC, 10));
        userValid.setForeground(Color.red);
        gbc.gridy = 2;
        gbc.gridx = 1;
        gbc.insets = new Insets(0, 40, 5, 0);
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(userValid, gbc);

        ////////// Password Items //////////
        // Password text
        passLabel = new JLabel("Enter password");
        passLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.insets = new Insets(20, 15, 5, 0); // Add spacing
        gbc.anchor = GridBagConstraints.EAST; // Keep this to the left
        panel.add(passLabel, gbc);

        // Password text field
        passTxt = new JTextField(15);
        gbc.gridy = 3;
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST; // Keep this to the right
        panel.add(passTxt, gbc);

        // Confirm password is valid
        passValid = new JLabel("Invalid Password");
        passValid.setFont(new Font("SansSerif", Font.ITALIC, 10));
        passValid.setForeground(Color.red);
        gbc.gridy = 4;
        gbc.gridx = 1;
        gbc.insets = new Insets(0, 40, 5, 0);
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(passValid, gbc);

        //////// Buttons /////////

        /// Back button
        // Back button Design
        rBack = new JButton("Back");
        rBack.setPreferredSize(new Dimension(120, 30));
        gbc.gridy = 5;
        gbc.gridx = 0;
        gbc.insets = new Insets(20, 0, 10, 0); // Add spacing in between buttons
        panel.add(rBack, gbc);

        // Back Button Function
        rBack.addActionListener(e -> cardLayout.show(cardpanel, "Welcome Screen"));

        /// Confirmation Button

        // Confirm Button design
        rConfirm = new JButton("Confirm");
        rConfirm.setPreferredSize(new Dimension(120, 30));
        gbc.gridy = 5;
        gbc.gridx = 1;
        gbc.insets = new Insets(20, 80, 10, 0); // Add spacing in between buttons
        panel.add(rConfirm, gbc);

        // Confirm button functionality
        rConfirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("I have to go to work now lololol");
            }
        });


        return panel;
    }
}
