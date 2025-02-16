package com.example.agenda;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.agenda.entity.Evenement;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;

public class BottomSheet extends BottomSheetDialogFragment {
    private int jour, mois, annee;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottom_sheet, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MaterialButton ajoutButton = view.findViewById(R.id.add_bouton);

        ajoutButton.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), AjoutActivity.class);
            intent.putExtra("jour", jour);
            intent.putExtra("mois", mois);
            intent.putExtra("annee", annee);
            startActivityForResult(intent, 1);
            //dismiss();
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String eventString = data.getStringExtra("nouvelEvenement");
        Evenement nouvelEvenement = Evenement.fromString(eventString); // À implémenter si nécessaire

        if (getActivity() instanceof MainActivity) {
            Snackbar.make(this.getView(), nouvelEvenement.getTitre(), Snackbar.LENGTH_LONG).show();
            ((MainActivity) getActivity()).addEvent(nouvelEvenement.getJour(),nouvelEvenement);
        }

        dismiss();
    }


    public void setJour(int jour) {
        this.jour = jour;
    }

    public void setMois(int mois) {
        this.mois = mois;
    }

    public void setAnnee(int annee) {
        this.annee = annee;
    }

    public int getJour() {
        return jour;
    }

    public int getMois() {
        return mois;
    }

    public int getAnnee() {
        return annee;
    }
}
