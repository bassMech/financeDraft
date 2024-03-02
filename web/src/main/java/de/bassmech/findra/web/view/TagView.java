package de.bassmech.findra.web.view;

import java.io.Serializable;
import java.time.Year;
import java.util.ArrayList;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.primefaces.PrimeFaces;
import org.primefaces.model.DualListModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import de.bassmech.findra.core.repository.TagRepository;
import de.bassmech.findra.web.service.AccountService;
import de.bassmech.findra.web.service.SettingService;
import de.bassmech.findra.web.service.TagService;
import de.bassmech.findra.web.util.ToViewModelUtil;
import de.bassmech.findra.web.util.statics.FormIds;
import de.bassmech.findra.web.view.model.AccountViewModel;
import de.bassmech.findra.web.view.model.TagDetailDialogViewModel;
import de.bassmech.findra.web.view.model.TagViewModel;
import jakarta.annotation.PostConstruct;
import jakarta.faces.bean.SessionScoped;
import jakarta.faces.view.ViewScoped;

@Component
@SessionScoped
public class TagView implements Serializable {

	private static final long serialVersionUID = 1L;
	private Logger logger = LoggerFactory.getLogger(TagView.class);

	@Autowired
	private TagService tagService;

	@Autowired
	private AccountService accountService;

	private List<TagViewModel> tagList;
	private List<AccountViewModel> selectableAccounts;
	private Integer selectedAccountId;
	private List<TagViewModel> tagsForAccountAvailable;
	private List<TagViewModel> tagsForAccountAssigned;

	private TagDetailDialogViewModel tagDialog;

	@PostConstruct
	@DependsOn(value = { "TagService", "AccountService" })
	public void init() {
		tagList = tagService.getAllTags();

		selectableAccounts = accountService.getAccountList();
		if (!selectableAccounts.isEmpty()) {
			selectedAccountId = selectableAccounts.get(0).getId();
		}

		reloadTagsForAccount();
	}

	public void onAccountTagsSave() {
		logger.debug("Saving tags for account");
		tagService.saveAccountTags(selectedAccountId, tagsForAccountAssigned);
		reloadTagsForAccount();
	}

	public void onTagEdit(int tagId) {
		logger.debug("onTagEdit: " + tagId);
		TagViewModel tag = tagList.stream().filter(tagX -> tagX.getId().equals(tagId)).findFirst().orElse(null);
		tagDialog = new TagDetailDialogViewModel();
		tagDialog.setId(tag.getId());
		tagDialog.setTitle(tag.getTitle());
		tagDialog.setDescription(tag.getDescription());
		tagDialog.setTextHexColor(tag.getTextHexColor());
		tagDialog.setBackgroundHexColor(tag.getBackgroundHexColor());

		PrimeFaces.current().ajax().update(FormIds.MAIN_FORM.getValue());
		PrimeFaces.current().executeScript("PF('tagDetailDialog').show()");
	}

	private void reloadTagsForAccount() {
		tagsForAccountAvailable = new ArrayList<>();
		tagsForAccountAssigned = new ArrayList<>();

		List<TagViewModel> accountTags = tagService.getTagsForAccount(selectedAccountId);
		for (TagViewModel tag : tagList) {
			if (accountTags.contains(tag)) {
				tagsForAccountAssigned.add(tag);
			} else {
				tagsForAccountAvailable.add(tag);
			}
		}
	}

	public void onAccountChanged() {
		logger.debug("STUB onAccountChanged");
	}

	public void deleteTag() {
		logger.debug("STUB deleteTag");
	}

	public void closeDialogAndSaveTag() {
		logger.debug("STUB closeDialogAndSaveTag");
	}
	
	public void onTagAssign(int tagId) {
		logger.debug("STUB onTagAssign");
		TagViewModel vm = tagsForAccountAvailable.stream().filter(tag -> tag.getId().equals(tagId)).findFirst().orElse(null);
		tagsForAccountAvailable.remove(vm);
		tagsForAccountAssigned.add(vm);
	}
	
	public void onTagRemove(int tagId) {
		logger.debug("STUB onTagRemove");
		TagViewModel vm = tagsForAccountAssigned.stream().filter(tag -> tag.getId().equals(tagId)).findFirst().orElse(null);
		tagsForAccountAssigned.remove(vm);
		tagsForAccountAvailable.add(vm);
	}
	
	public void onAllTagAssign() {
		logger.debug("STUB onAllTagAssign");
		tagsForAccountAssigned.addAll(tagsForAccountAvailable);
		tagsForAccountAvailable.clear();
	}
	
	public void onAllTagRemove() {
		logger.debug("STUB onAllTagRemove");
		tagsForAccountAvailable.addAll(tagsForAccountAssigned);
		tagsForAccountAssigned.clear();
	}

	public List<TagViewModel> getTagList() {
		return tagList;
	}

	public List<AccountViewModel> getSelectableAccounts() {
		return selectableAccounts;
	}

	public Integer getSelectedAccountId() {
		return selectedAccountId;
	}

	public void setSelectedAccountId(Integer selectedAccountId) {
		this.selectedAccountId = selectedAccountId;
	}

	public List<TagViewModel> getTagsForAccountAvailable() {
		return tagsForAccountAvailable;
	}

	public void setTagsForAccountAvailable(List<TagViewModel> tagsForAccountAvailable) {
		this.tagsForAccountAvailable = tagsForAccountAvailable;
	}

	public List<TagViewModel> getTagsForAccountAssigned() {
		return tagsForAccountAssigned;
	}

	public void setTagsForAccountAssigned(List<TagViewModel> tagsForAccountAssigned) {
		this.tagsForAccountAssigned = tagsForAccountAssigned;
	}

	public TagDetailDialogViewModel getTagDialog() {
		return tagDialog;
	}

	public void setTagDialog(TagDetailDialogViewModel tagDialog) {
		this.tagDialog = tagDialog;
	}

}