\c demodb;

DROP TABLE IF EXISTS transactions;

CREATE TABLE IF NOT EXISTS transactions (
  id BIGSERIAL,
  amount NUMERIC(22,2),
  card_number VARCHAR(50) NOT NULL,
  date_time VARCHAR(50) NOT NULL,
  holder VARCHAR(50) NOT NULL,
  installments smallint,
  card_type VARCHAR(50) NOT NULL,
  email VARCHAR(50),
  status VARCHAR(50),
  PRIMARY KEY(id)
);