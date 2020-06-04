package com.github.smeucci.geo.data.generator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GeoDataGeneratorApplication {

	public static void main(String[] args) {
		SpringApplication.run(GeoDataGeneratorApplication.class, args);
	}

}
