package tn.esprit.models;
import java.time.LocalDate;

public class BloodStock {
    private int id;
    private String name;
    private float quantityAvailable;
    private String bloodType;
    private LocalDate expiryDate;

    public BloodStock() {
    }

    public BloodStock(int id, String name, float quantityAvailable, String bloodType, LocalDate expiryDate) {
        this.id = id;
        this.name = name;
        this.quantityAvailable = quantityAvailable;
        this.bloodType = bloodType;
        this.expiryDate = expiryDate;
    }

    public BloodStock(String name, float quantityAvailable, String bloodType, LocalDate expiryDate) {
        this.name = name;
        this.quantityAvailable = quantityAvailable;
        this.bloodType = bloodType;
        this.expiryDate = expiryDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getQuantityAvailable() {
        return quantityAvailable;
    }

    public void setQuantityAvailable(float quantityAvailable) {
        this.quantityAvailable = quantityAvailable;
    }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    @Override
    public String toString() {
        return "BloodStock{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", quantityAvailable=" + quantityAvailable +
                ", bloodType='" + bloodType + '\'' +
                ", expiryDate=" + expiryDate +
                '}';
    }
}
