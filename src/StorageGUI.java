import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.util.Comparator;
import javax.swing.text.PlainDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import org.jdatepicker.impl.*;
import java.util.Properties;




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
                "Price: Lowest to Highest" ,
                "Price: Highest to Lowest"
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
                "Size: Smallest to Biggest",
                "Size: Biggest to Smallest"
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
        backBtn.addActionListener(e -> myGui.loginUser(myGui.getUsername()));
        add(backBtn, BorderLayout.SOUTH);

        // directs the button to what it will execute
        viewAllBtn.addActionListener(e -> showAllUnits());
        viewAvailBtn.addActionListener(e -> showAvailableUnits());
    }

    private void showAllUnits() {
        allUnits = MySQL.getAllStorageDetails();
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
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;

        // Add basic details
        unitPanel.add(new JLabel("ID: " + sd.getId()), gbc);
        gbc.gridy++;
        unitPanel.add(new JLabel("Size: " + sd.getSize()), gbc);
        gbc.gridy++;
        unitPanel.add(new JLabel("Price: $" + sd.getPrice()), gbc);
        gbc.gridy++;
        unitPanel.add(new JLabel("Location: " + sd.getLocation()), gbc);

        // Check reservation status and add indicator
        if (sd.isReserved()) {
            String currentUserEmail = MySQL.getEmailByUsername(myGui.getUsername());
            JLabel reservedLabel = new JLabel();
            if (MySQL.isUnitReservedByUser(sd.getId(), currentUserEmail)) {
                // Reserved by logged in user: use light green
                unitPanel.setBackground(new Color(144, 238, 144)); // light green
                reservedLabel.setText("Reserved by you");
                reservedLabel.setForeground(Color.GREEN.darker());
            } else {
                // Reserved by someone else: use light red
                unitPanel.setBackground(new Color(255, 182, 193)); // light red
                reservedLabel.setText("Reserved");
                reservedLabel.setForeground(Color.RED);
            }
            gbc.gridy++;
            unitPanel.add(reservedLabel, gbc);
        }

        // Mouse listener remains unchanged
        unitPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int storageID = sd.getId();
                boolean isReserved = sd.isReserved();
                String username = myGui.getUsername();
                String email = MySQL.getEmailByUsername(username);

                if (isReserved) {
                    if (MySQL.isUnitReservedByUser(storageID, email)) {
                        openReservationPanel(storageID);
                    } else {
                        JOptionPane.showMessageDialog(null,
                                "You don't have this unit reserved.",
                                "Access Denied",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    openReservationPanel(storageID); // allow reservation
                }
            }
        });
        return unitPanel;
    }

    private void openReservationPanel(int storageID) {
        boolean reserved = MySQL.isUnitReserved(storageID);
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = baseGBC();

        String email = MySQL.getEmailByUsername(myGui.getUsername());
        int price = MySQL.getPrice(storageID);

        addHeader(panel, gbc, storageID, email);
        JPasswordField passwordField = addPasswordField(panel, gbc);
        JPasswordField cardField = reserved ? null : addCardField(panel, gbc);

        JDatePickerImpl startPicker = null;
        JDatePickerImpl endPicker = null;
        if (!reserved) {
            var pickers = addDatePickers(panel, gbc);
            startPicker = pickers[0];
            endPicker = pickers[1];
        }

        addActionButtons(panel, gbc, reserved, storageID, email, price, passwordField, cardField, startPicker, endPicker);

        myGui.addPanel(panel, "Reservation Panel");
        myGui.showMain("Reservation Panel");
    }

    private GridBagConstraints baseGBC() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        return gbc;
    }

    private void addHeader(JPanel panel, GridBagConstraints gbc, int storageID, String email) {
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(new JLabel("Storage ID: " + storageID, SwingConstants.CENTER), gbc);

        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        panel.add(new JLabel(email), gbc);
    }

    private JPasswordField addPasswordField(JPanel panel, GridBagConstraints gbc) {
        gbc.gridy++;
        gbc.gridx = 0;
        panel.add(new JLabel("Reenter Password:"), gbc);
        gbc.gridx = 1;
        JPasswordField pass = new JPasswordField(15);
        panel.add(pass, gbc);
        return pass;
    }

    private JPasswordField addCardField(JPanel panel, GridBagConstraints gbc) {
        gbc.gridy++;
        gbc.gridx = 0;
        panel.add(new JLabel("Enter 16 Digit Credit Card Number:"), gbc);
        gbc.gridx = 1;
        JPasswordField card = new JPasswordField();
        card.setDocument(new PlainDocument() {
            @Override
            public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
                if (str != null && str.matches("\\d+") && (getLength() + str.length() <= 16)) {
                    super.insertString(offs, str, a);
                }
            }
        });
        panel.add(card, gbc);
        return card;
    }

    private JDatePickerImpl[] addDatePickers(JPanel panel, GridBagConstraints gbc) {
        Properties props = new Properties();
        props.put("text.month", "Month");
        props.put("text.today", "Today");
        props.put("text.year", "Year");

        JDatePickerImpl startPicker = new JDatePickerImpl(new JDatePanelImpl(new UtilDateModel(), props), new DateLabelFormatter());
        JDatePickerImpl endPicker = new JDatePickerImpl(new JDatePanelImpl(new UtilDateModel(), props), new DateLabelFormatter());

        gbc.gridy++;
        gbc.gridx = 0;
        panel.add(new JLabel("Start Date:"), gbc);
        gbc.gridx = 1;
        panel.add(startPicker, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        panel.add(new JLabel("End Date:"), gbc);
        gbc.gridx = 1;
        panel.add(endPicker, gbc);

        return new JDatePickerImpl[]{startPicker, endPicker};
    }

    private void addActionButtons(JPanel panel, GridBagConstraints gbc, boolean reserved, int storageID, String email, int price,
                                  JPasswordField passwordField, JPasswordField cardField,
                                  JDatePickerImpl startPicker, JDatePickerImpl endPicker) {
        JButton actionButton = new JButton(reserved ? "Cancel Reservation" : "Reserve");

        actionButton.addActionListener(e -> {
            String password = new String(passwordField.getPassword()).trim();
            if (password.isEmpty() || !MySQL.isUsernameAndPassword(myGui.getUsername(), password)) {
                JOptionPane.showMessageDialog(null, "Invalid password", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (reserved) {
                handleCancellation(storageID, email, password);
            } else {
                handleReservation(storageID, email, price, password, cardField, startPicker, endPicker);
            }
        });

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        panel.add(actionButton, gbc);

        gbc.gridy++;
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> myGui.showMain("Storage Screen"));
        panel.add(backButton, gbc);
    }


    private void handleReservation(int storageID, String email, int price, String password,
                                   JPasswordField cardField, JDatePickerImpl startPicker, JDatePickerImpl endPicker) {
        String cardNumber = new String(cardField.getPassword()).trim();
        if (!cardNumber.matches("\\d{16}")) {
            JOptionPane.showMessageDialog(null, "Invalid card number", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Date startDate = (Date) startPicker.getModel().getValue();
        Date endDate = (Date) endPicker.getModel().getValue();

        if (startDate == null || (endDate != null && !endDate.after(startDate))) {
            JOptionPane.showMessageDialog(null, "Invalid date range", "Date Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Calendar c = Calendar.getInstance();
        c.setTime(startDate);
        c.add(Calendar.DATE, 30);
        Date billEnd = c.getTime();

        int userID = MySQL.getUserID(myGui.getUsername());
        MySQL.reserveStorageUnit(storageID, email, userID, password, startDate, billEnd, price);
        EmailNotifier.sendReservationConfirmation(email, storageID, startDate, endDate);
        JOptionPane.showMessageDialog(null, "Reservation successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
        allUnits = MySQL.getAllStorageDetails();
        myGui.loginUser(myGui.getUsername());
    }

    private void handleCancellation(int storageID, String email, String password) {
        MySQL.cancelReservation(storageID, email, password);
        JOptionPane.showMessageDialog(null, "Reservation canceled!", "Success", JOptionPane.INFORMATION_MESSAGE);
        allUnits = MySQL.getAllStorageDetails();
        showAllUnits();
        myGui.showMain("Storage Screen");
    }

}
