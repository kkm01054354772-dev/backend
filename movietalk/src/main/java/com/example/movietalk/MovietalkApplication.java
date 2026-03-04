package com.example.movietalk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling // FileCheckTask @Scheduled 어노테이션 활성화
@EnableJpaAuditing // BaseEntity @EntityListeners 동작
@SpringBootApplication
public class MovietalkApplication {

	public static void main(String[] args) {
		SpringApplication.run(MovietalkApplication.class, args);
	}

}
