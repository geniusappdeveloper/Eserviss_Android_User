/*
package com.app.taxisharingDriver;

*/
/**
 * Created by anubhootigupta on 15/10/15.
 *//*


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

import com.app.taxisharingDriver.pojo.PubnubResponse;
import com.app.taxisharingDriver.utility.PublishUtility;
import com.app.taxisharingDriver.utility.SessionManager;
import com.app.taxisharingDriver.utility.Utility;
import com.app.taxisharingDriver.utility.VariableConstants;
import com.google.gson.Gson;
import com.pubnub.api.Callback;
import com.pubnub.api.Pubnub;
import com.pubnub.api.PubnubException;

public class PubnubService extends Service {

    SessionManager sessionManager ;
    String channel;

    Pubnub pubnub = new Pubnub(VariableConstants.PUB_KEY,VariableConstants.SUB_KEY,"",false);
    PowerManager.WakeLock wl = null;

    private final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            String pnMsg = msg.obj.toString();

            final Toast toast = Toast.makeText(getApplicationContext(), pnMsg, Toast.LENGTH_SHORT);
            //toast.show();

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    toast.cancel();
                }
            }, 200);

        }
    };

    private void notifyUser(Object message) {

        Message msg = handler.obtainMessage();

        try {
            final String obj = (String) message;
            msg.obj = obj;
            handler.sendMessage(msg);
            Log.i("anu Received msg : ", obj.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onCreate() {
        super.onCreate();
       // Toast.makeText(this, "PubnubService created...", Toast.LENGTH_LONG).show();
        sessionManager = new SessionManager(this);
        channel = sessionManager.getListerChannel();
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "SubscribeAtBoot");
        if (wl != null) {
            wl.acquire();
           // Log.i("PUBNUB", "Partial Wake Lock : " + wl.isHeld());
           // Toast.makeText(this, "Partial Wake Lock : " + wl.isHeld(), Toast.LENGTH_LONG).show();
        }

        Log.i("PUBNUB", "PubnubService created...");
        try {
            pubnub.subscribe(new String[]{channel}, new Callback() {

                @Override
                public void connectCallback(String s, Object o) {
                    super.connectCallback(s, o);
                    notifyUser("Anu CONNECT on channel:" + channel);
                    Utility.printLog("Anu CONNECT Listener channel = " + sessionManager.getListerChannel());
                }

                @Override
                public void disconnectCallback(String s, Object o) {
                    super.disconnectCallback(s, o);
                    notifyUser("Anu DISCONNECT on channel:" + channel);
                }

                @Override
                public void reconnectCallback(String s, Object o) {
                    super.reconnectCallback(s, o);
                    notifyUser("Anu RECONNECT on channel:" + channel);
                }

                @Override
                public void successCallback(String channel, Object message) {

                    NotificationManager notificationManager = (NotificationManager) PubnubService.this.getSystemService(Context.NOTIFICATION_SERVICE);
                    Notification notification;
                    PubnubResponse pubnubResponse;
                    Gson gson = new Gson();
                    pubnubResponse = gson.fromJson(String.valueOf(message), PubnubResponse.class);

                    if ("10".equals(pubnubResponse.getA())) {
                        sessionManager.setPubnubData(String.valueOf(message));
                    }
                    else
                    {
                        if (sessionManager.getBookingIdPublish() < Long.valueOf(pubnubResponse.getBid()))
                        {
                            sessionManager.setBookingIdPublish(Long.valueOf(pubnubResponse.getBid()));
                            publishForNotify(pubnubResponse.getBid());
                        }
                    }

                    notifyUser(channel + " " + message.toString());
                    int icon = R.drawable.ic_launcher;
                    long when = System.currentTimeMillis();

                    String title = PubnubService.this.getString(R.string.app_name);
                    SessionManager sessionManager = new SessionManager(PubnubService.this);
                    Intent notificationIntent;
                    notificationIntent = new Intent(PubnubService.this, MainActivity.class);
                    notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

                    PendingIntent intent = PendingIntent.getActivity(PubnubService.this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);


                    if ("10".equals(pubnubResponse.getA()))
                    {
                        notification = new Notification(icon, "Sorry passenger cancelled your duty.", when);
                        notification.setLatestEventInfo(PubnubService.this, title, "Sorry passenger cancelled your duty.", intent);

                        notification.flags |= Notification.FLAG_AUTO_CANCEL;
                        notification.defaults |= Notification.DEFAULT_SOUND;
                        //notification.defaults |= Notification.DEFAULT_SOUND;
                        // Vibrate if vibrate is enabled
                        notification.defaults |= Notification.DEFAULT_VIBRATE;
                        notificationManager.notify(0, notification);
                    }
                    else if(!sessionManager.getIsInBooking())
                    {
                        notification = new Notification(icon, "Congratulations you got a new booking", when);
                        notification.setLatestEventInfo(PubnubService.this, title, "Congratulations you got a new booking", intent);

                        notification.flags |= Notification.FLAG_AUTO_CANCEL;
                        notification.sound = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.taxina);
                        //notification.defaults |= Notification.DEFAULT_SOUND;
                        // Vibrate if vibrate is enabled
                        notification.defaults |= Notification.DEFAULT_VIBRATE;
                        notificationManager.notify(0, notification);
                    }

                }

                @Override
                public void errorCallback(String channel, Object message) {
                    notifyUser(channel + " " + message.toString());
                }
            });
        } catch (PubnubException e) {
            e.printStackTrace();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (wl != null) {
            wl.release();
            Log.i("PUBNUB", "Partial Wake Lock : " + wl.isHeld());
         //   Toast.makeText(this, "Partial Wake Lock : " + wl.isHeld(), Toast.LENGTH_LONG).show();
            wl = null;
        }
     //   Toast.makeText(this, "PubnubService destroyed...", Toast.LENGTH_LONG).show();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }
    */
/**
     * Method for publish current location to passenger.
     *//*

    public void publishForNotify(String bid)
    {
        Utility utility = new Utility();
        String message ;
        SessionManager sessionManager = new SessionManager(this);
        //{“a”:11,”bid”:123,”receiveDt”:”2015-10-21 15:26:00”}
        message="{\"a\" :\""+11+"\",\"bid\" :\""+bid+"\",\"receiveDt\" :\""+utility.getCurrentGmtTime()+"\"}";
        Utility.printLog("Publish NotifyChannel pubnub= "+sessionManager.getChannelName());
        Utility.printLog("Publish NotifyChannel pubnub= "+message);
        PublishUtility.publish(sessionManager.getChannelName(), message, pubnub);
    }
}
*/
