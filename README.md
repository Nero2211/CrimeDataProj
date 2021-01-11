# CrimeDataProj

This is a Spring Boot microservice that allows you to request crime data for the following endpoints.
In order to do so, you need to run the service locally and access the relevant access points (RESTful API) to attain the information needed.

This can be achieved in the following steps:
1. Clone the repo locally
2. Run the following command depending on the OS:

      Windows:
      ```mvnw spring-boot:run```

      Linux/Mac:
      ```./mvnw spring-boot:run```
      
      This command will build the repo and run it as a service.
     
From this point forth, you should be able to access the corresponding APIs via ```https://localhost:8080/crimes...```

### RESTful end points available
| HTTP Method  | URL | Description | Possible outcomes | Response example |
| ------------- | ------------- | ------------- | ------------- |  ------------- |
| `@GET`| `localhost:8080/hello`  | A test call to ensure the services is up and running  | Response code: 200 (OK)  | `Hello World` |
| `@GET`| `localhost:8080/crime/categories`  | Retrieves a list of crime categories  | Response code: 200 (OK),  <br> 404 (NOT FOUND), <br> 500 (INTERNAL SERVER ERROR)  | `{"Categories":["All crime","Anti-social behaviour","Bicycle theft","Burglary","Criminal damage and arson","Drugs",...` |
| `@GET`| `localhost:8080/crimes?postcode=<postcode>&date=<yyyy-mm>`  | Retrieves a list of crime details for a certain postcode for a given date (yyyy-mm)  | Response code: 200 (OK),  <br> 400 (BAD REQUEST), <br> 404 (NOT FOUND), <br> 500 (INTERNAL SERVER ERROR)  | `[{"category":"anti-social-behaviour","location_type":"Force","location":{"latitude":"52.592306","street":{"id":1270318,"name":"On or near William Street"},"longitude":"-1.976781"},"context":"","outcome_status":null,"persistent_id":"","id":72281112,"location_subtype...` |
