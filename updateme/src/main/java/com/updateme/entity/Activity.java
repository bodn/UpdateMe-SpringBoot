package com.updateme.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Profile_Activity")
public class Activity {

    @Id
    @GeneratedValue
    private Integer postId;

    private Integer userId;

}
