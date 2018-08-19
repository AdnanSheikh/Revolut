CREATE TABLE CUSTOMER (
   customer_id LONG PRIMARY KEY AUTO_INCREMENT,
   first_name VARCHAR(20) NOT NULL,
   last_name VARCHAR(20) NOT NULL,
   personal_identity_no VARCHAR(10) NOT NULL
);

CREATE UNIQUE INDEX uq_customer ON CUSTOMER(personal_identity_no);


CREATE TABLE CURRENCY (
   code VARCHAR(3) PRIMARY KEY,
   name VARCHAR(64) NOT NULL,
   symbol CHAR(3) DEFAULT NULL
);

CREATE TABLE ACCOUNT (
     account_id LONG PRIMARY KEY AUTO_INCREMENT,
     customer_id LONG NOT NULL,
     currency_code VARCHAR(4) NOT NULL,
     amount DECIMAL(10, 2) NOT NULL,
     FOREIGN KEY (customer_id) REFERENCES CUSTOMER(customer_id),
     FOREIGN KEY (currency_code) REFERENCES CURRENCY(code)
);

CREATE UNIQUE INDEX uq_account ON ACCOUNT(customer_id, currency_code);

CREATE TABLE TRANSACTION (
   transaction_id LONG PRIMARY KEY AUTO_INCREMENT,
   from_account_id LONG NOT NULL,
   to_account_id LONG NOT NULL,
   transaction_date timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
   amount DECIMAL(10, 2) NOT NULL,
   rate DECIMAL(10,2) NOT NULL,
   FOREIGN KEY (from_account_id) REFERENCES ACCOUNT(account_id),
   FOREIGN KEY (to_account_id) REFERENCES ACCOUNT(account_id)
);

CREATE TABLE EXCHANGE_RATE (
  code_from VARCHAR(3) NOT NULL,
  code_to VARCHAR(3) NOT NULL,
  rate DECIMAL(10,2) NOT NULL,
  created timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (code_from, code_to),
  FOREIGN KEY (code_from) REFERENCES CURRENCY(code) ON DELETE CASCADE,
  FOREIGN KEY (code_to) REFERENCES CURRENCY(code) ON DELETE CASCADE
);

INSERT INTO CURRENCY (code, name, symbol)
VALUES
  ('GBP','British pound','£'),
  ('EUR','Euro','€'),
  ('CHF','Swiss franc','CHF'),
  ('USD','US dollar','$'),
  ('AUD','Australian dollar','AU$'),
  ('CAD','Canadian dollar','C$'),
  ('CNY','Chinese renminbi yuan','¥'),
  ('PKR','Pakistani rupee','Rs');

INSERT INTO EXCHANGE_RATE (code_from, code_to, rate)
VALUES
  ('GBP','EUR',1.11),
  ('GBP','CHF',1.27),
  ('GBP','USD',1.28),
  ('GBP','AUD',1.74),
  ('GBP','CAD',1.67),
  ('GBP','CNY',8.76),
  ('EUR','CHF',1.14),
  ('EUR','USD',1.14),
  ('EUR','AUD',1.56),
  ('EUR','CAD',1.50),
  ('EUR','CNY',7.87),
  ('CHF','USD',1.00),
  ('CHF','AUD',1.37),
  ('CHF','CAD',1.31),
  ('CHF','CNY',6.91),
  ('USD','AUD',1.37),
  ('USD','CAD',1.31),
  ('USD','CNY',6.88),
  ('AUD','CAD',0.96),
  ('AUD','CNY',5.03),
  ('CAD','CNY',5.27);