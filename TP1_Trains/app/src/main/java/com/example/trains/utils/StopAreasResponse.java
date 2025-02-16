package com.example.trains.utils;
import java.util.List;
import com.google.gson.annotations.SerializedName;

public class StopAreasResponse {
    @SerializedName("places")
    private List<StopArea> stops;

    public List<StopArea> getStops() {
        return stops;
    }

    public static class Trajet {
        @SerializedName("departure_date_time")
        private String depart;

        @SerializedName("arrival_date_time")
        private String arrivee;

        private String prix;

        @SerializedName("duration")
        private String duree;

        public Trajet(String depart, String arrivee, String prix, String duree) {
            this.depart = depart;
            this.arrivee = arrivee;
            this.prix = prix;
            this.duree = duree;
        }

        public String getDepart() {
            return depart;
        }

        public String getArrivee() {
            return arrivee;
        }

        public String getPrix() {
            return prix;
        }

        public String getDuree() {
            return duree;
        }

        public void setDepart(String depart) {
            this.depart = depart;
        }

        public void setArrivee(String arrivee) {
            this.arrivee = arrivee;
        }

        public void setPrix(String prix) {
            this.prix = prix;
        }

        public void setDuree(String duree) {
            this.duree = duree;
        }
    }
}
