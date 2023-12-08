-- User data
INSERT INTO sec_user (email, encryptedPassword, enabled) VALUES 
('fahad.jan@sheridancollege.ca', '$2a$10$1ltibqiyyBJMJQ4hqM7f0OusP6np/IHshkYc4TjedwHnwwNChQZCy', 1),
('frank@sheridancollege.ca', '$2a$10$PrI5Gk9L.tSZiW9FXhTS8O8Mz9E97k2FZbFvGFFaSsiTUIl.TCrFu', 1);

-- User roles
INSERT INTO sec_role (roleName) VALUES ('ROLE_USER'), ('ROLE_GUEST');

-- User roles mapping
INSERT INTO user_role (userId, roleId) VALUES (1, 1), (2, 2);

-- Java Programming Questions
INSERT INTO questions (question_text, option_a, option_b, option_c, option_d, correct_option, subject) VALUES
('What is the main purpose of a constructor in Java?', 'To initialize class variables', 'To create objects', 'To define the class methods', 'To handle exceptions', 'B', 'Java'),
('Which keyword is used to define a method in Java?', 'func', 'method', 'function', 'public', 'D', 'Java'),
('What is the difference between `==` and `equals()` in Java when comparing strings?', 'They are functionally equivalent', '`==` compares references, `equals()` compares content', '`==` compares content, `equals()` compares references', 'There is no difference', 'B', 'Java'),
('What is the purpose of the `static` keyword in Java?', 'To define a constant', 'To indicate that a method or variable belongs to the class, not an instance', 'To create an instance of a class', 'To prevent inheritance', 'B', 'Java'),
('What is the Java Virtual Machine (JVM) responsible for?', 'Compiling Java code', 'Executing Java code', 'Debugging Java code', 'Packaging Java code', 'B', 'Java'),
('What is the default value of a boolean variable in Java?', 'true', 'false', 'null', '0', 'B', 'Java'),
('Which loop is used to iterate a specific number of times in Java?', 'for', 'while', 'do-while', 'foreach', 'A', 'Java');

-- Science Questions
INSERT INTO questions (question_text, option_a, option_b, option_c, option_d, correct_option, subject) VALUES
('Which gas is responsible for the green color of plants?', 'Oxygen', 'Carbon Dioxide', 'Chlorine', 'Chlorophyll', 'D', 'Science'),
('What is the chemical symbol for water?', 'H2O', 'CO2', 'O2', 'NaCl', 'A', 'Science'),
('What is the SI unit of force?', 'Newton', 'Watt', 'Joule', 'Pascal', 'A', 'Science'),
('Which element is the primary component of the Earths atmosphere?', 'Oxygen', 'Nitrogen', 'Carbon Dioxide', 'Argon', 'B', 'Science'),
('What is the process by which plants make their own food using sunlight?', 'Photosynthesis', 'Respiration', 'Transpiration', 'Digestion', 'A', 'Science'),
('What is the atomic number of carbon?', '6', '8', '12', '16', 'A', 'Science'),
('Which planet is known as the "Red Planet"?', 'Venus', 'Mars', 'Jupiter', 'Saturn', 'B', 'Science');

-- Scores for Java
INSERT INTO scores (username, score, subject) VALUES
('joe@sheridan.ca', '120', 'Java'),
('jane@sheridan.ca', '80', 'Java'),
('bob@sheridan.ca', '160', 'Java'),
('alice@sheridan.ca', '90', 'Java'),
('dave@sheridan.ca', '110', 'Java'),
('fahad.jan@sheridancollege.ca', '80', 'Java');

-- Scores for Science
INSERT INTO scores (username, score, subject) VALUES
('sara@sheridan.ca', '180', 'Science'),
('peter@sheridan.ca', '70', 'Science'),
('emily@sheridan.ca', '140', 'Science'),
('tom@sheridan.ca', '100', 'Science'),
('lisa@sheridan.ca', '120', 'Science'),
('frank@sheridancollege.ca', '70', 'Science');




