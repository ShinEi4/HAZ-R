package crevettes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import util.Connexion;

public class Frais {
    private int idFrais;
    private int type;
    private int region;
    private double frais;

    // Constructors
    public Frais() {}

    public Frais(int idFrais, int type, int region, double frais) {
        this.idFrais = idFrais;
        this.type = type;
        this.region = region;
        this.frais = frais;
    }

    // Getters
    public int getIdFrais() {
        return idFrais;
    }

    public int getType() {
        return type;
    }

    public int getRegion() {
        return region;
    }

    public double getFrais() {
        return frais;
    }

    // Setters
    public void setIdFrais(int idFrais) {
        this.idFrais = idFrais;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setRegion(int region) {
        this.region = region;
    }

    public void setFrais(double frais) {
        this.frais = frais;
    }

    // Save method
    public void save() throws Exception {
        String sql = "INSERT INTO Frais (type, region, frais) VALUES (?, ?, ?)";
        Connection conn = null;
        PreparedStatement prstm = null;
        try {
            conn = Connexion.connectePostgres();
            prstm = conn.prepareStatement(sql);

            prstm.setInt(1, type);
            prstm.setInt(2, region);
            prstm.setDouble(3, frais);

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
        String sql = "SELECT * FROM Frais WHERE idFrais = ?";
        Connection conn = null;
        PreparedStatement prstm = null;
        ResultSet resultSet = null;

        try {
            conn = Connexion.connectePostgres();
            prstm = conn.prepareStatement(sql);
            prstm.setInt(1, id);
            resultSet = prstm.executeQuery();

            if (resultSet.next()) {
                this.idFrais = resultSet.getInt("idFrais");
                this.type = resultSet.getInt("type");
                this.region = resultSet.getInt("region");
                this.frais = resultSet.getDouble("frais");
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
        String sql = "UPDATE Frais SET type = ?, region = ?, frais = ? WHERE idFrais = ?";
        Connection conn = null;
        PreparedStatement prstm = null;

        try {
            conn = Connexion.connectePostgres();
            prstm = conn.prepareStatement(sql);

            prstm.setInt(1, type);
            prstm.setInt(2, region);
            prstm.setDouble(3, frais);
            prstm.setInt(4, idFrais);

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
        String sql = "DELETE FROM Frais WHERE idFrais = ?";
        Connection conn = null;
        PreparedStatement prstm = null;

        try {
            conn = Connexion.connectePostgres();
            prstm = conn.prepareStatement(sql);
            prstm.setInt(1, idFrais);

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

    // Static method to get all Frais
    public static ArrayList<Frais> getAllFrais() throws Exception {
        ArrayList<Frais> fraisList = new ArrayList<>();
        String sql = "SELECT * FROM Frais";
        Connection conn = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            conn = Connexion.connectePostgres();
            statement = conn.createStatement();
            resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                int idFrais = resultSet.getInt("idFrais");
                int type = resultSet.getInt("type");
                int region = resultSet.getInt("region");
                double frais = resultSet.getDouble("frais");
                fraisList.add(new Frais(idFrais, type, region, frais));
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

        return fraisList;
    }

    // Get Frais by Region and Type
    public static Frais getFraisByRegionAndType(int region, int type) throws Exception {
        String sql = "SELECT * FROM Frais WHERE region = ? AND type = ?";
        Connection conn = null;
        PreparedStatement prstm = null;
        ResultSet resultSet = null;
        Frais frais = null;

        try {
            conn = Connexion.connectePostgres();
            prstm = conn.prepareStatement(sql);
            prstm.setInt(1, region);
            prstm.setInt(2, type);
            resultSet = prstm.executeQuery();

            if (resultSet.next()) {
                int idFrais = resultSet.getInt("idFrais");
                double fraisAmount = resultSet.getDouble("frais");
                frais = new Frais(idFrais, type, region, fraisAmount);
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

        return frais;
    }
}
