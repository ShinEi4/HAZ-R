package util;
import java.sql.*;

public class Connexion {
    String driver;
    String url;
    String user;
    String password;
    public String getDriver() {
        return driver;
    }
    public void setDriver(String driver) {
        this.driver = driver;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getUser() {
        return user;
    }
    public void setUser(String user) {
        this.user = user;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public Connexion() {
    }
    public Connexion(String driver, String url, String user, String password) {
        this.driver = driver;
        this.url = url;
        this.user = user;
        this.password = password;
    }
    public Connection connect() throws Exception
    {
        Class.forName(this.getDriver());
        Connection con=DriverManager.getConnection(this.getUrl(),this.getUser(),this.getPassword());
        return con;
    }   
    public static Connection connectPostgres() throws Exception
    {
        String driver= "org.postgresql.Driver";
        String url="jdbc:postgres://localhost:5433/crevette";
        String user="postgres";
        String mdp="postgres1234";
        Connexion postgres=new Connexion(driver,url,user,mdp);
        return postgres.connect();
    } 
}
