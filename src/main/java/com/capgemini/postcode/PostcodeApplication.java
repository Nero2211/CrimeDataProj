package com.capgemini.postcode;

import com.capgemini.postcode.constants.BaseUrl;
import com.capgemini.postcode.constants.ErrorCode;
import com.capgemini.postcode.model.CrimeCategory;
import com.capgemini.postcode.model.ResponseErrorMessage;
import com.capgemini.postcode.model.crime.CrimeRoot;
import com.capgemini.postcode.services.PostcodeService;
import com.capgemini.postcode.utils.PostCodeUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.util.List;

@SpringBootApplication
@RestController
public class PostcodeApplication {

	public static void main(String[] args) {
		SpringApplication.run(PostcodeApplication.class, args);
	}

	@GetMapping("/hello")
	public String hello(@RequestParam(value = "name", defaultValue = "Worssld") String name) {
		return String.format("Hello %s!", name);
	}

	@GetMapping("/crimes")
    public ResponseEntity<String> getCrimeDataForPostCode(@RequestParam(value = "postcode") String postcode, @RequestParam(value = "date") String date){
		List<CrimeRoot> crimeDetails;

		try {
			crimeDetails = PostCodeUtils.getCrimeDetailsForLocation(postcode, date);
			if (crimeDetails == null || crimeDetails.isEmpty()) {
				ResponseErrorMessage msg = new ResponseErrorMessage(ErrorCode.NO_DATA_FOUND, "No data found");
				return new ResponseEntity(msg, HttpStatus.NOT_FOUND);
			}
        } catch (IllegalArgumentException | IOException | InterruptedException ex) {
            ResponseErrorMessage msg = new ResponseErrorMessage(ErrorCode.EXCEPTION_OCCURRED, "Exception occurred: " + ex.getMessage());
            return new ResponseEntity(msg, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(crimeDetails, HttpStatus.OK);
    }

	@GetMapping("/crime/categories")
	public ResponseEntity<String> getCrimeCategories() {
		JSONObject jsonResponse = new JSONObject();
		JSONArray crimeCategoriesArray = new JSONArray();

		try{
			List<CrimeCategory> crimeCategories = getCrimeCategoriesResponse();
			if (crimeCategories != null && !crimeCategories.isEmpty()) {
				crimeCategories.stream().forEach(crimeCategory -> crimeCategoriesArray.put(crimeCategory.getname()));
			} else {
				ResponseErrorMessage msg = new ResponseErrorMessage(ErrorCode.NO_DATA_FOUND, "No data found");
				return new ResponseEntity(msg, HttpStatus.NOT_FOUND);
			}
		}catch (IOException e) {
			ResponseErrorMessage msg = new ResponseErrorMessage(ErrorCode.EXCEPTION_OCCURRED, e.getMessage());
			return new ResponseEntity(msg, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		try {
			// The purpose of using is to wrap categories array in a JSON object
			jsonResponse.put("Categories", crimeCategoriesArray);
		} catch (JSONException e) {
			ResponseErrorMessage msg = new ResponseErrorMessage(ErrorCode.EXCEPTION_OCCURRED, e.getMessage());
			return new ResponseEntity(msg, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity(jsonResponse.toString(), HttpStatus.OK);
	}


	/**
	 * This method will make an API call and retrieve the list of crime categories from the API
	 * For each category, an independent @see com.capgemini.postcode.model.CrimeCategory
	 * @return
	 * @throws IOException
	 */
	private List<CrimeCategory> getCrimeCategoriesResponse() throws IOException {
		Retrofit retrofit = new Retrofit.Builder()
				.baseUrl(BaseUrl.POLICEDATA_BASE_URL)
				.addConverterFactory(JacksonConverterFactory.create())
				.build();

		PostcodeService service = retrofit.create(PostcodeService.class);
		Call<List<CrimeCategory>> crimeCategoriesService = service.getCrimeCategoriesList();
		retrofit2.Response<List<CrimeCategory>> apiResponse = crimeCategoriesService.execute();

		List<CrimeCategory> crimeCategories = apiResponse.body();
		return crimeCategories;
	}
}

