package com.example.redissionspringbootstarter.controller;

import com.example.redissionspringbootstarter.service.CounterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CounterController {

    @Autowired
    CounterService counterCorrectService;

    @GetMapping(path = "/ping")
    public Long pingApi()
    {
        return counterCorrectService.count();
    }

}
