import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class mySQL {
    private static final String URL = "jdbc:mysql://caboose.proxy.rlwy.net:54157/railway";
    private static final String USER = "root";
    private static final String PASSWORD = "Replace Here"; // Replace with actual password

    private static Connection getConnection() throws SQLException {  //this gets the connection to the database

        return DriverManager.getConnection(URL, USER, PASSWORD);

    }

    public static boolean isUser(String username) { //returns true if the given string username already exists
        String checkIfUsernameExists = "SELECT EXISTS (SELECT 1 FROM users WHERE username = ?)";

        try(Connection conn = mySQL.getConnection();
            PreparedStatement stmt = conn.prepareStatement(checkIfUsernameExists)) {

            stmt.setString(1, username);

            try(ResultSet rs = stmt.executeQuery()) {
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

    public static boolean isUsernameAndPassword(String username, String password) {
        String checkIfUserCorrect = "SELECT EXISTS (SELECT 1 FROM users WHERE username = ? AND password = ?)";

        try(Connection conn = mySQL.getConnection();
            PreparedStatement stmt = conn.prepareStatement(checkIfUserCorrect)) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            try(ResultSet rs = stmt.executeQuery()) {
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

        try (Connection conn = mySQL.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.executeUpdate();
            System.out.println("User " + username + " added!");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void deleteUser(String username) { //removes user from the database based on the user
        String sql = "DELETE FROM users WHERE username = ?";

        try (Connection conn = mySQL.getConnection();
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

        try (Connection conn = mySQL.getConnection();
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


    public static boolean reserveStorageUnit(int storageID, String customerName) { // reserves
        String checkAvailability = "SELECT * FROM storage_reservations WHERE storage_id = ?";
        String reserveUnit = "INSERT INTO storage_reservations (storage_id, customer_name) VALUES (?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkAvailability);
             PreparedStatement reserveStmt = conn.prepareStatement(reserveUnit)) {

            checkStmt.setInt(1, storageID);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                return false;  // Reservation failed (already taken)
            } else {
                reserveStmt.setInt(1, storageID);
                reserveStmt.setString(2, customerName);
                reserveStmt.executeUpdate();
                return true;  // Reservation successful
            }

        } catch (SQLException e) {
            System.err.println("Error reserving storage unit: " + e.getMessage());
            return false;
        }
    }

    public static boolean cancelReservation(int storageID, String customerName) { // cancels
        String deleteReservation = "DELETE FROM storage_reservations WHERE storage_id = ? AND customer_name = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(deleteReservation)) {

            stmt.setInt(1, storageID);
            stmt.setString(2, customerName);
            int rowsAffected = stmt.executeUpdate();

            return rowsAffected > 0;  // Returns true if a reservation was canceled

        } catch (SQLException e) {
            System.err.println("Error cancelling reservation: " + e.getMessage());
            return false;
        }
    }

    public static boolean isUnitReserved(int storageID) {
        String query = "SELECT * FROM storage_reservations WHERE storage_id = ? LIMIT 1";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, storageID);
            ResultSet rs = stmt.executeQuery();
            return rs.next(); // If we get at least one row, it's reserved

        } catch (SQLException e) {
            System.err.println("Error checking reservation status: " + e.getMessage());
            return false;  // Fallback to 'not reserved' if there's an error
        }
    }

}