package users.support;

import config.Config;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.DataProvider;

public class LoginDataProvider {

    @DataProvider(name = "valid-credentials")
    public static Object[][] validCredentials() {

        Login validCredentials = new Login(
            Config.getUserCredentials().get("email"),
            Config.getUserCredentials().get("password")
        );
        return new Object[][] { { validCredentials } };
    }

    @DataProvider(name = "invalid-credentials")
    public static Object[][] invalidCredentials() {

        Login invalidCredentials = new Login(
            Config.getUserCredentials().get("email"),
            RandomStringUtils.random(10, true, true)
        );
        return new Object[][] {
            { invalidCredentials },
            { new Login(Config.getUserCredentials().get("email"), "") },
            { new Login("", Config.getUserCredentials().get("password")) },
            { new Login(null, Config.getUserCredentials().get("password")) },
            { new Login(Config.getUserCredentials().get("email"), null) },
            { new Login("a@a.cz", System.getenv("PASSWORD")) },
        };
    }
}
