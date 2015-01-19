package com.example.weatherapp.modelclass;

import android.graphics.Bitmap;

public class WeatherListRowItem {
	
	private long date;
	private String condition;
	private String description;
	private double temparature;
	private double pressure;
	private double humidity;
	private double windSpeed;
	private String country;
	private String city;
	private Bitmap image;
	
	public WeatherListRowItem(long date, String condition, String description,
			double temparature, double pressure, double humidity,
			double windSpeed, String country, String city, Bitmap img) {
		// TODO Auto-generated constructor stub
		this.date = date; 
		this.condition = condition;
		this.description = description;
		this.temparature = temparature;
		this.pressure = pressure;
		this.humidity =humidity;
		this.windSpeed = windSpeed;
		this.country = country;
		this.city = city;	
		this.image = img;
	}

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getTemparature() {
		return temparature;
	}

	public void setTemparature(double temparature) {
		this.temparature = temparature;
	}

	public double getPressure() {
		return pressure;
	}

	public void setPressure(double pressure) {
		this.pressure = pressure;
	}

	public double getHumidity() {
		return humidity;
	}

	public void setHumidity(double humidity) {
		this.humidity = humidity;
	}

	public double getWindSpeed() {
		return windSpeed;
	}

	public void setWindSpeed(double windSpeed) {
		this.windSpeed = windSpeed;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Bitmap getImage() {
		return image;
	}

	public void setImage(Bitmap image) {
		this.image = image;
	}
	
	
	
}
