package td1.android.slotmachine.storage;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import td1.android.slotmachine.model.Jeu;
import td1.android.slotmachine.model.Theme;

public class JsonStorage {
    private static final String THEMES = "themes";
    private static final String JEUX = "jeux";
    private static final String NOM = "nom";
    private static final String ANNEE = "annee";
    private static final String RESUME = "resume";

    private List<Jeu> jeux;
    private List<Theme> themes;
    private Context context;

    //Getters Setters
    public List<Jeu> getJeux() {
        return jeux;
    }
    public void setJeux(List<Jeu> jeux) {
        this.jeux = jeux;
    }
    public List<Theme> getThemes() {
        return themes;
    }
    public void setThemes(List<Theme> themes) {
        this.themes = themes;
    }

    //Constructeur
    public JsonStorage(Context context) {
        this.context = context;

        //Verification que l'on possède bien les fichiers
        File fileThemes = new File(context.getFilesDir(),"themes.json");
        File fileJeux = new File(context.getFilesDir(),"jeux.json");
        FileReader fileReaderThemes = null;
        FileReader fileReaderJeux = null;
        boolean fileFind = false;

        while (!fileFind) //Si les fichiers n'existe pas, on les créer puis les charges
        {
            try {
                jeux = new ArrayList<Jeu>();
                themes = new ArrayList<Theme>();
                fileReaderThemes = new FileReader(fileThemes);
                fileReaderJeux = new FileReader(fileJeux);
                fileFind = true;
            } catch (FileNotFoundException e) {
                createData();
            }
        }

        //On ouvre le fichier et copie son contenu sous forme de string
        BufferedReader bufferedReader = new BufferedReader(fileReaderThemes);
        StringBuilder stringBuilder = new StringBuilder();
        String line = null;
        try {
            line = bufferedReader.readLine();
            while (line != null){
                stringBuilder.append(line).append("\n");
                line = bufferedReader.readLine();
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String jsonTheme = stringBuilder.toString();

        //On ajoute les thèmes en json dans la liste de thème
        try {
            JSONObject objectJson = new JSONObject(jsonTheme);
            themes = getJsonThemeList(objectJson.getJSONArray(THEMES));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //On ouvre le fichier et copie son contenu sous forme de string
        BufferedReader buffer= new BufferedReader(fileReaderJeux);
        stringBuilder = new StringBuilder();
        try {
            line = buffer.readLine();
            while (line != null){
                stringBuilder.append(line).append("\n");
                line = buffer.readLine();
            }
            buffer.close();// This responce will have Json Format String
        } catch (IOException e) {
            e.printStackTrace();
        }
        String jsonJeu = stringBuilder.toString();

        //On ajoutes les éléments dans la liste de jeux
        try {
            JSONObject objectJson = new JSONObject(jsonJeu);
            for (int i = 0; i < objectJson.getJSONArray(JEUX).length(); i++) {
                JSONObject jeuJson = objectJson.getJSONArray(JEUX).getJSONObject(i);
                jeux.add(jsonToJeu(jeuJson));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //Ecriture

    //On sauvegarde la liste de thème
    public void saveThemes() {
        //On récupère la liste sous forme d'objetJson
        JSONObject object = new JSONObject();
        try {
            object.put(THEMES, themesToJson(this.themes));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String str = object.toString() ;

        //Et on écrit dans le fichier
        File file = new File(context.getFilesDir(),"themes.json");
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(str);
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //On sauvegarde la liste de jeux
    public void saveJeux() {
        //On récupère la liste sous forme d'objetJson
        String str = jeuxToJson(this.jeux).toString();

        //Et on écrit dans le fichier
        File file = new File(context.getFilesDir(),"jeux.json");
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(str);
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Traductions

    //Renvoie un Jeu traduit en JSON
    private JSONObject jeuToJson(Jeu jeu) {
        JSONObject object = new JSONObject();
        try {
            object.put(THEMES, themesToJson(jeu.getThemes()));
            object.put(NOM, jeu.getNom());
            object.put(ANNEE, jeu.getAnnee());
            object.put(RESUME, jeu.getResume());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }

    //Renvoie un Jeu traduit en JSON
    private JSONObject jeuxToJson(List<Jeu> jeux) {
        JSONObject object = new JSONObject();
        try {
            JSONArray array = new JSONArray();
            for(int i = 0; i<jeux.size();i++)
                array.put( jeuToJson(jeux.get(i)));
            object.put(JEUX, array);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }

    //Pour une liste de themes donné, renvoie celle-ci traduit en JSON
    private JSONArray themesToJson(List<Theme> liste) {
        JSONArray array = new JSONArray();
        try {
            for (int i = 0; i < liste.size(); i++)
            {
                JSONObject object = new JSONObject();
                object.put(NOM, liste.get(i).getNom());
                array.put(object);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return array;
    }

    //Lecture
    private Jeu jsonToJeu(JSONObject jsonObject) {
        Jeu jeu = null;
        try {
            jeu = new Jeu(
                    jsonObject.getString(NOM),
                    jsonObject.getInt(ANNEE),
                    jsonObject.getString(RESUME)
            );
            jeu.setThemes(getJsonThemeList(jsonObject.getJSONArray(THEMES)));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jeu;
    }

    //Tranforme un JSONObject en Theme
    private Theme jsonToTheme(JSONObject jsonObject) {
        Theme theme = null;
        try {
            theme = new Theme(jsonObject.getString(NOM));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return theme;
    }

    //Renvoie la liste des themes d'un JSONArray
    private List<Theme> getJsonThemeList(JSONArray objectJson) {
        List<Theme> themeList = new ArrayList<>();
        try {
            for (int i = 0; i < objectJson.length(); i++) {
                JSONObject themeJson = objectJson.getJSONObject(i);
                themeList.add(jsonToTheme(themeJson));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return themeList;
    }

    //Créer des données ainsi que les fichiers JSON
    private void createData(){
        //Données de base :
        themes = new ArrayList<Theme>();
        jeux = new ArrayList<Jeu>();

        themes.add(new Theme("RPG"));
        themes.add(new Theme("FPS"));
        themes.add(new Theme("Tactical"));
        themes.add(new Theme("Action"));
        themes.add(new Theme("Open world"));
        themes.add(new Theme("Tour par tour"));

        jeux.add(new Jeu(new ArrayList<Theme>() {{
            add(new Theme("RPG"));
            add(new Theme("Action"));
            add(new Theme("Open world"));
        }}, "The Witcher 3: Wild Hunt", 2015,
                "À la suite des événements de The Witcher 2: Assassins of Kings, la guerre fait rage entre l'empire du Nilfgaard et ce qu'il reste des Royaumes du Nord."
        ));

        jeux.add(new Jeu(new ArrayList<Theme>() {{
            add(new Theme("Tactical"));
            add(new Theme("RPG"));
            add(new Theme("Tour par tour"));
        }}, "XCOM 2", 2016,
                "L'histoire de XCOM 2 prend place à la suite de celle de XCOM: Enemy Unknown dans le cas où la partie est perdue. Les extra-terrestres ont pris le contrôle de la terre."
        ));

        jeux.add(new Jeu(new ArrayList<Theme>() {{
            add(new Theme("RPG"));
            add(new Theme("Tour par tour"));
            add(new Theme("Tactical"));
        }}, "Fire Emblem: Three Houses", 2019,
                "Le jeu se divise en deux parties. Dans la première partie, qui dure douze chapitres, le joueur joue son rôle de professeur à l'Académie des officiers."
        ));

        jeux.add(new Jeu(new ArrayList<Theme>() {{
            add(new Theme("RPG"));
            add(new Theme("Action"));
            add(new Theme("FPS"));
            add(new Theme("Open world"));
        }}, "Cyberpunk 2077", 2020,
                "L'histoire de Cyberpunk 2077 prend place sur Terre en 2077 et se déroule dans la mégapole futuriste de Night City, dans l’État libre de Californie. "
        ));

        jeux.add(new Jeu(new ArrayList<Theme>() {{
            add(new Theme("RPG"));
            add(new Theme("FPS"));
            add(new Theme("Tour par tour"));
            add(new Theme("Action"));
            add(new Theme("Open world"));
        }}, "Fallout 4", 2015,
                "Un matin, à Boston, quelques jours avant Halloween, le protagoniste passe une journée comme une autre chez lui : il découvre le quartier de Sanctuary, et Codsworth, son robot personnel."
        ));

        jeux.add(new Jeu(new ArrayList<Theme>() {{
            add(new Theme("RPG"));
            add(new Theme("Tour par tour"));
            add(new Theme("Tactical"));
        }}, "Darkest Dungeon", 2016,
                "Le jeu est divisé en deux phases : une phase orientée sur la gestion d'un hameau et d'une équipe d'aventuriers et une phase orientée sur l'action et l'exploration par cette équipe."
        ));

        jeux.add(new Jeu(new ArrayList<Theme>() {{
            add(new Theme("FPS"));
            add(new Theme("Action"));
            add(new Theme("Tactical"));
        }}, "Insurgency: Sandstorm", 2018,
                "En début de partie, les joueurs doivent choisir une classe et peuvent ensuite sélectionner leurs armes et accessoires en utilisant des points d'équipement qui sont en nombre limité."
        ));

        saveJeux();
        saveThemes();
    }
    public void changeThemeChaqueJeu(String nomAChanger,String nomNouveau){
        List<Jeu> jeux=new ArrayList<Jeu>();
        jeux=this.getJeux();
        for (int i=0;i<jeux.size();i++){
            for (int j=0;j<jeux.get(i).getThemes().size();j++){
                if(jeux.get(i).getThemes().get(j).getNom().equals(nomAChanger))
                    jeux.get(i).getThemes().get(j).setNom(nomNouveau);
            }
        }
        this.saveJeux();
    }
}
