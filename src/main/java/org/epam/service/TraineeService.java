package org.epam.service;

import org.epam.data.TraineeDao;
import org.epam.model.Trainee;
import org.epam.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class TraineeService {
    private TraineeDao traineeDao;
    @Autowired
    public void setTraineeDao(TraineeDao traineeDao) {
        this.traineeDao = traineeDao;
    }

    public void save(Trainee trainee) {
        String username = UserUtil.generateUsername(trainee.getFirstName(), trainee.getLastName(), traineeDao::existsByUsername);
        String password = UserUtil.generatePassword();
        trainee.setUsername(username);
        trainee.setPassword(password);
        traineeDao.save(trainee);
    }

    public Trainee update(Trainee trainee) {
        return traineeDao.update(trainee);
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
