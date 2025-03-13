package org.epam.service;

import org.epam.data.impl.TrainerRepositoryImpl;
import org.epam.model.Trainer;
import org.epam.util.Authenticator;
import org.epam.util.UserUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainerServiceTest {

    @Mock
    private TrainerRepositoryImpl trainerRepository;

    @InjectMocks
    private TrainerService trainerService;

    private Trainer trainer;

    @BeforeEach
    void setUp() {
        trainer = new Trainer();
        trainer.setFirstName("John");
        trainer.setLastName("Doe");
        trainer.setUsername("trainer1");
        trainer.setPassword("securePass");
        trainer.setActive(true);
    }

    @Test
    void testCreate() {
        when(trainerRepository.existsByUsername(anyString())).thenReturn(false);
        when(trainerRepository.create(any(Trainer.class))).thenAnswer(invocation -> {
            Trainer createdTrainer = invocation.getArgument(0);
            createdTrainer.setId(1L);
            return createdTrainer;
        });

        Trainer createdTrainer = trainerService.create(trainer);

        assertNotNull(createdTrainer, "Созданный тренер не должен быть null");
        assertNotNull(createdTrainer.getUsername(), "Username должен быть установлен");
        assertNotNull(createdTrainer.getPassword(), "Пароль должен быть установлен");
        assertTrue(createdTrainer.isActive(), "Тренер должен быть активным");

        verify(trainerRepository).existsByUsername(anyString());
        verify(trainerRepository).create(any(Trainer.class));
    }

    @Test
    void testFindByUsername() {
        try (MockedStatic<Authenticator> mockedAuthenticator = mockStatic(Authenticator.class)) {
            mockedAuthenticator.when(() -> Authenticator.authenticateUser("trainer1", "securePass", trainerRepository::findByUsername)).then(invocation -> null);

            when(trainerRepository.findByUsername("trainer1")).thenReturn(Optional.of(trainer));

            Trainer found = trainerService.findByUsername("trainer1", "securePass", "trainer1");

            assertEquals(trainer, found);
            verify(trainerRepository, times(1)).findByUsername("trainer1");
        }
    }

    @Test
    void testChangePassword() {
        String newPassword = "newSecurePass";

        try (MockedStatic<Authenticator> mockedAuthenticator = mockStatic(Authenticator.class); MockedStatic<UserUtil> mockedUserUtil = mockStatic(UserUtil.class)) {

            mockedAuthenticator.when(() -> Authenticator.authenticateUser("trainer1", "securePass", trainerRepository::findByUsername)).then(invocation -> null);
            mockedUserUtil.when(() -> UserUtil.passwordFormatValidator(newPassword)).thenReturn(true);

            when(trainerRepository.changePassword(trainer, newPassword)).thenReturn(trainer);

            Trainer updatedTrainer = trainerService.changePassword(trainer, newPassword);

            assertEquals(trainer, updatedTrainer);
            verify(trainerRepository).changePassword(trainer, newPassword);
        }
    }

    @Test
    void testChangePassword_InvalidFormat() {
        String invalidPassword = "short";

        try (MockedStatic<Authenticator> mockedAuthenticator = mockStatic(Authenticator.class); MockedStatic<UserUtil> mockedUserUtil = mockStatic(UserUtil.class)) {

            mockedAuthenticator.when(() -> Authenticator.authenticateUser("trainer1", "securePass", trainerRepository::findByUsername)).then(invocation -> null);
            mockedUserUtil.when(() -> UserUtil.passwordFormatValidator(invalidPassword)).thenReturn(false);

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> trainerService.changePassword(trainer, invalidPassword));

            assertEquals("New Password should be at least 10 chars long", exception.getMessage());
            verify(trainerRepository, never()).changePassword(any(), any());
        }
    }

    @Test
    void testUpdate() {
        try (MockedStatic<Authenticator> mockedAuthenticator = mockStatic(Authenticator.class)) {
            mockedAuthenticator.when(() -> Authenticator.authenticateUser("trainer1", "securePass", trainerRepository::findByUsername)).then(invocation -> null);

            when(trainerRepository.update(trainer)).thenReturn(trainer);

            Trainer updatedTrainer = trainerService.update(trainer);

            assertEquals(trainer, updatedTrainer);
            verify(trainerRepository).update(trainer);
        }
    }

    @Test
    void testSwitchActivate() {
        try (MockedStatic<Authenticator> mockedAuthenticator = mockStatic(Authenticator.class)) {
            mockedAuthenticator.when(() -> Authenticator.authenticateUser("trainer1", "securePass", trainerRepository::findByUsername)).then(invocation -> null);

            trainerService.switchActivate(trainer);

            verify(trainerRepository).switchActivate("trainer1");
        }
    }
}
