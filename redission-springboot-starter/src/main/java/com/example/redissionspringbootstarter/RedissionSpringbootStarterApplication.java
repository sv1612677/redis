package com.example.redissionspringbootstarter;

import com.example.redissionspringbootstarter.entity.Counter;
import com.example.redissionspringbootstarter.repository.CounterRepository;
import com.example.redissionspringbootstarter.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;


@SpringBootApplication
public class RedissionSpringbootStarterApplication {

	public static void main(String[] args) {
		ApplicationContext applicationContext = SpringApplication.run(RedissionSpringbootStarterApplication.class, args);
		String[] beans = applicationContext.getBeanDefinitionNames();
		for (String bean:
			 beans) {
			System.out.println(bean.getClass());
		}
	}
}
