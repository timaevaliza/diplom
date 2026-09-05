package ru.diplom.tests;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.diplom.db.DbHelper;
import ru.diplom.pages.MainPage;
import ru.diplom.utils.DataGenerator;

import java.sql.SQLException;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PaymentTest {

    @BeforeEach
    void setup() {
        Configuration.browser = "chrome";
        Configuration.headless = false;
        open("http://localhost:8080");
    }

    @BeforeEach
    void setUp() throws SQLException {
        DbHelper.cleanDatabase("mysql");
    }

    @Test
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
        paymentPage.checkErrorMessage();

        String status = DbHelper.getPaymentStatus("mysql");
        assertEquals("DECLINED", status);
    }
}
