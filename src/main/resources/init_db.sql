CREATE TABLE users (
                       id BIGINT PRIMARY KEY,
                       username VARCHAR(50) NOT NULL UNIQUE,
                       email VARCHAR(100) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       name VARCHAR(100),
                       surname VARCHAR(100),
                       birthdate DATE,
                       CONSTRAINT unique_username UNIQUE (username)
);

CREATE TABLE friendships (
                             id SERIAL PRIMARY KEY,
                             user1_id BIGINT NOT NULL,
                             user2_id BIGINT NOT NULL,
                             CONSTRAINT fk_user1 FOREIGN KEY (user1_id) REFERENCES users(id) ON DELETE CASCADE,
                             CONSTRAINT fk_user2 FOREIGN KEY (user2_id) REFERENCES users(id) ON DELETE CASCADE,
                             CONSTRAINT unique_friendship UNIQUE (user1_id, user2_id)
);
CREATE TABLE friend_requests (
                                 id SERIAL PRIMARY KEY,
                                 request_from BIGINT NOT NULL,
                                 request_to BIGINT NOT NULL,
                                 status VARCHAR(20) DEFAULT 'PENDING',
                                 CONSTRAINT fk_request_from FOREIGN KEY (request_from) REFERENCES users(id) ON DELETE CASCADE,
                                 CONSTRAINT fk_request_to FOREIGN KEY (request_to) REFERENCES users(id) ON DELETE CASCADE
);
CREATE TABLE notifications (
                               id SERIAL PRIMARY KEY,
                               receiver_id BIGINT NOT NULL,
                               content TEXT NOT NULL,
                               generated_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                               seen_on TIMESTAMP NULL DEFAULT NULL,
                               CONSTRAINT fk_receiver FOREIGN KEY (receiver_id) REFERENCES users(id) ON DELETE CASCADE
);

select * from friendships