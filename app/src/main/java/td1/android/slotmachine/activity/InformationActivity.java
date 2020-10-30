package td1.android.slotmachine.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import td1.android.slotmachine.R;
import td1.android.slotmachine.adapter.JeuAdapter;
import td1.android.slotmachine.adapter.ThemeAdapter;
import td1.android.slotmachine.model.Jeu;
import td1.android.slotmachine.model.Theme;

public class InformationActivity extends AppCompatActivity {
    private Jeu jeu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        //Si aucun jeu n'est renvoyer, on donne les valeurs suivante
        List<Theme> themes = new ArrayList<>();
        themes.add(new Theme("RPG"));
        themes.add(new Theme("Action"));
        jeu = new Jeu(themes, "Test 2", 2000,  "Dans ce jeu, tu peux jouer !");

        //Récupération des données
        Intent extraIntent = getIntent();
        jeu = (Jeu) extraIntent.getSerializableExtra("EXTRA_GAME");

        //Lier les données au layout
        ((TextView) findViewById(R.id.information_title)).setText(jeu.getNom());
        ((TextView) findViewById(R.id.information_information)).setText(getString(R.string.app_information_information, jeu.getAnnee(), jeu.getResume()));

        //Envoie pour affichage des thèmes
        RecyclerView list = (RecyclerView) findViewById(R.id.information_themes);
        list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        list.setAdapter(new ThemeAdapter(jeu.getThemes()) {
            @Override
            public void onItemClick(View v) {
                //rien
            }

            @Override
            public void onItemLongClick(View v) {
                //rien
            }
        });

        //Pour Renvoyer sur la page wikipedia
        findViewById(R.id.information_wiki).setOnClickListener((v) -> {
            String url = "https://fr.wikipedia.org/wiki/Wikip%C3%A9dia:Accueil_principal"; //TODO à changer
            // exemple https://fr.wikipedia.org/wiki/The_Witcher_3:_Wild_Hunt
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        });

        //Pour renvoyer sur la page d'accueil
        findViewById(R.id.information_replay).setOnClickListener((v)-> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        });
    }
}