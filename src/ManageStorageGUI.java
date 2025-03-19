import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class ManageStorageGUI extends JPanel {

    private MyGUI myGui;
    private static ArrayList<Integer> storageIDs;

    public ManageStorageGUI(MyGUI myGui) {
        this.myGui = myGui;
        this.storageIDs = (ArrayList<Integer>) MySQL.getStorageID();
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints(); // Allows for ordered positioning

        // Create a panel with GridBagLayout
        JPanel panel = new JPanel(new GridBagLayout());
        gbc.fill = GridBagConstraints.HORIZONTAL; // Stretch components horizontally

        // Title text
        //JLabel titleTxt = new JLabel("<html><center>Manage<br>Storage Units</center></html>", SwingConstants.CENTER);
        JLabel titleTxt = new JLabel("Manage Storage Units", SwingConstants.CENTER);
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

        // Set size text field
        JTextField sizeTxtField = new JTextField(15);
        gbc.gridy = 1;
        gbc.gridx = 1;
        panel.add(sizeTxtField, gbc);

        // Confirm size is valid
        JLabel sizeValid = new JLabel("");
        sizeValid.setFont(new Font("SansSerif", Font.ITALIC, 10));
        sizeValid.setForeground(Color.red);
        gbc.gridy = 2;
        gbc.gridx = 1;
        gbc.weighty = 0;
        gbc.insets = new Insets(0, 40, 5, 0);
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(sizeValid, gbc);

        /// Set ID items ///
        // Set id text
        JLabel idTxt = new JLabel("Set Storage ID");
        idTxt.setFont(new Font("SansSerif", Font.PLAIN, 12));
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.insets = new Insets(20, 15, 5, 0); // Add spacing
        gbc.anchor = GridBagConstraints.EAST; // Keep this to the left
        panel.add(idTxt, gbc);

        // Set id text field
        JTextField idTxtField = new JTextField(15);
        gbc.gridy = 3;
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST; // Keep this to the right
        panel.add(idTxtField, gbc);

        // Confirm id is valid
        JLabel idValid = new JLabel("");
        idValid.setFont(new Font("SansSerif", Font.ITALIC, 10));
        idValid.setForeground(Color.red);
        gbc.gridy = 4;
        gbc.gridx = 1;
        gbc.insets = new Insets(0, 40, 5, 0);
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(idValid, gbc);

        /// Set location items ///
        // Set id text
        JLabel locationTxt = new JLabel("Set Location");
        locationTxt.setFont(new Font("SansSerif", Font.PLAIN, 12));
        gbc.gridy = 5;
        gbc.gridx = 0;
        gbc.insets = new Insets(20, 15, 5, 0); // Add spacing
        gbc.anchor = GridBagConstraints.EAST; // Keep this to the left
        panel.add(locationTxt, gbc);

        // Set id text field
        JTextField locationTxtField = new JTextField(15);
        gbc.gridy = 5;
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST; // Keep this to the right
        panel.add(locationTxtField, gbc);

        // Confirm id is valid
        JLabel locationValid = new JLabel("");
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
        JList<Integer> list = new JList<>(storageIDs.toArray(new Integer[0]));
        JScrollPane scrollPane = new JScrollPane(list);
        gbc.gridx = 1;
        gbc.gridy = 7;
        scrollPane.setPreferredSize(new Dimension(100, 100)); // Set fixed size for list
        panel.add(scrollPane, gbc);



        ///////// Buttons ////////

        // Back button
        JButton rBack = new JButton("Back");
        rBack.setPreferredSize(new Dimension(120, 30));
        gbc.gridy = 8;
        gbc.gridx = 0;
        gbc.insets = new Insets(20, 0, 10, 0); // Add spacing in between buttons
        panel.add(rBack, gbc);

        // Back button functionality
        rBack.addActionListener(e -> myGui.showMain("Admin Screen"));

        // Create Button
        JButton cButton = new JButton("Create Unit");
        cButton.setPreferredSize(new Dimension(120, 30));
        gbc.gridy = 8;
        gbc.gridx = 1;
        gbc.insets = new Insets(20, 80, 10, 0); // Add spacing in between buttons
        panel.add(cButton, gbc);

        // Create button functionality
        cButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String size = sizeTxtField.getText();
                String tempId = idTxtField.getText();
                String location = locationTxtField.getText();
                int id;

                // Clear error messages
                idValid.setText("");
                sizeValid.setText("");
                locationValid.setText("");

                if (validateStorage(size, tempId, location)) { // Create storage in system
                    id = Integer.parseInt(tempId);
                    MySQL.createNewStorageUnit(size, id, location);

                    // Clear error messages
                    idValid.setText("");
                    sizeValid.setText("");
                    locationValid.setText("");

                    // Pop up window for success
                    JOptionPane.showMessageDialog(null, "Storage Unit Created", "", JOptionPane.INFORMATION_MESSAGE);

                    // Return to admin page
                    myGui.showMain("Admin Screen");
                }
                else {
                    if (tempId.matches(".*\\D.*")) { // Error: character has non integer character
                        idValid.setText("ID has a non-integer character");
                    }
                    else if (tempId.isEmpty()) { // If nothing is entered in id field
                        idValid.setText("Enter a ID");
                    }
                    else {
                        id = Integer.parseInt(tempId);
                        if (isUnitInList(id)) { // Error: id is already in the system
                            idValid.setText("Entered ID already in system");
                        }
                        if (size.isEmpty()) {
                            sizeValid.setText("Enter a size");
                        }
                        if (location.isEmpty()) {
                            locationValid.setText("Enter a location");
                        }
                    }
                }
            }
        });

        add(panel);
    }

    public static boolean validateStorage(String size, String tempid, String location) {

        List<Object> hello = MySQL.getStorageInformation(231241235);
        for (Object i : hello) { System.out.println(i); }

        if (tempid.matches(".*\\D.*")) { // id has non integer character
            return false;
        }
        else if (tempid.isEmpty()) { // If nothing is entered in id field
            return false;
        }
        else {
            int id = Integer.parseInt(tempid);
            if (isUnitInList(id)) {
                return false;
            }
            else if (size.isEmpty()) {
                return false;
            }
            else if (location.isEmpty()) {
                return false;
            }
        }

        return true;
    }

    public static boolean isUnitInList(int id) { // Returns true if given id is already in the storage list

        for (int i = 0; i < storageIDs.size(); ++i) {
            if (storageIDs.get(i) == id) {
                return true;
            }
        }
        return false;
    }
}
