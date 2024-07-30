package com.social.mc_post;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication
@EnableJpaRepositories

public class McPostApplication {
	public static void main(String[] args) {
		SpringApplication.run(McPostApplication.class, args);
	}
}
