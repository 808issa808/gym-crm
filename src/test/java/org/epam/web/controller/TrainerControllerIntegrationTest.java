package org.epam.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.epam.service.TrainerService;
import org.epam.service.TrainingService;
import org.epam.service.workload.dto.TrainerSummary;
import org.epam.web.dto.training.TrainerTrainingsRequest;
import org.epam.web.dto.users.ChangeLoginRequest;
import org.epam.web.dto.users.UserCredentialsDto;
import org.epam.web.dto.users.trainer.TrainerRegistrationRequest;
import org.epam.web.dto.users.trainer.TrainerUpdateRequest;
import org.epam.web.dto.users.trainer.TrainerWithListDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TrainerControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TrainerService trainerService;

    @MockBean
    private TrainingService trainingService;

    @Test
    @DisplayName("Should register new trainer")
    void registerTrainer() throws Exception {
        // Given
        TrainerRegistrationRequest request = new TrainerRegistrationRequest("John", "Doe", 1L);
        when(trainerService.create(any())).thenReturn("jwt-token");

        // When & Then
        mockMvc.perform(post("/trainers/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().string("jwt-token"));
    }

    @Test
    @DisplayName("Should log in trainer")
    void loginTrainer() throws Exception {
        // Given
        UserCredentialsDto creds = new UserCredentialsDto("john", "password");
        when(trainerService.login(any())).thenReturn("jwt-token");

        // When & Then
        mockMvc.perform(get("/trainers/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(creds)))
                .andExpect(status().isOk())
                .andExpect(content().string("jwt-token"));
    }

    @Test
    @DisplayName("Should change trainer login")
    void changeLogin() throws Exception {
        // Given
        ChangeLoginRequest changeRequest = new ChangeLoginRequest("","old", "new");

        // When & Then
        mockMvc.perform(put("/trainers/change-login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(changeRequest)))
                .andExpect(status().isOk());

        verify(trainerService).changePassword(any());
    }

    @Test
    @DisplayName("Should return trainer profile by username")
    void getTrainerProfile() throws Exception {
        // Given
        UserCredentialsDto creds = new UserCredentialsDto("john", "pass");
        String username = "john";
        TrainerWithListDto response = new TrainerWithListDto();
        when(trainerService.findByUsername(username)).thenReturn(response);

        // When & Then
        mockMvc.perform(get("/trainers/{username}", username)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(creds)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should update trainer profile")
    void updateTrainer() throws Exception {
        // Given
        TrainerUpdateRequest request = new TrainerUpdateRequest();
        when(trainerService.update(any())).thenReturn(new TrainerWithListDto());

        // When & Then
        mockMvc.perform(put("/trainers/{username}", "john")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should toggle trainer activation")
    void toggleActivation() throws Exception {
        // Given
        UserCredentialsDto creds = new UserCredentialsDto("john", "pass");

        // When & Then
        mockMvc.perform(patch("/trainers/{username}", "john")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(creds)))
                .andExpect(status().isOk());

        verify(trainerService).switchActivate();
    }

    @Test
    @DisplayName("Should get trainer trainings by filter")
    void getTrainerTrainings() throws Exception {
        // Given
        TrainerTrainingsRequest request = new TrainerTrainingsRequest();
        when(trainingService.findTrainingsForTrainer(any(), any(), any())).thenReturn(List.of());

        // When & Then
        mockMvc.perform(get("/trainers/{username}/trainings", "john")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return trainer summary")
    void getTrainerSummary() throws Exception {
        // Given
        when(trainingService.getSummary("john")).thenReturn(new TrainerSummary());

        // When & Then
        mockMvc.perform(get("/trainers/summary/john"))
                .andExpect(status().isOk());
    }
}
