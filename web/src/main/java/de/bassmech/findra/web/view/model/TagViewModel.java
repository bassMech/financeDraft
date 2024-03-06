package de.bassmech.findra.web.view.model;

import java.time.Instant;
import java.util.Objects;

public class TagViewModel {
	private Integer id;
	private String title;
	private String description;
	private String backgroundHexColor;
	private String textHexColor;
	private Instant deletedAt;
	
	public boolean isDeleted() {
		return deletedAt != null;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String ttile) {
		this.title = ttile;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getBackgroundHexColor() {
		return backgroundHexColor;
	}

	public void setBackgroundHexColor(String hexColor) {
		this.backgroundHexColor = hexColor;
	}

	public String getTextHexColor() {
		return textHexColor;
	}

	public void setTextHexColor(String textHexColor) {
		this.textHexColor = textHexColor;
	}
	
	public Instant getDeletedAt() {
		return deletedAt;
	}

	public void setDeletedAt(Instant deletedAt) {
		this.deletedAt = deletedAt;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TagViewModel other = (TagViewModel) obj;
		return Objects.equals(id, other.id);
	}

}
