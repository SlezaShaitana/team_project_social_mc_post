package com.social.mc_post.services.impl;

import com.social.mc_post.dto.TagDto;
import com.social.mc_post.dto.mappers.ConvertToListMapper;
import com.social.mc_post.dto.mappers.TagMapper;
import com.social.mc_post.repository.TagRepository;
import com.social.mc_post.services.TagService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;
    private final ConvertToListMapper convertToList;
    private final TagMapper tagMapper;

    @Autowired
    public TagServiceImpl(TagRepository tagRepository, ConvertToListMapper convertToList, TagMapper tagMapper) {
        this.tagRepository = tagRepository;
        this.convertToList = convertToList;
        this.tagMapper = tagMapper;
    }

    @Override
    public List<TagDto> getAllTags() {
        return convertToList.convertList(tagRepository.getAllTags(), tagMapper::mapToTagDto);
    }
}
