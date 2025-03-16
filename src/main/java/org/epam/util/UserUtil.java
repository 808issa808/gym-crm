package org.epam.util;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Random;
import java.util.function.Function;

@Component
public class UserUtil {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final Random RANDOM = new SecureRandom();

    public static String generateUsername(String firstName, String lastName, Function<String, Integer> getSimilarCount) {
        String baseUsername = firstName + "." + lastName;
        int similarCount = getSimilarCount.apply(baseUsername);

        return similarCount == 0 ? baseUsername : baseUsername + similarCount;
    }

    public static String generatePassword() {
        StringBuilder password = new StringBuilder(10);
        for (int i = 0; i < 10; i++) {
            password.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }
        return password.toString();
    }
}
