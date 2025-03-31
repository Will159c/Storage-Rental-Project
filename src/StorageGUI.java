import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.util.Comparator;
import javax.swing.text.PlainDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;


public class StorageGUI extends JPanel {
    private MyGUI myGui;
    private JPanel squaresPanel;
    private JScrollPane scrollPane;

    // NEW: In-memory list of storage units to avoid repeated DB calls
    private List<MySQL.StorageDetails> allUnits;

    // Removed the original inner StorageDetails class; now using MySQL.StorageDetails

    public StorageGUI(MyGUI myGui) {
        this.myGui = myGui;
        setLayout(new BorderLayout());

        // NEW: Load all storage units in one call
        allUnits = MySQL.getAllStorageDetails();

        // title
        JLabel titleTxt = new JLabel("Storage Units Browser", SwingConstants.CENTER);
        titleTxt.setFont(new Font("SansSerif", Font.BOLD, 20));
        add(titleTxt, BorderLayout.NORTH);

        // 2 buttons for the 2 search types
        JPanel topBtnPanel = new JPanel();
        JButton viewAllBtn = new JButton("View All Units");
        JButton viewAvailBtn = new JButton("View Available Units");

        // combo box for price sort options
        String[] priceOptions = {
                "Sort by Price: Lowest to Highest",
                "Sort by Price: Highest to Lowest"
        };
        JComboBox<String> priceSortCombo = new JComboBox<>(priceOptions);
        priceSortCombo.addActionListener(e -> {
            if (priceSortCombo.getSelectedIndex() == 0) {
                showUnitsSortedByPrice(true);
            } else {
                showUnitsSortedByPrice(false);
            }
        });

        // combo box for size sort options
        String[] sizeOptions = {
                "Sort by Size: Smallest to Biggest",
                "Sort by Size: Biggest to Smallest"
        };
        JComboBox<String> sizeSortCombo = new JComboBox<>(sizeOptions);
        sizeSortCombo.addActionListener(e -> {
            // Here, adjust the order as needed (true/false) based on your original behavior
            if (sizeSortCombo.getSelectedIndex() == 0) {
                showUnitsSortedBySize(false);
            } else {
                showUnitsSortedBySize(true);
            }
        });

        topBtnPanel.add(viewAllBtn);
        topBtnPanel.add(viewAvailBtn);
        topBtnPanel.add(priceSortCombo);
        topBtnPanel.add(sizeSortCombo);

        Set<String> locationSet = new HashSet<>();
        for (MySQL.StorageDetails sd : allUnits) {
            locationSet.add(sd.getLocation());
        }
        List<String> locations = new ArrayList<>(locationSet);
        Collections.sort(locations);  // sort alphabetically
        String[] locationOptions = new String[locations.size() + 1];
        locationOptions[0] = "All Locations";
        for (int i = 0; i < locations.size(); i++) {
            locationOptions[i + 1] = locations.get(i);
        }
        JComboBox<String> locationCombo = new JComboBox<>(locationOptions);
        locationCombo.addActionListener(e -> {
            String selected = (String) locationCombo.getSelectedItem();
            if (selected.equals("All Locations")) {
                showAllUnits();
            } else {
                showUnitsFilteredByLocation(selected);
            }
        });
        topBtnPanel.add(locationCombo);

        add(topBtnPanel, BorderLayout.PAGE_START);

        // it lets us scroll when there are more units than the screen fits
        squaresPanel = new JPanel();
        squaresPanel.setLayout(new GridLayout(0, 4, 10, 10));
        squaresPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        scrollPane = new JScrollPane(squaresPanel);
        add(scrollPane, BorderLayout.CENTER);

        // a back button that sends the user back to the welcome screen
        JButton backBtn = new JButton("Back");
        backBtn.addActionListener(e -> myGui.showMain("Welcome Screen"));
        add(backBtn, BorderLayout.SOUTH);

        // directs the button to what it will execute
        viewAllBtn.addActionListener(e -> showAllUnits());
        viewAvailBtn.addActionListener(e -> showAvailableUnits());
    }

    private void showAllUnits() {
        squaresPanel.removeAll();
        for (MySQL.StorageDetails sd : allUnits) {
            squaresPanel.add(createStorageSquare(sd));
        }
        squaresPanel.revalidate();
        squaresPanel.repaint();
    }

    private void showAvailableUnits() {
        squaresPanel.removeAll();
        for (MySQL.StorageDetails sd : allUnits) {
            if (!sd.isReserved()) {
                squaresPanel.add(createStorageSquare(sd));
            }
        }
        squaresPanel.revalidate();
        squaresPanel.repaint();
    }

