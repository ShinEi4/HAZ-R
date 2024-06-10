package crevettes;
import java.util.Date;

import javax.management.Query;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import util.Connexion;
public class Equipement {
    private double prixUnitaire;
    private int idEquipement;
    private String nom;


    // Constructor
    public Equipement(int idEquipement, String nom, double prixUnitaire) {
        this.idEquipement = idEquipement;
        this.nom = nom;
        this.prixUnitaire = prixUnitaire;
    }
    public Equipement(){}

    // Method to get stock equipment
    //ok
    public Object[][] getStockEquipement(String givenDate) throws Exception {
        // Create an ArrayList to store equipement data
        System.out.println("hello!");
        ArrayList<Object[]> equipementData = new ArrayList<>();

        try (Connection con = Connexion.connectePostgres()) {
            // Check if the givenDate is null
            String query;
            if (givenDate != null && !givenDate.isEmpty()) {
                query = "CREATE OR REPLACE VIEW StockEquipementVueDate AS " +
                        "SELECT e.idEquipement, e.Nom, " +
                        "COALESCE(SUM(CASE WHEN s.action = 1 THEN s.quantite ELSE -s.quantite END), 0) AS quantite " +
                        "FROM Equipement e " +
                        "LEFT JOIN StockEquipement s ON e.idEquipement = s.equipement " +
                        "WHERE s.date <= '" + givenDate + "' " +
                        "GROUP BY e.idEquipement, e.Nom ORDER BY quantite DESC;";
            } else {
                query = "CREATE OR REPLACE VIEW StockEquipementVueDate AS " +
                        "SELECT e.idEquipement, e.Nom, " +
                        "COALESCE(SUM(CASE WHEN s.action = 1 THEN s.quantite ELSE -s.quantite END), 0) AS quantite " +
                        "FROM Equipement e " +
                        "LEFT JOIN StockEquipement s ON e.idEquipement = s.equipement " +
                        "GROUP BY e.idEquipement, e.Nom ORDER BY quantite DESC;";
            }

            System.out.println(query);
            System.out.println(givenDate);

            // Create the view
            try (PreparedStatement pstmt = con.prepareStatement(query)) {
                pstmt.executeUpdate();
            }

            // Select data from the view
            query = "SELECT * FROM StockEquipementVueDate";
            try (PreparedStatement pstmt = con.prepareStatement(query);
                 ResultSet rs = pstmt.executeQuery()) {

                // Iterate through the result set and add each row to the equipementData ArrayList
                while (rs.next()) {
                    Object[] rowData = new Object[2];
                    rowData[0] = rs.getString("Nom");
                    rowData[1] = rs.getInt("quantite");
                    equipementData.add(rowData);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Convert the ArrayList to a 2D array
        Object[][] data = new Object[equipementData.size()][2];
        for (int i = 0; i < equipementData.size(); i++) {
            data[i] = equipementData.get(i);
        }

        return data;
    }
    
    // Method to insert an Equipement
    //ok
    public void insertEquipement() throws Exception {
        String query = "INSERT INTO Equipement (Nom, prixUnitaire) VALUES (?, ?)";

        try (Connection con = Connexion.connectePostgres();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, this.getNom());
            pstmt.setDouble(2, this.getPrixUnitaire());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    // Method to update data in Equipement table based on idEquipement
    //efa updatena ilay object
    //ok
    public void update() throws Exception{
        // SQL query to update data in Equipement table
        String query = "UPDATE Equipement SET Nom = ?, prixUnitaire = ? WHERE idEquipement = ?";

        try (Connection con =Connexion.connectePostgres();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            // Set parameters for the prepared statement
            pstmt.setString(1, this.getNom());
            pstmt.setDouble(2, this.getPrixUnitaire());
            pstmt.setInt(3, this.getIdEquipement());

            // Execute the update query
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(); // Handle any SQL exceptions
        }
    }
    
    public void getById(int idEquipement) throws Exception {
        // SQL query to select data from Equipement table based on idEquipement
        String query = "SELECT * FROM Equipement WHERE idEquipement = ?";

        try (Connection con = Connexion.connectePostgres();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            // Set parameter for the prepared statement
            pstmt.setInt(1, idEquipement);

            // Execute the query
            try (ResultSet rs = pstmt.executeQuery()) {
                // If the result set has at least one row, retrieve the data and set the values in the existing Equipement object
                if (rs.next()) {
                    // Retrieve data from the result set
                    String nom = rs.getString("Nom");
                    double prixUnitaire = rs.getDouble("prixUnitaire");
                    
                    // Set values in the existing Equipement object
                    this.idEquipement = idEquipement;
                    this.nom = nom;
                    this.prixUnitaire = prixUnitaire;
                } else {
                    throw new SQLException("No record found with idEquipement = " + idEquipement);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle any SQL exceptions
            throw e; // Rethrow the exception for further handling if necessary
        }
    }

    // Method to delete an equipement from the database based on idEquipement
    //ok
    public void delete() throws Exception {
        // SQL query to delete an equipement from the database based on idEquipement
        String query = "DELETE FROM Equipement WHERE idEquipement = ?";

        try (Connection con =Connexion.connectePostgres();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            // Set parameter for the prepared statement
            pstmt.setInt(1, this.getIdEquipement());

            // Execute the delete query
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(); // Handle any SQL exceptions
        }
    }

    //ok
    public void save(int equipementId, int quantite, String dateString, int actionId) throws Exception {
        // Parse the input dateString to java.sql.Date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date parsedDate;
        try {
            parsedDate = sdf.parse(dateString);
        } catch (ParseException e) {
            throw new Exception("Invalid date format. Please use the format yyyy-MM-dd");
        }
        java.sql.Date sqlDate = new java.sql.Date(parsedDate.getTime());

        // SQL query to insert data into StockEquipement table
        String query = "INSERT INTO StockEquipement (equipement, quantite, date, action) VALUES (?, ?, ?, ?)";
        try (Connection con = Connexion.connectePostgres();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            // Set parameters for the prepared statement
            pstmt.setInt(1, equipementId);
            pstmt.setInt(2, quantite);
            pstmt.setDate(3, sqlDate);
            pstmt.setInt(4, actionId);

            // Execute the insert query
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(); // Handle any SQL exceptions
        }
    }
    
    
    // Getter for idEquipement
    public int getIdEquipement() {
        return idEquipement;
    }

    // Setter for idEquipement
    public void setIdEquipement(int idEquipement) {
        this.idEquipement = idEquipement;
    }

    // Getter for nom
    public String getNom() {
        return nom;
    }

    // Setter for nom
    public void setNom(String nom) {
        this.nom = nom;
    }

    public double getPrixUnitaire() {
        return prixUnitaire;
    }

    public void setPrixUnitaire(double prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }

    // Override toString() method
    @Override
    public String toString() {
        return "Equipement ID: " + idEquipement + ", Name: " + nom + ", Price per Unit: " + prixUnitaire;
    }
}
