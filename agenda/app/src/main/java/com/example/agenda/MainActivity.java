package com.example.agenda;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.applandeo.materialcalendarview.CalendarDay;
import com.applandeo.materialcalendarview.CalendarView;

import com.applandeo.materialcalendarview.listeners.OnCalendarDayClickListener;
import com.example.agenda.entity.Evenement;
import com.google.android.material.snackbar.Snackbar;


import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private HashMap<String, List<Evenement>> evenementsParJour = new HashMap<>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        MainActivity mainActivity = this;

        CalendarView calendarView = findViewById(R.id.agenda);
        calendarView.setCalendarDayLayout(R.layout.day_card);

        Calendar calendar = Calendar.getInstance();
        CalendarDay calendarDaytest = new CalendarDay(calendar);
        Evenement test = new Evenement(calendarDaytest, "test","test","autre",17,0,R.drawable.priority_high_24dp_e8eaed_fill0_wght400_grad0_opsz24);
        addEvent(calendarDaytest, test);

        List<CalendarDay> calendarDays = new ArrayList<>();
        calendarDays.add(calendarDaytest);
        //calendarDaytest.setImageResource(test.getIconResId());

        calendarView.setCalendarDays(calendarDays);

        TextView welcome = findViewById(R.id.welcome_text);
        welcome.setText(welcome.getText().toString()+ "\n " + calendar.get(Calendar.DAY_OF_MONTH) + " " + new DateFormatSymbols().getMonths()[calendar.get(Calendar.MONTH)] + " " + calendar.get(Calendar.YEAR));


        calendarView.setOnCalendarDayClickListener(new OnCalendarDayClickListener() {
            @Override
            public void onClick(@NonNull CalendarDay calendarDay) {
                Calendar clickedDayCalendar = calendarDay.getCalendar();
                int day = clickedDayCalendar.get(Calendar.DAY_OF_MONTH);
                int month = clickedDayCalendar.get(Calendar.MONTH) + 1;
                int year = clickedDayCalendar.get(Calendar.YEAR);
                BottomSheet bottomSheet = new BottomSheet();
                bottomSheet.setJour(day);
                bottomSheet.setMois(month);
                bottomSheet.setAnnee(year);
                bottomSheet.show(getSupportFragmentManager(), "BottomSheet");
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ArrayList<String> eventList = new ArrayList<>();
        for (String date : evenementsParJour.keySet()) {
            for (Evenement event : evenementsParJour.get(date)) {
                eventList.add(date + ";" + event.toString());
            }
        }
        outState.putStringArrayList("evenements", eventList);
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        ArrayList<String> eventList = savedInstanceState.getStringArrayList("evenements");
        if (eventList != null) {
            evenementsParJour.clear();
            for (String entry : eventList) {
                String[] parts = entry.split(";");
                String date = parts[0];
                Evenement event = Evenement.fromString(parts[1]);
                evenementsParJour.putIfAbsent(date, new ArrayList<>());
                evenementsParJour.get(date).add(event);
            }
        }

        updateCalendar();
    }
    @Override
    protected void onResume() {
        super.onResume();
        updateCalendar();
    }


    private void updateCalendar() {
        CalendarView calendarView = findViewById(R.id.agenda);
        List<CalendarDay> calendarDays = new ArrayList<>();

        for (String key : evenementsParJour.keySet()){
            Calendar day = stringToDate(key);
            CalendarDay calendarDay = new CalendarDay(day);
            if (!evenementsParJour.get(key).isEmpty()) {
                calendarDay.setImageResource(R.drawable.priority_high_24dp_e8eaed_fill0_wght400_grad0_opsz24);
            }

            calendarDays.add(calendarDay);
        }
        calendarView.setCalendarDays(calendarDays);
    }

    public void addEvent(CalendarDay date, Evenement evenement){
        String key = dateToString(date);
        Log.d("JOUR", date.getCalendar().get(Calendar.DAY_OF_MONTH) + " " + date.getCalendar().get(Calendar.MONTH) + " " + date.getCalendar().get(Calendar.YEAR));
        Log.d("EVENEMENT", key);
        if (!key.equals("2025-02-16")){
            Snackbar.make(findViewById(R.id.main_layout), "test", Snackbar.LENGTH_SHORT).show();
        }
        evenementsParJour.putIfAbsent(key, new ArrayList<>());
        evenementsParJour.get(key).add(evenement);
        updateCalendar();
    }

    public String dateToString(CalendarDay calendar) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return sdf.format(calendar.getCalendar().getTime());
    }

    public Calendar stringToDate(String dateString) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(sdf.parse(dateString));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return calendar;
    }
}
