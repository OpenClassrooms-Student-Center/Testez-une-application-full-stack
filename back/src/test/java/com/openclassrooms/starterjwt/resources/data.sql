-- Create the tables
CREATE TABLE TEACHERS (
  id INT PRIMARY KEY IDENTITY,
  last_name VARCHAR(40),
  first_name VARCHAR(40),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE SESSIONS (
  id INT PRIMARY KEY IDENTITY,
  name VARCHAR(50),
  description VARCHAR(2000),
  date TIMESTAMP,
  teacher_id INT,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE USERS (
  id INT PRIMARY KEY IDENTITY,
  last_name VARCHAR(40),
  first_name VARCHAR(40),
  admin BOOLEAN NOT NULL DEFAULT false,
  email VARCHAR(255),
  password VARCHAR(255),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE PARTICIPATE (
  user_id INT, 
  session_id INT
);

-- Add foreign keys to the tables
ALTER TABLE SESSIONS ADD FOREIGN KEY (teacher_id) REFERENCES TEACHERS (id);
ALTER TABLE PARTICIPATE ADD FOREIGN KEY (user_id) REFERENCES USERS (id);
ALTER TABLE PARTICIPATE ADD FOREIGN KEY (session_id) REFERENCES SESSIONS (id);

-- Insert values into the tables
INSERT INTO TEACHERS (first_name, last_name)
VALUES ('Margot', 'DELAHAYE'),
       ('Hélène', 'THIERCELIN');

INSERT INTO USERS (first_name, last_name, admin, email, password)
VALUES ('Admin', 'Admin', true, 'yoga@studio.com', '$2a$10$.Hsa/ZjUVaHqi0tp9xieMeewrnZxrZ5pQRzddUXE/WjDu2ZThe6Iq'),
('User', 'User', false, 'user@user.com', '$2a$10$.Hsa/ZjUVaHqi0tp9xieMeewrnZxrZ5pQRzddUXE/WjDu2ZThe6Iq');
