package ru.diplom.pages;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class MainPage {

    private final SelenideElement buyButton = $x("//span[text()='Купить']");
    private final SelenideElement creditButton = $x("//span[text()='Купить в кредит']");

    public MainPage open() {
        Selenide.open("http://localhost:8080");
        return this;
    }


    public PaymentPage buyWithCard() {
        buyButton.click();
        return new PaymentPage();
    }


    public PaymentPage buyWithCredit() {
        creditButton.click();
        return new PaymentPage();
    }
}
