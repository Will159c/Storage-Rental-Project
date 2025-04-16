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

/**
 * A) Class Name: StorageGUI
 * B) Date of Code: April 15, 2025,
 * C) Programmer's Name: Juan Acevedo, Alexis Anguiano
 * D) Brief Desc: StorageGUI the GUI page to display and manage storage units.
 It displays storage units as squares with a bunch of sort options,
 sort them by price or size, filter by location, and reserve or cancel a reservation.
 * e) Brief Explanation of Important Functions:
 *    - showAllUnits: Fetches all units from the database and displays them.
 *    - showAvailableUnits: Filters and displays only non-reserved units.
 *    - createStorageSquare: Builds a GUI panel to represent a single storage unit,
 *      including status indicators and a reserve button.
 *    - openReservationPanel: Opens a panel to handle reservation or cancellation details.
 * f) Important Data Structures:
 *    - MySQL: Stores storage units in memory (in a List).
 *    - Comparator: Used to sort units by price or size.
 * g) Algorithms Used:
 *    - Sorting via Collections.sort() with Comparator for ascending/descending price or size.
 *    - Basic date validation and credit card number checks (simple string matching).
 */

public class StorageGUI extends JPanel {

    /**
     * Reference to the main GUI page.
     * */
    private MyGUI myGui;

    /**
     * A panel used to display the storage unit squares.
     * */
    private JPanel squaresPanel;

    /**
     * Scroll pane to scroll the squaresPanel.
     * */
    private JScrollPane scrollPane;

    /**
     * In-memory list of storage units to avoid repeated DB calls.
     * Uses MySQL.StorageDetails to store unit details.
     */
    private List<MySQL.StorageDetails> allUnits;

