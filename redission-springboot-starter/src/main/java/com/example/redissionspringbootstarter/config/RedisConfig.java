package com.example.redissionspringbootstarter.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.FstCodec;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
public class RedisConfig {
    @Bean(destroyMethod = "shutdown")
    RedissonClient RedissionClient()
    {
        Config config =  new Config();
        config.useSingleServer().setAddress("redis://127.0.0.1:6379");
        config.setCodec(new FstCodec());
        return Redisson.create(config);
    }
}
