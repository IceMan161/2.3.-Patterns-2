package ru.netilogy.test;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;
import static ru.netilogy.data.DataGenerator.Registration.*;
import static ru.netilogy.data.DataGenerator.getRandomLogin;
import static ru.netilogy.data.DataGenerator.getRandomPassword;

public class AuthTest {

    @BeforeEach
    void setup() {
        Configuration.headless = true;
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successfully login with active registered user")
    void shouldSuccessfulLoginIfRegisteredActiveUser() {

        var registeredUser = getRegisteredUser("active");
        $("[name ='login']").val(registeredUser.getLogin());
        $("[name ='password']").val(registeredUser.getPassword());
        $(withText("Продолжить")).click();

        $x("//*[contains(text(),'Личный кабинет')]").should(appear, Duration.ofSeconds(5));

    }

    @Test
    @DisplayName("Should get error message if login with not registered user")
    void shouldGetErrorIfNotRegisteredUser() {

        var notRegisteredUser = getUser("not registered");
        $("[name ='login']").val(notRegisteredUser.getLogin());
        $("[name ='password']").val(notRegisteredUser.getPassword());
        $(withText("Продолжить")).click();

        $("[data-test-id=error-notification] .notification__content").shouldHave(text("Неверно указан логин или пароль"));

    }

    @Test
    @DisplayName("Should get error message if login with blocked registered user")
    void shouldGetErrorIfBlockedUser() {

        var blockedUser = getRegisteredUser("blocked");
        $("[name ='login']").val(blockedUser.getLogin());
        $("[name ='password']").val(blockedUser.getPassword());
        $(withText("Продолжить")).click();

        $("[data-test-id=error-notification] .notification__content").shouldHave(text("Пользователь заблокирован"));

    }

    @Test
    @DisplayName("Should get error message if login with wrong login")
    void shouldGetErrorIfWrongLogin() {

        var registeredUser = getRegisteredUser("active");
        var wrongLogin = getRandomLogin();
        $("[name ='login']").val(wrongLogin);
        $("[name ='password']").val(registeredUser.getPassword());
        $(withText("Продолжить")).click();

        $("[data-test-id=error-notification] .notification__content").shouldHave(text("Неверно указан логин или пароль"));

    }

    @Test
    @DisplayName("Should get error message if login with wrong password")
    void shouldGetErrorIfWrongPassword() {

        var registeredUser = getRegisteredUser("active");
        var wrongPassword = getRandomPassword();
        $("[name ='login']").val(registeredUser.getLogin());
        $("[name ='password']").val(wrongPassword);
        $(withText("Продолжить")).click();

        $("[data-test-id=error-notification] .notification__content").shouldHave(text("Неверно указан логин или пароль"));

    }
}


