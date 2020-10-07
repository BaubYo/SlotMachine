package td1.android.slotmachine.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import td1.android.slotmachine.R;
import td1.android.slotmachine.model.Theme;

public abstract class JeuAdapter  extends RecyclerView.Adapter<JeuAdapter.JeuHolder>  {

    //Classe holder
    public class JeuHolder extends RecyclerView.ViewHolder {
        public TextView nom;

        public JeuHolder(@NonNull View itemView) {
            super(itemView);
            nom = itemView.findViewById(R.id.theme_layout_information_name);
        }
    }

    List<Theme> liste;

    public JeuAdapter(List<Theme> liste) {
        this.liste = liste;
    }

    //Récupération des données
    @NonNull
    @Override
    public JeuHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.theme_layout_information, parent, false);
        return new JeuHolder(view);
    }

    //Lien entre données et vue
    @Override
    public void onBindViewHolder(@NonNull JeuHolder holder, int position) {
        holder.nom.setText(liste.get(position).getName());
    }

    //Nombres de thèmes
    @Override
    public int getItemCount() {
        return liste.size();
    }

}
