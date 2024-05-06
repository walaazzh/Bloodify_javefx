package services;


import models.user.User;

import java.sql.SQLException;
import java.util.List;

public interface UserCRUD {
    void insertUserPst(User user) throws SQLException;
    void updateUser(User user) throws SQLException;
    void updateUser1(User user) throws SQLException;
    void updatePassword(int userId, String newPassword) throws SQLException;
    boolean emailExists(String email);
    List<User> getAllUsers() throws SQLException;
    void deleteUser(int userId) throws SQLException;
    int getUserIdByEmail(String email) throws SQLException;
    void deleteUserByEmail(String email) throws SQLException;
    boolean addToken(String email, String token) throws SQLException;
    String findResetTokenByEmail(String email) throws SQLException;
    void resetPassword(String email, String newPassword) throws SQLException;
    void sendEmailAsync(String receiverMail, String code);
    void mail(String receiverMail, String code);
}
