package de.bassmech.findra.web.service;

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
			if(newTags.stream().noneMatch(tagX -> tagX.getId().equals(tag.getId()))) {
				tagIt.remove();
			} else {
				newTags.removeIf(tagX -> tagX.getId().equals(tag.getId()));
			}
		}
		
		List<Tag> dbTags = tagRespoRepository.findAllById(newTags.stream().map(TagViewModel::getId).collect(Collectors.toList()));
		
		account.getTags().addAll(dbTags);
		account = accountRepository.save(account);
		tagsByAccountId.put(accountId, ToViewModelUtil.toTagViewModelList(account.getTags()));
	}

	public List<TagViewModel> getAllTags() {
		return allTags;
	}

}
