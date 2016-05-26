package com.app.taxisharingDriver;


import com.app.taxisharingDriver.utility.SessionManager;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

public class GcmIntentService extends IntentService 
{
	public static final int NOTIFICATION_ID = 1;
	NotificationCompat.Builder builder;


	public GcmIntentService()
	{
		super("340619111229");
	}

	@Override
	protected void onHandleIntent(Intent intent) 
	{
		Bundle extras = intent.getExtras();
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
		// The getMessageType() intent parameter must be the intent you received
		// in your BroadcastReceiver.
		String messageType = gcm.getMessageType(intent);

		if (!extras.isEmpty()) 
		{ 
			/**
			 * Filter messages based on message type. Since it is likely that GCM
			 * will be extended in the future with new message types, just ignore
			 * any message types you're not interested in, or that you don't
			 * recognize.
			 */
			if (GoogleCloudMessaging.
					MESSAGE_TYPE_SEND_ERROR.equals(messageType)) 
			{
				sendNotification("Send error: " + extras.toString(),"");
			} 
			else if (GoogleCloudMessaging.
					MESSAGE_TYPE_DELETED.equals(messageType))
			{
				sendNotification("Deleted messages on server: " +
						extras.toString(),"");
				// If it's a regular GCM message, do some work.
			} 
			else if (GoogleCloudMessaging.
					MESSAGE_TYPE_MESSAGE.equals(messageType)) 
			{
				// Post notification of received message.
				String message=null;
				String extra=null;
				if (extras!=null&&extras.toString().length()>0)
				{
					message = extras.getString("payload");
					extra = extras.getString("action");
				}
				sendNotification(getResources().getString(R.string.recieve)+" " + message,extra);
			}
		}
		// Release the wake lock provided by the WakefulBroadcastReceiver.
		GcmBroadcastReceiver.completeWakefulIntent(intent);

	}

	@SuppressWarnings("deprecation")
	private void sendNotification(String msg,String action) 
	{
		int icon = R.drawable.ic_launcher;             
		long when = System.currentTimeMillis();
		NotificationManager notificationManager = (NotificationManager)this.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification(icon, msg, when);
		String title = this.getString(R.string.app_name);
		PendingIntent intent =null;
		
		Intent notificationIntent = new Intent(this, MainActivity.class);
		
		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		intent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);

		notification.setLatestEventInfo(this, title, msg, intent);
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		
		SessionManager sessionManager = new SessionManager(this);

		// Play default notification sound
		if (sessionManager.getFlagNewBooking()) 
		{
			notification.sound = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.taxina);
			sessionManager.setFlagNewBooking(false);
		}
		else
		{
			notification.defaults |= Notification.DEFAULT_SOUND;
		}
		
		/*// Vibrate if vibrate is enabled
		notification.defaults |= Notification.DEFAULT_VIBRATE;*/
		// Vibrate if vibrate is enabled
		// Get instance of Vibrator from current Context
		/*Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

		// Vibrate for 400 milliseconds
		v.vibrate(15000);*/
		notificationManager.notify(0, notification); 
	}

}
