package com.aicoach.aisystemdesigncoachbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.aicoach.aisystemdesigncoachbackend.repository") // 👈 FORCE Jpa Scan
@EntityScan(basePackages = "com.aicoach.aisystemdesigncoachbackend.entity")
public class AiSystemDesignCoachBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(AiSystemDesignCoachBackendApplication.class, args);
	}

}
