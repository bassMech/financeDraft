package de.bassmech.findra.web.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.primefaces.PrimeFaces;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import de.bassmech.findra.model.entity.Client;
import de.bassmech.findra.web.auth.SessionHandler;
import de.bassmech.findra.web.handler.FacesMessageHandler;
import de.bassmech.findra.web.service.AccountService;
import de.bassmech.findra.web.service.TagService;
import de.bassmech.findra.web.util.LocalizationUtil;
import de.bassmech.findra.web.util.statics.FormIds;
import de.bassmech.findra.web.util.statics.TagName;
import de.bassmech.findra.web.view.model.AccountViewModel;
import de.bassmech.findra.web.view.model.TagDetailDialogViewModel;
import de.bassmech.findra.web.view.model.TagViewModel;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;

@Component
@SessionScoped
public class TagView implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final String DEFAULT_SWATCHES_HEX_COLORS = "#000000, #ffffff, #067bc2, #84bcda, #80e377, #ecc30b, #f37748, #d56062";
	private Logger logger = LoggerFactory.getLogger(TagView.class);
	
	@Autowired
	private TagService tagService;

	@Autowired
	private AccountService accountService;
	
	@Autowired
	private SessionHandler sessionHandler;

	private List<TagViewModel> tagList;
	private List<TagViewModel> notDeletedTagList;
	private List<TagViewModel> tagManagementList;
	
	private List<AccountViewModel> selectableAccounts;
	private Integer selectedAccountId;
	private List<TagViewModel> tagsForAccountAvailable;
	private List<TagViewModel> tagsForAccountAssigned;

	private TagDetailDialogViewModel tagDialog = new TagDetailDialogViewModel("");
	
	private boolean showDeletedTags = false;
	private Client client;

	@PostConstruct
	@DependsOn(value = { "TagService", "AccountService" })
	public void init() {
		client = sessionHandler.getLoggedInClientWithSessionCheck();
		reloadAllTags();

		selectableAccounts = accountService.getAccountList();
		if (!selectableAccounts.isEmpty()) {
			selectedAccountId = selectableAccounts.get(0).getId();
			reloadTagsForAccountAssignment();
		}
	}
	
	private void reloadAllTags() {
		tagList = tagService.getAllTags();
		notDeletedTagList = tagList.stream().filter(x -> x.getDeletedAt() == null).toList();
		populateTagManagementList();
	}
		
	private void reloadTagsForAccountAssignment() {
		tagsForAccountAvailable = new ArrayList<>();
		tagsForAccountAssigned = new ArrayList<>();

		List<TagViewModel> accountTags = tagService.getTagsForAccount(selectedAccountId, true);
		for (TagViewModel tag : notDeletedTagList) {
			if (accountTags.contains(tag)) {
				tagsForAccountAssigned.add(tag);
			} else {
				tagsForAccountAvailable.add(tag);
			}
		}
	}
	
	private void populateTagManagementList() {
		tagManagementList = new ArrayList<>(showDeletedTags ? tagList : notDeletedTagList);		
	}
		
///
/// open tag dialog
///
	
	public void onTagEdit(int tagId) {
		logger.debug("onTagEdit: " + tagId);
		TagViewModel tag = tagList.stream().filter(tagX -> tagX.getId().equals(tagId)).findFirst().orElse(null);
		tagDialog = new TagDetailDialogViewModel(LocalizationUtil.getTag(TagName.TAG_EDIT.getValue()));
		tagDialog.setId(tag.getId());
		tagDialog.setTitle(tag.getTitle());
		tagDialog.setDescription(tag.getDescription());
		tagDialog.setTextHexColor(tag.getTextHexColor());
		tagDialog.setBackgroundHexColor(tag.getBackgroundHexColor());
		tagDialog.setDeletedAt(tag.getDeletedAt());
		
		openTagDetailDialog();
	}
	
	public void onTagNew() {
		logger.debug("onTagNew");
		tagDialog = new TagDetailDialogViewModel(LocalizationUtil.getTag(TagName.TAG_NEW.getValue()));	
		
		openTagDetailDialog();
	}
	
	private void openTagDetailDialog() {
		PrimeFaces.current().ajax().update(FormIds.MAIN_FORM.getValue());
		PrimeFaces.current().executeScript("PF('tagDetailDialog').show()");
	}
	
	public void onTagToogleDeletedVisibility() {
		logger.debug("onTagToogleDeletedVisibility old: " + Boolean.toString(showDeletedTags));
		showDeletedTags = !showDeletedTags;
		populateTagManagementList();
	}
	
