package com.github.smeucci.geo.data.kafka.streams.service;

import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.smeucci.geo.data.kafka.streams.costant.GeoDataConstant;
import com.github.smeucci.geo.data.kafka.streams.utils.GeoDataUtils;

public class FilterAndCountByHemisphere {

	private static final Logger log = LoggerFactory.getLogger(FilterAndCountByHemisphere.class.getSimpleName());

	/**
	 * Filter and count northern hemisphere geo data
	 * 
	 * @param geoDataStream The source geo data stream
	 */
	public void northern(KStream<String, String> geoDataStream) {

		geoDataStream
				// keep northern hemisphere geo data
				.filter(GeoDataUtils.isInNorthernHemisphere)
				// peek geo data
				.peek((k, v) -> log.info("Northern Hemisphere: {}", v))
				// send to northern hemisphere topic
				.through(GeoDataConstant.NORTHERN_HEMISPHERE_GEO_DATA_TOPIC)
				// change key, use northern hemisphere
				.selectKey((k, v) -> GeoDataConstant.NORTHERN_HEMISPHERE_KEY)
				// group by key
				.groupByKey()
				// count northern hemisphere occurrences
				.count(Named.as("CountNorthernHemisphere"))
				// convert to stream
				.toStream()
				// send to hemisphere statistics topic
				.to(GeoDataConstant.HEMISPHERE_GEO_DATA_STATISTICS_TOPIC);

	}

	/**
	 * Filter and count southern geo data
	 * 
	 * @param geoDataStream The source geo data stream
	 */
	public void southern(KStream<String, String> geoDataStream) {

		geoDataStream
				// keep southern hemisphere geo data
				.filter(GeoDataUtils.isInSouthernHemisphere)
				// peek geo data
				.peek((k, v) -> log.info("Southern Hemisphere: {}", v))
				// send to southern hemisphere topic
				.through(GeoDataConstant.SOUTHERN_HEMISPHERE_GEO_DATA_TOPIC)
				// change key, use southern hemisphere
				.selectKey((k, v) -> GeoDataConstant.SOUTHERN_HEMISPHERE_KEY)
				// group by key
				.groupByKey()
				// count southern hemisphere occurrences
				.count(Named.as("CountSouthernHemisphere"))
				// convert to stream
				.toStream()
				// send to hemisphere statistics topic
				.to(GeoDataConstant.HEMISPHERE_GEO_DATA_STATISTICS_TOPIC);

	}

}
