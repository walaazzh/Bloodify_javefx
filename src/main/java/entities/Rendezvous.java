package entities;



import java.sql.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Rendezvous {
    private int id;
    private String date;
    private String heure;
    private String name;
    private String email;
    private String bloodType;
    private String centredecollectName;

    public int getCentredecollectId() {
        return centredecollectId;
    }

    public void setCentredecollectId(int centredecollectId) {
        this.centredecollectId = centredecollectId;
    }

    private int centredecollectId;

    public Rendezvous(String date, String heure, String name, String email, String bloodType,int centredecollectId) {
        this.date = date;
        this.heure = heure;
        this.name = name;
        this.email = email;
        this.bloodType = bloodType;
        this.centredecollectId = centredecollectId;
    }

    public String getCentredecollectName() {
        return centredecollectName;
    }

    public void setCentredecollectName(String centredecollectName) {
        this.centredecollectName = centredecollectName;
    }







    public boolean validateEmail(String email) {
        // Expression régulière pour vérifier le format de l'email
        String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHeure() {
        return heure;
    }

    public void setHeure(String heure) {
        this.heure = heure;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }
    public boolean validate() {
        if (date == null || heure == null || name == null || bloodType == null) {
            return false;
        }
        if (email != null && !validateEmail(email)) {
            return false;
        }
        // Ajoutez d'autres conditions de validation si nécessaire
        return true;
    }
    public Rendezvous(int id, long date, String heure, String name, String email, String bloodType) {
    }

    public Rendezvous(int id, String date, String heure, String name, String email, String bloodType) {
        this.id = id;
        this.date = date;
        this.heure = heure;
        this.name = name;
        this.email = email;
        this.bloodType = bloodType;
    }

    @Override
    public String toString() {
        return "Rendezvous{" +
                "id=" + id +
                ", date=" + date +
                ", heure=" + heure +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", bloodType='" + bloodType + '\'' +
                '}';
    }
}
