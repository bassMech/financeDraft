package de.bassmech.findra.web.util.statics;

public enum TagName {
	ACCOUNT_NEW("account.new"),
	ACCOUNT_EDIT("account.edit"),
	TAG_NEW("tag.new"),
	TAG_EDIT("tag.edit"),
	;
	private String value;

	private TagName(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
