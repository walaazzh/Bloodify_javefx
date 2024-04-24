package services;

import entities.centredecollect;
import utils.MyDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Servicecentrededecollect implements IService<centredecollect> {
    private Connection connection;

    public Servicecentrededecollect(){
        this.connection = MyDatabase.getInstance().getConnection();
    }



    @Override
    public void ajouter(centredecollect centreCollecte) throws SQLException {
        String query = "INSERT INTO centredecollect (codePostal, capaciteMax, name, ville) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, centreCollecte.getCodePostal());
            statement.setInt(2, centreCollecte.getCapaciteMax());
            statement.setString(3, centreCollecte.getName());
            statement.setString(4, centreCollecte.getVille());
            statement.executeUpdate();
        }
    }


    @Override
    public void modifier(centredecollect centreCollecte) throws SQLException {
        String query = "UPDATE centredecollect SET codePostal=?, capaciteMax=?, name=?, ville=? WHERE id=?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, centreCollecte.getCodePostal());
            statement.setInt(2, centreCollecte.getCapaciteMax());
            statement.setString(3, centreCollecte.getName());
            statement.setString(4, centreCollecte.getVille());
            statement.setInt(5, centreCollecte.getId());
            statement.executeUpdate();
        }
    }

    @Override
    public void supprimer(int id) throws SQLException {
        String query = "DELETE FROM centredecollect WHERE id=?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }





    @Override
    public List<centredecollect> afficher() throws SQLException {
        List<centredecollect> centresCollectesList = new ArrayList<>();
        String query = "SELECT * FROM centredecollect";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    int codePostal = resultSet.getInt("codePostal");
                    int capaciteMax = resultSet.getInt("capaciteMax");
                    String name = resultSet.getString("name");
                    String ville = resultSet.getString("ville");
                    centredecollect centreCollecte = new centredecollect(id, codePostal, capaciteMax, name, ville);
                    centresCollectesList.add(centreCollecte);
                }
            }
        }
        return centresCollectesList;
    }
}
