import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.awt.event.*;

public class StorageGUI extends JPanel {
    private MyGUI myGui;

    private JPanel squaresPanel;
    private JScrollPane scrollPane;

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

        topBtnPanel.add(viewAllBtn);
        topBtnPanel.add(viewAvailBtn);

        add(topBtnPanel, BorderLayout.PAGE_START);

        // it lets us scroll when there are more units than the screen fits
        squaresPanel = new JPanel();
        squaresPanel.setLayout(new GridLayout(0, 4, 10, 10));
        squaresPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        scrollPane = new JScrollPane(squaresPanel);
        add(scrollPane, BorderLayout.CENTER);

        // a back button that sends the user back to the welcom screen
        JButton backBtn = new JButton("Back");
        backBtn.addActionListener(e -> myGui.showMain("Welcome Screen"));
        add(backBtn, BorderLayout.SOUTH);

        // directs the button to what it will execute
        viewAllBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAllUnits();
            }
        });

        viewAvailBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAvailableUnits();
            }
        });
    }

    // this is what the view all units button uses
    private void showAllUnits() {
        squaresPanel.removeAll(); // clear the boxes if it was displaying something before

        // Get all storage IDs from where the boy Will has the mysql database lol
        List<Integer> allIds = mySQL.getStorageID();

        for (Integer id : allIds) {
            boolean isReserved = mySQL.isUnitReserved(id);
            squaresPanel.add(createStorageSquare(id, isReserved));
        }
        squaresPanel.revalidate();
        squaresPanel.repaint();
    }

    // this is what the show all units button uses
    private void showAvailableUnits() {
        squaresPanel.removeAll(); // Clear

        List<Integer> allIds = mySQL.getStorageID();
        for (Integer id : allIds) {
            boolean isReserved = mySQL.isUnitReserved(id);
            if (!isReserved) {
                squaresPanel.add(createStorageSquare(id, false));
            }
        }
        squaresPanel.revalidate();
        squaresPanel.repaint();
    }

    private JPanel createStorageSquare(int storageID, boolean reserved) {
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

        JLabel idLabel = new JLabel("ID: " + storageID);
        unitPanel.add(idLabel);

        return unitPanel;
    }

    private void openReservationPanel(int storageID) {
        boolean reserved = mySQL.isUnitReserved(storageID);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        JLabel title = new JLabel("Storage ID: " + storageID, SwingConstants.CENTER);
        panel.add(title, gbc);

        // Email Label
        gbc.gridy = 1; gbc.gridwidth = 1; gbc.gridx = 0;
        panel.add(new JLabel("Enter Email:"), gbc);

        // Email Field
        gbc.gridx = 1;
        JTextField emailField = new JTextField(15);
        panel.add(emailField, gbc);

        // Password Label
        gbc.gridy = 2; gbc.gridx = 0;
        panel.add(new JLabel("Enter Password:"), gbc);

        // Password Field
        gbc.gridx = 1;
        JPasswordField passwordField = new JPasswordField(15);
        panel.add(passwordField, gbc);

        // Reserve/Cancel Button
        gbc.gridy = 3; gbc.gridx = 0; gbc.gridwidth = 2;
        JButton actionButton = new JButton(reserved ? "Cancel Reservation" : "Reserve");
        actionButton.addActionListener(e -> {
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
            if (email.isEmpty() || password.isEmpty()) return;

            if (reserved) {
                mySQL.cancelReservation(storageID, email, password);
            } else {
                mySQL.reserveStorageUnit(storageID, email, password);
            }

            myGui.showMain("Storage Screen"); // Refresh display
        });
        panel.add(actionButton, gbc);

        // Back Button
        gbc.gridy = 4;
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> myGui.showMain("Storage Screen"));
        panel.add(backButton, gbc);

        myGui.addPanel(panel, "Reservation Panel");
        myGui.showMain("Reservation Panel");
    }

}
