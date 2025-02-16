package com.example.agenda;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.applandeo.materialcalendarview.CalendarDay;
import com.example.agenda.entity.Evenement;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;


import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class AjoutActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ajout_layout);
        Intent intent = getIntent();
        int jour = intent.getIntExtra("jour", 0);
        int mois_int = intent.getIntExtra("mois", 0);
        int annee = intent.getIntExtra("annee", 0);

        String mois = new DateFormatSymbols().getMonths()[mois_int-1];

        MaterialToolbar toolbar = findViewById(R.id.tool_bar);
        toolbar.setNavigationOnClickListener(v -> {
            finish();
        });

        TextView date = findViewById(R.id.text_date);
        date.setText(jour + " " + mois + " " + annee);

        TextInputEditText frominput = findViewById(R.id.from);
        frominput.setText(Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + ":00");
        TextInputEditText toinput = findViewById(R.id.to);
        toinput.setText((Calendar.getInstance().get(Calendar.HOUR_OF_DAY)+1) + ":00");

        TextInputEditText nameInput = findViewById(R.id.name_input);

        ArrayList<TextInputEditText> inputs = new ArrayList<>();
        inputs.add(frominput);
        inputs.add(toinput);

        inputs.forEach(textinput -> {
            textinput.setFocusable(false);
            textinput.setClickable(true);
            textinput.setHorizontallyScrolling(false);
            textinput.setMaxLines(2);
            textinput.setOnClickListener(view -> {
                MaterialTimePicker timePicker = new MaterialTimePicker.Builder()
                        .setTimeFormat(TimeFormat.CLOCK_24H)
                        .setHour(Calendar.getInstance().get(Calendar.HOUR_OF_DAY))
                        .setTitleText("Sélectionnez l'heure")
                        .build();

                timePicker.addOnPositiveButtonClickListener(v -> {
                    String timeString = String.format(Locale.getDefault(), "%02d:%02d", timePicker.getHour(), timePicker.getMinute());
                    textinput.setText(timeString);
                });
                timePicker.show(getSupportFragmentManager(), "MATERIAL_TIME_PICKER");
                //Snackbar.make(findViewById(R.id.add_layout), "test", Snackbar.LENGTH_SHORT).show();
            });
        });
        HashMap<String, Integer> categories = new HashMap<>();
        categories.put("Quotidien", R.drawable.schedule_24dp_e8eaed_fill0_wght400_grad0_opsz24);
        categories.put("Travail", R.drawable.work_24dp_e8eaed_fill0_wght400_grad0_opsz24);
        categories.put("Sport",  R.drawable.sprint_24dp_e8eaed_fill0_wght400_grad0_opsz24);
        categories.put("Autre", R.drawable.question_exchange_24dp_e8eaed_fill0_wght400_grad0_opsz24);

        String[] choices = categories.keySet().toArray(new String[0]);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, choices);
        MaterialAutoCompleteTextView category = findViewById(R.id.category);
        category.setAdapter(adapter);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                 String title = nameInput.getText().toString().trim();
                 String from = frominput.getText().toString().trim();
                 String to = toinput.getText().toString().trim();
                 String categoryText = category.getText().toString().trim();
                if (!title.isEmpty() && !from.isEmpty() && !to.isEmpty() && !categoryText.isEmpty()) {
                    findViewById(R.id.create_button).setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };

        nameInput.addTextChangedListener(textWatcher);
        frominput.addTextChangedListener(textWatcher);
        toinput.addTextChangedListener(textWatcher);
        category.addTextChangedListener(textWatcher);
        TextInputEditText description = findViewById(R.id.description);

        Button createButton = findViewById(R.id.create_button);
        createButton.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            calendar.set(annee,mois_int-1,jour);

            CalendarDay calendarDay = new CalendarDay(calendar);
            Log.d("JOURACTIVITY", ""+calendarDay.getCalendar().get(Calendar.DAY_OF_MONTH));

            String descriptionString = (description.getText().length() == 0? "" : description.getText().toString());
            String[] horaire = frominput.getText().toString().split(":");
            int hour =  Integer.parseInt(horaire[0]);
            int minute = Integer.parseInt(horaire[1]);

            Evenement evenement = new Evenement(calendarDay, nameInput.getText().toString(), descriptionString, category.getText().toString(), hour, minute, categories.get(category.getText().toString()));
            Intent resultIntent = new Intent();
            resultIntent.putExtra("nouvelEvenement", evenement.toString()); // ou en tant qu'objet Serializable/Parcelable
            setResult(RESULT_OK, resultIntent);
            finish();
            //Snackbar.make(findViewById(R.id.add_layout), category.getText().toString()+" "+nameInput.getText().toString()+" "+frominput.getText().toString()+" "+toinput.getText().toString(), Snackbar.LENGTH_SHORT).show();
        });
    }
}
