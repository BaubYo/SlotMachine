package td1.android.slotmachine.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.Menu;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import td1.android.slotmachine.R;
import td1.android.slotmachine.model.Theme;

public class MainActivity extends AppCompatActivity {

    private Handler handler = new Handler();

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            roulette(ThemeTest);

            // 30 ms boucle
            handler.postDelayed(runnable, 20);
            if (!Slot1Continue && !Slot2Continue && !Slot3Continue) {
                handler.removeCallbacks(runnable);
                timerEndRoulette();
            }

        }
    };

    //BOOLEAN POUR LA LOOP
    Boolean Slot1Continue=false;
    Boolean Slot2Continue=false;
    Boolean Slot3Continue=false;

    CountDownTimer countDownTimer= new CountDownTimer(3600, 900)
    {
        public void onTick(long millisUntilFinished) {
            ((TextView)findViewById(R.id.countDown)).setText(millisUntilFinished / 1000 + " seconds restantes");
        }

        public void onFinish() {
            ((TextView)findViewById(R.id.countDown)).setText("termine!");
            Intent intent = new Intent(getApplicationContext(),ChoiceActivity.class);
            List<Theme> themeToNextAct = new ArrayList<>();
            themeToNextAct.add(new Theme(((TextView) findViewById(R.id.Slot1)).getText().toString()));
            themeToNextAct.add(new Theme(((TextView) findViewById(R.id.Slot2)).getText().toString()));
            themeToNextAct.add(new Theme(((TextView) findViewById(R.id.Slot3)).getText().toString()));

            intent.putExtra("ThemesSelect", (Serializable)themeToNextAct);
            startActivity(intent);
        }
    };


    protected List<Theme> ThemeTest = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        ThemeTest.add(new Theme("RPG"));
        ThemeTest.add(new Theme("Tactique"));
        ThemeTest.add(new Theme("RTS"));
        ThemeTest.add(new Theme("Strategie"));
        ThemeTest.add(new Theme("Mystere"));
        ThemeTest.add(new Theme("Action"));

        //Onclick sur le button levier envoie les themes ( A FAIRE ) et affiche la vue ChoiceActivity
        findViewById(R.id.MainSlotButton).setOnClickListener((v)-> {

            if (!Slot1Continue && !Slot2Continue && !Slot3Continue) {
                handler.removeCallbacks(runnable);
                countDownTimer.cancel();
                ((TextView)findViewById(R.id.countDown)).setText("");
                handler.post(runnable);
            }
            Slot1Continue = true;
            Slot2Continue = true;
            Slot3Continue = true;
        });
        findViewById(R.id.changer_page).setOnClickListener((v)-> {
            Intent intent = new Intent(getApplicationContext(),ChoiceActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.StopSlot1Button).setOnClickListener((v)-> {
            Slot1Continue=false;
        });

        findViewById(R.id.StopSlot2Button).setOnClickListener((v)-> {
            Slot2Continue=false;
        });

        findViewById(R.id.StopSlot3Button).setOnClickListener((v)-> {
            Slot3Continue=false;
        });

    }

    protected void roulette(List<Theme> ThemeTest){
        Random rand = new Random();
        if (Slot1Continue) {
            int randSlot1 = rand.nextInt(ThemeTest.size());
            //TEXT VIEW GAUCHE (METTRE SON NOM + CHANGEMENT COULEUR)
            ((TextView) findViewById(R.id.Slot1)).setText(ThemeTest.get(randSlot1).getName());
            (findViewById(R.id.Slot1)).setBackgroundColor(ThemeTest.get(randSlot1).getColor());
        }
        if (Slot2Continue) {
            int randSlot2 = rand.nextInt(ThemeTest.size());
            //TEXT VIEW GAUCHE (METTRE SON NOM + CHANGEMENT COULEUR)
            ((TextView) findViewById(R.id.Slot2)).setText(ThemeTest.get(randSlot2).getName());
            (findViewById(R.id.Slot2)).setBackgroundColor(ThemeTest.get(randSlot2).getColor());
        }
        if (Slot3Continue) {
            int randSlot3 = rand.nextInt(ThemeTest.size());
            //TEXT VIEW GAUCHE (METTRE SON NOM + CHANGEMENT COULEUR)
            ((TextView) findViewById(R.id.Slot3)).setText(ThemeTest.get(randSlot3).getName());
            (findViewById(R.id.Slot3)).setBackgroundColor(ThemeTest.get(randSlot3).getColor());
        }
    }

    protected void timerEndRoulette(){
        countDownTimer.start();
    }

    //Pour afficher menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings, menu);
        return true;
    }
}