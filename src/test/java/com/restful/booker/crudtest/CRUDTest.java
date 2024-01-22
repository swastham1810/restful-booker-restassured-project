package com.restful.booker.crudtest;

import com.restful.booker.model.AuthPojo;
import com.restful.booker.model.BookingDatesPojo;
import com.restful.booker.model.BookingPojo;
import com.restful.booker.testbase.TestBase;
import com.restful.booker.utils.TestUtils;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;

public class CRUDTest extends TestBase {
    static String username = "admin";
    static String password = "password123";
    static String firstname = "Asha" + TestUtils.getRandomValue();
    static String lastname = "Kakadiya" + TestUtils.getRandomValue();
    static int totalprice = 555;
    static boolean depositpaid = true;
    static String additionalneeds = "Dinner" + TestUtils.getRandomValue();
    static String checkin = "2024-10-18";
    static String checkout = "2025-10-18";
    static String token;
    static int bookingId;
    BookingPojo bookingPojo = new BookingPojo();

    @Test(priority = 1)

    public void createToken() {
        AuthPojo authPojo = new AuthPojo();
        authPojo.setUsername(username);
        authPojo.setPassword(password);
        Response response = given()
                .contentType(ContentType.JSON)
                .when()
                .body(authPojo)
                .post("https://restful-booker.herokuapp.com/auth");
        response.then().log().all().statusCode(200);
        String jsonString = response.asString();
        token = JsonPath.from(jsonString).get("token");
        System.out.println("Token is: " + token);
    }

    @Test(priority = 2)
    public void postBookingTest() {
        System.out.println("=========== CREATE BOOKING USING POST ===============");
        BookingDatesPojo bookingDates = new BookingDatesPojo();
        bookingDates.setCheckin(checkin);
        bookingDates.setCheckout(checkout);

        bookingPojo.setFirstname(firstname);
        bookingPojo.setLastname(lastname);
        bookingPojo.setTotalprice(totalprice);
        bookingPojo.setDepositpaid(depositpaid);
        bookingPojo.setBookingdates(bookingDates);
        bookingPojo.setAdditionalneeds(additionalneeds);

        Response response = given().log().all()
                .header("Content-Type", "application/json")
                .header("Authorization", "Basic YWRtaW46cGFzc3dvcmQxMjM=")
                .header("Accept", "application/json")
                .when()
                .body(bookingPojo)
                .post();
        bookingId = response.then().statusCode(200)
                .extract().path("bookingid");
        System.out.println("ID = " + bookingId);


    }

    @Test(priority = 3)
    public void getBookingId() {
        System.out.println("=========== GET BOOKING ID USING GET ===============");
        Response response = given().log().all()
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("Cookie", "token=" + token)
                .header("Authorization", "Basic YWRtaW46cGFzc3dvcmQxMjM=")
                .body(bookingPojo)
                .when()
                .get("/" + bookingId);
        response.then().log().all().statusCode(200);
    }

    @Test(priority = 4)
    public void updateBookingId() {
        System.out.println("=========== UPDATE BOOKING USING PUT ===============");

        String firstname = "Krishn" + TestUtils.getRandomValue();
        String lastname = "Patel" + TestUtils.getRandomValue();

        BookingDatesPojo bookingDates = new BookingDatesPojo();
        bookingDates.setCheckin(checkin);
        bookingDates.setCheckout(checkout);

        BookingPojo bookingPojo = new BookingPojo();
        bookingPojo.setFirstname(firstname);
        bookingPojo.setLastname(lastname);
        bookingPojo.setTotalprice(123);
        bookingPojo.setDepositpaid(true);
        bookingPojo.setAdditionalneeds("Dinner & bed");
        bookingPojo.setBookingdates(bookingDates);

        Response response =
                given()
                        .header("Content-Type", "application/json")
                        .header("Accept", "application/json")
                        .header("Cookie", "token=" + token)
                        .header("Authorization", "Basic YWRtaW46cGFzc3dvcmQxMjM=")
                        .body(bookingPojo)
                        .when()
                        .put("/" + bookingId);
        response.then().statusCode(200);
        response.prettyPrint();
    }

    @Test(priority = 5)
    public void PartialUpdateBooking() {

        System.out.println("=========== PARTIAL UPDATE BOOKING ===============");

        String firstname = "Automation" + TestUtils.getRandomValue();
        String lastname = "Tester" + TestUtils.getRandomValue();

        BookingDatesPojo bookingDates = new BookingDatesPojo();
        bookingDates.setCheckin(checkin);
        bookingDates.setCheckout(checkout);

        bookingPojo.setFirstname(firstname);
        bookingPojo.setLastname(lastname);

        Response response =
                given().log().all()
                        .header("Content-Type", "application/json")
                        .header("Accept", "application/json")
                        .header("Cookie", "token=" + token)
                        .header("Authorization", "Basic YWRtaW46cGFzc3dvcmQxMjM=")
                        .body(bookingPojo)
                        .when()
                        .patch("/" + bookingId);
        response.then().statusCode(200);
        response.prettyPrint();
    }

    @Test(priority = 6)
    public void deleteBooking() {

        System.out.println("=========== DELETE BOOKING ===============");

        Response response = given().log().all()
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("Cookie", "token=" + token)
                .header("Authorization", "Basic YWRtaW46cGFzc3dvcmQxMjM=")
                .when()
                .delete("/" + bookingId);
        response.then().statusCode(201);
        response.prettyPrint();
    }

    @Test(priority = 7)
    public void getBookingIds() {
        Response response = given().log().all()
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("Cookie", "token=" + token)
                .header("Authorization", "Basic YWRtaW46cGFzc3dvcmQxMjM=")
                .body(bookingPojo)
                .when()
                .get("/" + bookingId);
        response.then().log().all().statusCode(404);
    }

    @Test(priority = 8)
    public void getAllBookingIds() {
        BookingPojo bookingPojo = new BookingPojo();
        bookingPojo.setFirstname(firstname);
        bookingPojo.setLastname(lastname);
        List<String> bookingdates = new ArrayList<>();
        bookingdates.add("2024-10-18");
        bookingdates.add("2025-10-18");
        Response response = given()
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("Cookie", "token=" + token)
                .header("Authorization", "Basic YWRtaW46cGFzc3dvcmQxMjM=")
                .body(bookingPojo)
                .when().get();
        response.then().log().all().statusCode(200);
    }

    @Test(priority = 8)
    public void getBookingWithFirstName() {
        BookingPojo bookingPojo = new BookingPojo();
        Response response = given()
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("Cookie", "token=" + token)
                .header("Authorization", "Basic YWRtaW46cGFzc3dvcmQxMjM=")
                .body(bookingPojo)
                .param("firstname", firstname)
                .when().get("https://restful-booker.herokuapp.com/booking");
        response.then().log().all().statusCode(200);
    }

}
