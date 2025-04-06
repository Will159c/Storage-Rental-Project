import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CancelGUI extends JPanel {

    private MyGUI myGui;
    private String username;
    private int id;

    public CancelGUI(MyGUI myGUI, String username, int id) {
        this.myGui = myGUI;
        this.username = username;
        this.id = id;

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints(); // Allows for ordered positioning

        // Create a panel with GridBagLayout
        JPanel panel = new JPanel(new GridBagLayout());
        gbc.fill = GridBagConstraints.HORIZONTAL; // Stretch components horizontally

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
        gbc.gridy = 1;
        gbc.insets = new Insets(20, 15, 5, 0);
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(emailTxt, gbc);

        // Email text field
        JTextField emailFieldTxt = new JTextField(15);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(emailFieldTxt, gbc);

        // Credentials validation message
        JLabel credentialsValid = new JLabel("");
        credentialsValid.setFont(new Font("SansSerif", Font.ITALIC, 10));
        credentialsValid.setForeground(Color.red);
        gbc.gridy = 2;
        gbc.gridx = 1;
        gbc.insets = new Insets(0, 40, 5, 0);
        panel.add(credentialsValid, gbc);

        // Password label
        JLabel passLabel = new JLabel("Enter Password:");
        passLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
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
                String email = emailFieldTxt.getText();
                String password = String.valueOf(passTxt.getPassword());
                if (cancelValidation(username, email, password)) {
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
        });

        add(panel);
    }

    public static boolean cancelValidation(String user, String email, String password) {
        String realEmail = MySQL.getEmailByUsername(user);

        if (!MySQL.isUsernameAndPassword(user, password)) {
            return false;
        }
        if (!email.equals(realEmail)) {
            return false;
        }
        return true;
    }
}
