package GoRest;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class GorestuserTest {
    @BeforeClass
    void  Setup(){
        baseURI="https://gorest.co.in/public/v2/";
    }

public String getrandomname()
{
    return RandomStringUtils.randomAlphabetic(8);
}
public String getrandomemail(){
        return RandomStringUtils.randomAlphabetic(8)+"@gmail.com";
}

    int userID=0;
    User newuser;
    @Test(priority = 1)
    public void createUserObject()
    {
         newuser=new User();
        newuser.setName(getrandomname());
        newuser.setGender("male");
        newuser.setEmail(getrandomemail());
        newuser.setStatus("active");

        userID=
                given()//api methodona gitmeden once hazirliklar:token,gidecek body,parametreler

                        .header("Authorization","Bearer 54c4b2d97ee535f67e2336f135448c4fe13ebb8a940263128627b9e6aed78017")
                        .contentType(ContentType.JSON)
                        .body(newuser)
                        .log().body()


                        .when()
                        .post("users")


                        .then()
                        .log().body()
                        .statusCode(201)
                        .contentType(ContentType.JSON)
                       // .extract().path("id")
                        .extract().jsonPath().getInt("id")

                // path : class veya tip dönüşümüne imkan veremeyen direk veriyi verir. List<String> gibi
                // jsonPath : class dönüşümüne ve tip dönüşümüne izin vererek , veriyi istediğimiz formatta verir.
                ;System.out.println("userID = " + userID);

    }

    @Test(dependsOnMethods = " createUserObject",priority = 2)
    public void UpdateUserObject()
    {
        Map<String,String> updateUser=new HashMap<>();
        updateUser.put("name","marziiiii");


                given()

                        .header("Authorization","Bearer 54c4b2d97ee535f67e2336f135448c4fe13ebb8a940263128627b9e6aed78017")
                        .contentType(ContentType.JSON)
                        .body(updateUser)
                        .log().body()
                        .pathParam("userID",userID)


                        .when()
                        .put("users/{userID}")


                        .then()
                        .log().body()
                        .statusCode(200)
                        .body("name",equalTo("marziyeh haydari"))

                ;
    }








    @Test(enabled = false)
    public void createUserMap()
    {
        Map<String,String> newuser=new HashMap<>();
        newuser.put("name",getrandomname());
        newuser.put("gender","female");
        newuser.put("email",getrandomemail());
        newuser.put("status","active");

        int userID=
                given()//api methodona gitmeden once hazirliklar:token,gidecek body,parametreler

                        .header("Authorization","Bearer 54c4b2d97ee535f67e2336f135448c4fe13ebb8a940263128627b9e6aed78017")
                        .contentType(ContentType.JSON)
                        .body(newuser)
                        .log().body()


                        .when()
                        .post("users")


                        .then()
                        .log().body()
                        .statusCode(201)
                        .contentType(ContentType.JSON)
                        .extract().path("id")

                ;

        System.out.println("userID = " + userID);


    }
    @Test(enabled = false)
    public void createUser()
    {
        int userID=
                given()//api methodona gitmeden once hazirliklar:token,gidecek body,parametreler

                        .header("Authorization","Bearer 54c4b2d97ee535f67e2336f135448c4fe13ebb8a940263128627b9e6aed78017")
                        .contentType(ContentType.JSON)
                        .body("{\"name\":\""+getrandomname()+"\",\"email\":\""+getrandomemail()+"\",\"gender\":\"male\",\"status\":\"active\"}")


                        .when()
                        .post("users")


                        .then()
                        .log().body()
                        .statusCode(201)
                        .contentType(ContentType.JSON)
                        .extract().path("id")

                ;

        System.out.println("userID = " + userID);


    }

    @Test
    public void getUsers()
    {
        Response response=
                given()
                        .header("Authorization","Bearer 523891d26e103bab0089022d20f1820be2999a7ad693304f560132559a2a152d")

                        .when()
                        .get("users")

                        .then()
                        .log().body()
                        .statusCode(200)
                        .extract().response()
                ;

        // perşembe veya pazartesi, veya salı yapılacak...
        // TODO : 3 usersın id sini alınız (path ve jsonPath ile ayrı ayrı yapınız)
        int idUser3path=response.path("[2].id");
        int idUser3Jsonpath=response.jsonPath().getInt("[2].id");
        System.out.println("idUser3path = " + idUser3path);
        System.out.println("idUser3Jsonpath = " + idUser3Jsonpath);

        // TODO : Tüm gelen veriyi bir nesneye atınız (google araştırması)
        // TODO : GetUserByID testinde dönen user ı bir nesneye atınız.
    }
    @Test
    public void getUserByID()
    {
        given()
                .header("Authorization","Bearer 523891d26e103bab0089022d20f1820be2999a7ad693304f560132559a2a152d")
                .contentType(ContentType.JSON)

                .pathParam("userID", 3414)

                .when()
                .get("users/{userID}")

                .then()
                .log().body()
                .statusCode(200)

        ;
    }


}
class User{

    private   String name;
    private   String gender;
    private   String email;
    private   String status;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", email='" + email + '\'' +
                ", status='" + status + '\'' +
                '}';
    }




}
