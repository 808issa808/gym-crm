package org.epam.security;

import lombok.RequiredArgsConstructor;
import org.epam.data.impl.TraineeRepositoryImpl;
import org.epam.data.impl.TrainerRepositoryImpl;
import org.epam.model.Trainee;
import org.epam.model.Trainer;
import org.epam.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final TraineeRepositoryImpl traineeRepository;
    private final TrainerRepositoryImpl trainerRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Trainee> trainee = traineeRepository.findByUsername(username);
        if (trainee.isPresent()) {
            return createUserDetails(trainee.get(), "TRAINEE");
        }

        Optional<Trainer> trainer = trainerRepository.findByUsername(username);
        if (trainer.isPresent()) {
            return createUserDetails(trainer.get(), "TRAINER");
        }

        throw new UsernameNotFoundException("User not found with username: " + username);
    }

    private UserDetails createUserDetails(User user, String role) {
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(role)
                .build();
    }
}