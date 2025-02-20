import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class mySQL {
    private static final String URL = "jdbc:mysql://caboose.proxy.rlwy.net:54157/railway";
    private static final String USER = "root";
    private static final String PASSWORD = "REPLACE HERE"; // Replace with actual password

    private static Connection getConnection() throws SQLException {  //this gets the connection to the database

        return DriverManager.getConnection(URL, USER, PASSWORD);

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

        try(Connection conn = mySQL.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()) {

            while(rs.next()) {
                ids.add(rs.getInt("id"));
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ids;
    }
}


