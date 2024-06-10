package crevettes;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Vector;

import util.Connexion;
import personne.*;

public class Depense {
    int id;
    Admin user;
    double qte;
    double pu; // prix unitaire
    LocalDate date;
    String depense; // New field

    // Default constructor
    public Depense() {
    }

    // Parameterized constructor
    public Depense(int id, Admin user, double pu, double qte, LocalDate date, String depense) {
        this.id = id;
        this.user = user;
        this.pu = pu;
        this.qte = qte;
        this.date = date;
        this.depense = depense;
    }
    public Depense( Admin user, double pu, double qte, LocalDate date, String depense) {
        this.user = user;
        this.pu = pu;
        this.qte = qte;
        this.date = date;
        this.depense = depense;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Admin getUser() {
        return user;
    }

    public void setUser(Admin user) {
        this.user = user;
    }

    public double getQte() {
        return qte;
    }

    public void setQte(double qte) {
        this.qte = qte;
    }

    public double getPu() {
        return pu;
    }

    public void setPu(double pu) {
        this.pu = pu;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getDepense() {
        return depense;
    }

    public void setDepense(String depense) {
        this.depense = depense;
    }
 
    //fonctions crud
    public void save(Connection co)throws Exception
    {
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = co;
            String query;
    
            query = "insert into depense (idadmin,quantite,prixunitaire,date,depense) values (?,?,?,?,?)";
            pstmt=conn.prepareStatement(query);
            java.sql.Date datesql=java.sql.Date.valueOf(this.getDate());
            pstmt.setInt(1, this.getUser().getId());
            pstmt.setDouble(2, this.getQte());
            pstmt.setDouble(3, this.getPu());
            pstmt.setDate(4, datesql);
            pstmt.setString(5, this.getDepense());

            int v=pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    
    public void update(int id)
    {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = Connexion.connectePostgres();
            String query;
    
            query = "update depense set idadmin=?, quantite =? ,prixunitaire=?,date=?,depense=?  where iddepense=?";
            pstmt=conn.prepareStatement(query);
            java.sql.Date datesql=java.sql.Date.valueOf(this.getDate());
            pstmt.setInt(1, this.getUser().getId());
            pstmt.setDouble(1, this.getQte());
            pstmt.setDouble(2, this.getPu());
            pstmt.setDate(3, datesql);
            pstmt.setInt(4, id);
            pstmt.setString(5, this.getDepense());

            int v=pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static Depense getById(int id)
    {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Depense a=null;
        try {
            conn = Connexion.connectePostgres();
            String query;
    
            query = "SELECT * FROM depense where iddepense=?";
            pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, id);
        
            rs = pstmt.executeQuery();
    
            if (rs.next()) {
                int idu=rs.getInt("idadmin");
                Admin user=Admin.getById(idu);
                int id2=rs.getInt("iddepense");
                java.sql.Date dateSql=rs.getDate("date");
                LocalDate date=dateSql.toLocalDate();
                double quantite =rs.getDouble("quantite");
                double pu=rs.getDouble("prixunitaire");
                String dep=rs.getString("depense");
                a = new Depense(id2,user,pu,quantite,date,dep);
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
        return a;
    }
    
    public static Vector<Depense> getAll()
    {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Vector<Depense> ls=new Vector<Depense>();
        try {
            conn = Connexion.connectePostgres();
            String query;
    
            query = "SELECT * FROM depense";
            pstmt = conn.prepareStatement(query);
        
            rs = pstmt.executeQuery();
    
            while (rs.next()) {
                int idu=rs.getInt("idadmin");
                Admin user=Admin.getById(idu);
                int id2=rs.getInt("iddepense");
                java.sql.Date dateSql=rs.getDate("date");
                LocalDate date=dateSql.toLocalDate();
                double quantite =rs.getDouble("quantite");
                double pu=rs.getDouble("prixunitaire");
                String dep=rs.getString("depense");
                Depense a = new Depense(id2,user,pu,quantite,date,dep);
                ls.add(a);
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

    public static Vector<Depense> getAllBetweenDates(String debutStr,String finStr)
    {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Vector<Depense> ls=new Vector<Depense>();

        DateTimeFormatter formatter=DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate debutDate = LocalDate.parse(debutStr,formatter);
        LocalDate finDate = LocalDate.parse(finStr, formatter);
        
         try {
            conn = Connexion.connectePostgres();
            String query;
    
            query = "SELECT * FROM depense WHERE date BETWEEN ? AND ?";
            pstmt = conn.prepareStatement(query);
            pstmt.setDate(1, java.sql.Date.valueOf(debutDate));
            pstmt.setDate(2, java.sql.Date.valueOf(finDate));
        
            rs = pstmt.executeQuery();
    
            while (rs.next()) {
                int idu=rs.getInt("idadmin");
                Admin user=Admin.getById(idu);
                int id2=rs.getInt("iddepense");
                java.sql.Date dateSql=rs.getDate("date");
                LocalDate date=dateSql.toLocalDate();
                double quantite =rs.getDouble("quantite");
                double pu=rs.getDouble("prixunitaire");
                String dep=rs.getString("depense");
                Depense a = new Depense(id2,user,pu,quantite,date,dep);
                ls.add(a);
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
    
    public static double getPrixTotal(Vector<Depense> ls) //afaka alaina direct avy any amin'ny base
    {
        double s=0;
        for (Depense dep : ls) {
            double pu=dep.getPu();
            pu*=dep.getQte();
            s+=pu;
        }
        return s;
    }
}

