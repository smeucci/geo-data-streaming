package com.github.smeucci.geo.data.kafka.streams.service;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Named;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.stereotype.Service;

import com.github.smeucci.geo.data.kafka.streams.costant.GeoDataConstant;
import com.github.smeucci.geo.data.kafka.streams.utils.GeoDataUtils;

@Service
public class CountByHemisphere {

	/**
	 * Count the number of geo data for each hemisphere
	 * 
	 * @param geoDataStream The source geo data stream
	 */
	public void count(KStream<String, String> geoDataStream) {

		// count geo data occurrences by hemisphere
		KStream<String, Long> hemisphereStatsStream = geoDataStream
				// change key, use hemisphere
				.selectKey((k, v) -> GeoDataUtils.keyForHemisphere.apply(v))
				// group by key
				.groupByKey()
				// count occurrences for each hemisphere
				.count(Named.as("CountByHemisphere"))
				// convert to stream
				.toStream();

		// set output topic
		hemisphereStatsStream.to(GeoDataConstant.HEMISPHERE_GEO_DATA_STATISTICS_TOPIC,
				Produced.valueSerde(Serdes.Long()));

	}

}
