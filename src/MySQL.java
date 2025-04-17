import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.net.URL;
import java.sql.SQLException;
import java.util.Date;

/**
 * A) Class name: DateLabelFormatter
 * B) Date of the Code: March 30 ,2025
 * C) Programmer's name: William Alexis Anguiano
 * D) Brief description: This class serves as the main data access layer for the application. It connects to a
 * mysql database and performs operations.
 * E) Brief explanation of important function:
 * -getAllStorageDetails: Retrieves a full list of all storage units with reservation status.
 * -isUser, isUsernameAndPassword: Authenticates user credentials.
 * -insertUser,deleteUser: Creates and deletes user accounts
 * -insertContactInfo: Adds contact info
 * -reserveStorageUnit, cancelReservation: Handles storage units reservation/cancelation
 * -getUserID, getEmailByUsername: gets users info based on parameters inserted
 * -getReservationRevenueInfo: Helper for revenue calculation
 * F) Important data structures: StorageDetails
 * G) Algorithm used:
 * -Uses basic SQL query delegation via JDBC
 * -reservation logic checks for duplicates
 */

public class MySQL {
    private static final String URL = "jdbc:mysql://caboose.proxy.rlwy.net:54157/railway";
    private static final String USER = "root";
    private static final String PASSWORD = "replace here"; // Replace with actual password

    /**
     * Establishes and returns a database connection using predefined URL, user, and password.
     * @return a {@link Connection} to the database
     * @throws SQLException if a database access error occurs
     */
    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    /**
     * Holds the details of a storage unit in memory to display the units faster.
     */
    public static class StorageDetails {
        private int id;
        private String size;
        private int price;
        private String location;
        private boolean reserved;

        /**
         * new instance for the cache like structure of StorageDetails.
         * @param id        the storage unit's ID
         * @param size      the size of the storage unit
         * @param price     the price of the storage unit
         * @param location  the location of the storage unit
         * @param reserved  {@code true} if the storage unit is reserved; {@code false} otherwise
         */
        public StorageDetails(int id, String size, int price, String location, boolean reserved) {
            this.id = id;
            this.size = size;
            this.price = price;
            this.location = location;
            this.reserved = reserved;
        }

        /**
         * @return the storage unit's ID
         */
        public int getId() {
            return id;
        }

        /**
         * @return the storage unit's size
         */
        public String getSize() {
            return size;
        }

        /**
         * @return the storage unit's price
         */
        public int getPrice() {
            return price;
        }

        /**
         * @return the storage unit's location
         */
        public String getLocation() {
            return location;
        }

        /**
         * Indicates whether the storage unit is reserved.
         *
         * @return {@code true} if reserved, {@code false} otherwise
         */
        public boolean isReserved() {
            return reserved;
        }
    }

