package practice.code;

import org.apache.commons.lang3.RandomStringUtils;

public class Code {

    public static String randomCode() {
        return RandomStringUtils.randomAlphanumeric(10).toUpperCase();
    }
}
