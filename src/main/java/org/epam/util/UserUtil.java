package org.epam.util;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Random;
import java.util.function.Predicate;

@Component
public class UserUtil {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final Random RANDOM = new SecureRandom();

    public static String generateUsername(String firstName, String lastName, Predicate<String> usernameExists) {
        String baseUsername = firstName + "." + lastName;
        String username = baseUsername;
        int counter = 1;

        while (usernameExists.test(username)) {
            username = baseUsername + counter;
            counter++;
        }
        return username;
    }

    public static String generatePassword() {
        StringBuilder password = new StringBuilder(10);
        for (int i = 0; i < 10; i++) {
            password.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }
        return password.toString();
    }
    public static boolean passwordFormatValidator(String password)
    {
        return password.length()>=10;
    }
}
