package tn.esprit.models;

import java.time.LocalDate;

public class BloodTransaction {
    private int id;
    private float quantityDonated;
    private String transactionType;
    private LocalDate donationDate;

    public BloodTransaction() {
    }

    public BloodTransaction(int id, float quantityDonated, String transactionType, LocalDate donationDate) {
        this.id = id;
        this.quantityDonated = quantityDonated;
        this.transactionType = transactionType;
        this.donationDate = donationDate;
    }

    public BloodTransaction(float quantityDonated, String transactionType, LocalDate donationDate) {
        this.quantityDonated = quantityDonated;
        this.transactionType = transactionType;
        this.donationDate = donationDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getQuantityDonated() {
        return quantityDonated;
    }

    public void setQuantityDonated(float quantityDonated) {
        this.quantityDonated = quantityDonated;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public LocalDate getDonationDate() {
        return donationDate;
    }

    public void setDonationDate(LocalDate donationDate) {
        this.donationDate = donationDate;
    }

    @Override
    public String toString() {
        return "BloodTransaction{" +
                "id=" + id +
                ", quantityDonated=" + quantityDonated +
                ", transactionType='" + transactionType + '\'' +
                ", donationDate=" + donationDate +
                '}';
    }
}
