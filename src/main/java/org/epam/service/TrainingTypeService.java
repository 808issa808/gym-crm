package org.epam.service;

import lombok.RequiredArgsConstructor;
import org.epam.data.impl.TrainingTypeRepository;
import org.epam.data.impl.UserRepositoryImpl;
import org.epam.model.TrainingType;
import org.epam.util.Authenticator;
import org.epam.web.dto.users.UserCredentialsDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TrainingTypeService {

    private final TrainingTypeRepository trainingTypeRepository;
    private final UserRepositoryImpl userRepository;

    public List<TrainingType> getAll(UserCredentialsDto auth) {
        Authenticator.authenticateUser(auth.getUsername(), auth.getPassword(), userRepository::findByUsernameUser);
        return trainingTypeRepository.findAll();
    }
}