    /**
     * Retrieves all storage unit details from the database.
     * @return a {@code List} of {@link StorageDetails} objects containing storage unit information
     */
    public static List<StorageDetails> getAllStorageDetails() {
        String sql = "SELECT s.id, s.size, s.price, s.location, r.storage_id AS reserved " +
                "FROM storage s " +
                "LEFT JOIN storage_reservations r ON s.id = r.storage_id";

        List<StorageDetails> allUnits = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String size = rs.getString("size");
                int price = rs.getInt("price");
                String location = rs.getString("location");
                // Determines if the storage unit is reserved by checking if the "reserved" column is non-null.
                boolean isReserved = (rs.getObject("reserved") != null);
                allUnits.add(new StorageDetails(id, size, price, location, isReserved));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return allUnits;
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

    public static void insertUser(String username, String password, String email) { //this inserts a new user into the database
        try {
            URL url = new URL("http://localhost:8080/api/users");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setDoOutput(true);

            String jsonInputString = String.format(
                    "{\"username\":\"%s\", \"email\":\"%s\", \"password\":\"%s\"}",
                    username, email, password
            );

            try (OutputStream os = con.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int code = con.getResponseCode();
            System.out.println("Response Code: " + code);

            if (code == 200 || code == 201) {
                System.out.println("User created successfully.");
            } else {
                System.out.println("Error creating user.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


//    public static void insertUser(String username, String password, String email) { //old insertUser method. Updated API method above.
//        String sql = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
//
//        try (Connection conn = MySQL.getConnection();
//             PreparedStatement stmt = conn.prepareStatement(sql)) {
//
//            stmt.setString(1, username);
//            stmt.setString(2, password);
//            stmt.setString(3, email);
//            stmt.executeUpdate();
//            System.out.println("User " + username + " added!");
//
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }

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
        if (isEmail(email)) {
            System.out.println("Error, email already exists.");
            return;
        }

        try (Connection conn = MySQL.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setString(2, username);
            stmt.executeUpdate();

        } catch (SQLException e) {
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

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Integer> getUserReservations(int id_user) { //this takes a input of a user id and returns a list of storage id's that that user has reserved
        String sql = "SELECT * FROM storage_reservations WHERE user_id = ?";
        List<Integer> storageID = new ArrayList<>();

        try (Connection conn = MySQL.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id_user);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                storageID.add(rs.getInt("storage_id"));
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return storageID;
    }

    public static int getUserID(String username) { // this takes input of a username and returns the id of the given user
        String sql = "SELECT * FROM users WHERE userName = ?";

        try (Connection conn = MySQL.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            } else {
                throw new RuntimeException("No user found with username: " + username);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Created by: Alexis Anguiano
     * Attempts to reserve a storage unit
     * @param storageID unit ID
     * @param email customer email
     * @param id_user user ID
     * @param password password for verification
     * @param startDate reservation start date
     * @param price price of unit
     * @return true if successful false otherwise
     */
    public static boolean reserveStorageUnit(int storageID, String email, int id_user, String password, Date startDate, int price) {
        if (!isEmailAndPasswordValid(email, password)) {
            System.out.println("Invalid email or password.");
            return false;
        }

        String checkAvailability = "SELECT * FROM storage_reservations WHERE storage_id = ?";
        String reserveUnit = "INSERT INTO storage_reservations (storage_id, customer_email, user_id, start_date, price) VALUES (?, ?, ?, ?, ?)";

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
                reserveStmt.setInt(3, id_user);
                reserveStmt.setDate(4, new java.sql.Date(startDate.getTime()));
                reserveStmt.setInt(5, price);

                if (reserveStmt.executeUpdate() > 0) {
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

    /**
     * Created by: Alexis Anguiano
     * Cancels a reservation if the user and unit match
     * @param storageID unit ID
     * @param email customer email
     * @param password password for verification
     * @return true if successfully canceled.
     */
    public static boolean cancelReservation(int storageID, String email, String password) {
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
                EmailNotifier.sendCancellationConfirmation(email, storageID);
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

    /**
     *Created by: Alexis Anguiano
     * Validates email/password combination exists in users table
     * @param email unit ID
     * @param password users password
     * @return true if valid
     */
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

    /**
     * Created by: Alexis Anguiano
     * gets a users id by email
     * @param email the email
     * @return user id if found
     */
    public static int getUserIDByEmail(String email) {
        String sql = "SELECT id FROM users WHERE email = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            } else {
                throw new RuntimeException("No user found with email: " + email);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Created by: Alexis Anguiano
     * Gets users email from username
      * @param username clients username
     * @return associated email or null
     */
    public static String getEmailByUsername(String username) {
        String sql = "SELECT email FROM users WHERE userName = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getString("email");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    /**
     * Created by: Alexis Anguiano
     * Checks if a reservation belongs to a specific user
     * @param storageID unit id
     * @param email customer email
     * @return true if match
     */
    public static boolean isUnitReservedByUser(int storageID, String email) {
        String query = "SELECT 1 FROM storage_reservations WHERE storage_id = ? AND customer_email = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, storageID);
            stmt.setString(2, email);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.err.println("Error checking reservation ownership: " + e.getMessage());
            return false;
        }
    }

    public static List<Integer> getReservationRevenueInfo() {
        String sql = "SELECT start_date, price FROM storage_reservations";
        List<Integer> storageInfo = new ArrayList<>();

        try (Connection conn = MySQL.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                LocalDate startDate = rs.getDate("start_date").toLocalDate();
                int price = rs.getInt("price");

                storageInfo.add(startDate.getYear());  // Year
                storageInfo.add(startDate.getMonthValue()); // Month
                storageInfo.add(price);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return storageInfo;
    }


    public static int getPrice(int storage_id) { //Helper for getReservationRevenueInfo, it grabs the price of a storage unit given a units id.
        String sql = "SELECT price FROM storage WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, storage_id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("price");
                } else {
                    return -1;
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}

