package main;

import java.sql.Date;
import java.util.ArrayList;

import commande.*;

public class Main {
    public static void main(String[] args) {
        try {
            ArrayList<Commande> commandes = Commande.getAllCommandesParDate(3, "2000-05-05", "2050-05-05");
            for (Commande c : commandes) {
                System.out.println(c.toString());
            }
            Date d = Date.valueOf("2023-5-5");
            Commande c = new Commande();
            c.getById(1);
            // c.mametrakaCommande(1);
            System.out.println(c.toString());

        } catch (Exception e) {
            System.out.println(e);
        }

    }

}