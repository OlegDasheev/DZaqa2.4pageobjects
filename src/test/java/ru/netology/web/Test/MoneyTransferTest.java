package ru.netology.web.Test;

import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.netology.web.Data.DataHelper;
import ru.netology.web.Page.DashboardPage;
import ru.netology.web.Page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.*;

public class MoneyTransferTest {
    int begBalance1;
    int begBalance2;
    int endBalance1;
    int endBalance2;
    int sum;
    DashboardPage dashboardPage;

    @BeforeEach
    void SetUp() {
        open("http://localhost:9999");
        val loginPage = new LoginPage();
        val authInfo = DataHelper.getAuthInfo();
        val verificationPage = loginPage.validLogin(authInfo);
        val verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        dashboardPage = verificationPage.validVerify(verificationCode);
        begBalance1 = dashboardPage.getBalance(dashboardPage.card1);
        begBalance2 = dashboardPage.getBalance(dashboardPage.card2);
    }

    @Test
    @DisplayName("Перевод денег сo второй карты на первую")
    void shouldTransferMoneyFromSecondToFirstCard() {
        sum = 100;
        val topUpPage = dashboardPage.clickTopUp(dashboardPage.card1);
        val cardNum = DataHelper.getFirstCardInfo().getCardNumber();
        val dashboardPage2 = topUpPage.successfulTopUp(Integer.toString(sum), cardNum);
        endBalance1 = dashboardPage2.getBalance(dashboardPage2.card1);
        endBalance2 = dashboardPage2.getBalance(dashboardPage2.card2);
        assertEquals(begBalance1 + sum, endBalance1);
        assertEquals(begBalance2 - sum, endBalance2);
    }

    @Test
    @DisplayName("Перевод денег с первой карты на вторую")
    void shouldTransferMoneyFromFirstToSecondCard() {
        sum = 100;
        val topUpPage = dashboardPage.clickTopUp(dashboardPage.card2);
        val cardNum = DataHelper.getSecondCardInfo().getCardNumber();
        val dashboardPage2 = topUpPage.successfulTopUp(Integer.toString(sum), cardNum);
        endBalance1 = dashboardPage2.getBalance(dashboardPage2.card1);
        endBalance2 = dashboardPage2.getBalance(dashboardPage2.card2);
        assertEquals(begBalance1 - sum, endBalance1);
        assertEquals(begBalance2 + sum, endBalance2);
    }

    @Test
    @DisplayName("Не должен переводить больше, чем есть на карте")
    void shouldNotTransferMoreThanAvailable() {
        sum = begBalance1 + 100;
        val topUpPage = dashboardPage.clickTopUp(dashboardPage.card2);
        val cardNum = DataHelper.getFirstCardInfo().getCardNumber();
        topUpPage.unsuccessfulTopUp(Integer.toString(sum), cardNum);
    }
}
