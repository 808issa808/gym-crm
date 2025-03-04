package org.epam.data;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.epam.model.*;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Slf4j
@Component
@Getter
public class Storage {
    private final Map<Long, Trainee> trainees = new HashMap<>();
    private final Map<Long, Trainer> trainers = new HashMap<>();
    private final Map<Long, Training> trainings = new HashMap<>();
    private final Map<String, Long> traineeUsernameToId = new HashMap<>();
    private final Map<String, Long> trainerUsernameToId = new HashMap<>();

    public long nextTraineeId() {
        return trainees.keySet().stream().max(Long::compare).orElse(0L) + 1;
    }

    public long nextTrainerId() {
        return trainers.keySet().stream().max(Long::compare).orElse(0L) + 1;
    }

    public long nextTrainingId() {
        return trainings.keySet().stream().max(Long::compare).orElse(0L) + 1;
    }

    @PostConstruct
    public void initialize() {
        Properties prop = new Properties();
        try (InputStream input = Files.newInputStream(Paths.get("src/main/resources/application.properties"))) {
            prop.load(input);
            String storageFile = prop.getProperty("storage.file.path");

            try (BufferedReader reader = new BufferedReader(new FileReader(storageFile))) {
                log.info("{} file has been accessed", storageFile);
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] fields = line.split(",");
                    switch (fields[0]) {
                        case "trainee" -> {
                            long id = nextTraineeId();
                            Trainee trainee = new Trainee(id, fields[1], fields[2], fields[3], fields[4], true, null, "");
                            trainees.put(id, trainee);
                            traineeUsernameToId.put(fields[3], id);
                            log.info("Trainee added: {}", trainee);
                        }
                        case "trainer" -> {
                            long id = nextTrainerId();
                            Trainer trainer = new Trainer(id, fields[1], fields[2], fields[3], fields[4], true, new TrainingType(fields[5]));
                            trainers.put(id, trainer);
                            trainerUsernameToId.put(fields[3], id);
                            log.info("Trainer added: {}", trainer);
                        }
                        case "training" -> {
                            long id = nextTrainingId();
                            Training training = new Training(fields[1], new TrainingType(fields[2]), null, null, null, null);
                            trainings.put(id, training);
                            log.info("Training added: {}", training);
                        }
                        default -> log.warn("Unknown entry type: {}", fields[0]);
                    }
                }
            }
        } catch (IOException e) {
            log.error("IOException occurred while working with file", e);
            throw new RuntimeException("Error reading data file", e);
        }
    }
}
