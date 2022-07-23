import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.pseudo;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;


class BankServiceTest {
    @BeforeEach
    void openBrowser() {
        open("http://localhost:9999/");
    }

    // POSITIVE
    @Test
    void shouldSendForm() {
        SelenideElement form = $(".form");
        form.$("[data-test-id='name'] input").setValue("Иванов-Петров Иван");
        form.$("[data-test-id='phone'] input").setValue("+79099876545");
        form.$("[data-test-id='agreement']").click();
        form.$("button").click();
        $("[data-test-id='order-success']").shouldHave(exactText("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время."));
    }

    // NEGATIVE
    @Test
    void shouldNoValidNameWithEmptyField() {
        SelenideElement form = $(".form");
        form.$("[data-test-id='phone'] input").setValue("+79099876545");
        form.$("[data-test-id='agreement']").click();
        form.$(".button").click();
        $("[data-test-id='name'].input_invalid .input__sub").shouldHave(exactText("Поле обязательно для заполнения"));
    }

    @Test
    void shouldNoValidPhoneWithEmptyField() {
        SelenideElement form = $(".form");
        form.$("[data-test-id='name'] input").setValue("Иванов-Петров Иван");
        form.$("[data-test-id='agreement']").click();
        form.$(".button").click();
        $("[data-test-id='phone'].input_invalid .input__sub").shouldHave(exactText("Поле обязательно для заполнения"));
    }

    @Test
    void shouldNoSendFormWithEmptyCheckbox() {
        SelenideElement form = $(".form");
        form.$("[data-test-id='name'] input").setValue("Иванов-Петров Иван");
        form.$("[data-test-id='phone'] input").setValue("+79099876545");
        form.$(".button").click();
        $("[data-test-id='agreement'].input_invalid").shouldHave(pseudo(":checkbox__text", "color", "rgb(255, 92, 92)"));
    }

    @Test
    void shouldNoValidNameWithNoValidData() {
        SelenideElement form = $(".form");
        form.$("[data-test-id='name'] input").setValue("+79099876545");
        form.$("[data-test-id='phone'] input").setValue("+79099876545");
        form.$("[data-test-id='agreement']").click();
        form.$(".button").click();
        $("[data-test-id='name'].input_invalid .input__sub").shouldHave(exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    void shouldNoValidPhoneWithNoValidData() {
        SelenideElement form = $(".form");
        form.$("[data-test-id='name'] input").setValue("Иванов-Петров Иван");
        form.$("[data-test-id='phone'] input").setValue("Иванов-Петров Иван");
        form.$("[data-test-id='agreement']").click();
        form.$(".button").click();
        $("[data-test-id='phone'].input_invalid .input__sub").shouldHave(exactText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

}