package com.tpi.depositosservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication(scanBasePackages = {
		"com.tpi.depositosservice",
		"com.commonlib" 
})

@EnableJpaRepositories(basePackages = {
		"com.tpi.depositosservice.repositorios"
})

@EntityScan(basePackages = {
		"com.commonlib.entidades" 
})

public class DepositosServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(DepositosServiceApplication.class, args);
	}

}
