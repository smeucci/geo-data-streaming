package com.github.smeucci.geo.data.kafka.streams.service;

import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class GeoDataStream {

	private static final String GEO_DATA_TOPIC = "mongo.geo-data-streaming.geoData";

	private KStream<String, String> geoDataStream;

	@Autowired
	private FilterByHemisphere filterByHemisphere;

	public void initStream() {

		// create topology
		StreamsBuilder streamsBuilder = new StreamsBuilder();

		// geo data topic
		this.geoDataStream = streamsBuilder.stream(GEO_DATA_TOPIC);

	}

	public void filterByHemisphere() {

		Assert.notNull(geoDataStream, "geoDataStream cannot be null.");

		filterByHemisphere.northern(geoDataStream);
		filterByHemisphere.southern(geoDataStream);

	}

	public void countByHemisphere() {

		Assert.notNull(geoDataStream, "geoDataStream cannot be null.");

	}

}
