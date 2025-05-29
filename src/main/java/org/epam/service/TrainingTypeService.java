package org.epam.service;

import lombok.RequiredArgsConstructor;
import org.epam.data.impl.TrainingTypeRepository;
import org.epam.model.TrainingType;
import org.epam.web.dto.users.UserCredentialsDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TrainingTypeService {

    private final TrainingTypeRepository trainingTypeRepository;
    public List<TrainingType> getAll() {
        return trainingTypeRepository.findAll();
    }
}
