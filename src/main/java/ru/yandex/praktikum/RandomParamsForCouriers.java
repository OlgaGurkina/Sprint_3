package ru.yandex.praktikum;

import org.apache.commons.lang3.RandomStringUtils;

public class RandomParamsForCouriers {
    private final int length = 5;
    private final boolean useLetters = true;
    private final boolean useNumbers = true;
    String generatedLogin;
    String generatedPassword;
    String generatedFirstName;

    public RandomParamsForCouriers() {
        this.generatedLogin = RandomStringUtils.random(length, useLetters, useNumbers);
        this.generatedPassword = RandomStringUtils.random(length, useLetters, useNumbers);
        this.generatedFirstName = RandomStringUtils.random(length, useLetters, useNumbers);
    }

}
