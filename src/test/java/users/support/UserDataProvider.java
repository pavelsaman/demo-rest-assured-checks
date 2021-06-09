package users.support;

import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.DataProvider;

public class UserDataProvider {

    @DataProvider(name = "random-user")
    public static Object[][] randomUser() {
        String randomName = RandomStringUtils.random(10, true, false);
        String randomJob = RandomStringUtils.random(8, true, false);

        return new Object[][] { { new User(randomName, randomJob) } };
    }
}
