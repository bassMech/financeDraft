package de.bassmech.findra.web.view.model;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

public class DraftListDialogViewModel implements Serializable {
	private static final long serialVersionUID = 1L;

	private List<DraftViewModel> draftList = new ArrayList<>();

	public DraftListDialogViewModel(List<DraftViewModel> draftList) {
		this.draftList = draftList;
	}

	public List<DraftViewModel> getDraftList() {
		return draftList;
	}

	public void setDraftList(List<DraftViewModel> draftList) {
		this.draftList = draftList;
	}
}
