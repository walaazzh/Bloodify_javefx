package models.user;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class Event {
    private int Idevent;
    private int Idcategory;
    private String name;
    private String description;
    private String datedebut;
    private String datefin;
    private String organisateur;
    private String contact;
    private String status;
    private int maxParticipant;
    private String image;
    private BooleanProperty selected;

    public Event() {
    }

    public Event(int Idcategory, String name, String description, String datedebut,
                 String datefin, String organisateur, String contact, String status, int maxParticipant,
                 String image) {

        this.Idcategory = Idcategory;
        this.name = name;
        this.description = description;
        this.datedebut = datedebut;
        this.datefin = datefin;
        this.organisateur = organisateur;
        this.contact = contact;
        this.status = status;
        this.maxParticipant = maxParticipant;
        this.image = image;
        this.selected = new SimpleBooleanProperty(false);
    }




    // Getters
    public int getIdevent() {
        return Idevent;
    }

    public int getIdcategory() {
        return Idcategory;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getDatedebut() {
        return datedebut;
    }

    public String getDatefin() {
        return datefin;
    }

    public String getOrganisateur() {
        return organisateur;
    }

    public String getContact() {
        return contact;
    }

    public String getStatus() {
        return status;
    }

    public int getMaxParticipant() {
        return maxParticipant;
    }

    public String getImage() {
        return image;
    }


    // Setters
    public void setIdevent(int Idevent) {
        this.Idevent = Idevent;
    }

    public void setIdcategory(int Idcategory) {
        this.Idcategory = Idcategory;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDatedebut(String datedebut) {
        this.datedebut = datedebut;
    }

    public void setDatefin(String datefin) {
        this.datefin = datefin;
    }

    public void setOrganisateur(String organisateur) {
        this.organisateur = organisateur;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setMaxParticipant(int maxParticipant) {
        this.maxParticipant = maxParticipant;
    }

    public void setImage(String image) {
        this.image = image;
    }


    // Getters and setters for selected property
    // Define the selected property
    private final BooleanProperty selectedProperty = new SimpleBooleanProperty(false);

    // Getter and setter for the selected property
    public boolean isSelected() {
        return selectedProperty.get();
    }

    public BooleanProperty selectedProperty() {
        return selectedProperty;
    }

    public void setSelected(boolean selected) {
        this.selectedProperty.set(selected);
    }

    public Event( int idevent,int Idcategory, String name, String description, String datedebut,
                  String datefin, String organisateur, String contact, String status, int maxParticipant,
                  String image) {
        this.Idevent=idevent;
        this.Idcategory = Idcategory;
        this.name = name;
        this.description = description;
        this.datedebut = datedebut;
        this.datefin = datefin;
        this.organisateur = organisateur;
        this.contact = contact;
        this.status = status;
        this.maxParticipant = maxParticipant;
        this.image = image;
        this.selected = new SimpleBooleanProperty(false);
    }
    @Override
    public String toString() {
        return "Event{" +
                "Idevent=" + Idevent +
                ", Idcategory=" + Idcategory +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", datedebut='" + datedebut + '\'' +
                ", datefin='" + datefin + '\'' +
                ", organisateur='" + organisateur + '\'' +
                ", contact='" + contact + '\'' +
                ", status='" + status + '\'' +
                ", maxParticipant=" + maxParticipant +
                ", image='" + image + '\'' +
                '}';
    }
}
