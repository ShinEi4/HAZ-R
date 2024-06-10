package crevettes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import util.Connexion;

public class Bilan {

    public List<StockDepense> getTheoriqueStock(String dateDebut, String dateFin) {
        String query = "SELECT " +
                "    equipement.nom AS type, " +
                "    SUM(besoinbassin.qteEquipement) AS quantite, " +
                "    equipement.prixUnitaire, " +
                "    saison.dateDebut " +
                "FROM " +
                "    BesoinBassin besoinbassin " +
                "JOIN " +
                "    Equipement equipement ON besoinbassin.idEquipement = equipement.idEquipement " +
                "JOIN " +
                "    Saison saison ON saison.idSaison = besoinbassin.idBassin " +
                "WHERE " +
                "    saison.dateDebut BETWEEN CAST(? AS DATE) AND CAST(? AS DATE) " +
                "GROUP BY " +
                "    equipement.nom, " +
                "    equipement.prixUnitaire, " +
                "    saison.dateDebut;";
        return executeQuery(query, dateDebut, dateFin);
    }

    public List<StockDepense> getReelStock(String dateDebut, String dateFin) {
        String query = "SELECT " +
                "    equipement.nom AS type, " +
                "    SUM(achatequipement.quantite) AS quantite, " +
                "    equipement.prixUnitaire, " +
                "    achatequipement.date " +
                "FROM " +
                "    AchatEquipement achatequipement " +
                "JOIN " +
                "    Equipement equipement ON achatequipement.idEquipement = equipement.idEquipement " +
                "WHERE " +
                "    achatequipement.date BETWEEN CAST(? AS DATE) AND CAST(? AS DATE) " +
                "GROUP BY " +
                "    equipement.nom, " +
                "    equipement.prixUnitaire, " +
                "    achatequipement.date;";
        return executeQuery(query, dateDebut, dateFin);
    }

    private List<StockDepense> executeQuery(String query, String dateDebut, String dateFin) {
        List<StockDepense> stockDepenseList = new ArrayList<>();
        try (Connection conn = Connexion.connectePostgres();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, dateDebut);
            stmt.setString(2, dateFin);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                StockDepense sd = new StockDepense();
                sd.setType(rs.getString("type"));
                sd.setQuantite(rs.getInt("quantite"));
                sd.setPrixUnitaire(rs.getInt("prixUnitaire"));
                sd.setDate(rs.getString("dateDebut") != null ? rs.getString("dateDebut") : rs.getString("date"));
                stockDepenseList.add(sd);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stockDepenseList;
    }

    public List<StockDepenceDifference> calculateDifferences(List<StockDepense> theorique, List<StockDepense> real) {
        List<StockDepenceDifference> differences = new ArrayList<>();

        for (StockDepense theo : theorique) {
            StockDepense realItem = real.stream()
                    .filter(r -> r.getType().equals(theo.getType()) && r.getDate().equals(theo.getDate()))
                    .findFirst()
                    .orElse(null);

            if (realItem != null) {
                StockDepenceDifference diff = new StockDepenceDifference();
                diff.setType(theo.getType());
                diff.setDifferenceQuantite(theo.getQuantite() - realItem.getQuantite());
                diff.setDifferencePrix((theo.getQuantite() * theo.getPrixUnitaire())
                        - (realItem.getQuantite() * realItem.getPrixUnitaire()));
                differences.add(diff);
            } else {
                StockDepenceDifference diff = new StockDepenceDifference();
                diff.setType(theo.getType());
                diff.setDifferenceQuantite(theo.getQuantite());
                diff.setDifferencePrix(theo.getQuantite() * theo.getPrixUnitaire());
                differences.add(diff);
            }
        }

        for (StockDepense realItem : real) {
            StockDepense theoItem = theorique.stream()
                    .filter(t -> t.getType().equals(realItem.getType()) && t.getDate().equals(realItem.getDate()))
                    .findFirst()
                    .orElse(null);

            if (theoItem == null) {
                StockDepenceDifference diff = new StockDepenceDifference();
                diff.setType(realItem.getType());
                diff.setDifferenceQuantite(-realItem.getQuantite());
                diff.setDifferencePrix(-(realItem.getQuantite() * realItem.getPrixUnitaire()));
                differences.add(diff);
            }
        }

        return differences;
    }
}
