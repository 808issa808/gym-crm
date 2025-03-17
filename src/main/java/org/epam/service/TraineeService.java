package org.epam.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.epam.data.impl.TraineeRepositoryImpl;
import org.epam.model.Trainee;
import org.epam.model.Trainer;
import org.epam.util.Authenticator;
import org.epam.util.UserUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class TraineeService {
    private final TraineeRepositoryImpl traineeRepository;

    @Transactional
    public void create(Trainee trainee) {
        String username = UserUtil.generateUsername(trainee.getFirstName(), trainee.getLastName(), traineeRepository::countByUsernamePrefix);
        String password = UserUtil.generatePassword();
        trainee.setUsername(username);
        trainee.setPassword(password);
        traineeRepository.save(trainee);
    }

    @Transactional
    public Trainee update(@Valid Trainee trainee) {
        Authenticator.authenticateUser(trainee.getUsername(), trainee.getPassword(), traineeRepository::findByUsername);
        return traineeRepository.save(trainee);
    }

    public Trainee findByUsername(String username, String password, String searchedUsername) {
        Authenticator.authenticateUser(username, password, traineeRepository::findByUsername);
        return traineeRepository.findByUsername(searchedUsername).orElseThrow(() -> new IllegalArgumentException("No trainee with given username : " + username + " exists."));
    }

    public List<Trainer> getNotMineTrainersByUsername(String username, String password) {
        Authenticator.authenticateUser(username, password, traineeRepository::findByUsername);
        return traineeRepository.getNotMineTrainersByUsername(username);
    }

    @Transactional
    public Trainee updateTrainersList(String username, String password, @Valid List<Trainer> updatedTrainers) {
        Trainee updating = Authenticator.authenticateUser(username, password, traineeRepository::findByUsername);
        updating.setTrainers(updatedTrainers);
        return traineeRepository.save(updating);
    }

    @Transactional
    public Trainee changePassword(@Valid Trainee trainee, String password) {
        Authenticator.authenticateUser(trainee.getUsername(), trainee.getPassword(), traineeRepository::findByUsername);
        if (password.trim().length() >= 10) {
            trainee.setPassword(password);
            return traineeRepository.save(trainee);
        } else {
            throw new IllegalArgumentException("New Password should be at least 10 chars long");
        }
    }

    @Transactional
    public void switchActivate(@Valid Trainee trainee) {
        Authenticator.authenticateUser(trainee.getUsername(), trainee.getPassword(), traineeRepository::findByUsername);
        trainee.setActive(!trainee.isActive());
        traineeRepository.save(trainee);
    }

    @Transactional
    public void deleteTraineeByUsername(String username, String password) {
        Trainee trainee = Authenticator.authenticateUser(username, password, traineeRepository::findByUsername);
        traineeRepository.delete(trainee);
    }
}