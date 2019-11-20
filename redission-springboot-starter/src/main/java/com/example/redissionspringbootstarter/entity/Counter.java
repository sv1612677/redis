package com.example.redissionspringbootstarter.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "counter")
public class Counter implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long numberCount;

    public Counter()
    {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNumberCount() {
        return numberCount;
    }

    public void setNumberCount(Long numberCount) {
        this.numberCount = numberCount;
    }
}
