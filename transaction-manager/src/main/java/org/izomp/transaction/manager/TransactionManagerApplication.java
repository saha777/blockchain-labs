package org.izomp.transaction.manager;

import org.izomp.transaction.manager.config.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class TransactionManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(TransactionManagerApplication.class, args);
	}

}
