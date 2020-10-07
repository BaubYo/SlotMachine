package td1.android.slotmachine.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import td1.android.slotmachine.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Onclick sur le button levier envoie les themes ( A FAIRE ) et affiche la vue ChoiceActivity
        findViewById(R.id.MainSlotButton).setOnClickListener((v)-> {
            Intent intent = new Intent(getApplicationContext(),ChoiceActivity.class);
            //intent.putExtra(EXTRA_CONTACT,contacts.get(list.getChildViewHolder(v).getAdapterPosition()));
            //FAUDRA DONNER LES TROIS THEMES
            startActivity(intent);
        });
    }


}