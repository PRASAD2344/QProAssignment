package com.qpro.repository;

import com.qpro.bo.Story;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.List;

@Repository
public class StoryRepository {

    private RedisTemplate<String, Object> redisTemplate;
    private HashOperations hashOperations;
    private final String KEY = "storyCache";

    @Autowired
    public StoryRepository(RedisTemplate<String, Object> redisTemplate){
        this.redisTemplate = redisTemplate;
    }

    @PostConstruct
    private void init(){
        hashOperations = redisTemplate.opsForHash();
    }

    public boolean containsKey(long storyId){
        return hashOperations.get(KEY, storyId) != null;
    }

    public void save(Story story){
        hashOperations.put(KEY, story.getId(), story);
    }

    public void update(Story story){
        save(story);
    }

    public List<Story> findAll(){
        return hashOperations.values(KEY);
    }

    public Story findById(long storyId){
        return (Story) hashOperations.get(KEY, storyId);
    }

}
