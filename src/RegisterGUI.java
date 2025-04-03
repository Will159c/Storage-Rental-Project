import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegisterGUI extends JPanel {

    private MyGUI myGui;

    public RegisterGUI(MyGUI myGui) {
        this.myGui = myGui;
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints(); // Allows for ordered positioning

        // Create a panel with GridBagLayout
        JPanel panel = new JPanel(new GridBagLayout());
        gbc.fill = GridBagConstraints.HORIZONTAL; // Stretch components horizontally

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
        gbc.gridy = 1;
        gbc.insets = new Insets(20, 15, 5, 0); // Add spacing
        gbc.anchor = GridBagConstraints.EAST; // Keep this to the left
        panel.add(userNameTxt, gbc);

        // Username text field
        JTextField registerUserTxt = new JTextField(15);
        gbc.gridy = 1;
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST; // Keep this to the right
        panel.add(registerUserTxt, gbc);

        // Confirm username is valid
        JLabel userValid = new JLabel("");
        userValid.setFont(new Font("SansSerif", Font.ITALIC, 10));
        userValid.setForeground(Color.red);
        gbc.gridy = 2;
        gbc.gridx = 1;
        gbc.insets = new Insets(0, 40, 5, 0);
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(userValid, gbc);

        ///////// Email Items //////////
        // Email text
        JLabel emailLabel = new JLabel("Enter email");
        emailLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.insets = new Insets(20, 15, 5, 0); // Add spacing
        gbc.anchor = GridBagConstraints.EAST; // Keep this to the left
        panel.add(emailLabel, gbc);

        // Email text field
        JTextField emailTxt = new JTextField(15);
        gbc.gridy = 3;
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST; // Keep this to the right
        panel.add(emailTxt, gbc);

        // Confirm email is valid
        JLabel emailValid = new JLabel("");
        emailValid.setFont(new Font("SansSerif", Font.ITALIC, 10));
        emailValid.setForeground(Color.red);
        gbc.gridy = 4;
        gbc.gridx = 1;
        gbc.insets = new Insets(0, 40, 5, 0);
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(emailValid, gbc);

        ////////// Password Items //////////
        // Password text
        JLabel passLabel = new JLabel("Enter password");
        passLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        gbc.gridy = 5;
        gbc.gridx = 0;
        gbc.insets = new Insets(20, 15, 5, 0); // Add spacing
        gbc.anchor = GridBagConstraints.EAST; // Keep this to the left
        panel.add(passLabel, gbc);

        // Password text field
        JPasswordField passTxt = new JPasswordField(15);
        gbc.gridy = 5;
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST; // Keep this to the right
        panel.add(passTxt, gbc);

        // Confirm password is valid
        JLabel passValid = new JLabel("");
        passValid.setFont(new Font("SansSerif", Font.ITALIC, 10));
        passValid.setForeground(Color.red);
        gbc.gridy = 6;
        gbc.gridx = 1;
        gbc.insets = new Insets(0, 0, 5, 0);
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(passValid, gbc);

        /////////// Confirm password stuff /////////////
        // Confirm Password text
        JLabel cpassLabel = new JLabel("Confirm password");
        cpassLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        gbc.gridy = 7;
        gbc.gridx = 0;
        gbc.insets = new Insets(20, 15, 5, 0); // Add spacing
        gbc.anchor = GridBagConstraints.EAST; // Keep this to the left
        panel.add(cpassLabel, gbc);

        // Confirm Password text field
        JPasswordField cpassTxt = new JPasswordField(15);
        gbc.gridy = 7;
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST; // Keep this to the right
        panel.add(cpassTxt, gbc);

        // Confirm password confirmation is valid
        JLabel cpassValid = new JLabel("");
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

                // Clear error messages
                userValid.setText("");
                passValid.setText("");
                cpassValid.setText("");
                emailValid.setText("");
                //emailValid.setText("Email already in use");

                // Check registration validity
                if (validate(getUser, getEmail, getPass, getCPass)) {
                    //System.out.println("Valid Registration");
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
                    if (!getPass.contains(getCPass)) {
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
        });

        add(panel);
    }

    public static boolean validate(String userTxt, String emailTxt, String password, String cPassword) {

        if (MySQL.isUser(userTxt)) { // Check if username is already used, if so reject registration
            return false;
        }
        else if (password.length() < 4) { // Check if password is less than 4 chara
            return false;
        }
        else if (!password.contains(cPassword)) {
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
