CREATE TABLE transaction_tag(
    id INTEGER PRIMARY KEY,
	transaction_id INTEGER NOT NULL,
	tag_id INTEGER NOT NULL,
    UNIQUE(transaction_id, tag_id),
    FOREIGN KEY (transaction_id)
       REFERENCES account_transaction(id),
   	FOREIGN KEY (tag_id)
   	REFERENCES tag(id)
);