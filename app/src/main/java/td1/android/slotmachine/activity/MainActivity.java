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
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

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

    //Storage est la pour recuperer les themes et les jeux
    protected JsonStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //On récupère les données depuis le JSON
        storage = new JsonStorage(getBaseContext());



        //Quand on baisse le levier, et quand il atteint le bout, on lance le tirage de thème aléatoire
        SeekBar seekBar = (SeekBar)findViewById(R.id.launch_machine);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            //Dès que la valeur change
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(progress >= 95)
                {
                    //Si les trois slot ne tournent pas alors on enleve la boucle et on reset/cancel le timer puis on relance la boucle
                    //Si on ne remove pas le runnable alors on peut lancer des boucles sur boucles
                    if (!Slot1Continue && !Slot2Continue && !Slot3Continue) {
                        handler.removeCallbacks(runnable);
                        countDownTimer.cancel();
                        ((TextView)findViewById(R.id.countDown)).setText("");
                        handler.post(runnable);
                    }
                    //permet de refaire fonctionner toutes les cases
                    Slot1Continue = true;
                    Slot2Continue = true;
                    Slot3Continue = true;
                }
            }
            //Quand on commence a appuyer sur la seekbar
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // rien
            }
            //Quand on relache la seekbar (on remet à 0 la valeur)
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                seekBar.setProgress(0);
            }
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

    //Boucle toute les 20 ms qui s'execute en parrallele de la main thread
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            //Changer le nom dans les cases
            roulette(storage.getThemes());
            //Relance cette boucle dans 20ms
            handler.postDelayed(runnable, 20);
            if (!Slot1Continue && !Slot2Continue && !Slot3Continue) {
                //Si jamais les trois sont stopper alors lance le timer en enlevant le runnable pour ne pas cause de boucle qui tourne en meme temps
                handler.removeCallbacks(runnable);
                timerEndRoulette();
            }
        }
    };

    //Compteur jusqu'à 0 en commencant à 3 (meme si on commence a 3,6 sec pour que le temps que l'affichage se fasse cela indique bien 3)
    CountDownTimer countDownTimer= new CountDownTimer(3600, 900)
    {
        //affichage du temps restant
        public void onTick(long millisUntilFinished) {
            ((TextView)findViewById(R.id.countDown)).setText(millisUntilFinished / 1000 + " secondes restantes");
        }

        //Quand le delay est terminé, on envoi les thèmes selectionné avec Intent dans l'activity ChoiceActivity
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
    protected void roulette(List<Theme> ThemeListe){
        Random rand = new Random();
        if (Slot1Continue) {
            int randSlot1 = rand.nextInt(ThemeListe.size());
            ((TextView) findViewById(R.id.Slot1)).setText(ThemeListe.get(randSlot1).getNom());
            (findViewById(R.id.Slot1)).setBackgroundColor(ThemeListe.get(randSlot1).getCouleur());
        }
        if (Slot2Continue) {
            int randSlot2 = rand.nextInt(ThemeListe.size());
            ((TextView) findViewById(R.id.Slot2)).setText(ThemeListe.get(randSlot2).getNom());
            (findViewById(R.id.Slot2)).setBackgroundColor(ThemeListe.get(randSlot2).getCouleur());
        }
        if (Slot3Continue) {
            int randSlot3 = rand.nextInt(ThemeListe.size());
            ((TextView) findViewById(R.id.Slot3)).setText(ThemeListe.get(randSlot3).getNom());
            (findViewById(R.id.Slot3)).setBackgroundColor(ThemeListe.get(randSlot3).getCouleur());
        }
    }
    //Active le Timer
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
        //Instancie un inflater
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        //Switch sur le choix du menu
        switch (item.getItemId()) {
            //Si on veux ajouter un jeu
            case R.id.menu_game_add:
                //Preparer une liste de theme qui vont etre selectionner
                List<Theme> themeSelect=new ArrayList<Theme>();
                //Choisis le layout et creer un builder pour les fenetre de dialogue
                View layout = inflater.inflate(R.layout.prompts_add_jeu,null);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                //Facilite acces vers les donnes qu'on devrai save
                final EditText jeuName = (EditText) layout.findViewById(R.id.promptsJeuName);
                final EditText jeuAn = (EditText) layout.findViewById(R.id.promptsJeuAn);
                final EditText jeuResume = (EditText) layout.findViewById(R.id.promptsJeuResume);
                //Creer une RecyclerView pour la liste des themes et lui attribue une adpter
                RecyclerView list = (RecyclerView) layout.findViewById(R.id.listThemes);
                list.setLayoutManager(new LinearLayoutManager(this));
                list.setAdapter(new ThemeAdapter(storage.getThemes()) {
                    //Si l'item est Blanc alors ajoute le a la liste des themes selectionnées et le met en vert
                    //Sinon le mets en Blanc et le remove de la liste des themes selectionnées
                    @Override
                    public void onItemClick(View v) {
                        if(((ColorDrawable)list.getChildViewHolder(v).itemView.getBackground()).getColor()==Color.WHITE) {
                            themeSelect.add(storage.getThemes().get(list.getChildViewHolder(v).getAdapterPosition()));
                            list.getChildViewHolder(v).itemView.setBackgroundColor(Color.GREEN);
                        }
                        else{
                            themeSelect.remove(storage.getThemes().get(list.getChildViewHolder(v).getAdapterPosition()));
                            list.getChildViewHolder(v).itemView.setBackgroundColor(Color.WHITE);
                        }
                    }

                    @Override
                    public void onItemLongClick(View v) {
                        //rien
                    }
                });

                //Sauvegarde + affichage du toast
                builder.setView(layout)
                        .setCancelable(false)
                        .setPositiveButton(R.string.app_button_ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String sJeuName = jeuName.getText().toString();
                                int sJeuAn = Integer.parseInt(jeuAn.getText().toString());
                                String sJeuResume = jeuResume.getText().toString();
                                storage.getJeux().add(new Jeu(themeSelect,sJeuName,sJeuAn,sJeuResume));
                                storage.saveJeux();
                                Toast.makeText(getApplicationContext(), sJeuName + " à été ajouté", Toast.LENGTH_SHORT).show(); //Affichage d'un Toast
                            }
                        })
                        .setNegativeButton(R.string.app_button_cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        })
                        .create()
                        .show();
                return true;

            //Si on veux ajouter un thème
            case R.id.menu_theme_add:
                //Choisis le layout et creer un builder pour les fenetre de dialogue
                View layoutThemeAdd = inflater.inflate(R.layout.prompts_add_theme,null);
                AlertDialog.Builder builderThemeAdd = new AlertDialog.Builder(this);
                //Facilite acces vers les donnes qu'on devrai save
                final EditText themeName = (EditText) layoutThemeAdd.findViewById(R.id.promptsThemeName);
                builderThemeAdd.setView(layoutThemeAdd)
                        .setCancelable(false)
                        .setPositiveButton(R.string.app_button_ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String temp = themeName.getText().toString();
                                storage.getThemes().add(new Theme(temp));
                                storage.saveThemes();
                                Toast.makeText(getApplicationContext(), temp + " à été ajouté", Toast.LENGTH_SHORT).show(); //Affichage d'un Toast
                            }
                        })
                        .setNegativeButton(R.string.app_button_cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        })
                        .create()
                        .show();
                return true;

            //Si on veux supprimer un theme
            case R.id.menu_theme_delete:
                //Choisis le layout et creer un builder pour les fenetre de dialogue
                View layoutThemeDel = inflater.inflate(R.layout.prompts_delete_theme,null);
                AlertDialog.Builder builderThemeDel = new AlertDialog.Builder(this);
                //Preparer une liste de theme qui vont etre potentiellement delete
                RecyclerView listThemeDel = (RecyclerView) layoutThemeDel.findViewById(R.id.listThemesDel);
                listThemeDel.setLayoutManager(new LinearLayoutManager(this));

                listThemeDel.setAdapter(new ThemeAdapter(storage.getThemes()) {
                    @Override
                    public void onItemClick(View v) {
                        //rien
                    }

                    //Quand on click longtemps, on supprime le thème
                    @Override
                    public void onItemLongClick(View v) {
                        Theme ancienTheme = storage.getThemes().get(listThemeDel.getChildViewHolder(v).getAdapterPosition());
                        storage.getThemes().remove(ancienTheme);
                        this.notifyDataSetChanged();
                        Toast.makeText(getApplicationContext(), ancienTheme.getNom() + " à été supprimé", Toast.LENGTH_SHORT).show(); //Affichage d'un Toast
                    }
                });
                //Sauvegarde
                builderThemeDel.setView(layoutThemeDel)
                        .setCancelable(false)
                        .setPositiveButton(R.string.app_button_ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                storage.saveThemes();
                            }
                        })
                        .create()
                        .show();
                return true;

            //Si on veux supprimer un jeu
            case R.id.menu_game_delete:
                //Choisis le layout et creer un builder pour les fenetre de dialogue
                View layoutJeuDel = inflater.inflate(R.layout.prompts_delete_theme,null);
                AlertDialog.Builder builderJeuDel = new AlertDialog.Builder(this);
                //Preparer une liste de jeu qui vont etre potentiellement delete
                RecyclerView listJeuDel = (RecyclerView) layoutJeuDel.findViewById(R.id.listThemesDel);

                listJeuDel.setLayoutManager(new LinearLayoutManager(this));
                listJeuDel.setAdapter(new JeuAdapter(storage.getJeux()) {
                    @Override
                    public void onItemClick(View v) {
                        //rien
                    }

                    //Quand on click longtemps, on supprime le jeu
                    @Override
                    public void onItemLongClick(View v) {
                        Jeu ancienJeu = storage.getJeux().get(listJeuDel.getChildViewHolder(v).getAdapterPosition());
                        storage.getJeux().remove(ancienJeu);
                        this.notifyDataSetChanged();
                        Toast.makeText(getApplicationContext(), ancienJeu.getNom() + " à été supprimé", Toast.LENGTH_SHORT).show(); //Affichage d'un Toast
                    }
                });
                //Sauvegarde
                builderJeuDel.setView(layoutJeuDel)
                        .setCancelable(false)
                        .setPositiveButton(R.string.app_button_ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                storage.saveJeux();
                            }
                        })
                        .create()
                        .show();
                return true;

            //Si on veut modifier un theme
            case R.id.menu_theme_modify:
                //Choisis le layout et creer deux builder pour les fenetre de dialogue
                View layoutThemeModify = inflater.inflate(R.layout.prompts_modify_theme,null);
                AlertDialog.Builder builderThemeModify = new AlertDialog.Builder(this);
                AlertDialog.Builder builderThemeModify2 = new AlertDialog.Builder(this);
                //Preparer une liste de theme qui vont etre potentiellement modifier
                RecyclerView listThemeModify = (RecyclerView) layoutThemeModify.findViewById(R.id.listThemesDel);
                listThemeModify.setLayoutManager(new LinearLayoutManager(this));
                listThemeModify.setAdapter(new ThemeAdapter(storage.getThemes()) {
                    @Override
                    public void onItemClick(View v) {
                        //Creer une autre view par dessus pour modifier le text
                        View layoutThemeModify2 = inflater.inflate(R.layout.prompts_add_theme,null);
                        ((EditText)layoutThemeModify2.findViewById(R.id.promptsThemeName)).setText(storage.getThemes().get(listThemeModify.getChildViewHolder(v).getAdapterPosition()).getNom());
                        builderThemeModify2.setView(layoutThemeModify2)
                                .setCancelable(false)
                                .setPositiveButton(R.string.app_button_ok, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        String tempNomAncien=storage.getThemes().get(listThemeModify.getChildViewHolder(v).getAdapterPosition()).getNom();
                                        storage.getThemes().get(listThemeModify.getChildViewHolder(v).getAdapterPosition()).setNom(((EditText)layoutThemeModify2.findViewById(R.id.promptsThemeName)).getText().toString());
                                        //Fonction qui permet de faire en sorte que les jeux avec le Theme dont le nom a ete modifier ont le meme theme mais avec le nom deja modifier
                                        storage.changeThemeChaqueJeu(tempNomAncien,((EditText)layoutThemeModify2.findViewById(R.id.promptsThemeName)).getText().toString());
                                        listThemeModify.getAdapter().notifyDataSetChanged();
                                        Toast.makeText(getApplicationContext(), tempNomAncien + " à été modifié", Toast.LENGTH_SHORT).show(); //Affichage d'un Toast
                                    }
                                })
                                .setNegativeButton(R.string.app_button_cancel, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                })
                                .create()
                                .show();
                    }

                    @Override
                    public void onItemLongClick(View v) {
                        //storage.getThemes().remove(storage.getThemes().get(listThemeDel.getChildViewHolder(v).getAdapterPosition()));
                       //this.notifyDataSetChanged();

                        //rien
                    }
                });

                //Sauvegarde
                builderThemeModify.setView(layoutThemeModify)
                        .setCancelable(false)
                        .setPositiveButton(R.string.app_button_ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                storage.saveThemes();
                            }
                        })
                        .create()
                        .show();
                return true;

            //Si on veut modifier un jeu
            case R.id.menu_game_modify:
                List<Theme> themeSelectM=new ArrayList<Theme>();
                View layoutJeuModify = inflater.inflate(R.layout.prompts_delete_theme,null);

                //On créer plusieurs AlertDialog (selection puis modification)
                AlertDialog.Builder builderEcranSelection = new AlertDialog.Builder(this);
                AlertDialog.Builder builderEcranModification = new AlertDialog.Builder(this);
                //Preparer une liste de jeu qui vont etre potentiellement modifier
                RecyclerView listJeuModify = (RecyclerView) layoutJeuModify.findViewById(R.id.listThemesDel);
                listJeuModify.setLayoutManager(new LinearLayoutManager(this));

                listJeuModify.setAdapter(new JeuAdapter(storage.getJeux()) {
                    //Quand on clique on envoi le jeu selectionné vers la deuxième
                    @Override
                    public void onItemClick(View v) {
                        View layoutJeuModify2 = inflater.inflate(R.layout.prompts_modify_jeu,null);
                        //Facilite acces vers les donnes qu'on devrai save
                        final EditText jeuNomModify = (EditText) layoutJeuModify2.findViewById(R.id.promptsJeuName);
                        final EditText jeuAnModify = (EditText) layoutJeuModify2.findViewById(R.id.promptsJeuAn);
                        final EditText jeuResumeModify = (EditText) layoutJeuModify2.findViewById(R.id.promptsJeuResume);
                        //Set leur text de la valeur du jeu choisis
                        jeuNomModify.setText(storage.getJeux().get(listJeuModify.getChildViewHolder(v).getAdapterPosition()).getNom());
                        jeuAnModify.setText(String.valueOf(storage.getJeux().get(listJeuModify.getChildViewHolder(v).getAdapterPosition()).getAnnee()));
                        jeuResumeModify.setText(storage.getJeux().get(listJeuModify.getChildViewHolder(v).getAdapterPosition()).getResume());
                        //Montre sa liste de Themes
                        RecyclerView listJeuModify2 = (RecyclerView) layoutJeuModify2.findViewById(R.id.listThemes);
                        listJeuModify2.setLayoutManager(new LinearLayoutManager(null,LinearLayoutManager.VERTICAL,false));
                        listJeuModify2.setAdapter(new ThemeAdapter(storage.getThemes()) {
                            @Override
                            public void onItemClick(View v) {
                                //Mets en Vert si on l'ajoute au theme du jeu Sinon y met en blanc
                                if(((ColorDrawable)listJeuModify2.getChildViewHolder(v).itemView.getBackground()).getColor()==Color.WHITE) {
                                    themeSelectM.add(storage.getThemes().get(listJeuModify2.getChildViewHolder(v).getAdapterPosition()));
                                    listJeuModify2.getChildViewHolder(v).itemView.setBackgroundColor(Color.GREEN);
                                }
                                else{
                                    themeSelectM.remove(storage.getThemes().get(listJeuModify2.getChildViewHolder(v).getAdapterPosition()));
                                    listJeuModify2.getChildViewHolder(v).itemView.setBackgroundColor(Color.WHITE);
                                }
                            }

                            @Override
                            public void onItemLongClick(View v) {
                                //rien
                            }
                        });

                        //Sauvegarde + affichage du toast
                        builderEcranModification.setView(layoutJeuModify2)
                                .setCancelable(false)
                                .setPositiveButton(R.string.app_button_ok, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Jeu ancienJeu = storage.getJeux().get(listJeuModify.getChildViewHolder(v).getAdapterPosition());
                                        ancienJeu.setNom(jeuNomModify.getText().toString());
                                        ancienJeu.setAnnee(Integer.parseInt(jeuAnModify.getText().toString()));
                                        ancienJeu.setResume(jeuResumeModify.getText().toString());
                                        ancienJeu.setThemes(themeSelectM);

                                        listJeuModify.getAdapter().notifyDataSetChanged();
                                        storage.saveJeux();
                                        Toast.makeText(getApplicationContext(), ancienJeu.getNom() + " à été modifié", Toast.LENGTH_SHORT).show(); //Affichage d'un Toast
                                    }
                                })
                                .setNegativeButton(R.string.app_button_cancel, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                })
                                .create()
                                .show();
                    }

                    @Override
                    public void onItemLongClick(View v) {
                        //rien
                    }
                });
                //Sauvegarde
                builderEcranSelection.setView(layoutJeuModify)
                        .setCancelable(false)
                        .setPositiveButton(R.string.app_button_ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        })
                        .create()
                        .show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}