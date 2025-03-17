-- Добавляем пользователей
INSERT INTO users (firstname, lastname, username, password, is_active) VALUES
                                                                             ('Ivan', 'Иванов', 'ivanov', 'password1', true),
                                                                             ('Петр', 'Петров', 'petrov', 'password2', true),
                                                                             ('Мария', 'Сидорова', 'sidorova', 'password3', true),
                                                                             ('Анна', 'Козлова', 'kozlova', 'password4', true),
                                                                             ('Удаляемый', 'Иванов', 'deleting', 'password1', true);

-- Добавляем типы тренировок
INSERT INTO training_types (training_type_name) VALUES
                                                    ('Йога'),
                                                    ('Кроссфит'),
                                                    ('Бокс');

-- Добавляем тренеров (ссылаются на users)
INSERT INTO trainers (id, training_type_id) VALUES
                                                (1, 1),  -- Иван - тренер по йоге
                                                (2, 2);  -- Петр - тренер по кроссфиту

-- Добавляем учеников (ссылаются на users)
INSERT INTO trainees (id, date_of_birth, address) VALUES
                                                      (3, '1995-06-15', 'ул. Ленина, 10'),
                                                      (4, '1998-09-23', 'пр-т Победы, 25'),
                                                      (5, '1998-09-23', 'пр-т удаления, 204');

-- Добавляем тренировки
INSERT INTO trainings (training_name, training_type_id, trainer_id, trainee_id, training_date, training_duration) VALUES
                                                                                                                      ('Йога для начинающих', 1, 1, 3, '2024-03-12', 60),
                                                                                                                      ('Кроссфит интенсив', 2, 2, 4, '2024-03-13', 90);

-- Добавляем связи тренер-ученик
INSERT INTO trainer_trainee (trainer_id, trainee_id) VALUES
                                                         (1, 3), -- Иван обучает Марию
                                                         (2, 4); -- Петр обучает Анну
