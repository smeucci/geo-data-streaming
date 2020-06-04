package com.github.smeucci.geo.data.kafka.streams.service;

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

		// filter for northern hemisphere geo data
		KStream<String, String> northernHemisphereStream = geoDataStream
				// keep northern hemisphere geo data
				.filter(GeoDataUtils.isInNorthernHemisphere)
				// peek geo data
				.peek((k, v) -> log.info("Northern Hemisphere: {}", v));

		// set output topic
		northernHemisphereStream.to(GeoDataConstant.NORTHERN_HEMISPHERE_GEO_DATA_TOPIC);

	}

	/**
	 * Filter southern geo data
	 * 
	 * @param geoDataStream The source geo data stream
	 */
	public void southern(KStream<String, String> geoDataStream) {

		// filter for southern hemisphere geo data
		KStream<String, String> southernHemisphereStream = geoDataStream
				// keep southern hemisphere geo data
				.filter(GeoDataUtils.isInSouthernHemisphere)
				// peek geo data
				.peek((k, v) -> log.info("Southern Hemisphere: {}", v));

		// set output topic
		southernHemisphereStream.to(GeoDataConstant.SOUTHERN_HEMISPHERE_GEO_DATA_TOPIC);

	}

}
