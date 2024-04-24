package tn.esprit.services;

import tn.esprit.interfaces.ITransaction;
import tn.esprit.models.BloodStock;
import tn.esprit.models.BloodTransaction;
import tn.esprit.util.MaConnexion;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class BloodTransactionService implements ITransaction<BloodTransaction> {

    Connection cnx = MaConnexion.getInstance().getCnx();

    @Override
    public void add(BloodTransaction bloodTransaction) {
        String req ="INSERT INTO `bloodtransaction`(`quantityDonated`, `transactionType`, `donationDate`) VALUES ('"+bloodTransaction.getQuantityDonated()+"','"+bloodTransaction.getTransactionType()+"','"+bloodTransaction.getDonationDate()+"')";
        try {
            Statement st = cnx.createStatement();
            st.executeUpdate(req);
            System.out.println("Transaction added successfully");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(BloodTransaction bloodTransaction) {
        String req ="UPDATE `bloodtransaction` SET `quantityDonated`='"+bloodTransaction.getQuantityDonated()+"',`transactionType`='"+bloodTransaction.getTransactionType()+"',`donationDate`='"+bloodTransaction.getDonationDate()+"' WHERE `id`='"+bloodTransaction.getId()+"'";
        try {
            Statement st = cnx.createStatement();
            st.executeUpdate(req);
            System.out.println("Transaction updated successfully");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void delete(BloodTransaction bloodTransaction) {
        String req ="DELETE FROM `bloodtransaction` WHERE `id`='"+bloodTransaction.getId()+"'";
        try {
            Statement st = cnx.createStatement();
            st.executeUpdate(req);
            System.out.println("Transaction deleted successfully");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public List<BloodTransaction> getAll() {
        List<BloodTransaction> bloodTransactions = new ArrayList<>();
        String req ="SELECT * FROM BloodTransaction";
        try {
            Statement st = cnx.createStatement();
            ResultSet res = st.executeQuery(req);
            while (res.next()){
                BloodTransaction bloodTransaction =new BloodTransaction();
                bloodTransaction.setId(res.getInt("id"));
                bloodTransaction.setQuantityDonated(res.getInt("quantityDonated"));
                bloodTransaction.setTransactionType(res.getString(3));
                bloodTransaction.setDonationDate(res.getDate(4).toLocalDate());
                bloodTransactions.add(bloodTransaction);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return bloodTransactions;
    }

    @Override
    public BloodTransaction getOne(int id) {
        return null;
    }
}
