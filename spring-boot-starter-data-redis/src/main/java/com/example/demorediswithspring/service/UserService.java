package com.example.demorediswithspring.service;

import com.example.demorediswithspring.entity.User;

public interface UserService {
    User findUserById(Long id);
    void saveUser(User user);
    void deleteUserById(Long id);
}
