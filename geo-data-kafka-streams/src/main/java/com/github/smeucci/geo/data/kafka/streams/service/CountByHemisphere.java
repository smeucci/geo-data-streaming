package com.github.smeucci.geo.data.kafka.streams.service;

import org.apache.kafka.streams.kstream.KStream;
import org.springframework.stereotype.Service;

@Service
public class CountByHemisphere {

	private static final String HEMISPHERE_GEO_DATA_STATISTICS_TOPIC = "hemisphere.geo.data.statistics";

	public void count(KStream<String, String> geoDataStream) {

	}

}
