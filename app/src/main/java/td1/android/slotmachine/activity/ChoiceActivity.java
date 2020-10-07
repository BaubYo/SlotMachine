package td1.android.slotmachine.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import td1.android.slotmachine.R;
import td1.android.slotmachine.model.Jeu;

public class ChoiceActivity extends AppCompatActivity {
    private Jeu jeu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice);

        //Pour renlancer le jeu
        findViewById(R.id.choice_another).setOnClickListener((v) -> {
            //TODO masse code ici !
            jeu = new Jeu();
        });
    }
}