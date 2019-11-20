package com.haopn.demo;

import com.haopn.demo.model.Employee;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.PostConstruct;
import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
class DemoApplicationTests {
	@Autowired
	RedissonClient redisClientWithFst;
	@Autowired
	RedissonClient redisClientWithJsonJackson;
	@Autowired
	RedissonClient redisClientWithKryo;

	private Employee employee;

	@PostConstruct
	public void initData() {
		employee = new Employee();
		employee.setFirstName("Hao");
		employee.setLastName("Abc");
		employee.setDepartment("IT");
		employee.setHireDate(new Date());
		employee.setPosition("Fresher");
		employee.setSalary(300.0);
		employee.setSocialSecurityNumber("12345678");
		employee.setSupervisor(new Employee());
	}
	// total 124 ms
	@Test
	public void persist_Fst() {
		RBucket<Employee> employeeRBucket = redisClientWithFst.getBucket("fst");
		long startTime = System.currentTimeMillis();
		employeeRBucket.set(employee);
		long endTime = System.currentTimeMillis();
		System.out.println("Total execution time: " + (endTime - startTime) + " ms");
	}
	// total 165 ms
	@Test
	public void persist_Kryo() {
		RBucket<Employee> employeeRBucket = redisClientWithKryo.getBucket("kryo");
		long startTime = System.currentTimeMillis();
		employeeRBucket.set(employee);
		long endTime = System.currentTimeMillis();
		System.out.println("Total execution time: " + (endTime - startTime) + " ms");
	}
	// total 199 ms
	@Test
	public void persist_JsonJackson() {
		RBucket<Employee> employeeRBucket = redisClientWithJsonJackson.getBucket("json");
		long startTime = System.currentTimeMillis();
		employeeRBucket.set(employee);
		long endTime = System.currentTimeMillis();
		System.out.println("Total execution time: " + (endTime - startTime) + " ms");
	}

	// total 136 ms
	@Test
	public void deserialization_Fst() {
		RBucket<Employee> employeeRBucket = redisClientWithFst.getBucket("fst");
		long startTime = System.currentTimeMillis();
		System.out.println(employeeRBucket.get().toString());
		long endTime = System.currentTimeMillis();
		System.out.println("Total execution time: " + (endTime - startTime) + " ms");
	}
	// total 162 ms
	@Test
	public void deserialization_Kryo() {
		RBucket<Employee> employeeRBucket = redisClientWithKryo.getBucket("kryo");
		long startTime = System.currentTimeMillis();
		System.out.println(employeeRBucket.get().toString());
		long endTime = System.currentTimeMillis();
		System.out.println("Total execution time: " + (endTime - startTime) + " ms");
	}
	// total 119 ms
	@Test
	public void deserialization_JsonJackson() {
		RBucket<Employee> employeeRBucket = redisClientWithJsonJackson.getBucket("json");
		long startTime = System.currentTimeMillis();
		System.out.println(employeeRBucket.get().toString());
		long endTime = System.currentTimeMillis();
		System.out.println("Total execution time: " + (endTime - startTime) + " ms");
	}

}
