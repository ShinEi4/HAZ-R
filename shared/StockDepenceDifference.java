package crevettes;

import java.sql.*;

public class StockDepenceDifference {
    private String type;
    private int differenceQuantite;
    private double differencePrix;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getDifferenceQuantite() {
        return differenceQuantite;
    }

    public void setDifferenceQuantite(int differenceQuantite) {
        this.differenceQuantite = differenceQuantite;
    }

    public double getDifferencePrix() {
        return differencePrix;
    }

    public void setDifferencePrix(double differencePrix) {
        this.differencePrix = differencePrix;
    }

    @Override
    public String toString() {
        return "StockDepenseDifference{" +
                "type='" + type + '\'' +
                ", differenceQuantite=" + differenceQuantite +
                ", differencePrix=" + differencePrix +
                '}';
    }
}