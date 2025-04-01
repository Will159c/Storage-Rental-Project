import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class UserGUI extends JPanel {

    private MyGUI myGui;
    private JLabel titleTxt;
    private String username;
    private static ArrayList<Integer> storageIDs = new ArrayList<Integer>();

    public UserGUI(MyGUI myGUI) {
        this.myGui = myGUI;
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints(); // Allows for ordered positioning

        // Create a panel with GridBagLayout
        JPanel panel = new JPanel(new GridBagLayout());
        gbc.fill = GridBagConstraints.HORIZONTAL; // Stretch components horizontally

        // Title text
        titleTxt = new JLabel("", SwingConstants.CENTER);
        titleTxt.setFont(new Font("SansSerif", Font.BOLD, 40));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // Ensure title spans both columns
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(titleTxt, gbc);

        gbc.gridwidth = 1; // Reset width

        // Storage List text
        JLabel listTxt = new JLabel("Your Storage Units:");
        listTxt.setFont(new Font("SansSerif", Font.BOLD, 12));
        gbc.gridy = 1;
        gbc.gridx = 0;
        gbc.insets = new Insets(20, 15, 5, 0); // Add spacing
        gbc.anchor = GridBagConstraints.EAST; // Keep this to the left
        panel.add(listTxt, gbc);

        // Storage Info text
        JLabel storageInfo = new JLabel("Storage Unit Information:");
        storageInfo.setFont(new Font("SansSerif", Font.BOLD, 12));
        gbc.gridy = 1;
        gbc.gridx = 1;
        gbc.insets = new Insets(20, 15, 5, 0); // Add spacing
        gbc.anchor = GridBagConstraints.EAST; // Keep this to the left
        panel.add(storageInfo, gbc);

        // Display storage unit list
        JList<Integer> list = new JList<>(storageIDs.toArray(new Integer[0]));
        JScrollPane scrollPane = new JScrollPane(list);
        gbc.gridx = 0;
        gbc.gridy = 2;
        scrollPane.setPreferredSize(new Dimension(100, 100)); // Set fixed size for list
        panel.add(scrollPane, gbc);

        ////////// Buttons ///////////

        ///////// Storage Button
        JButton reserveButton = new JButton("Reserve Units");
        reserveButton.setPreferredSize(new Dimension(130, 30));
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.insets = new Insets(20, 0, 10, 0);
        panel.add(reserveButton, gbc);

        // Go to reserve a new unit
        reserveButton.addActionListener(e -> myGui.showMain("Storage Screen"));

        //////// Log Out Button
        JButton rBack = new JButton("Log Out");
        rBack.setPreferredSize(new Dimension(130, 30));
        gbc.gridy = 3;
        gbc.gridx = 1;
        gbc.insets = new Insets(20, 80, 10, 0);
        panel.add(rBack, gbc);

        // Log out and return to home screen!!!!!!!11!!
        rBack.addActionListener(e -> myGui.showMain("Welcome Screen"));

        add(panel);
    }

    public void setUsername(String username) { // Set up
        this.username = username;
        titleTxt.setText("Welcome " + username);
    }

    public String getUsername() {
        return username;
    }
}
