package fr.ulm.centrage.data;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import fr.ulm.centrage.util.Utils;

public final class Ulm implements Serializable {

    public static final long serialVersionUID = 1L;

    public enum ElementDefaut {
        PAX,
        ROUES,
        ROULETTE,
        BAG,
        ESS_AIL,
        ESS_AV,
        PILOTE
    }

    // ArrayList pour plus tard pour que l'utilisateur ajoute des elements
    private final Element[] elements = new Element[]
            {
                    new Element(ElementDefaut.PAX.toString()),
                    new Element(ElementDefaut.ROUES.toString()),
                    new Element(ElementDefaut.ROULETTE.toString()),
                    new Element(ElementDefaut.BAG.toString()),
                    new Element(ElementDefaut.ESS_AIL.toString()),
                    new Element(ElementDefaut.ESS_AV.toString()),
                    new Element(ElementDefaut.PILOTE.toString())
            };

    private float masseMax;

    private int min, max; // Positions extrêmes du CG (cm)

    private String nom;

    private String dateModif;

    private float centreGravite;

    public Ulm() {
        dateModif = Utils.getDate();
    }

    public Element[] getElements() {
        return elements;
    }

    public Element getElement(ElementDefaut element) {
        return elements[element.ordinal()];
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    public String getDateModif() {
        return dateModif;
    }

    public String getNom() {
        return this.nom;
    }

    public float getCentreGravite() {
        return centreGravite;
    }

    public float getMasseMax() {
        return masseMax;
    }

    public void setMasseMax(float masseMax) {
        this.masseMax = masseMax;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setDateModif(String date) {
        this.dateModif = date;
    }


    public float calculerCG() {
        float masseTT = 0, momentTT = 0;
        for (int i = 0; i < elements.length; i++) {
            momentTT += elements[i].levier * elements[i].masse;
            masseTT += elements[i].masse;
        }
        if (masseTT == 0f) return 0;
        centreGravite = momentTT / masseTT;
        return centreGravite;
    }

    public float calculerMasseTT() {
        float masseTT = 0f;
        for (int i = 0; i < elements.length; i++) {
            masseTT += elements[i].masse;
        }
        return masseTT;
    }

    public boolean isCGValid() {
        return centreGravite >= min && centreGravite <= max;
    }
}
