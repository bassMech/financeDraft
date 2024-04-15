package de.bassmech.findra.web.view.model;

import java.util.HashMap;
import java.util.Map;

import de.bassmech.findra.model.statics.Interval;
import de.bassmech.findra.web.util.LocalizationUtil;

public class TransactionDraftDetailDialogViewModel extends TransactionDetailBaseDialogViewModel {
	private static final long serialVersionUID = 1L;

	private int selectedStartMonth;
	private int selectedStartYear;
	
	private Integer selectedEndMonth;
	private Integer selectedEndYear;
	
	private int selectedInterval;
	
	private Map<Integer, String> selectableIntervals;

	public TransactionDraftDetailDialogViewModel(String dialogTitle, boolean isDraft) {
		super(dialogTitle, isDraft);
		selectableIntervals = new HashMap<>();
		for (Interval interval : Interval.values()) {
			selectableIntervals.put(interval.getDbValue(), LocalizationUtil.getTag(interval.getTagString()));
		}
		selectedInterval = Interval.EVERY_MONTH.getDbValue();
	}

	public int getSelectedStartMonth() {
		return selectedStartMonth;
	}

	public void setSelectedStartMonth(int selectedStartMonth) {
		this.selectedStartMonth = selectedStartMonth;
	}

	public int getSelectedStartYear() {
		return selectedStartYear;
	}

	public void setSelectedStartYear(int selectedStartYear) {
		this.selectedStartYear = selectedStartYear;
	}

	public Integer getSelectedEndMonth() {
		return selectedEndMonth;
	}

	public void setSelectedEndMonth(Integer selectedEndMonth) {
		this.selectedEndMonth = selectedEndMonth;
	}

	public Integer getSelectedEndYear() {
		return selectedEndYear;
	}

	public void setSelectedEndYear(Integer selectedEndYear) {
		this.selectedEndYear = selectedEndYear;
	}

	public int getSelectedInterval() {
		return selectedInterval;
	}

	public void setSelectedInterval(int selectedInterval) {
		this.selectedInterval = selectedInterval;
	}

	public Map<Integer, String> getSelectableIntervals() {
		return selectableIntervals;
	}

	public void setSelectableIntervals(Map<Integer, String> selectableIntervals) {
		this.selectableIntervals = selectableIntervals;
	}


}
