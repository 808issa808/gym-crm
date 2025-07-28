package org.epam.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.epam.model.TrainingType;
import org.epam.service.TrainingTypeService;
import org.epam.web.dto.users.UserCredentialsDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("testController")
@Transactional
public class TrainingTypeControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TrainingTypeService trainingTypeService;

    @Test
    @DisplayName("GET /training-types returns list of training types")
    @WithMockUser(username = "petrov",roles = {"TRAINER"})
    void getAllTrainingTypes_success() throws Exception {
        // Given
        List<TrainingType> types = List.of(
                new TrainingType(1L, "Yoga"),
                new TrainingType(2L, "Boxing")
        );
        when(trainingTypeService.getAll()).thenReturn(types);

        UserCredentialsDto creds = new UserCredentialsDto("petrov", "password2");
        String credsJson = objectMapper.writeValueAsString(creds);

        // When & Then
        mockMvc.perform(get("/training-types")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(credsJson))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].trainingTypeName").value("Yoga"))
                .andExpect(jsonPath("$[1].trainingTypeName").value("Boxing"));
    }
}
