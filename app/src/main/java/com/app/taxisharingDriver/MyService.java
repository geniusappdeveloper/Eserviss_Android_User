package com.app.taxisharingDriver;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.app.taxisharingDriver.utility.PublishUtility;
import com.app.taxisharingDriver.utility.SessionManager;
import com.app.taxisharingDriver.utility.Utility;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.pubnub.api.Pubnub;

public class MyService extends Service implements ConnectionCallbacks,OnConnectionFailedListener
{
	double currentLat ;
	double currentLng ;
	float previousmLatitude = 0 ;
	float previousmLongitude = 0; 
	private Pubnub pubnub;
	private SessionManager sessionManager;
	double previousLat = 0.00 ,previousLng = 0.00;
	//private int locationupdateCount = 0;
	private ArrayList<String>ChanneList ;
	private String subscribChannel;
	private String driverChannel;
	Timer myTimer_publish ;
	TimerTask myTimerTask_publish;
	double lati = 0.0 ,longi = 0.0;
	static double distancespd;
	double distanceKm;
	String strDouble;
	private static final String KILOMETER = "Kilometers";
	private static final String METER = "meters";
	private static final String MILES = "miles";
	private static final String NAUTICAL_MILES = "nauticalMiles";
	private GoogleApiClient mGoogleApiClient;
	private LocationRequest mLocationRequest;
	private Location myLoc;
	
