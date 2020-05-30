package com.github.smeucci.geodatagenerator.data;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class GeoData {

	@Id
	private String id;

	private final long timestamp;
	private final double latitude;
	private final double longitude;

	public GeoData(double latitude, double longitude) {
		this.timestamp = Instant.now().toEpochMilli();
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public static GeoData generate() {

		double latitude = (Math.random() * 180) - 90;

		double longitude = (Math.random() * 360) - 180;

		return new GeoData(latitude, longitude);

	}

	public String getId() {
		return id;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	@Override
	public String toString() {
		return "GeoData [timestamp="
				+ LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneOffset.systemDefault()) + ", latitude="
				+ latitude + ", longitude=" + longitude + "]";
	}

}
