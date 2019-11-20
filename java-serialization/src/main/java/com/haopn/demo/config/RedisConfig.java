package com.haopn.demo.config;

import com.haopn.demo.model.Employee;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.FstCodec;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.codec.KryoCodec;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class RedisConfig {

    private Config config;

    @PostConstruct
    public void initConfig() {
        config = new Config();
        config.useSingleServer().setAddress("127.0.0.1:6379");
    }

    @Bean(destroyMethod = "shutdown")
    public RedissonClient redisClientWithFst() {
        config.setCodec(new FstCodec());
        return Redisson.create(config);
    }
    @Bean(destroyMethod = "shutdown")
    public RedissonClient redisClientWithKryo() {
        List<Class<?>> classes = new ArrayList<>();
        classes.add(Employee.class);
        classes.add(java.util.Date.class);
        ExtendKryoCodec kryoCodec = new ExtendKryoCodec(classes);
        config.setCodec(kryoCodec);
        return Redisson.create(config);
    }
    @Bean(destroyMethod = "shutdown")
    public RedissonClient redisClientWithJsonJackson() {
        config.setCodec(new JsonJacksonCodec());
        return Redisson.create(config);
    }

}
