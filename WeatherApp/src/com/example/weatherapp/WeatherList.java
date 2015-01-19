package com.example.weatherapp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.example.weatherapp.config.Config;
import com.example.weatherapp.modelclass.WeatherListRowItem;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class WeatherList extends Activity{
	
	ListView weatherListView;
	
	WeatherListAdapter weatheAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.weather_list);
		
		weatherListView = (ListView) findViewById(R.id.lv_weather_listview);
		
		weatheAdapter = new WeatherListAdapter(WeatherList.this, R.layout.weather_list_row, Config.globalWeatherList);
		weatherListView.setAdapter(weatheAdapter);
	}
	
	
	public class WeatherListAdapter extends ArrayAdapter<WeatherListRowItem>{
		
		Context context;
		ViewHolder holder = null;
		WeatherListRowItem rowItem = null;
		
		public WeatherListAdapter(Context context, int weatherListRow,
				List<WeatherListRowItem> globalWeatherList) {
			// TODO Auto-generated constructor stub
			super(context, weatherListRow, globalWeatherList);
			this.context = context;
		}
		
		private class ViewHolder{
			ImageView weatherImageIV;
			TextView weatherDateTV;
			TextView weatherDesriptionTV;
			TextView weatherTempTV;
			TextView weatherPressureTV;
			TextView weatherHumidityTV;
			TextView weatherWindSpeedTV;			
		}
		
		public View getView(int position, View convertView, ViewGroup parent){
			
			rowItem = getItem(position);
			
			LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			
			if(convertView == null){
				
				convertView = mInflater.inflate(R.layout.weather_list_row, null);
		         
				holder = new ViewHolder();
				
				holder.weatherImageIV = (ImageView) convertView.findViewById(R.id.iv_weather_type);
				holder.weatherDateTV = (TextView) convertView.findViewById(R.id.tv_weather_date);
				holder.weatherDesriptionTV = (TextView) convertView.findViewById(R.id.tv_description);
				holder.weatherTempTV = (TextView) convertView.findViewById(R.id.tv_temp);
				holder.weatherPressureTV = (TextView) convertView.findViewById(R.id.tv_pressure);
				holder.weatherHumidityTV = (TextView) convertView.findViewById(R.id.tv_humidity);
				holder.weatherWindSpeedTV = (TextView) convertView.findViewById(R.id.tv_windspeed);
				holder.weatherImageIV.setImageBitmap(rowItem.getImage());
				
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			
			holder.weatherDateTV.setText(rowItem.getDate()+"");
			
//			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy"); 
//			Calendar calendar = Calendar.getInstance();
//			calendar.setTime(new Date());
//			calendar.setTimeInMillis(rowItem.getDate());			
//			String dateString = formatter.format(calendar.getTime());			
//			holder.weatherDateTV.setText(dateString);
			
			holder.weatherDesriptionTV.setText(rowItem.getCondition()+" - "+rowItem.getDescription());
			holder.weatherTempTV.setText("Temp: "+rowItem.getTemparature()+"");
			holder.weatherPressureTV.setText("Pressure: "+rowItem.getPressure()+"");
			holder.weatherHumidityTV.setText("Humidity: "+rowItem.getHumidity()+"");
			holder.weatherWindSpeedTV.setText("Wind speed: "+rowItem.getWindSpeed()+"");
			
			return convertView;
			
		}
		
	}
}
