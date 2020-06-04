package com.github.smeucci.geo.data.kafka.streams.service;

import java.util.Properties;

import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.KStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.github.smeucci.geo.data.kafka.streams.costant.GeoDataConstant;

@Service
public class GeoDataStream {

	private static final Logger log = LoggerFactory.getLogger(GeoDataStream.class.getSimpleName());

	@Autowired
	private FilterByHemisphere filterByHemisphere;

	@Autowired
	private CountByHemisphere countByHemisphere;

	private StreamsBuilder streamsBuilder;

	private KStream<String, String> geoDataStream;

	private KafkaStreams kafkaStreams;

	public GeoDataStream init() {

		Assert.isNull(geoDataStream, "geoDataStream has already been set. Cannot initialize again.");
		Assert.isNull(streamsBuilder, "streamsBuilder has already been set. Cannot initialize again.");
		Assert.isNull(kafkaStreams, "kafkaStreams has already been set. Cannot initialize again.");

		// create topology
		streamsBuilder = new StreamsBuilder();

		// geo data topic
		geoDataStream = streamsBuilder.stream(GeoDataConstant.GEO_DATA_TOPIC);

		return this;

	}

	public GeoDataStream build(Properties properties) {

		Assert.notNull(geoDataStream, "geoDataStream is not set. Must first initialized.");
		Assert.notNull(streamsBuilder, "streamsBuilder is not set. Must first initialized.");
		Assert.isNull(kafkaStreams, "kafkaStreams has already been set. Cannot build again.");

		// build the topology
		Topology topology = this.streamsBuilder.build();

		// print topology
		log.info("{}", topology.describe());

		// create the kafka streams
		kafkaStreams = new KafkaStreams(topology, properties);

		return this;

	}

	public void start() {

		Assert.notNull(geoDataStream, "geoDataStream is not set. Must first be initialized.");
		Assert.notNull(streamsBuilder, "streamsBuilder is not set. Must first be initialized.");
		Assert.notNull(kafkaStreams, "kafkaStreams is null. Must first build.");

		// start the stream application
		kafkaStreams.start();

		// add shutdown hook
		Runtime.getRuntime().addShutdownHook(new Thread(kafkaStreams::close));

	}

	public GeoDataStream filterByHemisphere() {

		Assert.notNull(geoDataStream, "geoDataStream is not set. Must first be initialized.");
		Assert.notNull(streamsBuilder, "streamsBuilder is not set. Must first be initialized.");
		Assert.isNull(kafkaStreams, "kafkaStreams has already been set. Topology already built.");

		Assert.notNull(filterByHemisphere, "filterByHemisphere is not set.");

		filterByHemisphere.northern(geoDataStream);
		filterByHemisphere.southern(geoDataStream);

		return this;

	}

	public GeoDataStream countByHemisphere() {

		Assert.notNull(geoDataStream, "geoDataStream is not set. Must first be initialized.");
		Assert.notNull(streamsBuilder, "streamsBuilder is not set. Must first be initialized.");
		Assert.isNull(kafkaStreams, "kafkaStreams has already been set. Topology already built.");

		Assert.notNull(countByHemisphere, "countByHemisphere is not set.");

		countByHemisphere.count(geoDataStream);

		return this;

	}

}
