package org.epam.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.epam.service.TrainingService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("testController")
@Transactional
class TrainingControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TrainingService trainingService;

    @Test
    @DisplayName("Given valid training create request, When POST /trainings, Then return 201 Created")
    @WithMockUser(username = "petrov",roles = {"TRAINER"})
    void createTraining_success() throws Exception {
        String trainingJson = """
        {
            "auth": {
            "username": "ivanov",
            "password": "password123"
          },
            "trainee": "sidorova",
            "trainer": "petrov",
            "name": "some training",
            "date": "2025-07-27T21:46:43.000+0500",
            "duration": 60
        }
        """;

        mockMvc.perform(post("/trainings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(trainingJson))
                .andExpect(status().isCreated());
    }
}