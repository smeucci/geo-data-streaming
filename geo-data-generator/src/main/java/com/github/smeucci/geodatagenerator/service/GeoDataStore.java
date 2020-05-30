package com.github.smeucci.geodatagenerator.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.smeucci.geodatagenerator.data.GeoData;
import com.github.smeucci.geodatagenerator.repository.GeoDataRepository;

@Service
public class GeoDataStore {

	@Autowired
	private GeoDataRepository geoDataRepository;

	public void store(GeoData geoData) {

		this.geoDataRepository.save(geoData);

	}

}
