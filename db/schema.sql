DROP TABLE IF EXISTS tbl_account;
DROP TABLE IF EXISTS tbl_transaction;

CREATE TABLE tbl_account
(
  id   uuid
    CONSTRAINT unique_account_id PRIMARY KEY,
  name TEXT
    CONSTRAINT unique_account_name UNIQUE NOT NULL,
  currency TEXT NOT NULL,
  reconciliation_date DATE NOT NULL DEFAULT CURRENT_DATE,
  balance numeric NOT NULL DEFAULT 0
);

CREATE TABLE tbl_transaction
(
  id      uuid
    CONSTRAINT unique_transaction_id PRIMARY KEY,
  transaction_date DATE NOT NULL DEFAULT CURRENT_DATE,
  description TEXT NOT NULL,
  amount numeric NOT NULL DEFAULT 0,
  from_account_id uuid REFERENCES tbl_account (id) NOT NULL,
  to_account_id uuid REFERENCES tbl_account (id) NOT NULL
);