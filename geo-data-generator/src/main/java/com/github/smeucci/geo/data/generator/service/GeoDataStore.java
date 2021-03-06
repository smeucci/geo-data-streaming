package com.github.smeucci.geo.data.generator.service;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.github.smeucci.geo.data.generator.data.GeoData;
import com.github.smeucci.geo.data.generator.repository.GeoDataRepository;

@Service
public class GeoDataStore implements InitializingBean {

	@Autowired
	private GeoDataRepository geoDataRepository;

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(geoDataRepository, "geoDataRepository cannot be null.");
	}

	public void store(GeoData geoData) {

		this.geoDataRepository.save(geoData);

	}

}
