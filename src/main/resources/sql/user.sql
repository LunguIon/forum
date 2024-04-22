INSERT INTO users (username, email, password_hash, image_url_profile, create_date, id_role, banned)
VALUES
('john_doe', 'john@example.com', 'hashedpassword123', 'https://example.com/profile.jpg', '2023-04-01', 1, FALSE),
('jane_doe', 'jane@example.com', 'hashedpassword321', 'https://example.com/jane.jpg', '2023-04-01', 2, FALSE),
('alice_smith', 'alice@example.com', 'securehash456', 'https://example.com/alice.jpg', '2023-04-01', 2, FALSE),
('bob_jones', 'bob@example.com', 'mypassword789', 'https://example.com/bob.jpg', '2023-04-01', 1, TRUE),
('charlie_brown', 'charlie@example.com', 'pass1234', 'https://example.com/charlie.jpg', '2023-04-01', 3, FALSE),
('david_lee', 'david@example.com', 'password4321', 'https://example.com/david.jpg', '2023-04-01', 3, TRUE),
('ella_white', 'ella@example.com', 'hashpass567', 'https://example.com/ella.jpg', '2023-04-01', 2, FALSE),
('frank_black', 'frank@example.com', 'hashcode123', 'https://example.com/frank.jpg', '2023-04-01', 1, FALSE),
('grace_hill', 'grace@example.com', 'myhash890', 'https://example.com/grace.jpg', '2023-04-01', 3, FALSE);
