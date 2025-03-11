DROP TABLE IF EXISTS trainer_trainee;
DROP TABLE IF EXISTS trainings;
DROP TABLE IF EXISTS trainers;
DROP TABLE IF EXISTS trainees;
DROP TABLE IF EXISTS training_types;
DROP TABLE IF EXISTS users;


CREATE TABLE users (
                       id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                       firstname VARCHAR(255) NOT NULL,
                       lastname VARCHAR(255) NOT NULL,
                       username VARCHAR(255) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       isActive BOOLEAN NOT NULL
);

CREATE TABLE training_types (
                                id BIGSERIAL PRIMARY KEY,
                                training_type_name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE trainers (
                          id BIGINT PRIMARY KEY,
                          training_type_id BIGINT NOT NULL,
                          FOREIGN KEY (id) REFERENCES users(id) ON DELETE CASCADE,
                          FOREIGN KEY (training_type_id) REFERENCES training_types(id) ON DELETE CASCADE
);

CREATE TABLE trainees (
                          id BIGINT PRIMARY KEY,
                          date_of_birth DATE,
                          address VARCHAR(255),
                          FOREIGN KEY (id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE trainings (
                           id BIGSERIAL PRIMARY KEY,
                           training_name VARCHAR(255) NOT NULL,
                           training_type_id BIGINT NOT NULL,
                           trainer_id BIGINT NOT NULL,
                           trainee_id BIGINT NOT NULL,
                           training_date DATE NOT NULL,
                           training_duration INT NOT NULL,
                           FOREIGN KEY (training_type_id) REFERENCES training_types(id) ON DELETE CASCADE,
                           FOREIGN KEY (trainer_id) REFERENCES trainers(id) ON DELETE CASCADE,
                           FOREIGN KEY (trainee_id) REFERENCES trainees(id) ON DELETE CASCADE
);

CREATE TABLE trainer_trainee (
                                 trainer_id BIGINT NOT NULL,
                                 trainee_id BIGINT NOT NULL,
                                 PRIMARY KEY (trainer_id, trainee_id),
                                 FOREIGN KEY (trainer_id) REFERENCES trainers(id) ON DELETE CASCADE,
                                 FOREIGN KEY (trainee_id) REFERENCES trainees(id) ON DELETE CASCADE
);
