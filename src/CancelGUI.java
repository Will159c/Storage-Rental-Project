import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * GUI for the user to cancel their reservation for a storage
 * unit. Necessitates the user to verify their credentials and
 * confirm cancellation.
 */
public class CancelGUI extends JPanel {

    private MyGUI myGui;
    private String username;
    private int id;
    private JLabel credentialsValid;

    /**
     * Constructs the cancellation page. Contains a field for
     * the user to reenter their password and confirm their
     * cancellation. If the user wishes they can return to
     * the user menu.
     * @param myGUI reference to the main GUI controller
     * @param username the user's username
     * @param id the ID of the storage unit to cancel
     */
    public CancelGUI(MyGUI myGUI, String username, int id) {
        this.myGui = myGUI;
        this.username = username;
        this.id = id;
        setLayout(new BorderLayout());

        // Create a panel with GridBagLayout
        JPanel panel = new BackgroundSetter("/background_blur.jpg", new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints(); // Allows for ordered positioning

        // Initial settings
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.anchor = GridBagConstraints.CENTER;

        ///////// Labels, text fields, and buttons

        // Title
        JLabel titleTxt = new JLabel("<html><center>Enter Information<br>To Cancel</center></html>", SwingConstants.CENTER);
        titleTxt.setFont(new Font("SansSerif", Font.BOLD, 40));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // Ensure title spans both columns
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(titleTxt, gbc);

        gbc.gridwidth = 1; // Reset width

        // Email label
        JLabel emailTxt = new JLabel("Enter Email:");
        emailTxt.setFont(new Font("SansSerif", Font.PLAIN, 12));
        emailTxt.setOpaque(true); // Allow for background of border to be colored
        emailTxt.setBackground(Color.BLACK); // Set background border color
        emailTxt.setForeground(Color.WHITE); // Set text color
        emailTxt.setBorder(BorderFactory.createLineBorder(Color.black)); // Create the border
        gbc.gridy = 1;
        gbc.insets = new Insets(20, 15, 5, 0);
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(emailTxt, gbc);

        // Email text field
        String email = MySQL.getEmailByUsername(username);
        JLabel emailLabel = new JLabel(email);
        emailLabel.setOpaque(true); // Allow for background of border to be colored
        emailLabel.setBackground(Color.BLACK); // Set background border color
        emailLabel.setForeground(Color.WHITE); // Set text color
        emailLabel.setBorder(BorderFactory.createLineBorder(Color.black)); // Create the border
        panel.add(emailLabel, gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(emailLabel, gbc);

        // Credentials validation message
        credentialsValid = new JLabel("");
        credentialsValid.setFont(new Font("SansSerif", Font.ITALIC, 10));
        credentialsValid.setOpaque(true); // Allow for background of border to be colored
        credentialsValid.setBackground(Color.BLACK); // Set background border color
        credentialsValid.setForeground(Color.red); // Set text color
        credentialsValid.setBorder(BorderFactory.createLineBorder(Color.black)); // Create the border
        gbc.gridy = 2;
        gbc.gridx = 1;
        gbc.insets = new Insets(0, 40, 5, 0);
        panel.add(credentialsValid, gbc);

        // Password label
        JLabel passLabel = new JLabel("Enter Password:");
        passLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        passLabel.setOpaque(true); // Allow for background of border to be colored
        passLabel.setBackground(Color.BLACK); // Set background border color
        passLabel.setForeground(Color.WHITE); // Set text color
        passLabel.setBorder(BorderFactory.createLineBorder(Color.black)); // Create the border
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

        //////// Buttons /////////
        // Back Button
        JButton rBack = new JButton("Back");
        rBack.setPreferredSize(new Dimension(120, 30));
        gbc.gridy = 4;
        gbc.gridx = 0;
        gbc.insets = new Insets(20, 0, 10, 0);
        panel.add(rBack, gbc);

        // Back button functionality
        rBack.addActionListener(new ActionListener() { // Return user to user screen
            @Override
            public void actionPerformed(ActionEvent e) {
                credentialsValid.setText("");
                myGui.loginUser(username);
            }
        });

        // Confirmation button
        JButton cButton = new JButton("Confirm");
        cButton.setPreferredSize(new Dimension(120, 30));
        gbc.gridy = 4;
        gbc.gridx = 1;
        gbc.insets = new Insets(20, 80, 10, 0);
        panel.add(cButton, gbc);

        cButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = emailLabel.getText();
                String password = String.valueOf(passTxt.getPassword());

                cancelUnit(password, email);
            }
        });

        add(panel, BorderLayout.CENTER);
    }

    /**
     * Cancels the reservation for the selected storage unit if the correct
     * password is given. If successful, dialog box pops up confirming the
     * cancellation or an error message appears if incorrect password is
     * given.
     * @param password the user's password
     * @param email the user's email
     */
    private void cancelUnit(String password, String email) {
        if (cancelValidation(username, password)) {
            credentialsValid.setText("");
            // Success message
            JOptionPane.showMessageDialog(null, "Reservation Cancelled", "", JOptionPane.INFORMATION_MESSAGE);

            // Cancel reservation
            MySQL.cancelReservation(id, email, password);

            myGui.loginUser(username);
        }
        else {
            credentialsValid.setText("Incorrect credentials");
        }
    }

    /**
     * Validates whether the username and given password match in the
     * database.
     * @param user the username
     * @param password the given password
     * @return true is credentials are valid, false otherwise
     */
    private boolean cancelValidation(String user, String password) {

        if (!MySQL.isUsernameAndPassword(user, password)) {
            return false;
        }

        return true;
    }
}
