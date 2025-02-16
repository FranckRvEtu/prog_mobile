package com.example.trains.utils;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SncfApi {
    @GET("journeys")
    Call<JourneysReponse> getTrajets(@Query("from") String from,
                                     @Query("to") String to,
                                     @Query("datetime") String datetime,
                                     @Query("count") int count);

    @GET("journeys")
    Call<JourneysReponse> getTrajets(@Query("from") String from,
                                     @Query("to") String to,
                                     @Query("datetime") String datetime,
                                     @Query("timeframe_duration") long timeframe_duration);

    @GET("stop_areas")
    Call<StopAreasResponse> getStops(@Query("language") String language);

    @GET("places")
    Call<StopAreasResponse> getPlaces(@Query("q") String q, @Query("count") int count);
}
