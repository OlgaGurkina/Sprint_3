package ru.yandex.praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.Matchers.*;

public class TestGetOrdersList {

    @Before
    public  void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }


    @Test
    @DisplayName("get order list for Courier")
    @Description("create Courier > Create Order > assign Order to Courier > get Order list for Courier")
    public void getOrderListForCourier(){
        //create courier and get his ID
        RandomParamsForCouriers params = new RandomParamsForCouriers();
        Courier testCourier = new Courier(params.generatedLogin, params.generatedPassword, params.generatedFirstName);
        RestAssured.with()
                .header("Content-Type", "application/json")
                .body(testCourier)
                .post("/api/v1/courier")
                .then()
                .statusCode(201);

        LoginData loginData = new LoginData(params.generatedLogin, params.generatedPassword);
        int courierId = RestAssured.with()
                .header("Content-Type", "application/json")
                .body(loginData)
                .post("/api/v1/courier/login")
                .then()
                .statusCode(200)
                .extract().body().path("id");
        System.out.println("courier id = "+ courierId);

       //create Order and get orderID
        RandomParamsForOrder orderParam = new RandomParamsForOrder();
        List<String> color = List.of("GREY");
        Orders order = new Orders(orderParam.customerFirstName,
                orderParam.customerLastName,
                orderParam.customerAddress,
                orderParam.customerMetroStation,
                orderParam.customerPhone,
                orderParam.customerRentTime,
                orderParam.customerDeliveryDate,
                orderParam.customComment, color );

        int orderTrack = RestAssured.with()
                .header("Content-Type", "application/json")
                .body(order)
                .post("/api/v1/orders")
                .then()
                .statusCode(201)
                .extract().body().path("track");
        System.out.println("track= " + orderTrack);

        int orderID = RestAssured.with()
                .header("Content-Type", "application/json")
                .queryParam("t", orderTrack)
                .get("/api/v1/orders/track")
                .then()
                .statusCode(200)
                .extract().body().path("order.id");
        System.out.println("order id = " + orderID);

        //accept Order by Courier (assign order to courier)
        RestAssured.with()
                .header("Content-Type", "application/json")
                .queryParam("courierId", courierId)
                .put("/api/v1/orders/accept/{id}", orderID)
                .then()
                .statusCode(200);

        //get order list for Courier
        //тест падает, т.к. при привязкке заказа к курьеру создается две записи
        //которые отличаются ид (у 1й - ид = orderID,  cоответствующий созднному заказу, a у 2й ид = orderID+1)
        Response response = RestAssured.with()
                .header("Content-Type", "application/json")
                .queryParam("courierId", courierId)
                .get("/api/v1/orders");
        response.then().assertThat().body("orders.id", contains(orderID))
                .and()
                .statusCode(200);


        //complete created order
        RestAssured.with()
                .header("Content-Type", "application/json")
                .put("/api/v1/orders/finish/{id}", orderID)
                .then()
                .statusCode(200);

        //delete created courier
        String loginID = Integer.toString(courierId);
        RestAssured.with()
                .header("Content-Type", "application/json")
                .delete("/api/v1/courier/{id}", loginID)
                .then()
                .statusCode(200);

    }

}
