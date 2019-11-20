package com.example.redissionspringbootstarter.service;

import com.example.redissionspringbootstarter.entity.Counter;
import com.example.redissionspringbootstarter.repository.CounterRepository;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class CounterServiceImpl implements CounterService {

    @Autowired
    RedissonClient redissonClient;

    @Autowired
    CounterRepository counterRepository;

    private RAtomicLong counter;

    @PostConstruct
    public void configCounterInRedis() {
        counter = redissonClient.getAtomicLong(ConfigService.getCounterKey());
        counter.set(ConfigService.getStartCountValue());
    }

    @Override
    public long count() {
        return counter.incrementAndGet();
    }

    @Override
    public long getCount() {
        return counter.get();
    }
}
