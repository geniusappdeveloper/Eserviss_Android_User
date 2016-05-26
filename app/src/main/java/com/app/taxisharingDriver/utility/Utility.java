package com.app.taxisharingDriver.utility;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

@SuppressLint("SimpleDateFormat")
public class Utility 
{
	public static void hideSoftKeyboard(Activity activity) 
	{
		InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
	}
	public  static boolean validateEmail(String email)
	{
		boolean isValid = false;


		String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
		CharSequence inputStr = email;

		Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(inputStr);
		if (matcher.matches())
		{
			isValid = true;
		}

		return isValid;
	}
	public static String getDeviceId(Context context)
	{
		TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		return telephonyManager.getDeviceId();

		//return "123456789101112DummyId2";
	}
	@SuppressLint("SimpleDateFormat")
	public String getCurrentGmtTime()
	{
		String curentdateTime=null;

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		curentdateTime = sdf.format(new Date());

		return curentdateTime;
	}
	//Process dialog

	public static ProgressDialog GetProcessDialog(Activity activity)
	{
		// prepare the dialog box
		ProgressDialog dialog = new ProgressDialog(activity);
		// make the progress bar cancelable
		dialog.setCancelable(true);
		// set a message text
		dialog.setMessage("Loading...");

		// show it
		return dialog;
	}

	public  RelativeLayout.LayoutParams  getRelativelayoutParams(int width,int height)
	{
		RelativeLayout.LayoutParams lp=null;

		lp = new RelativeLayout.LayoutParams(/*RelativeLayout.LayoutParams.WRAP_CONTENT*/width, /*RelativeLayout.LayoutParams.WRAP_CONTENT*/height);


		return lp;
	} 

