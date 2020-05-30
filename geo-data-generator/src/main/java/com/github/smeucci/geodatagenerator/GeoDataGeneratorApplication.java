package com.github.smeucci.geodatagenerator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication()
public class GeoDataGeneratorApplication implements CommandLineRunner {

	private static final Logger log = LoggerFactory.getLogger(GeoDataGeneratorApplication.class.getSimpleName());

	public static void main(String[] args) {
		SpringApplication.run(GeoDataGeneratorApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		log.info("Hello from Geo Data Generator!");

	}

}
