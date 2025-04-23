package org.epam.service;

import lombok.RequiredArgsConstructor;
import org.epam.data.impl.TrainerRepositoryImpl;
import org.epam.data.impl.TrainingTypeRepository;
import org.epam.model.Trainer;
import org.epam.model.TrainingType;
import org.epam.security.jwt.JwtService;
import org.epam.util.UserUtil;
import org.epam.web.dto.users.UserCredentialsDto;
import org.epam.web.dto.users.ChangeLoginRequest;
import org.epam.web.dto.users.trainer.TrainerDto;
import org.epam.web.dto.users.trainer.TrainerRegistrationRequest;
import org.epam.web.dto.users.trainer.TrainerWithListDto;
import org.epam.web.exp.ForbiddenException;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.NoSuchElementException;


@Service
@RequiredArgsConstructor
public class TrainerService {

    private final TrainerRepositoryImpl trainerRepository;
    private final TrainingTypeRepository trainingTypeRepository;
    private final ModelMapper modelMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final PasswordEncoder encoder;

    @Transactional
    public String create(TrainerRegistrationRequest registrationDto) {
        TrainingType specialization = trainingTypeRepository.findById(registrationDto.getSpecialization())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Specialization not found"));

        Trainer trainer = modelMapper.map(registrationDto, Trainer.class);

        String username = UserUtil.generateUsername(trainer.getFirstName(), trainer.getLastName(), trainerRepository::countByUsernamePrefix);
        String password = UserUtil.generatePassword();
        trainer.setUsername(username);
        trainer.setPassword(encoder.encode(password));
        trainer.setSpecialization(specialization);

        trainerRepository.save(trainer);

        UserDetails authed = (UserDetails) authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)).getPrincipal();

        return jwtService.generateToken(authed);
    }

    public TrainerWithListDto findByUsername(UserCredentialsDto auth, String searchedUsername) {
        var trainer = trainerRepository.findByUsername(searchedUsername)
                .orElseThrow(() -> new NoSuchElementException("There is no trainer with username: " + searchedUsername));

        return modelMapper.map(trainer, TrainerWithListDto.class);
    }

    @Transactional
    public Trainer changePassword(ChangeLoginRequest changeDto) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        Trainer trainer = trainerRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new NoSuchElementException("Trainer with username: " + currentUsername + " not found"));
        trainer.setPassword(encoder.encode(changeDto.getNewPassword()));

        return trainerRepository.save(trainer);
    }

    @Transactional
    public TrainerWithListDto update(UserCredentialsDto auth, TrainerDto trainerDto) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        if (!currentUsername.equals(trainerDto.getUsername())) {
            throw new ForbiddenException("Нельзя изменить данные другого пользователя");
        }

        TrainingType specialization = trainingTypeRepository.findById(trainerDto.getSpecialization())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Specialization not found"));

        Trainer trainer = trainerRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new NoSuchElementException("There is no trainer with username: " + currentUsername));

        modelMapper.map(trainerDto, trainer);
        trainer.setSpecialization(specialization);
        trainerRepository.save(trainer);

        return modelMapper.map(trainer, TrainerWithListDto.class);
    }

    @Transactional
    public void switchActivate(UserCredentialsDto auth) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        Trainer trainer = trainerRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new NoSuchElementException("There is no trainer with username: " + currentUsername));

        trainer.setActive(!trainer.isActive());
        trainerRepository.save(trainer);
    }

    public String login(UserCredentialsDto loginDto) {
        UserDetails authed = (UserDetails) authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword())).getPrincipal();
        return jwtService.generateToken(authed);
    }
}