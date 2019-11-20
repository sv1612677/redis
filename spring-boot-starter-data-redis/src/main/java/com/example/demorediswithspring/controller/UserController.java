package com.example.demorediswithspring.controller;

import com.example.demorediswithspring.dao.UserDao;
import com.example.demorediswithspring.entity.User;
import com.example.demorediswithspring.repository.UserRepository;
import com.example.demorediswithspring.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.annotation.AliasFor;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    private final Logger LOG = LoggerFactory.getLogger(getClass());


    @Autowired
    @Qualifier("redis-template")
    UserService userService;

    @Cacheable(value = "users", key = "#userId", unless = "#result.followers < 12000")
    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    public User getUser(@PathVariable String userId) {
        LOG.info("Getting user with ID {}.", userId);
        return userService.findUserById(Long.valueOf(userId));
    }

    @CachePut(value = "users", key = "#user.id")
    @PutMapping("/update")
    public User updatePersonByID(@RequestBody User user) {
        userService.saveUser(user);
        return user;
    }

    @CacheEvict(value = "users", key = "#userId")
    @DeleteMapping("/{userId}")
    public void deleteUserByID(@PathVariable Long userId) {
        LOG.info("deleting person with id {}", userId);
        userService.deleteUserById(userId);
    }
}
