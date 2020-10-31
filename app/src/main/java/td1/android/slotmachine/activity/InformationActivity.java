package td1.android.slotmachine.activity;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import td1.android.slotmachine.R;
import td1.android.slotmachine.adapter.ThemeAdapter;
import td1.android.slotmachine.model.Jeu;
import td1.android.slotmachine.model.Theme;

public class InformationActivity extends AppCompatActivity {
    private Jeu jeu;
    public static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final String JPG = ".jpg";

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

        //Récuparation de l'image de fond si existant
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = cw.getDir("images", Context.MODE_PRIVATE);
        File file = new File(directory, jeu.getNom().replace(' ', '_') + JPG);
        if(file.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            ImageView myImage = (ImageView) findViewById(R.id.information_game);
            myImage.setImageBitmap(myBitmap);
        }

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
            String url = "https://fr.wikipedia.org/wiki/" + jeu.getNom().replace(' ', '_');
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        });

        //Pour appareil photo
        findViewById(R.id.information_game).setOnClickListener((v) -> {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
        });

        //Pour renvoyer sur la page d'accueil
        findViewById(R.id.information_replay).setOnClickListener((v)-> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        });
    }

    //On récupère l'image (prise en photo) puis on la sauvegarde
    @Override
    protected void onActivityResult(int request, int result, Intent data) {
        super.onActivityResult(request, result, data);
        if (request == REQUEST_IMAGE_CAPTURE && result == RESULT_OK) {
            //On remplace l'image
            ImageView img = (ImageView) findViewById(R.id.information_game);
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            img.setImageBitmap(imageBitmap);

            //Et on la sauvegarde
            ContextWrapper cw = new ContextWrapper(getApplicationContext());
            File directory = cw.getDir("images", Context.MODE_PRIVATE);
            File file = new File(directory, jeu.getNom().replace(' ', '_') + JPG);
            Log.d("path", file.toString());
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(file);
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.flush();
                fos.close();
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
        }
    }
}