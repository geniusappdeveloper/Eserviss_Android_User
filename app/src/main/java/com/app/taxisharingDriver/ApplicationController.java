package com.app.taxisharingDriver;

import com.app.taxisharingDriver.utility.VariableConstants;

import java.util.ArrayList;

import android.content.Intent;

import com.pubnub.api.Pubnub;
public class ApplicationController extends android.app.Application 
{
	private static Pubnub pubnub;
	private static ArrayList<String>ChanneList=new ArrayList<String>();
	//private static boolean IsCurrentStatusIsIAmOntheWay;
	private static Intent serviceIntent;
	@Override
	public void onCreate()
	{
		super.onCreate();
		serviceIntent=new Intent(this, MyService.class);
	//pubnub = new Pubnub("pub-c-e0bbb276-2674-46cf-a0ec-446b796dd54a","sub-c-702561d2-4b40-11e5-854b-02ee2ddab7fe","",true);
		pubnub = new Pubnub(VariableConstants.PUB_KEY,VariableConstants.SUB_KEY,"",true);
	}
	public static Pubnub getInstacePubnub()
	{
		return pubnub;
	}
	public static ArrayList<String> getChannelList()
	{
		return ChanneList;
	}
	public static Intent getMyServiceInstance()
	{
		return serviceIntent;
	}

}
