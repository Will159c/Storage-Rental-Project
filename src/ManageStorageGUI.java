import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * a) Class Name: ManageStorageGUI
 * b) Date of the Code: March 19, 2025
 * c) Programmers Names: Miguel Sicaja
 * d) Brief description: Admin GUI page for viewing all storage units and creating
 * storage units.
 * e) Brief explanation of important functions:
 * - createStorageUnit: Creates new storage unit into database if the provided input is valid
 * - validateStorage: Checks if the provided input is valid
 * - refreshList: Updates the unit details after selecting it from the
 * storage unit list.
 * - refreshStorageList: Allows for the list of all storage units to be
 * updated to include new created storage unit
 * f) Important Data Structures:
 * - MySQL: Allows for the list of storage units, storage unit details, checks of admin input,
 * and insertion of new unit to be implemented into the database
 * g) Algorithms used:
 * - Uses basic algorithm to check if admin input is valid and insert a new storage unit into the
 * database
 * - Uses lists such as ArrayList and DefaultListModel to display all storage units and their details
 */
public class ManageStorageGUI extends JPanel {

    private MyGUI myGui;
    /**
     * An ArrayList that stores a list of all storage unit IDs, using the
     * getStorage() method from the MySQL class.
     */
    private static ArrayList<Integer> storageIDs;
    /**
     * Used for the unit details JList.
     * Dynamically displays selected storage unit information.
     */
    private DefaultListModel<Object> listModel;
    private JList<Integer> list;
    private JLabel sizeValid;
    private JLabel priceValid;
    private JLabel locationValid;
    private JTextField sizeTxtField;
    private JTextField priceTxtField;
    private JTextField locationTxtField;
    private static String[] sizes = {"Small", "Medium", "Large"};
    private JComboBox sizeList;

