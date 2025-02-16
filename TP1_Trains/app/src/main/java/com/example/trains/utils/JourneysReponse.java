package com.example.trains.utils;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class JourneysReponse {
    @SerializedName("journeys")
    private List<StopAreasResponse.Trajet> trajets;

    public List<StopAreasResponse.Trajet> getTrajets() {
        return trajets;
    }

    public void setTrajets(List<StopAreasResponse.Trajet> trajets) {
        this.trajets = trajets;
    }
}
