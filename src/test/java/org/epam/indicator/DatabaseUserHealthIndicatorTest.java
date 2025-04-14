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
class DatabaseUserHealthIndicatorTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private Query query;

    @InjectMocks
    private DatabaseUserHealthIndicator healthIndicator;

    @Test
    void health_shouldReturnUpWhenUserTableIsAccessible() {
        // Arrange
        when(entityManager.createQuery(anyString())).thenReturn(query);
        when(query.getResultList()).thenReturn(mock(java.util.List.class));

        // Act
        Health health = healthIndicator.health();

        // Assert
        assertEquals(Status.UP, health.getStatus());
        assertTrue(health.getDetails().containsKey("Database table User"));
        assertEquals("Up and running", health.getDetails().get("Database table User"));
        assertNull(health.getDetails().get("error"));
    }

    @Test
    void health_shouldReturnDownWhenDatabaseConnectionFails() {
        // Arrange
        when(entityManager.createQuery(anyString())).thenReturn(query);
        when(query.getResultList()).thenThrow(new PersistenceException("Connection timeout"));

        // Act
        Health health = healthIndicator.health();

        // Assert
        assertEquals(Status.DOWN, health.getStatus());
        assertTrue(health.getDetails().containsKey("Database table User"));
        assertEquals("Down", health.getDetails().get("Database table User"));
        assertNotNull(health.getDetails().get("error"));
    }

    @Test
    void health_shouldReturnDownWhenUserTableDoesNotExist() {
        // Arrange
        when(entityManager.createQuery(anyString()))
                .thenThrow(new IllegalArgumentException("Unknown entity: User"));

        // Act
        Health health = healthIndicator.health();

        // Assert
        assertEquals(Status.DOWN, health.getStatus());
        assertTrue(health.getDetails().containsKey("Database table User"));
        assertEquals("Down", health.getDetails().get("Database table User"));
        assertNotNull(health.getDetails().get("error"));
    }

    @Test
    void health_shouldReturnDownWhenQueryExecutionFails() {
        // Arrange
        when(entityManager.createQuery(anyString())).thenReturn(query);
        when(query.getResultList()).thenThrow(new RuntimeException("Query execution failed"));

        // Act
        Health health = healthIndicator.health();

        // Assert
        assertEquals(Status.DOWN, health.getStatus());
        assertTrue(health.getDetails().containsKey("Database table User"));
        assertEquals("Down", health.getDetails().get("Database table User"));
        assertNotNull(health.getDetails().get("error"));
    }
}