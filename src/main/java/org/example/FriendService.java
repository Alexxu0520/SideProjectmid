package org.example;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FriendService {

    // Helper method to get user ID by email
    private Integer getUserIdByEmail(String email) {
        String query = "SELECT user_id FROM users WHERE email = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("user_id");
            } else {
                return null;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String sendFriendRequest(String userEmail, String friendEmail) {
        // Find user ID from email
        Integer userId = getUserIdByEmail(userEmail);
        Integer friendId = getUserIdByEmail(friendEmail);

        if (userId == null || friendId == null) {
            return "Error: One or both users not found.";
        }

        // Check if a friendship already exists
        String checkFriendQuery = "SELECT * FROM friends WHERE user_id = ? AND friend_user_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement checkFriendStmt = connection.prepareStatement(checkFriendQuery)) {

            checkFriendStmt.setInt(1, userId);
            checkFriendStmt.setInt(2, friendId);
            ResultSet friendResult = checkFriendStmt.executeQuery();

            if (friendResult.next()) {
                return "Error: Friend request already sent or you are already friends.";
            }

            // Insert friend request if it does not exist
            String addFriendQuery = "INSERT INTO friends (user_id, friend_user_id, status) VALUES (?, ?, 'pending')";
            try (PreparedStatement addFriendStmt = connection.prepareStatement(addFriendQuery)) {
                addFriendStmt.setInt(1, userId);
                addFriendStmt.setInt(2, friendId);
                addFriendStmt.executeUpdate();
            }

            return "Friend request sent successfully!";

        } catch (SQLException e) {
            e.printStackTrace();
            return "Error: Unable to send friend request.";
        }
    }

    public String acceptFriendRequest(String userEmail, String friendEmail) {
        Integer userId = getUserIdByEmail(userEmail);
        Integer friendId = getUserIdByEmail(friendEmail);

        if (userId == null || friendId == null) {
            return "Error: User not found.";
        }

        String query = "UPDATE friends SET status = 'accepted' WHERE user_id = ? AND friend_user_id = ? AND status = 'pending'";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, friendId);
            statement.setInt(2, userId);
            int rowsUpdated = statement.executeUpdate();

            if (rowsUpdated > 0) {
                String reciprocalQuery = "INSERT INTO friends (user_id, friend_user_id, status) VALUES (?, ?, 'accepted')";
                try (PreparedStatement reciprocalStmt = connection.prepareStatement(reciprocalQuery)) {
                    reciprocalStmt.setInt(1, userId);
                    reciprocalStmt.setInt(2, friendId);
                    reciprocalStmt.executeUpdate();
                }

                return "Friend request accepted!";
            } else {
                return "Error: No pending friend request from this user.";
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return "Error: Unable to accept friend request.";
        }
    }


    public List<String> getPendingFriendRequests(String userEmail) {
        Integer userId = getUserIdByEmail(userEmail);
        List<String> pendingRequests = new ArrayList<>();

        if (userId == null) {
            return pendingRequests;
        }

        String query = "SELECT users.email FROM friends JOIN users ON friends.user_id = users.user_id WHERE friends.friend_user_id = ? AND friends.status = 'pending'";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                pendingRequests.add(resultSet.getString("email"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pendingRequests;
    }

    public List<String> getFriendsList(String userEmail) {
        Integer userId = getUserIdByEmail(userEmail);
        List<String> friendsList = new ArrayList<>();

        if (userId == null) {
            return friendsList;
        }

        String query = "SELECT users.email FROM friends JOIN users ON friends.friend_user_id = users.user_id WHERE friends.user_id = ? AND friends.status = 'accepted'";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                friendsList.add(resultSet.getString("email"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return friendsList;
    }


    public String removeFriend(String userEmail, String friendEmail) {
        Integer userId = getUserIdByEmail(userEmail);
        Integer friendId = getUserIdByEmail(friendEmail);

        if (userId == null || friendId == null) {
            return "Error: One or both users not found.";
        }

        String query = "DELETE FROM friends WHERE (user_id = ? AND friend_user_id = ?) OR (user_id = ? AND friend_user_id = ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, userId);
            statement.setInt(2, friendId);
            statement.setInt(3, friendId);
            statement.setInt(4, userId);
            int rowsDeleted = statement.executeUpdate();

            if (rowsDeleted > 0) {
                return "Friend removed successfully!";
            } else {
                return "Error: Friend not found.";
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return "Error: Unable to remove friend.";
        }
    }


}
