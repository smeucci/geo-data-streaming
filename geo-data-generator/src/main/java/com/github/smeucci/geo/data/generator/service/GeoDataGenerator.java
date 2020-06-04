package com.github.smeucci.geo.data.generator.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.github.smeucci.geo.data.generator.data.GeoData;

@Service
public class GeoDataGenerator implements InitializingBean {

	private static final Logger log = LoggerFactory.getLogger(GeoDataGenerator.class.getSimpleName());

	@Autowired
	private GeoDataStore geoDataStore;

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(geoDataStore, "geoDataStore cannot be null.");
	}

	@Scheduled(fixedRateString = "${geo.data.generation.rate.ms:1000}")
	public void generateAndStore() {

		GeoData geoData = GeoData.generate();

		log.info("{}", geoData);

		geoDataStore.store(geoData);

	}

}
