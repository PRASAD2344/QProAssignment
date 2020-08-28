package com.qpro.repository;

import com.qpro.bo.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.List;

@Repository
public class ItemRepository {

    private RedisTemplate<String, Object> redisTemplate;
    private HashOperations hashOperations;
    private final String KEY = "itemCache";

    @Autowired
    public ItemRepository(RedisTemplate<String, Object> redisTemplate){
        this.redisTemplate = redisTemplate;
    }

    @PostConstruct
    private void init(){
        hashOperations = redisTemplate.opsForHash();
    }

    public boolean containsKey(long itemId){
        return hashOperations.get(KEY, itemId) != null;
    }

    public void save(Item item){
        hashOperations.put(KEY, item.getId(), item);
    }

    public void update(Item item){
        save(item);
    }

    public List<Item> findAll(){
        return hashOperations.values(KEY);
    }

    public Item findById(long itemId){
        return (Item) hashOperations.get(KEY, itemId);
    }

}
