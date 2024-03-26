package de.bassmech.findra.web.view.model;

import java.time.YearMonth;

import de.bassmech.findra.model.statics.Interval;
import de.bassmech.findra.web.util.LocalizationUtil;

public class DraftViewModel extends TransactionBaseViewModel {
	private Integer accountId;
	private Integer predecessorId;
	private YearMonth startsAt;
	private YearMonth endsAt;
	private Interval interval;

	public DraftViewModel() {
		super();
	}

	@Override
	public boolean isDraft() {
		return true;
	}
	
	public String getIntervalForDisplay() {
		return LocalizationUtil.getTag(interval.getTagString());
	}

	public Integer getAccountId() {
		return accountId;
	}

	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}

	public Integer getPredecessorId() {
		return predecessorId;
	}

	public void setPredecessorId(Integer predecessorId) {
		this.predecessorId = predecessorId;
	}

	public YearMonth getStartsAt() {
		return startsAt;
	}

	public void setStartsAt(YearMonth startsAt) {
		this.startsAt = startsAt;
	}

	public YearMonth getEndsAt() {
		return endsAt;
	}

	public void setEndsAt(YearMonth endsAt) {
		this.endsAt = endsAt;
	}

	public Interval getInterval() {
		return interval;
	}

	public void setInterval(Interval interval) {
		this.interval = interval;
	}

}
