package org.example;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserService {

    // Method to register a new user
    public String registerUser(String email, String nickname, String password) {
        // Check if email already exists
        if (emailExists(email) || nicknameExists(nickname)) {
            return "Error: Email or nickname already exists.";
        }

        // Insert new user if email doesn't exist
        String query = "INSERT INTO users (email, password, nickname) VALUES (?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, email);
            statement.setString(2, password); // Use hashed password here for security
            statement.setString(3, nickname);
            statement.executeUpdate();

            return "User registered successfully!";

        } catch (SQLException e) {
            e.printStackTrace();
            return "Error: Unable to register user.";
        }
    }

    // Additional helper method to check nickname uniqueness
    private boolean nicknameExists(String nickname) {
        String query = "SELECT 1 FROM users WHERE nickname = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, nickname);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Method to check if email already exists
    private boolean emailExists(String email) {
        String query = "SELECT 1 FROM users WHERE email = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next(); // Returns true if email exists

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Method to validate login credentials
    public String loginUser(String email, String password) {
        String query = "SELECT password FROM users WHERE email = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String storedPassword = resultSet.getString("password");

                if (storedPassword.equals(password)) { // Compare hashed passwords in real case
                    return "Login successful!";
                } else {
                    return "Error: Incorrect password.";
                }
            } else {
                return "Error: Email not found.";
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return "Error: Unable to login.";
        }
    }

    public String changePassword(String email, String currentPassword, String newPassword) {
        // Verify current password
        String query = "SELECT password FROM users WHERE email = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement selectStatement = connection.prepareStatement(query)) {

            selectStatement.setString(1, email);
            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                String storedPassword = resultSet.getString("password");

                // Check if current password matches
                if (!storedPassword.equals(currentPassword)) {
                    return "Error: Current password is incorrect.";
                }

                // Update password if current password matches
                String updateQuery = "UPDATE users SET password = ? WHERE email = ?";
                try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                    updateStatement.setString(1, newPassword); // In a real application, use hashing here
                    updateStatement.setString(2, email);
                    updateStatement.executeUpdate();

                    return "Password updated successfully!";
                }
            } else {
                return "Error: User not found.";
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return "Error: Unable to change password.";
        }
    }
}
