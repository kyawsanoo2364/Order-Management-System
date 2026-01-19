package com.vodica.order_system;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class OrderSystemApplication {
	static {
		Dotenv dotenv = Dotenv.configure()
				.directory("./")
				.ignoreIfMissing()
				.load();
		dotenv.entries().forEach(e->
				System.setProperty(e.getKey(), e.getValue())
		);
	}

	public static void main(String[] args) {
		SpringApplication.run(OrderSystemApplication.class, args);
	}

}
