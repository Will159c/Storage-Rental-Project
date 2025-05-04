import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.time.LocalDate;
import java.util.List;

/**
 * a) Class Name: AdminGUI
 * b) Date of the Code: March 17, 2025
 * c) Programmers Names: Miguel Sicaja, William Woodruff
 * d) Brief description: The Admin page that provides a way for the admin to navigate
 * through and create storage units or manage users. Displays the
 * total monthly and overall revenue and a way to return to the main
 * menu.
 * e) Brief explanation of important functions:
 * - getMonthlyRevenue: Gets the total revenue received per month
 * f) Important Data Structures:
 * - MySQL: Provides means of checking monthly revenue
 * g) Algorithms used:
 * - Uses a basic UI logic algorithm to control page navigation through MyGUI
 */
public class AdminGUI extends JPanel {
    private MyGUI myGui;

    /**
     * Constructs the admin screen with the navigation
     * buttons and displays the total and monthly revenues.
     * @param myGui Reference to the main GUI controller for navigation
     */
    public AdminGUI(MyGUI myGui) {
        this.myGui = myGui; // Save reference
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints(); // Allows for ordered positioning
        String monthlyRevenue = "Total Monthly Revenue: $" + getMonthlyRevenue();

        // Create a panel with GridBagLayout
        JPanel panel = new JPanel(new GridBagLayout());
        gbc.fill = GridBagConstraints.HORIZONTAL; // Stretch components horizontally

        // Title text
        JLabel titleTxt = new JLabel("Welcome Admin", SwingConstants.CENTER);
        titleTxt.setFont(new Font("SansSerif", Font.BOLD, 40));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1; // Ensure title spans both columns
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(titleTxt, gbc);

        ////////// Buttons ///////////
        // User Button
        JButton userButton = new JButton("Manage Users");
        userButton.setPreferredSize(new Dimension(100, 50));
        gbc.gridy = 1;
        gbc.gridx = 0;
        gbc.insets = new Insets(20, 0, 10, 0);
        panel.add(userButton, gbc);

        // Storage Button
        JButton storageButton = new JButton("Manage Storage Units");
        storageButton.setPreferredSize(new Dimension(100, 50));
        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.insets = new Insets(20, 0, 10, 0);
        panel.add(storageButton, gbc);

        // Log Out Button
        JButton rBack = new JButton("Log Out");
        rBack.setPreferredSize(new Dimension(100, 50));
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.insets = new Insets(20, 0, 10, 0);
        panel.add(rBack, gbc);

        /////// Button functionalities //////////

        // Log out and return to home screen!!!!!!!11!!
        rBack.addActionListener(e -> myGui.showMain("Welcome Screen"));

        // Go to manage storage screen
        storageButton.addActionListener(e -> myGui.showMain("Manage Storage Screen"));

        // Go to manage user screen
        userButton.addActionListener(e -> myGui.showMain("Manage User Screen"));

        // Show revenue: Labels
        JLabel revenueLabel = new JLabel(monthlyRevenue, SwingConstants.CENTER);
        revenueLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1; // Ensure label spans both columns
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(revenueLabel, gbc);

        // Refreshes monthly revenue after entering the page
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                revenueLabel.setText("Total Monthly Revenue: $" + getMonthlyRevenue());
            }
        });

        // Add panel to AdminGUI
        add(panel);
    }

    /**
     * Created by: Will Woodruff
     * Calculates the total revenue for all storage reservations up to and including the current month.
     * Each reservation contributes its price to the total if it falls within or before the current month/year.
     *
     * @return the total monthly revenue
     */
    public static int getMonthlyRevenue() {
        // Retrieve reservation data: [year, month, price] triplets
        List<Integer> reservations = MySQL.getReservationRevenueInfo();
        int total = 0;

        // Get the current year and month
        LocalDate now = LocalDate.now();
        int currentYear = now.getYear();
        int currentMonth = now.getMonthValue();

        // Iterate through reservation entries in sets of three (year, month, price)
        for (int i = 0; i < reservations.size(); i += 3) {
            int year = reservations.get(i);
            int month = reservations.get(i + 1);
            int price = reservations.get(i + 2);

            // Include the reservation in total if it's from this month or earlier
            if ((year < currentYear) || (year == currentYear && month <= currentMonth)) {
                total += price;
            }
        }

        return total;
    }


}
