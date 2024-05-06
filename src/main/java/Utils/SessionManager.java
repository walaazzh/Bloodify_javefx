package Utils;

import models.user.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import at.favre.lib.crypto.bcrypt.BCrypt;


public final class SessionManager {
    private static SessionManager instance;
    private static User currentUser;
    private static Connection connection; // Changed to static to make it accessible in static methods
    private static int userId;

    private SessionManager() {
        connection = MyDataBase.getInstance().getConnection();
    }

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        SessionManager.currentUser = currentUser;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        SessionManager.userId = userId;
    }
    public User auth(String email, String password) throws SQLException {
        Connection connection = MyDataBase.getInstance().getConnection();
        if (connection == null) {
            // Handle null connection
            throw new SQLException("Database connection is null");
        }

        String query = "SELECT * FROM user WHERE email=?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String hashedPasswordFromDatabase = resultSet.getString("password");

                    // Verify the password using bcrypt
                    boolean passwordMatches = BCrypt.verifyer().verify(password.toCharArray(), hashedPasswordFromDatabase).verified;

                    if (passwordMatches) {
                        User user = new User(
                                resultSet.getString("email"),
                                resultSet.getString("first_name"),
                                resultSet.getString("last_name"),
                                resultSet.getString("password"),
                                resultSet.getString("roles")
                        );
                        setUserId(resultSet.getInt("id"));
                        setCurrentUser(user); // Set the id attribute when user is authenticated
                        System.out.println("Login successful");
                        return user;
                    } else {
                        // Incorrect password
                        System.out.println("Incorrect password");
                        return null;
                    }
                }
            }
        } catch (SQLException e) {
            // Handle SQL exception properly
            e.printStackTrace();
            throw e; // Re-throw the exception for the caller to handle
        }

        // User not found
        System.out.println("User not found");
        return null;
    }
//    public User auth(String email, String password) throws SQLException {
//        connection = MyDataBase.getInstance().getConnection();
//        if (connection == null) {
//            // Handle null connection
//            throw new SQLException("Database connection is null");
//        }
//
//        String query = "SELECT * FROM user WHERE email=? AND password=?";
//        try (PreparedStatement statement = connection.prepareStatement(query)) {
//            statement.setString(1, email);
//            statement.setString(2, password);
//            try (ResultSet resultSet = statement.executeQuery()) {
//                if (resultSet.next()) {
//                    User user = new User(
//                            resultSet.getString("email"),
//                            resultSet.getString("first_name"),
//                            resultSet.getString("last_name"),
//                            resultSet.getString("password"),
//                            resultSet.getString("roles")
//                    );
//                    setUserId(resultSet.getInt("id"));
//                    setCurrentUser(user);// Set the id attribute when user is authenticated
//                    System.out.println("Login successful");
//                    return user;
//                }
//            }
//        } catch (SQLException e) {
//            // Handle SQL exception properly
//            e.printStackTrace();
//            throw e; // Re-throw the exception for the caller to handle
//        }
//        System.out.println("Login not successful");
//        return null;
//    }



    public void cleanUserSession() {
        currentUser = null;
        userId = 0; // Reset id attribute when user session is cleaned
    }

}
