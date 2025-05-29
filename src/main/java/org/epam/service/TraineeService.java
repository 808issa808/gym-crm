package org.epam.service;

import lombok.RequiredArgsConstructor;
import org.epam.data.impl.TraineeRepositoryImpl;
import org.epam.data.impl.TrainerRepositoryImpl;
import org.epam.model.Trainee;
import org.epam.model.Trainer;
import org.epam.security.jwt.JwtService;
import org.epam.util.UserUtil;
import org.epam.web.dto.users.ChangeLoginRequest;
import org.epam.web.dto.users.UserCredentialsDto;
import org.epam.web.dto.users.trainee.TraineeDto;
import org.epam.web.dto.users.trainee.TraineeRegistrationRequest;
import org.epam.web.dto.users.trainee.TraineeWithListDto;
import org.epam.web.dto.users.trainee.TrainerListPutDto;
import org.epam.web.dto.users.trainer.TrainerShortDto;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TraineeService {
    private final TraineeRepositoryImpl traineeRepository;
    private final TrainerRepositoryImpl trainerRepository;
    private final ModelMapper modelMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final PasswordEncoder encoder;

    @Transactional
    public String create(TraineeRegistrationRequest registrationDto) {
        Trainee trainee = modelMapper.map(registrationDto, Trainee.class);

        String username = UserUtil.generateUsername(
                registrationDto.getFirstName(),
                registrationDto.getLastName(),
                traineeRepository::countByUsernamePrefix
        );
        String password = UserUtil.generatePassword();

        trainee.setUsername(username);
        trainee.setPassword(encoder.encode(password));
        traineeRepository.save(trainee);

        UserDetails authed = (UserDetails) authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        ).getPrincipal();

        return jwtService.generateToken(authed);
    }

    public String login(UserCredentialsDto loginDto) {
        UserDetails authed = (UserDetails) authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword())
        ).getPrincipal();

        return jwtService.generateToken(authed);
    }

    @Transactional
    public Trainee changePassword(ChangeLoginRequest changeDto) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        Trainee trainee = traineeRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new NoSuchElementException("Trainee with username: " + currentUsername + " not found"));

        trainee.setPassword(encoder.encode(changeDto.getNewPassword()));
        return traineeRepository.save(trainee);
    }

    @Transactional
    public TraineeWithListDto update(TraineeDto traineeDto) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        Trainee trainee = traineeRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new NoSuchElementException("Trainee with username: " + currentUsername + " not found"));

        modelMapper.map(traineeDto, trainee);
        traineeRepository.save(trainee);

        return modelMapper.map(trainee, TraineeWithListDto.class);
    }

    public TraineeWithListDto findByUsername(String searchedUsername) {
        Trainee trainee = traineeRepository.findByUsername(searchedUsername)
                .orElseThrow(() -> new NoSuchElementException("No trainee with username: " + searchedUsername + " exists."));

        return modelMapper.map(trainee, TraineeWithListDto.class);
    }

    public List<TrainerShortDto> getNotMineTrainersByUsername(UserCredentialsDto auth) {
        List<Trainer> notMine = traineeRepository.getNotMineTrainersByUsername(auth.getUsername());

        return notMine.stream()
                .map(x -> modelMapper.map(x, TrainerShortDto.class))
                .collect(Collectors.toList());
    }

    @Transactional
    public List<TrainerShortDto> updateTrainersList(TrainerListPutDto trainerListPutDto) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        Trainee updating = traineeRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new NoSuchElementException("Trainee with username: " + currentUsername + " not found"));

        List<Trainer> updatedTrainers = trainerListPutDto.getTrainerUsernameList().stream()
                .map(username -> trainerRepository.findByUsername(username)
                        .orElseThrow(() -> new IllegalArgumentException("Trainer with username: " + username + " does not exist")))
                .collect(Collectors.toList());

        updating.setTrainers(updatedTrainers);
        traineeRepository.save(updating);

        return updatedTrainers.stream()
                .map(x -> modelMapper.map(x, TrainerShortDto.class))
                .collect(Collectors.toList());
    }

    @Transactional
    public void switchActivate() {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        Trainee trainee = traineeRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new NoSuchElementException("Trainee with username: " + currentUsername + " not found"));

        trainee.setActive(!trainee.isActive());
        traineeRepository.save(trainee);
    }

    @Transactional
    public void deleteTraineeByUsername() {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        Trainee trainee = traineeRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new NoSuchElementException("Trainee with username: " + currentUsername + " not found"));

        traineeRepository.delete(trainee);
    }
}