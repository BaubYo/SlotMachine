package td1.android.slotmachine.model;

import android.graphics.Color;

import java.io.Serializable;

public class Theme implements Serializable {

    private String nom;
    private int couleur;

    //Constructeur
    public Theme(String nom){
        this.nom=nom;
        nom = nom.toLowerCase();

        //Ici on choisi la couleur en fonction des trois premières lettres
        //normalement *255/122 mais pour avoir des couleurs plus vive on change ça
        this.couleur= Color.rgb(nom.charAt(0)*200/122,nom.charAt(1)*255/122,nom.charAt(2)*255/122);
    }

    //Getter et Setter
    public String getNom() {
        return nom;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getCouleur() {
        return couleur;
    }
    public void setCouleur(int couleur) {
        this.couleur = couleur;
    }


}
