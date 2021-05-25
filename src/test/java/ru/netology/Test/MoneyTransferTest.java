package ru.netology.Test;

import lombok.val;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.Test;
import ru.netology.Data.DataHelper;
import ru.netology.Page.DashboardPage;
import ru.netology.Page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static org.hamcrest.Matchers.containsString;
import static ru.netology.Data.DataHelper.getFirstCardInfo;
import static ru.netology.Data.DataHelper.getSecondCardInfo;

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
        val firstCardBalance = dashboardPage.getCardBalance(getFirstCardInfo());
        val secondCardBalance = dashboardPage.getCardBalance(getSecondCardInfo());
        val transferPage = dashboardPage.replenishButtonClick(getSecondCardInfo());
        transferPage.moneyTransfer(amount, getFirstCardInfo().getCardNumber());
        val dashboardPageAfterTransfer = new DashboardPage();
        dashboardPageAfterTransfer.getCardBalance(getFirstCardInfo());
        assertThat(dashboardPageAfterTransfer, containsString(String.valueOf(getFirstCardInfo())));
        assertThat(dashboardPageAfterTransfer, containsString(String.valueOf(getSecondCardInfo())));

    }

    private void assertThat(DashboardPage dashboardPageAfterTransfer, Matcher<String> containsString) {
    }

}