///
/// tag detail dialog
///

	public void onAccountTagsSave() {
		logger.debug("Saving tags for account");
		tagService.saveAccountTags(selectedAccountId, tagsForAccountAssigned);
		reloadTagsForAccountAssignment();
	}

	public void onDeleteTag() {
		logger.debug("deleteTag");
		
		tagService.changeTagDeletionState(tagDialog.getId(), true);
		reloadAllTags();
		reloadTagsForAccountAssignment();
		
		PrimeFaces.current().ajax().update(FormIds.MAIN_FORM.getValue());
	}
	
	public void onUndeleteTag() {
		logger.debug("undeleteTag");
		
		tagService.changeTagDeletionState(tagDialog.getId(), false);
		reloadAllTags();
		reloadTagsForAccountAssignment();
		
		PrimeFaces.current().ajax().update(FormIds.MAIN_FORM.getValue());
	}
	
	public boolean isTagDialogValid() {
		boolean isValid = true;
		if (tagDialog.getTitle() == null || tagDialog.getTitle().isBlank()) {
			FacesMessageHandler.addMessageFromKey(FacesMessage.SEVERITY_ERROR
					, "error.title.must.not.be.empty");
			isValid = false;
		}

		return isValid;
	}

	public void closeDialogAndSaveTag() {
		logger.debug("closeDialogAndSaveTag");
		
		if (isTagDialogValid()) {
			tagService.saveTag(client, tagDialog);
			reloadAllTags();
			reloadTagsForAccountAssignment();
			PrimeFaces.current().ajax().update(FormIds.MAIN_FORM.getValue());
		}
	}
	
///
///	tag assignment
///
	public void onAccountChanged() {
		logger.debug("onAccountChanged");
		
		reloadAllTags();
		reloadTagsForAccountAssignment();
	}
	
	public void onTagAssign(int tagId) {
		logger.debug("onTagAssign: " + tagId);
		TagViewModel vm = tagsForAccountAvailable.stream().filter(tag -> tag.getId().equals(tagId)).findFirst().orElse(null);
		tagsForAccountAvailable.remove(vm);
		tagsForAccountAssigned.add(vm);
	}
	
	public void onTagRemove(int tagId) {
		logger.debug("onTagRemove: " + tagId);
		TagViewModel vm = tagsForAccountAssigned.stream().filter(tag -> tag.getId().equals(tagId)).findFirst().orElse(null);
		tagsForAccountAssigned.remove(vm);
		tagsForAccountAvailable.add(vm);
	}
	
	public void onAllTagAssign() {
		logger.debug("onAllTagAssign");
		tagsForAccountAssigned.addAll(tagsForAccountAvailable);
		tagsForAccountAvailable.clear();
	}
	
	public void onAllTagRemove() {
		logger.debug("onAllTagRemove");
		tagsForAccountAvailable.addAll(tagsForAccountAssigned);
		tagsForAccountAssigned.clear();
	}
	
	public static String getDefaultSwatchesHexColors() {
		return DEFAULT_SWATCHES_HEX_COLORS;
	}
	
///
/// misc
///
	
	public boolean isViewRendered() {
		return selectedAccountId != null;
	}
	
///
/// getter setter
///
	
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

	public List<TagViewModel> getNotDeletedTagList() {
		return notDeletedTagList;
	}

	public List<TagViewModel> getTagManagementList() {
		return tagManagementList;
	}

	public void setTagManagementList(List<TagViewModel> tagManagementList) {
		this.tagManagementList = tagManagementList;
	}
	
	

}