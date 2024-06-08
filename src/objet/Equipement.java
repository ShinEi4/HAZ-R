package objet;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Vector;

import compte.Achat;
import util.Connexion;

public class Equipement
{
    int id;
    String nom;
    double prixUnitaire;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getNom() {
        return nom;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }
    public double getPrixUnitaire() {
        return prixUnitaire;
    }
    public void setPrixUnitaire(double prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }

    public Equipement() {
    }

    public Equipement(int id, String nom, double prixUnitaire) {
        this.id = id;
        this.nom = nom;
        this.prixUnitaire = prixUnitaire;
    }

    public void save()
    {
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = Connexion.connectPostgres();
            String query;
    
            query = "insert into equipement (nom,prixunitaire) values (?,?)";
            pstmt=conn.prepareStatement(query);
            pstmt.setString(1, this.getNom());
            pstmt.setDouble(2,this.getPrixUnitaire());

            int v=pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void update(int id)
    {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = Connexion.connectPostgres();
            String query;
    
            query = "update equipement set nom =? ,prixunitaire=? where idequipement=?";
            pstmt=conn.prepareStatement(query);
            
            pstmt.setString(1, this.getNom());
            pstmt.setDouble(2,this.getPrixUnitaire());
            pstmt.setInt(3, id);

            int v=pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static Equipement getById(int id)
    {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Equipement equip=null;
        try {
            conn = Connexion.connectPostgres();
            String query;
    
            query = "SELECT * FROM equipement where idequipement =?";
            pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, id);
        
            rs = pstmt.executeQuery();
    
            if (rs.next()) {
                String nom=rs.getString("nom");
                double pu=rs.getDouble("prixunitaire");
                equip=new Equipement(id, nom, pu);
            }
    
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return equip;
    }
    
    public static Vector<Equipement> getAll()
    {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Vector<Equipement> ls=new Vector<Equipement>();

        try {
            conn = Connexion.connectPostgres();
            String query;
    
            query = "SELECT * FROM equipement";
            pstmt = conn.prepareStatement(query);
        
            rs = pstmt.executeQuery();
    
            while (rs.next()) {
                int id=rs.getInt("idequipement");
                String nom=rs.getString("nom");
                double pu=rs.getDouble("prixunitaire");
                Equipement equip=new Equipement(id, nom, pu);
                ls.add(equip);
            }
    
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return ls;
    }

}