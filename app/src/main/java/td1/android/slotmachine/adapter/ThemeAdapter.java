package td1.android.slotmachine.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import td1.android.slotmachine.R;
import td1.android.slotmachine.model.Theme;

public abstract class ThemeAdapter  extends RecyclerView.Adapter<ThemeAdapter.ThemeHolder>  {

    //Classe holder
    public class ThemeHolder extends RecyclerView.ViewHolder {
        public TextView nom;
        public int color;

        public ThemeHolder(@NonNull View itemView) {
            super(itemView);
            nom = itemView.findViewById(R.id.theme_layout_information_name);
        }
    }

    List<Theme> liste;

    public ThemeAdapter(List<Theme> liste) {
        this.liste = liste;
    }

    //Récupération des données
    @NonNull
    @Override
    public ThemeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.theme_layout_information, parent, false);
        view.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                onItemClick(v);
            }
        });
        view.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v){
                onItemLongClick(v);
                return true;
            }
        });
        return new ThemeHolder(view);
    }

    //Lien entre données et vue
    @Override
    public void onBindViewHolder(@NonNull ThemeHolder holder, int position) {
        holder.nom.setText(liste.get(position).getNom());
        holder.nom.setBackgroundColor(liste.get(position).getCouleur());
        holder.itemView.setBackgroundColor(Color.WHITE);

    }

    //Nombres de thèmes
    @Override
    public int getItemCount() {
        return liste.size();
    }


    //Methode abstraite, donc à completer
    public abstract void onItemClick(View v);
    public abstract void onItemLongClick(View v);

}
