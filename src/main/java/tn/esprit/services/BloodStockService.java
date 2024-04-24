package tn.esprit.services;

import tn.esprit.interfaces.IStock;
import tn.esprit.models.BloodStock;
import tn.esprit.util.MaConnexion;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class BloodStockService implements IStock<BloodStock> {
    Connection cnx = MaConnexion.getInstance().getCnx();
    @Override
    public void add(BloodStock bloodStock) {
        String req ="INSERT INTO `bloodstock`(`name`, `quantityAvailable`, `bloodType`, `expiryDate`) VALUES ('"+bloodStock.getName()+"','"+bloodStock.getQuantityAvailable()+"','"+bloodStock.getBloodType()+"','"+bloodStock.getExpiryDate()+"')";
        try {
            Statement st = cnx.createStatement();
            st.executeUpdate(req);
            System.out.println("Stock added successfully");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(BloodStock bloodStock) {
        String req ="UPDATE `bloodstock` SET `name`='"+bloodStock.getName()+"',`quantityAvailable`='"+bloodStock.getQuantityAvailable()+"',`bloodType`='"+bloodStock.getBloodType()+"',`expiryDate`='"+bloodStock.getExpiryDate()+"' WHERE `id`='"+bloodStock.getId()+"'";
        try {
            Statement st = cnx.createStatement();
            st.executeUpdate(req);
            System.out.println("Stock updated successfully");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void delete(BloodStock bloodStock) {
        String req ="DELETE FROM `bloodstock` WHERE `id`='"+bloodStock.getId()+"'";
        try {
            Statement st = cnx.createStatement();
            st.executeUpdate(req);
            System.out.println("Stock deleted successfully");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public List<BloodStock> getAll() {
        List<BloodStock> bloodStocks = new ArrayList<>();
        String req ="SELECT * FROM BloodStock";
        try {
            Statement st = cnx.createStatement();
            ResultSet res = st.executeQuery(req);
            while (res.next()){
                BloodStock bloodStock =new BloodStock();
                bloodStock.setId(res.getInt("id"));
                bloodStock.setName(res.getString(2));
                bloodStock.setQuantityAvailable(res.getInt("quantityAvailable"));
                bloodStock.setBloodType(res.getString(4));
                bloodStock.setExpiryDate(res.getDate(5).toLocalDate());
                bloodStocks.add(bloodStock);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return bloodStocks;
    }

    @Override
    public BloodStock getOne(int id) {
        return null;
    }
}
