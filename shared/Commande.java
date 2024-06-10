package crevettes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.sql.Date;
import util.Connexion;
import java.text.SimpleDateFormat;
public class Commande {
    private int idCommande;
    private int idUser;
    private double poids;
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
    public void setPoids(double poids) {
        this.poids = poids;
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
    public double getPoids()
    {
        return this.poids;
    }

    public String toString() {
        return "Commande{" +
                "idCommande=" + idCommande +
                ", idUser=" + idUser +
                ", poids=" + poids +
                ", date=" + date +
                ", type='" + type + '\'' +
                ", idRegion=" + idRegion +
                ", statue=" + statue +
                ", dateFin=" + dateFin +
                '}';
    }

    public Commande(int idCommande,int idUser ,double poids ,Date date, int type, int idRegion, int statue, Date dateFin) {
        this.idCommande = idCommande;
        this.idUser=idUser;
        this.poids=poids;
        this.date = date;
        this.type = type;
        this.idRegion = idRegion;
        this.statue = statue;
        this.dateFin = dateFin;
    }
    //sans idCommande
    public Commande(int idUser ,double poids ,Date date, int type, int idRegion, int statue, Date dateFin) {
        this.idUser=idUser;
        this.poids=poids;
        this.date = date;
        this.type = type;
        this.idRegion = idRegion;
        this.statue = statue;
        this.dateFin = dateFin;
    }
    // Method to update an existing commande
    //now date
    public void updateCommande(Connection co) throws Exception {
        String sql = "UPDATE commande SET date = ?, type = ?, idRegion = ?, statue = ?, dateFin = ?, idUser = ?, poids = ? WHERE idCommande = ?";
        try (PreparedStatement preparedStatement = co.prepareStatement(sql)) {
            //closed
            preparedStatement.setDate(1, date);
            preparedStatement.setInt(2, type);
            preparedStatement.setInt(3, idRegion);
            preparedStatement.setInt(4, statue);
            preparedStatement.setDate(5, dateFin);
            preparedStatement.setInt(6, idUser);
            preparedStatement.setDouble(7, poids);
            preparedStatement.setInt(8, idCommande);
            
            preparedStatement.executeUpdate();
        }catch(Exception e){
            throw e;
        }
    }

    
    public static ArrayList<Commande> getAllCommandes() throws Exception {
        ArrayList<Commande> commandes = new ArrayList<>();
        String sql = "SELECT * FROM commande";
        Connection conn = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            conn = Connexion.connectePostgres();
            statement = conn.createStatement();
            resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                int idCommande = resultSet.getInt("idCommande");
                int idUser = resultSet.getInt("idUser");
                double poids = resultSet.getDouble("poids");
                Date date = resultSet.getDate("date");
                int type = resultSet.getInt("type");
                int idRegion = resultSet.getInt("idRegion");
                int statue = resultSet.getInt("statue");
                Date dateFin = resultSet.getDate("dateFin");
                commandes.add(new Commande(idCommande,idUser , poids,date, type, idRegion, statue, dateFin));
            }
        } catch (Exception e) {
            e.printStackTrace();
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
        String sql = "INSERT INTO commande (date, type, idRegion, statue, dateFin,idUser,poids) VALUES (?, ?, ?, ?, ?,?,?)";
        Connection conn = null;
        PreparedStatement prstm = null;
        try {
            conn = Connexion.connectePostgres();
            prstm = conn.prepareStatement(sql);
            prstm.setDate(2, date);
            prstm.setInt(3, type);
            prstm.setInt(4, idRegion);
            prstm.setInt(5, statue);
            prstm.setDate(6, dateFin);
            prstm.setInt(7,idUser);
            prstm.setDouble(8,poids);
            

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
    
    //get its id commande as well
    public int saveGet(Connection co) throws Exception {
        String sql = "INSERT INTO commande (date, type, idRegion, statue, dateFin, idUser, poids) VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING idCommande";
        Connection conn = null;
        PreparedStatement prstm = null;
        ResultSet rs = null;
        int generatedId = -1;
    
        try {
            conn = co;
            prstm = conn.prepareStatement(sql);
            prstm.setDate(1, date);
            prstm.setInt(2, type);
            prstm.setInt(3, idRegion);
            prstm.setInt(4, statue);
            prstm.setDate(5, dateFin);
            prstm.setInt(6, idUser);
            prstm.setDouble(7, poids);
            
            rs = prstm.executeQuery();
            if (rs.next()) {
                generatedId = rs.getInt(1);
            }
    
        } catch (Exception e) {
            throw e;
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (prstm != null) {
                prstm.close();
            }
            // if (conn != null) {
            //     conn.close();
            // }
        }
    
        return generatedId;
    }
    
    public void getById(int id) throws Exception {
        String sql = "SELECT * FROM commande WHERE idCommande = ?";
        Connection conn = null;
        PreparedStatement prstm = null;
        ResultSet resultSet = null;

        try {
            conn = Connexion.connectePostgres();
            prstm = conn.prepareStatement(sql);
            prstm.setInt(1, id);
            resultSet = prstm.executeQuery();

            if (resultSet.next()) {
                this.idCommande = resultSet.getInt("idCommande");
                this.idUser=resultSet.getInt("idUser");
                this.poids=resultSet.getDouble("poids");
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
    //updates
    public void mametrakaCommande(int statue) throws Exception {
        //avy updatena ilay dateFin anle commandenao farany
        //mi-inserer commande vaovao 
        String sql = "Update commande set dateFin=now() where idCommande=?";
        Connection conn = null;
        PreparedStatement prstm = null;
        try {
            this.getById(this.idCommande);
            this.setDateFin(null);
            this.setStatue(statue);
            conn = Connexion.connectePostgres();
            prstm = conn.prepareStatement(sql);
            prstm.setInt(1, idCommande); //set as commander
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
            
            conn = Connexion.connectePostgres();
            prstm = conn.prepareStatement(sql);
            prstm.setInt(1, stat);
            resultSet = prstm.executeQuery();

            while (resultSet.next()) {
                int idCommande = resultSet.getInt("idCommande");
                int idUser = resultSet.getInt("idUser");
                double poids = resultSet.getDouble("poids");
                Date date = resultSet.getDate("date");
                int type = resultSet.getInt("type");
                int idRegion = resultSet.getInt("idRegion");
                int statue = resultSet.getInt("statue");
                Date dateFin = resultSet.getDate("dateFin");
                commandes.add(new Commande(idCommande,idUser ,poids,date, type, idRegion, statue, dateFin));
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
            conn = Connexion.connectePostgres();
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
                double poids = resultSet.getDouble("poids");
                Date date = resultSet.getDate("date");
                int type = resultSet.getInt("type");
                int idRegion = resultSet.getInt("idRegion");
                int statue = resultSet.getInt("statue");
                Date dateFarany = resultSet.getDate("dateFin");
                commandes.add(new Commande(idCommande,idUser , poids,date, type, idRegion, statue, dateFarany));
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
