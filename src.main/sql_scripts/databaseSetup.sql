DROP TABLE Happens_In;
DROP TABLE Wins_Season;
DROP TABLE Signed_Contract;
DROP TABLE Team_Plays_Team;
DROP TABLE Player_Plays_for_Team;
DROP TABLE Sponsor_Sponsors_Team;
DROP TABLE Sponsor;
DROP TABLE Works_For;
DROP TABLE Staff;
DROP TABLE Doctor;
DROP table Manager;
DROP TABLE Coach;
DROP TABLE Trainer;
DROP TABLE Team;
DROP TABLE Division;
DROP TABLE GameSession;
DROP TABLE Location;
DROP TABLE Season;
DROP TABLE League;

CREATE TABLE Sponsor
(
    SID    INTEGER PRIMARY KEY,
    Name   VARCHAR2 (50) NOT NULL,
    Slogan VARCHAR2 (100)
);
--
CREATE TABLE Division
(
    Division_Name VARCHAR2 (50) PRIMARY KEY,
    Conference    VARCHAR2 (50)
);

CREATE TABLE Team
(
    TName         VARCHAR2 (50),
    Cap_Space     INTEGER,
    Arena         VARCHAR2 (50),
    City          VARCHAR2 (50),
    DName VARCHAR2 (50) NOT NULL,
    PRIMARY KEY (TName, City),
    FOREIGN KEY (DName )REFERENCES Division (Division_Name)
        ON DELETE CASCADE
);



CREATE TABLE League
(
    Name         VARCHAR2 (50) PRIMARY KEY,
    Revenue      INTEGER,
    Commissioner VARCHAR2 (50)
);

CREATE TABLE Location
(
    Venue_Name VARCHAR2 (50) ,
    City  VARCHAR2 (50) ,
    PRIMARY KEY (Venue_Name)
);

CREATE TABLE GameSession
(
    Venue VARCHAR2 (50),
    played_date  DATE,
    PRIMARY KEY (Venue, played_date),
    FOREIGN KEY (Venue)
        REFERENCES Location (Venue_Name)
);

CREATE TABLE Season
(
    Start_Date DATE,
    LName      VARCHAR2 (50),
    End_Date   DATE,
    PRIMARY KEY (Start_Date, LName),
    FOREIGN KEY (LName)
        REFERENCES League (Name)
);


CREATE TABLE Staff
(
    StID   INTEGER PRIMARY KEY,
    Name   VARCHAR2 (50) NOT NULL,
    Salary INTEGER
);
--
CREATE TABLE Doctor
(
    StID  INTEGER PRIMARY KEY,
    Field VARCHAR2 (50)
);

CREATE TABLE Coach
(
    StID            INTEGER PRIMARY KEY,
    number_of_rings INTEGER
);

CREATE TABLE Trainer
(
    StID      INTEGER PRIMARY KEY,
    Specialty VARCHAR2 (50)
);

CREATE TABLE Manager
(
    StID                INTEGER PRIMARY KEY,
    Years_of_Experience INTEGER
);

CREATE TABLE Player_Plays_for_Team
(
    Debut_Year    DATE,
    Date_of_Birth DATE,
    Height        INTEGER,
    Name          VARCHAR2 (50) NOT NULL,
    Jersey#       INTEGER,
    PID           INTEGER PRIMARY KEY,
    TName         VARCHAR2 (50),
    City          VARCHAR2 (50),
    FOREIGN KEY (TName, City) REFERENCES Team (TName, City)
        ON DELETE SET NULL
--         ON UPDATE CASCADE
);

CREATE TABLE Signed_Contract
(
    Bonus       INTEGER,
    PID         INTEGER NOT NULL,
    Length      INTEGER,
    Value       INTEGER,
    Signed_Date DATE,
    CID         INTEGER PRIMARY KEY,
    FOREIGN KEY (PID)
        REFERENCES Player_Plays_for_Team (PID)
            ON DELETE SET NULL
--         ON UPDATE CASCADE
);


