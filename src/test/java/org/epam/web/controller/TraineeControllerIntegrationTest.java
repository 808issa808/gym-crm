package org.epam.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.epam.data.TraineeRepository;
import org.epam.data.TrainerRepository;
import org.epam.data.impl.TrainingTypeRepository;
import org.epam.model.Trainee;
import org.epam.model.Trainer;
import org.epam.security.jwt.JwtService;
import org.epam.web.dto.training.TraineeTrainingsRequest;
import org.epam.web.dto.users.ChangeLoginRequest;
import org.epam.web.dto.users.UserCredentialsDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("testController")
@Transactional
class TraineeControllerIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private TraineeRepository traineeRepository;
    @Autowired private TrainerRepository trainerRepository;
    @Autowired private TrainingTypeRepository trainingTypeRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private JwtService jwtService;

    private String jwt;
    private Trainee savedTrainee;

    @BeforeEach
    void setup() {
        savedTrainee = new Trainee();
        savedTrainee.setFirstName("Ivan");
        savedTrainee.setLastName("Ivanov");
        savedTrainee.setUsername("ivan_login");
        savedTrainee.setPassword(passwordEncoder.encode("password123"));
        savedTrainee.setActive(true);
        savedTrainee.setAddress("г. Алматы, ул. Абая 10");
        traineeRepository.save(savedTrainee);

        jwt = jwtService.generateToken(mapToUserDetails(savedTrainee));
    }

    private UserDetails mapToUserDetails(Trainee trainee) {
        return new org.springframework.security.core.userdetails.User(
                trainee.getUsername(), trainee.getPassword(), List.of(() -> "ROLE_TRAINEE")
        );
    }

    @Test
    void shouldChangePassword() throws Exception {
        // Given
        ChangeLoginRequest request = new ChangeLoginRequest();
        request.setNewPassword("newPass123");
        request.setPassword("anything");
        request.setUsername("anyone");

        // When / Then
        mockMvc.perform(put("/trainees/change-login")
                        .header("Authorization", "Bearer " + jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void register_ShouldReturn201_WhenValidRequest() throws Exception {
        // Given
        String requestBody = """
                {
                  "firstName": "Иван",
                  "lastName": "Иванов",
                  "dateOfBirth": "2000-01-01",
                  "address": "г. Алматы, ул. Абая 10"
                }
                """;

        // When / Then
        mockMvc.perform(post("/trainees/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(content().string(not(emptyOrNullString())));
    }

    @Test
    void register_ShouldReturn400_WhenInvalidRequest() throws Exception {
        // Given
        String invalidBody = """
                {
                  "firstName": "",
                  "lastName": "Иванов"
                }
                """;

        // When / Then
        mockMvc.perform(post("/trainees/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    void login_shouldReturnToken_whenCorrectLoginRequest() throws Exception {
        // Given
        String loginJson = """
                {
                  "username": "ivan_login",
                  "password": "password123"
                }
                """;

        // When / Then
        mockMvc.perform(get("/trainees/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().isOk())
                .andExpect(content().string(not(emptyOrNullString())));
    }

    @Test
    void login_shouldReturn400_whenBadLoginRequest() throws Exception {
        // Given
        String badRequest = """
                {
                  "username": "",
                  "password": "password123"
                }
                """;

        // When / Then
        mockMvc.perform(get("/trainees/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(badRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    void login_shouldReturn401_whenIncorrectPassword() throws Exception {
        // Given
        String invalidLogin = """
                {
                  "username": "ivan_login",
                  "password": "wrongPassword"
                }
                """;

        // When / Then
        mockMvc.perform(get("/trainees/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidLogin))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getProfile_shouldReturnCorrectProfileWithTrainer() throws Exception {
        // Given
        Trainer trainer = new Trainer();
        trainer.setFirstName("Test");
        trainer.setLastName("trainer");
        trainer.setUsername("trainer_login");
        trainer.setPassword(passwordEncoder.encode("password123"));
        trainer.setActive(true);
        trainer.setSpecialization(trainingTypeRepository.findById(2L).orElseThrow());
        trainerRepository.save(trainer);

        savedTrainee.setTrainers(new ArrayList<>(List.of(trainer)));
        traineeRepository.save(savedTrainee);

        // When / Then
        mockMvc.perform(get("/trainees/" + savedTrainee.getUsername())
                        .header("Authorization", "Bearer " + jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "username": "ivan_login",
                                  "password": "password123"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(savedTrainee.getUsername()))
                .andExpect(jsonPath("$.trainerShortDtoList.length()").value(1))
                .andExpect(jsonPath("$.trainerShortDtoList[0].username").value("trainer_login"));
    }

    @Test
    void updateProfile_shouldUpdateFields_WhenValidRequest() throws Exception {
        // Given
        String updateJson = """
        {
          "userCredentialsDto": {
            "username": "%s",
            "password": "password123"
          },
          "traineeDto": {
            "firstName": "IvanNew",
            "lastName": "IvanovNew",
            "username": "%s",
            "active": true,
            "dateOfBirth": "2000-01-01 10:00:00",
            "address": "new address"
          }
        }
        """.formatted(savedTrainee.getUsername(), savedTrainee.getUsername());

        // When / Then
        mockMvc.perform(put("/trainees/" + savedTrainee.getUsername())
                        .header("Authorization", "Bearer " + jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("IvanNew"));
    }

    @Test
    void deleteTrainee_shouldDelete_WhenAuthIsCorrect() throws Exception {
        // When / Then
        mockMvc.perform(delete("/trainees/" + savedTrainee.getUsername())
                        .header("Authorization", "Bearer " + jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "username": "ivan_login",
                                  "password": "password123"
                                }
                            """))
                .andExpect(status().isOk());
    }

    @Test
    void toggleActivate_shouldSwitchActiveState() throws Exception {
        // When / Then
        mockMvc.perform(patch("/trainees/" + savedTrainee.getUsername())
                        .header("Authorization", "Bearer " + jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "username": "ivan_login",
                                  "password": "password123"
                                }
                            """))
                .andExpect(status().isOk());
    }

    @Test
    void getNotAssignedTrainers_shouldReturnList() throws Exception {
        // Given
        Trainer trainer = new Trainer();
        trainer.setFirstName("Unassigned");
        trainer.setLastName("Trainer");
        trainer.setUsername("unassigned_trainer");
        trainer.setPassword(passwordEncoder.encode("password123"));
        trainer.setActive(true);
        trainer.setSpecialization(trainingTypeRepository.findById(1L).orElseThrow());
        trainerRepository.save(trainer);

        // When / Then
        mockMvc.perform(get("/trainees/" + savedTrainee.getUsername() + "/not-assigned-trainers")
                        .header("Authorization", "Bearer " + jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "username": "ivan_login",
                                  "password": "password123"
                                }
                            """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[2].username").value("unassigned_trainer"));
    }

    @Test
    void updateTrainersList_shouldUpdateAssignedTrainers() throws Exception {
        // Given
        Trainer trainer = new Trainer();
        trainer.setFirstName("Assign");
        trainer.setLastName("Trainer");
        trainer.setUsername("assigned_trainer");
        trainer.setPassword(passwordEncoder.encode("password123"));
        trainer.setActive(true);
        trainer.setSpecialization(trainingTypeRepository.findById(1L).orElseThrow());
        trainerRepository.save(trainer);

        String requestJson = """
        {
          "loginDto": {
            "username": "%s",
            "password": "%s"
          },
          "updateUsernames": {
            "trainerUsernameList": ["assigned_trainer"]
          }
        }
        """.formatted(savedTrainee.getUsername(), "password123");

        // When / Then
        mockMvc.perform(put("/trainees/" + savedTrainee.getUsername() + "/trainers-list")
                        .header("Authorization", "Bearer " + jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("assigned_trainer"));
    }

    @Test
    void findByTraineeUsernameCriteria_shouldReturnTrainings_whenValidRequest() throws Exception {
        // Given
        TraineeTrainingsRequest request = new TraineeTrainingsRequest();
        UserCredentialsDto credentials = new UserCredentialsDto();
        credentials.setUsername("ivanov");
        credentials.setPassword("password123");
        request.setAuth(credentials);
        request.setPeriodFrom(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2004-01-01 00:00:00"));
        request.setPeriodTo(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2025-12-31 23:59:59"));

        String json = objectMapper.writeValueAsString(request);

        // When / Then
        mockMvc.perform(get("/trainees/ivanov/trainings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .header("Authorization", "Bearer " + jwt))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}