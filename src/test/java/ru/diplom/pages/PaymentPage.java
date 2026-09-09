package ru.diplom.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import java.time.Duration;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
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
    private final SelenideElement continueButton = $(byText("Продолжить"));

    // =============================================
    // УВЕДОМЛЕНИЯ
    // =============================================
    private final SelenideElement successNotification = $(".notification_status_ok");
    private final SelenideElement errorNotification = $(".notification_status_error");

    // =============================================
    // СООБЩЕНИЯ ОБ ОШИБКАХ ПОД ПОЛЯМИ
    // Поиск через фильтрацию по названию поля
    // Селекторы с ДВУМЯ подчеркиваниями: .input__top и .input__sub
    // =============================================

    private ElementsCollection allLabels() {
        return $$(".input__top");
    }

    public SelenideElement getCardNumberError() {
        return allLabels()
                .filterBy(Condition.text("Номер карты"))
                .first()
                .closest(".input")
                .$(".input__sub");
    }

    public SelenideElement getMonthError() {
        return allLabels()
                .filterBy(Condition.text("Месяц"))
                .first()
                .closest(".input")
                .$(".input__sub");
    }

    public SelenideElement getYearError() {
        return allLabels()
                .filterBy(Condition.text("Год"))
                .first()
                .closest(".input")
                .$(".input__sub");
    }

    public SelenideElement getHolderError() {
        return allLabels()
                .filterBy(Condition.text("Владелец"))
                .first()
                .closest(".input")
                .$(".input__sub");
    }

    public SelenideElement getCvcError() {
        return allLabels()
                .filterBy(Condition.text("CVC/CVV"))
                .first()
                .closest(".input")
                .$(".input__sub");
    }

    // =============================================
    // МЕТОДЫ ДЛЯ РАБОТЫ С ФОРМОЙ
    // =============================================

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
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return this;
    }

    public PaymentPage checkSuccessMessage() {
        successNotification.shouldBe(Condition.visible, Duration.ofSeconds(15));
        return this;
    }

    public PaymentPage checkErrorMessage() {
        errorNotification.shouldBe(Condition.visible, Duration.ofSeconds(15));
        return this;
    }
}