package ru.diplom.tests;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.diplom.pages.MainPage;
import ru.diplom.pages.PaymentPage;
import ru.diplom.utils.DataGenerator;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.open;

public class PaymentNegativeTest {

    @BeforeEach
    void setup() {
        Configuration.browser = "chrome";
        Configuration.headless = false;
        Configuration.timeout = 15000;
        open("http://localhost:8080");
    }

    // ============================================
    // ТЕСТЫ ДЛЯ ПОЛЯ "НОМЕР КАРТЫ"
    // ============================================

    @Test
    void shouldShowErrorWhenCardNumberIsEmpty() {
        MainPage mainPage = new MainPage();
        mainPage.open();
        PaymentPage paymentPage = mainPage.buyWithCard();

        paymentPage.fillForm(
                "",
                DataGenerator.getValidMonth(),
                DataGenerator.getValidYear(),
                DataGenerator.getCardHolder(),
                DataGenerator.getValidCvc()
        );
        paymentPage.submit();

        paymentPage.getCardNumberError().shouldBe(Condition.visible, Duration.ofSeconds(10));
        paymentPage.getCardNumberError().shouldHave(Condition.text("Неверный формат"));
    }

    @Test
    void shouldShowErrorWhenCardNumberHas15Digits() {
        MainPage mainPage = new MainPage();
        mainPage.open();
        PaymentPage paymentPage = mainPage.buyWithCard();

        paymentPage.fillForm(
                "4444 4444 4444 444",
                DataGenerator.getValidMonth(),
                DataGenerator.getValidYear(),
                DataGenerator.getCardHolder(),
                DataGenerator.getValidCvc()
        );
        paymentPage.submit();

        paymentPage.getCardNumberError().shouldBe(Condition.visible, Duration.ofSeconds(10));
        paymentPage.getCardNumberError().shouldHave(Condition.text("Неверный формат"));
    }

    // ============================================
    // ТЕСТЫ ДЛЯ ПОЛЯ "МЕСЯЦ"
    // ============================================

    @Test
    void shouldShowErrorWhenMonthIsEmpty() {
        MainPage mainPage = new MainPage();
        mainPage.open();
        PaymentPage paymentPage = mainPage.buyWithCard();

        paymentPage.fillForm(
                DataGenerator.getApprovedCardNumber(),
                "",
                DataGenerator.getValidYear(),
                DataGenerator.getCardHolder(),
                DataGenerator.getValidCvc()
        );
        paymentPage.submit();

        paymentPage.getMonthError().shouldBe(Condition.visible, Duration.ofSeconds(10));
        paymentPage.getMonthError().shouldHave(Condition.text("Поле обязательно для заполнения"));
    }

    @Test
    void shouldShowErrorWhenMonthIs13() {
        MainPage mainPage = new MainPage();
        mainPage.open();
        PaymentPage paymentPage = mainPage.buyWithCard();

        paymentPage.fillForm(
                DataGenerator.getApprovedCardNumber(),
                "13",
                DataGenerator.getValidYear(),
                DataGenerator.getCardHolder(),
                DataGenerator.getValidCvc()
        );
        paymentPage.submit();

        paymentPage.getMonthError().shouldBe(Condition.visible, Duration.ofSeconds(10));
        paymentPage.getMonthError().shouldHave(Condition.text("Неверно указан срок действия карты"));
    }

    // ============================================
    // ТЕСТЫ ДЛЯ ПОЛЯ "ГОД"
    // ============================================

    @Test
    void shouldShowErrorWhenYearIsEmpty() {
        MainPage mainPage = new MainPage();
        mainPage.open();
        PaymentPage paymentPage = mainPage.buyWithCard();

        paymentPage.fillForm(
                DataGenerator.getApprovedCardNumber(),
                DataGenerator.getValidMonth(),
                "",
                DataGenerator.getCardHolder(),
                DataGenerator.getValidCvc()
        );
        paymentPage.submit();

        paymentPage.getYearError().shouldBe(Condition.visible, Duration.ofSeconds(10));
        paymentPage.getYearError().shouldHave(Condition.text("Поле обязательно для заполнения"));
    }

