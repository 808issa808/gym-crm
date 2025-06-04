package org.epam.config;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.epam.model.Trainee;
import org.epam.model.Trainer;
import org.epam.model.Training;
import org.epam.service.workload.dto.UpdateReport;
import org.epam.web.dto.training.GetTraineeTrainingsResponse;
import org.epam.web.dto.training.GetTrainerTrainingsResponse;
import org.epam.web.dto.users.UserShortDto;
import org.epam.web.dto.users.trainee.TraineeWithListDto;
import org.epam.web.dto.users.trainer.TrainerShortDto;
import org.epam.web.dto.users.trainer.TrainerWithListDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.ZoneId;
import java.util.Date;

@Configuration
public class HibernateWebAppConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setSkipNullEnabled(true);
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);


        modelMapper.typeMap(Trainee.class, TraineeWithListDto.class).addMappings(mapper ->
                mapper.map(src -> src.getTrainers(), TraineeWithListDto::setTrainerShortDtoList)
        );
        modelMapper.typeMap(Trainer.class, TrainerWithListDto.class).addMappings(mapper -> {
            mapper.map(src -> src.getTrainees(), TrainerWithListDto::setTraineeShortDtoList);
            mapper.map(src -> src.getSpecialization().getId(), TrainerWithListDto::setSpecialization);
        });

        modelMapper.typeMap(Trainer.class, TrainerShortDto.class).addMappings(mapper -> {
            mapper.map(Trainer::getUsername, TrainerShortDto::setUsername);
            mapper.map(Trainer::getFirstName, TrainerShortDto::setFirstName);
            mapper.map(Trainer::getLastName, TrainerShortDto::setLastName);
            mapper.map(trainer -> trainer.getSpecialization().getId(), TrainerShortDto::setSpecialization);
        });

        modelMapper.typeMap(Trainee.class, UserShortDto.class).addMappings(mapper -> {
            mapper.map(Trainee::getUsername, UserShortDto::setUsername);
            mapper.map(Trainee::getFirstName, UserShortDto::setFirstName);
            mapper.map(Trainee::getLastName, UserShortDto::setLastName);
        });

        modelMapper.typeMap(Training.class, GetTraineeTrainingsResponse.class).addMappings(mapper -> {
            mapper.map(src -> src.getTrainer().getFirstName(), GetTraineeTrainingsResponse::setTrainerName);
            mapper.map(src -> src.getType().getTrainingTypeName(), GetTraineeTrainingsResponse::setType);
        });

        modelMapper.typeMap(Training.class, GetTrainerTrainingsResponse.class).addMappings(mapper -> {
            mapper.map(src -> src.getTrainee().getFirstName(), GetTrainerTrainingsResponse::setTraineeName);
            mapper.map(src -> src.getType().getTrainingTypeName(), GetTrainerTrainingsResponse::setType);
        });

        modelMapper.typeMap(Training.class, UpdateReport.class).addMappings(mapper -> {
            mapper.map(src -> src.getTrainer().getUsername(), UpdateReport::setUsername);
            mapper.map(src -> src.getTrainer().getFirstName(), UpdateReport::setFirstname);
            mapper.map(src -> src.getTrainer().getLastName(), UpdateReport::setLastname);
            mapper.map(src -> src.getTrainer().isActive(), UpdateReport::setActive);
            mapper.using(ctx -> {
                Date date = (Date) ctx.getSource();
                return date.toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate();
            }).map(Training::getDate, UpdateReport::setTrainingDate);
            mapper.map(src -> src.getDuration(), UpdateReport::setDuration);
        });

        return modelMapper;
    }

    @Bean
    public Counter getCounter(MeterRegistry registry) {
        return Counter
                .builder("trainees_created_total")
                .description("Number of trainees created")
                .register(registry);
    }

    @Bean
    public Timer getTimer(MeterRegistry registry) {
        return Timer.builder("trainee_registration_time")
                .register(registry);
    }
}
