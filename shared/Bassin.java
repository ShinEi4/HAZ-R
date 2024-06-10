package crevettes;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;
import util.Connexion;
public class Bassin {
    private int idBassin;
    private String nom;

    public Bassin(){}

    public Bassin(int idBassin, String nom) {
        this.idBassin = idBassin;
        this.nom = nom;
    }

    // Getters and setters
    public int getIdBassin() {
        return idBassin;
    }

    public void setIdBassin(int idBassin) {
        this.idBassin = idBassin;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    // Method to retrieve all Bassins from the database
    //ok
    public static List<Bassin> getAll()  throws Exception{
        List<Bassin> bassins = new ArrayList<>();
    
        // SQL query to select all rows from Bassin table
        String query = "SELECT * FROM Bassin";
    
        try (Connection con = Connexion.connectePostgres();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
    
            // Iterate through the result set and create Bassin objects
            while (rs.next()) {
                Bassin bassin = new Bassin(); // Create a new instance for each row
                bassin.setIdBassin(rs.getInt("idBassin"));
                bassin.setNom(rs.getString("nom"));
                bassins.add(bassin);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle any SQL exceptions
        }
    
        return bassins;
    }
    //ok
    public void getById(int idBassin) throws Exception {
        // SQL query to select data for the given idBassin
        String query = "SELECT * FROM Bassin WHERE idBassin = ?";
        
        try (Connection con = Connexion.connectePostgres();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            
            // Set parameter for the prepared statement
            pstmt.setInt(1, idBassin);
            
            // Execute the query
            ResultSet rs = pstmt.executeQuery();
            
            // If data is found, set the attributes using setters
            if (rs.next()) {
                this.setIdBassin(rs.getInt("idBassin"));
                this.setNom(rs.getString("nom"));
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle any SQL exceptions
        }
    }
    
}