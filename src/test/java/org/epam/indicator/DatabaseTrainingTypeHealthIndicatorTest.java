package org.epam.indicator;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;

import jakarta.persistence.PersistenceException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DatabaseTrainingTypeHealthIndicatorTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private Query query;

    @InjectMocks
    private DatabaseTrainingTypeHealthIndicator healthIndicator;

    @Test
    void health_shouldReturnUpWhenDatabaseIsAccessible() {
        // Arrange
        when(entityManager.createQuery(anyString())).thenReturn(query);
        when(query.getResultList()).thenReturn(mock(java.util.List.class));

        // Act
        Health health = healthIndicator.health();

        // Assert
        assertEquals(Status.UP, health.getStatus());
        assertTrue(health.getDetails().containsKey("Database table TrainingType"));
        assertEquals("Up and running", health.getDetails().get("Database table TrainingType"));
    }

    @Test
    void health_shouldReturnDownWhenDatabaseThrowsException() {
        // Arrange
        when(entityManager.createQuery(anyString())).thenReturn(query);
        when(query.getResultList()).thenThrow(new PersistenceException("Connection failed"));

        // Act
        Health health = healthIndicator.health();

        // Assert
        assertEquals(Status.DOWN, health.getStatus());
        assertTrue(health.getDetails().containsKey("Database table TrainingType"));
        assertEquals("Down", health.getDetails().get("Database table TrainingType"));
        assertNotNull(health.getDetails().get("error"));
    }

    @Test
    void health_shouldReturnDownWhenQueryIsInvalid() {
        // Arrange
        when(entityManager.createQuery(anyString())).thenThrow(new IllegalArgumentException("Invalid query"));

        // Act
        Health health = healthIndicator.health();

        // Assert
        assertEquals(Status.DOWN, health.getStatus());
        assertTrue(health.getDetails().containsKey("Database table TrainingType"));
        assertEquals("Down", health.getDetails().get("Database table TrainingType"));
        assertNotNull(health.getDetails().get("error"));
    }
}