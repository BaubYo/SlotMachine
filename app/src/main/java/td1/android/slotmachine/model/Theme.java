package td1.android.slotmachine.model;

import java.io.Serializable;
import android.graphics.Color;

public class Theme implements Serializable {


    private String name;
    private int color;

    //Constructeur
    public Theme(String name){
        this.name=name;

        switch(name.toUpperCase()) {
            case "RPG":
                this.color= Color.rgb(0,0,255);
                break;
            case "RTS":
                this.color=Color.rgb(0,255,0);
                break;
            case "MOBA":
                this.color=Color.rgb(255,255,0);
                break;
            case "TACTICAL":
                this.color=Color.rgb(255,0,255);
                break;
            default:
                this.color=Color.rgb(255,255,255);
        }


    }


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
