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

    @DataProvider(name = "partial-user")
    public static Object[][] partialUser() {
        String randomName = RandomStringUtils.random(10, true, false);
        String randomJob = RandomStringUtils.random(8, true, false);
        User fullUser = new User(randomName, randomJob);

        return new Object[][] {
            { fullUser, new User(randomName, null) },
            { fullUser, new User(null, randomJob) },
        };
    }
}
