package org.example;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class SharedRecordsService {

    private final UserService userService;

    public SharedRecordsService(UserService userService) {
        this.userService = userService;
    }

    // Method to share a history with a friend
    public String shareHistory(String userEmail, String friendEmail, int historyId) {
        Integer userId = userService.getUserIdByEmail(userEmail);
        Integer friendUserId = userService.getUserIdByEmail(friendEmail);

        if (userId == null || friendUserId == null) {
            return "Error: One or both users not found.";
        }

        // Check if the users are friends
        if (!areFriends(userId, friendUserId)) {
            return "Error: You can only share histories with friends.";
        }


        // Check if the history belongs to the user
        if (!historyBelongsToUser(userId, historyId)) {
            return "Error: You can only share your own histories.";
        }

        String query = "INSERT INTO sharedrecords (history_id, user_id, friend_user_id) VALUES (?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, historyId);
            statement.setInt(2, userId);
            statement.setInt(3, friendUserId);
            statement.executeUpdate();

            return "History shared successfully.";

        } catch (SQLException e) {
            e.printStackTrace();
            return "Error: Unable to share history.";
        }
    }

    // Helper method to check if two users are friends
    private boolean areFriends(int userId, int friendUserId) {
        String query = "SELECT 1 FROM friends WHERE (user_id = ? AND friend_user_id = ? OR user_id = ? AND friend_user_id = ?) AND status = 'accepted'";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, userId);
            statement.setInt(2, friendUserId);
            statement.setInt(3, friendUserId);
            statement.setInt(4, userId);
            ResultSet rs = statement.executeQuery();

            return rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Helper method to check if a history belongs to a user
    private boolean historyBelongsToUser(int userId, int historyId) {
        String query = "SELECT 1 FROM histories WHERE history_id = ? AND user_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, historyId);
            statement.setInt(2, userId);
            ResultSet rs = statement.executeQuery();

            return rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Method to get all shared records for a user
    public List<HashMap<String, Object>> getAllSharedRecords(String userEmail) {
        Integer userId = userService.getUserIdByEmail(userEmail);

        if (userId == null) {
            throw new IllegalArgumentException("User not found.");
        }

        String query = "SELECT sr.history_id, u.email AS shared_from_email " +
                "FROM sharedrecords sr " +
                "JOIN users u ON sr.user_id = u.user_id " +
                "WHERE sr.friend_user_id = ?";

        List<HashMap<String, Object>> sharedRecords = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, userId);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                HashMap<String, Object> record = new HashMap<>();
                record.put("history_id", rs.getInt("history_id"));
                record.put("shared_from_email", rs.getString("shared_from_email"));
                sharedRecords.add(record);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to retrieve shared records.");
        }

        return sharedRecords;
    }

    // Method to get shared record details
    public HashMap<String, Object> getSharedRecordDetail(int historyId, String userEmail) {
        Integer userId = userService.getUserIdByEmail(userEmail);

        if (userId == null) {
            throw new IllegalArgumentException("User not found.");
        }

        // Check if the user has access to this shared history
        if (!hasAccessToHistory(userId, historyId)) {
            throw new IllegalArgumentException("You do not have access to this history.");
        }

        String query = "SELECT categories, category_scores, sensitivity_score FROM histories WHERE history_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, historyId);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                HashMap<String, Object> details = new HashMap<>();
                ObjectMapper objectMapper = new ObjectMapper();

                // Parse JSON fields back into objects
                ModerationCategories categories = objectMapper.readValue(rs.getString("categories"), ModerationCategories.class);
                ModerationCategoryScores categoryScores = objectMapper.readValue(rs.getString("category_scores"), ModerationCategoryScores.class);
                double sensitivityScore = rs.getDouble("sensitivity_score");

                details.put("categories", categories);
                details.put("category_scores", categoryScores);
                details.put("sensitivity_score", sensitivityScore);

                return details;
            } else {
                throw new IllegalArgumentException("History not found.");
            }

        } catch (SQLException | IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to retrieve shared record details.");
        }
    }

    // Helper method to check if the user has access to the shared history
    private boolean hasAccessToHistory(int userId, int historyId) {
        String query = "SELECT 1 FROM sharedrecords WHERE friend_user_id = ? AND history_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, userId);
            statement.setInt(2, historyId);
            ResultSet rs = statement.executeQuery();

            return rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}