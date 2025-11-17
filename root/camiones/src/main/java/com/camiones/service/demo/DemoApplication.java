package com.camiones.service.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {
		"com.camiones.service.demo",
		"com.commonlib" 
})

@EnableJpaRepositories(basePackages = {
		"com.camiones.service.demo.repositorios"
})

@EntityScan(basePackages = {
		"com.commonlib.entidades" 
})


public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
