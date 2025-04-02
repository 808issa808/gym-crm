package org.epam.service;

import lombok.RequiredArgsConstructor;
import org.epam.data.impl.TraineeRepositoryImpl;
import org.epam.data.impl.TrainerRepositoryImpl;
import org.epam.model.Trainee;
import org.epam.model.Trainer;
import org.epam.util.Authenticator;
import org.epam.util.UserUtil;
import org.epam.web.exp.ForbiddenException;
import org.epam.web.dto.users.ChangeLoginRequest;
import org.epam.web.dto.users.UserCredentialsDto;
import org.epam.web.dto.users.trainee.TraineeDto;
import org.epam.web.dto.users.trainee.TraineeRegistrationRequest;
import org.epam.web.dto.users.trainee.TraineeWithListDto;
import org.epam.web.dto.users.trainee.TrainerListPutDto;
import org.epam.web.dto.users.trainer.TrainerShortDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class TraineeService {
    private final TraineeRepositoryImpl traineeRepository;
    private TrainerRepositoryImpl trainerRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public void setTrainerRepository(TrainerRepositoryImpl trainerRepository) {
        this.trainerRepository = trainerRepository;
    }
    @Transactional
    public UserCredentialsDto create(TraineeRegistrationRequest registrationDto) {
        Trainee trainee = modelMapper.map(registrationDto, Trainee.class);

        String username = UserUtil.generateUsername(registrationDto.getFirstName(), registrationDto.getLastName(), traineeRepository::countByUsernamePrefix);
        String password = UserUtil.generatePassword();

        trainee.setUsername(username);
        trainee.setPassword(password);

        Trainee saved = traineeRepository.save(trainee);

        UserCredentialsDto response = modelMapper.map(saved, UserCredentialsDto.class);
        return response;
    }

    public void login(UserCredentialsDto loginDto) {
        Authenticator.authenticateUser(loginDto.getUsername(), loginDto.getPassword(), traineeRepository::findByUsername);
    }
    @Transactional
    public Trainee changePassword(ChangeLoginRequest changeDto) {
        Trainee trainee = Authenticator.authenticateUser(changeDto.getUsername(), changeDto.getPassword(), traineeRepository::findByUsername);
        trainee.setPassword(changeDto.getNewPassword());

        return traineeRepository.save(trainee);
    }

    @Transactional
    public TraineeWithListDto update(UserCredentialsDto auth, TraineeDto traineeDto) {
        var trainee = Authenticator.authenticateUser(auth.getUsername(), auth.getPassword(), traineeRepository::findByUsername);

        if (!auth.getUsername().equals(traineeDto.getUsername())) {
            throw new ForbiddenException("Нельзя изменить данные другого пользователя");
        }

        modelMapper.map(traineeDto, trainee);
        traineeRepository.save(trainee);

        return modelMapper.map(trainee,TraineeWithListDto.class);
    }

    public TraineeWithListDto findByUsername(UserCredentialsDto auth, String searchedUsername) {
        Authenticator.authenticateUser(auth.getUsername(), auth.getPassword(), traineeRepository::findByUsername);

        var trainee = traineeRepository.findByUsername(searchedUsername)
                .orElseThrow(() -> new NoSuchElementException("No trainee with given username : " + searchedUsername + " exists."));

        TraineeWithListDto res = modelMapper.map(trainee, TraineeWithListDto.class);

        return res;

    }

    public List<TrainerShortDto> getNotMineTrainersByUsername(UserCredentialsDto auth) {
        Authenticator.authenticateUser(auth.getUsername(), auth.getPassword(), traineeRepository::findByUsername);
        List<Trainer> notMine=traineeRepository.getNotMineTrainersByUsername(auth.getUsername());

        return notMine.stream().map(x->modelMapper.map(x, TrainerShortDto.class))
                .collect(Collectors.toList());
    }

    @Transactional
    public List<TrainerShortDto> updateTrainersList(UserCredentialsDto auth, TrainerListPutDto trainerListPutDto) {
        Trainee updating = Authenticator.authenticateUser(auth.getUsername(), auth.getPassword(), traineeRepository::findByUsername);

        List<Trainer> updatedTrainers = trainerListPutDto.getTrainerUsernameList().stream()
                .map(x -> trainerRepository.findByUsername(x)
                        .orElseThrow(() -> new IllegalArgumentException("Trainer with username: " + x + " does not exist")))
                .collect(Collectors.toList());

        updating.setTrainers(updatedTrainers);

        traineeRepository.save(updating);
        return updatedTrainers.stream().map(x->modelMapper.map(x, TrainerShortDto.class))
                .collect(Collectors.toList());
    }

    @Transactional
    public void switchActivate(UserCredentialsDto auth) {
        var trainee=Authenticator.authenticateUser(auth.getUsername(), auth.getPassword(), traineeRepository::findByUsername);
        trainee.setActive(!trainee.isActive());
        traineeRepository.save(trainee);
    }

    @Transactional
    public void deleteTraineeByUsername(UserCredentialsDto auth) {
        Trainee trainee = Authenticator.authenticateUser(auth.getUsername(), auth.getPassword(), traineeRepository::findByUsername);
        traineeRepository.delete(trainee);
    }
}