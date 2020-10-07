package td1.android.slotmachine.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import td1.android.slotmachine.R;
import td1.android.slotmachine.model.Jeu;
import td1.android.slotmachine.model.Theme;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TEST RANDO
        List<Theme> ThemeTest =new ArrayList<>();

        ThemeTest.add(new Theme("RPG"));
        ThemeTest.add(new Theme("TACTICAL"));
        ThemeTest.add(new Theme("RTS"));
        ThemeTest.add(new Theme("STRATEGIE"));
        ThemeTest.add(new Theme("MYSTERE"));
        ThemeTest.add(new Theme("MOBA"));



        //Onclick sur le button levier envoie les themes ( A FAIRE ) et affiche la vue ChoiceActivity
        findViewById(R.id.MainSlotButton).setOnClickListener((v)-> {
            Random rand = new Random();
            int randSlot1=rand.nextInt(ThemeTest.size());
            int randSlot2=rand.nextInt(ThemeTest.size());
            int randSlot3=rand.nextInt(ThemeTest.size());
            //TEXT VIEW GAUCHE (METTRE SON NOM + CHANGEMENT COULEUR)
            ((TextView) findViewById(R.id.Slot1)).setText(ThemeTest.get(randSlot1).getName());
            (findViewById(R.id.Slot1)).setBackgroundColor(ThemeTest.get(randSlot1).getColor());
            //TEXT VIEW MILIEU (METTRE SON NOM + CHANGEMENT COULEUR)
            ((TextView) findViewById(R.id.Slot2)).setText(ThemeTest.get(randSlot2).getName());
            ( findViewById(R.id.Slot2)).setBackgroundColor(ThemeTest.get(randSlot2).getColor());
            //TEXT VIEW DROITE (METTRE SON NOM + CHANGEMENT COULEUR)
            ((TextView) findViewById(R.id.Slot3)).setText(ThemeTest.get(randSlot3).getName());
            (findViewById(R.id.Slot3)).setBackgroundColor(ThemeTest.get(randSlot3).getColor());





            //Intent intent = new Intent(getApplicationContext(),ChoiceActivity.class);
            //METTRE COMPTEUR


            //intent.putExtra(EXTRA_CONTACT,contacts.get(list.getChildViewHolder(v).getAdapterPosition()));
            //FAUDRA DONNER LES TROIS THEMES
            //startActivity(intent);
        });
    }


}