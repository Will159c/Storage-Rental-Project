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
        JLabel userValid = new JLabel("Invalid username");
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
        JLabel emailValid = new JLabel("Email already in use");
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
        JTextField passTxt = new JTextField(15);
        gbc.gridy = 5;
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST; // Keep this to the right
        panel.add(passTxt, gbc);

        // Confirm password is valid
        JLabel passValid = new JLabel("Password must be at least 4 characters");
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
        JTextField cpassTxt = new JTextField(15);
        gbc.gridy = 7;
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST; // Keep this to the right
        panel.add(cpassTxt, gbc);

        // Confirm password confirmation is valid
        JLabel cpassValid = new JLabel("Passwords don't match");
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
                System.out.println("Hot dog water");
            }
        });

        add(panel);
    }
}
