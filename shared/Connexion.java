package util;
import java.sql.*;
public class Connexion
{
      
        String driver ;
        String url;
        String user ;
        String password ;

        String getDriver()
        {
            return this.driver;
        }
        String getUrl()
        {
            return this.url;
        }
        String getUser()
        {
            return this.user;
        }
        String getPassword()
        {
            return this.password;
        }
        void setDriver(String d)
        {
            this.driver=d;
        }
        void setUrl(String d)
        {
            this.url=d;
        }
        void setUser(String d)
        {
            this.user=d;
        }
        void setPassword(String d)
        {
            this.password=d;
        }
        public Connexion(String d,String u,String us,String pass)  {
            setDriver(d);
            setUrl(u);
            setUser(us);
            setPassword(pass);
        }

        public Connection connectt() throws Exception
        {
            Class.forName(this.getDriver());
            Connection con=DriverManager.getConnection(this.getUrl(),this.getUser(),this.getPassword());
            return con;
        }

        public static Connection connectePostgres () throws Exception{ //connexion à postgres
            String driver = "org.postgresql.Driver";
            String url = "jdbc:postgresql://localhost:5432/crevettes";
            String user = "postgres";
            String password = "itu16";
            Connexion postgres = new Connexion(driver,url,user,password);
            return postgres.connectt();
        }

}
// C:\Users\Loic\OneDrive\Documents\S4\MmeBaovola\crevetteBack\lib\postgresql-42.6.2.jar
