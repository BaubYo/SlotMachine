package td1.android.slotmachine.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import td1.android.slotmachine.R;

public class InformationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        //Pour Renvoyer sur la page wikipedia
        findViewById(R.id.information_wiki).setOnClickListener((v) -> {
            String url = "https://fr.wikipedia.org/wiki/Wikip%C3%A9dia:Accueil_principal"; //TODO à changer
            // exemple https://fr.wikipedia.org/wiki/The_Witcher_3:_Wild_Hunt
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        });
    }
}