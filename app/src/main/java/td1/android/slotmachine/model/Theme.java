package td1.android.slotmachine.model;

import android.content.Context;
import android.graphics.Color;

import androidx.core.content.ContextCompat;

import td1.android.slotmachine.R;

public class Theme {


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
                this.color=Color.rgb(0,0,0);
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
