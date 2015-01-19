package com.example.weatherapp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.weatherapp.config.Config;
import com.example.weatherapp.config.ConnectionDetector;
import com.example.weatherapp.config.GPSTracker;
import com.example.weatherapp.modelclass.Weather;
import com.example.weatherapp.modelclass.WeatherListRowItem;

import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	Spinner cityNamesSpinner;
	Button getWeatherByNameBtn, getWeatherByGPSBtn;
		
	GPSTracker gps;
	
	String cityNameByGPS = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		findIds();
		
		
		final List<String> cityList = new ArrayList<String>();
		cityList.add("Pune");
		cityList.add("Mumbai");
		cityList.add("Nashik");
		cityList.add("Aurangabad");
		cityList.add("Solapur");
		
		
		ArrayAdapter<String> cityNamesAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, cityList);
		cityNamesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		cityNamesSpinner.setAdapter(cityNamesAdapter);
		
		
		gps = new GPSTracker(MainActivity.this);
		
		getWeatherByNameBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				ConnectionDetector netConnection = new ConnectionDetector(MainActivity.this);
				
				if(netConnection.isConnectingToInternet()){
					JSONWeatherTask task = new JSONWeatherTask();
					task.execute(new String[]{cityNamesSpinner.getSelectedItem().toString()});
				}else{
					Toast.makeText(MainActivity.this, "Internet connection is not available. Please connect to the internet and try again.", Toast.LENGTH_SHORT).show();
				}
				
//				JSONWeatherTask task = new JSONWeatherTask();
//				task.execute(new String[]{cityNamesSpinner.getSelectedItem().toString()});
								
			}
		});
		
		
		
		getWeatherByGPSBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				ConnectionDetector netConnection = new ConnectionDetector(MainActivity.this);
				if(netConnection.isConnectingToInternet()){
					
					if(gps.canGetLocation()){
						
						List<Address> addresses = null ;
	                    Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());                    
						try {
							addresses = geocoder.getFromLocation(gps.getLatitude(), gps.getLongitude(), 1);
							
							 if (addresses.size() > 0) {
								 
								Address address = addresses.get(0);
														    
							    String[] citynames = address.getAddressLine(2).split(",");
							    cityNameByGPS = citynames[0];
							    
							    for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
							    	Log.e("=Adress=",address.getAddressLine(i));
								}
							    
							    if(cityNameByGPS != null){
							    	
							    	JSONWeatherTask task = new JSONWeatherTask();
									task.execute(new String[]{cityNameByGPS});
							    	
							    }else{
							    	Toast.makeText(MainActivity.this, "Something went wrong, please try again.", Toast.LENGTH_SHORT).show();
							    }
							    
							 }
							
							
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							Log.e("GEOCODER", "InGeoder catch");
							e1.printStackTrace();
						}
						
						
					}else{
						gps.showSettingsAlert();
					}
				
				}else{
					Toast.makeText(MainActivity.this, "Internet connection is not available. Please connect to the internet and try again.", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		
	}

	private void findIds() {
		// TODO Auto-generated method stub
		cityNamesSpinner = (Spinner) findViewById(R.id.spinner_city_names);
		getWeatherByNameBtn = (Button) findViewById(R.id.btn_get_weather_by_name);
		getWeatherByGPSBtn = (Button) findViewById(R.id.btn_get_weather_gps);
	}

	
	private class JSONWeatherTask extends AsyncTask<String, Void, Long> {
		
		private ProgressDialog pd;
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			
			pd = new ProgressDialog(MainActivity.this);
			pd.setTitle("Getting Weather...");
			pd.setMessage("Please wait.");
			pd.setCancelable(false);
			pd.setIndeterminate(true);
			pd.show();
			
		}
		
		@Override
		protected Long doInBackground(String... params) {
			// TODO Auto-generated method stub
						
			
			Config.globalWeatherList = new ArrayList<WeatherListRowItem>();
			
			String data = ( (new WeatherHttpClient()).getWeatherData(params[0]));						
			
			Log.e("data :: ",""+data);
			
			if(data != null){
				
				try {
					
					// We create out JSONObject from the data
					JSONObject jObject = new JSONObject(data);
					
					String code = jObject.getString("cod");
					String message = (String) jObject.getString("message");
					
					JSONObject cityJsonObject = jObject.getJSONObject("city");
					
					String cityID = (String) cityJsonObject.getString("id");
					String cityName = cityJsonObject.getString("name");
					String countryName = cityJsonObject.getString("country");
					
					JSONArray weatherArray = jObject.getJSONArray("list");
					
					for(int i = 0; i < weatherArray.length(); i++){
						
						JSONObject listObj = weatherArray.getJSONObject(i);
						
						long date = listObj.getLong("dt");
						
						JSONObject tempJsonObj = listObj.getJSONObject("temp");
						double temp = tempJsonObj.getDouble("day");
						
						double pressure = listObj.getDouble("pressure");
						
						double humidity = listObj.getDouble("humidity");
												
						JSONArray weatherJsonArray = listObj.getJSONArray("weather");					
						JSONObject weatherJsonObject = (JSONObject) weatherJsonArray.get(0);
						
						String main = weatherJsonObject.getString("main");
						String description = weatherJsonObject.getString("description");
						String icon = weatherJsonObject.getString("icon");
						
//						byte[] iconData = ( (new WeatherHttpClient()).getImage(icon));
						
						Bitmap img = null;
						
//						to get icon of the weather
						
//						if(iconData != null && iconData.length > 0){
//							img = BitmapFactory.decodeByteArray(iconData, 0, iconData.length);
//						}
						
						double windSpeed = listObj.getDouble("speed");
						
						WeatherListRowItem item = new WeatherListRowItem(date, main, description, temp, pressure, humidity,
																		 windSpeed, countryName, cityName, img);
						
						Config.globalWeatherList.add(item);						
						
					}
					
					return (long) 1;
					
				} catch (JSONException e) {				
					e.printStackTrace();
					return null;
				}
			
		}else{
			return null;
		}
			
			
		}
		
		
		@Override
		protected void onPostExecute(Long result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			
			pd.dismiss();
			
			if(result != null){
				
				if(Config.globalWeatherList.size() > 0){
					
					Intent dispayIntent = new Intent(MainActivity.this, WeatherList.class);
					startActivity(dispayIntent);
					
				}else{
					Toast.makeText(MainActivity.this, "Something went wrong, please try again.", Toast.LENGTH_SHORT).show();
					
				}
				
			}else{
				Toast.makeText(MainActivity.this, "Something went wrong, please try again.", Toast.LENGTH_SHORT).show();
			}
			
			
		}
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	
}
