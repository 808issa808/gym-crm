package org.epam.service;

import lombok.RequiredArgsConstructor;
import org.epam.data.impl.TrainerRepositoryImpl;
import org.epam.model.Trainer;
import org.epam.util.Authenticator;
import org.epam.util.UserUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class TrainerService {

    private final TrainerRepositoryImpl trainerRepository;

    @Transactional
    public Trainer create(Trainer trainer) {

        String username = UserUtil.generateUsername(trainer.getFirstName(), trainer.getLastName(), trainerRepository::countByUsernamePrefix);
        String password = UserUtil.generatePassword();
        trainer.setUsername(username);
        trainer.setPassword(password);
        trainer.setActive(true);
        return trainerRepository.save(trainer);
    }

    public Trainer findByUsername(String username, String password, String searchedUsername) {
        Authenticator.authenticateUser(username, password, trainerRepository::findByUsername);
        return trainerRepository.findByUsername(searchedUsername)
                .orElseThrow(() -> new IllegalArgumentException("There is no trainer with username: " + searchedUsername));
    }


    @Transactional
    public Trainer changePassword(Trainer trainer, String password) {
        Authenticator.authenticateUser(trainer.getUsername(), trainer.getPassword(), trainerRepository::findByUsername);
        if (password.trim().length()>=10) {
            trainer.setPassword(password);
            return trainerRepository.save(trainer);
        } else {
            throw new IllegalArgumentException("New Password should be at least 10 chars long");
        }
    }

    @Transactional
    public Trainer update(Trainer trainer) {
        Authenticator.authenticateUser(trainer.getUsername(), trainer.getPassword(), trainerRepository::findByUsername);
        return trainerRepository.save(trainer);
    }

    @Transactional
    public void switchActivate(Trainer trainer) {
        Authenticator.authenticateUser(trainer.getUsername(), trainer.getPassword(), trainerRepository::findByUsername);
        trainer.setActive(!trainer.isActive());
        trainerRepository.save(trainer);
    }
}
