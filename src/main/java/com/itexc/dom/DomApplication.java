package com.itexc.dom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@ConfigurationPropertiesScan
@EnableTransactionManagement
@SpringBootApplication
public class DomApplication {

	public static void main(String[] args) {
		SpringApplication.run(DomApplication.class, args);
	}

}
