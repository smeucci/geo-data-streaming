package com.github.smeucci.geodatagenerator.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.github.smeucci.geodatagenerator.data.GeoData;

public interface GeoDataRepository extends MongoRepository<GeoData, String> {

}
