package com.qpro.service;

import com.qpro.bo.Item;
import com.qpro.bo.User;
import com.qpro.repository.ItemRepository;
import com.qpro.repository.UserRepository;
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
    ItemRepository itemRepository;

    @Autowired
    UserRepository userRepository;

    private RestTemplate restTemplate = new RestTemplate();
    private String hackerNewBaseURL = "https://hacker-news.firebaseio.com/v0";

    private Item getTopItem(long itemId){
        Item response = getItem(itemId);
        response.setTopItemRetrieval(true);
        itemRepository.update(response);
        return response;
    }

    public Item getItem(long itemId) {
        if(itemRepository.containsKey(itemId)){
            return itemRepository.findById(itemId);
        }
        String url = hackerNewBaseURL + "/item/"+itemId+".json";
        Item response = restTemplate.getForEntity(url, Item.class).getBody();
        itemRepository.save(response);
        return response;
    }

    public User getUser(String userId) {
        if(userRepository.containsKey(userId)){
            return userRepository.findById(userId);
        }
        String url = hackerNewBaseURL + "/user/"+userId+".json";
        User response = restTemplate.getForEntity(url, User.class).getBody();
        userRepository.save(response);
        return response;
    }

    public List<Item> bestStories(){
        String url = hackerNewBaseURL + "/beststories.json";
        ResponseEntity<Long[]> response = restTemplate.getForEntity(url, Long[].class);
        return Arrays.stream(response.getBody())
                .limit(10)
                .map(storyId -> getTopItem(storyId))
                .collect(Collectors.toList());
    }

    public List<Item> pastStories(){
        return itemRepository.findAll()
                .stream()
                .filter(Item::isStory)
                .filter(Item::isTopItemRetrieval)
                .sorted(Comparator.comparingLong(Item::getScore).reversed())
                .collect(Collectors.toList());
    }

    public List<Item> comments(long storyId){
        Item item = getItem(storyId);
        return item.getKids()
                .stream()
                .limit(10)
                .map(this::getItem)
                .sorted(Comparator.comparingInt(Item::numberOfKids).reversed())
                .collect(Collectors.toList());
    }
}
