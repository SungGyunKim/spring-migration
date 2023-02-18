package com.egoksg.migration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
public class SpringMigrationApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringMigrationApplication.class, args);
	}

}
