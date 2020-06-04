package com.github.smeucci.geo.data.kafka.streams.service;

import java.util.function.Consumer;
import java.util.function.Predicate;

import org.apache.kafka.streams.kstream.KStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.github.smeucci.geo.data.kafka.streams.costant.GeoDataConstant;
import com.github.smeucci.geo.data.kafka.streams.utils.GeoDataUtils;

@Service
public class FilterByHemisphere {

	private static final Logger log = LoggerFactory.getLogger(FilterByHemisphere.class.getSimpleName());

	/**
	 * Filter northern hemisphere geo data
	 * 
	 * @param geoDataStream The source geo data stream
	 */
	public void northern(KStream<String, String> geoDataStream) {

		// consumer log northern hemisphere
		Consumer<String> logNorthernHemisphere = geoData -> log.info("Northern Hemisphere: {}", geoData);

		// filter for northern hemisphere geo data
		KStream<String, String> northernHemisphereStream = geoDataStream
				.filter((k, v) -> filterByLatitude(v, GeoDataUtils.isInNorthernHemisphere, logNorthernHemisphere));

		// set output topic
		northernHemisphereStream.to(GeoDataConstant.NORTHERN_HEMISPHERE_GEO_DATA_TOPIC);

	}

	/**
	 * Filter southern geo data
	 * 
	 * @param geoDataStream The source geo data stream
	 */
	public void southern(KStream<String, String> geoDataStream) {

		// consumer log southern hemisphere
		Consumer<String> logSouthernHemisphere = geoData -> log.info("Southern Hemisphere: {}", geoData);

		// filter for southern hemisphere geo data
		KStream<String, String> southernHemisphereStream = geoDataStream
				.filter((k, v) -> filterByLatitude(v, GeoDataUtils.isInSouthernHemisphere, logSouthernHemisphere));

		// set output topic
		southernHemisphereStream.to(GeoDataConstant.SOUTHERN_HEMISPHERE_GEO_DATA_TOPIC);

	}

	private boolean filterByLatitude(String geoDataJson, Predicate<Double> isInThisHemisphere,
			Consumer<String> logThisHemisphere) {

		Double latitude = GeoDataUtils.extractLatitude(geoDataJson);

		boolean inThisHemisphere = isInThisHemisphere.test(latitude);

		if (inThisHemisphere && logThisHemisphere != null) {
			logThisHemisphere.accept(geoDataJson);
		}

		return inThisHemisphere;

	}

}
