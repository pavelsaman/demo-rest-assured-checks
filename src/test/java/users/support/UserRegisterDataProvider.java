package users.support;

import org.testng.annotations.DataProvider;

public class UserRegisterDataProvider {

    @DataProvider(name = "valid-registration-credentials")
    public static Object[][] validRegistrationCredentials() {
        return new Object[][]{
            { new UserRegister("eve.holt@reqres.in", "pistol") },
            { new UserRegister("tracey.ramos@reqres.in", "pistol") },
        };
    }

    @DataProvider(name = "invalid-registration-credentials")
    public static Object[][] invalidRegistrationCredentials() {
        return new Object[][]{
            { new UserRegister("", "123") },
            { new UserRegister("@a.cz", "1") },
            { new UserRegister("a@a", "1") },
            { new UserRegister("a@.cz", "1") },
            { new UserRegister("a@a.cz", "") },
            { new UserRegister("a@a.cz", null) },
            { new UserRegister(null, "12") },
            { new UserRegister("eve.holt@reqres.in", "pisto") },
            { new UserRegister("eve@reqres.in", "pistol") },
        };
    }
}
