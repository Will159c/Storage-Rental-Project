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
import java.util.Calendar;


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

@SuppressWarnings("serial")
public class StorageGUI extends JPanel {

    private final MyGUI myGui;
    private final JPanel squaresPanel;
    private final JScrollPane scrollPane;
    /** in‑memory cache so we don’t hit DB every refresh */
    private List<MySQL.StorageDetails> allUnits;
    private boolean hideOthersReserved = false;
    private Runnable redrawCallback = null; // remembers last drawing routine

    public StorageGUI(MyGUI myGui) {
        this.myGui = myGui;
        setLayout(new BorderLayout());
        allUnits = MySQL.getAllStorageDetails();
        JLabel titleTxt = new JLabel("Storage Units Browser", SwingConstants.CENTER);
        titleTxt.setFont(new Font("SansSerif", Font.BOLD, 20));
        add(titleTxt, BorderLayout.NORTH);
        JPanel topBtnPanel = new JPanel();
        topBtnPanel.setOpaque(false);

        // view all availaible units
        JButton viewAvailBtn = new JButton("View Available Units");

        // price sort
        String[] priceOptions = {"Price: Lowest to Highest", "Price: Highest to Lowest"};
        JComboBox<String> priceSortCombo = new JComboBox<>(priceOptions);
        priceSortCombo.addActionListener(e -> {
            if (priceSortCombo.getSelectedIndex() == 0) {
                showUnitsSortedByPrice(true);
            } else {
                showUnitsSortedByPrice(false);
            }
        });

        // size sort
        String[] sizeOptions = {"Size: Smallest to Biggest", "Size: Biggest to Smallest"};
        JComboBox<String> sizeSortCombo = new JComboBox<>(sizeOptions);
        sizeSortCombo.addActionListener(e -> {
            if (sizeSortCombo.getSelectedIndex() == 0) {
                showUnitsSortedBySize(false);
            } else {
                showUnitsSortedBySize(true);
            }
        });

        // location filter
        Set<String> locationSet = new HashSet<>();
        for (MySQL.StorageDetails sd : allUnits) locationSet.add(sd.getLocation());
        List<String> locations = new ArrayList<>(locationSet);
        Collections.sort(locations);
        String[] locationOptions = new String[locations.size() + 1];
        locationOptions[0] = "All Locations";
        for (int i = 0; i < locations.size(); i++) locationOptions[i + 1] = locations.get(i);
        JComboBox<String> locationCombo = new JComboBox<>(locationOptions);
        locationCombo.addActionListener(e -> {
            String sel = (String) locationCombo.getSelectedItem();
            if ("All Locations".equals(sel)) {
                showAllUnits();
            } else {
                showUnitsFilteredByLocation(sel);
            }
        });

        // Exclude‑reserved checkbox
        JCheckBox excludeChk = new JCheckBox("Hide other users’ reserved units");
        excludeChk.addItemListener(e -> {
            hideOthersReserved = (e.getStateChange() == ItemEvent.SELECTED);
            if (redrawCallback != null) redrawCallback.run();
        });

        topBtnPanel.add(viewAvailBtn);
        topBtnPanel.add(priceSortCombo);
        topBtnPanel.add(sizeSortCombo);
        topBtnPanel.add(locationCombo);
        topBtnPanel.add(excludeChk);
        add(topBtnPanel, BorderLayout.PAGE_START);

        squaresPanel = new JPanel(new GridLayout(0, 4, 10, 10));
        squaresPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        squaresPanel.setOpaque(false);
        scrollPane = new JScrollPane(squaresPanel);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        add(scrollPane, BorderLayout.CENTER);
        JButton backBtn = new JButton("Back");
        backBtn.addActionListener(e -> myGui.loginUser(myGui.getUsername()));
        add(backBtn, BorderLayout.SOUTH);
        viewAvailBtn.addActionListener(e -> showAvailableUnits());
        showAllUnits();
    }

    //filter helper
    private boolean passesReservedFilter(MySQL.StorageDetails sd) {
        if (!hideOthersReserved) return true;          // filter disabled
        if (!sd.isReserved())   return true;          // not reserved at all
        String email = MySQL.getEmailByUsername(myGui.getUsername());
        return MySQL.isUnitReservedByUser(sd.getId(), email); // keep if mine
    }

    private void showAllUnits() {
        redrawCallback = this::showAllUnits;
        allUnits = MySQL.getAllStorageDetails();
        squaresPanel.removeAll();
        for (MySQL.StorageDetails sd : allUnits) {
            if (!passesReservedFilter(sd)) continue;
            squaresPanel.add(createStorageSquare(sd));
        }
        squaresPanel.revalidate();
        squaresPanel.repaint();
    }

    private void showAvailableUnits() {
        redrawCallback = this::showAvailableUnits;
        squaresPanel.removeAll();
        for (MySQL.StorageDetails sd : allUnits) {
            if (sd.isReserved()) continue;
            if (!passesReservedFilter(sd)) continue;
            squaresPanel.add(createStorageSquare(sd));
        }
        squaresPanel.revalidate();
        squaresPanel.repaint();
    }

