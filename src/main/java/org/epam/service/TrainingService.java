package org.epam.service;

import org.epam.data.TrainingDao;
import org.epam.model.Training;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class TrainingService {
    @Autowired
    private TrainingDao trainingDao;

    public void save(Training training) {
        trainingDao.save(training);
    }

    public Optional<Training> findByTraineeId(Long traineeId) {
        return trainingDao.findByTraineeId(traineeId);
    }

    public Collection<Training> findAll() {
        return trainingDao.findAll();
    }
}