	public MyService()
	{

	}
	//private LocationUpdate locationUpdate;
	@Override
	public IBinder onBind(Intent intent) 
	{
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public void onCreate() 
	{
		//Toast.makeText(this, "The new Service Created", Toast.LENGTH_SHORT).show();
		pubnub=ApplicationController.getInstacePubnub();
		//locationupdateCount=0;
		ChanneList=ApplicationController.getChannelList(); 
		sessionManager=new SessionManager(MyService.this);
		if (sessionManager.getBeginJourney()) 
		{
			distancespd = sessionManager.getDistanceInDouble();
		}
		else 
		{
			distancespd = 0.0;
		}

		driverChannel=sessionManager.getChannelName();	
		subscribChannel=sessionManager.getSubscribeChannel();
		ChanneList.clear();
		ChanneList.add(driverChannel);
		ChanneList.add(subscribChannel);
		buildGoogleApiClient();
	}

	@Override
	public void onStart(Intent intent, int startId) 
	{
		mGoogleApiClient.connect();
		startPublishingWithTimer();
	}

	@Override
	public void onDestroy() 
	{
		mGoogleApiClient.disconnect();
		previousmLatitude = 0;
		previousmLongitude = 0;  
	}

	protected synchronized void buildGoogleApiClient() {
		mGoogleApiClient = new GoogleApiClient.Builder(this)
		.addConnectionCallbacks(this)
		.addOnConnectionFailedListener(this)
		.addApi(LocationServices.API)
		.build();
	}

	public void publishLocation(double latitude,double longitude)
	{
		//locationupdateCount=0;
		String driverName=sessionManager.getDriverName();
		if (sessionManager.isUserLogdIn())
		{
			for (int i = 0; i < ChanneList.size(); i++)
			{
				String message;
				if (sessionManager.getBeginJourney() && !"".equals(sessionManager.getBookingIdStatus()))
				{
					message="{\"a\" :"+4+", \"e_id\" :\""+sessionManager.getUserEmailid()+"\",\"tp\" :\""+sessionManager.getVehTypeId()+"\", \"lt\" :"+latitude+", \"lg\" :"+longitude+",\"n\" :\""+driverName+"\",\"bid\" :\""+sessionManager.getBookingIdStatus()+"\", \"chn\" :\""+subscribChannel+"\"}";
				}
				else {
					message = "{\"a\" :"+4+", \"e_id\" :\""+sessionManager.getUserEmailid()+"\",\"tp\" :\""+sessionManager.getVehTypeId()+"\", \"lt\" :"+latitude+", \"lg\" :"+longitude+",\"n\" :\""+driverName+"\", \"chn\" :\""+subscribChannel+"\"}";
				}

				Utility.printLog("MyService","Message  " +message );
				Utility.printLog("MyService","channel name  " +ChanneList.get(i) );
				//Toast.makeText(getApplicationContext(),   message,2000 ).show();
				PublishUtility.publish(ChanneList.get(i),message,pubnub);
				Log.e("publish details",message);
			}
		}
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) 
	{
		
	}
	@Override
	public void onConnected(Bundle connectionHint)
	{
		mLocationRequest = LocationRequest.create();
		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		mLocationRequest.setInterval(2000); // Update location every 2 second
		mLocationRequest.setSmallestDisplacement(20);
		LocationServices.FusedLocationApi.requestLocationUpdates(
				mGoogleApiClient, mLocationRequest, new LocationListener() 
				{
					@Override
					public void onLocationChanged(Location currentLoc)
					{
						myLoc = currentLoc;
						//Utility.printLog("Location is"+location.toString());

						currentLat = currentLoc.getLatitude();
						currentLng = currentLoc.getLongitude();
						sessionManager.setDriverCurrentlat(""+currentLat);
						sessionManager.setDriverCurrentLongi(""+currentLng);
						if (sessionManager.getBeginJourney()) 
						{
							if (previousLat == 0.00 && previousLng == 0.00) {
								previousLat = currentLat;
								previousLng = currentLng;
							} else {

							}
							Location previousLoc = new Location("");
							previousLoc.setLatitude(previousLat);
							previousLoc.setLongitude(previousLng);

							double distance = 0.0;

							if (currentLat != previousLat || currentLng != previousLng) {
								double tempDist = distance(previousLat, previousLng, currentLat, currentLng, METER);
								try {
									DecimalFormat dformat = new DecimalFormat("#.####");
									if(String.valueOf(tempDist).contains(","))
									{
										String temp=String.valueOf(tempDist);
										temp=temp.replace(",",".");
										tempDist=Double.parseDouble(temp);
									}
									distance = Double.parseDouble(dformat.format(tempDist));

									Utility.printLog("Myservice Distance  = " + distance);

									if (distance > 10.00 && distance < 300.00) {
										distancespd += distance;
										Utility.printLog("Myservice Total distance" + distancespd);
										//Toast.makeText(getApplicationContext(), "" +distance+" total: "+distancespd,Toast.LENGTH_SHORT).show();
										sessionManager.setDistanceInDouble("" + distancespd);
										distanceKm = distancespd * (0.001);
										//distanceKm = distancespd * (0.00062137);
										DecimalFormat df = new DecimalFormat("#.##");
										strDouble = df.format(distanceKm);
										//Utility.printLog("Myservice Distance  = " + strDouble);
										sessionManager.setDistance(strDouble);
										//sessionManager.setDistance("" + distancespd);
										previousLat = currentLat;
										previousLng = currentLng;
									}
								}catch (Exception e){
									e.printStackTrace();
								}
							}
							if (sessionManager.getIsPassengerDropped())
							{
								sessionManager.setIsPassengerDropped(false);
								distancespd = 0.0;
							}
						}
						//publishLocation(currentLat,currentLng);
					}
				});
		
		Handler postHandler = new Handler();
		
		postHandler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				if(myLoc==null)
				{
					Utility.printLog("NO LOCATION");
					//showSettingsAlert();
				}


			}
		}, 10000);
	}
	@Override
	public void onConnectionSuspended(int cause)
	{
		
	}

	public static double distance(double lat1, double lng1, double lat2, double lng2, String unit)
	{
		double earthRadius = 3958.75; // miles (or 6371.0 kilometers)
		double dLat = Math.toRadians(lat2-lat1);
		double dLng = Math.toRadians(lng2-lng1);
		double sindLat = Math.sin(dLat / 2);
		double sindLng = Math.sin(dLng / 2);
		double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2) * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
		double dist = earthRadius * c ;

		if (KILOMETER.equals(unit))				// Kilometer
		{
			dist = dist * 1.609344;
		}
		else if (NAUTICAL_MILES.equals(unit))			// Nautical Miles
		{
			dist = dist * 0.8684;
		}
		else if(METER.equals(unit))			// meter
		{
			dist = dist * 1609.344;
		}

		return dist;
	}


	/**
	 * This method run in every 2 seconds.
	 */
	private void startPublishingWithTimer()
	{
		Utility.printLog("CONTROL INSIDE startPublishingWithTimer");

		if(myTimer_publish!= null)
		{
			Utility.printLog("Timer already started");
			return;
		}
		myTimer_publish = new Timer();

		myTimerTask_publish = new TimerTask()
		{
			@Override
			public void run()
			{
				this.runOnUiThread(new Runnable()
				{
					public void run()
					{

					}
				});

				publishLocation(sessionManager.getDriverCurrentLat(),sessionManager.getDriverCurrentLongi());
				//publishLocation(38.907192,-77.036871);
			}

			private void runOnUiThread(Runnable runnable)
			{

			}
		};
		myTimer_publish.schedule(myTimerTask_publish, 000, 2000);
	}
}