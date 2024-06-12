package commande;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.sql.Date;
import connection.Connex;

public class BesoinCommande {
    private int idBesoin;
    private int idEquipement;
    private int qteEquipement;
    private String depense;
    private int qteDepense;
    private double frequence;

    // Constructeurs
    public BesoinCommande() {
    }

    public BesoinCommande(int idBesoin, int idEquipement, int qteEquipement, String depense, int qteDepense, double frequence) {
        this.idBesoin = idBesoin;
        this.idEquipement = idEquipement;
        this.qteEquipement = qteEquipement;
        this.depense = depense;
        this.qteDepense = qteDepense;
        this.frequence = frequence;
    }

    // Getters et Setters
    public int getIdBesoin() {
        return idBesoin;
    }

    public void setIdBesoin(int idBesoin) {
        this.idBesoin = idBesoin;
    }

    public int getIdEquipement() {
        return idEquipement;
    }

    public void setIdEquipement(int idEquipement) {
        this.idEquipement = idEquipement;
    }

    public int getQteEquipement() {
        return qteEquipement;
    }

    public void setQteEquipement(int qteEquipement) {
        this.qteEquipement = qteEquipement;
    }

    public String getDepense() {
        return depense;
    }

    public void setDepense(String depense) {
        this.depense = depense;
    }

    public int getQteDepense() {
        return qteDepense;
    }

    public void setQteDepense(int qteDepense) {
        this.qteDepense = qteDepense;
    }

    public double getFrequence() {
        return frequence;
    }

    public void setFrequence(double frequence) {
        this.frequence = frequence;
    }

    @Override
    public String toString() {
        return "BesoinCommande{" +
                "idBesoin=" + idBesoin +
                ", idEquipement=" + idEquipement +
                ", qteEquipement=" + qteEquipement +
                ", depense='" + depense + '\'' +
                ", qteDepense=" + qteDepense +
                ", frequence=" + frequence +
                '}';
    }
    public static ArrayList<BesoinCommande> getAllBesoinCommandes() throws Exception {
        ArrayList<BesoinCommande> besoins = new ArrayList<>();
        String sql = "SELECT * FROM BesoinCommande";
        Connection conn = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            conn = Connex.getConnection();
            statement = conn.createStatement();
            resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                int idBesoin = resultSet.getInt("idBesoin");
                int idEquipement = resultSet.getInt("idEquipement");
                int qteEquipement = resultSet.getInt("qteEquipement");
                String depense = resultSet.getString("depense");
                int qteDepense = resultSet.getInt("qteDepense");
                double frequence = resultSet.getDouble("frequence");

                BesoinCommande besoin = new BesoinCommande(idBesoin, idEquipement, qteEquipement, depense, qteDepense, frequence);
                besoins.add(besoin);
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

        return besoins;
    }
    public static ArrayList<BesoinCommande> prevision(double poid) throws Exception
    {
        ArrayList<BesoinCommande> valiny=null;
        try {
            valiny=BesoinCommande.getAllBesoinCommandes();
            
            for(BesoinCommande besoin : valiny)
            {   double equart=poid/besoin.getFrequence();
                besoin.frequence=poid;
                besoin.qteEquipement=(int)(besoin.getQteEquipement()*equart);
                besoin.qteDepense=(int)(besoin.getQteDepense()*equart);
                
            }
        } catch (Exception e) {
            throw e;
        }
        return valiny;
    }
}
