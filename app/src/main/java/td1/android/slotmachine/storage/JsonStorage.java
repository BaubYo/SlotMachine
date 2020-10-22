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
    private static final String NAME = "name";
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
        jeux = new ArrayList<Jeu>();
        themes = new ArrayList<Theme>();

        /*
        themes.add(new Theme("RPG"));
        themes.add(new Theme("Action"));

        jeux.add(new Jeu(themes, "Cyberpunk", 2020,  "Dans ce jeu, tu peux jouer !"));
         */

        //saveThemes();
        //saveJeux();

        //On ouvre le fichier et copie son contenu sous forme de string
        File file = new File(context.getFilesDir(),"themes.json");
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader bufferedReader = new BufferedReader(fileReader);
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
        file = new File(context.getFilesDir(),"jeux.json");
        try {
            fileReader = new FileReader(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader buffer= new BufferedReader(fileReader);
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

        // https://medium.com/@nayantala259/android-how-to-read-and-write-parse-data-from-json-file-226f821e957a
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
                object.put(NAME, liste.get(i).getName());
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
            theme = new Theme(jsonObject.getString(NAME));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return theme;
    }

    //Renvoie la liste des themes d'un JSONArray                            //Faire ou pas la meme pour les jeux ? (c'est actuellement pas une fonction)
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
}
