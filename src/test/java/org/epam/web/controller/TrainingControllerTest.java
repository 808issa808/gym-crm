package org.epam.web.controller;

import org.epam.service.TrainingService;
import org.epam.web.dto.training.TrainingCreateDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TrainingControllerTest {

    @Mock
    private TrainingService trainingService;

    @InjectMocks
    private TrainingController trainingController;

    @Test
    void add_ShouldCallServiceAndReturnCreatedStatus() {
        // Arrange
        TrainingCreateDto trainingCreateDto = new TrainingCreateDto();

        // Act
        trainingController.add(trainingCreateDto);

        // Assert
        verify(trainingService).create(trainingCreateDto);
        assertCreateMethodHasCorrectResponseStatus();
    }

    private void assertCreateMethodHasCorrectResponseStatus() {
        try {
            ResponseStatus annotation = TrainingController.class
                    .getMethod("add", TrainingCreateDto.class)
                    .getAnnotation(ResponseStatus.class);
            assertEquals(HttpStatus.CREATED, annotation.value());
        } catch (NoSuchMethodException e) {
            fail("Method 'add' not found in TrainingController");
        }
    }
}