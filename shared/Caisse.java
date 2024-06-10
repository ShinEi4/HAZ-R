package crevettes;
import java.sql.*;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import util.Connexion;

public class Caisse {
    private int idAction;
    private double montant;
    private int action;
    private Date date;
    private String libelle;
    private int idAdmin; // Changement de IdAdmin à idAdmin

    public Caisse() {}

    // Constructeur pour initialiser tous les champs
    public Caisse(int idAdmin, double montant, int action, Date date, String libelle) {
        this.idAdmin = idAdmin; // Initialisation de idAdmin
        this.montant = montant;
        this.action = action;
        this.date = date;
        this.libelle = libelle;
    }


    // Getters and Setters
    public int getIdAction() {
        return idAction;
    }

    public void setIdAction(int idAction) {
        this.idAction = idAction;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }
    public void setDate(Date date) {
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
    public int getIdAdmin() { // Getter pour idAdmin
        return idAdmin;
    }

    public void setIdAdmin(int idAdmin) { // Setter pour idAdmin
        this.idAdmin = idAdmin;
    }

    // Get all transactions between two dates
    //ok
    public static List<Caisse> getAllTransactionsBetweenDates(String dateDebut, String dateFin) throws Exception {
        List<Caisse> transactions = new ArrayList<>();
        String query;
    
        if (dateDebut == null && dateFin == null) {
            query = "SELECT * FROM Caisse";
        } else if (dateDebut == null) {
            query = "SELECT * FROM Caisse WHERE date <= ?";
        } else if (dateFin == null) {
            query = "SELECT * FROM Caisse WHERE date >= ?";
        } else {
            query = "SELECT * FROM Caisse WHERE date BETWEEN ? AND ?";
        }
    
        try (Connection con = Connexion.connectePostgres();
            PreparedStatement pstmt = con.prepareStatement(query)) {
            int parameterIndex = 1;

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date parsedDate;
            Date parsedDate2;
            try {
                parsedDate = sdf.parse(dateDebut);
                parsedDate2 = sdf.parse(dateFin);
            } catch (ParseException e) {
                throw new Exception("Invalid date format. Please use the format yyyy-MM-dd");
            }
            java.sql.Date sqlDate = new java.sql.Date(parsedDate.getTime());
            java.sql.Date sqlDate2 = new java.sql.Date(parsedDate2.getTime());

            if (dateDebut != null) {
                pstmt.setDate(parameterIndex++, sqlDate);
            }
            if (dateFin != null) {
                pstmt.setDate(parameterIndex++, sqlDate2);
            }
    
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Caisse caisse = new Caisse();
                    caisse.setIdAction(rs.getInt("idAction"));
                    caisse.setIdAdmin(rs.getInt("idAdmin"));
                    caisse.setMontant(rs.getInt("montant"));
                    caisse.setAction(rs.getInt("action"));
                    caisse.setDate(rs.getDate("date"));
                    caisse.setLibelle(rs.getString("libelle"));
                    transactions.add(caisse);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }
    
    // Function to get the total amount in the Caisse at a given date
    //par defaut ajd no atao any am interface fa afaka ovaina
    //ok
    public static double getCaisseTotalAtDate(String date) throws Exception {
        double total = 0;

        String query = "SELECT COALESCE(SUM(CASE WHEN action = 1 THEN montant ELSE -montant END), 0) AS total " +
                       "FROM Caisse " +
                       "WHERE date <= ?";

        try (Connection con = Connexion.connectePostgres();
            PreparedStatement pstmt = con.prepareStatement(query)) {

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date parsedDate;
            try {
                parsedDate = sdf.parse(date);
            } catch (ParseException e) {
                throw new Exception("Invalid date format. Please use the format yyyy-MM-dd");
            }
            java.sql.Date sqlDate = new java.sql.Date(parsedDate.getTime());

            pstmt.setDate(1, sqlDate);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    total = rs.getDouble("total");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e; // Re-throw the exception to signal failure to the caller
        }

        return total;
    }
    //argent total en caisse
    //ok
    public static double getTotalAmount() throws Exception {
        double totalAmount = 0.0;

        // SQL query to calculate total amount in the cash register
        String query = "SELECT COALESCE(SUM(CASE WHEN action = 1 THEN montant ELSE -montant END), 0) AS total FROM Caisse";

        try (Connection connection = Connexion.connectePostgres();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            // Retrieve the total amount from the result set
            if (resultSet.next()) {
                totalAmount = resultSet.getDouble("total");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return totalAmount;
    }

    public boolean checkTransaction()throws Exception {
        if(this.getAction()==2){ //prendre de l argent
            // Convert the date to a string in the required format
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String dateString = sdf.format(this.getDate());
            // Get the available amount in the cash register at the given date
            double argentDispo = getCaisseTotalAtDate(dateString);
            System.out.println("argent dispo:"+ argentDispo);
            // Check if the transaction can be made
            if (this.montant > argentDispo) {
                return false; // Not enough money available
            }  
        }
        return true;
    }
    //here
    //ok
    // a fixer le rollback
    public void save(Connection co) throws Exception {
    // SQL query to insert data into the Caisse table
    String query = "INSERT INTO Caisse (idAdmin, montant, action, date, libelle) VALUES (?, ?, ?, ?, ?)";
    if(!this.checkTransaction()){
        throw new Exception("not enough money in caisse");
    }
    try (PreparedStatement preparedStatement = co.prepareStatement(query)) {
        //closed
        // Transform java.util.Date to java.sql.Date
        java.util.Date utilDate = this.getDate();
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());

        // Set parameters for the prepared statement
        preparedStatement.setInt(1, this.getIdAdmin());
        preparedStatement.setDouble(2, this.getMontant());
        preparedStatement.setInt(3, this.getAction());
        preparedStatement.setDate(4, sqlDate);
        preparedStatement.setString(5, this.getLibelle());

        // Execute the insert query
        preparedStatement.executeUpdate();

        System.out.println("Data inserted into Caisse table successfully.");

    } catch (SQLException e) {
        e.printStackTrace();
        throw e;
    }
}
    
    //ok
    public static Caisse getById(int idAction) throws Exception {
        String query = "SELECT * FROM Caisse WHERE idAction = ?";
        Caisse caisse = null;

        try (Connection connection = Connexion.connectePostgres();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, idAction);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                caisse = new Caisse();
                caisse.setIdAction(resultSet.getInt("idAction"));
                caisse.setIdAdmin(resultSet.getInt("idAdmin"));
                caisse.setMontant(resultSet.getDouble("montant"));
                caisse.setAction(resultSet.getInt("action"));
                caisse.setDate(resultSet.getDate("date"));
                caisse.setLibelle(resultSet.getString("libelle"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return caisse;
    }
    //ok
    public void update() throws Exception {
        String query = "UPDATE Caisse SET idAdmin = ?, montant = ?, action = ?, date = ?, libelle = ? WHERE idAction = ?";

        try (Connection connection = Connexion.connectePostgres();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            // Transform java.util.Date to java.sql.Date
            java.util.Date utilDate = this.getDate();
            java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());

            preparedStatement.setInt(1, this.getIdAdmin());
            preparedStatement.setDouble(2, this.getMontant());
            preparedStatement.setInt(3, this.getAction());
            preparedStatement.setDate(4, sqlDate);
            preparedStatement.setString(5, this.getLibelle());
            preparedStatement.setInt(6, this.getIdAction());

            // Execute the update query
            preparedStatement.executeUpdate();

            System.out.println("Data updated in Caisse table successfully.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //ok
    public void delete() throws Exception {
        String query = "DELETE FROM Caisse WHERE idAction = ?";

        try (Connection connection = Connexion.connectePostgres();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, this.getIdAction());

            // Execute the delete query
            preparedStatement.executeUpdate();

            System.out.println("Data deleted from Caisse table successfully.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

