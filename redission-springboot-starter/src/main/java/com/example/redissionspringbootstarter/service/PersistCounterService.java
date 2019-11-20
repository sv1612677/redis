package com.example.redissionspringbootstarter.service;

import com.example.redissionspringbootstarter.entity.Counter;
import com.example.redissionspringbootstarter.repository.CounterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@EnableScheduling
public class PersistCounterService {

    @Autowired
    CounterRepository counterRepository;

    @Autowired
    CounterService counterService;


    @Scheduled(fixedDelay = 1000)
    @Transactional
    public void startLoopPersistCounter() {
        Counter counterInDb = counterRepository.findCounterById(ConfigService.getCounterInDB());
        counterInDb.setNumberCount(counterService.getCount());
        counterRepository.save(counterInDb);
    }
}
