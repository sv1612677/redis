package com.haopn.demo.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Employee implements Serializable {
    private static final long serialVersionUID = 3657773293974543890L;

    private String firstName;
    private String lastName;
    private String socialSecurityNumber;
    private String department;
    private String position;
    private Date hireDate;
    private Double salary;
    private Employee supervisor;
}
