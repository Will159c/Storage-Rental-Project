import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.Comparator;

public class StorageGUI extends JPanel {
    private MyGUI myGui;
    private JPanel squaresPanel;
    private JScrollPane scrollPane;

    // New inner class to hold storage details for sorting
    private static class StorageDetails {
        private int id;
        private String size;
        private int price;
        private boolean reserved;

        public StorageDetails(int id, String size, int price, boolean reserved) {
            this.id = id;
            this.size = size;
            this.price = price;
            this.reserved = reserved;
        }

        public int getId() { return id; }
        public String getSize() { return size; }
        public int getPrice() { return price; }
        public boolean isReserved() { return reserved; }
    }

    public StorageGUI(MyGUI myGui) {
        this.myGui = myGui;
        setLayout(new BorderLayout());

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
                "Sort by Size: Lowest to Highest",
                "Sort by Size: Highest to Lowest"
        };
        JComboBox<String> sizeSortCombo = new JComboBox<>(sizeOptions);
        sizeSortCombo.addActionListener(e -> {
            if (sizeSortCombo.getSelectedIndex() == 0) {
                showUnitsSortedBySize(true);
            } else {
                showUnitsSortedBySize(false);
            }
        });

        topBtnPanel.add(viewAllBtn);
        topBtnPanel.add(viewAvailBtn);
        topBtnPanel.add(priceSortCombo);
        topBtnPanel.add(sizeSortCombo);
        add(topBtnPanel, BorderLayout.PAGE_START);

        // it lets us scroll when there are more units than the screen fits
        squaresPanel = new JPanel();
        squaresPanel.setLayout(new GridLayout(0, 4, 10, 10));
        squaresPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        scrollPane = new JScrollPane(squaresPanel);
        add(scrollPane, BorderLayout.CENTER);

        // a back button that sends the user back to the welcom screen
        JButton backBtn = new JButton("Back");
        backBtn.addActionListener(e -> myGui.showMain("Welcome Screen"));
        add(backBtn, BorderLayout.SOUTH);

        // directs the button to what it will execute
        viewAllBtn.addActionListener(e -> showAllUnits());
        viewAvailBtn.addActionListener(e -> showAvailableUnits());
    }

    // this is what the view all units button uses
    private void showAllUnits() {
        squaresPanel.removeAll();
        List<Integer> allIds = MySQL.getStorageID();
        for (Integer id : allIds) {
            boolean isReserved = MySQL.isUnitReserved(id);
            squaresPanel.add(createStorageSquare(id, isReserved));
        }
        squaresPanel.revalidate();
        squaresPanel.repaint();
    }

    // this is what the show all units button uses
    private void showAvailableUnits() {
        squaresPanel.removeAll();
        List<Integer> allIds = MySQL.getStorageID();
        for (Integer id : allIds) {
            boolean isReserved = MySQL.isUnitReserved(id);
            if (!isReserved) {
                squaresPanel.add(createStorageSquare(id, false));
            }
        }
        squaresPanel.revalidate();
        squaresPanel.repaint();
    }

    // This is for sorting by price
    private void showUnitsSortedByPrice(boolean ascending) {
        squaresPanel.removeAll();
        List<Integer> allIds = MySQL.getStorageID();
        java.util.List<StorageDetails> units = new java.util.ArrayList<>();
        for (Integer id : allIds) {
            boolean reserved = MySQL.isUnitReserved(id);
            List<Object> info = MySQL.getStorageInformation(id);
            String size = (String) info.get(1);
            int price = (Integer) info.get(2);
            units.add(new StorageDetails(id, size, price, reserved));
        }
        if (ascending) {
            units.sort(Comparator.comparingInt(StorageDetails::getPrice));
        } else {
            units.sort(Comparator.comparingInt(StorageDetails::getPrice).reversed());
        }
        for (StorageDetails unit : units) {
            squaresPanel.add(createStorageSquare(unit.getId(), unit.isReserved(), unit.getSize(), unit.getPrice()));
        }
        squaresPanel.revalidate();
        squaresPanel.repaint();
    }

    // Sorts units by size
    private void showUnitsSortedBySize(boolean ascending) {
        squaresPanel.removeAll();
        List<Integer> allIds = MySQL.getStorageID();
        java.util.List<StorageDetails> units = new java.util.ArrayList<>();
        for (Integer id : allIds) {
            boolean reserved = MySQL.isUnitReserved(id);
            List<Object> info = MySQL.getStorageInformation(id);
            String size = (String) info.get(1);
            int price = (Integer) info.get(2);
            units.add(new StorageDetails(id, size, price, reserved));
        }
        if (ascending) {
            units.sort(Comparator.comparing(StorageDetails::getSize, String.CASE_INSENSITIVE_ORDER));
        } else {
            units.sort(Comparator.comparing(StorageDetails::getSize, String.CASE_INSENSITIVE_ORDER).reversed());
        }
        for (StorageDetails unit : units) {
            squaresPanel.add(createStorageSquare(unit.getId(), unit.isReserved(), unit.getSize(), unit.getPrice()));
        }
        squaresPanel.revalidate();
        squaresPanel.repaint();
    }

    private JPanel createStorageSquare(int storageID, boolean reserved, String size, int price) {
        JPanel unitPanel = new JPanel();
        unitPanel.setPreferredSize(new Dimension(120, 120));
        unitPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        unitPanel.setLayout(new GridBagLayout());
        unitPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                openReservationPanel(storageID);
            }
        });
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        unitPanel.add(new JLabel("ID: " + storageID), gbc);
        gbc.gridy++;
        unitPanel.add(new JLabel("Size: " + size), gbc);
        gbc.gridy++;
        unitPanel.add(new JLabel("Price: $" + price), gbc);
        return unitPanel;
    }

    // Original method updated to fetch size and price
    private JPanel createStorageSquare(int storageID, boolean reserved) {
        List<Object> info = MySQL.getStorageInformation(storageID);
        String size = (String) info.get(1);
        int price = (Integer) info.get(2);
        return createStorageSquare(storageID, reserved, size, price);
    }

    // Opens the reservation panel for a given storage unit
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

        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        JButton actionButton = new JButton(reserved ? "Cancel Reservation" : "Reserve");
        actionButton.addActionListener(e -> {
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
            if (email.isEmpty() || password.isEmpty()) return;
            if (reserved) {
                MySQL.cancelReservation(storageID, email, password);
            } else {
                MySQL.reserveStorageUnit(storageID, email, password);
            }
            myGui.showMain("Storage Screen");
        });
        panel.add(actionButton, gbc);

        gbc.gridy = 4;
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> myGui.showMain("Storage Screen"));
        panel.add(backButton, gbc);

        myGui.addPanel(panel, "Reservation Panel");
        myGui.showMain("Reservation Panel");
    }
}
