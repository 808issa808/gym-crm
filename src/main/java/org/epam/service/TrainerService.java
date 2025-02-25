package org.epam.service;

import org.epam.data.TrainerDao;
import org.epam.model.Trainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class TrainerService {
    @Autowired
    private TrainerDao trainerDao;

    public void save(Trainer trainer) {
        trainerDao.save(trainer);
    }

    public Optional<Trainer> findById(Long id) {
        return trainerDao.findById(id);
    }

    public Collection<Trainer> findAll() {
        return trainerDao.findAll();
    }


}
