package com.github.smeucci.geodatakafkastreams;

import java.util.Properties;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.google.gson.JsonParser;

@SpringBootApplication
public class GeoDataKafkaStreamsApplication implements CommandLineRunner {

	private static final Logger log = LoggerFactory.getLogger(GeoDataKafkaStreamsApplication.class.getSimpleName());

	private static final String BOOTSTRAP_SERVER = "localhost:9092";
	private static final String APPLICATION_ID = "geo-data-kafka-streams";

	private static final String GEO_DATA_TOPIC = "mongo.geo-data-streaming.geoData";
	private static final String NORTHERN_HEMISPHERE_GEO_DATA_TOPIC = "northern.hemisphere.geo.data";
	private static final String SOUTHERN_HEMISPHERE_GEO_DATA_TOPIC = "southern.hemisphere.geo.data";

	public static void main(String[] args) {
		SpringApplication.run(GeoDataKafkaStreamsApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		// create properties
		Properties properties = new Properties();
		properties.setProperty(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
		properties.setProperty(StreamsConfig.APPLICATION_ID_CONFIG, APPLICATION_ID);
		properties.setProperty(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.StringSerde.class.getName());
		properties.setProperty(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.StringSerde.class.getName());

		// create topology
		StreamsBuilder streamsBuilder = new StreamsBuilder();

		// geo data topic
		KStream<String, String> geoDataTopic = streamsBuilder.stream(GEO_DATA_TOPIC);

		// predicate is in hemisphere
		Predicate<Double> isInNorthernHemisphere = latitude -> latitude > 0;
		Predicate<Double> isInSouthernHemisphere = latitude -> latitude < 0;

		// consumer log hemisphere
		Consumer<String> logNorthernHemisphere = geoData -> log.info("Northern Hemisphere: {}", geoData);
		Consumer<String> logSouthernHemisphere = geoData -> log.info("Southern Hemisphere: {}", geoData);

		// filter for northern hemisphere geo data
		KStream<String, String> northernHemisphereStream = geoDataTopic
				.filter((k, v) -> filterGeoDataByLatitude(v, isInNorthernHemisphere, logNorthernHemisphere));

		// filter for southern hemisphere geo data
		KStream<String, String> southernHemisphereStream = geoDataTopic
				.filter((k, v) -> filterGeoDataByLatitude(v, isInSouthernHemisphere, logSouthernHemisphere));

		// set output topics
		northernHemisphereStream.to(NORTHERN_HEMISPHERE_GEO_DATA_TOPIC);
		southernHemisphereStream.to(SOUTHERN_HEMISPHERE_GEO_DATA_TOPIC);

		// build the topology
		KafkaStreams kafkaStreams = new KafkaStreams(streamsBuilder.build(), properties);

		// start the stream application
		kafkaStreams.start();

	}

	private boolean filterGeoDataByLatitude(String geoDataJson, Predicate<Double> isInThisHemisphere,
			Consumer<String> logThisHemisphere) {

		try {

			Double latitude = JsonParser.parseString(geoDataJson) //
					.getAsJsonObject() //
					.get("latitude") //
					.getAsDouble();

			boolean inThisHemisphere = isInThisHemisphere.test(latitude);

			if (inThisHemisphere) {
				logThisHemisphere.accept(geoDataJson);
			}

			return inThisHemisphere;

		} catch (Exception e) {

			return false;

		}

	}

}
