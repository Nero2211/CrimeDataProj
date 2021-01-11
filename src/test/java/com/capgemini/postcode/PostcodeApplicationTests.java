package com.capgemini.postcode;

import com.capgemini.postcode.model.crime.CrimeRoot;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static com.capgemini.postcode.constants.TestConstants.expectedCategoriesList;
import static com.capgemini.postcode.constants.TestConstants.expectedCrimeList;

@SpringBootTest
class PostcodeApplicationTests {

	public static PostcodeApplication application = new PostcodeApplication();

	@Test
	void testCrimeCategoriesRetrieval() {
		ResponseEntity<String> rawResponse = application.getCrimeCategories();

		Assert.assertNotNull("Response from the service is null", rawResponse);
		Assert.assertEquals("Status code from service is expected to be 200", 200, rawResponse.getStatusCodeValue());

		Assert.assertFalse("Response body is empty", rawResponse.getBody().trim().isEmpty());

		int startIndex = rawResponse.getBody().indexOf("[") + 1;
		int endIndex = rawResponse.getBody().indexOf("]");
		String categories = rawResponse.getBody().substring(startIndex, endIndex).replace("\"", "");
		String[] categoriesList = categories.split(",");

		Assert.assertEquals("Array size between expected and actual does not match", expectedCategoriesList.size(), categoriesList.length);
		for (String category : categoriesList) {
			Assert.assertTrue("Category [" + category + "] was not found in the expected list of categories", expectedCategoriesList.contains(category));
		}
	}

	@Test
	void testStreetCrimeParamValidation(){
		ResponseEntity<Object> rawResponse = application.getCrimeDataForPostCode("bbbb", "2019-02");

		Assert.assertNotNull("Response from the service is null", rawResponse);
		Assert.assertEquals("Status code from service is expected to be 400", 400, rawResponse.getStatusCodeValue());
		Assert.assertEquals("Reason phrase is not as expected", "Bad Request", rawResponse.getStatusCode().getReasonPhrase());
		Assert.assertEquals("ResponseErrorMessage{errorCode='e1002', message='Exception occurred: Provided postcode [bbbb] is not valid as per UK's standard, please check and try again'}", rawResponse.getBody().toString());

		ResponseEntity<Object> rawResponse2 = application.getCrimeDataForPostCode("WS11QA", "2019-02-12");
		Assert.assertNotNull("Response from the service is null", rawResponse2);
		Assert.assertEquals("Status code from service is expected to be 400", 400, rawResponse2.getStatusCodeValue());
		Assert.assertEquals("Reason phrase is not as expected", "Bad Request", rawResponse2.getStatusCode().getReasonPhrase());
		Assert.assertEquals("ResponseErrorMessage{errorCode='e1002', message='Exception occurred: Provided date [2019-02-12] does not meet the expected format [yyyy-mm], please check and try again'}", rawResponse2.getBody().toString());

	}

	@Test
	void testSuccessfulStreetCrimeResponse(){
		// TODO: risky test as we are comparing live data from the API with hardcoded data (also taken from the API)
		// approach needs to be improved
		ResponseEntity<Object> rawResponse = application.getCrimeDataForPostCode("WS11QA", "2020-07");

		Assert.assertNotNull("Response from the service is null", rawResponse);
		Assert.assertEquals("Status code from service is expected to be 200", 200, rawResponse.getStatusCodeValue());

		List<CrimeRoot> actualCrimeResponse = (List<CrimeRoot>) rawResponse.getBody();
		Assert.assertTrue("List from the response is empty", !actualCrimeResponse.isEmpty());

		CrimeRoot expectedCrimeA = expectedCrimeList.get(0);
		CrimeRoot expectedCrimeB = expectedCrimeList.get(1);
		CrimeRoot actualCrimeA = actualCrimeResponse.stream().filter(crimeRoot -> crimeRoot.getId() == expectedCrimeA.getId()).findFirst().orElse(null);
		CrimeRoot actualCrimeB = actualCrimeResponse.stream().filter(crimeRoot -> crimeRoot.getId() == expectedCrimeB.getId()).findFirst().orElse(null);

		Assert.assertNotNull("Unable to find crime A with the ID of: " + expectedCrimeA.getId(), actualCrimeA);
		Assert.assertNotNull("Unable to find crime B with the ID of: " + expectedCrimeB.getId(), actualCrimeB);

		Assert.assertEquals("Crime A comparison failed", expectedCrimeA.getCategory(), actualCrimeA.getCategory());
		Assert.assertEquals("Crime A comparison failed", expectedCrimeA.getLocation_type(), actualCrimeA.getLocation_type());
		Assert.assertEquals("Crime A comparison failed", expectedCrimeA.getMonth(), actualCrimeA.getMonth());

		Assert.assertEquals("Crime B comparison failed", expectedCrimeB.getCategory(), actualCrimeB.getCategory());
		Assert.assertEquals("Crime B comparison failed", expectedCrimeB.getLocation_type(), actualCrimeB.getLocation_type());
		Assert.assertEquals("Crime B comparison failed", expectedCrimeB.getMonth(), actualCrimeB.getMonth());

	}

}
