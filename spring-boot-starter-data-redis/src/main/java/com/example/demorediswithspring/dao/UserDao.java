package com.example.demorediswithspring.dao;

import com.example.demorediswithspring.config.ConfigDefault;
import com.example.demorediswithspring.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Repository;

@Repository
public class UserDao {

    @Autowired
    HashOperations<String, Long, User> hashOperations;

    public void addUser(User user) {
        hashOperations.putIfAbsent(ConfigDefault.getUserKey(), user.getId(), user);
    }

    public void updateUser(User user) {
        hashOperations.put(ConfigDefault.getUserKey(), user.getId(), user);
    }

    public User getUser(Long id){
        return hashOperations.get(ConfigDefault.getUserKey(),id);
    }
    public long getNumberOfUser(){
        return hashOperations.size(ConfigDefault.getUserKey());
    }
    public void deleteUser(Long id){
        hashOperations.delete(ConfigDefault.getUserKey(),id);
    }

}
