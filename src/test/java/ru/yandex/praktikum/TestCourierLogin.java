package ru.yandex.praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.hamcrest.Matchers.*;

public class TestCourierLogin {
    static RandomParamsForCouriers params = new RandomParamsForCouriers();

    @BeforeClass
    public static void setUp() {
        RestAssured.baseURI= "http://qa-scooter.praktikum-services.ru";
        Courier testCourier = new Courier(params.generatedLogin, params.generatedPassword, params.generatedFirstName);
        RestAssured.with()
                .header("Content-Type", "application/json")
                .body(testCourier)
                .post("/api/v1/courier")
                .then()
                .statusCode(201);
    }

    @Test
    @DisplayName("Check login - positive case with valid data")
    @Description(" login with correct login+password")
    public void checkLoginWithCorrectData(){
        LoginData loginData = new LoginData(params.generatedLogin, params.generatedPassword);
        RestAssured.with()
                .header("Content-Type", "application/json")
                .body(loginData)
                .post("/api/v1/courier/login")
                .then()
                .statusCode(200)
                .assertThat().body("id", is(instanceOf(Integer.class)));

    }
    @Test
    @DisplayName("Check login - negative case with not valid data")
    @Description(" login with no login")
    public void checkLoginWithNoLogin(){
        LoginData loginData = new LoginData(params.generatedLogin, params.generatedPassword);
        loginData.setLogin(null);
        RestAssured.with()
                .header("Content-Type", "application/json")
                .body(loginData)
                .post("/api/v1/courier/login")
                .then()
                .statusCode(400)
                .assertThat().body("message", equalTo("Недостаточно данных для входа"));

    }

    @Test
    // тест падает, т.к. работа сервиса не соответствует документации
    // при отсутсвии пароля ожидается 400 код и сообщение "Недостаточно данных для входа"
    // на практике падает 504 Service unavailable
    @DisplayName("Check login - negative case with not valid data")
    @Description(" login with no Password")
    public void checkLoginWithNoPassword(){
        LoginData loginData = new LoginData(params.generatedLogin, params.generatedPassword);
        loginData.setPassword(null);
        RestAssured.with()
                .header("Content-Type", "application/json")
                .body(loginData)
                .post("/api/v1/courier/login")
                .then()
                .statusCode(400)
                .assertThat().body("message", equalTo("Недостаточно данных для входа"));

    }

    @Test
    // тест падает, т.к. работа сценария не описана документации
    // при отсутсвии пароля и логина предполагаю логику как и при отсутствии или логина, или пароля
    // на практике падает 504 Service unavailable
    @DisplayName("Check login with NO Login and Password")
    @Description("login and password are not set")
    public void checkLoginWithNoData(){
        LoginData loginData = new LoginData();
        loginData.setLogin("incorrect");
        RestAssured.with()
                .header("Content-Type", "application/json")
                .body(loginData)
                .post("/api/v1/courier/login")
                .then()
                .statusCode(400)
                .assertThat().body("message", equalTo("Недостаточно данных для входа"));

    }

    @Test
    @DisplayName("Check login - incorrect pair login+password")
    @Description("incorrect password for existing login")
    public void checkLoginWithIncorrectPassword(){
        LoginData loginData = new LoginData(params.generatedLogin, params.generatedPassword);
        loginData.setPassword("incorrect");
        RestAssured.with()
                .header("Content-Type", "application/json")
                .body(loginData)
                .post("/api/v1/courier/login")
                .then()
                .statusCode(404)
                .assertThat().body("message", equalTo("Учетная запись не найдена"));

    }

    @Test
    @DisplayName("Check login - incorrect pair login+password")
    @Description("incorrect login (password exists)")
    public void checkLoginWithIncorrectLogin(){
        LoginData loginData = new LoginData(params.generatedLogin, params.generatedPassword);
        loginData.setLogin("incorrect");
        RestAssured.with()
                .header("Content-Type", "application/json")
                .body(loginData)
                .post("/api/v1/courier/login")
                .then()
                .statusCode(404)
                .assertThat().body("message", equalTo("Учетная запись не найдена"));

    }

    @Test
    @DisplayName("Check login for NOT Existing Courier")
    @Description("login and password do not exist(courier with such data is not created)")
    public void checkLoginWithNotExistingCourier(){
        RandomParamsForCouriers notExistingCourierParams = new RandomParamsForCouriers();
        LoginData loginData = new LoginData(notExistingCourierParams.generatedLogin, notExistingCourierParams.generatedPassword);
        RestAssured.with()
                .header("Content-Type", "application/json")
                .body(loginData)
                .post("/api/v1/courier/login")
                .then()
                .statusCode(404)
                .assertThat().body("message", equalTo("Учетная запись не найдена"));

    }

    @AfterClass
    public static void cleanUp(){
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
}
