package com.pedidos.service.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication(scanBasePackages = {
		"com.pedidos.service.demo",
		"com.commonlib" 
})

@EnableJpaRepositories(basePackages = {
		"com.pedidos.service.demo.repositorios"
})

@EntityScan(basePackages = {
		"com.commonlib.entidades" 
})

public class DemoApplication {
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
}
