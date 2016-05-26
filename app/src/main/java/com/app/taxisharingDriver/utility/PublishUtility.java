package com.app.taxisharingDriver.utility;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

import java.util.Hashtable;

import org.json.JSONArray;
import org.json.JSONObject;

import com.app.taxisharingDriver.MainActivity;
import com.app.taxisharingDriver.R;
import com.app.taxisharingDriver.log.LogFile;
import com.pubnub.api.Callback;
import com.pubnub.api.Pubnub;
import com.pubnub.api.PubnubError;

public class PublishUtility 
{
	Context c;



	public static void publish(String channelName, String Message , Pubnub pubnub)
	{
		_publish(channelName, Message, pubnub);

	}

	public static void _publish(final String channel,String message,Pubnub pubnub)
	{
		LogFile.log("Channel: " +channel+"\n Message:"+message);
		Hashtable<String, Object> args = new Hashtable<String, Object>(2);
		if (args.get("message") == null)
		{
			try {
				Integer i = Integer.parseInt(message);
				args.put("message", i);
			} catch (Exception e) {

			}
		}
		if (args.get("message") == null) {
			try {
				Double d = Double.parseDouble(message);
				args.put("message", d);
			} catch (Exception e) {

			}
		}
		if (args.get("message") == null) {
			try {
				JSONArray js = new JSONArray(message);
				args.put("message", js);
			} catch (Exception e) {

			}
		}
		if (args.get("message") == null)
		{
			try {
				JSONObject js = new JSONObject(message);
				args.put("message", js);
			} catch (Exception e) {

			}
		}
		if (args.get("message") == null)
		{
			args.put("message", message);
		}
		try {
			args.put("channel", channel); // Channel Name
		}
		catch (Exception e){}
		pubnub.publish(args, new Callback()
		{
			@Override
			public void successCallback(String channel,	Object message)
			{

			//	ErrorMessageForExpired("Pubnub Success Message","Channel: " +channel+"\n Message:"+message,c);
				notifyUserforPublish("PUBLISH : " + message);
			}
			@Override
			public void errorCallback(String channel, PubnubError error)
			{
			//	ErrorMessageForExpired("Pubnub Success Message","Channel: " +channel+"\n ErrorMessage:"+error,c);
				notifyUserforPublish("channel name : " + channel);
				notifyUserforPublish("PUBLISH : " + error);

			}
		});
	}

	private static void notifyUserforPublish(final String msg)
	{
		LogFile.log(msg);
		Utility.printLog("MainActivityDrower ", "notifyUserforBublish  msg "+msg);
	}

}
