
CREATE TABLE project (
    projectid BIGINT AUTO_INCREMENT PRIMARY KEY,
    project_name VARCHAR(100),
    description TEXT,
    launch_date DATE DEFAULT CURRENT_DATE,
    end_date DATE DEFAULT NULL
);

CREATE TABLE worker (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    email VARCHAR(100) UNIQUE,
    password VARCHAR(100),
    role VARCHAR(50),
    managerid BIGINT default 0,
    FOREIGN KEY (manager_id) REFERENCES worker(id)
);

CREATE TABLE daily_log (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    activity VARCHAR(255),
    description TEXT,
    date DATE DEFAULT CURRENT_DATE,
    worker_id BIGINT,
    projectid BIGINT,
    FOREIGN KEY (worker_id) REFERENCES worker(id),
    FOREIGN KEY (projectid) REFERENCES project(projectid)
);

-- Insert Project
INSERT INTO project (project_name, description, launch_date, end_date)
VALUES ('AI Chatbot', 'Develop an AI-powered chatbot system', '2024-07-01', NULL);

-- Insert Worker
INSERT INTO worker (name, email, password, role, managerid)
VALUES ('Manish Verma', 'manish@example.com', 'secure123', 'user', 1);

-- Insert Daily Log
INSERT INTO daily_log (activity, description, date, worker_id, projectid)
VALUES ('Coding', 'Worked on login and registration module', '2024-07-17', 1, 1);
