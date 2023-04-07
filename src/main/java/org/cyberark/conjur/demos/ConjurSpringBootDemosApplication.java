package org.cyberark.conjur.demos;

import com.cyberark.conjur.springboot.processor.SpringBootConjurAutoConfiguration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(SpringBootConjurAutoConfiguration.class)
public class ConjurSpringBootDemosApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConjurSpringBootDemosApplication.class, args);
	}

}
