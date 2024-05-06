package models.user;

public class Participation {
    private int id;
    private int eventId;
    private int userId;
    public Participation(int id, int eventId, int userId) {
        this.id = id;
        this.eventId = eventId;
        this.userId = userId;
    }
    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

}
