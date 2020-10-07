package td1.android.slotmachine.model;

import java.util.List;

public class Jeu {

    //Create Getter, Setter, Constructor : Alt+Insert

    //Atributs
    private List<Theme> themes;
    private String nom;
    private int annee;
    private String resume;

    //Constructeur
    public Jeu(List<Theme> themes, String nom, int annee, String resume) {
        this.themes = themes;
        this.nom = nom;
        this.annee = annee;
        this.resume = resume;
    }


    //Getter et Setter
    public List<Theme> getThemes() {
        return themes;
    }

    public void setThemes(List<Theme> themes) {
        this.themes = themes;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getAnnee() {
        return annee;
    }

    public void setAnnee(int annee) {
        this.annee = annee;
    }

    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }
}
