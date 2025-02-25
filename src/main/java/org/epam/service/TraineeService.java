package org.epam.service;

import org.epam.data.TraineeDao;
import org.epam.model.Trainee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class TraineeService {
    @Autowired
    private TraineeDao traineeDao;

    public void save(Trainee trainee) {
        traineeDao.save(trainee);
    }

    public Optional<Trainee> findById(Long id) {
        return traineeDao.findById(id);
    }

    public Collection<Trainee> findAll() {
        return traineeDao.findAll();
    }

    public void deleteById(Long id) {
        traineeDao.deleteById(id);
    }
}