    private void showUnitsSortedByPrice(boolean ascending) {
        squaresPanel.removeAll();
        List<MySQL.StorageDetails> sorted = new ArrayList<>(allUnits);
        if (ascending) {
            sorted.sort(Comparator.comparingInt(MySQL.StorageDetails::getPrice));
        } else {
            sorted.sort(Comparator.comparingInt(MySQL.StorageDetails::getPrice).reversed());
        }
        for (MySQL.StorageDetails sd : sorted) {
            squaresPanel.add(createStorageSquare(sd));
        }
        squaresPanel.revalidate();
        squaresPanel.repaint();
    }

    private void showUnitsSortedBySize(boolean ascending) {
        squaresPanel.removeAll();
        List<MySQL.StorageDetails> sorted = new ArrayList<>(allUnits);
        if (ascending) {
            sorted.sort(Comparator.comparing(MySQL.StorageDetails::getSize, String.CASE_INSENSITIVE_ORDER));
        } else {
            sorted.sort(Comparator.comparing(MySQL.StorageDetails::getSize, String.CASE_INSENSITIVE_ORDER).reversed());
        }
        for (MySQL.StorageDetails sd : sorted) {
            squaresPanel.add(createStorageSquare(sd));
        }
        squaresPanel.revalidate();
        squaresPanel.repaint();
    }

    private void showUnitsFilteredByLocation(String location) {
        squaresPanel.removeAll();
        for (MySQL.StorageDetails sd : allUnits) {
            if (sd.getLocation().equalsIgnoreCase(location)) {
                squaresPanel.add(createStorageSquare(sd));
            }
        }
        squaresPanel.revalidate();
        squaresPanel.repaint();
    }

    private JPanel createStorageSquare(MySQL.StorageDetails sd) {
        JPanel unitPanel = new JPanel();
        unitPanel.setPreferredSize(new Dimension(120, 140));
        unitPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        unitPanel.setLayout(new GridBagLayout());
        unitPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                openReservationPanel(sd.getId());
            }
        });
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        unitPanel.add(new JLabel("ID: " + sd.getId()), gbc);
        gbc.gridy++;
        unitPanel.add(new JLabel("Size: " + sd.getSize()), gbc);
        gbc.gridy++;
        unitPanel.add(new JLabel("Price: $" + sd.getPrice()), gbc);
        gbc.gridy++;
        unitPanel.add(new JLabel("Location: " + sd.getLocation()), gbc);
        return unitPanel;
    }

    private void openReservationPanel(int storageID) {
        boolean reserved = MySQL.isUnitReserved(storageID);
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel title = new JLabel("Storage ID: " + storageID, SwingConstants.CENTER);
        panel.add(title, gbc);

        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        panel.add(new JLabel("Enter Email:"), gbc);

        gbc.gridx = 1;
        JTextField emailField = new JTextField(15);
        panel.add(emailField, gbc);

        gbc.gridy = 2;
        gbc.gridx = 0;
        panel.add(new JLabel("Enter Password:"), gbc);

        gbc.gridx = 1;
        JPasswordField passwordField = new JPasswordField(15);
        panel.add(passwordField, gbc);

        // Card Number
        JPasswordField cardField = new JPasswordField();
        if (!reserved) {
            gbc.gridy = 3;
            gbc.gridx = 0;
            panel.add(new JLabel("Enter 16 Digit Card Number:"), gbc);
            gbc.gridx = 1;
            // 16 digits only, no spaces
            cardField.setDocument(new PlainDocument() {
                @Override
                public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
                    if (str == null) return;

                    // Only allow digits, max 16 characters, no spaces
                    if (str.matches("\\d+") && (getLength() + str.length() <= 16)) {
                        super.insertString(offs, str, a);
                    }
                }
            });

            panel.add(cardField, gbc);
        }

        gbc.gridy = 4;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        JButton actionButton = new JButton(reserved ? "Cancel Reservation" : "Reserve");
        actionButton.addActionListener(e -> {
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();
            String cardNumber = new String(cardField.getPassword()).trim();

            int userID = (myGui.getUsername() != null)
                    ? MySQL.getUserID(myGui.getUsername())
                    : MySQL.getUserIDByEmail(email);

            if (email.isEmpty() || password.isEmpty()) return;

            if (reserved) {
                MySQL.cancelReservation(storageID, email, password);
                JOptionPane.showMessageDialog(null, "Reservation canceled!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                if (!cardNumber.matches("\\d{16}")) {
                    JOptionPane.showMessageDialog(null, "Please enter a valid 16-digit card number", "Invalid Card", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                MySQL.reserveStorageUnit(storageID, email, userID, password);
                JOptionPane.showMessageDialog(null, "Reservation successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
            }

            allUnits = MySQL.getAllStorageDetails();  // Refresh units
            myGui.showMain("Storage Screen");

        });
        panel.add(actionButton, gbc);

        gbc.gridy = 5;
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> myGui.showMain("Storage Screen"));
        panel.add(backButton, gbc);

        myGui.addPanel(panel, "Reservation Panel");
        myGui.showMain("Reservation Panel");
    }
}
