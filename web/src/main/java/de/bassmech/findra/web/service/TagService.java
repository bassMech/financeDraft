package de.bassmech.findra.web.service;

import java.time.Instant;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.bassmech.findra.core.repository.AccountRepository;
import de.bassmech.findra.core.repository.TagRepository;
import de.bassmech.findra.model.entity.Account;
import de.bassmech.findra.model.entity.Tag;
import de.bassmech.findra.web.util.ToViewModelUtil;
import de.bassmech.findra.web.view.model.TagDetailDialogViewModel;
import de.bassmech.findra.web.view.model.TagViewModel;
import jakarta.annotation.PostConstruct;

@Component
public class TagService {
	private Logger logger = LoggerFactory.getLogger(TagService.class);

	@Autowired
	private TagRepository tagRespoRepository;

	@Autowired
	private AccountRepository accountRepository;

	private List<TagViewModel> allTags;

	private Map<Integer, List<TagViewModel>> tagsByAccountId = new HashMap<>();

	@PostConstruct
	public void init() {
		reloadTags();
	}

	private void reloadTags() {
		allTags = ToViewModelUtil.toTagViewModelList(tagRespoRepository.findAllByOrderByTitleDesc());
	}

	public List<TagViewModel> getTagsForAccount(Integer accountId) {
		if (accountId == null) {
			logger.error("Given account id was null");
		}
		List<TagViewModel> tags = tagsByAccountId.get(accountId);
		if (tags == null) {
			Account account = accountRepository.findById(accountId.longValue()).orElse(null);
			tags = ToViewModelUtil.toTagViewModelList(account.getTags());
			tagsByAccountId.put(accountId, tags);
		}

		return tags;
	}

	public void saveAccountTags(Integer accountId, List<TagViewModel> newTags) {
		if (accountId == null) {
			logger.error("Given account id was null");
		}
		Account account = accountRepository.findById(accountId.longValue()).orElse(null);

		Iterator<Tag> tagIt = account.getTags().iterator();
		while (tagIt.hasNext()) {
			Tag tag = tagIt.next();
			if (newTags.stream().noneMatch(tagX -> tagX.getId().equals(tag.getId()))) {
				tagIt.remove();
			} else {
				newTags.removeIf(tagX -> tagX.getId().equals(tag.getId()));
			}
		}

		List<Tag> dbTags = tagRespoRepository
				.findAllById(newTags.stream().map(TagViewModel::getId).collect(Collectors.toList()));

		account.getTags().addAll(dbTags);
		account = accountRepository.save(account);
		tagsByAccountId.put(accountId, ToViewModelUtil.toTagViewModelList(account.getTags()));
	}

	public void saveTag(TagDetailDialogViewModel tagDialog) {
		Tag tag = null;
		if (tagDialog.getId() == null) {
			tag = new Tag();
		} else {
			tag = tagRespoRepository.findById(tagDialog.getId()).orElse(null);
		}

		tag.setTitle(tagDialog.getTitle());
		tag.setDescription(tagDialog.getDescription());
		tag.setTextHexColor(tagDialog.getTextHexColor());
		tag.setBackgroundHexColor(tagDialog.getBackgroundHexColor());
		tag = tagRespoRepository.save(tag);

		if (tagDialog.getId() != null) {
			allTags.removeIf(tagX -> tagX.getId().equals(tagDialog.getId()));
		}

		allTags.add(ToViewModelUtil.toViewModel(tag));
		allTags.sort(Comparator.comparing(TagViewModel::getTitle));
	}
	
	public void changeTagDeletionState(Integer tagId, boolean isDeleted) {
		Tag tag = tagRespoRepository.findById(tagId).orElse(null);
		if (tag == null) {
			logger.error("Tag with id not found: " + tagId);
		}
		if (isDeleted) {
			if (tag.getDeletedAt() == null) {
				tag.setDeletedAt(Instant.now());
			} else {
				logger.error("Tag is already deleted. id: " + tagId);
			}
		} else {
			if (tag.getDeletedAt() == null) {
				logger.error("Tag is not deleted. id: " + tagId);
				
			} else {
				tag.setDeletedAt(null);
			}
		}
		tag = tagRespoRepository.save(tag);
		
		allTags.removeIf(tagX -> tagX.getId().equals(tagId));
		
		allTags.add(ToViewModelUtil.toViewModel(tag));
		allTags.sort(Comparator.comparing(TagViewModel::getTitle));
	}
	
	
	public void setTagDeleted(int tagId) {
		Tag tag = tagRespoRepository.findById(tagId).orElse(null);
		tag.setDeletedAt(Instant.now());
		tag = tagRespoRepository.save(tag);
		allTags.removeIf(tagX -> tagX.getId().equals(tagId));
		allTags.add(ToViewModelUtil.toViewModel(tag));
	}

	public List<TagViewModel> getAllTags() {
		return allTags;
	}
}
