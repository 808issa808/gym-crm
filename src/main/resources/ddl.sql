CREATE TABLE Users (
                       ID SERIAL PRIMARY KEY,
                       FirstName VARCHAR(255) NOT NULL,
                       LastName VARCHAR(255) NOT NULL,
                       Username VARCHAR(255) NOT NULL UNIQUE,
                       Password VARCHAR(255) NOT NULL,
                       IsActive BOOLEAN NOT NULL
);

CREATE TABLE Trainee (
                         ID SERIAL PRIMARY KEY,
                         DateOfBirth DATE NULL,
                         Address TEXT NULL,
                         FOREIGN KEY (ID) REFERENCES Users(ID) ON DELETE CASCADE
);

CREATE TABLE Trainer (
                         ID SERIAL PRIMARY KEY,
                         Specialization VARCHAR(255) NOT NULL,
                         FOREIGN KEY (ID) REFERENCES Users(ID) ON DELETE CASCADE
);

CREATE TABLE TrainingType (
                              ID SERIAL PRIMARY KEY,
                              TrainingTypeName VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE Training (
                          ID SERIAL PRIMARY KEY,
                          TraineeId INT NOT NULL,
                          TrainerId INT NOT NULL,
                          TrainingName VARCHAR(255) NOT NULL,
                          TrainingTypeId INT NOT NULL,
                          TrainingDate DATE NOT NULL,
                          TrainingDuration INT NOT NULL,
                          FOREIGN KEY (TraineeId) REFERENCES Trainee(ID) ON DELETE CASCADE,
                          FOREIGN KEY (TrainerId) REFERENCES Trainer(ID) ON DELETE CASCADE,
                          FOREIGN KEY (TrainingTypeId) REFERENCES TrainingType(ID) ON DELETE SET NULL
);

CREATE TABLE Trainer_Trainee (
                                 TrainerID INT NOT NULL,
                                 TraineeID INT NOT NULL,
                                 PRIMARY KEY (TrainerID, TraineeID),
                                 FOREIGN KEY (TrainerID) REFERENCES Trainer(ID) ON DELETE CASCADE,
                                 FOREIGN KEY (TraineeID) REFERENCES Trainee(ID) ON DELETE CASCADE
);