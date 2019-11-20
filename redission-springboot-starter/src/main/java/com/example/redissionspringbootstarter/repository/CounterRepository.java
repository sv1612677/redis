package com.example.redissionspringbootstarter.repository;

import com.example.redissionspringbootstarter.entity.Counter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CounterRepository extends JpaRepository<Counter, Long> {
    Counter findCounterById(Long id);
}
