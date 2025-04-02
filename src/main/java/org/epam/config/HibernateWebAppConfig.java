package org.epam.config;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.epam.model.Trainee;
import org.epam.model.Trainer;
import org.epam.model.Training;
import org.epam.web.dto.training.GetTraineeTrainingsResponse;
import org.epam.web.dto.training.GetTrainerTrainingsResponse;
import org.epam.web.dto.users.UserShortDto;
import org.epam.web.dto.users.trainee.TraineeWithListDto;
import org.epam.web.dto.users.trainer.TrainerShortDto;
import org.epam.web.dto.users.trainer.TrainerWithListDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Configuration
@ComponentScan("org.epam")
public class HibernateWebAppConfig {

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new JpaTransactionManager();
    }

    @Bean
    public Validator validator() {
        return Validation.buildDefaultValidatorFactory().getValidator();
    }

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

        return modelMapper;
    }
}
