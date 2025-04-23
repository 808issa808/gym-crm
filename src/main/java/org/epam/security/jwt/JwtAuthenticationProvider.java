package org.epam.security.jwt;

import lombok.RequiredArgsConstructor;
import org.epam.security.CustomUserDetailsService;
import org.epam.security.LoginAttemptService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {
    private final CustomUserDetailsService userDetailsService;
    private final JwtService jwtService;
    private final LoginAttemptService loginAttemptService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String token = (String) authentication.getCredentials();
        String username = jwtService.extractUserName(token);

        if (loginAttemptService.isBlocked(username)) {
            throw new LockedException("Account is temporarily locked");
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if (!jwtService.isTokenValid(token, userDetails)) {
            loginAttemptService.loginFailed(username);
            throw new BadCredentialsException("Invalid JWT token");
        }

        loginAttemptService.loginSucceeded(username);

        return new JwtAuthenticationToken(
                userDetails,
                token,
                userDetails.getAuthorities()
        );
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(JwtAuthenticationToken.class);
    }
}