package org.epam.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.epam.service.TrainingService;
import org.epam.web.controller.swaggerInterface.TrainingApi;
import org.epam.web.dto.training.TrainingCreateDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/trainings")
@RequiredArgsConstructor
public class TrainingController implements TrainingApi {
    private final TrainingService trainingService;

    @Override
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void add(@Valid @RequestBody TrainingCreateDto trainingCreateDto) {
        trainingService.create(trainingCreateDto);
    }
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        trainingService.deleteTrainingById(id);
    }
}