CREATE TABLE Sponsor_Sponsors_Team
(
    Sponsored_Amount INTEGER,
    SID              INTEGER,
    TName            VARCHAR2 (50),
    City             VARCHAR2 (50),
    PRIMARY KEY (TName, City, SID),
    FOREIGN KEY (SID) REFERENCES Sponsor (SID),
    FOREIGN KEY (TName, City) REFERENCES Team (TName, City)
);
--
CREATE TABLE Works_For
(
    StID  INTEGER,
    TName VARCHAR2 (50),
    City  VARCHAR2 (50),
    PRIMARY KEY (StID, TName, City),
    FOREIGN KEY (StID) REFERENCES Staff (StID),
    FOREIGN KEY (TName, City) REFERENCES Team (TName, City)
);

CREATE TABLE Team_Plays_Team
(
    HomeTeamName VARCHAR2 (50),
    HomeTeamCity VARCHAR2 (50),
    AwayTeamName VARCHAR2 (50),
    AwayTeamCity VARCHAR2 (50),
    played_date  DATE,
    Venue        VARCHAR2 (50),
    PRIMARY KEY (HomeTeamName, HomeTeamCity, AwayTeamName, AwayTeamCity, played_date, Venue),
    FOREIGN KEY (HomeTeamName, HomeTeamCity) REFERENCES Team (TName, City),
    FOREIGN KEY (AwayTeamName, AwayTeamCity) REFERENCES Team (TName, City),
    FOREIGN KEY (played_date, Venue) REFERENCES GameSession (played_date, Venue)
);

CREATE TABLE Happens_In
(
    Start_Date   Date,
    LName        VARCHAR2 (50),
    Venue        VARCHAR2 (50),
    played_date  Date,
    PRIMARY KEY (Start_Date, LName, Venue, played_date),
    FOREIGN KEY (Start_Date, LName) REFERENCES Season(Start_Date, LName),
    FOREIGN KEY (Venue, played_date) REFERENCES GameSession(Venue, played_date)
);

CREATE TABLE Wins_Season
(
    Start_Date date,
    LName      VARCHAR2 (50),
    TName      VARCHAR2 (50),
    City       VARCHAR2 (50),
    PRIMARY KEY (Start_Date, LName, TName, City),
    FOREIGN KEY (Start_Date, LName) REFERENCES Season(Start_Date, LName),
    FOREIGN KEY (TName, City) REFERENCES Team (TName, City)
);

-- Division table
INSERT INTO Division VALUES ('Atlantic', 'Eastern');
INSERT INTO Division VALUES ('Central', 'Eastern');
INSERT INTO Division VALUES ('Pacific', 'Western');
INSERT INTO Division VALUES ('Southwest', 'Western');
INSERT INTO Division VALUES ('Southeast', 'Eastern');


-- Team table
INSERT INTO Team VALUES ('Raptors', 136000000, 'ScotiaBank Arena', 'Toronto', 'Atlantic');
INSERT INTO Team VALUES ('Pacers', 136123345, 'Gainbridge Fieldhouse', 'Indiana', 'Central');
INSERT INTO Team VALUES ('Thunder', 123123123, 'Paycom Center', 'Oklahoma City', 'Southwest');
INSERT INTO Team VALUES ('Lakers', 139242132, 'Crypto Arena', 'Los Angeles', 'Pacific');
INSERT INTO Team VALUES ('Clippers', 139242134, 'Crypto Arena', 'Los Angeles', 'Pacific');

-- -- Sponsor table
INSERT INTO Sponsor
VALUES (1, 'EcoFusion Solutions', 'Harmony in Every Innovation – Where Green Meets Ingenuity');

INSERT INTO Sponsor
VALUES (2, 'Quantum Quench Beverages', 'Taste the Future, One Quantum Leap at a Time!');

INSERT INTO Sponsor
VALUES (3, 'Galactic Gearworks', 'Universe''s Finest Creations, Engineered for the Stars!');

