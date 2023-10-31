package org.cyberark.conjur.demos;

import com.cyberark.conjur.springboot.processor.SpringBootConjurAutoConfiguration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@ImportAutoConfiguration(classes = { SpringBootConjurAutoConfiguration.class})
public class ConjurSpringBootDemosApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConjurSpringBootDemosApplication.class, args);
	}

}
