package com.qpro.repository;

import com.qpro.bo.AppUser;
import com.qpro.bo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.List;

@Repository
public class AppUserRepository {

    private RedisTemplate<String, Object> redisTemplate;
    private HashOperations hashOperations;
    private final String KEY = "appUserCache";

    @Autowired
    public AppUserRepository(RedisTemplate<String, Object> redisTemplate){
        this.redisTemplate = redisTemplate;
    }

    @PostConstruct
    private void init(){
        hashOperations = redisTemplate.opsForHash();
    }

    public boolean containsKey(String remoteAddress){
        return hashOperations.get(KEY, remoteAddress) != null;
    }

    public void save(AppUser user){
        hashOperations.put(KEY, user.getRemoteAddress(), user);
    }

    public void update(AppUser user){
        save(user);
    }

    public List<AppUser> findAll(){
        return hashOperations.values(KEY);
    }

    public AppUser findById(String remoteAddress){
        return (AppUser) hashOperations.get(KEY, remoteAddress);
    }

}
