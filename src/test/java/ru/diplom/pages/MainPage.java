package ru.diplom.pages;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class MainPage {

    private final SelenideElement buyButton = $(byText("Купить"));
    private final SelenideElement creditButton = $(byText("Купить в кредит"));

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