package services;

import Utils.MyDataBase;
import models.user.Category;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryService {
    private Connection connection;

    public CategoryService() {
        connection = MyDataBase.getInstance().getConnection();
    }

    public Category search(int id) throws SQLException {
        Category category = null;

        try {
            String sql = "SELECT * FROM event_category WHERE id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);

            ResultSet resultSet = ps.executeQuery();

            if (resultSet.next()) {
                category = new Category();
                category.setIdcategory(resultSet.getInt("id"));
                category.setName(resultSet.getString("name"));
                category.setDescription(resultSet.getString("description"));
            }

            // Close the ResultSet and PreparedStatement in a finally block to ensure proper resource management
        } catch (SQLException ex) {
            // Handle any SQL exceptions
            ex.printStackTrace(); // Log the exception or handle it appropriately
            throw ex; // Re-throw the exception to propagate it to the caller
        }
        return category;
    }

    public void create(Category category) throws SQLException {
        String sql = "INSERT INTO event_category (name, description) VALUES (?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, category.getName());
        preparedStatement.setString(2, category.getDescription());

        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    public void update(Category category) throws SQLException {
        String sql = "UPDATE event_category SET name = ?, description = ? WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, category.getName());
        preparedStatement.setString(2, category.getDescription());
        preparedStatement.setInt(3, category.getIdcategory());

        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM event_category WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    public List<Category> read() throws SQLException {
        String sql = "SELECT * FROM event_category";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        List<Category> categories = new ArrayList<>();
        while (rs.next()) {
            Category category = new Category();
            category.setIdcategory(rs.getInt("id"));
            category.setName(rs.getString("name"));
            category.setDescription(rs.getString("description"));
            categories.add(category);
        }
        rs.close();
        statement.close();
        return categories;
    }

    public int getCategoryIdByName(String categoryName) throws SQLException {
        String query = "SELECT id FROM event_category WHERE name = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, categoryName);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("id");
                } else {
                    throw new SQLException("Category not found with name: " + categoryName);
                }
            }
        }
    }

    public List<String> getAllCategoryNames() throws SQLException {
        List<String> categoryNames = new ArrayList<>();

        try {

            String query = "SELECT name FROM event_category";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String categoryName = resultSet.getString("name");
                categoryNames.add(categoryName);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {

            e.printStackTrace();
            throw e;
        }

        // Return the list of category names
        return categoryNames;
    }

    public String getCategoryName(int categoryId) throws SQLException {
        String query = "SELECT name FROM event_category WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, categoryId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("name");
                } else {
                    throw new SQLException("Category not found with ID: " + categoryId);
                }
            }
        }
    }

    public Category getCategoryWithMostEvents() throws SQLException {
        String sql = "SELECT event_category_id, COUNT(*) AS event_count FROM event GROUP BY event_category_id ORDER BY event_count DESC LIMIT 1";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            if (resultSet.next()) {
                int categoryId = resultSet.getInt("event_category_id");
                return search(categoryId);
            } else {
                throw new SQLException("No categories found.");
            }
        }
    }


    public Category getCategoryWithLeastEvents() throws SQLException {
        String sql = "SELECT event_category_id, COUNT(*) AS event_count FROM event GROUP BY event_category_id ORDER BY event_count ASC LIMIT 1";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            if (resultSet.next()) {
                int categoryId = resultSet.getInt("event_category_id");
                return search(categoryId);
            } else {
                throw new SQLException("No categories found.");
            }
        }
    }
}