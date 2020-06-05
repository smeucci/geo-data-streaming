package com.github.smeucci.geo.data.kafka.streams;

import java.util.Properties;

import org.apache.kafka.streams.StreamsConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.github.smeucci.geo.data.kafka.streams.service.GeoDataStream;
import com.github.smeucci.geo.data.kafka.streams.utils.GeoDataUtils;

@SpringBootApplication
public class GeoDataKafkaStreamsApplication implements CommandLineRunner {

	private static final String APPLICATION_ID = "geo-data-kafka-streams";

	@Value("${KAFKA_URI:localhost:9092}")
	private String bootstrapServer;

	@Autowired
	private GeoDataStream geoDataStream;

	public static void main(String[] args) {
		SpringApplication.run(GeoDataKafkaStreamsApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		// create properties
		Properties properties = GeoDataUtils.properties();
		properties.setProperty(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
		properties.setProperty(StreamsConfig.APPLICATION_ID_CONFIG, APPLICATION_ID);

		geoDataStream
				// initialize geo data stream
				.init()
				// filter by hemisphere and count occurrences for each
				.filterAndCountByHemisphere()
				// build topology
				.build(properties)
				// start kafka streams app
				.start();

	}

}
