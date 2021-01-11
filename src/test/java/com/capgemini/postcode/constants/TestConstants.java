package com.capgemini.postcode.constants;

import com.capgemini.postcode.model.crime.CrimeRoot;

import java.util.ArrayList;
import java.util.List;

public class TestConstants {

    public static List<String> expectedCategoriesList = new ArrayList<>();
    static {
        expectedCategoriesList.add("All crime");
        expectedCategoriesList.add("Anti-social behaviour");
        expectedCategoriesList.add("Bicycle theft");
        expectedCategoriesList.add("Burglary");
        expectedCategoriesList.add("Criminal damage and arson");
        expectedCategoriesList.add("Drugs");
        expectedCategoriesList.add("Other theft");
        expectedCategoriesList.add("Possession of weapons");
        expectedCategoriesList.add("Public order");
        expectedCategoriesList.add("Robbery");
        expectedCategoriesList.add("Shoplifting");
        expectedCategoriesList.add("Theft from the person");
        expectedCategoriesList.add("Vehicle crime");
        expectedCategoriesList.add("Violence and sexual offences");
        expectedCategoriesList.add("Other crime");
    }

    // few examples obtained from real life data
    // risky move as the data may get updated and will not always be in the same order
    // TODO: find better approach to test the data
    public static List<CrimeRoot> expectedCrimeList = new ArrayList<>();
    static {
        expectedCrimeList.add(new CrimeRoot("anti-social-behaviour", "Force", 85660487, "2020-07"));
        expectedCrimeList.add(new CrimeRoot("anti-social-behaviour", "Force", 85657691, "2020-07"));
    }

}
