package com.github.smeucci.geo.data.kafka.streams;

import java.util.Properties;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Named;
import org.apache.kafka.streams.kstream.Produced;
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
	private static final String HEMISPHERE_GEO_DATA_STATISTICS_TOPIC = "hemisphere.geo.data.statistics";

	public static void main(String[] args) {
		SpringApplication.run(GeoDataKafkaStreamsApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		// create properties
		Properties properties = new Properties();
		properties.setProperty(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
		properties.setProperty(StreamsConfig.APPLICATION_ID_CONFIG, APPLICATION_ID);
		properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		properties.setProperty(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.StringSerde.class.getName());
		properties.setProperty(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.StringSerde.class.getName());
		properties.setProperty(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, "0");

		// create topology
		StreamsBuilder streamsBuilder = new StreamsBuilder();

		// geo data topic
		KStream<String, String> geoDataStream = streamsBuilder.stream(GEO_DATA_TOPIC);

		// predicate is in hemisphere
		Predicate<Double> isInNorthernHemisphere = latitude -> latitude > 0;
		Predicate<Double> isInSouthernHemisphere = latitude -> latitude < 0;

		// consumer log hemisphere
		Consumer<String> logNorthernHemisphere = geoData -> log.info("Northern Hemisphere: {}", geoData);
		Consumer<String> logSouthernHemisphere = geoData -> log.info("Southern Hemisphere: {}", geoData);

		// function get key name for hemisphere
		Function<String, String> keyForHemisphere = geoData -> isInNorthernHemisphere
				.test(getLatitudeFromGeoDataJson(geoData)) ? "northern_hemisphere" : "southern_hemisphere";

		// filter for northern hemisphere geo data
		KStream<String, String> northernHemisphereStream = geoDataStream
				.filter((k, v) -> filterGeoDataByLatitude(v, isInNorthernHemisphere, logNorthernHemisphere));

		// filter for southern hemisphere geo data
		KStream<String, String> southernHemisphereStream = geoDataStream
				.filter((k, v) -> filterGeoDataByLatitude(v, isInSouthernHemisphere, logSouthernHemisphere));

		// count geo data occurrences by hemisphere
		KStream<String, Long> hemisphereStatsStream = geoDataStream
				// change key, use hemisphere
				.selectKey((k, v) -> keyForHemisphere.apply(v))
				// group by key
				.groupByKey()
				// count occurrences for each hemisphere
				.count(Named.as("CountByHemisphere"))
				// convert to stream
				.toStream();

		// set output topics
		northernHemisphereStream.to(NORTHERN_HEMISPHERE_GEO_DATA_TOPIC);
		southernHemisphereStream.to(SOUTHERN_HEMISPHERE_GEO_DATA_TOPIC);
		hemisphereStatsStream.to(HEMISPHERE_GEO_DATA_STATISTICS_TOPIC, Produced.valueSerde(Serdes.Long()));

		// build the topology
		Topology topology = streamsBuilder.build();
		KafkaStreams kafkaStreams = new KafkaStreams(topology, properties);

		// start the stream application
		kafkaStreams.start();

		// print topology
		log.info("{}", topology.describe());

		// add shutdown hook
		Runtime.getRuntime().addShutdownHook(new Thread(kafkaStreams::close));

	}

	private boolean filterGeoDataByLatitude(String geoDataJson, Predicate<Double> isInThisHemisphere,
			Consumer<String> logThisHemisphere) {

		Double latitude = getLatitudeFromGeoDataJson(geoDataJson);

		boolean inThisHemisphere = isInThisHemisphere.test(latitude);

		if (inThisHemisphere && logThisHemisphere != null) {
			logThisHemisphere.accept(geoDataJson);
		}

		return inThisHemisphere;

	}

	private double getLatitudeFromGeoDataJson(String geoDataJson) {

		try {

			return JsonParser.parseString(geoDataJson) //
					.getAsJsonObject() //
					.get("latitude") //
					.getAsDouble();

		} catch (Exception e) {

			return 0;

		}

	}

}
