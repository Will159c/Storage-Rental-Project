import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.net.URL;
import java.sql.SQLException;
import java.util.Date;
import java.time.ZoneId;


/**
 * A) Class name: DateLabelFormatter
 * B) Date of the Code: March 30 ,2025
 * C) Programmer's name: William, Alexis Anguiano
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
    private static final String PASSWORD = "OaWunWeWjnACHWrhVxwAIQJVZPtotFuD"; // Replace with actual password

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


    /**
     * Created by: Will Woodruff
     * Checks if a user with the given username already exists in the database.
     *
     * @param username the username to check for existence
     * @return true if the username exists, false otherwise
     */
    public static boolean isUser(String username) {
        // SQL query that returns true if a row with the given username exists
        String checkIfUsernameExists = "SELECT EXISTS (SELECT 1 FROM users WHERE username = ?)";

        try (Connection conn = MySQL.getConnection();
             PreparedStatement stmt = conn.prepareStatement(checkIfUsernameExists)) {

            // Set the username parameter in the query
            stmt.setString(1, username);

            // Execute the query and retrieve the result
            try (ResultSet rs = stmt.executeQuery()) {
                // Return the boolean result from the query (true if exists, false otherwise)
                if (rs.next()) {
                    return rs.getBoolean(1);
                } else {
                    return false;
                }
            }
        } catch (SQLException e) {
            // Wrap any SQL exception into a runtime exception
            throw new RuntimeException(e);
        }
    }


    /**
     * Created by: Will Woodruff
     * Verifies if a user exists with the given username and password combination.
     *
     * @param username the username to authenticate
     * @param password the corresponding password
     * @return true if the username and password match a user in the database, false otherwise
     */
    public static boolean isUsernameAndPassword(String username, String password) {
        // SQL query to check if a user exists with the provided username and password
        String checkIfUserCorrect = "SELECT EXISTS (SELECT 1 FROM users WHERE username = ? AND password = ?)";

        try (Connection conn = MySQL.getConnection();
             PreparedStatement stmt = conn.prepareStatement(checkIfUserCorrect)) {

            // Bind the username and password to the prepared statement
            stmt.setString(1, username);
            stmt.setString(2, password);

            // Execute the query and check if a matching user was found
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getBoolean(1);
                } else {
                    return false;
                }
            }
        } catch (SQLException e) {
            // Rethrow any SQL exceptions as runtime exceptions
            throw new RuntimeException(e);
        }
    }


    /**
     * Created by: Will Woodruff
     * Sends a POST request to the backend API to insert a new user into the database.
     *
     * @param username the username of the new user
     * @param password the password for the new user
     * @param email the email address of the new user
     */
    public static void insertUser(String username, String password, String email) {
        try {
            // Set up the connection to the API endpoint
            URL url = new URL("http://localhost:8080/api/users");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setDoOutput(true);

            // Create the JSON payload to send in the request body
            String jsonInputString = String.format(
                    "{\"username\":\"%s\", \"email\":\"%s\", \"password\":\"%s\"}",
                    username, email, password
            );

            // Write the JSON data to the output stream
            try (OutputStream os = con.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // Get and print the response code from the server
            int code = con.getResponseCode();
            System.out.println("Response Code: " + code);

            // Check if the user creation was successful
            if (code == 200 || code == 201) {
                System.out.println("User created successfully.");
            } else {
                System.out.println("Error creating user.");
            }

        } catch (Exception e) {
            // Print any exceptions that occur during the request
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

    /**
     * Created by: Will Woodruff
     * Inserts a new contact information record into the database.
     *
     * @param email the user's email address
     * @param phoneNumber the user's phone number
     */
    public static void insertContactInfo(String email, String phoneNumber) {
        // SQL statement to insert a new row into the contactInfo table
        String sql = "INSERT INTO contactInfo (Email, PhoneNumber) VALUES (?, ?)";

        try (Connection conn = MySQL.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Set the email and phone number parameters in the SQL statement
            stmt.setString(1, email);
            stmt.setString(2, phoneNumber);

            // Execute the insert operation
            stmt.executeUpdate();
            System.out.println("Contact Info Added!");

        } catch (SQLException e) {
            // Rethrow any SQL exceptions as runtime exceptions
            throw new RuntimeException(e);
        }
    }


    /**
     * Created by: Will Woodruff
     * Retrieves all email addresses from the contactInfo table.
     *
     * @return a list of email strings found in the contactInfo table
     */
    private static List<String> getContactEmails() {
        // SQL query to select all records from the contactInfo table
        String sql = "SELECT * FROM contactInfo";
        List<String> emails = new ArrayList<>();

        try (Connection conn = MySQL.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            // Iterate through the result set and add each email to the list
            while (rs.next()) {
                emails.add(rs.getString("Email"));
            }

        } catch (Exception e) {
            // Wrap and rethrow any exceptions as runtime exceptions
            throw new RuntimeException(e);
        }

        return emails;
    }


    /**
     * Created by: Will Woodruff
     * Prints all contact information (phone numbers and emails) from the database.
     */
    public static void printContactInfo() {
        // Retrieve all phone numbers and emails from the database
        List<String> phoneNumber = getContactNumbers();
        List<String> emails = getContactEmails();

        // Print all phone numbers
        System.out.println("Phone Numbers:");
        for (String s : phoneNumber) {
            System.out.println(s);
        }

        // Print all email addresses
        System.out.println("\nEmails:");
        for (String email : emails) {
            System.out.println(email);
        }
    }


    /**
     * Created by: Will Woodruff
     * Retrieves all contact information (phone numbers and emails) and combines them into a single list.
     *
     * @return a list containing all phone numbers followed by all emails
     */
    public static List<String> getContactInformation() {
        // Get phone numbers and emails from the database
        List<String> phoneNumber = getContactNumbers();
        List<String> emails = getContactEmails();

        // Create a list to store all contact information
        List<String> contactInformation = new ArrayList<>();

        // Add phone numbers first
        contactInformation.addAll(phoneNumber);

        // Add emails afterward
        contactInformation.addAll(emails);

        return contactInformation;
    }



    /**
     * Created by: Will Woodruff
     * Retrieves all phone numbers from the contactInfo table and stores them in a list.
     *
     * @return a list of phone numbers found in the contactInfo table
     */
    private static List<String> getContactNumbers() {
        // SQL query to select all records from the contactInfo table
        String sql = "SELECT * FROM contactInfo";
        List<String> phoneNumbers = new ArrayList<>();

        try (Connection conn = MySQL.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            // Iterate through the result set and add each phone number to the list
            while (rs.next()) {
                phoneNumbers.add(rs.getString("PhoneNumber"));
            }

        } catch (Exception e) {
            // Wrap and rethrow any exceptions as runtime exceptions
            throw new RuntimeException(e);
        }

        return phoneNumbers;
    }


    /**
     * Created by: Will Woodruff
     * Updates the email address associated with the given username.
     * If the email is already linked to another user, the update is aborted.
     *
     * @param username the username whose email is to be set
     * @param email the new email to assign to the user
     */
    public static void setEmail(String username, String email) {
        // SQL query to update the email for a specific username
        String sql = "UPDATE users SET email = ? WHERE username = ?";

        // Check if the email is already associated with an existing user
        if (isEmail(email)) {
            System.out.println("Error, email already exists.");
            return;
        }

        try (Connection conn = MySQL.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Set the new email and the corresponding username in the query
            stmt.setString(1, email);
            stmt.setString(2, username);

            // Execute the update
            stmt.executeUpdate();

        } catch (SQLException e) {
            // Wrap and rethrow any SQL exceptions
            throw new RuntimeException(e);
        }
    }


    /**
     * Created by: Will Woodruff
     * Checks if the given email is already associated with an existing user in the database.
     *
     * @param email the email address to check
     * @return true if the email exists in the users table, false otherwise
     */
    public static boolean isEmail(String email) {
        // SQL query to check if the given email exists in the users table
        String checkIfEmailExists = "SELECT EXISTS (SELECT 1 FROM users WHERE email = ?)";

        try (Connection conn = MySQL.getConnection();
             PreparedStatement stmt = conn.prepareStatement(checkIfEmailExists)) {

            // Set the email parameter in the query
            stmt.setString(1, email);

            // Execute the query and return the result
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getBoolean(1);
                } else {
                    return false;
                }
            }
        } catch (SQLException e) {
            // Rethrow as a runtime exception
            throw new RuntimeException(e);
        }
    }


    /**
     * Created by: Will Woodruff
     * Deletes a user from the database using their username.
     *
     * @param username the username of the user to delete
     */
    public static void deleteUser(String username) {
        // SQL statement to delete the user with the given username
        String sql = "DELETE FROM users WHERE username = ?";

        try (Connection conn = MySQL.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Set the username parameter in the SQL query
            stmt.setString(1, username);

            // Execute the delete operation
            stmt.executeUpdate();

        } catch (SQLException e) {
            // Rethrow any SQL exceptions as runtime exceptions
            throw new RuntimeException(e);
        }
    }


    /**
     * Created by: Will Woodruff
     * Retrieves all storage unit IDs from the storage table and returns them in a dynamic list.
     *
     * @return a list of storage unit IDs
     */
    public static List<Integer> getStorageID() {
        // SQL query to select all records from the storage table
        String sql = "SELECT * FROM storage";
        List<Integer> ids = new ArrayList<>();

        try (Connection conn = MySQL.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            // Add each storage unit ID to the list
            while (rs.next()) {
                ids.add(rs.getInt("id"));
            }

        } catch (Exception e) {
            // Wrap and rethrow any exceptions as runtime exceptions
            throw new RuntimeException(e);
        }

        return ids;
    }


    /**
     * Created by: Will Woodruff
     * Retrieves all storage unit information for a given ID and stores it in a list of objects.
     * The returned list includes: ID (int), size (String), price (int), and location (String).
     *
     * @param id the ID of the storage unit to retrieve
     * @return a list containing the storage unit's information, or an empty list if not found
     */
    public static List<Object> getStorageInformation(int id) {
        // SQL query to select a storage unit by its ID
        String sql = "SELECT * FROM storage WHERE id = ?";
        List<Object> unit = new ArrayList<>();

        try (Connection conn = MySQL.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Set the ID parameter in the query
            stmt.setInt(1, id);

            // Execute the query and retrieve the storage unit's data
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
            // Rethrow any exceptions as runtime exceptions
            throw new RuntimeException(e);
        }

        return unit;
    }


    /**
     * Created by: Will Woodruff
     * Creates a new storage unit entry in the database with the given size, price, and location.
     *
     * @param size the size description of the storage unit (e.g. "10x10")
     * @param price the monthly rental price of the storage unit
     * @param location the location of the storage unit
     */
    public static void createNewStorageUnit(String size, int price, String location) {
        // SQL query to insert a new storage unit into the storage table
        String sql = "INSERT INTO storage (size, price, location) VALUES (?, ?, ?)";

        try (Connection conn = MySQL.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Set the size, price, and location parameters in the SQL query
            stmt.setString(1, size);
            stmt.setInt(2, price);
            stmt.setString(3, location);

            // Execute the insert operation
            stmt.executeUpdate();

        } catch (SQLException e) {
            // Rethrow any SQL exceptions as runtime exceptions
            throw new RuntimeException(e);
        }
    }


    /**
     * Created by: Will Woodruff
     * Updates the price of a specific storage unit in the database.
     *
     * @param id the ID of the storage unit to update
     * @param price the new price to set for the unit
     */
    public static void changePriceOfUnit(int id, int price) {
        // SQL query to update the price of a storage unit by ID
        String sql = "UPDATE storage SET price = ? WHERE id = ?";

        try (Connection conn = MySQL.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Set the new price and unit ID in the query
            stmt.setInt(1, price);
            stmt.setInt(2, id);

            // Execute the update
            stmt.executeUpdate();

        } catch (SQLException e) {
            // Wrap and rethrow any SQL exceptions
            throw new RuntimeException(e);
        }
    }


    /**
     * Created by: Will Woodruff
     * Retrieves a list of storage unit IDs that the specified user has reserved.
     *
     * @param id_user the ID of the user whose reservations are being fetched
     * @return a list of storage unit IDs reserved by the user
     */
    public static List<Integer> getUserReservations(int id_user) {
        // SQL query to select all reservations made by the user
        String sql = "SELECT * FROM storage_reservations WHERE user_id = ?";
        List<Integer> storageID = new ArrayList<>();

        try (Connection conn = MySQL.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Set the user ID in the query
            stmt.setInt(1, id_user);

            // Execute the query and collect the storage IDs
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                storageID.add(rs.getInt("storage_id"));
            }

        } catch (Exception e) {
            // Rethrow any exceptions as runtime exceptions
            throw new RuntimeException(e);
        }

        return storageID;
    }


    /**
     * Created by: Will Woodruff
     * Retrieves the user ID associated with the given username.
     *
     * @param username the username to search for
     * @return the ID of the user with the given username
     * @throws RuntimeException if no user is found with the given username
     */
    public static int getUserID(String username) {
        // SQL query to select the user by username
        String sql = "SELECT * FROM users WHERE userName = ?";

        try (Connection conn = MySQL.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Set the username parameter in the query
            stmt.setString(1, username);

            // Execute the query and return the user ID if found
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            } else {
                // Throw an exception if no user is found
                throw new RuntimeException("No user found with username: " + username);
            }

        } catch (Exception e) {
            // Rethrow any exception as a runtime exception
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
            }

            reserveStmt.setInt(1, storageID);
            reserveStmt.setString(2, email);
            reserveStmt.setInt(3, id_user);

            ZoneId pacificZone = ZoneId.of("America/Los_Angeles");
            LocalDate shifted = startDate.toInstant()
                    .atZone(pacificZone)
                    .toLocalDate()
                    .plusDays(1); // shift to fix DATE+UTC truncation
            reserveStmt.setDate(4, java.sql.Date.valueOf(shifted));

            reserveStmt.setInt(5, price);

            if (reserveStmt.executeUpdate() > 0) {
                EmailNotifier.sendEmail("storagerentalproject@gmail.com", "New Reservation",
                        "A new storage unit (ID: " + storageID + ") has been reserved.");
                return true;
            }

            return false;

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

    /**
     * Created by: Will Woodruff
     * Retrieves reservation revenue data from the storage_reservations table.
     * For each reservation, it extracts the year and month of the start date, and the price.
     *
     * @return a list containing year, month, and price for each reservation (in that order, repeated)
     */
    public static List<Integer> getReservationRevenueInfo() {
        // SQL query to retrieve the start date and price of each reservation
        String sql = "SELECT start_date, price FROM storage_reservations";
        List<Integer> storageInfo = new ArrayList<>();

        try (Connection conn = MySQL.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();

            // For each reservation, extract year, month, and price, and add to the list
            while (rs.next()) {
                LocalDate startDate = rs.getDate("start_date").toLocalDate();
                int price = rs.getInt("price");

                storageInfo.add(startDate.getYear());        // Add year
                storageInfo.add(startDate.getMonthValue());  // Add month
                storageInfo.add(price);                      // Add price
            }

        } catch (Exception e) {
            // Rethrow any exceptions as runtime exceptions
            throw new RuntimeException(e);
        }

        return storageInfo;
    }



    /**
     * Created by: Will Woodruff
     * Retrieves the price of a storage unit based on its ID.
     * Used as a helper method for calculating reservation revenue.
     *
     * @param storage_id the ID of the storage unit
     * @return the price of the storage unit, or -1 if the unit is not found
     */
    public static int getPrice(int storage_id) {
        // SQL query to get the price of a storage unit by its ID
        String sql = "SELECT price FROM storage WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Set the storage unit ID in the query
            stmt.setInt(1, storage_id);

            // Execute the query and return the price if found
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("price");
                } else {
                    // Return -1 if no storage unit was found
                    return -1;
                }
            }

        } catch (SQLException e) {
            // Rethrow any SQL exceptions as runtime exceptions
            throw new RuntimeException(e);
        }
    }



}

