package org.epam.service;

import jakarta.transaction.Transactional;
import org.epam.data.impl.TraineeRepositoryImpl;
import org.epam.model.Trainee;
import org.epam.model.Trainer;
import org.epam.util.Authenticator;
import org.epam.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.epam.util.UserUtil.setStandardTrainee;

@Service
public class TraineeService {
    private TraineeRepositoryImpl traineeRepository;

    @Autowired
    public void setTraineeRepository(TraineeRepositoryImpl traineeRepository) {
        this.traineeRepository = traineeRepository;
    }

    @Transactional
    public void create(Trainee trainee) {
        String username = UserUtil.generateUsername(trainee.getFirstName(), trainee.getLastName(), traineeRepository::existsByUsername);
        String password = UserUtil.generatePassword();
        trainee.setUsername(username);
        trainee.setPassword(password);
        trainee.setActive(true);
        setStandardTrainee(trainee);
        traineeRepository.create(trainee);
    }

    @Transactional
    public Trainee update(Trainee trainee) {
        Authenticator.authenticateUser(trainee.getUsername(), trainee.getPassword(), traineeRepository::findByUsername);
        return traineeRepository.update(trainee);
    }

    public Trainee findByUsername(String username, String password, String searchedUsername) {
        Authenticator.authenticateUser(username, password, traineeRepository::findByUsername);
        return traineeRepository.findByUsername(searchedUsername)
                .orElseThrow(() -> new IllegalArgumentException("No trainee with given username : " + username + " exists."));
    }

    public List<Trainer> getNotMineTrainersByUsername(String username, String password) {
        Authenticator.authenticateUser(username, password, traineeRepository::findByUsername);
        return traineeRepository.getNotMineTrainersByUsername(username);
    }

    @Transactional
    public Trainee updateTrainersList(String username, String password, List<Trainer> updatedTrainers) {
        Trainee updating = Authenticator.authenticateUser(username, password, traineeRepository::findByUsername);
        return traineeRepository.updateTrainersList(updating, updatedTrainers);
    }

    @Transactional
    public Trainee changePassword(Trainee trainee, String password) {
        Authenticator.authenticateUser(trainee.getUsername(), trainee.getPassword(), traineeRepository::findByUsername);
        if (UserUtil.passwordFormatValidator(password)) {
            return traineeRepository.changePassword(trainee, password);
        } else {
            throw new IllegalArgumentException("New Password should be at least 10 chars long");
        }
    }

    @Transactional
    public void switchActivate(Trainee trainee) {
        Authenticator.authenticateUser(trainee.getUsername(), trainee.getPassword(), traineeRepository::findByUsername);
        traineeRepository.switchActivate(trainee.getUsername());
    }

    @Transactional
    public void deleteTraineeByUsername(String username, String password) {
        Authenticator.authenticateUser(username, password, traineeRepository::findByUsername);
        traineeRepository.deleteByUsername(username);
    }
}