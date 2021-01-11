package com.capgemini.postcode.utils;

import com.capgemini.postcode.constants.BaseUrl;
import com.capgemini.postcode.model.postcode.Postcode;
import com.capgemini.postcode.model.crime.CrimeRoot;
import com.capgemini.postcode.services.PostcodeService;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PostCodeUtils {

    public static final String POSTCODE_REGEX = "^([A-Za-z][A-Ha-hJ-Yj-y]?[0-9][A-Za-z0-9]? ?[0-9][A-Za-z]{2}|[Gg][Ii][Rr] ?0[Aa]{2})$";
    public static final String DATE_REGEX = "^([0-9]{4}-[0-9]{2}$)";

    public static void validateInput(String postcode, String date) throws IllegalArgumentException {
        if(postcode == null || postcode.trim().isEmpty()){
            throw new IllegalArgumentException("No postcode has been provided");
        }

        if(date == null || date.trim().isEmpty()){
            throw new IllegalArgumentException("No date has been provided");
        }

        Pattern pcPattern = Pattern.compile(POSTCODE_REGEX);
        Matcher pcMatcher = pcPattern.matcher(postcode);
        boolean pcMatchFound = pcMatcher.find();

        if(!pcMatchFound){
            throw new IllegalArgumentException("Provided postcode [" + postcode + "] is not valid as per UK's standard, please check and try again");
        }

        Pattern datePattern = Pattern.compile(DATE_REGEX);
        Matcher dateMatcher = datePattern.matcher(date);
        boolean dateMatchFound = dateMatcher.find();

        if(!dateMatchFound){
            throw new IllegalArgumentException("Provided date [" + date + "] does not meet the expected format [yyyy-mm], please check and try again");
        }
    }

    public static List<CrimeRoot> getCrimeDetailsForLocation(String postCode, String date) throws IOException, InterruptedException, IllegalArgumentException {
        validateInput(postCode, date);

        List<CrimeRoot> crimesList;
        Postcode pcInfo = getLocationFromAddress(postCode);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BaseUrl.POLICEDATA_BASE_URL)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();

        PostcodeService service = retrofit.create(PostcodeService.class);
        Call<List<CrimeRoot>> crimeService = service.getCrimeDetailsForLocation(String.valueOf(pcInfo.getResult().getLatitude()), String.valueOf(pcInfo.getResult().getLongitude()), date);
        Response<List<CrimeRoot>> apiResponse = crimeService.execute();

        crimesList = apiResponse.body();

        return crimesList;
    }

    private static Postcode getLocationFromAddress(String postcode) throws InterruptedException, IOException {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BaseUrl.POSTCODE_BASE_URL)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();

        PostcodeService service = retrofit.create(PostcodeService.class);
        Call<Postcode> postcodeInfo = service.getPostcodeInfo(postcode);
        Response<Postcode> apiResponse = postcodeInfo.execute();

        Postcode postcodeRoot = apiResponse.body();
        return postcodeRoot;
    }
}
