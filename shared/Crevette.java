package crevettes;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import crevettes.Bassin;
import util.Connexion;

public class Crevette {

    //asina 0 ny ao am ilay idBassin raha n'importe quel bassin
    //ok
    public static Object[][] getStockCrevette(String date, int idBassin) throws Exception {
        // Create an ArrayList to store crevette data
        ArrayList<Object[]> crevetteData = new ArrayList<>();
        String createViewQuery;
        String selectQuery = "SELECT * FROM StockCrevetteVueDate";
    
        try (Connection con = Connexion.connectePostgres()) {
            // Construct the base query for creating the view
            StringBuilder queryBuilder = new StringBuilder();
            queryBuilder.append("CREATE OR REPLACE VIEW StockCrevetteVueDate AS ")
                        .append("SELECT b.idBassin, ")
                        .append("COALESCE(SUM(CASE WHEN sc.action = 1 THEN sc.poids ELSE -sc.poids END), 0) AS quantite, ")
                        .append("'kg' AS unite ")
                        .append("FROM Bassin b ")
                        .append("LEFT JOIN StockCrevette sc ON b.idBassin = sc.idBassin ");
            
            // Add conditions based on the parameters
            if (idBassin != 0 || (date != null && !date.isEmpty())) {
                queryBuilder.append("WHERE ");
                boolean addAnd = false;
                if (idBassin != 0) {
                    queryBuilder.append("b.idBassin = ").append(idBassin).append(" ");
                    addAnd = true;
                }
                if (date != null && !date.isEmpty()) {
                    if (addAnd) queryBuilder.append("AND ");
                    queryBuilder.append("sc.date <= '").append(date).append("' ");
                }
            }
            
            queryBuilder.append("GROUP BY b.idBassin ")
                        .append("ORDER BY b.idBassin ASC;");
            
            createViewQuery = queryBuilder.toString();
            
            // Create the view
            try (PreparedStatement pstmt = con.prepareStatement(createViewQuery)) {
                pstmt.executeUpdate();
            }
            
            // Select data from the view
            try (PreparedStatement pstmt = con.prepareStatement(selectQuery);
                 ResultSet rs = pstmt.executeQuery()) {
                 
                // Iterate through the result set and add each row to the crevetteData ArrayList
                while (rs.next()) {
                    Object[] rowData = new Object[3];
                    rowData[0] = rs.getInt("idBassin");
                    rowData[1] = rs.getDouble("quantite");
                    rowData[2] = rs.getString("unite");
                    crevetteData.add(rowData);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        // Convert the ArrayList to a 2D array
        Object[][] data = new Object[crevetteData.size()][3];
        for (int i = 0; i < crevetteData.size(); i++) {
            data[i] = crevetteData.get(i);
        }
        
        return data;
    }
    
    //ok
    public static void insertIntoEtatCrevette(int idAdmin,double poids, int idBassin, String date, int etat) throws Exception {
        // SQL query to insert data into EtatCrevette table
        String query = "INSERT INTO EtatCrevette (idAdmin,poids, idBassin, date, etat) VALUES ( ?,?, ?, ?, ?)";
        //6 si libre ilay etat
        // Connect to the database and prepare the statement
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
            // Set parameters for the prepared statement
            pstmt.setInt(1, idAdmin);
            pstmt.setDouble(2, poids);
            pstmt.setInt(3, idBassin);
            pstmt.setDate(4, sqlDate);
            pstmt.setInt(5, etat);

            // Execute the insert query
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(); // Handle any SQL exceptions
            throw e; // Rethrow the exception to be handled by the caller
        }
    }

    // Method to insert a row into StockCrevette table
    //ok
    public static void insertIntoStockCrevette(Connection co, int idAdmin, int idBassin, double poids, String date, int action) throws Exception {
        String query = "INSERT INTO StockCrevette (idAdmin, idBassin, poids, date, action) VALUES (?, ?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = co.prepareStatement(query)) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date parsedDate;
            try {
                parsedDate = sdf.parse(date);
            } catch (ParseException e) {
                throw new Exception("Invalid date format. Please use the format yyyy-MM-dd");
            }
            java.sql.Date sqlDate = new java.sql.Date(parsedDate.getTime());
            pstmt.setInt(1, idAdmin);
            pstmt.setInt(2, idBassin);
            pstmt.setDouble(3, poids);
            pstmt.setDate(4, sqlDate);
            pstmt.setInt(5, action);
    
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }
    

    // Calculate the number of kg left in the basin using StockCrevetteVueDate for today's date
    //ok
    //date can be null
    //on peut boucler pour avoir tout les basins
    public static double getKgLeftInBassin(String date,int idBassin) throws Exception {
        double kgLeft = 0.0;
        Object [][]obj=Crevette.getStockCrevette(date,idBassin); //pour initailiser la vue
        String query = "SELECT quantite FROM StockCrevetteVueDate WHERE idBassin = ?";

        try (Connection con = Connexion.connectePostgres();
             PreparedStatement pstmt = con.prepareStatement(query)) {

            pstmt.setInt(1, idBassin);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    kgLeft = rs.getDouble("quantite");
                }
            }
        }
        return kgLeft;
    }

    public static double getKgLeftInAllBassin(String date) throws Exception {
        double total = 0.0;
        // Loop through all bassins
        for (int i = 0; i < Bassin.getAll().size(); i++) {
            // Get the id of each bassin
            int currentBassinId = Bassin.getAll().get(i).getIdBassin();
            // Calculate the kg left in the current bassin
            total += getKgLeftInBassin(date, currentBassinId);
        }
        return total;
    }
    // Check if the move is valid
    //500 kg max par defaut-- a modifier si besoin
    //ok
    public static boolean checkMooveCrevette(int idBassin, double poids, int action,String date) throws Exception {
        double kgLeft = getKgLeftInBassin(date,idBassin);
        double newWeight;
    
        if (action == 1) { // Putting in
            newWeight = kgLeft + poids;
        } else if (action == 2) { // Taking out
            newWeight = kgLeft - poids;
        } else {
            return false; // Invalid action
        }
    
        if (action == 1) { // Putting in
            return newWeight <= 500;
        } else if (action == 2) { // Taking out
            return newWeight >= 0;
        }
        return false;
    }
    
    // Call this function !!! Move crevette
    //ok
    public static boolean mooveCrevette(Connection co,int idAdmin,int idBassin, double poids, String date, int action) throws Exception {
        if (checkMooveCrevette(idBassin, poids, action,date)) {
            insertIntoStockCrevette(co,idAdmin,idBassin, poids, date, action);
            return true;
        } 
        System.out.println("moove crevette tsy nety basin= "+ idBassin +" poids angatahana:"+poids );
        // throw new Exception("problem quantity transaction, tsy ampy ny ao");
        return false;
    }

    
    
}
