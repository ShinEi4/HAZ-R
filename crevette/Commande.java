package commande;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.sql.Date;
import connection.Connex;

public class Commande {
    private int idCommande;
    private int idUser;
    private int idBassin;
    private int poids;
    private Date date;
    private int type;
    private int idRegion;
    private int statue;
    private Date dateFin;

    public void setIdCommande(int idCommande) {
        this.idCommande = idCommande;
    }
    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }
    public void setPoids(int poids) {
        this.poids = poids;
    }
    public void setIdBassin(int idBassin) {
        this.idBassin = idBassin;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setIdRegion(int idRegion) {
        this.idRegion = idRegion;
    }

    public void setStatue(int statue) {
        this.statue = statue;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public Commande() {

    }

    public int getIdCommande() {
        return idCommande;
    }

    public int getIdBassin() {
        return idBassin;
    }

    public Date getDate() {
        return date;
    }

    public int getType() {
        return type;
    }

    public int getIdRegion() {
        return idRegion;
    }

    public int getStatue() {
        return statue;
    }

    public Date getDateFin() {
        return dateFin;
    }
    public int getIdUser()
    {
        return this.idUser;
    }
    public int getPoids()
    {
        return this.poids;
    }

    public String toString() {
        return "Commande{" +
                "idCommande=" + idCommande +
                ", idBassin=" + idBassin +
                ", idUser=" + idUser +
                ", poids=" + poids +
                ", date=" + date +
                ", type='" + type + '\'' +
                ", idRegion=" + idRegion +
                ", statue=" + statue +
                ", dateFin=" + dateFin +
                '}';
    }
    public Commande(int idCommande,int idUser ,int idBassin,int poids ,Date date, int type, int idRegion, int statue, Date dateFin) {
        this.idCommande = idCommande;
        this.idUser=idUser;
        this.idBassin = idBassin;
        this.poids=poids;
        this.date = date;
        this.type = type;
        this.idRegion = idRegion;
        this.statue = statue;
        this.dateFin = dateFin;
    }
    public static ArrayList<Commande> getAllCommandes() throws Exception {
        ArrayList<Commande> commandes = new ArrayList<>();
        String sql = "SELECT * FROM commande";
        Connection conn = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            conn = Connex.getConnection();
            statement = conn.createStatement();
            resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                int idCommande = resultSet.getInt("idCommande");
                int idUser = resultSet.getInt("idUser");
                int poids = resultSet.getInt("poids");
                int idBassin = resultSet.getInt("idBassin");
                Date date = resultSet.getDate("date");
                int type = resultSet.getInt("type");
                int idRegion = resultSet.getInt("idRegion");
                int statue = resultSet.getInt("statue");
                Date dateFin = resultSet.getDate("dateFin");
                commandes.add(new Commande(idCommande,idUser ,idBassin, poids,date, type, idRegion, statue, dateFin));
            }
        } catch (Exception e) {
            throw e;
        }

        finally {
            if (resultSet != null) {
                resultSet.close();
            }
            if (resultSet != null) {
                resultSet.close();
            }
            if (conn != null) {
                conn.close();
            }

        }

        return commandes;
    }

    public void save() throws Exception {
        String sql = "INSERT INTO commande (idBassin, date, type, idRegion, statue, dateFin,idUser,poids) VALUES (?, ?, ?, ?, ?, ?,?,?)";
        Connection conn = null;
        PreparedStatement prstm = null;
        try {
            conn = Connex.getConnection();
            prstm = conn.prepareStatement(sql);

            prstm.setInt(1, idBassin);
            prstm.setDate(2, date);
            prstm.setInt(3, type);
            prstm.setInt(4, idRegion);
            prstm.setInt(5, statue);
            prstm.setDate(6, dateFin);
            prstm.setInt(7,idUser);
            prstm.setInt(8,poids);
            

            prstm.executeUpdate();

        } catch (Exception e) {
            throw e;
        }

        finally {
            if (prstm != null) {
                prstm.close();
            }
            if (conn != null) {
                conn.close();
            }

        }

    }

    public void getById(int id) throws Exception {
        String sql = "SELECT * FROM commande WHERE idCommande = ?";
        Connection conn = null;
        PreparedStatement prstm = null;
        ResultSet resultSet = null;

        try {
            conn = Connex.getConnection();
            prstm = conn.prepareStatement(sql);
            prstm.setInt(1, id);
            resultSet = prstm.executeQuery();

            if (resultSet.next()) {
                this.idCommande = resultSet.getInt("idCommande");
                this.idUser=resultSet.getInt("idUser");
                this.idBassin = resultSet.getInt("idBassin");
                this.poids=resultSet.getInt("poids");
                this.date = resultSet.getDate("date");
                this.type = resultSet.getInt("type");
                this.idRegion = resultSet.getInt("idRegion");
                this.statue = resultSet.getInt("statue");
                this.dateFin = resultSet.getDate("dateFin");
                
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
            if (prstm != null) {
                prstm.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }

    public void mametrakaCommande(int statue) throws Exception {

        String sql = "Update  commande set dateFin=now() where idCommande=?";
        Connection conn = null;
        PreparedStatement prstm = null;

        try {
            this.getById(this.idCommande);
            this.setDateFin(null);
            this.setStatue(statue);
            conn = Connex.getConnection();
            prstm = conn.prepareStatement(sql);
            prstm.setInt(1, idCommande);
            prstm.executeUpdate();
            this.save();
            System.out.println("mety");

        } catch (Exception e) {
            throw e;
        }

    }

    // commande mbola tsy tapitra
    public static ArrayList<Commande> getAllCommandesParStatueExiste(int stat) throws Exception {
        ArrayList<Commande> commandes = new ArrayList<>();
        String sql = "SELECT * FROM commande where statue=? and dateFin is null";
        Connection conn = null;
        PreparedStatement prstm = null;
        ResultSet resultSet = null;

        try {
            
            conn = Connex.getConnection();
            prstm = conn.prepareStatement(sql);
            prstm.setInt(1, stat);
            resultSet = prstm.executeQuery();

            while (resultSet.next()) {
                int idCommande = resultSet.getInt("idCommande");
                int idUser = resultSet.getInt("idUser");
                int poids = resultSet.getInt("poids");
                int idBassin = resultSet.getInt("idBassin");
                Date date = resultSet.getDate("date");
                int type = resultSet.getInt("type");
                int idRegion = resultSet.getInt("idRegion");
                int statue = resultSet.getInt("statue");
                Date dateFin = resultSet.getDate("dateFin");
                commandes.add(new Commande(idCommande,idUser ,idBassin, poids,date, type, idRegion, statue, dateFin));
            }
        } catch (Exception e) {
            throw e;
        }

        finally {
            if (resultSet != null) {
                resultSet.close();
            }
            if (resultSet != null) {
                resultSet.close();
            }
            if (conn != null) {
                conn.close();
            }

        }

        return commandes;
    }

    public static ArrayList<Commande> getAllCommandesParDate(int stat, String dateDebut, String dateFin)
            throws Exception {
        ArrayList<Commande> commandes = new ArrayList<>();
        String sql = "SELECT * FROM commande WHERE statue=? AND date>=? AND date<=?";
        Connection conn = null;
        PreparedStatement prstm = null;
        ResultSet resultSet = null;

        try {
            conn = Connex.getConnection();
            prstm = conn.prepareStatement(sql);

            // Convert String to java.sql.Date
            Date sqlDateDebut = Date.valueOf(dateDebut);
            Date sqlDateFin = Date.valueOf(dateFin);

            prstm.setInt(1, stat);
            prstm.setDate(2, sqlDateDebut);
            prstm.setDate(3, sqlDateFin);
            resultSet = prstm.executeQuery();

            while (resultSet.next()) {
                int idCommande = resultSet.getInt("idCommande");
                int idUser = resultSet.getInt("idUser");
                int poids = resultSet.getInt("poids");
                int idBassin = resultSet.getInt("idBassin");
                Date date = resultSet.getDate("date");
                int type = resultSet.getInt("type");
                int idRegion = resultSet.getInt("idRegion");
                int statue = resultSet.getInt("statue");
                Date dateFarany = resultSet.getDate("dateFin");
                commandes.add(new Commande(idCommande,idUser ,idBassin, poids,date, type, idRegion, statue, dateFarany));
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (Exception e) {
                    // Log exception
                }
            }
            if (prstm != null) {
                try {
                    prstm.close();
                } catch (Exception e) {
                    // Log exception
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception e) {
                    // Log exception
                }
            }
        }

        return commandes;
    }

}
