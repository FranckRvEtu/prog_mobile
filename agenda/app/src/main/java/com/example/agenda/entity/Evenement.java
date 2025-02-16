package com.example.agenda.entity;

import android.util.Log;

import com.applandeo.materialcalendarview.CalendarDay;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Evenement {
    private CalendarDay jour;
    private String titre;
    private String description;
    private String categorie;
    private int heure;
    private int minute;
    private int iconResId;

    public Evenement(CalendarDay jour, String titre, String description, String categorie, int heure, int minute, int iconResId) {
        this.jour = jour;
        this.titre = titre;
        this.description = description;
        this.categorie = categorie;
        this.heure = heure;
        this.minute = minute;
        this.iconResId = iconResId;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public CalendarDay getJour() {
        return jour;
    }

    public void setJour(CalendarDay jour) {
        this.jour = jour;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getHeure() {
        return heure;
    }

    public void setHeure(int heure) {
        this.heure = heure;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getIconResId() {
        return iconResId;
    }

    public void setIconResId(int iconResId) {
        this.iconResId = iconResId;
    }

    @Override
    public String toString() {
        return "Evenement{" +
                "jour=" + jour.getCalendar().get(Calendar.DAY_OF_MONTH) + "-" + (jour.getCalendar().get(Calendar.MONTH) + 1) + "-" + jour.getCalendar().get(Calendar.YEAR) +
                ", titre='" + titre + '\'' +
                ", description='" + description + '\'' +
                ", categorie='" + categorie + '\'' +
                ", heure=" + heure +
                ", minute=" + minute +
                ", iconResId=" + iconResId +
                '}';
    }

    public static Evenement fromString(String str) {
        if (!str.startsWith("Evenement{") || !str.endsWith("}")) {
            throw new IllegalArgumentException("Format invalide : " + str);
        }

        str = str.substring(10, str.length() - 1);

        // Découper les parties
        String[] parts = str.split(", ");
        Log.d("PARTS", parts[0]);
        CalendarDay jour = new CalendarDay(parseCalendar(parts[0].split("=")[1]));
        String titre = parts[1].split("=")[1].replace("'", "");
        String description = parts[2].split("=")[1].replace("'", "");
        String categorie = parts[3].split("=")[1].replace("'", "");
        int heure = Integer.parseInt(parts[4].split("=")[1]);
        int minute = Integer.parseInt(parts[5].split("=")[1]);
        int iconResId = Integer.parseInt(parts[6].split("=")[1]);
        Log.d("JOUREVENT", ""+jour.getCalendar().get(Calendar.DAY_OF_MONTH));
        return new Evenement(jour, titre, description, categorie, heure, minute, iconResId);
    }
    private static Calendar parseCalendar(String dateStr) {
        Calendar calendar = Calendar.getInstance();
        Log.d("PARSE", dateStr);
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy", Locale.getDefault());
            Date date = sdf.parse(dateStr);
            Log.d("DATE",date.toString());
            if (date != null) {
                calendar.setTime(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return calendar;
    }



}