    private void showUnitsSortedByPrice(boolean ascending) {
        redrawCallback = () -> showUnitsSortedByPrice(ascending);
        squaresPanel.removeAll();
        List<MySQL.StorageDetails> sorted = new ArrayList<>(allUnits);
        sorted.sort(ascending ? Comparator.comparingInt(MySQL.StorageDetails::getPrice)
                : Comparator.comparingInt(MySQL.StorageDetails::getPrice).reversed());
        for (MySQL.StorageDetails sd : sorted) {
            if (!passesReservedFilter(sd)) continue;
            squaresPanel.add(createStorageSquare(sd));
        }
        squaresPanel.revalidate();
        squaresPanel.repaint();
    }

    private void showUnitsSortedBySize(boolean ascending) {
        redrawCallback = () -> showUnitsSortedBySize(ascending);
        squaresPanel.removeAll();
        List<MySQL.StorageDetails> sorted = new ArrayList<>(allUnits);
        sorted.sort(ascending ? Comparator.comparing(MySQL.StorageDetails::getSize, String.CASE_INSENSITIVE_ORDER)
                : Comparator.comparing(MySQL.StorageDetails::getSize, String.CASE_INSENSITIVE_ORDER).reversed());
        for (MySQL.StorageDetails sd : sorted) {
            if (!passesReservedFilter(sd)) continue;
            squaresPanel.add(createStorageSquare(sd));
        }
        squaresPanel.revalidate();
        squaresPanel.repaint();
    }

