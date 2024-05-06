package services;

import Utils.MyDataBase;
import models.user.Ticket;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TicketService {
    private Connection connection;
    public TicketService() {
        connection = MyDataBase.getInstance().getConnection();
    }
    public void create(Ticket ticket) throws SQLException {
        String sql = "INSERT INTO ticket(type,quantite_dispo,date_creation,event_id,user_id) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, ticket.getType());
        preparedStatement.setInt(2, ticket.getQuantite());
        preparedStatement.setDate(3, java.sql.Date.valueOf(ticket.getDate()));
        preparedStatement.setInt(4, ticket.getIdevent());
        preparedStatement.setInt(5, ticket.getIduser());

        preparedStatement.executeUpdate();
        preparedStatement.close();
    }
    public void update(Ticket ticket) throws SQLException {
        String sql = "UPDATE ticket SET type = ?, quantite_dispo = ?, date_creation= ? event_id = ?, user_id = ? WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, ticket.getType());
        preparedStatement.setInt(2, ticket.getQuantite());
        preparedStatement.setDate(3, java.sql.Date.valueOf(ticket.getDate()));
        preparedStatement.setInt(4, ticket.getIdevent());
        preparedStatement.setInt(5, ticket.getIduser());
        preparedStatement.setInt(6, ticket.getIdticket());

        preparedStatement.executeUpdate();
        preparedStatement.close();
    }
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM ticket WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }
    public List<Ticket> read() throws SQLException {
        String sql = "SELECT * FROM ticket";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        List<Ticket> tickets = new ArrayList<>();
        while (rs.next()) {
            Ticket ticket = new Ticket();
            ticket.setIdticket(rs.getInt("id"));
            ticket.setType(rs.getString("type"));
            ticket.setQuantite(rs.getInt("quantite_dispo"));
            ticket.setDate(rs.getString("date_creation"));
            ticket.setIdevent(rs.getInt("event_id"));
            ticket.setIduser(rs.getInt("user_id"));
            tickets.add(ticket);
        }
        rs.close();
        statement.close();
        return tickets;
    }
}
