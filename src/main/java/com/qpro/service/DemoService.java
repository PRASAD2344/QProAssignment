package com.qpro.service;

import com.qpro.bo.AppUser;
import com.qpro.bo.Item;
import com.qpro.bo.User;
import com.qpro.repository.AppUserRepository;
import com.qpro.repository.ItemRepository;
import com.qpro.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DemoService {

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AppUserRepository appUserRepository;

    private RestTemplate restTemplate = new RestTemplate();
    private String hackerNewBaseURL = "https://hacker-news.firebaseio.com/v0";

    private Item getItem(long itemId) {
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

    private AppUser getAppUser(String remoteAddress) {
        if(appUserRepository.containsKey(remoteAddress)){
            return appUserRepository.findById(remoteAddress);
        }
        AppUser appUser = new AppUser(remoteAddress,new HashSet<Long>());
        appUserRepository.save(appUser);
        return appUser;
    }

    @Cacheable(value="bestStoriesCache",key="#remoteAddress")
    public List<Item> bestStories(String remoteAddress){
        AppUser appUser = getAppUser(remoteAddress);
        String url = hackerNewBaseURL + "/beststories.json";
        ResponseEntity<Long[]> response = restTemplate.getForEntity(url, Long[].class);
        List<Item> bestStories = Arrays.stream(response.getBody())
                .limit(10)
                .map(storyId -> {
                    appUser.getTopItemsServed().add(storyId);
                    return storyId;
                })
                .map(storyId -> getItem(storyId))
                .collect(Collectors.toList());
        appUserRepository.update(appUser);
        return bestStories;
    }

    public List<Item> pastStories(String remoteAddress){
        AppUser appUser = getAppUser(remoteAddress);
        return appUser.getTopItemsServed()
                .stream()
                .map(storyId -> getItem(storyId))
                .filter(Item::isStory)
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
