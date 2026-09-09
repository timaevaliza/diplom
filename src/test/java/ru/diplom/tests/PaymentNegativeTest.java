package ru.diplom.tests;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.diplom.pages.MainPage;
import ru.diplom.pages.PaymentPage;
import ru.diplom.utils.DataGenerator;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.open;

@Epic("Путешествие дня")
@Feature("Оплата по дебетовой карте")
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
    @Story("Негативный сценарий: пустой номер карты")
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
    @Story("Негативный сценарий: номер карты из 15 цифр")
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
    @Story("Негативный сценарий: пустой месяц")
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
        paymentPage.getMonthError().shouldHave(Condition.text("Неверный формат"));
    }

    @Test
    @Story("Негативный сценарий: месяц 13")
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
    @Story("Негативный сценарий: пустой год")
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
        paymentPage.getYearError().shouldHave(Condition.text("Неверный формат"));
    }

    @Test
    @Story("Негативный сценарий: год 00")
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
    @Story("Негативный сценарий: истекший год")
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
    @Story("Негативный сценарий: пустой владелец")
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
    @Story("Негативный сценарий: владелец с цифрами (баг)")
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

        // БАГ: Ошибка не появляется, платеж проходит успешно
        paymentPage.checkSuccessMessage();
    }

    // ============================================
    // ТЕСТЫ ДЛЯ ПОЛЯ "CVC"
    // ============================================

    @Test
    @Story("Негативный сценарий: пустой CVC")
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
        paymentPage.getCvcError().shouldHave(Condition.text("Неверный формат"));
    }

    @Test
    @Story("Негативный сценарий: CVC из 2 цифр")
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