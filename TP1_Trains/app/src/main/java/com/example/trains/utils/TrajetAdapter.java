package com.example.trains.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trains.R;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class TrajetAdapter extends RecyclerView.Adapter<TrajetAdapter.TrajetViewHolder> {

    private List<StopAreasResponse.Trajet> trajets;
    private OnTrajetClickListener listener;

    public TrajetAdapter(List<StopAreasResponse.Trajet> trajets, OnTrajetClickListener listener) {
        this.trajets = trajets;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TrajetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_train, parent, false);
        return new TrajetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrajetViewHolder holder, int position) {
        StopAreasResponse.Trajet trajet = trajets.get(position);
        holder.bind(trajet, listener);

    }

    @Override
    public int getItemCount() {
        return trajets.size();
    }



    static class TrajetViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView cardView;
        TextView tvDepart, tvArrivee, tvPrice, tvDuration;

        public TrajetViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card);
            tvDepart = itemView.findViewById(R.id.depart);
            tvArrivee = itemView.findViewById(R.id.arrivee);
            tvPrice = itemView.findViewById(R.id.price);
            tvDuration = itemView.findViewById(R.id.duration);
        }

        public void bind(final StopAreasResponse.Trajet trajet, final OnTrajetClickListener listener){
            tvDepart.setText(trajet.getDepart());
            tvArrivee.setText(trajet.getArrivee());
            tvPrice.setText(trajet.getPrix());
            tvDuration.setText(trajet.getDuree());
            cardView.setOnClickListener(view -> {
                MaterialCardView card = (MaterialCardView) view;
                if (listener != null) {
                    listener.onTrajetClick(trajet);
                }
                card.setClickable(true);
                card.setFocusable(true);
                card.setCheckable(true);
                card.setChecked(!card.isChecked());
            });
        }
    }
    public interface OnTrajetClickListener {
        void onTrajetClick(StopAreasResponse.Trajet trajet);
    }

}