INSERT INTO Sponsor
VALUES (4, 'Eris', 'Just bang it!');

INSERT INTO Sponsor
VALUES (5, 'InfiniTech Labs', 'Limitless Possibilities, Crafted in Every Code!');

-- INSERT INTO Sponsor
-- VALUES (6, 'Additional Sponsor', 'Test division');

-- Sponsor Sponsors Team
INSERT INTO Sponsor_Sponsors_Team
VALUES (100000, 1, 'Raptors', 'Toronto');

INSERT INTO Sponsor_Sponsors_Team
VALUES (200000, 2, 'Pacers', 'Indiana');

INSERT INTO Sponsor_Sponsors_Team
VALUES (220000, 5, 'Clippers', 'Los Angeles');

INSERT INTO Sponsor_Sponsors_Team
VALUES (50000, 1, 'Thunder', 'Oklahoma City');

INSERT INTO Sponsor_Sponsors_Team
VALUES (12000, 2, 'Thunder', 'Oklahoma City');

INSERT INTO Sponsor_Sponsors_Team
VALUES (140000, 3, 'Thunder', 'Oklahoma City');

INSERT INTO Sponsor_Sponsors_Team
VALUES (20000, 4, 'Thunder', 'Oklahoma City');

INSERT INTO Sponsor_Sponsors_Team
VALUES (11000, 5, 'Thunder', 'Oklahoma City');

INSERT INTO Sponsor_Sponsors_Team
VALUES (100000, 1, 'Lakers', 'Los Angeles');

INSERT INTO Sponsor_Sponsors_Team
VALUES (200000, 2, 'Lakers', 'Los Angeles');

INSERT INTO Sponsor_Sponsors_Team
VALUES (140000, 3, 'Lakers', 'Los Angeles');

INSERT INTO Sponsor_Sponsors_Team
VALUES (370000, 4, 'Lakers', 'Los Angeles');

INSERT INTO Sponsor_Sponsors_Team
VALUES (13000, 5, 'Lakers', 'Los Angeles');


-- League table
INSERT INTO League VALUES ('NBA', 1234000123, 'Adam Silver');
INSERT INTO League VALUES ('WNBA', 1234009, 'Cathy Engelbert');
INSERT INTO League VALUES ('G-League', 1234001, 'Shareef Abdur-Rahim');
INSERT INTO League VALUES ('EuroLeague', 223400, 'Dejan Bodiroga');
INSERT INTO League VALUES ('CBA', 9234000, 'Yao Ming');

-- Location table
INSERT INTO Location VALUES ('Madison Square Garden', 'New York City');
INSERT INTO Location VALUES ('Crypto Arena', 'Los Angeles');
INSERT INTO Location VALUES ('ScotiaBank Arena', 'Toronto');
INSERT INTO Location VALUES ('Paycom Center', 'Oklahoma City');
INSERT INTO Location VALUES ('Gainbridge Fieldhouse', 'Indiana');

-- Session table
INSERT INTO GameSession VALUES ('Madison Square Garden', TO_DATE('2018-11-03', 'YYYY-MM-DD'));
INSERT INTO GameSession VALUES ('Crypto Arena', TO_DATE('2019-11-03', 'YYYY-MM-DD'));
INSERT INTO GameSession VALUES ('ScotiaBank Arena', TO_DATE('2020-11-03', 'YYYY-MM-DD'));
INSERT INTO GameSession VALUES ('Paycom Center', TO_DATE('2021-11-03', 'YYYY-MM-DD'));
INSERT INTO GameSession VALUES ('Gainbridge Fieldhouse', TO_DATE('2022-11-03', 'YYYY-MM-DD'));

