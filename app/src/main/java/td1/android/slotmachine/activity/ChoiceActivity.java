package td1.android.slotmachine.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import td1.android.slotmachine.R;
import td1.android.slotmachine.model.Jeu;
import td1.android.slotmachine.model.Theme;

public class ChoiceActivity extends AppCompatActivity {
    private Jeu jeu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice);

        List<Theme> themes = new ArrayList<>();
        themes.add(new Theme("RPG"));
        themes.add(new Theme("Action"));
        jeu = new Jeu(themes, "Cyberpunk", 2020,  "Dans ce jeu, tu peux jouer !");

        //Pour renlancer le jeu
        findViewById(R.id.choice_another).setOnClickListener((v) -> {
            //TODO masse code ici !
            jeu = new Jeu(themes, "Witcher 3", 2015,  "Dans ce jeu, tu peux jouer !");
        });

        //Pour renvoyer sur la page d'information
        findViewById(R.id.choice_game).setOnClickListener((v)-> {
            Intent intent = new Intent(getApplicationContext(),InformationActivity.class);
            //intent.putExtra(EXTRA_CONTACT,contacts.get(list.getChildViewHolder(v).getAdapterPosition()));
            //FAUDRA DONNER LE JEU
            startActivity(intent);
        });
    }
}