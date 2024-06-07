package personne;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import util.*;

public class Admin {
    int id;
    String nom;
    String motdepasse;

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
    public String getMotdepasse() {
        return motdepasse;
    }
    public void setMotdepasse(String m) {
        this.motdepasse=HashUtil.hashString(m);
    }

    public Admin() {
    }

    public Admin(int id, String nom, String m) {
        this.id = id;
        this.nom = nom;
        this.motdepasse =HashUtil.hashString(m);
    }
    public Admin(int id, String mdp) { //eviter hash
        this.id = id;
        this.motdepasse = mdp;
    }

    // mila fonction manao hash

    public void save()
    {
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = Connexion.connectPostgres();
            String query;
    
            query = "insert into admin (pseudo,motdepasse) values (?,?)";
            pstmt=conn.prepareStatement(query);
            pstmt.setString(1, this.getNom());
            pstmt.setString(2,this.getMotdepasse());

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
    
            query = "update admin set pseudo =? ,motdepasse=? where id=?";
            pstmt=conn.prepareStatement(query);
            
            pstmt.setString(1, this.getNom());
            pstmt.setString(2,this.getMotdepasse());
            pstmt.setInt(3, id);

            int v=pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static Admin getById(int id)
    {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Admin user=null;
        try {
            conn = Connexion.connectPostgres();
            String query;
    
            query = "SELECT * FROM admin where id =?";
            pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, id);
        
            rs = pstmt.executeQuery();
    
            if (rs.next()) {
                String nom=rs.getString("pseudo");
                String mdp =rs.getString("motdepasse");
                user=new Admin(id,mdp);
                user.setNom(nom);
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
        return user;
    }
    
    public static Vector<Admin> getAll()
    {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Vector<Admin> ls=new Vector<Admin>();

        try {
            conn = Connexion.connectPostgres();
            String query;
    
            query = "SELECT * FROM admin";
            pstmt = conn.prepareStatement(query);
        
            rs = pstmt.executeQuery();
    
            while (rs.next()) {
                int id=rs.getInt("id");
                String nom=rs.getString("pseudo");
                String mdp =rs.getString("motdepasse");
                Admin user=new Admin(id,mdp);
                user.setNom(nom);
                ls.add(user);
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
