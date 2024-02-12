import java.sql.*;

public class ClientService {

    private static final String JDBC_URL = "jdbc:mysql://127.0.0.1:3306/Users";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = ""; // Update this if there is a password

    private Connection connect() throws ClassNotFoundException, SQLException {
        // Register JDBC driver
        Class.forName("com.mysql.cj.jdbc.Driver");
        // Open a connection
        return DriverManager.getConnection(JDBC_URL, DB_USERNAME, DB_PASSWORD);
    }

    public boolean authenticate(String userName, String userPass) {
        String sql = "SELECT * FROM Clients WHERE userName = ? AND password = ?";
        try (Connection connection = connect();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            
            preparedStatement.setString(1, userName);
            preparedStatement.setString(2, userPass);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next(); // User found
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false; // User not found or an error occurred
    }

    public boolean changePassword(String userName, String oldPassword, String newPassword) {
        if (oldPassword.equals(newPassword)) {
            System.out.println("New password cannot be the same as the old password.");
            return false;
        }

        String updateSql = "UPDATE Clients SET password = ? WHERE userName = ?";
        String historySql = "INSERT INTO PasswordHistory (userName, oldPassword) VALUES (?, ?)";
        
        try (Connection connection = connect();
             PreparedStatement updateStmt = connection.prepareStatement(updateSql);
             PreparedStatement historyStmt = connection.prepareStatement(historySql)) {
            
            // Start a transaction
            connection.setAutoCommit(false);

            // Insert the old password into the history table
            historyStmt.setString(1, userName);
            historyStmt.setString(2, oldPassword);
            historyStmt.executeUpdate();

            // Update the user's password in the Clients table
            updateStmt.setString(1, newPassword);
            updateStmt.setString(2, userName);
            updateStmt.executeUpdate();

            // Commit the transaction
            connection.commit();

            return true;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false; // Password change failed or an error occurred
    }
}
