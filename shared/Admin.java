package personne;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Vector;
import crevettes.Bassin;
import crevettes.Crevette;
import util.HashUtil;
import util.Connexion;
import crevettes.*;
public class Admin {
    int id;
    String nom;
    String motdepasse;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getNom() {
        return nom;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }
    public String getMotdepasse() {
        return motdepasse;
    }
    public void setMotdepasse(String m) {
        this.motdepasse=HashUtil.hashString(m);
    }

    public Admin() {
    }

    public Admin(int id, String nom, String m) {
        this.id = id;
        this.nom = nom;
        this.motdepasse =HashUtil.hashString(m);
    }
    public Admin(int id, String mdp) { //eviter hash
        this.id = id;
        this.motdepasse = mdp;
    }

    // mila fonction manao hash

    public void save()
    {
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = Connexion.connectePostgres();
            String query;
    
            query = "insert into admin (pseudo,motdepasse) values (?,?)";
            pstmt=conn.prepareStatement(query);
            pstmt.setString(1, this.getNom());
            pstmt.setString(2,this.getMotdepasse());

            int v=pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void update(int id)
    {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = Connexion.connectePostgres();
            String query;
    
            query = "update admin set pseudo =? ,motdepasse=? where id=?";
            pstmt=conn.prepareStatement(query);
            
            pstmt.setString(1, this.getNom());
            pstmt.setString(2,this.getMotdepasse());
            pstmt.setInt(3, id);

            int v=pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static Admin getById(int id)
    {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Admin user=null;
        try {
            conn = Connexion.connectePostgres();
            String query;
            // idadmin | pseudo | motdepasse
            query = "SELECT * FROM admin where idAdmin =?";
            pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, id);
        
            rs = pstmt.executeQuery();
    
            if (rs.next()) {
                String nom=rs.getString("pseudo");
                String mdp =rs.getString("motdepasse");
                user=new Admin(id,mdp);
                user.setNom(nom);
            }
    
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return user;
    }
    
    public static Vector<Admin> getAll()
    {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Vector<Admin> ls=new Vector<Admin>();

        try {
            conn = Connexion.connectePostgres();
            String query;
    
            query = "SELECT * FROM admin";
            pstmt = conn.prepareStatement(query);
        
            rs = pstmt.executeQuery();
    
            while (rs.next()) {
                int id=rs.getInt("id");
                String nom=rs.getString("pseudo");
                String mdp =rs.getString("motdepasse");
                Admin user=new Admin(id,mdp);
                user.setNom(nom);
                ls.add(user);
            }
    
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return ls;
    }
    
    //insert first commande
    //by default date should be now
    public void commander(double poids, String date, int type, int idRegion, int statue) throws Exception {
        Connection connexion = Connexion.connectePostgres();
        int idAdmin=this.getId();
        try {
            connexion.setAutoCommit(false); // Set auto-commit to false to manage transactions
    
            double stock = Crevette.getKgLeftInAllBassin(date);
            if (stock < poids) {
                throw new Exception("Stock insuffisant");
            }
            System.out.println("stock suffisant");
            boolean ampy = false;
            double poidsTemp = poids;
            for (int i = 0; i < Bassin.getAll().size() && !ampy; i++) {
                double inBassin = Crevette.getKgLeftInBassin(date, Bassin.getAll().get(i).getIdBassin());
                if (inBassin >= poidsTemp) {
                    ampy = true;
                }
                boolean is = Crevette.mooveCrevette(connexion, this.getId(), Bassin.getAll().get(i).getIdBassin(), poidsTemp, date, 2);
                poidsTemp -= inBassin;
            }
            System.out.println("crevette moved");
            Cours cours = Cours.getCoursByRegionAndType(idRegion, type);
            double total_a_paye = cours.getPrixUnitaire() * poids;
            Etat etat = new Etat();
            etat.getById(statue);
            double pourcentage = etat.getPourcentage();
            double now_paid = (pourcentage / 100.0) * total_a_paye;
    
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date daty = new Date(sdf.parse(date).getTime());
            
            Commande order = new Commande(idAdmin, poids, daty, type, idRegion, statue, daty);
            int idCommande = order.saveGet(connexion);
            order.setIdCommande(idCommande);
            System.out.println("Order sent");
            String raison = "commande:" + order.getIdCommande() + "-" + order.getStatue();
            // if(2>1){
            //     throw new Exception("ao");
            // }
            Caisse action = new Caisse(idAdmin, now_paid, 1, daty, raison);
            action.save(connexion);
            System.out.println("money moved");
            connexion.commit(); // Commit the transaction if everything is successful
        } catch (Exception e) {
            connexion.rollback(); // Rollback the transaction if an exception occurs
            throw new Exception("Probleme dans la transaction de commande: " + e.getMessage());
        } finally {
            connexion.setAutoCommit(true); // Reset auto-commit to true
            connexion.close();
        }
        
    }

    //switch to next state of commande
    //date now
    public void changeStateCommande(int idCommande, int statue,String date) throws Exception {
        Connection connexion = null;
        int idAdmin=this.getId();
        try{ 
            connexion = Connexion.connectePostgres();
            connexion.setAutoCommit(false); // Set auto-commit to false to manage transactions
            Commande order = new Commande();
            //what to change in Caisse
            order.getById(idCommande);//pro
            Cours cours = Cours.getCoursByRegionAndType(order.getIdRegion(), order.getType());
            System.out.println(cours.getRegion());
            System.out.println(order.getPoids());
            double total=cours.getPrixUnitaire();
            total=total*(order.getPoids());
            Etat etat=new Etat();
            etat.getById(statue);
            double pourcentage=etat.getPourcentage();
            double paye_now= (pourcentage / 100.0) * total;
            String raison = "commande:"+order.getIdCommande()+"-"+statue;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date daty = new Date(sdf.parse(date).getTime());
            Caisse action = new Caisse(idAdmin, paye_now, 1, daty, raison);//add in caisse
            action.save(connexion);
            System.out.println("money moved");
            //modif commande
            order.setStatue(statue); 
            // Get the current date and time
            java.util.Date now = new java.util.Date();
            // Convert to java.sql.Date if needed
            java.sql.Date nowSql = new java.sql.Date(now.getTime());
            // Set the current date as the dateFin
            order.setDateFin(nowSql);
            // Update the Commande in the database
            order.updateCommande(connexion);
            System.out.println("commande updated");
            if(order.getStatue()==4){
                Frais frais=Frais.getFraisByRegionAndType(order.getIdRegion(), order.getType());
                double fr=frais.getFrais();
                double frTota=fr*order.getPoids();
                raison = "frais:"+order.getIdCommande()+"-"+order.getStatue();
                Caisse action2 = new Caisse(idAdmin, frTota, 2, daty, raison);//remove for frais
                action2.save(connexion);
                System.out.println("frais payed");
            }
            connexion.commit(); // Commit the transaction if everything is successful
        } catch (Exception e) {
            connexion.rollback(); // Rollback the transaction if an exception occurs
            throw new Exception("Probleme dans la transaction de commande: " + e.getMessage());
        } finally {
            connexion.setAutoCommit(true); // Reset auto-commit to true
            connexion.close(); // Close the connection
        }
        
    }

    public static void afficheCrevette()throws Exception{
        String date = "2024-10-10";
        double totalKgLeft = Crevette.getKgLeftInAllBassin(date);
        List<Bassin> allBassins = Bassin.getAll();

        System.out.println("Total kilograms left in all basins on " + date + ": " + totalKgLeft);

        for (int i = 0; i < allBassins.size(); i++) {
            double kgLeftInBassin = Crevette.getKgLeftInBassin(date, allBassins.get(i).getIdBassin());
            System.out.println("Kilograms left in basin " + allBassins.get(i).getIdBassin() + " on " + date + ": " + kgLeftInBassin);
        }
    }

    public void achatequipement(int idEquipement,double quantite,String date)throws Exception{
        Connection connexion =null;
        try{
            connexion = Connexion.connectePostgres();
            connexion.setAutoCommit(false); // Set auto-commit to false to manage transactions
            Equipement equipement=new Equipement();
            equipement.getById(idEquipement);
            LocalDate localDate;
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                localDate = LocalDate.parse(date, formatter);
            } catch (DateTimeParseException e) {
                throw new Exception("Invalid date format. Please use the format yyyy-MM-dd", e);
            }
            Achat achat= new Achat(this,equipement,quantite, localDate);
            //caisse
            double prix=equipement.getPrixUnitaire();
            prix=prix*quantite;//fois isa
            String raison = "achat:"+equipement.getNom()+"-"+quantite;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date daty = new Date(sdf.parse(date).getTime());
            Caisse action = new Caisse(this.getId(), prix, 2, daty, raison);//remove in caisse
            action.save(connexion);
            System.out.println("money moved (achat quipement)");
            //achat enregistre
            achat.save(connexion);
            System.out.println("achat saved (achat quipement)");
            connexion.commit();
        }catch (Exception e) {
            connexion.rollback(); // Rollback the transaction if an exception occurs
            throw new Exception("Probleme dans la transaction d'achat d equipement " + e.getMessage());
        } finally {
            connexion.setAutoCommit(true); // Reset auto-commit to true
            connexion.close(); // Close the connection
        }

    }

    public void depenser(double qte, double prixUnitaire,String date,String libelle) throws Exception{
        Connection connexion =null;
        try{
            connexion = Connexion.connectePostgres();
            connexion.setAutoCommit(false); // Set auto-commit to false to manage transactions
            LocalDate localDate;
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                localDate = LocalDate.parse(date, formatter);
            } catch (DateTimeParseException e) {
                throw new Exception("Invalid date format. Please use the format yyyy-MM-dd", e);
            }

            Depense dep=new Depense(this, prixUnitaire, qte, localDate, libelle);
            //caisse
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date daty = new Date(sdf.parse(date).getTime());
            double vola_miala=prixUnitaire*qte;
            String raison= "depense:"+dep.getDepense()+"-"+qte;
            Caisse action=new Caisse(this.getId(),vola_miala,2,daty,raison);//remove money
            action.save(connexion);
            System.out.println("money moved for depense");
            //enregistrement de la depense
            dep.save(connexion);
            System.out.println("depense registered");

            connexion.commit();
        }catch (Exception e) {
            connexion.rollback(); // Rollback the transaction if an exception occurs
            throw new Exception("Probleme dans la transaction d'achat d equipement " + e.getMessage());
        } finally {
            connexion.setAutoCommit(true); // Reset auto-commit to true
            connexion.close(); // Close the connection
        }

        
    }

}
