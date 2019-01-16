package com.updateme.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaRepositories("com.updateme.repository")
@EntityScan("com.updateme.entity")
@ComponentScan(basePackages = {"com.updateme"})
@EnableScheduling
public class UpdatemeApplication {

    public static void main(String[] args) {
        SpringApplication.run(UpdatemeApplication.class, args);
    }
}


/**
 * @Note Scheduled tasks are commented out
 *
 *DEACTIVATED FEATURES
 * TWITCH CLIPS // Unsure
 * Would be nice
 * To setup a list query to twitch to reduce request
 */