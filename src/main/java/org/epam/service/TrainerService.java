package org.epam.service;

import org.epam.data.TrainerDao;
import org.epam.model.Trainer;
import org.epam.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class TrainerService {
    private TrainerDao trainerDao;

    public void save(Trainer trainer) {

        String username = UserUtil.generateUsername(trainer.getFirstName(), trainer.getLastName(), trainerDao::existsByUsername);
        String password = UserUtil.generatePassword();
        trainer.setUsername(username);
        trainer.setPassword(password);
        trainerDao.save(trainer);
    }

    public Optional<Trainer> findById(Long id) {
        return trainerDao.findById(id);
    }

    public Collection<Trainer> findAll() {
        return trainerDao.findAll();
    }

    @Autowired
    public void setTrainerDao(TrainerDao trainerDao) {
        this.trainerDao = trainerDao;
    }
}