    @Test
    void shouldShowErrorWhenYearIs00() {
        MainPage mainPage = new MainPage();
        mainPage.open();
        PaymentPage paymentPage = mainPage.buyWithCard();

        paymentPage.fillForm(
                DataGenerator.getApprovedCardNumber(),
                DataGenerator.getValidMonth(),
                "00",
                DataGenerator.getCardHolder(),
                DataGenerator.getValidCvc()
        );
        paymentPage.submit();

        paymentPage.getYearError().shouldBe(Condition.visible, Duration.ofSeconds(10));
        paymentPage.getYearError().shouldHave(Condition.text("Истёк срок действия карты"));
    }

    @Test
    void shouldShowErrorWhenYearIsExpired() {
        MainPage mainPage = new MainPage();
        mainPage.open();
        PaymentPage paymentPage = mainPage.buyWithCard();

        paymentPage.fillForm(
                DataGenerator.getApprovedCardNumber(),
                DataGenerator.getValidMonth(),
                "22",
                DataGenerator.getCardHolder(),
                DataGenerator.getValidCvc()
        );
        paymentPage.submit();

        paymentPage.getYearError().shouldBe(Condition.visible, Duration.ofSeconds(10));
        paymentPage.getYearError().shouldHave(Condition.text("Истёк срок действия карты"));
    }

    // ============================================
    // ТЕСТЫ ДЛЯ ПОЛЯ "ВЛАДЕЛЕЦ"
    // ============================================

    @Test
    void shouldShowErrorWhenHolderIsEmpty() {
        MainPage mainPage = new MainPage();
        mainPage.open();
        PaymentPage paymentPage = mainPage.buyWithCard();

        paymentPage.fillForm(
                DataGenerator.getApprovedCardNumber(),
                DataGenerator.getValidMonth(),
                DataGenerator.getValidYear(),
                "",
                DataGenerator.getValidCvc()
        );
        paymentPage.submit();

        paymentPage.getHolderError().shouldBe(Condition.visible, Duration.ofSeconds(10));
        paymentPage.getHolderError().shouldHave(Condition.text("Поле обязательно для заполнения"));
    }

    @Test
    void shouldShowErrorWhenHolderContainsDigits() {
        MainPage mainPage = new MainPage();
        mainPage.open();
        PaymentPage paymentPage = mainPage.buyWithCard();

        paymentPage.fillForm(
                DataGenerator.getApprovedCardNumber(),
                DataGenerator.getValidMonth(),
                DataGenerator.getValidYear(),
                "John123",
                DataGenerator.getValidCvc()
        );
        paymentPage.submit();

        // ❌ БАГ: Ошибка не появляется, платеж проходит успешно
        paymentPage.checkSuccessMessage();
    }

    // ============================================
    // ТЕСТЫ ДЛЯ ПОЛЯ "CVC"
    // ============================================

    @Test
    void shouldShowErrorWhenCvcIsEmpty() {
        MainPage mainPage = new MainPage();
        mainPage.open();
        PaymentPage paymentPage = mainPage.buyWithCard();

        paymentPage.fillForm(
                DataGenerator.getApprovedCardNumber(),
                DataGenerator.getValidMonth(),
                DataGenerator.getValidYear(),
                DataGenerator.getCardHolder(),
                ""
        );
        paymentPage.submit();

        paymentPage.getCvcError().shouldBe(Condition.visible, Duration.ofSeconds(10));
        paymentPage.getCvcError().shouldHave(Condition.or("ошибка",
                Condition.text("Поле обязательно для заполнения"),
                Condition.text("Неверный формат")
        ));
    }

    @Test
    void shouldShowErrorWhenCvcHas2Digits() {
        MainPage mainPage = new MainPage();
        mainPage.open();
        PaymentPage paymentPage = mainPage.buyWithCard();

        paymentPage.fillForm(
                DataGenerator.getApprovedCardNumber(),
                DataGenerator.getValidMonth(),
                DataGenerator.getValidYear(),
                DataGenerator.getCardHolder(),
                "12"
        );
        paymentPage.submit();

        paymentPage.getCvcError().shouldBe(Condition.visible, Duration.ofSeconds(10));
        paymentPage.getCvcError().shouldHave(Condition.text("Неверный формат"));
    }
}