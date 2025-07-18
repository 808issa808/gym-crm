package org.epam.service;

import jakarta.persistence.EntityManager;
import org.epam.model.TrainingType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Import(TrainingTypeService.class)
@ActiveProfiles("test")
@Transactional
class TrainingTypeServiceComponentTest {

    @Autowired
    private TrainingTypeService trainingTypeService;

    @Autowired
    private EntityManager entityManager;

    @Test
    void getAll_shouldReturnAllTrainingTypes() {
        TrainingType type1 = new TrainingType();
        type1.setTrainingTypeName("Yoga");
        entityManager.persist(type1);
        TrainingType type2 = new TrainingType();
        type2.setTrainingTypeName("Boxing");
        entityManager.persist(type2);

        List<TrainingType> result = trainingTypeService.getAll();

        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(t -> "Yoga".equals(t.getTrainingTypeName())));
        assertTrue(result.stream().anyMatch(t -> "Boxing".equals(t.getTrainingTypeName())));
    }
}