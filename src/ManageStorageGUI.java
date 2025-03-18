import javax.swing.*;
import java.awt.*;

public class ManageStorageGUI extends JPanel {
    private MyGUI myGui;

    public ManageStorageGUI(MyGUI myGui) {
        this.myGui = myGui;
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints(); // Allows for ordered positioning

        // Create a panel with GridBagLayout
        JPanel panel = new JPanel(new GridBagLayout());
        gbc.fill = GridBagConstraints.HORIZONTAL; // Stretch components horizontally

        // Title text
        JLabel titleTxt = new JLabel("Manage Storage Units", SwingConstants.CENTER);
        titleTxt.setFont(new Font("SansSerif", Font.BOLD, 40));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // Ensure title spans both columns
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(titleTxt, gbc);

        // Reset constraints for next components
        gbc.gridwidth = 1;

        ////// Set size Items //////
        // Set size text
        JLabel sizeTxt = new JLabel("Set Storage Size");
        sizeTxt.setFont(new Font("SansSerif", Font.PLAIN, 12));
        gbc.gridy = 1;
        gbc.insets = new Insets(20, 15, 5, 0); // Add spacing
        gbc.anchor = GridBagConstraints.EAST; // Keep this to the left
        panel.add(sizeTxt, gbc);

        // Set size text field
        JTextField sizeTxtField = new JTextField(15);
        gbc.gridy = 1;
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST; // Keep this to the right
        panel.add(sizeTxtField, gbc);

        // Confirm username is valid
        JLabel userValid = new JLabel("");
        userValid.setFont(new Font("SansSerif", Font.ITALIC, 10));
        userValid.setForeground(Color.red);
        gbc.gridy = 2;
        gbc.gridx = 1;
        gbc.insets = new Insets(0, 40, 5, 0);
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(userValid, gbc);

        add(panel);
    }
}
