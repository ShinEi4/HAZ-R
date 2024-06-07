package connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connex {

    // Informations de connexion
    private static final String URL = "jdbc:postgresql://localhost:5432/crevette";
    private static final String USER = "postgres";
    private static final String PASSWORD = "edodrandria2203";

    // Méthode pour établir une connexion
    public static Connection getConnection() throws Exception {
        Connection connection = null;
        try {
            // Chargement du driver JDBC
            Class.forName("org.postgresql.Driver");
            // Établir la connexion
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (Exception e) {
            throw e;
        }
        return connection;
    }

}
