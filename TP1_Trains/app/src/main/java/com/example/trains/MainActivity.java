package com.example.trains;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.trains.utils.ApiClient;
import com.example.trains.utils.CityData;
import com.example.trains.utils.SncfApi;
import com.example.trains.utils.StopArea;
import com.example.trains.utils.StopAreasResponse;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private AutoCompleteTextView departureField, arrivalField;
    private String datetime1, datetime2;

    @Override
    public void onCreate(Bundle saveInstanceState) {
        EdgeToEdge.enable(this);
        super.onCreate(saveInstanceState);
        setContentView(R.layout.main_layout);
        ConstraintLayout parent = findViewById(R.id.main_layout);

        TextInputLayout departureInput = findViewById(R.id.departure_search_bar);
        departureInput.setHint(R.string.departure_label);
        TextInputLayout arrivalInput = findViewById(R.id.arrival_search_bar);
        arrivalInput.setHint(R.string.arrival_label);

        AutoCompleteTextView departureField = (AutoCompleteTextView) departureInput.getEditText();
        AutoCompleteTextView arrivalField = (AutoCompleteTextView) arrivalInput.getEditText();
        this.departureField = departureField;
        this.arrivalField = arrivalField;

        List<AutoCompleteTextView> gares = new ArrayList<>();

        gares.add(departureField);
        gares.add(arrivalField);
        assert departureField != null && arrivalField != null;

        TextInputLayout firstDate_layout = findViewById(R.id.first_date);
        firstDate_layout.setHint(R.string.first_date_label);
        AutoCompleteTextView firstDate = (AutoCompleteTextView) firstDate_layout.getEditText();
        TextInputLayout secondDate_layout = findViewById(R.id.second_date);
        secondDate_layout.setHint(R.string.second_date_label);
        AutoCompleteTextView secondDate = (AutoCompleteTextView) secondDate_layout.getEditText();
        assert firstDate != null && secondDate != null;

        ArrayList<AutoCompleteTextView> dates = new ArrayList<>();
        dates.add(firstDate);
        dates.add(secondDate);

        FloatingActionButton fab = findViewById(R.id.basket);
        fab.setOnClickListener(view ->{
            Snackbar.make(view, "Vous avez cliqué sur la panier", Snackbar.LENGTH_SHORT)
                    .setAnchorView(R.id.bottom_bar)
                    .show();
        });

        StringBuilder datetime1 = new StringBuilder();
        StringBuilder datetime2 = new StringBuilder();


        dates.forEach(date -> {
            date.setFocusable(false);
            date.setClickable(true);
            date.setHorizontallyScrolling(false);
            date.setMaxLines(2);

            date.setOnClickListener(view -> {
                MaterialDatePicker datePicker = MaterialDatePicker.Builder.datePicker()
                        .setTitleText(R.string.first_date_label)
                        .build();
                datePicker.addOnPositiveButtonClickListener(selection -> {
                    MaterialTimePicker timePicker = new MaterialTimePicker.Builder()
                            .setTimeFormat(TimeFormat.CLOCK_24H)
                            .setHour(Calendar.getInstance().get(Calendar.HOUR_OF_DAY))
                            .setTitleText("Sélectionnez l'heure")
                            .build();
                    Date Ldate = new Date((long) selection);
                    String dateString = String.format(Locale.getDefault(), "%02d/%02d/%04d", Ldate.getDate(), Ldate.getMonth() + 1, Ldate.getYear() + 1900);
                    date.setText(dateString);
                    timePicker.addOnPositiveButtonClickListener(v ->{
                        int hour = timePicker.getHour();
                        int minute = timePicker.getMinute();

                        String timeString = String.format(Locale.getDefault(), "%02dh%02d", hour, minute);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss",Locale.getDefault());
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis((long)selection);
                        calendar.set(Calendar.HOUR_OF_DAY, hour);
                        calendar.set(Calendar.MINUTE, minute);
                        calendar.set(Calendar.SECOND, 0);


                        if (date == firstDate){
                            if (!datetime1.toString().isEmpty()){
                                datetime1.setLength(0);
                            }
                            datetime1.append(dateFormat.format(calendar.getTime()));
                            this.datetime1 = datetime1.toString();

                        }else{
                            if (!datetime2.toString().isEmpty()){
                                datetime2.setLength(0);
                            }
                            datetime2.append(dateFormat.format(calendar.getTime()));
                            this.datetime2 = datetime2.toString();
                        }

                        date.setText(date.getText()+" "+getString(R.string.at)+" "+timeString);

                    });
                    timePicker.show(getSupportFragmentManager(), "MATERIAL_TIME_PICKER");
                });
                datePicker.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");

            });
        });

        Button swapper = findViewById(R.id.swap_button);
        Button searchButton = findViewById(R.id.search_button);

        SncfApi api = ApiClient.getRetrofit().create(SncfApi.class);


        final List<String> suggestions = CityData.getMajorFrenchCities();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, suggestions);

        gares.forEach(autoCompleteTextView -> {
            autoCompleteTextView.setAdapter(adapter);
            autoCompleteTextView.setThreshold(2);

            autoCompleteTextView.setDropDownAnchor(autoCompleteTextView.getId());
            autoCompleteTextView.setDropDownBackgroundDrawable(getDrawable(R.drawable.dropdown_border));

            autoCompleteTextView.setOnClickListener(view -> {
                autoCompleteTextView.clearFocus();
            });

            autoCompleteTextView.setOnFocusChangeListener((view, hasFocus)->{
                if (hasFocus && autoCompleteTextView.getText().toString().isEmpty()) {
                    autoCompleteTextView.showDropDown();
                }
                if (!hasFocus) {
                    String currentText = autoCompleteTextView.getText().toString();
                    boolean isValid = false;
                    for (String suggestion : suggestions) {
                        if (suggestion.equalsIgnoreCase(currentText.trim())) {
                            isValid = true;
                            break;
                        }
                    }
                    if (!isValid) {
                        autoCompleteTextView.setText("");
                    }
                }
            });
        });

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                String txt1 = departureField.getText().toString().trim();
                String txt2 = arrivalField.getText().toString().trim();
                String txt3 = firstDate.getText().toString().trim();
                swapper.setEnabled(!txt1.isEmpty() || !txt2.isEmpty());
                searchButton.setEnabled(!txt1.isEmpty() && !txt2.isEmpty() && !txt3.isEmpty());

                if (!txt1.isEmpty() && txt1.length()>=2){
                    departureField.showDropDown();
                }
                if (!txt2.isEmpty() && txt2.length()>=2){
                    arrivalField.showDropDown();
                }
                if(!txt1.isEmpty()&&txt1.equals(txt2)){
                    departureField.setError("Les deux champs ne peuvent pas être identiques.");
                    arrivalField.setError("Les deux champs ne peuvent pas être identiques.");
                }else{
                    departureField.setError(null);
                    arrivalField.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };



        departureField.addTextChangedListener(textWatcher);
        arrivalField.addTextChangedListener(textWatcher);
        firstDate.addTextChangedListener(textWatcher);



        swapper.setOnClickListener(v -> {
            departureField.dismissDropDown();
            arrivalField.dismissDropDown();
            String temp = String.valueOf(departureField.getText());
            departureField.setText(arrivalField.getText());
            arrivalField.setText(temp);
            departureField.clearFocus();
            arrivalField.clearFocus();
        });
        BottomNavigationView bottomBar = findViewById(R.id.bottom_bar);
        bottomBar.setOnItemSelectedListener(item -> {
            if (item.getItemId()!=R.id.travel_button){
                Snackbar.make(bottomBar, R.string.snack_error, Snackbar.LENGTH_SHORT)
                        .setAnchorView(bottomBar)
                        .show();
            }
            return false;
        });

        searchButton.setOnClickListener(view -> {
            HashMap<String, String> map = new HashMap<>();
            AtomicInteger completedRequests = new AtomicInteger(0);
            Call<StopAreasResponse> call1 = api.getPlaces(departureField.getText().toString(), 1);
            Call<StopAreasResponse> call2 = api.getPlaces(arrivalField.getText().toString(), 1);

            Callback<StopAreasResponse> callback = new Callback<StopAreasResponse>(){

                @Override
                public void onResponse(Call<StopAreasResponse> call, Response<StopAreasResponse> response) {
                    if (response.isSuccessful() && response.body() != null && !response.body().getStops().isEmpty()) {
                        String query = call == call1 ? departureField.getText().toString() : arrivalField.getText().toString();
                        map.put(query, response.body().getStops().get(0).getId());
                    }
                    // Vérifier si les deux requêtes sont complètes
                    if (completedRequests.incrementAndGet() == 2) {
                        launchSecondActivity(map);
                    }
                }

                @Override
                public void onFailure(Call<StopAreasResponse> call, Throwable t) {
                }
            };
            call1.enqueue(callback);
            call2.enqueue(callback);
        });

    }//Fin de onCreate

    private void launchSecondActivity(HashMap<String, String> map){

        Intent intent = new Intent(this, SecondActivity.class);
        intent.putExtra("departure_name", departureField.getText().toString());
        intent.putExtra("arrival_name", arrivalField.getText().toString());

        intent.putExtra("departure_id", map.get(departureField.getText().toString()));
        intent.putExtra("arrival_id", map.get(arrivalField.getText().toString()));

        intent.putExtra("departure_time", datetime1.toString());
        Log.d("PARAMETRES", departureField.getText().toString()+" - "+arrivalField.getText().toString());
        Log.d("IDS",map.get(departureField.getText().toString())+" - "+map.get(arrivalField.getText().toString()));
        long timeframe = -1L;
        if (datetime2 != null){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmm");

            LocalDateTime dateTime1Parsed = LocalDateTime.parse(datetime1.toString(), formatter);
            LocalDateTime dateTime2Parsed = LocalDateTime.parse(datetime2.toString(), formatter);

            long duration = Duration.between(dateTime1Parsed, dateTime2Parsed).getSeconds();

        }
        intent.putExtra("timeframe", timeframe);

        //Snackbar.make(findViewById(R.id.main_layout), map.get(departureField.getText().toString()), Snackbar.LENGTH_SHORT).setAnchorView(R.id.bottom_bar).show();
        launcher.launch(intent);

    }

    private final ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        String retour = data.getStringExtra("message_retour");
                        assert retour != null;
                        Snackbar.make(findViewById(R.id.main_layout), retour, Snackbar.LENGTH_SHORT)
                                .setAnchorView(R.id.bottom_bar)
                                .show();
                    }
                }
                else if (result.getResultCode() == RESULT_CANCELED){
                    Intent data = result.getData();
                    if (data != null){
                        String retour = data.getStringExtra("message_retour");
                        assert retour != null;
                        
                        Snackbar.make(findViewById(R.id.main_layout), retour, Snackbar.LENGTH_SHORT)
                                .setAnchorView(R.id.bottom_bar)
                                .show();
                    }
                }
            }
    );

}
