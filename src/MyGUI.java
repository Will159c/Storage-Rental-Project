import javax.swing.*;
import java.awt.*;

/**
 * This class is is the setup for the overall GUI. It holds the pages for the other GUI classes,
 * initializes them, and controls the navigation between pages. It allows the other pages to transfer
 * information between one another.
 */
public class MyGUI {

    private static JLabel titleTxt;
    private static JButton registerButton;
    private static JButton loginButton;
    private static JPanel cardpanel;
    private static CardLayout cardLayout;
    private String username;
    /**
     * Allows for user view after log in or returning to User page
     */
    private UserGUI userGUI; // make userGUI be a private attribute to be able to invoke it elsewhere
    /**
     * Needed for going to page for cancelling reservations
     */
    private CancelGUI cancelGUI;
    /**
     * Needed for going to admin page for creating storage units and updating page
     */
    private ManageStorageGUI storageManageScreen;

    /**
     * Initializes and displays the GUI frame used for the program. Also sets up the GUI pages using cardlayout
     */
    public MyGUI() {
        /////////////// Initial GUI Settings /////////////
        JFrame frame = new JFrame();
        frame.setSize(1000, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null); // Centers the window

        cardLayout = new CardLayout();
        cardpanel = new JPanel(cardLayout);

        // GUI pages and userGUI setup
        LoginScreenGUI loginScreen = new LoginScreenGUI(this);
        StorageGUI storageScreen = new StorageGUI(this);
        RegisterGUI registerScreen = new RegisterGUI(this);
        AdminGUI adminScreen = new AdminGUI(this);
        storageManageScreen = new ManageStorageGUI(this);
        ManageUsersGUI userManageScreen = new ManageUsersGUI(this);

        // Collect my pages
        cardpanel.add(welcomeScreen(), "Welcome Screen");
        cardpanel.add(registerScreen, "Registration");
        cardpanel.add(loginScreen, "Login");
        cardpanel.add(storageScreen, "Storage Screen");
        cardpanel.add(adminScreen, "Admin Screen");
        cardpanel.add(storageManageScreen, "Manage Storage Screen");
        cardpanel.add(userManageScreen, "Manage User Screen");


        frame.add(cardpanel);
        cardLayout.show(cardpanel, "Welcome Screen"); // Show the first page

        frame.setVisible(true);
    }

    /**
     * Creates the welcome screen panel, allowing the user to login or register using JButtons
     * @return the create welcome screen panel
     */
    private JPanel welcomeScreen() {
        ///////////// Initial Settings ///////////
        JPanel panel = new BackgroundSetter("/background.jpg", new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints(); // Allows for ordered positioning

        ///////////// Text ///////////

        // Title text
        titleTxt = new JLabel("Welcome to Storage Rental", SwingConstants.CENTER);
        titleTxt.setFont(new Font("SansSerif", Font.BOLD, 40));
        titleTxt.setOpaque(true); // Allow for background of border to be colored
        titleTxt.setBackground(Color.BLACK); // Set background border color
        titleTxt.setForeground(Color.WHITE); // Set text color
        titleTxt.setBorder(BorderFactory.createLineBorder(Color.pink)); // Create the border
        titleTxt.setMinimumSize(new Dimension(400, 400));
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(titleTxt, gbc);

        ////////////// Buttons ////////////

        /////// Register Button
        // Button Design
        registerButton = new JButton("Register");
        registerButton.setPreferredSize(new Dimension(180, 50));
        gbc.gridy = 1; // Move button to next row
        gbc.insets = new Insets(20, 0, 10, 0); // Add spacing in between buttons and labels
        panel.add(registerButton, gbc);

        // Register Button action
        registerButton.addActionListener(e -> showMain("Registration"));

        // Login Button
        loginButton = new JButton("Log-in");
        loginButton.setPreferredSize(new Dimension(180, 50));
        gbc.gridy = 2; // Move button to next row
        gbc.insets = new Insets(20, 0, 10, 0); // Add spacing in between buttons and labels
        panel.add(loginButton, gbc);

        // Login button action
        loginButton.addActionListener(e -> showMain("Login"));
        return panel;
    }

    /**
     * Displays the requested screen/panel using it's name.
     * Also checks if the requested page is the Manage Storage Page
     * (Create Storage Unit Page) to refresh the list of units after
     * new unit is created.
     *
     * @param panelName the name of the page/panel to display
     */
    public void showMain(String panelName) {
        if (panelName.equals("Manage Storage Screen")) {
            storageManageScreen.refreshStorageList();
        }
        cardLayout.show(cardpanel, panelName);
    }

    /**
     * Adds panel to the cardlayout in StorageGUI (the
     * Reservation page)
     *
     * @param panel
     * @param name
     */
    public void addPanel(JPanel panel, String name) {
        cardpanel.add(panel, name);
    }

    /**
     * Set's the current logged-in username
     *
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Returns currently set username
     *
     * @return username
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * Logs the user into the UserGUI page
     * Used for log-in screen and returns to the User page
     *
     * @param user username of the user currently logged in
     */
    public void loginUser(String user) { // Allow for information to be passed after login
        // Set the information ready for User GUI
        this.username = user;
        userGUI = new UserGUI(this, user);
        StorageGUI storageScreen = new StorageGUI(this);
        cardpanel.add(userGUI, "User Screen");
        cardpanel.add(storageScreen, "Storage Screen");

        // Switch the User GUI
        cardLayout.show(cardpanel, "User Screen");
    }

    /**
     * Switch to cancellation screen while transfering
     * user's username and the storage unit ID to cancel
     * reservation
     *
     * @param user the user cancelling their reservation
     * @param id the storage unit ID
     */
    public void toCancellation(String user, int id) {
        cancelGUI = new CancelGUI(this, user, id);
        cardpanel.add(cancelGUI, "Cancel Reservation");
        cardLayout.show(cardpanel, "Cancel Reservation");
    }

}


