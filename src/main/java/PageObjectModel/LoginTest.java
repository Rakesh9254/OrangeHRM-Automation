package PageObjectModel;

import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class LoginTest extends BaseTest {

    @Test
    public void testLoginFunctionality() {
        SoftAssert softAssert = new SoftAssert();
        driver.get("https://yoururl.com");

        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("yourUsername", "yourPassword");

        // Validate if login was successful
        String actualUserName = loginPage.getUserNameText();
        softAssert.assertEquals(actualUserName, "ExpectedUserName", "Username mismatch after login");

        // Add more validations here
        // softAssert.assertTrue(...);

        softAssert.assertAll(); // Collect all soft assert results
    }
}

