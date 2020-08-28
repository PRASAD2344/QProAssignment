package com.qpro.controller;

import com.qpro.bo.Story;
import com.qpro.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DemoController {

    @Autowired
    DemoService demoService;

    @GetMapping("/best-stories")
    @Cacheable(value="bestStoriesCache")
    public List<Story> bestStories() {
        return demoService.bestStories();
    }
}
