package crevettes;

import java.sql.Connection; 
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Statement;
import util.Connexion;

public class Etat {
    private int idEtat;
    private String nom;
    private double pourcentage;

    // Getters and Setters
    public int getIdEtat() {
        return idEtat;
    }

    public void setIdEtat(int idEtat) {
        this.idEtat = idEtat;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public double getPourcentage() {
        return pourcentage;
    }

    public void setPourcentage(double pourcentage) {
        this.pourcentage = pourcentage;
    }

    // Method to get all Etat entries
    public static List<Etat> getAll() throws Exception {
        List<Etat> etats = new ArrayList<>();

        String query = "SELECT * FROM Etat";

        try (Connection con = Connexion.connectePostgres();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Etat etat = new Etat();
                etat.setIdEtat(rs.getInt("idEtat"));
                etat.setNom(rs.getString("Nom"));
                etat.setPourcentage(rs.getDouble("pourcentage"));
                etats.add(etat);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return etats;
    }

    // Method to get a specific Etat by its ID
    public void getById(int id) throws Exception {
        String query = "SELECT * FROM Etat WHERE idEtat = ?";

        try (Connection con = Connexion.connectePostgres();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    this.setIdEtat(rs.getInt("idEtat"));
                    this.setNom(rs.getString("Nom"));
                    this.setPourcentage(rs.getDouble("pourcentage"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to save a new Etat entry
    public void save() throws Exception {
        String query = "INSERT INTO Etat (Nom, pourcentage) VALUES (?, ?)";

        try (Connection con = Connexion.connectePostgres();
             PreparedStatement pstmt = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, this.nom);
            pstmt.setDouble(2, this.pourcentage);
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    this.setIdEtat(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to update an existing Etat entry
    public void update() throws Exception {
        String query = "UPDATE Etat SET Nom = ?, pourcentage = ? WHERE idEtat = ?";

        try (Connection con = Connexion.connectePostgres();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, this.nom);
            pstmt.setDouble(2, this.pourcentage);
            pstmt.setInt(3, this.idEtat);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to delete an Etat entry
    public void delete() throws Exception {
        String query = "DELETE FROM Etat WHERE idEtat = ?";

        try (Connection con = Connexion.connectePostgres();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setInt(1, this.idEtat);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
