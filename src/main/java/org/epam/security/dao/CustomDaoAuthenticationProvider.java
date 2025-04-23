package org.epam.security.dao;

import lombok.RequiredArgsConstructor;
import org.epam.security.LoginAttemptService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

@RequiredArgsConstructor
public class CustomDaoAuthenticationProvider extends DaoAuthenticationProvider {

    private final LoginAttemptService loginAttemptService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();

        if (loginAttemptService.isBlocked(username)) {
            throw new LockedException("Account is temporarily locked");
        }
        try {
            Authentication auth = super.authenticate(authentication);
            loginAttemptService.loginSucceeded(username);
            return auth;
        } catch (BadCredentialsException ex) {
            loginAttemptService.loginFailed(username);
            throw ex;
        }
    }
}