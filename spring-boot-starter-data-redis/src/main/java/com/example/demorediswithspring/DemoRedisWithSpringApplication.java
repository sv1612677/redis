package com.example.demorediswithspring;

import com.example.demorediswithspring.dao.UserDao;
import com.example.demorediswithspring.entity.User;
import com.example.demorediswithspring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;


@SpringBootApplication
@EnableCaching
@EnableRedisRepositories
public class DemoRedisWithSpringApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(DemoRedisWithSpringApplication.class, args);
    }

    @Autowired
	UserRepository userRepository;

    @Autowired
	UserDao userDao;

	@Override
	public void run(String... args) throws Exception {
		User shubham = new User("Shubham", 2000);
		User pankaj = new User("Pankaj", 29000);
		User lewis = new User("Lewis", 550);

		shubham.setId(1L);
		pankaj.setId(2L);
		lewis.setId(3L);

		userDao.addUser(shubham);
		userDao.addUser(pankaj);
		userDao.addUser(lewis);

//		userRepository.save(shubham);
//		userRepository.save(pankaj);
//		userRepository.save(lewis);
	}
}