    private void showUnitsFilteredByLocation(String location) {
        redrawCallback = () -> showUnitsFilteredByLocation(location);
        squaresPanel.removeAll();
        for (MySQL.StorageDetails sd : allUnits) {
            if (!sd.getLocation().equalsIgnoreCase(location)) continue;
            if (!passesReservedFilter(sd)) continue;
            squaresPanel.add(createStorageSquare(sd));
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
        JPanel unitPanel = new JPanel(new BorderLayout());
        unitPanel.setPreferredSize(new Dimension(200, 300));
        unitPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // ── top: labels ───────────────────────────────────────────────
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setOpaque(false);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JLabel idLabel       = new JLabel("ID: " + sd.getId(),      SwingConstants.CENTER);
        JLabel sizeLabel     = new JLabel("Size: " + sd.getSize(),  SwingConstants.CENTER);
        JLabel priceLabel    = new JLabel("Price: $" + sd.getPrice(),SwingConstants.CENTER);
        JLabel locationLabel = new JLabel("Location: " + sd.getLocation(), SwingConstants.CENTER);
        for (JLabel l : new JLabel[]{idLabel, sizeLabel, priceLabel, locationLabel})
            l.setAlignmentX(Component.CENTER_ALIGNMENT);

        topPanel.add(idLabel);
        topPanel.add(sizeLabel);
        topPanel.add(priceLabel);
        topPanel.add(locationLabel);
        unitPanel.add(topPanel, BorderLayout.NORTH);

        // ── centre: image ─────────────────────────────────────────────
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        JLabel imageLabel = new JLabel();
        String location = sd.getLocation();
        String imageName = switch (location.toLowerCase()) {
            case "lancaster"    -> "lancaster.png";
            case "downtown la"  -> "dontownla.png";
            case "reseda"       -> "reseda.png";
            case "van nuys"     -> "vannuys.png";
            case "northridge"   -> "northridge.png";
            default              -> "background.jpg";
        };
        java.net.URL imgUrl = getClass().getResource("/" + imageName);
        if (imgUrl != null) {
            ImageIcon icon = new ImageIcon(imgUrl);
            int maxW = 240, maxH = 180;
            int w = icon.getIconWidth();
            int h = icon.getIconHeight();
            double aspect = (double) w / h;
            int newW = maxW;
            int newH = (int) (newW / aspect);
            if (newH > maxH) { newH = maxH; newW = (int) (newH * aspect); }
            Image scaled = icon.getImage().getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(scaled));
        } else {
            imageLabel.setText("No Image");
        }
        centerPanel.add(imageLabel);
        unitPanel.add(centerPanel, BorderLayout.CENTER);

        // ── bottom: reserve / status ──────────────────────────────────
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
        bottomPanel.setOpaque(false);

        if (sd.isReserved()) {
            String currentEmail = MySQL.getEmailByUsername(myGui.getUsername());
            JLabel reservedLabel = new JLabel();
            if (MySQL.isUnitReservedByUser(sd.getId(), currentEmail)) {
                unitPanel.setBackground(new Color(144, 238, 144)); // light green
                reservedLabel.setText("Reserved by you");
                reservedLabel.setForeground(Color.GREEN.darker());
            } else {
                unitPanel.setBackground(new Color(255, 182, 193)); // light red
                reservedLabel.setText("Reserved");
                reservedLabel.setForeground(Color.RED);
            }
            bottomPanel.add(reservedLabel);

            unitPanel.addMouseListener(new MouseAdapter() {
                @Override public void mouseClicked(MouseEvent e) {
                    int storageID = sd.getId();
                    String username = myGui.getUsername();
                    String email = MySQL.getEmailByUsername(username);
                    if (MySQL.isUnitReservedByUser(storageID, email)) {
                        openReservationPanel(storageID);
                    } else {
                        JOptionPane.showMessageDialog(null, "You don't have this unit reserved.", "Access Denied", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
        } else {
            JButton reserveBtn = new JButton("Reserve");
            reserveBtn.addActionListener(e -> openReservationPanel(sd.getId()));
            bottomPanel.add(reserveBtn);
        }
        unitPanel.add(bottomPanel, BorderLayout.SOUTH);
        return unitPanel;
    }


    /**
     * Created by: Alexis Anguiano
     * Opens a panel for the selected storage unit, allowing the user to reserve or cancel
     * @param storageID the ide of the unit selected
     */
    private void openReservationPanel(int storageID) {
        boolean reserved = MySQL.isUnitReserved(storageID);
        JPanel panel = new BackgroundSetter("/background_blur.jpg", new GridBagLayout());
        GridBagConstraints gbc = baseGBC();

        String email = MySQL.getEmailByUsername(myGui.getUsername());
        int price = MySQL.getPrice(storageID);

        addHeader(panel, gbc, storageID, email);
        JPasswordField passwordField = addPasswordField(panel, gbc);
        JPasswordField cardField = reserved ? null : addCardField(panel, gbc);

        JDatePickerImpl startPicker = null;
        if (!reserved) {
            startPicker = addStartDatePicker(panel, gbc);

        }

        addActionButtons(panel, gbc, reserved, storageID, email, price, passwordField, cardField, startPicker);

        myGui.addPanel(panel, "Reservation Panel");
        myGui.showMain("Reservation Panel");
    }

    /**
     * Created by: Alexis Anguiano
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
     * Created by: Alexis Anguiano
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
     * Created by: Alexis Anguiano
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
     * Created by: Alexis Anguiano
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
     * Created by: Alexis Anguiano
     * Adds start pickers to the panel
     * @param panel the panel to add to
     * @param gbc layout constraints
     * @return  start picker
     */
    private JDatePickerImpl addStartDatePicker(JPanel panel, GridBagConstraints gbc) {
        Properties props = new Properties();
        props.put("text.month", "Month");
        props.put("text.today", "Today");
        props.put("text.year", "Year");

        JDatePickerImpl startPicker = new JDatePickerImpl(new JDatePanelImpl(new UtilDateModel(), props), new DateLabelFormatter());

        gbc.gridy++;
        gbc.gridx = 0;
        panel.add(new JLabel("Start Date:"), gbc);
        gbc.gridx = 1;
        panel.add(startPicker, gbc);

        return startPicker;
    }


    /**
     * Created by: Alexis Anguiano
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
     */
    private void addActionButtons(JPanel panel, GridBagConstraints gbc, boolean reserved, int storageID, String email, int price,
                                  JPasswordField passwordField, JPasswordField cardField,
                                  JDatePickerImpl startPicker)
    {
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
                handleReservation(storageID, email, price, password, cardField, startPicker);
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
     * Created by: Alexis Anguiano
     * Handles reservation logic: validated card number,dates,and inserts reservation in datebase.
     * Sends email confirmation and updates the GUI.
     * @param storageID id of unit
     * @param email user's email
     * @param price price of unit
     * @param password password to verify
     * @param cardField card number input
     * @param startPicker start date picker
     */
    private void handleReservation(int storageID, String email, int price, String password,
                                   JPasswordField cardField, JDatePickerImpl startPicker)
    {
        String cardNumber = new String(cardField.getPassword()).trim();
        if (!cardNumber.matches("\\d{16}")) {
            JOptionPane.showMessageDialog(null, "Invalid card number", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Date startDate = (Date) startPicker.getModel().getValue();
        if (startDate == null) {
            JOptionPane.showMessageDialog(null, "Invalid date", "Date Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Calendar todayCal = Calendar.getInstance();
        todayCal.set(Calendar.HOUR_OF_DAY, 0);
        todayCal.set(Calendar.MINUTE, 0);
        todayCal.set(Calendar.SECOND, 0);
        todayCal.set(Calendar.MILLISECOND, 0);

        Calendar selectedCal = Calendar.getInstance();
        selectedCal.setTime(startDate);
        selectedCal.set(Calendar.HOUR_OF_DAY, 0);
        selectedCal.set(Calendar.MINUTE, 0);
        selectedCal.set(Calendar.SECOND, 0);
        selectedCal.set(Calendar.MILLISECOND, 0);

        if (selectedCal.before(todayCal)) {
            JOptionPane.showMessageDialog(null, "Start date cannot be in the past", "Date Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int userID = MySQL.getUserID(myGui.getUsername());
        MySQL.reserveStorageUnit(storageID, email, userID, password, startDate, price);
        EmailNotifier.sendReservationConfirmation(email, storageID, startDate);
        JOptionPane.showMessageDialog(null, "Reservation successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
        allUnits = MySQL.getAllStorageDetails();
        myGui.loginUser(myGui.getUsername());
    }


    /**
     * Created by: Alexis Anguiano
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