	public LinearLayout.LayoutParams getLinearLayoutParams(int width,int height)
	{
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width,height);
		return params;
	}
	public static void printLog(String... msg)
	{
		String str="";
		for(String i : msg)
		{
			str= str+"\n"+i;
		}
		if(true)
		{
			Log.i("DriverApp",str);
		}

	}
	public void displayMessageAndExit(Activity activity,String tiltleMassage,String message)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setTitle(tiltleMassage);
		builder.setMessage(message);
		builder.setPositiveButton("Ok", new FinishListener(activity));
		builder.setOnCancelListener(new FinishListener(activity));
		builder.setCancelable(false);
		builder.show();
	}
	public static void ShowAlert(String msg, Context context) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
		alertDialogBuilder.setTitle("Note");
		alertDialogBuilder.setMessage(msg).setCancelable(false)
		.setNegativeButton("ok", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();

	}
	public static boolean isNetworkAvailable(Context context) {

		ConnectivityManager connectivity = null;
		boolean isNetworkAvail = false;
		try {
			connectivity = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectivity != null) {
				NetworkInfo[] info = connectivity.getAllNetworkInfo();
				if (info != null) {
					for(int i = 0; i < info.length; i++)
						if (info[i].getState() == NetworkInfo.State.CONNECTED) 
						{
							return true;
						}
				}
			}
			return false;
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		finally{
			if (connectivity != null) {
				connectivity = null;
			}
		}
		return isNetworkAvail;
	}

	/**
	 * Show dialog confirm.
	 * 
	 * @param activity
	 *            the activity
	 * @param title
	 *            the title
	 * @param message
	 *            the message
	 * @param flag
	 *            the flag
	 * @return the alert dialog
	 */
	@SuppressWarnings("deprecation")
	public AlertDialog showDialogConfirm(final Activity activity, String title,
			String message, final boolean flag) 
	{
		AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
		// activity.getWindow().setBackgroundDrawableResource(R.color.orange);
		//alertDialog.setIcon(R.drawable.dialog_icon);
		alertDialog.setTitle(title);
		alertDialog.setMessage(message);
		alertDialog.setButton("OK" , new DialogInterface.OnClickListener() {
			@Override
			public void onClick(final DialogInterface dialog, final int which) 
			{
				dialog.dismiss();
				/*if (flag) 
					{
						//activity.finish();
					} 
					else 
					{
						return;
					}*/

			}
		});
		alertDialog.setCancelable(false);
		return alertDialog;
	}

	@SuppressLint("SimpleDateFormat")
	public static String getCurrentDateTimeStringGMT() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String currentDateTimeString=dateFormat.format(date);
		String currentDateTimeWithformat=Utility.changeDateTimeFormate(currentDateTimeString,"yyyy-MM-dd HH:mm:ss","yyyy-MM-dd HH:mm:ss");
		//Log.i("", "onActivityResult flight currentDateTimeWithformat..." + currentDateTimeWithformat);
		Date currentDateTimeDate=Utility.convertStringIntoDate(currentDateTimeWithformat, "yyyy-MM-dd hh:mm:ss");
		//Log.i("", "onActivityResult flight currentDateTimeDate..." + currentDateTimeDate);
		String gmtDateTime=Utility.getLocalTimeToGMT(currentDateTimeDate);
		//Log.i("", "onActivityResult gmtDateTime..............."+gmtDateTime);
		String currentDateTimeGMT=Utility.changeDateFormate(gmtDateTime,"MM/dd/yyyy HH:mm:ss","yyyy-MM-dd HH:mm:ss");
		//Log.i("", "onActivityResult currentDateTimeGMT..............."+currentDateTimeGMT);
		return currentDateTimeGMT;
	}

	@SuppressLint("SimpleDateFormat")
	public static String changeDateTimeFormate(String inputDate,String inputFormat,String outFormate) {

		String time24 =null;
		try {
			//String now = new SimpleDateFormat("hh:mm aa").format(new java.util.Date().getTime());
			//System.out.println("onActivityResult time in 12 hour format : " + inputDate);
			SimpleDateFormat inFormat = new SimpleDateFormat(inputFormat);
			SimpleDateFormat outFormat = new SimpleDateFormat(outFormate);
			time24 = outFormat.format(inFormat.parse(inputDate));
			//System.out.println("onActivityResult time in 24 hour format : " + time24);
		} catch (Exception e) {
			//System.out.println("Exception : " + e.getMessage());
		}
		return time24;

	}

	@SuppressLint("SimpleDateFormat")
	public static Date convertStringIntoDate(String dateString,String inputFormat) {
		SimpleDateFormat formatter = new SimpleDateFormat(inputFormat);
		//String dateInString = "7-Jun-2013";
		//System.out.println("dateString......."+dateString);
		Date date=null;


		try {
			date = formatter.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		//System.out.println(date);
		//System.out.println(formatter.format(date));


		return date;
	}

	public static String getLocalTimeToGMT(Date localTime) 
	{
		//Date will return local time in Java  
		//Date localTime = new Date(); 

		//creating DateFormat for converting time from local timezone to GMT
		DateFormat converter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

		//getting GMT timezone, you can get any timezone e.g. UTC
		converter.setTimeZone(TimeZone.getTimeZone("GMT"));

		//System.out.println("local time : " + localTime);;
		//System.out.println("time in GMT : " + converter.format(localTime));
		return converter.format(localTime);
		//Read more: http://javarevisited.blogspot.com/2012/04/how-to-convert-local-time-to-gmt-in.html#ixzz2i5QriBRI
	}

	public static String changeDateFormate(String inputDate,String inputFormate,String outputFormate) {
		//String dateStr = "Jul 27, 2011 8:35:29 AM";
		DateFormat readFormat = new SimpleDateFormat(inputFormate);
		DateFormat writeFormat = new SimpleDateFormat(outputFormate);
		Date date = null;
		try
		{
			date = readFormat.parse( inputDate );
		}
		catch ( ParseException e )
		{
			e.printStackTrace();
		}
		if( date != null )
		{
			@SuppressWarnings("unused")
			String formattedDate = writeFormat.format( date );
		}
		return writeFormat.format( date );

	}

	public String getImageHostUrl(Activity activity)
	{
		//////Log.i(TAG, "getImageHeightAndWidth");

		String devieytype="";
		int screenHeight = getHeight(activity);
		int screenWidth=getWidth(activity);
		//////Log.i(TAG, "getImageHeightAndWidth  screenHeight "+screenHeight);
		//////Log.i(TAG, "getImageHeightAndWidth  screenWidth  "+screenWidth);
		//	int imagehiegth;
		//int imagewidth;
		if ((screenHeight <= 500 && screenHeight >= 480)&& (screenWidth <= 340 && screenWidth >= 300))
		{
			//////Log.i(TAG, "getImageHeightAndWidth mdpi");
			devieytype=VariableConstants.ImageUrl+"mdpi/";

		}

		else if ((screenHeight <= 400 && screenHeight >= 300)&& (screenWidth <= 240 && screenWidth >= 220))

		{

			//////Log.i(TAG, "getImageHeightAndWidth ldpi");
			devieytype=VariableConstants.ImageUrl+"ldpi/";

		}

		else if ((screenHeight <= 840 && screenHeight >= 780)&& (screenWidth <= 500 && screenWidth >= 440)) 
		{

			//////Log.i(TAG, "getImageHeightAndWidth hdpi");
			devieytype=VariableConstants.ImageUrl+"hdpi/";

		}
		else if ((screenHeight <= 1280 && screenHeight >= 840)&& (screenWidth <= 720 && screenWidth >= 500)) 
		{

			//////Log.i(TAG, "getImageHeightAndWidth xdpi");

			devieytype=VariableConstants.ImageUrl+"xhdpi/";
		}
		else if((screenHeight <= 1920&& screenHeight >= 1280)&& (screenWidth <= 1080 && screenWidth >= 720)) 
		{

			devieytype=VariableConstants.ImageUrl+"xxhdpi/";

		}
		else 
		{
			devieytype=VariableConstants.ImageUrl+"xxhdpi/";
		}

		return devieytype;
	}

	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	public static int getWidth(Context mContext){
		int width=0;
		WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		if(Build.VERSION.SDK_INT>12){                   
			Point size = new Point();
			display.getSize(size);
			width = size.x;
		}
		else{
			width = display.getWidth();  // deprecated
		}
		return width;
	}

	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	public static int getHeight(Context mContext)
	{
		int height=0;
		WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		if(Build.VERSION.SDK_INT>12){               
			Point size = new Point();
			display.getSize(size);
			height = size.y;
		}else
		{          
			height = display.getHeight();  // deprecated
		}
		return height;      
	}

	public String makeHttpRequest(String url, String method,
			List<NameValuePair> params) 
	{

		InputStream is = null;
		// Making HTTP request
		try {

			// check for request method
			if(method.equals("POST"))
			{
				// request method is POST
				// defaultHttpClient
				DefaultHttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(url);
				httpPost.setEntity(new UrlEncodedFormEntity(params));
				//System.out.println("--------Orignal URL-------"+params);

				HttpResponse httpResponse = httpClient.execute(httpPost);
				HttpEntity httpEntity = httpResponse.getEntity();
				is = httpEntity.getContent();

			}
			else if(method.equals("GET"))
			{
				// request method is GET
				HttpParams httpParameters = new BasicHttpParams();
				HttpConnectionParams.setConnectionTimeout(httpParameters, 20000);
				HttpConnectionParams.setSoTimeout(httpParameters, 20000);
				DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
				//  DefaultHttpClient httpClient = new DefaultHttpClient();
				String paramString = URLEncodedUtils.format(params, "utf-8");
				//System.out.println("--------Orignal URL-------"+params);
				//System.out.println("***paramString***"+paramString);
				url += "?" + paramString;
				//System.out.println("***url***"+url);
				HttpGet httpGet = new HttpGet(url);

				HttpResponse httpResponse = httpClient.execute(httpGet);
				HttpEntity httpEntity = httpResponse.getEntity();
				is = httpEntity.getContent();
				//Log.e("is^",is.toString());
			}            

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		String response=null;
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader( is));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null)
			{
				sb.append(line + "\n");
			}

			is.close();
			//logDebug("makeHttpRequest  resposns  "+sb.toString());
			response=sb.toString();
			//  json = sb.toString();
		} 
		catch(NullPointerException ex)
		{
			//Log.e("Buffer Error", "Error converting result " + ex.toString());
		}
		catch (Exception e) 
		{
			//Log.e("Buffer Error", "Error converting result " + e.toString());
		}


		// try parse the string to a JSON object
		//		        try 
		//		        {
		//		            jObj = new JSONObject(json);
		//		        } 
		//		        catch (JSONException e)
		//		        {
		//		            Log.e("JSON Parser", "Error parsing data " + e.toString());
		//		        }
		// 
		// return JSON String
		//   return jObj;
		return response;

	}

	public  List<NameValuePair> getAppointmentStatusParameter(String [] params)
	{
		List<NameValuePair> namevaluepairs = new ArrayList<NameValuePair>();
		namevaluepairs.add(new BasicNameValuePair("ent_sess_token",params[0]));
		namevaluepairs.add(new BasicNameValuePair("ent_dev_id",params[1]));
		namevaluepairs.add(new BasicNameValuePair("ent_pat_email",params[2]));
		namevaluepairs.add(new BasicNameValuePair("ent_appnt_dt",params[3]));
		namevaluepairs.add(new BasicNameValuePair("ent_response",params[4]));
		namevaluepairs.add(new BasicNameValuePair("ent_doc_remarks",params[5]));
		namevaluepairs.add(new BasicNameValuePair("ent_date_time",params[6]));

		return namevaluepairs;
	}

	public  List<NameValuePair> getAppointmentHistoryParameter(String [] params)
	{
		List<NameValuePair> namevaluepairs = new ArrayList<NameValuePair>();
		namevaluepairs.add(new BasicNameValuePair("ent_sess_token",params[0]));
		namevaluepairs.add(new BasicNameValuePair("ent_dev_id",params[1]));
		namevaluepairs.add(new BasicNameValuePair("ent_pat_email",params[2]));
		namevaluepairs.add(new BasicNameValuePair("ent_page",params[3]));
		namevaluepairs.add(new BasicNameValuePair("ent_date_time",params[4]));

		return namevaluepairs;
	}
	public  List<NameValuePair> getLogoutParameter(String [] params)
	{
		List<NameValuePair> namevaluepairs = new ArrayList<NameValuePair>();
		namevaluepairs.add(new BasicNameValuePair("ent_sess_token",params[0]));
		namevaluepairs.add(new BasicNameValuePair("ent_dev_id",params[1]));
		namevaluepairs.add(new BasicNameValuePair("ent_user_type",params[2]));
		namevaluepairs.add(new BasicNameValuePair("ent_date_time",params[3]));

		return namevaluepairs;
	}
	public  List<NameValuePair> getResetPasswordParameter(String [] params)
	{
		List<NameValuePair> namevaluepairs = new ArrayList<NameValuePair>();
		namevaluepairs.add(new BasicNameValuePair("ent_sess_token",params[0]));
		namevaluepairs.add(new BasicNameValuePair("ent_dev_id",params[1]));
		namevaluepairs.add(new BasicNameValuePair("ent_date_time",params[2]));

		return namevaluepairs;
	}

	public  List<NameValuePair> getUploadParameter(String [] params)
	{
		List<NameValuePair> namevaluepairs = new ArrayList<NameValuePair>();
		namevaluepairs.add(new BasicNameValuePair("ent_sess_token",params[0]));
		namevaluepairs.add(new BasicNameValuePair("ent_dev_id",params[1]));
		namevaluepairs.add(new BasicNameValuePair("ent_snap_name",params[2]));
		namevaluepairs.add(new BasicNameValuePair("ent_snap_chunk",params[3]));
		namevaluepairs.add(new BasicNameValuePair("ent_upld_from",params[4]));
		namevaluepairs.add(new BasicNameValuePair("ent_snap_type",params[5]));
		namevaluepairs.add(new BasicNameValuePair("ent_offset",params[6]));
		namevaluepairs.add(new BasicNameValuePair("ent_date_time",params[7]));

		return namevaluepairs;
	}

	public boolean isMyServiceRunning(Class<?> serviceClass ,Activity activity) 
	{
		ActivityManager manager = (ActivityManager)activity.getSystemService(Context.ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) 
		{
			if (serviceClass.getName().equals(service.service.getClassName())) 
			{
				return true;
			}
		}
		return false;
	}	
	public static String callhttpRequest(String url) {
		System.out.println("utility url..."+url);
		url=url.replaceAll(" ", "%20");
		String resp = null;
	        	HttpGet httpRequest;
				try {
					httpRequest = new HttpGet(url);
					HttpParams httpParameters = new BasicHttpParams();
		        	int timeoutConnection = 60000;
		        	HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);		  	
		        	int timeoutSocket = 60000;
		        	HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);		        			        			        	
	        	HttpClient httpClient = new DefaultHttpClient(httpParameters);	        	
	        	HttpResponse response = httpClient.execute(httpRequest);
	        	HttpEntity entity = response.getEntity();
	        	BufferedHttpEntity bufHttpEntity = new BufferedHttpEntity(entity);
	        	final long contentLength = bufHttpEntity.getContentLength();
	        	if ((contentLength >= 0)) 
	        	{
	        		InputStream is = bufHttpEntity.getContent();
	        		int tobeRead = is.available();
	        		System.out.println("Utility callhttpRequest tobeRead.."+tobeRead);
	        		ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
	        		int ch;
	        			
	        			while ((ch = is.read()) != -1)
	        			{ 
	        				bytestream.write(ch);
	        			}

	        			resp = new String(bytestream.toByteArray());
	        			System.out.println("Utility callhttpRequest resp.."+resp);
	        	}
				} catch (MalformedURLException e) {
					System.out.println("Utility callhttpRequest.."+e);
					e.printStackTrace();
				} catch (ClientProtocolException e) {
					System.out.println("Utility callhttpRequest.."+e);
					e.printStackTrace();
				} catch (IOException e) {
					System.out.println("Utility callhttpRequest.."+e);
					e.printStackTrace();
				}catch (Exception e) {
					System.out.println("Utility Exception.."+e);
				}
		return resp;
	}
 
}
