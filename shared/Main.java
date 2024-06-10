package util;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import crevettes.Bassin;
import crevettes.Caisse;
import crevettes.Crevette;
import crevettes.Equipement;
import crevettes.Etat;
import personne.Admin;
import util.*;
public class Main {
    // Main method for testing
    public static void main(String[] args) throws Exception {
        // Test the getStockEquipement method
        Equipement equipment = new Equipement(12, "kofafaaa", 8.9);
        // Object[][] stockData = equipment.getStockEquipement("2024-06-06");  // Change the date as needed
        // for (Object[] rowData : stockData) {
        //     System.out.println("Equipment: " + rowData[0] + ", Quantity: " + rowData[1]);
        // }

        // equipment. insertEquipement() ;
        // equipment.updateEquipement("broom", 990.0);
        // equipment.getEquipementById(2);
        // System.out.println(equipment.getNom());

        // equipment.insertIntoStockEquipement(2, 100, "2024-10-10",1);
        String dateDebut = "2024-07-07";
            String dateFin = "2024-08-08";
            // Caisse caisse=new Caisse();
            // List<Caisse> transactions = caisse.getAllTransactionsBetweenDates(dateDebut, dateFin);

            // // Print the transactions
            // for (Caisse transaction : transactions) {
            //     System.out.println(transaction.toString());
            // }

        //     double cat=Caisse.getTotalAmount();
        //     System.out.println(cat);
            // Create a SimpleDateFormat object
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            // Parse the date string to create a Date object
            // Date date = sdf.parse(dateDebut);

            // Caisse ca=new  Caisse(2,2, 2,date, "buy");
            // ca.save();
            // ca.getById(1);
            // System.out.println(ca.getLibelle());
            // ca.setAction(1);
            // ca.update();
            // System.out.println(ca.checkTransaction());
            

            // List<Etat> etats= Etat.getAllEtat();
            // System.out.println(etats.get(0).getNom());

            // Etat etat=new Etat();
            // etat.getEtatById(1);
            // System.out.println(etat.getNom());
            // Object[][] stock=Crevette.getStockCrevette("", 1);
            // System.out.println(stock[0][0]);
            // System.out.println(stock[0][1]);
            // Object[][] stock2=Crevette.getStockCrevette("", 2);
            // System.out.println(stock2[0][0]);
            // System.out.println(stock2[0][1]);
            // double montant=Crevette.getKgLeftInBassin(1);
            // System.out.println(montant);
            // Crevette.mooveCrevette(1,1, 4,dateDebut, 1);
            // montant=Crevette.getKgLeftInBassin(1);
            // System.out.println(montant);
            // Crevette.mooveCrevette(1, 0.9, "2024-07-07", 2);
            // Crevette.insertIntoStockCrevette(1, 470,"2023-06-06", 1) ;
            // Crevette.insertIntoStockCrevette(1, 2,"2023-06-06", 2) ;
            // Crevette.insertIntoStockCrevette(2, 20,"2023-06-06", 1) ;
            // Crevette.insertIntoStockCrevette(2, 2,"2023-06-06", 2) ;
            // double montant=Crevette.getKgLeftInBassin(1);
            // System.out.println(montant);
            // String daty="2024-11-11";
            // // Admin.afficheCrevette();

            //test commande et update

            // Admin moi=Admin.getById(1);
            // System.out.println("money in caisse="+Caisse.getCaisseTotalAtDate(daty)+" at "+daty);
            // System.out.println("je commande ...");
            // moi.commander( 20.0,"2024-10-10" , 1, 1, 1) ;
            // System.out.println("money in caisse="+Caisse.getCaisseTotalAtDate(daty)+" at "+daty);
            // System.out.println("je change de statue ...");
            // moi.changeStateCommande(9, 2,"2024-11-11");
            // // System.out.println("----------------------------------------------");
            // System.out.println("money in caisse="+Caisse.getCaisseTotalAtDate(daty)+" at 2024-11-11");

            //test achat
            // Admin soy=Admin.getById(2);
            // System.out.println("money in caisse="+Caisse.getCaisseTotalAtDate("2024-10-10")+" at 2024-10-10");
            // soy.achatequipement(1,2,"2024-10-10");
            // System.out.println("money in caisse="+Caisse.getCaisseTotalAtDate("2024-10-10")+" at 2024-10-10");

            //test depense
            Admin me=Admin.getById(2);
            System.out.println("money in caisse="+Caisse.getCaisseTotalAtDate("2024-10-10")+" at 2024-10-10");
            me.depenser(2, 25.5,"2024-10-10","nividy kofafa");
            System.out.println("money in caisse="+Caisse.getCaisseTotalAtDate("2024-10-10")+" at 2024-10-10");

    }
}
//javac -cp "../lib/*" -d "../classes" *.java
//java -cp .:lib/* util/Main

// javac -d . *.java
// java util.Main