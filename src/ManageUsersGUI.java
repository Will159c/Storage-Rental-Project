import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ManageUsersGUI extends JPanel {
    private MyGUI myGui;

    public ManageUsersGUI(MyGUI myGUI) {
        this.myGui = myGUI;
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints(); // Allows for ordered positioning

        // Create a panel with GridBagLayout
        JPanel panel = new JPanel(new GridBagLayout());
        gbc.fill = GridBagConstraints.HORIZONTAL; // Stretch components horizontally

        // Title text
        JLabel titleTxt = new JLabel("Manage Users", SwingConstants.CENTER);
        titleTxt.setFont(new Font("SansSerif", Font.BOLD, 40));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // Ensure title spans all columns
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(titleTxt, gbc);

        // Reset constraints for next components
        gbc.gridwidth = 1;

        /// Delete User ///
        // Title text
        JLabel deleteUserTitle = new JLabel("Delete User");
        deleteUserTitle.setFont(new Font("SansSerif", Font.BOLD, 14));
        gbc.gridy = 1;
        gbc.gridx = 0;
        gbc.insets = new Insets(20, 15, 5, 0); // Add spacing
        gbc.anchor = GridBagConstraints.EAST; // Keep this to the left
        panel.add(deleteUserTitle, gbc);

        // Enter username text (delete user)
        JLabel deleteUserTxt = new JLabel("Enter Username");
        deleteUserTxt.setFont(new Font("SansSerif", Font.PLAIN, 12));
        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.insets = new Insets(20, 15, 5, 0); // Add spacing
        gbc.anchor = GridBagConstraints.EAST; // Keep this to the left
        panel.add(deleteUserTxt, gbc);

        // Username text field for deletion
        JTextField deleteUserTxtField = new JTextField(15);
        gbc.gridy = 2;
        gbc.gridx = 1;
        panel.add(deleteUserTxtField, gbc);

        // Error message for deleting user
        JLabel deleteUserError = new JLabel("");
        deleteUserError.setFont(new Font("SansSerif", Font.ITALIC, 10));
        deleteUserError.setForeground(Color.red);
        gbc.gridy = 3;
        gbc.gridx = 1;
        gbc.insets = new Insets(0, 40, 5, 0);
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(deleteUserError, gbc);

        // Delete Button
        JButton deleteButton = new JButton("Delete");
        deleteButton.setPreferredSize(new Dimension(120, 30));
        gbc.gridy = 4;
        gbc.gridx = 0;
        gbc.insets = new Insets(20, 0, 10, 0); // Add spacing in between buttons
        panel.add(deleteButton, gbc);

        /// Change user email ///
        // Title text
        JLabel updateEmailTitle = new JLabel("Update User Email");
        updateEmailTitle.setFont(new Font("SansSerif", Font.BOLD, 14));
        gbc.gridy = 5;
        gbc.gridx = 0;
        gbc.insets = new Insets(20, 15, 5, 0); // Add spacing
        gbc.anchor = GridBagConstraints.EAST; // Keep this to the left
        panel.add(updateEmailTitle, gbc);

        // Enter username text (update email)
        JLabel userEmailTxt = new JLabel("Enter Username");
        userEmailTxt.setFont(new Font("SansSerif", Font.PLAIN, 12));
        gbc.gridy = 6;
        gbc.gridx = 0;
        gbc.insets = new Insets(20, 15, 5, 0); // Add spacing
        gbc.anchor = GridBagConstraints.EAST; // Keep this to the left
        panel.add(userEmailTxt, gbc);

        // Username text field for email update
        JTextField userEmailTxtField = new JTextField(15);
        gbc.gridy = 6;
        gbc.gridx = 1;
        panel.add(userEmailTxtField, gbc);

        // Error message for username entered (update email)
        JLabel userEmailError = new JLabel("");
        userEmailError.setFont(new Font("SansSerif", Font.ITALIC, 10));
        userEmailError.setForeground(Color.red);
        gbc.gridy = 7;
        gbc.gridx = 1;
        gbc.insets = new Insets(0, 40, 5, 0);
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(userEmailError, gbc);

        // Enter email text (update email)
        JLabel emailTxt = new JLabel("Enter Username");
        emailTxt.setFont(new Font("SansSerif", Font.PLAIN, 12));
        gbc.gridy = 8;
        gbc.gridx = 0;
        gbc.insets = new Insets(20, 15, 5, 0); // Add spacing
        gbc.anchor = GridBagConstraints.EAST; // Keep this to the left
        panel.add(emailTxt, gbc);

        // Email text field for email update
        JTextField emailTxtField = new JTextField(15);
        gbc.gridy = 8;
        gbc.gridx = 1;
        panel.add(emailTxtField, gbc);

        // Error message for email entered (update email)
        JLabel emailError = new JLabel("");
        emailError.setFont(new Font("SansSerif", Font.ITALIC, 10));
        emailError.setForeground(Color.red);
        gbc.gridy = 9;
        gbc.gridx = 1;
        gbc.insets = new Insets(0, 40, 5, 0);
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(emailError, gbc);

        // Update Email Button
        JButton updateEmailButton = new JButton("Update Email");
        updateEmailButton.setPreferredSize(new Dimension(120, 30));
        gbc.gridy = 10;
        gbc.gridx = 0;
        gbc.insets = new Insets(20, 0, 10, 0); // Add spacing in between buttons
        panel.add(updateEmailButton, gbc);

        // Return button
        JButton returnButton = new JButton("Return");
        returnButton.setPreferredSize(new Dimension(120, 30));
        gbc.gridy = 11;
        gbc.gridx = 0;
        gbc.gridwidth = 2; // Ensure title spans all columns
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(returnButton, gbc);

        returnButton.addActionListener(e -> myGui.showMain("Admin Screen"));


        // Delete button functionality
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String user = deleteUserTxtField.getText();

                // Clear error messages
                deleteUserError.setText("");
                userEmailError.setText("");
                emailError.setText("");

                if (MySQL.isUser(user)) { // Delete the user
                    // Delete user
                    MySQL.deleteUser(user);

                    // Clear text fields
                    deleteUserTxtField.setText("");
                    userEmailTxtField.setText("");
                    emailTxtField.setText("");

                    // Pop up window for success
                    JOptionPane.showMessageDialog(null, "User Deleted", "", JOptionPane.INFORMATION_MESSAGE);

                    // Return to Admin page
                    myGui.showMain("Admin Screen");
                }
                else { // Display error message
                    deleteUserError.setText("User does not exist");
                }
            }
        });

        updateEmailButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String user = userEmailTxtField.getText();
                String email = emailTxtField.getText();

                // Clear error messages
                deleteUserError.setText("");
                userEmailError.setText("");
                emailError.setText("");

                if(validateEmailUpdate(user, email)) {
                    // Update user email
                    MySQL.setEmail(user, email);

                    // Clear text fields
                    deleteUserTxtField.setText("");
                    userEmailTxtField.setText("");
                    emailTxtField.setText("");

                    // Pop up window for success
                    JOptionPane.showMessageDialog(null, "User Email Updated", "", JOptionPane.INFORMATION_MESSAGE);

                    // Return to Admin page
                    myGui.showMain("Admin Screen");
                }
                else {
                    if (!MySQL.isUser(user)) { // If user is not in database, return false
                        userEmailError.setText("Username does not exist");
                    }
                    if (user.isEmpty()) {
                        userEmailError.setText("Please enter a username");
                    }
                    if (MySQL.isEmail(email)) { // If email is already in use, return false
                        emailError.setText("Email is already in use");
                    }
                    if (email.isEmpty()) {
                        emailError.setText("Please enter an email address");
                    }
                }
            }
        });

        add(panel);
    }

    public static boolean validateEmailUpdate(String user, String email) {

        if (!MySQL.isUser(user)) { // If user is not in database, return false
            return false;
        }
        else if (MySQL.isEmail(email)) { // If email is already in use, return false
            return false;
        }
        else if (email.isEmpty()) { // If nothing is given as an input for email
            return false;
        }
        else if (user.isEmpty()) { // If nothing is given as an input for the username
            return false;
        }

        return true;
    }
}
