package com.qpro.repository;

import com.qpro.bo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.List;

@Repository
public class UserRepository {

    private RedisTemplate<String, Object> redisTemplate;
    private HashOperations hashOperations;
    private final String KEY = "userCache";

    @Autowired
    public UserRepository(RedisTemplate<String, Object> redisTemplate){
        this.redisTemplate = redisTemplate;
    }

    @PostConstruct
    private void init(){
        hashOperations = redisTemplate.opsForHash();
    }

    public boolean containsKey(String userId){
        return hashOperations.get(KEY, userId) != null;
    }

    public void save(User user){
        hashOperations.put(KEY, user.getId(), user);
    }

    public void update(User user){
        save(user);
    }

    public List<User> findAll(){
        return hashOperations.values(KEY);
    }

    public User findById(String userId){
        return (User) hashOperations.get(KEY, userId);
    }

}
