package org.sejong.sulgamewiki;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SulGameWikiApplication {

	public static void main(String[] args) {
		SpringApplication.run(SulGameWikiApplication.class, args);
	}

}
