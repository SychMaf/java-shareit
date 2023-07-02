        DROP TABLE IF EXISTS users CASCADE;
CREATE TABLE IF NOT EXISTS users (
  user_id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  name VARCHAR(255) NOT NULL,
  email VARCHAR(512) NOT NULL,
  CONSTRAINT pk_user PRIMARY KEY (user_id),
  CONSTRAINT UQ_USER_EMAIL UNIQUE (email)
);

        DROP TABLE IF EXISTS request CASCADE;
CREATE TABLE IF NOT EXISTS request (
  request_id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  description VARCHAR(512) NOT NULL,
  requester BIGINT NOT NULL,
  created_time TIMESTAMP WITHOUT TIME ZONE,
  CONSTRAINT pk_requester PRIMARY KEY (request_id),
  CONSTRAINT fk__requester__request FOREIGN KEY (requester) REFERENCES users (user_id)
);

        DROP TABLE IF EXISTS items CASCADE;
CREATE TABLE IF NOT EXISTS items (
  item_id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  name VARCHAR(255) NOT NULL,
  description VARCHAR(512) NOT NULL,
  available VARCHAR NOT NULL,
  owner BIGINT NOT NULL,
  request BIGINT,
  CONSTRAINT pk_item PRIMARY KEY (item_id),
  CONSTRAINT fk__items__owner FOREIGN KEY (owner) REFERENCES users (user_id),
  CONSTRAINT fk__items__request FOREIGN KEY (request) REFERENCES request (request_id)
);

        DROP TABLE IF EXISTS booking CASCADE;
CREATE TABLE IF NOT EXISTS booking (
  booking_id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  start_time TIMESTAMP WITHOUT TIME ZONE,
  end_time TIMESTAMP WITHOUT TIME ZONE,
  item BIGINT NOT NULL,
  booker BIGINT NOT NULL,
  status VARCHAR NOT NULL,
  CONSTRAINT pk_booking PRIMARY KEY (booking_id),
  CONSTRAINT fk__booking__item FOREIGN KEY (item) REFERENCES items (item_id),
  CONSTRAINT fk__booking__booker FOREIGN KEY (booker) REFERENCES users (user_id)
);

