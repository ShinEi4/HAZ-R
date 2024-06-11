package crevettes;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Vector;

import objet.Equipement;
import personne.Admin;
import util.Connexion;

public class Achat {
    int id;
    Admin user;
    Equipement equipement;
    double qte;
    LocalDate date;
    
    public Achat() {
    }
    
    public Achat(int id,Admin u, Equipement equipement, double qte, LocalDate date) {
        this.id = id;
        this.equipement = equipement;
        this.qte = qte;
        this.date = date;
        this.user=u;
    }
    
    public Achat(Admin u, Equipement equipement, double qte, LocalDate date) {
        this.equipement = equipement;
        this.qte = qte;
        this.date = date;
        this.user=u;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public Equipement getEquipement() {
        return equipement;
    }
    public void setEquipement(Equipement equipement) {
        this.equipement = equipement;
    }
    public double getQte() {
        return qte;
    }
    public void setQte(double qte) {
        this.qte = qte;
    }
    public LocalDate getDate() {
        return date;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }
    public Admin getUser() {
        return user;
    }

    public void setUser(Admin user) {
        this.user = user;
    }
 
    //fonctions crud
    public void save(Connection co)throws Exception
    {
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = co;
            String query;
    
            query = "insert into achatequipement (idequipement,idadmin,quantite,date) values (?,?,?,?)";
            pstmt=conn.prepareStatement(query);
            java.sql.Date datesql=java.sql.Date.valueOf(this.getDate());
            pstmt.setInt(1, this.getEquipement().getIdEquipement());//edited
            pstmt.setInt(2,this.getUser().getId());
            pstmt.setDouble(3, this.getQte());
            pstmt.setDate(4, datesql);

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
    
            query = "update achatequipement set idequipement=? ,idadmin=?,quantite =? ,date=? where idachatequipement=?";
            pstmt=conn.prepareStatement(query);
            java.sql.Date datesql=java.sql.Date.valueOf(this.getDate());
            pstmt.setInt(1, this.getEquipement().getIdEquipement());//edited
            pstmt.setInt(2, this.getUser().getId());
            pstmt.setDouble(3, this.getQte());
            pstmt.setDate(4, datesql);
            pstmt.setInt(5, id);

            int v=pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static Achat getById(int id)
    {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Achat a=null;
        try {
            conn = Connexion.connectePostgres();
            String query;
    
            query = "SELECT * FROM AchatEquipement where idachatequipement=?";
            pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, id);
        
            rs = pstmt.executeQuery();
    
            if (rs.next()) {
                int idu=rs.getInt("idadmin");
                Admin user=Admin.getById(idu);
                int id2=rs.getInt("idAchatEquipement");
                int id_equip = rs.getInt("idEquipement");
                Equipement equip =new Equipement();
                equip=Equipement.getById(id_equip);
                java.sql.Date dateSql=rs.getDate("date");
                LocalDate date=dateSql.toLocalDate();
                double quantite =rs.getDouble("quantite");
                a = new Achat(id2,user,equip,quantite,date);
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
    
    public static Vector<Achat> getAll()
    {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Vector<Achat> ls=new Vector<Achat>();
        try {
            conn = Connexion.connectePostgres();
            String query;
    
            query = "SELECT * FROM AchatEquipement";
            pstmt = conn.prepareStatement(query);
        
            rs = pstmt.executeQuery();
    
            while (rs.next()) {
                int idu=rs.getInt("idadmin");
                Admin user=Admin.getById(idu);
                int id=rs.getInt("idAchatEquipement");
                int id_equip = rs.getInt("idEquipement");
                Equipement equip = new Equipement();
                equip=Equipement.getById(id_equip);
                java.sql.Date dateSql=rs.getDate("date");
                LocalDate date=dateSql.toLocalDate();
                double quantite =rs.getDouble("quantite");
                Achat a = new Achat(id,user,equip,quantite,date);
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

    public static Vector<Achat> getAllBetweenDates(String debutStr,String finStr)
    {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Vector<Achat> ls=new Vector<Achat>();

        DateTimeFormatter formatter=DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate debutDate = LocalDate.parse(debutStr,formatter);
        LocalDate finDate = LocalDate.parse(finStr, formatter);
        
         try {
            conn = Connexion.connectePostgres();
            String query;
    
            query = "SELECT * FROM AchatEquipement WHERE date BETWEEN ? AND ?";
            pstmt = conn.prepareStatement(query);
            pstmt.setDate(1, java.sql.Date.valueOf(debutDate));
            pstmt.setDate(2, java.sql.Date.valueOf(finDate));
        
            rs = pstmt.executeQuery();
    
            while (rs.next()) {
                int idu=rs.getInt("idadmin");
                Admin user=Admin.getById(idu);
                int id=rs.getInt("idAchatEquipement");
                int id_equip = rs.getInt("idEquipement");
                Equipement equip = new Equipement();
                equip=Equipement.getById(id_equip);
                java.sql.Date dateSql=rs.getDate("date");
                LocalDate date=dateSql.toLocalDate();
                double quantite =rs.getDouble("quantite");
                Achat a = new Achat(id,user,equip,quantite,date);
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
    public static Vector<Achat> getAllByDates(String debutStr, String finStr) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Vector<Achat> ls = new Vector<>();

        LocalDate debutDate = null;
        LocalDate finDate = null;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        if (debutStr != null && !debutStr.isEmpty()) {
            debutDate = LocalDate.parse(debutStr, formatter);
        }
        if (finStr != null && !finStr.isEmpty()) {
            finDate = LocalDate.parse(finStr, formatter);
        }

        try {
            conn = Connexion.connectePostgres();
            StringBuilder query = new StringBuilder("SELECT * FROM AchatEquipement WHERE 1=1");

            if (debutDate != null) {
                query.append(" AND date >= ?");
            }
            if (finDate != null) {
                query.append(" AND date <= ?");
            }

            pstmt = conn.prepareStatement(query.toString());

            int parameterIndex = 1;
            if (debutDate != null) {
                pstmt.setDate(parameterIndex++, Date.valueOf(debutDate));
            }
            if (finDate != null) {
                pstmt.setDate(parameterIndex++, Date.valueOf(finDate));
            }

            rs = pstmt.executeQuery();

            while (rs.next()) {
                int idu = rs.getInt("idadmin");
                Admin user = Admin.getById(idu);
                int id = rs.getInt("idAchatEquipement");
                int id_equip = rs.getInt("idEquipement");
                Equipement equip = new Equipement();
                equip=Equipement.getById(id_equip);
                java.sql.Date dateSql = rs.getDate("date");
                LocalDate date = dateSql.toLocalDate();
                double quantite = rs.getDouble("quantite");
                Achat a = new Achat(id, user, equip, quantite, date);
                ls.add(a);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return ls;
    }
    public static double getPrixTotal(Vector<Achat> ls) //afaka alaina direct avy any amin'ny base
    {
        double s=0;
        for (Achat achat : ls) {
            double pu=achat.getEquipement().getPrixUnitaire();
            pu*=achat.getQte();
            s+=pu;
        }
        return s;
    }
}
