package de.bassmech.findra.model.entity;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import de.bassmech.findra.model.converter.IntervalConverter;
import de.bassmech.findra.model.converter.YearMonthDbConverter;
import de.bassmech.findra.model.statics.Interval;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "account_transaction_draft")
public class AccountTransactionDraft extends TransactionBase {

	@ManyToOne(targetEntity = Account.class)
	@JoinColumn(referencedColumnName = "id", name = "account_id")
	private Account account;

	@ManyToOne(targetEntity = AccountTransactionDraft.class)
	@JoinColumn(referencedColumnName = "id", name = "predecessor_id")
	private AccountTransactionDraft predecessor;

	@Column(name = "starts_at", columnDefinition = "INTEGER", nullable = false)
	@Convert(converter = YearMonthDbConverter.class)
	private YearMonth startsAt;

	@Column(name = "ends_at", columnDefinition = "INTEGER")
	@Convert(converter = YearMonthDbConverter.class)
	private YearMonth endsAt;

	@Column(name = "interval", columnDefinition = "INTEGER", nullable = false)
	@Convert(converter = IntervalConverter.class)
	private Interval interval;

	@ManyToMany(cascade = { CascadeType.ALL })
	@JoinTable(name = "transaction_tag", joinColumns = { @JoinColumn(name = "transaction_id") }, inverseJoinColumns = {
			@JoinColumn(name = "tag_id") })
	private List<Tag> tags = new ArrayList<>();

	public List<Tag> getTags() {
		return tags;
	}

	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public AccountTransactionDraft getPredecessor() {
		return predecessor;
	}

	public void setPredecessor(AccountTransactionDraft predecessor) {
		this.predecessor = predecessor;
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
