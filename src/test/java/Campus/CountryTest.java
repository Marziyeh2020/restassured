package Campus;

import Campus.Model.Country;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.*;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class CountryTest {
    Cookies cookies;

    @BeforeClass
    public void logincampus()
    {

       baseURI="https://demo.mersys.io/";

        Map<String,String> credential=new HashMap<>();
        credential.put("username","richfield.edu");
        credential.put("password","Richfield2020!");
        credential.put("rememberMe","true");

      cookies =
       given()
               .contentType(ContentType.JSON)
               .body(credential)

               .when()
               .post("auth/login")

               .then()
               .log().all()

               .statusCode(200)
               .extract().response().getDetailedCookies()

              ;

    }

   String countryID;
    String countryname;
    String countrycode;
    @Test
    public void createcountry() {

        countryname=getRandomname();
        countrycode=getRandomcode();

        Country country=new Country();
        country.setName(countryname);//generate country name
        country.setCode(countrycode);//generate countrycode



        countryID=
        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(country)


                .when()
                .post("school-service/api/countries")

                .then()
                .log().body()
                .statusCode(201)
                .extract().jsonPath().getString("id")
                ;


    }
    @Test
    public void createcountryNegative() {

//        countryname=getRandomname();
//        countrycode=getRandomcode();

        Country country=new Country();
        country.setName(countryname);//generate country name
        country.setCode(countrycode);//generate countrycode




                given()
                        .cookies(cookies)
                        .contentType(ContentType.JSON)
                        .body(country)


                        .when()
                        .post("school-service/api/countries")

                        .then()
                        .log().body()
                        .statusCode(400)//
                        .body("message",equalTo("The Country with Name \""+countryname+"\" already exists."))

        ;


    }

    @Test(dependsOnMethods = "createcountry")
    public void UpdateCountry() {

       countryname=getRandomname();
//        countrycode=getRandomcode();

        Country country=new Country();
        country.setId(countryID);
        country.setName(countryname);//generate country name
        country.setCode(countrycode);//generate countrycode




        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(country)


                .when()
                .put("school-service/api/countries")

                .then()
                .log().body()
                .statusCode(200)//
                .body("name",equalTo(countryname))

        ;


    }

@Test(dependsOnMethods = "UpdateCountry")
    public void DeleteCountryById() {

        given()
                .cookies(cookies)
                .pathParam("countryID",countryID)



                .when()
                .delete("school-service/api/countries/{countryID}")

                .then()
                .log().body()
                .statusCode(200)//


        ;


    }
    @Test(dependsOnMethods = "DeleteCountryById")
    public void DeleteCountryByIdNegative() {

        given()
                .cookies(cookies)
                .pathParam("countryID",countryID)



                .when()
                .delete("school-service/api/countries/{countryID}")

                .then()
                .log().body()
                .statusCode(400)//


        ;


    }
    @Test(dependsOnMethods = "DeleteCountryById")
    public void UpdateCountryNegatif() {

        countryname=getRandomname();
//        countrycode=getRandomcode();

        Country country=new Country();
        country.setId(countryID);
        country.setName(countryname);//generate country name
        country.setCode(countrycode);//generate countrycode




        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(country)


                .when()
                .put("school-service/api/countries")

                .then()
                .log().body()
                .statusCode(400)//


        ;


    }

    public String getRandomname(){
        return RandomStringUtils.randomAlphabetic(8).toLowerCase();
    }

    public String getRandomcode(){
        return RandomStringUtils.randomAlphabetic(4).toLowerCase();
    }

}
