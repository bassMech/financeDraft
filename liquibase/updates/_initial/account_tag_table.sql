CREATE TABLE account_tag(
    id INTEGER PRIMARY KEY,
	account_id INTEGER NOT NULL,
	tag_id INTEGER NOT NULL,
    UNIQUE(account_id, tag_id),
    FOREIGN KEY (account_id)
       REFERENCES account(id),
   	FOREIGN KEY (tag_id)
   	REFERENCES tag(id)
);