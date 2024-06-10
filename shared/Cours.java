package crevettes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import util.Connexion;

public class Cours {
    private int idCours;
    private int idType; //unique
    private int region; //unique
    private double prixUnitaire;

    
    // Constructors
    public Cours() {}

    public Cours(int idCours, int idType, int region, double prixUnitaire) {
        this.idCours = idCours;
        this.idType = idType;
        this.region = region;
        this.prixUnitaire = prixUnitaire;
    }

    // Getters
    public int getIdCours() {
        return idCours;
    }

    public int getIdType() {
        return idType;
    }

    public int getRegion() {
        return region;
    }

    public double getPrixUnitaire() {
        return prixUnitaire;
    }

    // Setters
    public void setIdCours(int idCours) {
        this.idCours = idCours;
    }

    public void setIdType(int idType) {
        this.idType = idType;
    }

    public void setRegion(int region) {
        this.region = region;
    }

    public void setPrixUnitaire(double prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }

    // Save method
    public void save() throws Exception {
        String sql = "INSERT INTO Cours (idType, region, prixUnitaire) VALUES (?, ?, ?)";
        Connection conn = null;
        PreparedStatement prstm = null;
        try {
            conn = Connexion.connectePostgres();
            prstm = conn.prepareStatement(sql);

            prstm.setInt(1, idType);
            prstm.setInt(2, region);
            prstm.setDouble(3, prixUnitaire);

            prstm.executeUpdate();
        } catch (Exception e) {
            throw e;
        } finally {
            if (prstm != null) {
                prstm.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }

    // Get by ID method
    public void getById(int id) throws Exception {
        String sql = "SELECT * FROM Cours WHERE idCours = ?";
        Connection conn = null;
        PreparedStatement prstm = null;
        ResultSet resultSet = null;

        try {
            conn = Connexion.connectePostgres();
            prstm = conn.prepareStatement(sql);
            prstm.setInt(1, id);
            resultSet = prstm.executeQuery();

            if (resultSet.next()) {
                this.idCours = resultSet.getInt("idCours");
                this.idType = resultSet.getInt("idType");
                this.region = resultSet.getInt("region");
                this.prixUnitaire = resultSet.getDouble("prixUnitaire");
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

    // Update method
    public void update() throws Exception {
        String sql = "UPDATE Cours SET idType = ?, region = ?, prixUnitaire = ? WHERE idCours = ?";
        Connection conn = null;
        PreparedStatement prstm = null;

        try {
            conn = Connexion.connectePostgres();
            prstm = conn.prepareStatement(sql);

            prstm.setInt(1, idType);
            prstm.setInt(2, region);
            prstm.setDouble(3, prixUnitaire);
            prstm.setInt(4, idCours);

            prstm.executeUpdate();
        } catch (Exception e) {
            throw e;
        } finally {
            if (prstm != null) {
                prstm.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }

    // Delete method
    public void delete() throws Exception {
        String sql = "DELETE FROM Cours WHERE idCours = ?";
        Connection conn = null;
        PreparedStatement prstm = null;

        try {
            conn = Connexion.connectePostgres();
            prstm = conn.prepareStatement(sql);
            prstm.setInt(1, idCours);

            prstm.executeUpdate();
        } catch (Exception e) {
            throw e;
        } finally {
            if (prstm != null) {
                prstm.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }

    // Static method to get all Cours
    public static ArrayList<Cours> getAllCours() throws Exception {
        ArrayList<Cours> coursList = new ArrayList<>();
        String sql = "SELECT * FROM Cours";
        Connection conn = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            conn = Connexion.connectePostgres();
            statement = conn.createStatement();
            resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                int idCours = resultSet.getInt("idCours");
                int idType = resultSet.getInt("idType");
                int region = resultSet.getInt("region");
                double prixUnitaire = resultSet.getDouble("prixUnitaire");
                coursList.add(new Cours(idCours, idType, region, prixUnitaire));
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (conn != null) {
                conn.close();
            }
        }

        return coursList;
    }

    // Get Cours by Region and Type
    public static Cours getCoursByRegionAndType(int region, int type) throws Exception {
        String sql = "SELECT * FROM Cours WHERE region = ? AND idType = ?";
        Connection conn = null;
        PreparedStatement prstm = null;
        ResultSet resultSet = null;
        Cours cours = null;

        try {
            conn = Connexion.connectePostgres();
            prstm = conn.prepareStatement(sql);
            prstm.setInt(1, region);
            prstm.setInt(2, type);
            resultSet = prstm.executeQuery();

            if (resultSet.next()) {
                int idCours = resultSet.getInt("idCours");
                int idType = resultSet.getInt("idType");
                double prixUnitaire = resultSet.getDouble("prixUnitaire");
                cours = new Cours(idCours, idType, region, prixUnitaire);
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

        return cours;
    }
}
