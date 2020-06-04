package com.github.smeucci.geo.data.kafka.streams.service;

import java.util.function.Consumer;
import java.util.function.Predicate;

import org.apache.kafka.streams.kstream.KStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.gson.JsonParser;

@Service
public class FilterByHemisphere {

	private static final Logger log = LoggerFactory.getLogger(FilterByHemisphere.class.getSimpleName());

	private static final String NORTHERN_HEMISPHERE_GEO_DATA_TOPIC = "northern.hemisphere.geo.data";
	private static final String SOUTHERN_HEMISPHERE_GEO_DATA_TOPIC = "southern.hemisphere.geo.data";

	public void northern(KStream<String, String> geoDataStream) {

		// predicate is in northern hemisphere
		Predicate<Double> isInNorthernHemisphere = latitude -> latitude > 0;

		// consumer log northern hemisphere
		Consumer<String> logNorthernHemisphere = geoData -> log.info("Northern Hemisphere: {}", geoData);

		// filter for northern hemisphere geo data
		KStream<String, String> northernHemisphereStream = geoDataStream
				.filter((k, v) -> filterGeoDataByLatitude(v, isInNorthernHemisphere, logNorthernHemisphere));

		// set output topic
		northernHemisphereStream.to(NORTHERN_HEMISPHERE_GEO_DATA_TOPIC);

	}

	public void southern(KStream<String, String> geoDataStream) {

		// predicate is in southern hemisphere
		Predicate<Double> isInSouthernHemisphere = latitude -> latitude > 0;

		// consumer log southern hemisphere
		Consumer<String> logSouthernHemisphere = geoData -> log.info("Southern Hemisphere: {}", geoData);

		// filter for southern hemisphere geo data
		KStream<String, String> southernHemisphereStream = geoDataStream
				.filter((k, v) -> filterGeoDataByLatitude(v, isInSouthernHemisphere, logSouthernHemisphere));

		// set output topic
		southernHemisphereStream.to(SOUTHERN_HEMISPHERE_GEO_DATA_TOPIC);

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
