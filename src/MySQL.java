import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySQL {
    private static final String URL = "jdbc:mysql://caboose.proxy.rlwy.net:54157/railway";
    private static final String USER = "root";
    private static final String PASSWORD = "Replace Here"; // Replace with actual password

    private static Connection getConnection() throws SQLException {  //this gets the connection to the database

        return DriverManager.getConnection(URL, USER, PASSWORD);

    }

    public static boolean isUser(String username) { //returns true if the given string username already exists
        String checkIfUsernameExists = "SELECT EXISTS (SELECT 1 FROM users WHERE username = ?)";

        try (Connection conn = MySQL.getConnection();
             PreparedStatement stmt = conn.prepareStatement(checkIfUsernameExists)) {

            stmt.setString(1, username);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getBoolean(1);
                } else {
                    return false;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public static boolean isUsernameAndPassword(String username, String password) { // returns true if the username and password are correct.
        String checkIfUserCorrect = "SELECT EXISTS (SELECT 1 FROM users WHERE username = ? AND password = ?)";

        try (Connection conn = MySQL.getConnection();
             PreparedStatement stmt = conn.prepareStatement(checkIfUserCorrect)) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getBoolean(1);
                } else {
                    return false;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void insertUser(String username, String password) { //this inserts a new user into database
        String sql = "INSERT INTO users (username, password) VALUES (?, ?)";

        try (Connection conn = MySQL.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.executeUpdate();
            System.out.println("User " + username + " added!");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void insertContactInfo(String email, String phoneNumber) { //this inserts a new contactInfo into the database
        String sql = "INSERT INTO contactInfo (Email, PhoneNumber) VALUES (?, ?)";

        try (Connection conn = MySQL.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            stmt.setString(2, phoneNumber);
            stmt.executeUpdate();
            System.out.println("Contact Info Added!");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static List<String> getContactEmails() {  // Gets all the emails inside of the contactInfo table and puts them into a String list
        String sql = "SELECT * FROM contactInfo";
        List<String> emails = new ArrayList<>();

        try (Connection conn = MySQL.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                emails.add(rs.getString("Email"));
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return emails;
    }

    public static void printContactInfo() { // Prints all the contact information available.
        List<String> phoneNumber = getContactNumbers();
        List<String> emails = getContactEmails();

        // Print phone numbers
        System.out.println("Phone Numbers:");
        for (String s : phoneNumber) {
            System.out.println(s);
        }

        // Print emails
        System.out.println("\nEmails:");
        for (String email : emails) {
            System.out.println(email);
        }

    }

    public static List<String> getContactInformation() { //puts all contactInformation into a single List and returns it
        List<String> phoneNumber = getContactNumbers();
        List<String> emails = getContactEmails();

        List<String> contactInformation = new ArrayList<>();

        contactInformation.addAll(phoneNumber);

        contactInformation.addAll(emails);

        return contactInformation;
    }


    private static List<String> getContactNumbers() {  // Gets all the numbers inside of the contactInfo table and puts them into a String List
        String sql = "SELECT * FROM contactInfo";
        List<String> phoneNumbers = new ArrayList<>();

        try (Connection conn = MySQL.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                phoneNumbers.add(rs.getString("PhoneNumber"));
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return phoneNumbers;
    }

    public static void setEmail(String username, String email) { //checks if the given email already is connected, if not it connects the email to the given user overriding information aswell
        String sql = "UPDATE users SET email = ? WHERE username = ?";
        if(isEmail(email)) {
            System.out.println("Error, email already exists.");
            return;
        }

        try (Connection conn = MySQL.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setString(2, username);
            stmt.executeUpdate();

        } catch (SQLException e){
            throw new RuntimeException(e);
        }

    }

    public static boolean isEmail(String email) { //returns true if a string email is already connected to another account
        String checkIfEmailExists = "SELECT EXISTS (SELECT 1 FROM users WHERE email = ?)";

        try (Connection conn = MySQL.getConnection();
             PreparedStatement stmt = conn.prepareStatement(checkIfEmailExists)) {

            stmt.setString(1, email);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getBoolean(1);
                } else {
                    return false;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void deleteUser(String username) { //removes user from the database based on the user
        String sql = "DELETE FROM users WHERE username = ?";

        try (Connection conn = MySQL.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Integer> getStorageID() {  // gets all the IDs of the storage units and inputs into a single dynamic array and returns that array
        String sql = "SELECT * FROM storage";
        List<Integer> ids = new ArrayList<>();

        try (Connection conn = MySQL.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                ids.add(rs.getInt("id"));
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ids;
    }

    public static List<Object> getStorageInformation(int id) { // gets all the information of a given id and puts it into a Object list
        String sql = "SELECT * FROM storage WHERE id = ?";
        List<Object> unit = new ArrayList<>();

        try (Connection conn = MySQL.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                unit.add(rs.getInt("id"));
                unit.add(rs.getString("size"));
                unit.add(rs.getInt("price"));
                unit.add(rs.getString("location"));
            } else {
                System.out.println("No storage unit found for ID: " + id);
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return unit;
    }

    public static void createNewStorageUnit(String size, int price, String location) { //creates a new storageUnit
        String sql = "INSERT INTO storage (size, price, location) VALUES (?, ?, ?)";

        try (Connection conn = MySQL.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, size);
            stmt.setInt(2, price);
            stmt.setString(3, location);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void changePriceOfUnit(int id, int price) { //changes the price of a storage unit
        String sql = "UPDATE storage SET price = ? WHERE id = ?";

        try (Connection conn = MySQL.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, price);
            stmt.setInt(2, id);
            stmt.executeUpdate();

        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public static boolean reserveStorageUnit(int storageID, String email, String password) {
        // Verify user exists and credentials are correct
        if (!isEmailAndPasswordValid(email, password)) {
            System.out.println("Invalid email or password.");
            return false;
        }

        String checkAvailability = "SELECT * FROM storage_reservations WHERE storage_id = ?";
        String reserveUnit = "INSERT INTO storage_reservations (storage_id, customer_email) VALUES (?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkAvailability);
             PreparedStatement reserveStmt = conn.prepareStatement(reserveUnit)) {

            checkStmt.setInt(1, storageID);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                return false;  // Already reserved
            } else {
                reserveStmt.setInt(1, storageID);
                reserveStmt.setString(2, email);
                if (reserveStmt.executeUpdate() > 0) {
                    // Send email notification
                    EmailNotifier.sendEmail(email, "Storage Unit Reserved",
                            "Your storage unit (ID: " + storageID + ") has been successfully reserved.");

                    // Notify the admin
                    EmailNotifier.sendEmail("storagerentalproject@gmail.com", "New Reservation",
                            "A new storage unit (ID: " + storageID + ") has been reserved.");
                    return true;
                }
                return false;
            }

        } catch (SQLException e) {
            System.err.println("Error reserving storage unit: " + e.getMessage());
            return false;
        }
    }

    public static boolean cancelReservation(int storageID, String email, String password) {
        // Verify user exists and credentials are correct
        if (!isEmailAndPasswordValid(email, password)) {
            System.out.println("Invalid email or password.");
            return false;
        }

        String deleteReservation = "DELETE FROM storage_reservations WHERE storage_id = ? AND customer_email = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(deleteReservation)) {

            stmt.setInt(1, storageID);
            stmt.setString(2, email);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                // Notify the user that their reservation was canceled
                EmailNotifier.sendEmail(email, "Reservation Canceled",
                        "Your reservation for storage unit (ID: " + storageID + ") has been canceled.");

                // Notify the admin
                EmailNotifier.sendEmail("storagerentalproject@gmail.com", "Reservation Canceled",
                        "The reservation for storage unit (ID: " + storageID + ") has been canceled by " + email + ".");

                return true;
            } else {
                System.out.println("No reservation found to cancel.");
                return false;
            }

        } catch (SQLException e) {
            System.err.println("Error canceling reservation: " + e.getMessage());
            return false;
        }
    }



    public static boolean isUnitReserved(int storageID) {
        String query = "SELECT EXISTS (SELECT 1 FROM storage_reservations WHERE storage_id = ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, storageID);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getBoolean(1);  // Ensure it returns true only if reserved
        } catch (SQLException e) {
            System.err.println("Error checking reservation status: " + e.getMessage());
            return false;
        }
    }

    public static void createReservationsTable() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS storage_reservations (" +
                "storage_id INT PRIMARY KEY, " +
                "customer_email VARCHAR(255) NOT NULL, " +
                "customer_name VARCHAR(255) NULL" +
                ")";

        String checkColumnSQL = "SHOW COLUMNS FROM storage_reservations LIKE 'customer_name'";
        String modifyColumnSQL = "ALTER TABLE storage_reservations MODIFY COLUMN customer_name VARCHAR(255) NULL";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            // Create the table if it doesn't exist
            stmt.executeUpdate(createTableSQL);
            System.out.println("Checked/Created table: storage_reservations");

            // Check if the customer_name column exists and update it to allow NULL
            try (ResultSet rs = stmt.executeQuery(checkColumnSQL)) {
                if (rs.next()) { // If the column exists, make it nullable
                    stmt.executeUpdate(modifyColumnSQL);
                    System.out.println("Updated column: customer_name is now optional.");
                }
            }

        } catch (SQLException e) {
            System.err.println("Error creating/updating table: " + e.getMessage());
        }
    }


    public static boolean isEmailAndPasswordValid(String email, String password) {
        String query = "SELECT EXISTS (SELECT 1 FROM users WHERE email = ? AND password = ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            return rs.next() && rs.getBoolean(1);

        } catch (SQLException e) {
            System.err.println("Error verifying email and password: " + e.getMessage());
            return false;
        }
    }

}