import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * The User page after login. Displays the storage units the user
 * has reserved, as well as the unit details when clicking a unit,
 * the ability to cancel reservations, and the ability to navigate
 * to the reservation page.
 */
public class UserGUI extends JPanel {

    private MyGUI myGui;
    private JLabel titleTxt;
    private JLabel cancelError;
    private String username;
    /**
     * An ArrayList that stores a list of reserved storage unit IDs, using the
     * getUserReservations() method from the MySQL class.
     */
    private static ArrayList<Integer> storageIDs;
    /**
     * Used for the unit details JList.
     * Dynamically displays selected storage unit information.
     */
    private DefaultListModel<Object> listModel;

    /**
     * Constructs the user page and initializes the GUI components.
     * @param myGUI reference to the main GUI controller
     * @param username the logged-in user's username
     */
    public UserGUI(MyGUI myGUI, String username) {
        this.myGui = myGUI;
        this.listModel = new DefaultListModel<>();
        this.username = username;
        int userid = MySQL.getUserID(username);
        final Integer[] currStorage = new Integer[1];
        this.storageIDs = (ArrayList<Integer>) MySQL.getUserReservations(userid);
        setLayout(new BorderLayout());
        GridBagConstraints gbc = new GridBagConstraints(); // Allows for ordered positioning

        // Create a panel with GridBagLayout
        JPanel background = new BackgroundSetter("/background_blur.jpg", new GridBagLayout()); // Sets the background
        JPanel panel = new JPanel(new GridBagLayout());
        gbc.fill = GridBagConstraints.HORIZONTAL; // Stretch components horizontally
        panel.setOpaque(false); // Allow background to show through
        background.add(panel, new GridBagConstraints()); // Add your content panel centered

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
        listTxt.setFont(new Font("SansSerif", Font.BOLD, 14));
        listTxt.setOpaque(true); // Allow for background of border to be colored
        listTxt.setBackground(Color.BLACK); // Set background border color
        listTxt.setForeground(Color.WHITE); // Set text color
        listTxt.setBorder(BorderFactory.createLineBorder(Color.pink)); // Create the border
        gbc.gridy = 1;
        gbc.gridx = 0;
        gbc.insets = new Insets(20, 30, 5, 0); // Add spacing
        gbc.anchor = GridBagConstraints.CENTER; // Keep this to the left
        panel.add(listTxt, gbc);

        // Storage Info text
        JLabel storageInfo = new JLabel("Storage Unit Details:");
        storageInfo.setFont(new Font("SansSerif", Font.BOLD, 14));
        storageInfo.setOpaque(true); // Allow for background of border to be colored
        storageInfo.setBackground(Color.BLACK); // Set background border color
        storageInfo.setForeground(Color.WHITE); // Set text color
        storageInfo.setBorder(BorderFactory.createLineBorder(Color.pink)); // Create the border
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
        idLabel.setOpaque(true); // Allow for background of border to be colored
        idLabel.setBackground(Color.BLACK); // Set background border color
        idLabel.setForeground(Color.WHITE); // Set text color
        idLabel.setBorder(BorderFactory.createLineBorder(Color.black)); // Create the border

        JLabel sizeLabel = new JLabel("Size:");
        sizeLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        sizeLabel.setOpaque(true); // Allow for background of border to be colored
        sizeLabel.setBackground(Color.BLACK); // Set background border color
        sizeLabel.setForeground(Color.WHITE); // Set text color
        sizeLabel.setBorder(BorderFactory.createLineBorder(Color.black)); // Create the border

        JLabel priceLabel = new JLabel("Price (USD):");
        priceLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        priceLabel.setOpaque(true); // Allow for background of border to be colored
        priceLabel.setBackground(Color.BLACK); // Set background border color
        priceLabel.setForeground(Color.WHITE); // Set text color
        priceLabel.setBorder(BorderFactory.createLineBorder(Color.black)); // Create the border

        JLabel locationLabel = new JLabel("Location:");
        locationLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        locationLabel.setOpaque(true); // Allow for background of border to be colored
        locationLabel.setBackground(Color.BLACK); // Set background border color
        locationLabel.setForeground(Color.WHITE); // Set text color
        locationLabel.setBorder(BorderFactory.createLineBorder(Color.black)); // Create the border

        // Create new sub-panel to add Storage Unit ID's to
        JPanel labelWrapper = new JPanel(new GridBagLayout());
        GridBagConstraints labelGbc = new GridBagConstraints();
        labelWrapper.setOpaque(false);
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
        cancelError = new JLabel("");
        cancelError.setFont(new Font("SansSerif", Font.ITALIC, 12));
        cancelError.setOpaque(true); // Allow for background of border to be colored
        cancelError.setBackground(Color.BLACK); // Set background border color
        cancelError.setForeground(Color.red); // Set text color
        cancelError.setBorder(BorderFactory.createLineBorder(Color.pink)); // Create the border
        gbc.gridy = 5;
        gbc.gridx = 1;
        gbc.insets = new Insets(0, 40, 5, 0); // Add spacing
        gbc.anchor = GridBagConstraints.EAST; // Keep this to the left
        panel.add(cancelError, gbc);

        // Cancel Reservation
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleCancellation(currStorage[0]);
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

        add(background, BorderLayout.CENTER);
    }

    /**
     * Navigates user to cancellation page if valid storage unit is selected,
     * otherwise gives an error message.
     * @param unit the selected storage unit for reservation cancellation
     */
    private void handleCancellation(Integer unit) {
        if (unit == null) {
            cancelError.setText("Please choose a Storage Unit");
        }
        else {
            cancelError.setText("");

            myGui.toCancellation(username, unit);
        }
    }

    /**
     * Refreshes the displayed unit details when the user selects a storage
     * unit.
     * @param unitDetails the list of details for the storage unit
     */
    private void refreshList(ArrayList<Object> unitDetails) {
        listModel.clear();
        for (Object obj : unitDetails) {
            listModel.addElement(obj);
        }
    }
}
