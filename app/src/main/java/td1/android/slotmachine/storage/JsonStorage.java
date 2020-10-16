package td1.android.slotmachine.storage;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
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

    private JSONArray tableauJeux;
    private JSONArray tableauThemes;
    private List<Jeu> jeux;
    private List<Theme> themes;

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

    public JSONArray getTableauJeux() {
        return tableauJeux;
    }
    public JSONArray getTableauThemes() {
        return tableauThemes;
    }

    //Constructeur
    public JsonStorage(Context context) {

        jeux = new ArrayList<Jeu>();
        themes = new ArrayList<Theme>();

        //On récupère les données de thèmes du fichier JSON
        String jsonTheme = null;
        try {
            InputStream is = context.getAssets().open("themes.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            jsonTheme = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        //On ajoutes les éléments dans la liste de theme
        try {
            JSONObject objectJson = new JSONObject(jsonTheme);
            themes = getJsonThemeList(objectJson.getJSONArray(THEMES));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //On récupère les données de thèmes du fichier JSON
        String jsonJeu = null;
        try {
            InputStream is = context.getAssets().open("jeux.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            jsonJeu = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        //On ajoutes les éléments dans la liste de theme
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

    public void writeJsonJeux() {
        for (int i = 0; i < jeux.size(); i++)
        {
            tableauJeux.put(jeux.get(i));
        }
        //return tableauJeux;
    }

    public void writeJsonThemes() {
        for (int i = 0; i < themes.size(); i++)
        {
            tableauThemes.put(themes.get(i));
        }
        //return tableauJeux;
    }

    public JSONObject jeuToJson(Jeu jeu) {
        JSONObject object = new JSONObject();
        try {
            object.put(THEMES, jeu.getThemes());
            object.put(NOM, jeu.getNom());
            object.put(ANNEE, jeu.getAnnee());
            object.put(RESUME, jeu.getResume());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }

    public Jeu jsonToJeu(JSONObject jsonObject) {
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

    public JSONObject themeToJson(Theme theme) {
        JSONObject object = new JSONObject();
        try {
            object.put(THEMES, theme.getName());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }

    public Theme jsonToTheme(JSONObject jsonObject) {
        Theme theme = null;
        try {
            theme = new Theme(jsonObject.getString(NAME));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return theme;
    }

    public List<Theme> getJsonThemeList(JSONArray objectJson) {
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
