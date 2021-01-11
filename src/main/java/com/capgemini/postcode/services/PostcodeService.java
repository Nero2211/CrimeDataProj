package com.capgemini.postcode.services;

import com.capgemini.postcode.model.CrimeCategory;
import com.capgemini.postcode.model.crime.CrimeRoot;
import com.capgemini.postcode.model.postcode.Postcode;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

import java.util.List;

public interface PostcodeService {

    @GET("crime-categories")
    Call<List<CrimeCategory>> getCrimeCategoriesList();

    @GET("postcodes/{postcode}")
    Call<Postcode> getPostcodeInfo(@Path("postcode") String postcode);

    //https://data.police.uk/api/crimes-street/all-crime?lat=52.629729&lng=-1.131592&date=2017-01
    @GET("crimes-street/all-crime")
    Call<List<CrimeRoot>> getCrimeDetailsForLocation(@Query("lat") String lat, @Query("lng")String lng, @Query("date")String date);

}