-- Season table
INSERT INTO Season VALUES ( TO_DATE('2018-09-22', 'YYYY-MM-DD'), 'NBA',  TO_DATE('2019-06-22', 'YYYY-MM-DD'));
INSERT INTO Season VALUES ( TO_DATE('2019-09-22', 'YYYY-MM-DD'), 'NBA',  TO_DATE('2020-06-22', 'YYYY-MM-DD'));
INSERT INTO Season VALUES ( TO_DATE('2020-09-22', 'YYYY-MM-DD'), 'NBA',  TO_DATE('2021-06-22', 'YYYY-MM-DD'));
INSERT INTO Season VALUES ( TO_DATE('2021-09-22', 'YYYY-MM-DD'), 'NBA',  TO_DATE('2022-06-22', 'YYYY-MM-DD'));
INSERT INTO Season VALUES ( TO_DATE('2022-09-22', 'YYYY-MM-DD'), 'NBA',  TO_DATE('2023-06-22', 'YYYY-MM-DD'));

-- -- Staff table
INSERT INTO Staff VALUES (1, 'Logan Martinez', 350000);
INSERT INTO Staff VALUES (2, 'Grace Garcia', 250000);
INSERT INTO Staff VALUES (3, 'Michael Johnson', 120000);
INSERT INTO Staff VALUES (4, 'Emily Davis', 48000);
INSERT INTO Staff VALUES (5, 'Christopher Lee', 52000);
INSERT INTO Staff VALUES (6, 'Amanda Brown', 60000);
INSERT INTO Staff VALUES (7, 'Daniel Wilson', 80000);
INSERT INTO Staff VALUES (8, 'Olivia White', 45000);
INSERT INTO Staff VALUES (9, 'Matthew Taylor', 70000);
INSERT INTO Staff VALUES (10, 'Sophia Moore', 30000);
INSERT INTO Staff VALUES (11, 'Ethan Anderson', 90000);
INSERT INTO Staff VALUES (12, 'Isabella Garcia', 55000);
INSERT INTO Staff VALUES (13, 'William Martinez', 42000);
INSERT INTO Staff VALUES (14, 'Abigail Robinson', 48000);
INSERT INTO Staff VALUES (15, 'James Hernandez', 75000);
INSERT INTO Staff VALUES (16, 'Mia Smith', 38000);
INSERT INTO Staff VALUES (17, 'Alexander Johnson', 92000);
INSERT INTO Staff VALUES (18, 'Ava Davis', 32000);
INSERT INTO Staff VALUES (19, 'Benjamin Brown', 88000);
INSERT INTO Staff VALUES (20, 'Charlotte Wilson', 42000);


-- Doctor table
INSERT INTO Doctor VALUES (1, 'Cardiology');
INSERT INTO Doctor VALUES (2, 'Pediatrics');
INSERT INTO Doctor VALUES (3, 'Neurology');
INSERT INTO Doctor VALUES (4, 'Orthopedics');
INSERT INTO Doctor VALUES (5, 'Psychiatry');

-- Coach table
INSERT INTO Coach VALUES (6, 3);
INSERT INTO Coach VALUES (7, 5);
INSERT INTO Coach VALUES (8, 2);
INSERT INTO Coach VALUES (9, 4);
INSERT INTO Coach VALUES (10, 6);
--
-- Trainer table
INSERT INTO Trainer VALUES (11, 'Strength and Conditioning');
INSERT INTO Trainer VALUES (12, 'Sports Rehabilitation');
INSERT INTO Trainer VALUES (13, 'Nutrition and Dietetics');
INSERT INTO Trainer VALUES (14, 'Yoga and Flexibility');
INSERT INTO Trainer VALUES (15, 'High-Intensity Interval Training');

-- Manager table
INSERT INTO Manager VALUES (16, 15);
INSERT INTO Manager VALUES (17, 12);
INSERT INTO Manager VALUES (18, 20);
INSERT INTO Manager VALUES (19, 18);
INSERT INTO Manager VALUES (20, 22);

