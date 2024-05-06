package models.user;

public class Ticket {

    private int Idticket;
    private int Idevent;
    private int Iduser;
    private String type;
    private String date;
    private int quantite ;
    // Constructors
    public Ticket() {
    }

    public Ticket( String type, int quantite, String date,int Idevent,int Iduser) {
        this.type = type;
        this.quantite = quantite;
        this.date = date;
        this.Iduser=Iduser;
        this.Idevent = Idevent;
    }

    // Getters
    public int getIdticket() {
        return Idticket;
    }

    public int getIdevent() {
        return Idevent;
    }
    public int getIduser() {
        return Iduser;
    }

    public String getType() {
        return type;
    }

    public String getDate() {
        return date;
    }

    public int getQuantite() {
        return quantite;
    }

    // Setters
    public void setIdticket(int Idticket) {
        this.Idticket = Idticket;
    }

    public void setIdevent(int Idevent) {
        this.Idevent = Idevent;
    }
    public void setIduser(int Iduser ) {
        this.Iduser = Iduser;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    // toString() method
    @Override
    public String toString() {
        return "Ticket{" +
                "Idticket=" + Idticket +
                ", Idevent=" + Idevent +
                ", Iduser=" + Iduser +
                ", type='" + type + '\'' +
                ", date='" + date + '\'' +
                ", quantite='" + quantite + '\'' +
                '}';
    }
}
