package org.epam.security;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.epam.security.dao.CustomDaoAuthenticationProvider;
import org.epam.security.jwt.JwtAuthenticationProvider;
import org.epam.security.jwt.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationProvider jwtAuthenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, CustomDaoAuthenticationProvider daoAuthenticationProvider, JwtFilter jwtFilter) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(handling -> handling
                        .authenticationEntryPoint((request, response, ex) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage()))
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/trainees/signup").permitAll()
                        .requestMatchers("/trainees/login").permitAll()
                        .requestMatchers("/trainers/signup").permitAll()
                        .requestMatchers("/trainers/login").permitAll()
                        .requestMatchers("/trainees/**").hasRole("TRAINEE")
                        .requestMatchers("/trainers/**").hasRole("TRAINER")
                        .requestMatchers("/trainings/**").hasRole("TRAINER")
                        .anyRequest().authenticated()
                )
                .authenticationProvider(jwtAuthenticationProvider)
                .authenticationProvider(daoAuthenticationProvider)
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setExposedHeaders(List.of("Authorization"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtFilter jwtFilter(AuthenticationManager authenticationManager) {
        return new JwtFilter(authenticationManager);
    }

    @Bean
    public CustomDaoAuthenticationProvider customDaoAuthenticationProvider(LoginAttemptService loginAttemptService,
                                                                           CustomUserDetailsService userDetailsService,
                                                                           PasswordEncoder passwordEncoder) {
        CustomDaoAuthenticationProvider provider = new CustomDaoAuthenticationProvider(loginAttemptService);
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            JwtAuthenticationProvider jwtAuthenticationProvider,
            CustomDaoAuthenticationProvider daoAuthenticationProvider
    ) {
        return new ProviderManager(List.of(jwtAuthenticationProvider, daoAuthenticationProvider));
    }
}