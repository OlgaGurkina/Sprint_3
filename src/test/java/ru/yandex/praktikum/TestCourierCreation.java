package ru.yandex.praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;


import static org.hamcrest.Matchers.equalTo;

public class TestCourierCreation {

    @Before
    public void setUp() {
        RestAssured.baseURI= "http://qa-scooter.praktikum-services.ru";
    }


    @Test
    @DisplayName("Check courier creation")
    @Description("check ability to create courier with correct params")
    public void checkCourierCreation() {
        RandomParamsForCouriers params = new RandomParamsForCouriers();
        Courier testCourier = new Courier(params.generatedLogin, params.generatedPassword, params.generatedFirstName);
        RestAssured.with()
                .header("Content-Type", "application/json")
                .body(testCourier)
                .post("/api/v1/courier")
                .then()
                .statusCode(201)
                .assertThat().body("ok", equalTo(true));


        LoginData loginData = new LoginData(params.generatedLogin, params.generatedPassword);
        int id = RestAssured.with()
                .header("Content-Type", "application/json")
                .body(loginData)
                .post("/api/v1/courier/login")
                .then()
                .statusCode(200)
                .extract().body().path("id");

        String loginID = Integer.toString(id);
        RestAssured.with()
                .header("Content-Type", "application/json")
                .delete("/api/v1/courier/{id}", loginID)
                .then()
                .statusCode(200);
    }


    @Test
    // тест падает, т.к. поведение отличается от описанного в документации
    // согласно документации возвращаться должна строка "Этот логин уже используется"
    // на практике - "Этот логин уже используется. Попробуйте другой."
    @DisplayName("Check two equal couriers cannot be created")
    @Description("check couriers with equal parameters cannot be created")
    public void checkTwoEqualCouriersCannotBeCreated(){
        RandomParamsForCouriers params = new RandomParamsForCouriers();
        Courier testCourier = new Courier(params.generatedLogin, params.generatedPassword, params.generatedFirstName);
        RestAssured.with()
                .header("Content-Type", "application/json")
                .body(testCourier)
                .post("/api/v1/courier")
                .then()
                .statusCode(201)
                .assertThat().body("ok", equalTo(true));

        RestAssured.with()
                .header("Content-Type", "application/json")
                .body(testCourier)
                .post("/api/v1/courier")
                .then()
                .statusCode(409)
                .assertThat().body("message", equalTo("Этот логин уже используется"));

        LoginData loginData = new LoginData(params.generatedLogin, params.generatedPassword);
        int id = RestAssured.with()
                .header("Content-Type", "application/json")
                .body(loginData)
                .post("/api/v1/courier/login")
                .then()
                .statusCode(200)
                .extract().body().path("id");

        String loginID = Integer.toString(id);
        RestAssured.with()
                .header("Content-Type", "application/json")
                .delete("/api/v1/courier/{id}", loginID)
                .then()
                .statusCode(200);
    }

    @Test
    @DisplayName("Check courier with NO Login cannot be created")
    @Description("check ability to create courier with Incorrect params - NO Login")
    public void checkCourierCreationWithNoLogin() {
        RandomParamsForCouriers params = new RandomParamsForCouriers();
        Courier testCourier = new Courier(params.generatedLogin, params.generatedPassword, params.generatedFirstName);
        testCourier.setLogin(null);
        RestAssured.with()
                .header("Content-Type", "application/json")
                .body(testCourier)
                .post("/api/v1/courier")
                .then()
                .statusCode(400)
                .assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Check courier with NO Password cannot be created")
    @Description("check ability to create courier with Incorrect params - NO Password")
    public void checkCourierCreationWithNoPassword() {
        RandomParamsForCouriers params = new RandomParamsForCouriers();
        Courier testCourier = new Courier(params.generatedLogin, params.generatedPassword, params.generatedFirstName);
        testCourier.setPassword(null);
        RestAssured.with()
                .header("Content-Type", "application/json")
                .body(testCourier)
                .post("/api/v1/courier")
                .then()
                .statusCode(400)
                .assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Check courier with NO FirstName can be created")
    @Description("check ability to create courier with Incorrect params - NO FirstName")
    public void checkCourierCreationWithNoFirstName() {
        RandomParamsForCouriers params = new RandomParamsForCouriers();
        Courier testCourier = new Courier(params.generatedLogin, params.generatedPassword, params.generatedFirstName);
        testCourier.setFirstname(null);
        RestAssured.with()
                .header("Content-Type", "application/json")
                .body(testCourier)
                .post("/api/v1/courier")
                .then()
                .statusCode(201)
                .assertThat().body("ok", equalTo(true));


        LoginData loginData = new LoginData(params.generatedLogin, params.generatedPassword);
        int id = RestAssured.with()
                .header("Content-Type", "application/json")
                .body(loginData)
                .post("/api/v1/courier/login")
                .then()
                .statusCode(200)
                .extract().body().path("id");

        String loginID = Integer.toString(id);
        RestAssured.with()
                .header("Content-Type", "application/json")
                .delete("/api/v1/courier/{id}", loginID)
                .then()
                .statusCode(200);
    }

    @Test
    @DisplayName("Check courier with NO Password cannot be created")
    @Description("check ability to create courier with Incorrect params - NO Password")
    public void checkCourierCreationWithNoParams() {
        Courier testCourier = new Courier();
        RestAssured.with()
                .header("Content-Type", "application/json")
                .body(testCourier)
                .post("/api/v1/courier")
                .then()
                .statusCode(400)
                .assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }
}

