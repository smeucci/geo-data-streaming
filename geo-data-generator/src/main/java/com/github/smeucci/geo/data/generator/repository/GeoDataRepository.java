package com.github.smeucci.geo.data.generator.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.github.smeucci.geo.data.generator.data.GeoData;

public interface GeoDataRepository extends MongoRepository<GeoData, String> {

}
