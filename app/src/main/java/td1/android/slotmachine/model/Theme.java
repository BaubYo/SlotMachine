package td1.android.slotmachine.model;

public class Theme {


    private String name;
    private String color;

    //Constructeur
    public Theme(String name){
        this.name=name;

        switch(name.toUpperCase()) {
            case "RPG":
                this.color="@colors/jaune";
                break;
            case "RTS":
                this.color="@colors/bleu";
                break;
            case "MOBA":
                this.color="@colors/rouge";
                break;
            case "TACTICAL":
                this.color="@colors/vert";
                break;
            default:
                this.color="@colors/blanc";
        }


    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
