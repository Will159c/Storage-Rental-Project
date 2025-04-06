import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class UserGUI extends JPanel {

    private MyGUI myGui;
    private JLabel titleTxt;
    private String username;
    private String email;
    private String password;
    private static ArrayList<Integer> storageIDs;
    private DefaultListModel<Object> listModel;

    public UserGUI(MyGUI myGUI, String username) {
        this.myGui = myGUI;
        this.listModel = new DefaultListModel<>();
        this.username = username;
        int userid = MySQL.getUserID(username);
        final Integer[] currStorage = new Integer[1];
        this.storageIDs = (ArrayList<Integer>) MySQL.getUserReservations(userid);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints(); // Allows for ordered positioning

        // Create a panel with GridBagLayout
        JPanel panel = new JPanel(new GridBagLayout());
        gbc.fill = GridBagConstraints.HORIZONTAL; // Stretch components horizontally

        // Title text
        titleTxt = new JLabel("Welcome " + username, SwingConstants.CENTER);
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
        gbc.insets = new Insets(20, 30, 5, 0); // Add spacing
        gbc.anchor = GridBagConstraints.CENTER; // Keep this to the left
        panel.add(listTxt, gbc);

        // Storage Info text
        JLabel storageInfo = new JLabel("Storage Unit Details:");
        storageInfo.setFont(new Font("SansSerif", Font.BOLD, 12));
        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.insets = new Insets(16, 15, 0, 0); // Add spacing
        gbc.anchor = GridBagConstraints.EAST; // Keep this to the left
        panel.add(storageInfo, gbc);

        /////// Storage unit list and details /////////
        // List of reserved units
        JList<Integer> list = new JList<>(storageIDs.toArray(new Integer[0]));
        JScrollPane scrollPane = new JScrollPane(list);
        gbc.gridy = 1;
        gbc.gridx = 1;
        scrollPane.setPreferredSize(new Dimension(100, 100)); // Set fixed size for list
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        panel.add(scrollPane, gbc);

        // Add labels to Unit details
        JLabel idLabel = new JLabel("ID:");
        idLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));

        JLabel sizeLabel = new JLabel("Size:");
        sizeLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));

        JLabel priceLabel = new JLabel("Price (USD):");
        priceLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));

        JLabel locationLabel = new JLabel("Location:");
        locationLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));

        // Create new sub-panel to add Storage Unit ID's to
        JPanel labelWrapper = new JPanel(new GridBagLayout());
        GridBagConstraints labelGbc = new GridBagConstraints();
        labelGbc.anchor = GridBagConstraints.CENTER;
        labelGbc.insets = new Insets(1, 100, 1, 0); // small vertical padding
        labelGbc.gridx = 0;

        labelGbc.gridy = 0;
        labelWrapper.add(idLabel, labelGbc);
        labelGbc.gridy = 1;
        labelWrapper.add(sizeLabel, labelGbc);
        labelGbc.gridy = 2;
        labelWrapper.add(priceLabel, labelGbc);
        labelGbc.gridy = 3;
        labelWrapper.add(locationLabel, labelGbc);

        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(0, 15, 0, 0);
        panel.add(labelWrapper, gbc);

        // Show unit details as a JList
        JList unitDeets = new JList<>(listModel);
        JScrollPane scrollPaneDeets = new JScrollPane(unitDeets);
        gbc.gridy = 3;
        gbc.gridx = 1;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        scrollPaneDeets.setPreferredSize(new Dimension(100, 100)); // Set fixed size for list
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        panel.add(scrollPaneDeets, gbc);

        // Give Storage unit details
        list.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) { // Prevents multiple events
                    ArrayList<Object> unitDetails = new ArrayList<Object>(MySQL.getStorageInformation(list.getSelectedValue()));
                    refreshList(unitDetails);
                    currStorage[0] = list.getSelectedValue();
                }
            }
        });


        ////////// Buttons ///////////

        // Cancel Reservation Button
        JButton cancelButton = new JButton("Cancel Reservation");
        cancelButton.setPreferredSize(new Dimension(130, 30));
        gbc.gridy = 4;
        gbc.gridx = 1;
        gbc.insets = new Insets(20, 0, 10, 0);
        panel.add(cancelButton, gbc);

        // Error message for canceling reservation
        JLabel cancelError = new JLabel("");
        cancelError.setFont(new Font("SansSerif", Font.ITALIC, 12));
        cancelError.setForeground(Color.red);
        gbc.gridy = 5;
        gbc.gridx = 1;
        gbc.insets = new Insets(0, 40, 5, 0); // Add spacing
        gbc.anchor = GridBagConstraints.EAST; // Keep this to the left
        panel.add(cancelError, gbc);

        // Cancel Reservation
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currStorage[0] == null) {
                    cancelError.setText("Please a Storage Unit");
                }
                else {
                    cancelError.setText("");
                    System.out.println(currStorage[0]);
                }
            }
        });

        ///////// Storage Button
        JButton reserveButton = new JButton("Reserve Units");
        reserveButton.setPreferredSize(new Dimension(130, 30));
        gbc.gridy = 6;
        gbc.gridx = 0;
        gbc.insets = new Insets(20, 0, 10, 0);
        panel.add(reserveButton, gbc);

        // Go to reserve a new unit
        reserveButton.addActionListener(e -> myGui.showMain("Storage Screen"));

        //////// Log Out Button
        JButton rBack = new JButton("Log Out");
        rBack.setPreferredSize(new Dimension(130, 30));
        gbc.gridy = 6;
        gbc.gridx = 1;
        gbc.insets = new Insets(20, 80, 10, 0);
        panel.add(rBack, gbc);

        // Log out and return to home screen!!!!!!!11!!
        rBack.addActionListener(e -> myGui.showMain("Welcome Screen"));

        add(panel);
    }

    private void refreshList(ArrayList<Object> unitDetails) {
        listModel.clear();
        for (Object obj : unitDetails) {
            listModel.addElement(obj);
        }
    }
}
