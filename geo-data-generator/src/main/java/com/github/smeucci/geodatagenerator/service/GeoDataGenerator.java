package com.github.smeucci.geodatagenerator.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.github.smeucci.geodatagenerator.data.GeoData;

@Service
public class GeoDataGenerator {

	private static final Logger log = LoggerFactory.getLogger(GeoDataGenerator.class.getSimpleName());

	@Autowired
	private GeoDataStore geoDataMongoService;

	@Scheduled(fixedRateString = "${geo.data.generation.rate.ms:1000}")
	public void generateAndStore() {

		GeoData geoData = GeoData.generate();

		log.info("{}", geoData);

		geoDataMongoService.store(geoData);

	}

}
