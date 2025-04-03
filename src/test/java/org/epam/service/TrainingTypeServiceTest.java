package org.epam.service;

import org.epam.data.impl.TrainingTypeRepository;
import org.epam.data.impl.UserRepositoryImpl;
import org.epam.model.TrainingType;
import org.epam.model.User;
import org.epam.web.dto.users.UserCredentialsDto;
import org.epam.web.exp.AuthException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainingTypeServiceTest {

    @Mock
    private TrainingTypeRepository trainingTypeRepository;

    @Mock
    private UserRepositoryImpl userRepository;

    @InjectMocks
    private TrainingTypeService trainingTypeService;

    @Test
    void getAll_ShouldReturnTrainingTypes_WhenAuthenticationSuccessful() {
        // Arrange
        UserCredentialsDto credentials = new UserCredentialsDto("valid.user", "validPass");
        List<TrainingType> expectedTypes = List.of(
                new TrainingType(1L, "Yoga"),
                new TrainingType(2L, "Strength")
        );

        when(userRepository.findByUsernameUser("valid.user"))
                .thenReturn(Optional.of(new User("valid.user", "validPass")));
        when(trainingTypeRepository.findAll()).thenReturn(expectedTypes);

        // Act
        List<TrainingType> result = trainingTypeService.getAll(credentials);

        // Assert
        assertEquals(expectedTypes, result);
        verify(trainingTypeRepository).findAll();
    }

    @Test
    void getAll_ShouldThrowException_WhenAuthenticationFails() {
        // Arrange
        UserCredentialsDto credentials = new UserCredentialsDto("invalid.user", "wrongPass");

        when(userRepository.findByUsernameUser("invalid.user"))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(AuthException.class, () -> trainingTypeService.getAll(credentials));
        verifyNoInteractions(trainingTypeRepository);
    }

    @Test
    void getAll_ShouldReturnEmptyList_WhenNoTrainingTypesExist() {
        // Arrange
        UserCredentialsDto credentials = new UserCredentialsDto("valid.user", "validPass");

        when(userRepository.findByUsernameUser("valid.user"))
                .thenReturn(Optional.of(new User("valid.user", "validPass")));
        when(trainingTypeRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<TrainingType> result = trainingTypeService.getAll(credentials);

        // Assert
        assertTrue(result.isEmpty());
        verify(trainingTypeRepository).findAll();
    }
}