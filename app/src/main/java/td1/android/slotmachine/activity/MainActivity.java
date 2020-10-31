package td1.android.slotmachine.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import td1.android.slotmachine.R;
import td1.android.slotmachine.adapter.JeuAdapter;
import td1.android.slotmachine.adapter.ThemeAdapter;
import td1.android.slotmachine.model.Jeu;
import td1.android.slotmachine.model.Theme;
import td1.android.slotmachine.storage.JsonStorage;

public class MainActivity extends AppCompatActivity {

    private Handler handler = new Handler();

    //Boolean pour la boucle
    protected Boolean Slot1Continue=false;
    protected Boolean Slot2Continue=false;
    protected Boolean Slot3Continue=false;
    protected List<Theme> ThemeTest = new ArrayList<>();
    protected List<Jeu> JeuTest = new ArrayList<>();
    protected JsonStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //On récupère les données depuis le JSON
        storage = new JsonStorage(getBaseContext()); //ça crash ici !!!
        ThemeTest = storage.getThemes();
        JeuTest=storage.getJeux();

        //Onclick sur le button levier envoie les themes, puis affiche la vue ChoiceActivity au bout de 3s
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

        //On change les etats
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

    //Boucle toute les 20 ms
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            roulette(ThemeTest);
            handler.postDelayed(runnable, 20);
            if (!Slot1Continue && !Slot2Continue && !Slot3Continue) {
                handler.removeCallbacks(runnable);
                timerEndRoulette();
            }
        }
    };

    //Delay
    CountDownTimer countDownTimer= new CountDownTimer(3600, 900)
    {
        //affichage du temps restant
        public void onTick(long millisUntilFinished) {
            ((TextView)findViewById(R.id.countDown)).setText(millisUntilFinished / 1000 + " secondes restantes");
        }

        //Quand le delay est terminé, on envoi les thèmes selectionné
        public void onFinish() {
            ((TextView)findViewById(R.id.countDown)).setText("Terminé!");
            Intent intent = new Intent(getApplicationContext(),ChoiceActivity.class);
            List<Theme> themeToNextAct = new ArrayList<>();
            themeToNextAct.add(new Theme(((TextView) findViewById(R.id.Slot1)).getText().toString()));
            themeToNextAct.add(new Theme(((TextView) findViewById(R.id.Slot2)).getText().toString()));
            themeToNextAct.add(new Theme(((TextView) findViewById(R.id.Slot3)).getText().toString()));

            intent.putExtra("ThemesSelect", (Serializable)themeToNextAct);
            startActivity(intent);
        }
    };

    //Affichage des thèmes aléatoire
    protected void roulette(List<Theme> ThemeTest){
        Random rand = new Random();
        if (Slot1Continue) {
            int randSlot1 = rand.nextInt(ThemeTest.size());
            ((TextView) findViewById(R.id.Slot1)).setText(ThemeTest.get(randSlot1).getName());
            (findViewById(R.id.Slot1)).setBackgroundColor(ThemeTest.get(randSlot1).getColor());
        }
        if (Slot2Continue) {
            int randSlot2 = rand.nextInt(ThemeTest.size());
            ((TextView) findViewById(R.id.Slot2)).setText(ThemeTest.get(randSlot2).getName());
            (findViewById(R.id.Slot2)).setBackgroundColor(ThemeTest.get(randSlot2).getColor());
        }
        if (Slot3Continue) {
            int randSlot3 = rand.nextInt(ThemeTest.size());
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

    //Quand un menu est selectionné, on affiche sa fenêtre correspondante
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //view = requireActivity().getLayoutInflater().inflate(R.layout.prompts_add_jeu,null);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);

        switch (item.getItemId()) {
            //Si on veux ajouter un jeu
            case R.id.menu_game_add:
                List<Theme> themeSelect=new ArrayList<Theme>();
                View layout = inflater.inflate(R.layout.prompts_add_jeu,null);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                final EditText jeuName = (EditText) layout.findViewById(R.id.promptsJeuName);
                final EditText jeuAn = (EditText) layout.findViewById(R.id.promptsJeuAn);
                final EditText jeuResume = (EditText) layout.findViewById(R.id.promptsJeuResume);

                RecyclerView list = (RecyclerView) layout.findViewById(R.id.listThemes);
                list.setLayoutManager(new LinearLayoutManager(this));

                //list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
                //list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
                //FAIRE AVEC JSON GET ALL THEMES list.setAdapter(new JeuAdapter() {});

                list.setAdapter(new ThemeAdapter(ThemeTest) {
                    @Override
                    public void onItemClick(View v) {
                        if(((ColorDrawable)list.getChildViewHolder(v).itemView.getBackground()).getColor()==Color.WHITE) {
                            themeSelect.add(ThemeTest.get(list.getChildViewHolder(v).getAdapterPosition()));
                            list.getChildViewHolder(v).itemView.setBackgroundColor(Color.GREEN);
                        }
                        else{
                            themeSelect.remove(ThemeTest.get(list.getChildViewHolder(v).getAdapterPosition()));
                            list.getChildViewHolder(v).itemView.setBackgroundColor(Color.WHITE);
                        }
                    }

                    @Override
                    public void onItemLongClick(View v) {
                        //rien
                    }
                });

                builder.setView(layout)
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String sJeuName = jeuName.getText().toString();
                                int sJeuAn = Integer.parseInt(jeuAn.getText().toString());
                                String sJeuResume = jeuResume.getText().toString();
                                storage.getJeux().add(new Jeu(themeSelect,sJeuName,sJeuAn,sJeuResume));
                                storage.saveJeux();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        })
                        .create()
                        .show();
                return true;

            //Si on veux ajouter un thème
            case R.id.menu_theme_add:
                View layoutThemeAdd = inflater.inflate(R.layout.prompts_add_theme,null);
                AlertDialog.Builder builderThemeAdd = new AlertDialog.Builder(this);
                final EditText themeName = (EditText) layoutThemeAdd.findViewById(R.id.promptsThemeName);
                builderThemeAdd.setView(layoutThemeAdd)
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String temp = themeName.getText().toString();
                                storage.getThemes().add(new Theme(temp));
                                storage.saveThemes();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        })
                        .create()
                        .show();
                return true;

            //Si on veux supprimer un theme
            case R.id.menu_theme_delete:
                View layoutThemeDel = inflater.inflate(R.layout.prompts_delete_theme,null);
                AlertDialog.Builder builderThemeDel = new AlertDialog.Builder(this);

                RecyclerView listThemeDel = (RecyclerView) layoutThemeDel.findViewById(R.id.listThemesDel);
                listThemeDel.setLayoutManager(new LinearLayoutManager(this));
                //listThemeDel.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
                //FAIRE AVEC JSON GET ALL THEMES list.setAdapter(new JeuAdapter() {});
                listThemeDel.setAdapter(new ThemeAdapter(ThemeTest) {
                    @Override
                    public void onItemClick(View v) {
                        //rien
                    }

                    //Quand on click longtemps, on supprime le thème
                    @Override
                    public void onItemLongClick(View v) {
                        storage.getThemes().remove(ThemeTest.get(listThemeDel.getChildViewHolder(v).getAdapterPosition()));
                        this.notifyDataSetChanged();
                    }
                });

                builderThemeDel.setView(layoutThemeDel)
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //Puis on sauvegarde les thèmes
                                storage.saveThemes();
                            }
                        })
                        .create()
                        .show();
                return true;

            //Si on veux supprimer un jeu
            case R.id.menu_game_delete:
                View layoutJeuDel = inflater.inflate(R.layout.prompts_delete_theme,null);
                AlertDialog.Builder builderJeuDel = new AlertDialog.Builder(this);

                RecyclerView listJeuDel = (RecyclerView) layoutJeuDel.findViewById(R.id.listThemesDel);

                //listJeuDel.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
                listJeuDel.setLayoutManager(new LinearLayoutManager(this));
                //FAIRE AVEC JSON GET ALL THEMES list.setAdapter(new JeuAdapter() {});
                listJeuDel.setAdapter(new JeuAdapter(JeuTest) {
                    @Override
                    public void onItemClick(View v) {
                        //rien
                    }

                    //Quand on click longtemps, on supprime le jeu
                    @Override
                    public void onItemLongClick(View v) {
                        storage.getJeux().remove(JeuTest.get(listJeuDel.getChildViewHolder(v).getAdapterPosition()));
                        this.notifyDataSetChanged();
                    }
                });
                builderJeuDel.setView(layoutJeuDel)
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //Puis on sauvegarde la liste
                                storage.saveJeux();
                            }
                        })
                        .create()
                        .show();
                return true;

            //TODO Faire modify !!!

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}