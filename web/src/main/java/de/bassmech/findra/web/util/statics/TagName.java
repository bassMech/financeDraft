package de.bassmech.findra.web.util.statics;

public enum TagName {
	ACCOUNT_NEW("account.new"),
	ACCOUNT_EDIT("account.edit"),
	TAG_NEW("tag.new"),
	TAG_EDIT("tag.edit"),
	TRANSACTION_NEW("transaction.new"),
	TRANSACTION_EDIT("transaction.edit"),
	DRAFT_NEW("draft.new"),
	DRAFT_EDIT("draft.edit"),
	;
	private String value;

	private TagName(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
