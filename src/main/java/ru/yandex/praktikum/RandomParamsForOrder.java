package ru.yandex.praktikum;

import org.apache.commons.lang3.RandomStringUtils;

import java.time.LocalDate;
import java.util.Random;

public class RandomParamsForOrder {

    public String customerFirstName;
    public String customerLastName;
    public String customerAddress;
    public String customerMetroStation;
    public String customerPhone;
    public int customerRentTime;
    public String customerDeliveryDate;
    public String customComment;

    public RandomParamsForOrder() {
        int length = 5;
        boolean useLetters = true;
        boolean useNumbers = true;

        this.customerFirstName = RandomStringUtils.random(length, useLetters, useNumbers);
        this.customerLastName = RandomStringUtils.random(length, useLetters, useNumbers);
        length = 10;
        this.customerAddress = RandomStringUtils.random(length, useLetters, useNumbers);
        this.customComment = RandomStringUtils.random(length, useLetters, useNumbers);
        length = 1;
        useLetters = false;
        this.customerMetroStation = RandomStringUtils.random(length, useLetters, useNumbers);
        length = 11;
        this.customerPhone = RandomStringUtils.random(length, useLetters, useNumbers);
        this.customerDeliveryDate = String.valueOf(LocalDate.now());
        Random rand  = new Random();
        this.customerRentTime = rand.nextInt();
    }


}
