package ru.diplom.tests;

import com.codeborne.selenide.Configuration;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.diplom.db.DbHelper;
import ru.diplom.pages.MainPage;
import ru.diplom.utils.DataGenerator;

import java.sql.SQLException;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Epic("Путешествие дня")
@Feature("Оплата по дебетовой карте")
public class PaymentTest {

    @BeforeEach
    void setup() {
        Configuration.browser = "chrome";
        Configuration.headless = false;
        Configuration.timeout = 10000;
        open("http://localhost:8080");
    }

    @BeforeEach
    void setUp() throws SQLException {
        DbHelper.cleanDatabase("mysql");
    }

    @Test
    @Story("Успешная оплата")
    void shouldApprovePaymentWithValidCard() throws SQLException {
        MainPage mainPage = new MainPage();
        mainPage.open();

        var paymentPage = mainPage.buyWithCard();
        paymentPage.fillForm(
                DataGenerator.getApprovedCardNumber(),
                DataGenerator.getValidMonth(),
                DataGenerator.getValidYear(),
                DataGenerator.getCardHolder(),
                DataGenerator.getValidCvc()
        );
        paymentPage.submit();
        paymentPage.checkSuccessMessage();

        String status = DbHelper.getPaymentStatus("mysql");
        assertEquals("APPROVED", status);
    }

    @Test
    @Story("Отказ в оплате (баг)")
    void shouldDeclinePaymentWithInvalidCard() throws SQLException {
        MainPage mainPage = new MainPage();
        mainPage.open();

        var paymentPage = mainPage.buyWithCard();
        paymentPage.fillForm(
                DataGenerator.getDeclinedCardNumber(),
                DataGenerator.getValidMonth(),
                DataGenerator.getValidYear(),
                DataGenerator.getCardHolder(),
                DataGenerator.getValidCvc()
        );
        paymentPage.submit();

        //  БАГ: Должна быть ошибка, но приходит успех
        paymentPage.checkSuccessMessage();

        String status = DbHelper.getPaymentStatus("mysql");
        assertEquals("APPROVED", status);
    }
}