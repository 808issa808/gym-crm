package org.epam.config;

import jakarta.annotation.PostConstruct;
import org.epam.model.*;
import org.epam.service.TraineeService;
import org.epam.service.TrainerService;
import org.epam.service.TrainingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

@Component
public class DataLoader {
    private static final Logger logger = LoggerFactory.getLogger(DataLoader.class);

    private TraineeService traineeService;
    private TrainerService trainerService;
    private TrainingService trainingService;

    @Autowired
    public void setTraineeService(TraineeService traineeService) {
        this.traineeService = traineeService;
    }

    @Autowired
    public void setTrainerService(TrainerService trainerService) {
        this.trainerService = trainerService;
    }

    @Autowired
    public void setTrainingService(TrainingService trainingService) {
        this.trainingService = trainingService;
    }

    @PostConstruct
    public void initialize() {
        Properties prop = new Properties();
        try (InputStream input = Files.newInputStream(Paths.get("src/main/resources/application.properties"))) {
            prop.load(input);
            String storageFile = prop.getProperty("storage.file.path");

            try (BufferedReader reader = new BufferedReader(new FileReader(storageFile))) {
                logger.info("{} file has been accessed", storageFile);
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] fields = line.split(",");
                    switch (fields[0]) {
                        case "trainee" -> {
                            Trainee trainee = new Trainee();
                            trainee.setFirstName(fields[1]);
                            trainee.setLastName(fields[2]);
                            traineeService.save(trainee);
                        }
                        case "trainer" -> {
                            Trainer trainer = new Trainer();
                            trainer.setFirstName(fields[1]);
                            trainer.setLastName(fields[2]);
                            trainerService.save(trainer);
                        }
                        case "training" -> {
                            Training training = new Training();
                            training.setName(fields[1]);
                            training.setType(new TrainingType(fields[2]));
                            trainingService.save(training);
                        }
                        default -> logger.warn("Unknown entry type: {}", fields[0]);
                    }
                }
            }
        } catch (IOException e) {
            logger.error("IOException occurred while working with file: initialData.txt", e);
            throw new RuntimeException("Error reading initialData.txt", e);
        }
    }
}