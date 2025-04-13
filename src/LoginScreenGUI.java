import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginScreenGUI extends JPanel {

    private MyGUI myGui;

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
        JTextField registerUserTxt = new JTextField(15);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(registerUserTxt, gbc);

        // Username validation message
        JLabel userValid = new JLabel("");
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
        JPasswordField passTxt = new JPasswordField(15);
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
        rConfirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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
        });

        // Add panel to LoginScreenGUI
        add(panel, BorderLayout.CENTER);
    }
}
