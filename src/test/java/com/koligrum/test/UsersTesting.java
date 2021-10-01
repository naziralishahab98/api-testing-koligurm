package com.koligrum.test;


import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.*;

public class UsersTesting {

    final static String url ="http://localhost";
    final static Integer port = 1234;


    @Test
    public void getUsersByName(){
        Response response = given()
                .baseUri(url)
                .port(port)
                .header("accept", "application/json")
                .queryParam("name", "nazir")
                .basePath("v1")
                .log().all()
                .when()
                .get("users");

        int statusCode = response.getStatusCode();
        assertEquals(200,statusCode);


        //assert json path
        String firstName = response.jsonPath().getJsonObject("data[0].firstName");
        assertEquals("Nazir",firstName);
//        JsonPath jsonPathEvaluator = response.jsonPath();
//        String lastName = jsonPathEvaluator.get("data[0].lastName");
//        System.out.println("test : "+lastName);

    }

    @Test
    public void createUsers(){
        String FirstName = "muhammad";
        String LastName = "wildan";
        int age = 24;
        String occupation = "Kurir";
        String nationality = "Indonesia";
        String hobby = "Futsal";
        String gender = "MALE";

        String body = "{\n"
                + "\n"
                + "  \"firstName\": \"" + FirstName + "\",\n"
                + "  \"lastName\": \"" + LastName + "\",\n"
                + "  \"age\": " + age + ",\n"
                + "  \"occupation\": \"" + occupation + "\",\n"
                + "  \"nationality\": \"" + nationality + "\",\n"
                + "  \"hobbies\": [\n"
                + "    \""+ hobby +"\"\n"
                + "  ],\n"
                + "  \"gender\": \""+ gender +"\"\n"
                + "}";


        Response responsePOST = given()
                .baseUri(url)
                .port(port)
                .header("accept", "application/json")
                .contentType(ContentType.JSON)
                .basePath("v1")
                .body(body).log().all()
                .when().post("users");

        responsePOST.getBody().prettyPrint();

        //get id user
        String id = responsePOST.path("id");

        //validasi
        Response responseGetById = given()
                .baseUri(url)
                .port(port)
                .header("accept", "application/json")
                .pathParam("id",id)
                .basePath("v1")
                .log().all()
                .when()
                .get("users/{id}");

        responseGetById.getBody().prettyPrint();

        assertEquals(200,responseGetById.getStatusCode());
        assertEquals(FirstName,responseGetById.path("firstName"));
        assertEquals(LastName,responseGetById.path("lastName"));
        assertEquals(occupation,responseGetById.path("occupation"));
        assertEquals(String.valueOf(age),responseGetById.path("age"));
    }

    @Test
    public void tugasKoligrum(){
        String FirstName = "muhammad";
        String LastName = "wildan";
        int age = 24;
        String occupation = "Kurir";
        String nationality = "Indonesia";
        String hobby = "Futsal";
        String gender = "MALE";

        String bodyPOST = "{\n"
                + "\n"
                + "  \"firstName\": \"" + FirstName + "\",\n"
                + "  \"lastName\": \"" + LastName + "\",\n"
                + "  \"age\": " + age + ",\n"
                + "  \"occupation\": \"" + occupation + "\",\n"
                + "  \"nationality\": \"" + nationality + "\",\n"
                + "  \"hobbies\": [\n"
                + "    \""+ hobby +"\"\n"
                + "  ],\n"
                + "  \"gender\": \""+ gender +"\"\n"
                + "}";

        //hit api create users
        Response responsePOST = given()
                .baseUri(url)
                .port(port)
                .header("accept", "application/json")
                .contentType(ContentType.JSON)
                .basePath("v1")
                .body(bodyPOST).log().all()
                .when().post("users");

        //print response api create users
        responsePOST.getBody().prettyPrint();
        //get id from response
        String id = responsePOST.path("id");

        //validasi user succesfully created
        Response responseGetById = given()
                .baseUri(url)
                .port(port)
                .header("accept", "application/json")
                .pathParam("id",id)
                .basePath("v1")
                .log().all()
                .when()
                .get("users/{id}");

        //print hit api get users
        responseGetById.getBody().prettyPrint();
        assertEquals(200,responseGetById.getStatusCode());
        assertEquals(FirstName,responseGetById.path("firstName"));
        assertEquals(LastName,responseGetById.path("lastName"));
        assertEquals(occupation,responseGetById.path("occupation"));
        assertEquals(Integer.valueOf(age),responseGetById.path("age"));




        String updateFirstName = "muhammad edit";
        String updateLastName = "wildan edit";

        String bodyPUT = "{\n" +
                "  \"id\": \""+id+"\",\n" +
                "  \"firstName\": \""+updateFirstName+"\",\n" +
                "  \"lastName\": \""+updateLastName+"\",\n" +
                "  \"age\": "+age+",\n" +
                "  \"occupation\": \""+occupation+"\",\n" +
                "  \"nationality\": \""+nationality+"\",\n" +
                "  \"hobbies\": [\n" +
                "    \""+hobby+"\"\n" +
                "  ],\n" +
                "  \"gender\": \""+gender+"\"\n" +
                "}";


        //hit api update users
        Response responsePUT = given()
                .baseUri(url)
                .port(port)
                .header("accept", "application/json")
                .contentType(ContentType.JSON)
                .basePath("v1")
                .body(bodyPUT).log().all()
                .when().put("users");

        //print hit api update users
        responsePUT.getBody().prettyPrint();
        String getFNameUpdate = responsePUT.path("firstName");
        String getLNameUpdate = responsePUT.path("lastName");


        //validasi user succesfully updated
        Response responseGetById2 = given()
                .baseUri(url)
                .port(port)
                .header("accept", "application/json")
                .pathParam("id",id)
                .basePath("v1")
                .log().all()
                .when()
                .get("users/{id}");

        responseGetById2.getBody().prettyPrint();
        assertEquals(getFNameUpdate, responseGetById2.path("firstName"));
        assertEquals(getLNameUpdate, responseGetById2.path("lastName"));



        //delete users
        Response responseDELETE = given()
                .baseUri(url)
                .port(port)
                .header("accept", "application/json")
                .pathParam("id",id)
                .basePath("v1")
                .log().all()
                .when()
                .delete("users/{id}");

        //print response api delete users
        responseDELETE.getBody().prettyPrint();

        //validasi user succesfully deleted
        Response responseGetById3 = given()
                .baseUri(url)
                .port(port)
                .header("accept", "application/json")
                .pathParam("id",id)
                .basePath("v1")
                .log().all()
                .when()
                .get("users/{id}");

        responseGetById3.getBody().prettyPrint();
        int statusCode = responseGetById3.getStatusCode();
        assertEquals(404,statusCode);
        assertEquals("user not found",responseGetById3.path("message"));



    }


}




