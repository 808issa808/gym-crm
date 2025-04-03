package org.epam.data.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.epam.model.TrainingType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainingTypeRepositoryTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private TypedQuery<TrainingType> typedQuery;

    @InjectMocks
    private TrainingTypeRepository trainingTypeRepository;

    @Test
    void findById_ShouldReturnTrainingType_WhenExists() {
        // Arrange
        TrainingType expected = new TrainingType();
        expected.setId(1L);
        when(entityManager.find(TrainingType.class, 1L)).thenReturn(expected);

        // Act
        Optional<TrainingType> result = trainingTypeRepository.findById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        verify(entityManager).find(TrainingType.class, 1L);
    }

    @Test
    void findById_ShouldReturnEmpty_WhenNotExists() {
        // Arrange
        when(entityManager.find(TrainingType.class, 99L)).thenReturn(null);

        // Act
        Optional<TrainingType> result = trainingTypeRepository.findById(99L);

        // Assert
        assertFalse(result.isPresent());
        verify(entityManager).find(TrainingType.class, 99L);
    }

    @Test
    void findAll_ShouldReturnAllTrainingTypes() {
        // Arrange
        TrainingType type1 = new TrainingType();
        type1.setId(1L);
        TrainingType type2 = new TrainingType();
        type2.setId(2L);

        when(entityManager.createQuery("SELECT t FROM TrainingType t", TrainingType.class))
                .thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(List.of(type1, type2));

        // Act
        List<TrainingType> result = trainingTypeRepository.findAll();

        // Assert
        assertEquals(2, result.size());
        verify(entityManager).createQuery("SELECT t FROM TrainingType t", TrainingType.class);
    }

    @Test
    void findAll_ShouldReturnEmptyList_WhenNoTypesExist() {
        // Arrange
        when(entityManager.createQuery("SELECT t FROM TrainingType t", TrainingType.class))
                .thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(List.of());

        // Act
        List<TrainingType> result = trainingTypeRepository.findAll();

        // Assert
        assertTrue(result.isEmpty());
        verify(entityManager).createQuery("SELECT t FROM TrainingType t", TrainingType.class);
    }
}