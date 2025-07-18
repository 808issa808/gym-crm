package org.epam.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.epam.data.TraineeRepository;
import org.epam.model.Trainee;
import org.epam.web.dto.users.ChangeLoginRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

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
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private ObjectMapper objectMapper;

    // === Helpers ===

    private Trainee createTestTrainee(String username, String rawPassword) {
        Trainee trainee = new Trainee();
        trainee.setFirstName("Ivan");
        trainee.setLastName("Ivanov");
        trainee.setUsername(username);
        trainee.setPassword(passwordEncoder.encode(rawPassword));
        trainee.setActive(true);
        trainee.setAddress("г. Алматы, ул. Абая 10");
        return traineeRepository.save(trainee);
    }

    private String loginAndGetJwt(String username, String password) throws Exception {
        String loginJson = """
            {
              "username": "%s",
              "password": "%s"
            }
        """.formatted(username, password);

        return mockMvc.perform(get("/trainees/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().isOk())
                .andExpect(content().string(not(emptyOrNullString())))
                .andReturn().getResponse().getContentAsString();
    }

    // === Tests ===

    @Test
    void register_ShouldReturn201_WhenValidRequest() throws Exception {
        String requestBody = """
            {
              "firstName": "Иван",
              "lastName": "Иванов",
              "dateOfBirth": "2000-01-01",
              "address": "г. Алматы, ул. Абая 10"
            }
        """;

        mockMvc.perform(post("/trainees/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(content().string(not(emptyOrNullString())));
    }

    @Test
    void register_ShouldReturn400_WhenInvalidRequest() throws Exception {
        String invalidBody = """
            {
              "firstName": "",
              "lastName": "Иванов"
            }
        """;

        mockMvc.perform(post("/trainees/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    void login_shouldReturnToken_whenCorrectLoginRequest() throws Exception {
        createTestTrainee("ivan_login", "password123");

        String loginJson = """
            {
              "username": "ivan_login",
              "password": "password123"
            }
        """;

        mockMvc.perform(get("/trainees/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().isOk())
                .andExpect(content().string(not(emptyOrNullString())));
    }

    @Test
    void login_shouldReturn400_whenBadLoginRequest() throws Exception {
        createTestTrainee("ivan_login", "password123");

        String badRequest = """
            {
              "username": "",
              "password": "password123"
            }
        """;

        mockMvc.perform(get("/trainees/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(badRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    void login_shouldReturn401_whenIncorrectPassword() throws Exception {
        createTestTrainee("ivan_login", "password123");

        String invalidLogin = """
            {
              "username": "ivan_login",
              "password": "wrongPassword"
            }
        """;

        mockMvc.perform(get("/trainees/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidLogin))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldChangePassword() throws Exception {
        createTestTrainee("ivan_login", "password123");

        String jwt = loginAndGetJwt("ivan_login", "password123");

        ChangeLoginRequest request = new ChangeLoginRequest();
        request.setNewPassword("newPass123");
        request.setPassword("anything"); // старый пароль не проверяется в этом тесте
        request.setUsername("anyone");

        mockMvc.perform(put("/trainees/change-login")
                        .header("Authorization", "Bearer " + jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }
}