-- Works for Table
INSERT INTO Works_For VALUES (1, 'Raptors', 'Toronto');
INSERT INTO Works_For VALUES (2, 'Pacers', 'Indiana');
INSERT INTO Works_For VALUES (3, 'Thunder', 'Oklahoma City');
INSERT INTO Works_For VALUES (4, 'Lakers', 'Los Angeles');
INSERT INTO Works_For VALUES (5, 'Clippers', 'Los Angeles');

INSERT INTO Works_For VALUES (6, 'Raptors', 'Toronto');
INSERT INTO Works_For VALUES (7, 'Pacers', 'Indiana');
INSERT INTO Works_For VALUES (8, 'Thunder', 'Oklahoma City');
INSERT INTO Works_For VALUES (9, 'Lakers', 'Los Angeles');
INSERT INTO Works_For VALUES (10, 'Clippers', 'Los Angeles');

INSERT INTO Works_For VALUES (11, 'Raptors', 'Toronto');
INSERT INTO Works_For VALUES (12, 'Pacers', 'Indiana');
INSERT INTO Works_For VALUES (13, 'Thunder', 'Oklahoma City');
INSERT INTO Works_For VALUES (14, 'Lakers', 'Los Angeles');
INSERT INTO Works_For VALUES (15, 'Clippers', 'Los Angeles');

INSERT INTO Works_For VALUES (16, 'Raptors', 'Toronto');
INSERT INTO Works_For VALUES (17, 'Pacers', 'Indiana');
INSERT INTO Works_For VALUES (18, 'Thunder', 'Oklahoma City');
INSERT INTO Works_For VALUES (19, 'Lakers', 'Los Angeles');
INSERT INTO Works_For VALUES (20, 'Clippers', 'Los Angeles');

-- Player_Plays_For_Team table
INSERT INTO Player_Plays_For_Team VALUES (TO_DATE('2018-06-21', 'YYYY-MM-DD'), TO_DATE('1999-02-28', 'YYYY-MM-DD'), 201, 'Luka Doncic', 77, 2473875, 'Raptors', 'Toronto');
INSERT INTO Player_Plays_For_Team VALUES (TO_DATE('2015-06-25', 'YYYY-MM-DD'), TO_DATE('1993-03-14', 'YYYY-MM-DD'), 203, 'Stephen Curry', 30, 1729634, 'Pacers', 'Indiana');
INSERT INTO Player_Plays_For_Team VALUES (TO_DATE('2014-06-26', 'YYYY-MM-DD'), TO_DATE('1988-12-30', 'YYYY-MM-DD'), 206, 'LeBron James', 23, 1248756, 'Lakers', 'Los Angeles');
INSERT INTO Player_Plays_For_Team VALUES (TO_DATE('2013-06-27', 'YYYY-MM-DD'), TO_DATE('1989-03-29', 'YYYY-MM-DD'), 201, 'Anthony Davis', 3, 1342351, 'Lakers', 'Los Angeles');
INSERT INTO Player_Plays_For_Team VALUES (TO_DATE('2016-06-23', 'YYYY-MM-DD'), TO_DATE('1994-03-16', 'YYYY-MM-DD'), 193, 'Giannis Antetokounmpo', 34, 1534897, 'Thunder', 'Oklahoma City');
INSERT INTO Player_Plays_For_Team VALUES (TO_DATE('2019-06-20', 'YYYY-MM-DD'), TO_DATE('1998-01-30', 'YYYY-MM-DD'), 205, 'Zion Williamson', 1, 1876543, 'Raptors', 'Toronto');
INSERT INTO Player_Plays_For_Team VALUES (TO_DATE('2017-06-22', 'YYYY-MM-DD'), TO_DATE('1995-08-23', 'YYYY-MM-DD'), 204, 'Devin Booker', 1, 1987654, 'Pacers', 'Indiana');
INSERT INTO Player_Plays_For_Team VALUES (TO_DATE('2016-06-23', 'YYYY-MM-DD'), TO_DATE('1994-08-07', 'YYYY-MM-DD'), 207, 'Kawhi Leonard', 2, 1654321, 'Clippers', 'Los Angeles');
INSERT INTO Player_Plays_For_Team VALUES (TO_DATE('2018-06-21', 'YYYY-MM-DD'), TO_DATE('1999-06-29', 'YYYY-MM-DD'), 202, 'Trae Young', 11, 1765432, 'Thunder', 'Oklahoma City');
INSERT INTO Player_Plays_For_Team VALUES (TO_DATE('2015-06-25', 'YYYY-MM-DD'), TO_DATE('1992-05-04', 'YYYY-MM-DD'), 208, 'Karl-Anthony Towns', 32, 1898765, 'Lakers', 'Los Angeles');
INSERT INTO Player_Plays_For_Team VALUES (TO_DATE('2017-06-22', 'YYYY-MM-DD'), TO_DATE('1996-07-20', 'YYYY-MM-DD'), 209, 'Jayson Tatum', 0, 2019876, 'Raptors', 'Toronto');
INSERT INTO Player_Plays_For_Team VALUES (TO_DATE('2019-06-20', 'YYYY-MM-DD'), TO_DATE('1998-03-05', 'YYYY-MM-DD'), 210, 'Jamal Murray', 27, 1789098, 'Pacers', 'Indiana');
INSERT INTO Player_Plays_For_Team VALUES (TO_DATE('2016-06-23', 'YYYY-MM-DD'), TO_DATE('1994-07-03', 'YYYY-MM-DD'), 211, 'Ben Simmons', 25, 1912345, 'Clippers', 'Los Angeles');
INSERT INTO Player_Plays_For_Team VALUES (TO_DATE('2018-06-21', 'YYYY-MM-DD'), TO_DATE('1997-02-17', 'YYYY-MM-DD'), 212, 'Donovan Mitchell', 45, 1945678, 'Thunder', 'Oklahoma City');
INSERT INTO Player_Plays_For_Team VALUES (TO_DATE('2014-06-26', 'YYYY-MM-DD'), TO_DATE('1989-08-08', 'YYYY-MM-DD'), 213, 'CJ McCollum', 3, 2034567, 'Lakers', 'Los Angeles');

