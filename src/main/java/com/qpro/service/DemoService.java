package com.qpro.service;

import com.qpro.bo.Story;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DemoService {

    private RestTemplate restTemplate = new RestTemplate();

    private String hackerNewBaseURL = "https://hacker-news.firebaseio.com/v0";

    @Cacheable(value="storyCache",key="#storyId")
    public Story getStory(long storyId) {
        String url = hackerNewBaseURL + "/item/"+storyId+".json";
        ResponseEntity<Story> response = restTemplate.getForEntity(url, Story.class);
        return response.getBody();
    }

    public List<Story> bestStories(){
        String url = hackerNewBaseURL + "/beststories.json";
        ResponseEntity<Long[]> response = restTemplate.getForEntity(url, Long[].class);
        return Arrays.stream(response.getBody()).limit(10).map(storyId -> getStory(storyId)).collect(Collectors.toList());
    }

}
