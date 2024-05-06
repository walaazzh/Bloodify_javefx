package services;

import Utils.MyDataBase;
import models.user.Event;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EventService {
    private Connection connection;
    private List<Event> events;
    CategoryService categoryService = new CategoryService();
    public EventService() {
        connection = MyDataBase.getInstance().getConnection();
    }

    public Event search(int id) throws SQLException {
        Event event = null;

        try {
            String sql = "SELECT * FROM event WHERE id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);

            ResultSet resultSet = ps.executeQuery();

            if (resultSet.next()) {
                event = new Event();
                event.setIdevent(resultSet.getInt("id"));
                event.setName(resultSet.getString("name"));
                event.setDescription(resultSet.getString("description"));
                event.setDatedebut(resultSet.getString("date_debut"));
                event.setDatefin(resultSet.getString("date_fin"));
                event.setOrganisateur(resultSet.getString("organisateur"));
                event.setContact(resultSet.getString("contact"));
                event.setMaxParticipant(resultSet.getInt("max_participant"));
                event.setStatus(resultSet.getString("statut"));
                event.setIdcategory(resultSet.getInt("event_category_id"));
                event.setImage(resultSet.getString("image_name"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw ex;
        }
        return event;
    }
    public void create (Event event) throws SQLException {
        String sql = "INSERT INTO event (name, description,date_debut,date_fin, organisateur, contact,max_participant ,statut,event_category_id ,image_name) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        preparedStatement.setString(1, event.getName());
        preparedStatement.setString(2, event.getDescription());
        preparedStatement.setString(3, event.getDatedebut());
        preparedStatement.setString(4, event.getDatefin());
        preparedStatement.setString(5, event.getOrganisateur());
        preparedStatement.setString(6, event.getContact());
        preparedStatement.setInt(7, event.getMaxParticipant());
        preparedStatement.setString(8, event.getStatus());
        preparedStatement.setInt(9, event.getIdcategory());
        preparedStatement.setString(10, event.getImage());

        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM event WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }


    public void update(Event event) throws SQLException {
        String sql = "UPDATE event SET name=?, description=?, date_debut=?, date_fin=?, organisateur=?, contact=?, max_participant=?, statut=?, event_category_id=?, image_name=? WHERE id=?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        preparedStatement.setString(1, event.getName());
        preparedStatement.setString(2, event.getDescription());
        preparedStatement.setString(3, event.getDatedebut());
        preparedStatement.setString(4, event.getDatefin());
        preparedStatement.setString(5, event.getOrganisateur());
        preparedStatement.setString(6, event.getContact());
        preparedStatement.setInt(7, event.getMaxParticipant());
        preparedStatement.setString(8, event.getStatus());
        preparedStatement.setInt(9, event.getIdcategory());
        preparedStatement.setString(10, event.getImage());
        preparedStatement.setInt(11, event.getIdevent());

        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    public List<Event> read() throws SQLException {
        String sql = "SELECT * FROM event";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        List<Event> events = new ArrayList<>();
        while (rs.next()) {
            Event event = new Event();
            event.setIdevent(rs.getInt("id"));
            event.setName(rs.getString("name"));
            event.setDescription(rs.getString("description"));
            event.setDatedebut(rs.getString("date_debut"));
            event.setDatefin(rs.getString("date_fin"));
            event.setOrganisateur(rs.getString("organisateur"));
            event.setContact(rs.getString("contact"));
            event.setMaxParticipant(rs.getInt("max_participant"));
            event.setStatus(rs.getString("statut"));
            event.setIdcategory(rs.getInt("event_category_id"));
            event.setImage(rs.getString("image_name"));
            events.add(event);
        }
        rs.close();
        statement.close();
        return events;
    }

    public List<Event> filterEvents(String categoryName, LocalDate datedebut, LocalDate datefin, String name, String organizer, String status, boolean matchAll) throws SQLException {
        // Construct the base SQL query to select events
        String sql = "SELECT * FROM event WHERE 1=1";

        // Add conditions to the SQL query based on the provided criteria
        if (categoryName != null && !categoryName.isEmpty()) {
            int categoryId = categoryService.getCategoryIdByName(categoryName);
            sql += " AND event_category_id = " + categoryId;
        }
        if (datedebut != null) {
            sql += " AND date_debut >= '" + datedebut + "'";
        }
        if (datefin != null) {
            sql += " AND date_fin <= '" + datefin + "'";
        }
        if (name != null && !name.isEmpty()) {
            sql += " AND name LIKE '%" + name + "%'";
        }
        if (organizer != null && !organizer.isEmpty()) {
            sql += " AND organisateur LIKE '%" + organizer + "%'";
        }
        if (status != null && !status.isEmpty()) {
            sql += " AND statut LIKE '%" + status + "%'";
        }

        return getEventsBySQLQuery(sql);
    }

    public List<Event> getEventsBySQLQuery(String sqlQuery) throws SQLException {
        List<Event> events = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sqlQuery)) {
            while (resultSet.next()) {
                Event event = new Event();
                event.setIdevent(resultSet.getInt("id"));
                event.setName(resultSet.getString("name"));
                event.setDescription(resultSet.getString("description"));
                event.setDatedebut(resultSet.getString("date_debut"));
                event.setDatefin(resultSet.getString("date_fin"));
                event.setOrganisateur(resultSet.getString("organisateur"));
                event.setContact(resultSet.getString("contact"));
                event.setMaxParticipant(resultSet.getInt("max_participant"));
                event.setStatus(resultSet.getString("statut"));
                event.setIdcategory(resultSet.getInt("event_category_id"));
                event.setImage(resultSet.getString("image_name"));
                events.add(event);
            }
        }
        return events;
    }

    public List<Event> getEventsByCategory(int categoryId) throws SQLException {
        List<Event> events = new ArrayList<>();

        try {
            String sql = "SELECT * FROM event WHERE event_category_id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, categoryId);

            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                Event event = new Event();
                event.setIdevent(resultSet.getInt("id"));
                event.setName(resultSet.getString("name"));
                event.setDescription(resultSet.getString("description"));
                event.setDatedebut(resultSet.getString("date_debut"));
                event.setDatefin(resultSet.getString("date_fin"));
                event.setOrganisateur(resultSet.getString("organisateur"));
                event.setContact(resultSet.getString("contact"));
                event.setMaxParticipant(resultSet.getInt("max_participant"));
                event.setStatus(resultSet.getString("statut"));
                event.setIdcategory(resultSet.getInt("event_category_id"));
                event.setImage(resultSet.getString("image_name"));

                events.add(event);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw ex;
        }

        return events;
    }

    public List<Event> filterev(List<Event> events, LocalDate startDate, LocalDate endDate, String organizer) {
        List<Event> filteredEvents = new ArrayList<>();

        for (Event event : events) {
            boolean matchesCriteria = true;

            // Check if the event matches each filter criterion
            if (startDate != null && !LocalDate.parse(event.getDatedebut()).isAfter(startDate)) {
                matchesCriteria = false;
            }
            if (endDate != null && !LocalDate.parse(event.getDatefin()).isBefore(endDate)) {
                matchesCriteria = false;
            }
            if (organizer != null && !event.getOrganisateur().toLowerCase().contains(organizer.toLowerCase())) {
                matchesCriteria = false;
            }

            // If the event matches any of the criteria, add it to the filtered list
            if (matchesCriteria) {
                filteredEvents.add(event);
            }
        }

        return filteredEvents;
    }

    public Event getEventById(int eventId) throws SQLException {
        Event event = null;

        try {
            String sql = "SELECT * FROM event WHERE id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, eventId);

            ResultSet resultSet = ps.executeQuery();

            if (resultSet.next()) {
                event = new Event();
                event.setIdevent(resultSet.getInt("id"));
                event.setName(resultSet.getString("name"));
                event.setDescription(resultSet.getString("description"));
                event.setDatedebut(resultSet.getString("date_debut"));
                event.setDatefin(resultSet.getString("date_fin"));
                event.setOrganisateur(resultSet.getString("organisateur"));
                event.setContact(resultSet.getString("contact"));
                event.setMaxParticipant(resultSet.getInt("max_participant"));
                event.setStatus(resultSet.getString("statut"));
                event.setIdcategory(resultSet.getInt("event_category_id"));
                event.setImage(resultSet.getString("image_name"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw ex;
        }
        return event;
    }



}
