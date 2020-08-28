package com.qpro.service;

import com.qpro.bo.Story;
import com.qpro.repository.StoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DemoService {

    @Autowired
    StoryRepository storyRepository;
    private RestTemplate restTemplate = new RestTemplate();
    private String hackerNewBaseURL = "https://hacker-news.firebaseio.com/v0";

    public Story getStory(long storyId) {
        if(storyRepository.containsKey(storyId)){
            return storyRepository.findById(storyId);
        }
        String url = hackerNewBaseURL + "/item/"+storyId+".json";
        Story response = restTemplate.getForEntity(url, Story.class).getBody();
        storyRepository.save(response);
        return response;
    }

    public List<Story> bestStories(){
        String url = hackerNewBaseURL + "/beststories.json";
        ResponseEntity<Long[]> response = restTemplate.getForEntity(url, Long[].class);
        return Arrays.stream(response.getBody()).limit(10).map(storyId -> getStory(storyId)).collect(Collectors.toList());
    }

    public List<Story> pastStories(){
        return storyRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(Story::getScore).reversed())
                .collect(Collectors.toList());
    }
}
