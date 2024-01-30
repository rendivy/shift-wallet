package com.example.shiftwallet.dao.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class RedisRepository {

    private final RedisTemplate<String, String> redisTemplate;


    public void save(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
        redisTemplate.expire(key, 1, TimeUnit.HOURS);
    }

    public Boolean tokenExists(String key){
        return redisTemplate.hasKey(key);
    }


}
