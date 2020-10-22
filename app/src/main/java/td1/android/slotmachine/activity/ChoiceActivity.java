package td1.android.slotmachine.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import td1.android.slotmachine.R;
import td1.android.slotmachine.model.Jeu;
import td1.android.slotmachine.model.Theme;
import td1.android.slotmachine.storage.JsonStorage;

public class ChoiceActivity extends AppCompatActivity {

    private Jeu jeu;
    private Jeu jeu2;
    private List<Theme> listTheme;
    private JsonStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice);

        //On récupère les données depuis le JSON
        storage = new JsonStorage(getBaseContext());
        List<Theme> the = storage.getThemes();
        List<Jeu> je = storage.getJeux();

        listTheme = new ArrayList<>();

        //Récupération des données
        Intent extraIntent = getIntent();
        listTheme = (ArrayList<Theme>)extraIntent.getSerializableExtra("ThemesSelect");


        //FAUDRA GET DATA FROM JSON
        List<Theme> themes = new ArrayList<>();
        themes.add(new Theme("RPG"));
        themes.add(new Theme("Action"));
        /*
        jeu = new Jeu(themes, "Cyberpunk", 2020,  "Dans ce jeu, tu peux jouer !");
        jeu2 = new Jeu(themes, "CyberChocolat", 2020,  "74>73 !");*/

        jeu=TirageParThemes(listTheme);
        themes.add(new Theme("Action"));
        //jeu = new Jeu(the, "Cyberpunk" + je.size(), 2020,  "Dans ce jeu, tu peux jouer !");
        jeu = je.get(0);

        //Pour renlancer le jeu
        findViewById(R.id.choice_another).setOnClickListener((v) -> {
            //TODO masse code ici !
            jeu = new Jeu(themes, "Witcher 3", 2015,  "T'arrive tranquille sur ton cheval et tu pars rechercher une meuf");
        });

        //Pour renvoyer sur la page d'information
        findViewById(R.id.choice_game).setOnClickListener((v)-> {
            Intent intent = new Intent(getApplicationContext(), InformationActivity.class);
            intent.putExtra("EXTRA_GAME", jeu);
            startActivity(intent);
        });
    }

        public Jeu TirageParThemes(List<Theme> listTheme){
        Jeu jeuF = null;
        List<Jeu> tirageList=new ArrayList<>();
        List<Theme> th = new ArrayList<>();
        th.add(new Theme("RPG"));
        th.add(new Theme("Action"));
        //GET LIST OF JEU FROM JSON
        Jeu jeu = new Jeu(th, "Cyberpunk", 2020,  "Dans ce jeu, tu peux jouer !");
        Jeu jeu2 = new Jeu(th, "CyberChocolat", 2020,  "74>73 !");

        List<Jeu> jeux=new ArrayList<>();
        jeux.add(jeu);
        jeux.add(jeu2);

        for(int i=3;i>0;i--){

            if (i==3){
                for(Jeu j : jeux){
                    if(Correspondance(j.getThemes(),listTheme,i)){
                        tirageList.add(j);
                    }
                }
            }
            else if (i==2 && tirageList.isEmpty()){
                for(Jeu j : jeux){
                    if(Correspondance(j.getThemes(),listTheme,i)){
                        tirageList.add(j);
                    }
                }
            }
            else if (tirageList.isEmpty()){
                for(Jeu j : jeux){
                    if(Correspondance(j.getThemes(),listTheme,i)){
                        tirageList.add(j);
                    }
                }
            }
        }

        if (!tirageList.isEmpty()){
            Random rand=new Random();
            jeuF=tirageList.get(rand.nextInt(tirageList.size()));
        }
        return jeuF;
    }

    public Boolean Correspondance(List<Theme> themeDuJeu,List<Theme> themesAuto,int nbThemeNeed){
        int compteurThemeCorr=0;
        for (int i=0;i<themeDuJeu.size();i++){
            for(int j=0;j<themesAuto.size();j++)
                if(themeDuJeu.get(i).getName().equals(themesAuto.get(j).getName())){
                    compteurThemeCorr+=1;
                    break;
                }
        }
        if (compteurThemeCorr==nbThemeNeed)
            return true;
        return false;

    }


}