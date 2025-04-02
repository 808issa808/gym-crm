package org.epam.web.controller;

import org.epam.model.TrainingType;
import org.epam.service.TrainingTypeService;
import org.epam.web.dto.users.UserCredentialsDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainingTypeControllerTest {

    @Mock
    private TrainingTypeService trainingTypeService;

    @InjectMocks
    private TrainingTypeController trainingTypeController;

    @Test
    void getAll_ShouldReturnListOfTrainingTypes() {
        // Arrange
        UserCredentialsDto auth = new UserCredentialsDto("admin", "password");
        List<TrainingType> expectedTypes = Arrays.asList(
                new TrainingType(1L, "Yoga"),
                new TrainingType(2L, "Strength Training")
        );

        when(trainingTypeService.getAll(auth)).thenReturn(expectedTypes);

        // Act
        List<TrainingType> result = trainingTypeController.getAll(auth);

        // Assert
        assertEquals(expectedTypes, result);
        verify(trainingTypeService).getAll(auth);
    }

    @Test
    void getAll_ShouldReturnEmptyListWhenNoTypesExist() {
        // Arrange
        UserCredentialsDto auth = new UserCredentialsDto("admin", "password");
        List<TrainingType> expectedTypes = List.of();

        when(trainingTypeService.getAll(auth)).thenReturn(expectedTypes);

        // Act
        List<TrainingType> result = trainingTypeController.getAll(auth);

        // Assert
        assertEquals(expectedTypes, result);
        verify(trainingTypeService).getAll(auth);
    }
}