package com.github.smeucci.geo.data.kafka.streams.utils;

import java.util.Properties;
import java.util.function.Function;
import java.util.function.Predicate;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsConfig;

import com.google.gson.JsonParser; // TODO deprecated use jackson

public class GeoDataUtils {

	/**
	 * Predicate is in northern hemisphere
	 */
	public static Predicate<Double> isInNorthernHemisphere = latitude -> latitude > 0;

	/**
	 * Predicate is in southern hemisphere
	 */
	public static Predicate<Double> isInSouthernHemisphere = latitude -> latitude < 0;

	/**
	 * Function get key name for hemisphere
	 */
	public static Function<String, String> keyForHemisphere = geoData -> isInNorthernHemisphere
			.test(extractLatitude(geoData)) ? "northern_hemisphere" : "southern_hemisphere";

	/**
	 * Extract latitude from geo data json document
	 * 
	 * @param geoDataJson The json document representing the geo data
	 * @return the latitude
	 */
	public static double extractLatitude(String geoDataJson) {

		try {

			return JsonParser.parseString(geoDataJson) //
					.getAsJsonObject() //
					.get("latitude") //
					.getAsDouble();

		} catch (Exception e) {

			return 0;

		}

	}

	/**
	 * Return default properties for the kafka streams app
	 * 
	 * @return
	 */
	public static Properties properties() {

		// create properties
		Properties properties = new Properties();
		properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		properties.setProperty(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.StringSerde.class.getName());
		properties.setProperty(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.StringSerde.class.getName());

		// disable caching so that data is processing as it arrives
		properties.setProperty(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, "0");

		return properties;

	}

}
