DROP TABLE IF EXISTS tbl_recur_trans;
DROP TABLE IF EXISTS tbl_transaction;
DROP TABLE IF EXISTS tbl_account;

CREATE TABLE tbl_account
(
  id   uuid
    CONSTRAINT unique_account_id PRIMARY KEY,
  name TEXT
    CONSTRAINT unique_account_name UNIQUE NOT NULL,
  currency TEXT NOT NULL,
  recon_date DATE NOT NULL,
  balance decimal NOT NULL DEFAULT 0
);

CREATE TABLE tbl_transaction
(
  id      uuid
    CONSTRAINT unique_transaction_id PRIMARY KEY,
  transaction_date DATE NOT NULL,
  description TEXT NOT NULL,
  amount numeric NOT NULL DEFAULT 0,
  from_acc_id uuid REFERENCES tbl_account (id) NOT NULL,
  to_acc_id uuid REFERENCES tbl_account (id) NOT NULL
);

CREATE TABLE tbl_recur_trans
(
  id      uuid
    CONSTRAINT unique_rec_tran_id PRIMARY KEY,
  from_date DATE NOT NULL,
  to_date DATE NOT NULL,
  freq_type TEXT NOT NULL,
  freq_count TEXT NOT NULL,
  amount numeric NOT NULL DEFAULT 0,
  from_acc_id uuid REFERENCES tbl_account (id) NOT NULL,
  to_acc_id uuid REFERENCES tbl_account (id) NOT NULL
);