-- Signed_Contract table
INSERT INTO Signed_Contract VALUES (50000, 2473875, 5, 80000000, TO_DATE('2023-10-14', 'YYYY-MM-DD'), 1);
INSERT INTO Signed_Contract VALUES (60000, 1729634, 4, 100000000, TO_DATE('2023-10-28', 'YYYY-MM-DD'), 2);
INSERT INTO Signed_Contract VALUES (70000, 1248756, 5, 54000000, TO_DATE('2023-10-06', 'YYYY-MM-DD'), 3);
INSERT INTO Signed_Contract VALUES (55000, 1342351, 2, 85000000, TO_DATE('2023-10-25', 'YYYY-MM-DD'), 4);
INSERT INTO Signed_Contract VALUES (65000, 1534897, 3, 150000000, TO_DATE('2023-10-18', 'YYYY-MM-DD'), 5);
INSERT INTO Signed_Contract VALUES (52000, 1876543, 4, 140000000, TO_DATE('2023-11-02', 'YYYY-MM-DD'), 6);
INSERT INTO Signed_Contract VALUES (58000, 1987654, 3, 40000000, TO_DATE('2023-11-15', 'YYYY-MM-DD'), 7);
INSERT INTO Signed_Contract VALUES (72000, 1654321, 5, 32500000, TO_DATE('2023-11-10', 'YYYY-MM-DD'), 8);
INSERT INTO Signed_Contract VALUES (53000, 1765432, 1, 40000000, TO_DATE('2023-11-23', 'YYYY-MM-DD'), 9);
INSERT INTO Signed_Contract VALUES (67000, 1898765, 5, 56000000, TO_DATE('2023-11-05', 'YYYY-MM-DD'), 10);
INSERT INTO Signed_Contract VALUES (51000, 2019876, 3, 72000000, TO_DATE('2023-11-18', 'YYYY-MM-DD'), 11);
INSERT INTO Signed_Contract VALUES (59000, 1789098, 4, 31000000, TO_DATE('2023-11-13', 'YYYY-MM-DD'), 12);
INSERT INTO Signed_Contract VALUES (70000, 1912345, 5, 25000000, TO_DATE('2023-11-07', 'YYYY-MM-DD'), 13);
INSERT INTO Signed_Contract VALUES (54000, 1945678, 2, 88000000, TO_DATE('2023-11-20', 'YYYY-MM-DD'), 14);
INSERT INTO Signed_Contract VALUES (66000, 2034567, 1, 92000000, TO_DATE('2023-11-03', 'YYYY-MM-DD'), 15);

