package services;

import entities.Rendezvous;
import utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceRendezvous implements IService<Rendezvous> {
    private Connection connection;

    public ServiceRendezvous() {
        this.connection = MyDatabase.getInstance().getConnection();
    }

    @Override
    public void ajouter(Rendezvous rendezvous) throws SQLException {
        String query = "INSERT INTO rendezvous (date, heure, name, email, bloodType,centredecollect_id) VALUES (?, ?, ?, ?, ?,?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, rendezvous.getDate());
            statement.setString(2, rendezvous.getHeure());
            statement.setString(3, rendezvous.getName());
            statement.setString(4, rendezvous.getEmail());
            statement.setString(5, rendezvous.getBloodType());
            statement.setInt(6, rendezvous.getCentredecollectId());
            statement.executeUpdate();
        }
    }

    @Override
    public void modifier(Rendezvous rendezvous) throws SQLException {
        String query = "UPDATE rendezvous SET date=?, heure=?, name=?, email=?, bloodType=? WHERE id=?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, rendezvous.getDate());
            statement.setString(2, rendezvous.getHeure());
            statement.setString(3, rendezvous.getName());
            statement.setString(4, rendezvous.getEmail());
            statement.setString(5, rendezvous.getBloodType());
            statement.setInt(6, rendezvous.getId());
            statement.executeUpdate();
        }
    }

    @Override
    public void supprimer(int id) throws SQLException {
        String query = "DELETE FROM rendezvous WHERE id=?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }

    @Override
    public List<Rendezvous> afficher() throws SQLException {
        List<Rendezvous> rendezvousList = new ArrayList<>();
        String query = "SELECT R.*, C.name AS centredecollect_nom\n" +
                "FROM rendezvous R\n" +
                "INNER JOIN centredecollect C ON R.centredecollect_id = C.id";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String date = resultSet.getString("date");
                    String heure = resultSet.getString("heure");
                    String name = resultSet.getString("name");
                    String email = resultSet.getString("email");
                    String bloodType = resultSet.getString("bloodType");
                    String centreName = resultSet.getString("centredecollect_nom");
                    Rendezvous rendezvous = new Rendezvous(id, date, heure, name, email, bloodType);
                    rendezvous.setCentredecollectName(centreName);
                    rendezvousList.add(rendezvous);
                }
            }
        }
        return rendezvousList;
    }
}
