package ru.diplom.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class PaymentPage {

    // =============================================
    // ПОЛЯ ФОРМЫ
    // =============================================
    private final SelenideElement cardNumberInput = $("[placeholder='0000 0000 0000 0000']");
    private final SelenideElement monthInput = $("[placeholder='08']");
    private final SelenideElement yearInput = $("[placeholder='22']");
    private final SelenideElement cardHolderInput = $x("//span[text()='Владелец']/..//input");
    private final SelenideElement cvcInput = $("[placeholder='999']");

    // =============================================
    // КНОПКА "ПРОДОЛЖИТЬ"
    // =============================================
    private final SelenideElement continueButton = $x("//span[text()='Продолжить']");

    // =============================================
    // УВЕДОМЛЕНИЯ (исправлены на основе реального HTML)
    // =============================================
    private final SelenideElement successNotification = $(".notification_status_ok");
    private final SelenideElement errorNotification = $(".notification_status_error");

    public PaymentPage fillForm(String cardNumber, String month, String year, String cardHolder, String cvc) {
        cardNumberInput.setValue(cardNumber);
        monthInput.setValue(month);
        yearInput.setValue(year);
        cardHolderInput.setValue(cardHolder);
        cvcInput.setValue(cvc);
        return this;
    }

    public PaymentPage submit() {
        continueButton.click();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return this;
    }

    public PaymentPage checkSuccessMessage() {
        successNotification.shouldBe(Condition.visible, Duration.ofSeconds(10));
        return this;
    }

    public PaymentPage checkErrorMessage() {
        errorNotification.shouldBe(Condition.visible, Duration.ofSeconds(10));
        return this;
    }
}