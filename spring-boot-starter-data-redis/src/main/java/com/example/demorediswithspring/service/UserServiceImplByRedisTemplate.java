package com.example.demorediswithspring.service;

import com.example.demorediswithspring.dao.UserDao;
import com.example.demorediswithspring.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Qualifier("redis-template")
public class UserServiceImplByRedisTemplate implements UserService {

    @Autowired
    UserDao userDao;

    @Override
    public User findUserById(Long id) {
        return userDao.getUser(id);
    }

    @Override
    public void saveUser(User user) {
        userDao.addUser(user);
    }

    @Override
    public void deleteUserById(Long id) {
        userDao.deleteUser(id);
    }
}
