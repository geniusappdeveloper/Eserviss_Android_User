package com.javapapers.android.maps.path;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import com.app.taxisharingDriver.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class PathGoogleMapActivity extends FragmentActivity {

	private static  LatLng aptLatLng;
	private static  LatLng currentLatLng ;
	//private static final LatLng WALL_STREET = new LatLng(40.7064, -74.0094);

	GoogleMap googleMap;
	final String TAG = "PathGoogleMapActivity";
	private	double apntLat;
	private	double apntLong;
	private	double mLatitude;
	private	double mLongitude;
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_path_google_map);
		
		SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
		googleMap = fm.getMap();
         
		 Bundle bundle=getIntent().getExtras();
		 ActionBar	actionBar=getActionBar();
         actionBar.setDisplayShowTitleEnabled(true);
         actionBar.setDisplayHomeAsUpEnabled(true);
         actionBar.setDisplayUseLogoEnabled(false);
		//actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.navigation_bar));
		 actionBar.setIcon(android.R.color.transparent);
	     apntLat=bundle.getDouble("apntLat");
    	 apntLong=bundle.getDouble("apntLong");;
        if (apntLat!=0.0&&apntLong!=0.0) 
        {
        	//mLatitude = latitude;
    		//mLongitude = longitude;  
        	 mLatitude=bundle.getDouble("mLatitude");
        	 mLongitude=bundle.getDouble("mLongitude");;
        	 aptLatLng = new LatLng(apntLat,apntLong);
        	 currentLatLng = new LatLng(mLatitude, mLongitude);
    		MarkerOptions options = new MarkerOptions();
    		options.position(aptLatLng);
    		options.position(currentLatLng);
    		//options.position(WALL_STREET);
    		googleMap.addMarker(options);
    		String url = getMapsApiDirectionsUrl();
    		ReadTask downloadTask = new ReadTask();
    		downloadTask.execute(url);

    		googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng,	13));
    		addMarkers();
		}
        else 
        {
        	//mLatitude = latitude;
    		//mLongitude = longitude;  
        	 mLatitude=bundle.getDouble("mLatitude");
        	 mLongitude=bundle.getDouble("mLongitude");;
        	// aptLatLng = new LatLng(apntLat,apntLong);
        	 currentLatLng = new LatLng(mLatitude, mLongitude);
    		MarkerOptions options = new MarkerOptions();
    	//	options.position(aptLatLng);
    		options.position(currentLatLng);
    		//options.position(WALL_STREET);
    		googleMap.addMarker(options);
    		//String url = getMapsApiDirectionsUrl();
    		//ReadTask downloadTask = new ReadTask();
    		//downloadTask.execute(url);

    		googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng,	13));
    		//addMarkers();
		}
//    	//mLatitude = latitude;
//		//mLongitude = longitude;  
//    	 mLatitude=bundle.getDouble("mLatitude");
//    	 mLongitude=bundle.getDouble("mLongitude");;
//    	 aptLatLng = new LatLng(apntLat,apntLong);
//    	 currentLatLng = new LatLng(mLatitude, mLongitude);
//		MarkerOptions options = new MarkerOptions();
//		options.position(aptLatLng);
//		options.position(currentLatLng);
//		//options.position(WALL_STREET);
//		googleMap.addMarker(options);
//		String url = getMapsApiDirectionsUrl();
//		ReadTask downloadTask = new ReadTask();
//		downloadTask.execute(url);
//
//		googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng,	13));
//		addMarkers();

	}
	//private void 
	
	@Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) 
    {
    	switch (item.getItemId()) 
    	{
    	 case android.R.id.home:
		  		// NavUtils.navigateUpFromSameTask(this);
		  		 finish();
		         return true;
		         
    
		  		
		         
		default:
			return super.onOptionsItemSelected(item);
		}
    	
    }
	
	

	private String getMapsApiDirectionsUrl()
	{
		String waypoints = "waypoints=optimize:true|"+ aptLatLng.latitude + "," + aptLatLng.longitude+ "|" + "|" + currentLatLng.latitude + ","
				+ currentLatLng.longitude /*+ "|" + WALL_STREET.latitude + ","+ WALL_STREET.longitude*/;

		String sensor = "sensor=false";
		String params = waypoints + "&" + sensor;
		String output = "json";
		String url = "https://maps.googleapis.com/maps/api/directions/"
				+ output + "?" + params;
		return url;
	}

	private void addMarkers() 
	{
		if (googleMap != null) 
		{
			googleMap.addMarker(new MarkerOptions().position(aptLatLng)
					.title("First Point"));
			googleMap.addMarker(new MarkerOptions().position(currentLatLng)
					.title("Second Point"));
//			googleMap.addMarker(new MarkerOptions().position(WALL_STREET)
//					.title("Third Point"));
		}
	}

	private class ReadTask extends AsyncTask<String, Void, String> 
	{
		@Override
		protected String doInBackground(String... url) {
			String data = "";
			try {
				HttpConnection http = new HttpConnection();
				data = http.readUrl(url[0]);
			} catch (Exception e) {
				//Log.d("Background Task", e.toString());
			}
			return data;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			new ParserTask().execute(result);
		}
	}

	private class ParserTask extends
			AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

		@Override
		protected List<List<HashMap<String, String>>> doInBackground(
				String... jsonData) {

			JSONObject jObject;
			List<List<HashMap<String, String>>> routes = null;

			try {
				jObject = new JSONObject(jsonData[0]);
				PathJSONParser parser = new PathJSONParser();
				routes = parser.parse(jObject);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return routes;
		}

		@Override
		protected void onPostExecute(List<List<HashMap<String, String>>> routes) 
		{
			ArrayList<LatLng> points = null;
			PolylineOptions polyLineOptions = null;

			// traversing through routes
			for (int i = 0; i < routes.size(); i++) {
				points = new ArrayList<LatLng>();
				polyLineOptions = new PolylineOptions();
				List<HashMap<String, String>> path = routes.get(i);

				for (int j = 0; j < path.size(); j++) {
					HashMap<String, String> point = path.get(j);

					double lat = Double.parseDouble(point.get("lat"));
					double lng = Double.parseDouble(point.get("lng"));
					LatLng position = new LatLng(lat, lng);

					points.add(position);
				}

				polyLineOptions.addAll(points);
				polyLineOptions.width(2);
				polyLineOptions.color(Color.BLUE);
			}

			googleMap.addPolyline(polyLineOptions);
		}
	}
}
