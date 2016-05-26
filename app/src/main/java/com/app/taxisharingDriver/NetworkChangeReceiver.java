package com.app.taxisharingDriver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;

import com.app.taxisharingDriver.utility.NetworkUtil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.app.taxisharingDriver.utility.SessionManager;

public class NetworkChangeReceiver extends BroadcastReceiver 
{
	private Notification notification;
	@Override
	public void onReceive(final Context context, final Intent intent) 
	{
		String status = NetworkUtil.getConnectivityStatusString(context);
		
		String[] networkStatus = status.split(",");

		//Toast.makeText(context, networkStatus[0], Toast.LENGTH_LONG).show();
		
		Intent homeIntent=new Intent("com.app.driverapp.internetStatus");
		homeIntent.putExtra("STATUS", networkStatus[1]);
		context.sendBroadcast(homeIntent);

		if(!"1".equals(networkStatus[1]))
		{
			sendNotification(context,networkStatus[1]);
		}
	}

	private void sendNotification(Context context, String staus)
	{
		int icon = R.drawable.ic_launcher;
		long when = System.currentTimeMillis();
		NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

		String title = context.getString(R.string.app_name);
		SessionManager sessionManager = new SessionManager(context);
		Intent notificationIntent;
		notificationIntent = new Intent(context, MainActivity.class);
		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

		PendingIntent intent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);
		if ("1".equals(staus))
		{
			notification = new Notification(icon, "Internet connected", when);
			notification.setLatestEventInfo(context, title, "Internet connected", intent);
		}
		else{
			notification = new Notification(icon, "No network connection found.", when);
			notification.setLatestEventInfo(context, title, "No network connection found.", intent);
		}

		notification.flags |= Notification.FLAG_AUTO_CANCEL;

		notification.defaults |= Notification.DEFAULT_SOUND;

		// Vibrate if vibrate is enabled
		notification.defaults |= Notification.DEFAULT_VIBRATE;
		notificationManager.notify(0, notification);
	}
}
