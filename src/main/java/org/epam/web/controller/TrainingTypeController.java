package org.epam.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.epam.model.TrainingType;
import org.epam.service.TrainingTypeService;
import org.epam.web.controller.swaggerInterface.TrainingTypeApi;
import org.epam.web.dto.users.UserCredentialsDto;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/training-types")
@RequiredArgsConstructor
public class TrainingTypeController implements TrainingTypeApi {

    private final TrainingTypeService trainingTypeService;

    @Override
    @GetMapping
    public List<TrainingType> getAll(@Valid @RequestBody UserCredentialsDto auth) {
        return trainingTypeService.getAll();
    }
}