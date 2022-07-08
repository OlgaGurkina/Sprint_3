package ru.yandex.praktikum;


import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import java.util.List;
import static org.hamcrest.Matchers.*;

@RunWith(Parameterized.class)
public class TestOrderCreation {

    @Before
    public  void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }

    private List<String> color;

    public TestOrderCreation(List<String> color){
        this.color = color;
    }

    @Parameterized.Parameters
    public static List<List<String>> getColorForOrder() {
        List<List<String>> colorsInOrder;
        List<String> color1 = List.of("BLACK");
        List<String> color2 = List.of("GREY");
        List<String> color3 = List.of("BLACK", "GREY");
        List<String> color4 = List.of();
        colorsInOrder = List.of(color1, color2, color3, color4);
        return colorsInOrder;
    }

     @Test
     @DisplayName("Check Order creation")
     @Description("check order creation with different COLOR ")
    public void checkOrderCreation(){
        RandomParamsForOrder orderParam = new RandomParamsForOrder();
        Orders order = new Orders(orderParam.customerFirstName,
                orderParam.customerLastName,
                orderParam.customerAddress,
                orderParam.customerMetroStation,
                orderParam.customerPhone,
                orderParam.customerRentTime,
                orderParam.customerDeliveryDate,
                orderParam.customComment,
                color);

         int id = RestAssured.with()
                 .header("Content-Type", "application/json")
                 .body(order)
                 .post("/api/v1/orders")
                 .then()
                 .statusCode(201)
                 .assertThat().body("track", is(instanceOf(Integer.class)))
                 .extract().body().path("track");
         System.out.println(id);
         System.out.println(color);


         //делаю отмену заказа
         String orderId = Integer.toString(id);
         RestAssured.with()
                 .header("Content-Type", "application/json")
                 .queryParam("track", orderId)
                 .put("https://qa-scooter.praktikum-services.ru/api/v1/orders/cancel")
                 .then()
                 .statusCode(200);
     }
}
