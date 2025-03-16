package org.epam;

import org.epam.config.HibernateConfig;
import org.epam.model.Trainee;
import org.epam.model.Trainer;
import org.epam.model.Training;
import org.epam.model.TrainingType;
import org.epam.service.TraineeService;
import org.epam.service.TrainerService;
import org.epam.service.TrainingService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Collections;
import java.util.Date;
import java.util.List;

public class App {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(HibernateConfig.class);
        TraineeService traineeService = context.getBean(TraineeService.class);
        TrainerService trainerService = context.getBean(TrainerService.class);
        TrainingService trainingService = context.getBean(TrainingService.class);

        Trainee trainee = new Trainee();
        trainee.setFirstName("ivan");
        trainee.setLastName("ivanov");
        traineeService.create(trainee);

        // Обновление Trainee
        trainee.setAddress("ул. Ленина, д. 10");
        traineeService.update(trainee);

        // Поиск Trainee по логину
        Trainee foundTrainee = traineeService.findByUsername("sidorova", "password3", "kozlova");
        System.out.println("Найденный trainee: " + foundTrainee.getFirstName());

        // Получение списка тренеров, не принадлежащих Trainee
        List<Trainer> trainers = traineeService.getNotMineTrainersByUsername("sidorova", "password3");
        System.out.println("Количество тренеров: " + trainers.size());

        // Обновление списка тренеров
        traineeService.updateTrainersList("sidorova", "password3", Collections.emptyList());

        trainers = traineeService.getNotMineTrainersByUsername("sidorova", "password3");
        System.out.println("Количество тренеров: " + trainers.size());

        // Смена пароля
        traineeService.changePassword(foundTrainee, "newSecurePassword456");

        // Переключение статуса активности
        traineeService.switchActivate(foundTrainee);

        // Удаление Trainee
        traineeService.deleteTraineeByUsername("deleting", "password1");

        // Создание тренера
        Trainer trainer = new Trainer();
        trainer.setFirstName("ivan");
        trainer.setLastName("ivanych");
        TrainingType box = new TrainingType();
        box.setTrainingTypeName("Бокс");
        box.setId(3L);
        trainer.setSpecialization(box);
        trainer = trainerService.create(trainer);
        System.out.println("Метод create(Trainer) выполнен");

        // Обновление Trainer
        trainer.setFirstName("vladislav");
        trainerService.update(trainer);

        //switch

        trainerService.switchActivate(trainer);

        trainerService.changePassword(trainer, "newChangedPassword");


        // Создание тренировки
        Training training = new Training();
        training.setName("Кардио тренировка");
        training.setType(box);
        training.setTrainer(trainer);
        training.setTrainee(trainee);
        training.setDate(new Date());
        training.setDuration(60);
        trainingService.create(training);
        System.out.println("Метод create(Training) выполнен");

        // Получение всех тренировок ученика
        List<Training> traineeTrainings = trainingService.findByTraineeUsername("sidorova", "password3");
        System.out.println("Метод findByTraineeUsername выполнен, найдено тренировок: " + traineeTrainings.size());

        // Получение всех тренировок тренера
        List<Training> trainerTrainings = trainingService.findByTrainerUsername(trainer.getUsername(), trainer.getPassword());
        System.out.println("Метод findByTrainerUsername выполнен, найдено тренировок: " + trainerTrainings.size());

        // Получение тренировок ученика по фильтрам
        Date fromDate = new Date(0);
        Date toDate = new Date(); // Сегодня
        List<Training> traineeTrainings2 = trainingService.findTrainingsForTrainee("kozlova", "newSecurePassword456", "kozlova", fromDate, toDate, "Петр", "Кроссфит");
        System.out.println("Метод findTrainingsForTrainee выполнен, найдено тренировок: " + traineeTrainings2.size());

        // Получение тренировок тренера по фильтрам
        List<Training> trainerTrainings2 = trainingService.findTrainingsForTrainer(trainer.getUsername(), trainer.getPassword(), trainer.getUsername(), fromDate, toDate, "ivan");
        System.out.println("Метод findTrainingsForTrainer выполнен, найдено тренировок: " + trainerTrainings2.size());
        System.out.println("Все методы успешно выполнены!");
    }
}
