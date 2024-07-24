package com.social.mc_post;

import com.social.mc_post.repository.PostRepository;
import com.social.mc_post.structure.PostEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Date;
import java.util.UUID;

@SpringBootApplication
@EnableJpaRepositories
public class McPostApplication {
	public static void main(String[] args) {
		SpringApplication.run(McPostApplication.class, args);
	}
}