    /**
     * Constructor for the ManageStorageGUI page, otherwise known as
     * the Create Storage Unit page. Creates storage units and displays
     * a list of all storage units and gives the ability to see each unit's
     * details.
     * @param myGui reference to the main GUI controller
     */
    public ManageStorageGUI(MyGUI myGui) {
        this.myGui = myGui;
        this.storageIDs = (ArrayList<Integer>) MySQL.getStorageID();
        this.listModel = new DefaultListModel<>();
        final Integer[] currStorage = new Integer[1]; // Save selected storage unit
        setLayout(new GridBagLayout());

        // Create a panel with GridBagLayout and scroller
        JPanel panel = new JPanel(new GridBagLayout());
        JScrollPane scroller = new JScrollPane(panel);
        scroller.setPreferredSize(new Dimension(800, 500));

        // wrap scroller to center it
        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        wrapper.add(scroller);

        // Grid bad constraints
        GridBagConstraints gbc = new GridBagConstraints(); // Allows for ordered positioning
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        // Title text
        JLabel titleTxt = new JLabel("Create Storage Units", SwingConstants.CENTER);
        titleTxt.setFont(new Font("SansSerif", Font.BOLD, 40));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3; // Ensure title spans all columns
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

        // Set size text combo box
        sizeList = new JComboBox(sizes);
        //sizeTxtField = new JTextField(15);
        gbc.gridy = 1;
        gbc.gridx = 1;
        panel.add(sizeList, gbc);

        // Confirm size is valid
        sizeValid = new JLabel("");
        sizeValid.setFont(new Font("SansSerif", Font.ITALIC, 10));
        sizeValid.setForeground(Color.red);
        gbc.gridy = 2;
        gbc.gridx = 1;
        gbc.weighty = 0;
        gbc.insets = new Insets(0, 40, 5, 0);
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(sizeValid, gbc);

        /// Set ID items ///
        ///// Price /////
        // Set price text
        JLabel priceTxt = new JLabel("Set Price (USD)");
        priceTxt.setFont(new Font("SansSerif", Font.PLAIN, 12));
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.insets = new Insets(20, 15, 5, 0); // Add spacing
        gbc.anchor = GridBagConstraints.EAST; // Keep this to the left
        panel.add(priceTxt, gbc);

        // Set price text field
        priceTxtField = new JTextField(15);
        gbc.gridy = 3;
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST; // Keep this to the right
        panel.add(priceTxtField, gbc);

        // Confirm price is valid
        priceValid = new JLabel("");
        priceValid.setFont(new Font("SansSerif", Font.ITALIC, 10));
        priceValid.setForeground(Color.red);
        gbc.gridy = 4;
        gbc.gridx = 1;
        gbc.insets = new Insets(0, 40, 5, 0);
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(priceValid, gbc);

        /// Set location items ///
        // Set location text
        JLabel locationTxt = new JLabel("Set Location");
        locationTxt.setFont(new Font("SansSerif", Font.PLAIN, 12));
        gbc.gridy = 5;
        gbc.gridx = 0;
        gbc.insets = new Insets(20, 15, 5, 0); // Add spacing
        gbc.anchor = GridBagConstraints.EAST; // Keep this to the left
        panel.add(locationTxt, gbc);

        // Set location text field
        locationTxtField = new JTextField(15);
        gbc.gridy = 5;
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST; // Keep this to the right
        panel.add(locationTxtField, gbc);

        // Confirm location is valid
        locationValid = new JLabel("");
        locationValid.setFont(new Font("SansSerif", Font.ITALIC, 10));
        locationValid.setForeground(Color.red);
        gbc.gridy = 6;
        gbc.gridx = 1;
        gbc.insets = new Insets(0, 40, 5, 0);
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(locationValid, gbc);


        ////////// Storage List Information ///////////
        // Storage List text
        JLabel listTxt = new JLabel("List of Storage Unit IDs: ");
        listTxt.setFont(new Font("SansSerif", Font.BOLD, 12));
        gbc.gridy = 7;
        gbc.gridx = 0;
        gbc.insets = new Insets(20, 15, 5, 0); // Add spacing
        gbc.anchor = GridBagConstraints.EAST; // Keep this to the left
        panel.add(listTxt, gbc);

        // List of storage units
        this.list = new JList<>();
        refreshStorageList();
        JScrollPane scrollPane = new JScrollPane(list);
        gbc.gridx = 1;
        gbc.gridy = 7;
        scrollPane.setPreferredSize(new Dimension(100, 100)); // Set fixed size for list
        panel.add(scrollPane, gbc);

        //////// Storage Unit details //////
        JLabel storageInfo = new JLabel("Storage Unit Details:");
        storageInfo.setFont(new Font("SansSerif", Font.BOLD, 14));
        gbc.gridy = 8;
        gbc.gridx = 0;
        gbc.insets = new Insets(16, 15, 0, 0); // Add spacing
        gbc.anchor = GridBagConstraints.EAST; // Keep this to the left
        panel.add(storageInfo, gbc);

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

        gbc.gridy = 9;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(0, 15, 0, 0);
        panel.add(labelWrapper, gbc);

        // Show unit details as a JList
        JList unitDeets = new JList<>(listModel);
        JScrollPane scrollPaneDeets = new JScrollPane(unitDeets);
        gbc.gridy = 9;
        gbc.gridx = 1;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        scrollPaneDeets.setPreferredSize(new Dimension(100, 100)); // Set fixed size for list
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        panel.add(scrollPaneDeets, gbc);

        // Show Storage Unit Details
        list.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    Integer selected = list.getSelectedValue();
                    if (selected != null) {
                        ArrayList<Object> unitDetails = new ArrayList<>(MySQL.getStorageInformation(selected));
                        refreshList(unitDetails);
                        currStorage[0] = list.getSelectedValue();
                    }
                }
            }
        });


        ///////// Buttons ////////
        // Delete storage unit button
        JButton deleteButton = new JButton("Delete Unit");
        deleteButton.setPreferredSize(new Dimension(120, 30));
        gbc.gridy = 10;
        gbc.gridx = 1;
        gbc.insets = new Insets(20, 0, 10, 0); // Add spacing in between buttons
        panel.add(deleteButton, gbc);

        // Delete button functionality
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Dialog box
                JOptionPane.showMessageDialog(null, "Storage Unit Deleted", "", JOptionPane.INFORMATION_MESSAGE);

                // Delete the unit
                MySQL.deleteStorageUnit(currStorage[0]);

                myGui.showMain("Admin Screen");
            }
        });

        // Back button
        JButton rBack = new JButton("Back");
        rBack.setPreferredSize(new Dimension(120, 30));
        gbc.gridy = 11;
        gbc.gridx = 0;
        gbc.insets = new Insets(20, 0, 10, 0); // Add spacing in between buttons
        panel.add(rBack, gbc);

        // Back button functionality
        rBack.addActionListener(e -> myGui.showMain("Admin Screen"));

        // Create Button
        JButton cButton = new JButton("Create Unit");
        cButton.setPreferredSize(new Dimension(120, 30));
        gbc.gridy = 11;
        gbc.gridx = 1;
        gbc.insets = new Insets(20, 80, 10, 0); // Add spacing in between buttons
        panel.add(cButton, gbc);

        // Create button functionality
        cButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String size = sizes[sizeList.getSelectedIndex()];
                String location = locationTxtField.getText();
                String price = priceTxtField.getText();

                createStorageUnit(size, location, price);
            }
        });

        add(wrapper, gbc);
    }

    /**
     * Handles the creation of a new storage unit given the admin's input.
     * Validates input and updates the database to include the new storage unit.
     * @param size the size of the storage unit
     * @param location the location of the storage unit
     * @param price the price of the storage unit set as a string
     */
    private void createStorageUnit(String size, String location, String price) {
        // Clear error messages
        sizeValid.setText("");
        priceValid.setText("");
        locationValid.setText("");

        if (validateStorage(size, location, price)) { // Create storage in system
            int priceInt = Integer.parseInt(price);
            MySQL.createNewStorageUnit(size, priceInt, location);

            // Clear textfields
            priceTxtField.setText("");
            locationTxtField.setText("");

            refreshStorageList();

            // Pop up window for success
            JOptionPane.showMessageDialog(null, "Storage Unit Created", "", JOptionPane.INFORMATION_MESSAGE);

            // Return to admin page
            myGui.showMain("Admin Screen");
        }
        else {
            if (size.isEmpty()) {
                sizeValid.setText("Enter a size");
            }
            if (location.isEmpty()) {
                locationValid.setText("Enter a location");
            }
            if(price.isEmpty()) {
                priceValid.setText("Enter price");
            }
            else {
                try {
                    Integer.parseInt(price);
                } catch (NumberFormatException ex) {
                    priceValid.setText("Enter valid price");
                }
            }
        }
    }

    /**
     * Helper method for validating the input given when creating a
     * storage unit.
     * @param size given size of the unit
     * @param location given location of the unit
     * @param price given price of the unit
     * @return true if all inputs are valid, otherwise false
     */
    private boolean validateStorage(String size, String location, String price) {

        if (size.isEmpty()) {
            return false;
        }
        else if (location.isEmpty()) {
            return false;
        }
        else if (price.isEmpty()) {
            return false;
        }

        try {
            Integer.parseInt(price);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    /**
     * Updates the unit details after selecting it from the
     * storage unit list.
     * @param unitDetails the list of storage unit details
     */
    private void refreshList(ArrayList<Object> unitDetails) {
        listModel.clear();
        for (Object obj : unitDetails) {
            listModel.addElement(obj);
        }
    }

    /**
     * Refreshes the list of storage units after successfully creating
     * a new unit.
     */
    public void refreshStorageList() { // Refresh page upon entering for updated Storage Units
        storageIDs = (ArrayList<Integer>) MySQL.getStorageID();
        list.setListData(storageIDs.toArray(new Integer[0]));
    }
}
