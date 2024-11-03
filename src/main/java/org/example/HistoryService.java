package org.example;
import org.postgresql.util.PGobject;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Service
public class HistoryService {
    private final UserService userService;
    public HistoryService(UserService userService) {
        this.userService = userService;
    }
    private final ObjectMapper objectMapper = new ObjectMapper();
    public int saveModerationResult(ModerationNewResponse response, String text, double sensitivityScore, int userId) {
        String query = "INSERT INTO histories (user_id, text, categories, category_scores, sensitivity_score, model, flagged) VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING history_id";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            // Convert ModerationCategories and ModerationCategoryScores to JSON strings
            String categoriesJson = objectMapper.writeValueAsString(response.getResults().get(0).getCategories());
            String categoryScoresJson = objectMapper.writeValueAsString(response.getResults().get(0).getCategoryScores());

            // Create PGobject for jsonb columns
            PGobject categoriesObject = new PGobject();
            categoriesObject.setType("jsonb");
            categoriesObject.setValue(categoriesJson);

            PGobject categoryScoresObject = new PGobject();
            categoryScoresObject.setType("jsonb");
            categoryScoresObject.setValue(categoryScoresJson);

            // Set parameters in the PreparedStatement
            statement.setInt(1, userId);
            statement.setString(2, text);
            statement.setObject(3, categoriesObject);  // Set categories as jsonb
            statement.setObject(4, categoryScoresObject);  // Set category_scores as jsonb
            statement.setDouble(5, sensitivityScore);
            statement.setString(6, response.getModel());
            statement.setBoolean(7, response.getResults().get(0).isFlagged());

            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getInt("history_id");
            } else {
                throw new SQLException("Error inserting moderation result.");
            }

        } catch (SQLException | IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to save moderation result.");
        }
    }


    public ModerationCategories getCategoriesById(int id) {
        String query = "SELECT categories FROM histories WHERE history_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return new ObjectMapper().readValue(rs.getString("categories"), ModerationCategories.class);
            } else {
                throw new SQLException("No record found with ID: " + id);
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to retrieve categories.");
        }
    }

    public JsonNode getCategoryScoresById(int id) {
        String query = "SELECT category_scores FROM histories WHERE history_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return new ObjectMapper().readTree(rs.getString("category_scores"));
            } else {
                throw new SQLException("No record found with ID: " + id);
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to retrieve category scores.");
        }
    }

    public double getSensitivityScoreById(int id) {
        String query = "SELECT sensitivity_score FROM histories WHERE history_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getDouble("sensitivity_score");
            } else {
                throw new SQLException("No record found with ID: " + id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to retrieve sensitivity score.");
        }
    }

    public List<Map<String, Object>> getAllHistoriesByUserEmail(String email) {
        List<Map<String, Object>> histories = new ArrayList<>();

        Integer userId = userService.getUserIdByEmail(email);
        if (userId == null) {
            throw new IllegalArgumentException("User with this email does not exist.");
        }

        String query = "SELECT history_id, text FROM histories WHERE user_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, userId);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                Map<String, Object> history = new HashMap<>();
                history.put("history_id", rs.getInt("history_id"));
                history.put("text", rs.getString("text"));
                histories.add(history);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to retrieve histories.");
        }

        return histories;
    }
}
