package com.app.taxisharingDriver.utility;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class LocationUpdate 
{
	private static    String mDebugTag = "LocationFinder";
	private static boolean mDebugLog = true;
	private android.app.Service mActivity;
	private Activity mActivity2;
	
	void logError(String msg)
	{

		if (mDebugLog) 
		{
			Utility.printLog(mDebugTag, msg);
		}
	}

	Timer timer1;
	LocationManager lm;
	LocationResult locationResult;
	boolean gps_enabled=false;
	boolean network_enabled = false;

	public boolean getLocation(Context context, LocationResult result)
	{
		try 
		{
			mActivity=(android.app.Service) context;
		} 
		catch (ClassCastException e) 
		{
			
		}


		//I use LocationResult callback class to pass location value from MyLocation to user code.
		locationResult=result;
		if(lm==null)
			lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

		//exceptions will be thrown if provider is not permitted.
		try
		{
			gps_enabled=lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
		}
		catch(Exception ex)
		{
		}
		try
		{
			network_enabled=lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		}
		catch(Exception ex)
		{
			
		}

		//don't start listeners if no provider is enabled
		if(!gps_enabled && !network_enabled)
		{
			return false;
		}
		else if(gps_enabled)
		{
			lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, VariableConstants.updatelocationUpdateTimerTiem, 0, locationListenerGps);
			return true;
		}

		else if(network_enabled)
		{
			lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, VariableConstants.updatelocationUpdateTimerTiem,0,locationListenerNetwork);
			return true;
		}
		else 
		{
			return false;
		}

	}

	LocationListener locationListenerGps = new LocationListener() 
	{
		public void onLocationChanged(Location location)
		{
			try 
			{
				float rotation= location.getBearing();
				locationResult.gotLocation(location.getLatitude(),location.getLongitude(),rotation);
			} 

			catch (Exception e)
			{

			}
		}
		public void onProviderDisabled(String provider) 
		{

		}
		public void onProviderEnabled(String provider)
		{

		}
		public void onStatusChanged(String provider, int status, Bundle extras)
		{

		}
	};


	public void stotopGPSLocationListner ()
	{
		try 
		{
			lm.removeUpdates(locationListenerGps);

		} 
		catch (Exception e) 
		{
		}
		try
		{
			lm.removeUpdates(locationListenerNetwork);
		} 
		catch (Exception e)
		{
		}

	}

	LocationListener locationListenerNetwork = new LocationListener() 
	{
		public void onLocationChanged(Location location)
		{
			try
			{
				float ratation=location.getBearing();
				locationResult.gotLocation(location.getLatitude(),location.getLongitude(),ratation);
			}
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
		public void onProviderDisabled(String provider)
		{
			
		}
		public void onProviderEnabled(String provider)
		{
			
		}
		public void onStatusChanged(String provider, int status, Bundle extras)
		{

		}
	};
	
	class GetLastLocation extends TimerTask 
	{
		@Override
		public void run() 
		{
			Location net_loc=null, gps_loc=null;
			if(gps_enabled)
			{
				gps_loc=lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			}
			if(network_enabled)
			{
				net_loc=lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			}

			//if there are both values use the latest one

			if(gps_loc!=null && net_loc!=null)
			{
				if(gps_loc.getTime()>net_loc.getTime())
				{
					float ratation=gps_loc.getBearing();
					locationResult.gotLocation(gps_loc.getLatitude(),gps_loc.getLongitude(),ratation);
				}
				else
				{   
					float ratation=net_loc.getBearing();
				    locationResult.gotLocation(net_loc.getLatitude(),net_loc.getLongitude(),ratation);
				}
				return;
			}

			if(gps_loc!=null)
			{
				float ratation=gps_loc.getBearing();
				locationResult.gotLocation(gps_loc.getLatitude(),gps_loc.getLongitude(),ratation);
				return;
			}
			if(net_loc!=null)
			{
				float ratation=net_loc.getBearing();
				locationResult.gotLocation(net_loc.getLatitude(),net_loc.getLongitude(),ratation);
				return;
			}

			locationResult.gotLocation(0,0,0.0f);
		}
	}

	public interface LocationResult
	{
		public void gotLocation(double latitude, double longitude, float rotation);
	}

	public String getLocationName(Context context,double pLatitude,double pLongitude){

		Geocoder mGeoCoder = new Geocoder(context);
		String detailedAddress="";
		try 
		{
			List<Address> addresses = mGeoCoder.getFromLocation(pLatitude,pLongitude, 10); //<10>
			for (Address address : addresses) 
			{
				detailedAddress+=address.getAddressLine(0)+",";
			}
		} catch (IOException e) 
		{
			Utility.printLog("LocateMe", "Could not get Geocoder data"+e);
		}
		return detailedAddress;
	}
}
