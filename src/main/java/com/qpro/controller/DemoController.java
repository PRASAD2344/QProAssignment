package com.qpro.controller;

import com.qpro.bo.Item;
import com.qpro.dto.StoryDTO;
import com.qpro.service.DemoService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
public class DemoController {

    @Autowired
    DemoService demoService;

    @Autowired
    ModelMapper modelMapper;

    @GetMapping("/best-stories")
    @Cacheable(value="bestStoriesCache")
    public List<StoryDTO> bestStories() {
        return demoService.bestStories()
                .stream()
                .map(item -> modelMapper.map(item, StoryDTO.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/past-stories")
    public List<StoryDTO> pastStories() {
        return demoService.pastStories()
                .stream()
                .map(item -> modelMapper.map(item, StoryDTO.class))
                .collect(Collectors.toList());
    }

    /*@GetMapping("/comments/{storyId}")
    public List<Comment> comments(long storyId) {
        return demoService.comments(storyId);
    }*/
}
