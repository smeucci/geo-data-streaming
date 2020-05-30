package com.github.smeucci.geodatagenerator;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.github.smeucci.geodatagenerator.data.GeoData;
import com.github.smeucci.geodatagenerator.repository.GeoDataRepository;
import com.github.smeucci.geodatagenerator.service.GeoDataGenerator;
import com.github.smeucci.geodatagenerator.service.GeoDataStore;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { GeoDataGenerator.class, GeoDataStore.class })
@TestMethodOrder(OrderAnnotation.class)
class GeoDataGeneratorApplicationTests {

	private static final Logger log = LoggerFactory.getLogger(GeoDataGeneratorApplicationTests.class.getSimpleName());

	@Autowired
	private GeoDataGenerator geoDataGeneratorService;

	@Autowired
	private GeoDataStore geoDataMongoService;

	@MockBean
	private GeoDataRepository geoDataRepository;

	@BeforeEach
	public void before() {
		log.info("============ START TEST ============");
	}

	@AfterEach
	public void after() {
		log.info("=====================================\n");
	}

	@Test
	@Order(1)
	public void contextLoads() {

		log.info("==> contextLoads");

		Assertions.assertNotNull(geoDataGeneratorService);
		Assertions.assertNotNull(geoDataMongoService);
		Assertions.assertNotNull(geoDataRepository);

	}

	@Test
	@Order(2)
	public void geoDataGenerator() {

		log.info("==> geoDataGenerator");

		int total = 1_000_000;

		log.info("Generate {} geo data and verify their correctness.", total);

		for (int i = 0; i < total; i++) {

			GeoData geoData = GeoData.generate();

			Assertions.assertTrue(geoData.getTimestamp() > 0);

			Assertions.assertTrue(geoData.getLatitude() >= -90);
			Assertions.assertTrue(geoData.getLatitude() <= 90);

			Assertions.assertTrue(geoData.getLongitude() >= -180);
			Assertions.assertTrue(geoData.getLongitude() <= 180);

		}

	}

}
