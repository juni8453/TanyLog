package com.blog.tanylog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class TanylogApplication {

	public static void main(String[] args) {
		SpringApplication.run(TanylogApplication.class, args);
	}

}
