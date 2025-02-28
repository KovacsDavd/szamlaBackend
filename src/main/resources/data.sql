-- Jogosultság
INSERT INTO roles (name, description)
VALUES ('ADMIN', 'Adminisztrátor szerepkör'),
       ('ACCOUNTANT', 'Könyvelő szerepkör'),
       ('USER', 'Felhasználói szerepkör')
ON CONFLICT (name) DO NOTHING;

-- Felhasználók, jelszó: password
INSERT INTO users (name, username, password, last_login_date)
VALUES
    ('Admin User', 'admin', '$2a$10$CrN94PPLoxl.6x0HrydSR.WQqRsi8//NtiLb1v9unrymSAwqalaby', NOW()),
    ('Accountant User', 'accountant', '$2a$10$CrN94PPLoxl.6x0HrydSR.WQqRsi8//NtiLb1v9unrymSAwqalaby', NOW()),
    ('Regular User', 'user', '$2a$10$CrN94PPLoxl.6x0HrydSR.WQqRsi8//NtiLb1v9unrymSAwqalaby', NOW())
ON CONFLICT (username) DO NOTHING;

-- Felhasználók szerepkörei
INSERT INTO user_roles (user_id, role_id)
VALUES (1, 1),
       (2, 2),
       (3, 3);

-- Számlák
INSERT INTO invoices (customer_name, issue_date, deadline_date, item_name, comment, price)
VALUES ('John Doe', '2023-10-01', '2023-10-15', 'Web Development', 'Frontend and Backend development', 1500.00),
       ('Jane Doe', '2023-10-05', '2023-10-20', 'SEO Optimization', 'Search engine optimization', 800.00);