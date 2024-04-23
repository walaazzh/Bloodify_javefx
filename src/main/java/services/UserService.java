package services;

import Utils.MyDataBase;
import models.user.User;



import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


public class UserService implements UserCRUD {
    private Connection connection;


    public void insertUserPst(User user) {
        String SQL = "INSERT INTO user(email, first_name, last_name, password, roles) VALUES(?, ?, ?, ?, ?)";

        try (Connection conn = MyDataBase.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setString(1, user.getEmail());
            pstmt.setString(2, user.getFirstName());
            pstmt.setString(3, user.getLastName());
            pstmt.setString(4, user.getPassword());
            pstmt.setString(5, user.getRoles());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("User inserted successfully.");
            }
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }
    public void updateUser(User user) {
        String SQL = "UPDATE user SET first_name = ?, last_name = ?, email = ? WHERE id = ?";

        try (Connection conn = MyDataBase.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setString(1, user.getFirstName());
            pstmt.setString(2, user.getLastName());
            pstmt.setString(3, user.getEmail());
            pstmt.setInt(4, user.getId()); // Assuming getId() returns the user's ID

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("User updated successfully.");

            }else
            { System.out.println("no User updated successfully.");}

        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }

    public void updateUser1(User user) {
        String SQL = "UPDATE user SET first_name = ?, last_name = ?, email = ? ,password = ? ,roles = ? WHERE id = ?";

        try (Connection conn = MyDataBase.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setString(1, user.getFirstName());
            pstmt.setString(2, user.getLastName());
            pstmt.setString(3, user.getEmail());
            pstmt.setString(4, user.getPassword()); // Assuming getId() returns the user's ID
            pstmt.setString(5, user.getRoles());
            pstmt.setInt(6, user.getId()); // Assuming getId() returns the user's ID


            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("User updated successfully.");
                System.out.println(user.getPassword()+user.getEmail()+user.getRoles()+user.getLastName()+user.getFirstName()+user.getId());


            }else
            {
                System.out.println("no User updated successfully.");
                System.out.println(user.getPassword()+user.getEmail()+user.getRoles()+user.getLastName());
            }


        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }

    public void updatePassword(int userId, String newPassword) {
        String SQL = "UPDATE user SET password = ? WHERE id = ?";

        try (Connection conn = MyDataBase.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setString(1, newPassword);
            pstmt.setInt(2, userId);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Password updated successfully.");
            } else {
                System.out.println("No password updated.");
            }
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }
    public boolean emailExists(String email) {
        String SQL = "SELECT COUNT(*) FROM user WHERE email = ?";
        try (Connection conn = MyDataBase.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setString(1, email);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Optionally, consider logging this exception or rethrowing it.
        }
        return false;
    }
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String SQL = "SELECT  first_name, last_name, email, password ,roles FROM user";

        try (Connection conn = MyDataBase.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String email = rs.getString("email");
                String password = rs.getString("password");
                String roles = rs.getString("roles");

                // Create a new User object and add it to the list
                users.add(new User(email, firstName, lastName, password,roles));
//                System.out.println("Email: " + email + ", Name: " + firstName + " " + lastName);
            }
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }

        return users;
    }

    public void deleteUser(int userId) {
        String SQL = "DELETE FROM user WHERE id = ?";

        try (Connection conn = MyDataBase.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setInt(1, userId);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("User deleted successfully.");
            } else {
                System.out.println("No user deleted.");
            }
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }

    public int getUserIdByEmail(String email) {
        String sql = "SELECT id FROM user WHERE email = ?";
        try (Connection conn = MyDataBase.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            try (ResultSet resultSet = pstmt.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("id");
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage());
        }
        return -1; // Return -1 if user is not found or in case of an error
    }
    public void deleteUserByEmail(String email) {
        String SQL = "DELETE FROM user WHERE email = ?";

        try (Connection conn = MyDataBase.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setString(1, email);

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("User deleted successfully.");
                System.out.println("this user is "+email);

            } else {
                System.out.println("No user found with the specified email.");
                System.out.println("this user is "+email);
            }
        } catch (SQLException ex) {
            System.err.println("Error occurred: " + ex.getMessage());
        }
    }

    public boolean addToken(String email, String token) {
        // SQL query to update the reset_token column for the given email
        String updateQuery = "UPDATE user SET reset_token = ? WHERE email = ?";

        try (Connection connection = MyDataBase.getInstance().getConnection();
             PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {

            // Set parameters for the update query
            updateStatement.setString(1, token);
            updateStatement.setString(2, email);

            // Execute the update query
            int rowsUpdated = updateStatement.executeUpdate();

            // Check if the update was successful
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // Return false if something went wrong
        return false;
    }


    public String findResetTokenByEmail(String email) {
        String resetToken = null;
        String query = "SELECT reset_token FROM user WHERE email = ?";

        try (Connection connection = MyDataBase.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            // Set the email parameter
            preparedStatement.setString(1, email);

            // Execute the query
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    resetToken = resultSet.getString("reset_token");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resetToken;
    }
    public void resetPassword(String email , String newPassword) {
        String SQL = "UPDATE user SET password = ? WHERE email = ?";

        try (Connection conn = MyDataBase.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setString(1, newPassword);
            pstmt.setString(2, email);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Password updated successfully.");
            } else {
                System.out.println("No password updated.");
            }
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }

    public void sendEmailAsync(String receiverMail, String code) {
        // Start a new thread to send the email
        new Thread(() -> {
            try {
                mail(receiverMail, code);
            } catch (Exception e) {
                e.printStackTrace();
                // Handle failure: show a notification to the user, log the error, etc.
            }
        }).start();
    }

    public void mail(String receiverMail, String code) {
        String host = "bloodifyesprit@gmail.com";
        final String user = "bloodifyesprit@gmail.com";
        final String password = "enszeyfgrzrtaazp";

        String to = receiverMail;

        Properties props = new Properties();
        props.put("mail.smtp.ssl.trust", "*");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(user, password);
                    }
                });

        String subject = "Password Reset Code";
        String content = "<h1>Password Reset Code:</h1><p>Your password reset code is: " + code + "</p>";

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(user));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(subject);
            message.setContent(content, "text/html");

            Transport.send(message);

            System.out.println("Message sent successfully via email.");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}