    /**
     * Constructs a new StorageGUI panel.
     * @param myGui the main GUI controller object.
     */
    public StorageGUI(MyGUI myGui) {
        this.myGui = myGui;
        setLayout(new BorderLayout());

        // NEW: Load all storage units in one call.
        allUnits = MySQL.getAllStorageDetails();

        // Create and add title.
        JLabel titleTxt = new JLabel("Storage Units Browser", SwingConstants.CENTER);
        titleTxt.setFont(new Font("SansSerif", Font.BOLD, 20));
        add(titleTxt, BorderLayout.NORTH);

        // Create the top panel with search and sort controls.
        JPanel topBtnPanel = new JPanel();
        topBtnPanel.setOpaque(false);  // Make background transparent so our painted image shows through
        JButton viewAllBtn = new JButton("View All Units");
        JButton viewAvailBtn = new JButton("View Available Units");

        // Combo box for price sort options.
        String[] priceOptions = {
                "Price: Lowest to Highest",
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

        // Combo box for size sort options.
        String[] sizeOptions = {
                "Size: Smallest to Biggest",
                "Size: Biggest to Smallest"
        };
        JComboBox<String> sizeSortCombo = new JComboBox<>(sizeOptions);
        sizeSortCombo.addActionListener(e -> {
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

        // Create a combo box for location filtering.
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

        // Setup the scrolling panel for storage unit squares.
        squaresPanel = new JPanel();
        squaresPanel.setLayout(new GridLayout(0, 4, 10, 10));
        squaresPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        squaresPanel.setOpaque(false);  // Allow background image to show through

        scrollPane = new JScrollPane(squaresPanel);
        scrollPane.setOpaque(false);               // Make scroll pane transparent
        scrollPane.getViewport().setOpaque(false);   // Make viewport transparent
        add(scrollPane, BorderLayout.CENTER);

        // Create and add the back button.
        JButton backBtn = new JButton("Back");
        backBtn.addActionListener(e -> myGui.loginUser(myGui.getUsername()));
        add(backBtn, BorderLayout.SOUTH);

        // Add action listeners for viewing all and available units.
        viewAllBtn.addActionListener(e -> showAllUnits());
        viewAvailBtn.addActionListener(e -> showAvailableUnits());
    }

    /**
     * Retrieves all storage units, and displays them on the GUI.
     */
    private void showAllUnits() {
        allUnits = MySQL.getAllStorageDetails();
        squaresPanel.removeAll();
        for (MySQL.StorageDetails sd : allUnits) {
            squaresPanel.add(createStorageSquare(sd));
        }
        squaresPanel.revalidate();
        squaresPanel.repaint();
    }

    /**
     * Displays only the available (not reserved) storage units.
     */
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

    /**
     * Displays storage units sorted by price.
     * @param ascending if true, sorts from lowest to highest; otherwise, highest to lowest.
     */
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

    /**
     * Displays storage units sorted by size.
     * @param ascending if true, sorts from smallest to biggest; otherwise, biggest to smallest.
     */
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

    /**
     * Displays only storage units filtered by a specific location.
     * @param location the location filter to apply.
     */
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

    /**
     * Creates a storage unit square panel that displays unit details and appropriate action components.
     * @param sd the storage details object.
     * @return a JPanel representing the storage unit square
     */
    private JPanel createStorageSquare(MySQL.StorageDetails sd) {
        JPanel unitPanel = new JPanel();
        unitPanel.setPreferredSize(new Dimension(120, 140));
        unitPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        unitPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;

        // Display basic details
        unitPanel.add(new JLabel("ID: " + sd.getId()), gbc);
        gbc.gridy++;
        unitPanel.add(new JLabel("Size: " + sd.getSize()), gbc);
        gbc.gridy++;
        unitPanel.add(new JLabel("Price: $" + sd.getPrice()), gbc);
        gbc.gridy++;
        unitPanel.add(new JLabel("Location: " + sd.getLocation()), gbc);

        // Check reservation status and adjust display accordingly.
        if (sd.isReserved()) {
            String currentUserEmail = MySQL.getEmailByUsername(myGui.getUsername());
            JLabel reservedLabel = new JLabel();
            if (MySQL.isUnitReservedByUser(sd.getId(), currentUserEmail)) {
                // Reserved by the logged-in user: light green background.
                unitPanel.setBackground(new Color(144, 238, 144)); // light green.
                reservedLabel.setText("Reserved by you");
                reservedLabel.setForeground(Color.GREEN.darker());
            } else {
                // Reserved by someone else: light red background.
                unitPanel.setBackground(new Color(255, 182, 193)); // light red.
                reservedLabel.setText("Reserved");
                reservedLabel.setForeground(Color.RED);
            }
            gbc.gridy++;
            unitPanel.add(reservedLabel, gbc);

            // Add mouse listener for reserved squares.
            unitPanel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    int storageID = sd.getId();
                    String username = myGui.getUsername();
                    String email = MySQL.getEmailByUsername(username);
                    if (MySQL.isUnitReservedByUser(storageID, email)) {
                        openReservationPanel(storageID);
                    } else {
                        JOptionPane.showMessageDialog(null,
                                "You don't have this unit reserved.",
                                "Access Denied",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
        } else {
            // If the unit is available, add a dedicated "Reserve" button.
            JButton reserveBtn = new JButton("Reserve");
            reserveBtn.addActionListener(e -> openReservationPanel(sd.getId()));
            gbc.gridy++;
            unitPanel.add(reserveBtn, gbc);
        }
        return unitPanel;
    }

    /**
     * Opens a panel for the selected storage unit, allowing the user to reserve or cancel
     * @param storageID the ide of the unit selected
     */
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

    /**
     * Creates and return a base gridbadcontraints with defualt insetsand fill mode
     * @return configured gridbadcontraints object
     */
    private GridBagConstraints baseGBC() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        return gbc;
    }

    /**
     * Adds the header section to the reservation panel with storage ID and email
     * @param panel the panel to add compnents to
     * @param gbc layout constraints
     * @param storageID id of unit
     * @param email user's email
     */
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

    /**
     * Adds and return a pssword input field to the panel
     * @param panel the panel to add the field to
     * @param gbc layout constraints
     * @return the card input field
     */
    private JPasswordField addPasswordField(JPanel panel, GridBagConstraints gbc) {
        gbc.gridy++;
        gbc.gridx = 0;
        panel.add(new JLabel("Reenter Password:"), gbc);
        gbc.gridx = 1;
        JPasswordField pass = new JPasswordField(15);
        panel.add(pass, gbc);
        return pass;
    }

    /**
     * Adds and return a 16 digit credit card input field
     * @param panel the panel to add the field to
     * @param gbc layout constraints
     * @return the card input field
     */
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

    /**
     * Adds start and end date pickers to the panel
     * @param panel the panel to add to
     * @param gbc layout constraints
     * @return array of [startpicker,endpicker]
     */
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

    /**
     * Adds a reserve or cancel button depends on reservation state and a back button to return to main screen
     * @param panel reservation panel
     * @param gbc layout manager
     * @param reserved if the unit is reserved
     * @param storageID unit id
     * @param email user's email
     * @param price unit price
     * @param passwordField password input
     * @param cardField card input
     * @param startPicker start date picker
     * @param endPicker end date picker
     */
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

    /**
     * Handles reservation logic: validated card number,dates,and inserts reservation in datebase.
     * Sends email confirmation and updates the GUI.
     * @param storageID id of unit
     * @param email user's email
     * @param price price of unit
     * @param password password to verify
     * @param cardField card number input
     * @param startPicker start date picker
     * @param endPicker end date picker
     */
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

    /**
     * Handles cancelation logic: removes rerservation and refreshes the GUI
     * @param storageID unit ID the was canceled
     * @param email user's email
     * @param password verification password
     */
    private void handleCancellation(int storageID, String email, String password) {
        MySQL.cancelReservation(storageID, email, password);
        JOptionPane.showMessageDialog(null, "Reservation canceled!", "Success", JOptionPane.INFORMATION_MESSAGE);
        allUnits = MySQL.getAllStorageDetails();
        showAllUnits();
        myGui.showMain("Storage Screen");
    }

    @Override
    /**
     * Adds the blurred background photo to keep the theme
     */
    protected void paintComponent(Graphics g) {
        // First, clear the panel by letting the superclass draw its background.
        super.paintComponent(g);
        // Load the background image from resources. Ensure the image is in src/main/resources.
        Image bgImg = new ImageIcon(getClass().getResource("/background_blur.jpg")).getImage();
        // Draw the image to fill the entire panel.
        g.drawImage(bgImg, 0, 0, getWidth(), getHeight(), this);
    }
}
