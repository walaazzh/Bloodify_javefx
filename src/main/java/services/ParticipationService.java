package services;


import Utils.MyDataBase;
import models.user.Participation;

import java.sql.*;

public class ParticipationService {
    private Connection connection;
    public ParticipationService() {
        connection = MyDataBase.getInstance().getConnection();
    }

    public boolean hasParticipated(int userId, int eventId) throws SQLException {
        // Check if the user has participated in the given event
        String query = "SELECT * FROM participation WHERE user_id=? AND event_id=?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            statement.setInt(2, eventId);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public void saveParticipation(Participation participation) throws SQLException {
        String query = "INSERT INTO participation (event_id, user_id) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, participation.getEventId());
            statement.setInt(2, participation.getUserId());
            statement.executeUpdate();
        }
    }
}
