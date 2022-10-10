package fr.ulm.centrage.data;

import java.io.Serializable;

public class Element implements Serializable {

    public String nom;

    public float masse; // Masse en Kg

    public float levier; // Bras de levier en cm

    public Element(String nom) {
        this.nom = nom;
    }

    public Element(String nom, int masse, int levier) {
        this.masse = masse;
        this.levier = levier;
        this.nom = nom;
    }

    public float moment() {
        return masse * levier;
    }

}