package com.example.test.entity;

import lombok.Getter;
import lombok.Setter;

import javax.inject.Named;
import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "branch")
public class Branch {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String address;
    private Long company_id;
}
