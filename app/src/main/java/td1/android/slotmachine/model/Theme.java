package td1.android.slotmachine.model;

import android.graphics.Color;

import java.io.Serializable;

public class Theme implements Serializable {

    private String name;
    private int color;

    //Constructeur
    public Theme(String name){
        this.name=name;
        name = name.toLowerCase();

        //Ici on choisi la couleur en fonction des trois premières lettres
        //normalement *255/122 mais pour avoir des couleurs plus vive on change ça
        this.color= Color.rgb(name.charAt(0)*200/122,name.charAt(1)*255/122,name.charAt(2)*255/122);
    }

    //Getter et Setter
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public int getColor() {
        return color;
    }
    public void setColor(int color) {
        this.color = color;
    }


}
