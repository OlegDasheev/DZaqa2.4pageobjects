package ru.netology.web.Test;

import lombok.val;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.Test;
import ru.netology.web.Data.DataHelper;
import ru.netology.web.Page.DashboardPage;
import ru.netology.web.Page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static org.hamcrest.Matchers.containsString;

class MoneyTransferTest {

    @Test
    void shouldLogin() {
        int amount = 1000;
        open("http://localhost:9999");
        val loginPage = new LoginPage();
        val authInfo = DataHelper.getAuthInfo();
        val verificationPage = loginPage.validLogin(authInfo);
        val verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        val dashboardPage = verificationPage.validVerify(verificationCode);
        val firstCardBalance = dashboardPage.getCardBalance(DataHelper.getFirstCardInfo());
        val secondCardBalance = dashboardPage.getCardBalance(DataHelper.getSecondCardInfo());
        val transferPage = dashboardPage.replenishButtonClick(DataHelper.getSecondCardInfo());
        transferPage.moneyTransfer(amount, DataHelper.getFirstCardInfo().getCardNumber());
        val dashboardPageAfterTransfer = new DashboardPage();
        dashboardPageAfterTransfer.getCardBalance(DataHelper.getFirstCardInfo());
        assertThat(dashboardPageAfterTransfer, containsString(String.valueOf(DataHelper.getFirstCardInfo())));
        assertThat(dashboardPageAfterTransfer, containsString(String.valueOf(DataHelper.getSecondCardInfo())));

    }

    private void assertThat(DashboardPage dashboardPageAfterTransfer, Matcher<String> containsString) {
    }

}
