package com.example.trains;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trains.utils.ApiClient;
import com.example.trains.utils.JourneysReponse;
import com.example.trains.utils.SncfApi;
import com.example.trains.utils.StopAreasResponse;
import com.example.trains.utils.TrajetAdapter;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SecondActivity extends AppCompatActivity {
    private int selected;
    @Override
    public void onCreate(Bundle saveInstanceState){
        EdgeToEdge.enable(this);
        super.onCreate(saveInstanceState);
        setContentView(R.layout.second_activity);
        Intent intent = getIntent();

        String departure = intent.getStringExtra("departure_name");
        String arrival = intent.getStringExtra("arrival_name");

        String departure_id = intent.getStringExtra("departure_id");
        String arrival_id = intent.getStringExtra("arrival_id");

        String departureTime = intent.getStringExtra("departure_time");
        long timeframe = intent.getLongExtra("timeframe", -1L);

        SncfApi api = ApiClient.getRetrofit().create(SncfApi.class);

        BottomNavigationView bottomBar = findViewById(R.id.bottom_bar);
        bottomBar.setOnItemSelectedListener(item -> {
            if (item.getItemId()!=R.id.travel_button){
                Snackbar.make(bottomBar, R.string.snack_error, Snackbar.LENGTH_SHORT)
                        .setAnchorView(bottomBar)
                        .show();
            }
            return false;
        });

        AppBarLayout toolbar_layout = findViewById(R.id.top_bar);
        /*MaterialToolbar toolbar = (MaterialToolbar) toolbar_layout.getChildAt(0);
        toolbar.setNavigationOnClickListener(view -> {
            Intent cancelIntent = new Intent();
            cancelIntent.putExtra("message_retour", "Recherche annulée");
            setResult(RESULT_CANCELED, cancelIntent);
            finish();
        });*/

        RecyclerView recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ExtendedFloatingActionButton fab = findViewById(R.id.fab);



        //Snackbar.make(findViewById(R.id.second_activity), departure+"-"+arrival+"-"+departureTime, Snackbar.LENGTH_LONG).setAnchorView(R.id.bottom_bar).show();
        Call<JourneysReponse> call;
        if (timeframe==-1L){
            call = api.getTrajets(departure_id,arrival_id,departureTime,15);
        }else{
            call = api.getTrajets(departure_id,arrival_id,departureTime,timeframe);
        }
        call.enqueue(new Callback<JourneysReponse>() {
            @Override
            public void onResponse(Call<JourneysReponse> call, Response<JourneysReponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                   // Snackbar.make(findViewById(R.id.second_activity), "Call a fonctionné", Snackbar.LENGTH_SHORT).setAnchorView(R.id.bottom_bar).show();
                    List<StopAreasResponse.Trajet> trajets = response.body().getTrajets();
                    if (trajets == null) {
                        trajets = new ArrayList<>();
                        //Snackbar.make(findViewById(R.id.second_activity), ""+response.body(), Snackbar.LENGTH_SHORT).setAnchorView(R.id.bottom_bar).show();
                    }
                    trajets.forEach(trajet -> {
                        trajet.setDepart(departure);
                        trajet.setArrivee(arrival);
                        long timeSeconds = Long.parseLong(trajet.getDuree());
                        Time time = new Time(timeSeconds*1000);
                        trajet.setDuree(time.toString());
                    });
                    TrajetAdapter adapter = new TrajetAdapter(trajets, trajet -> {
                        fab.setEnabled(true);
                        fab.setOnClickListener(view -> {

                            Intent resultIntent = new Intent();
                            resultIntent.putExtra("message_retour", "Trajets ajoutés au panier !");
                            setResult(RESULT_OK, resultIntent);
                            finish();
                        });
                    });

                    recyclerView.setAdapter(adapter);

                }else {
                    // Par exemple, afficher le code d'erreur et/ou le message de la réponse
                    String errorMessage = "Erreur de call: " + response.code();
                    if(response.errorBody() != null) {
                        // On pourrait extraire plus d'information de errorBody si besoin
                        errorMessage += " " + response.errorBody().toString();
                    }
                    Log.e("API", "Call non successful: code = " + response.code() + " errorBody = " + response.errorBody().toString());

                }
            }

            @Override
            public void onFailure(Call<JourneysReponse> call, Throwable t) {
                Snackbar.make(findViewById(R.id.second_activity), t.getMessage(), Snackbar.LENGTH_SHORT).setAnchorView(R.id.bottom_bar).show();

            }
        });
    }
}
