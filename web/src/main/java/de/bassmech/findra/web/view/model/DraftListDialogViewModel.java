package de.bassmech.findra.web.view.model;

import java.util.ArrayList;
import java.util.List;

public class DraftListDialogViewModel {

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