-- Team_Plays_Team table
INSERT INTO Team_Plays_Team VALUES ('Raptors', 'Toronto', 'Pacers', 'Indiana', TO_DATE('2018-11-03', 'YYYY-MM-DD'), 'Madison Square Garden');
INSERT INTO Team_Plays_Team VALUES ('Raptors', 'Toronto', 'Pacers', 'Indiana', TO_DATE('2019-11-03', 'YYYY-MM-DD'), 'Crypto Arena');
INSERT INTO Team_Plays_Team VALUES ('Raptors', 'Toronto', 'Pacers', 'Indiana', TO_DATE('2020-11-03', 'YYYY-MM-DD'), 'ScotiaBank Arena');
INSERT INTO Team_Plays_Team VALUES ('Raptors', 'Toronto', 'Pacers', 'Indiana', TO_DATE('2021-11-03', 'YYYY-MM-DD'), 'Paycom Center');
INSERT INTO Team_Plays_Team VALUES ('Thunder', 'Oklahoma City', 'Raptors', 'Toronto', TO_DATE('2022-11-03', 'YYYY-MM-DD'), 'Gainbridge Fieldhouse');

-- Happens_In table
INSERT INTO Happens_In VALUES (TO_DATE('2018-09-22', 'YYYY-MM-DD'), 'NBA', 'Madison Square Garden', TO_DATE('2018-11-03', 'YYYY-MM-DD'));
INSERT INTO Happens_In VALUES (TO_DATE('2019-09-22', 'YYYY-MM-DD'), 'NBA', 'Crypto Arena', TO_DATE('2019-11-03', 'YYYY-MM-DD'));
INSERT INTO Happens_In VALUES (TO_DATE('2020-09-22', 'YYYY-MM-DD'), 'NBA', 'ScotiaBank Arena', TO_DATE('2020-11-03', 'YYYY-MM-DD'));
INSERT INTO Happens_In VALUES (TO_DATE('2021-09-22', 'YYYY-MM-DD'), 'NBA', 'Paycom Center', TO_DATE('2021-11-03', 'YYYY-MM-DD'));
INSERT INTO Happens_In VALUES (TO_DATE('2022-09-22', 'YYYY-MM-DD'), 'NBA', 'Gainbridge Fieldhouse', TO_DATE('2022-11-03', 'YYYY-MM-DD'));

-- Wins_Season table
INSERT INTO Wins_Season VALUES (TO_DATE('2018-09-22', 'YYYY-MM-DD'), 'NBA', 'Raptors', 'Toronto');
INSERT INTO Wins_Season VALUES (TO_DATE('2019-09-22', 'YYYY-MM-DD'), 'NBA', 'Raptors', 'Toronto');
INSERT INTO Wins_Season VALUES (TO_DATE('2020-09-22', 'YYYY-MM-DD'), 'NBA', 'Raptors', 'Toronto');
INSERT INTO Wins_Season VALUES (TO_DATE('2021-09-22', 'YYYY-MM-DD'), 'NBA', 'Raptors', 'Toronto');
INSERT INTO Wins_Season VALUES (TO_DATE('2022-09-22', 'YYYY-MM-DD'), 'NBA', 'Raptors', 'Toronto');


COMMIT;

