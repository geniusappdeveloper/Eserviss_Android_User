package com.threembed.utilities;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
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
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

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

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

public class Utility 
{

	//Process dialog

	public static void printLog(String... msg)
	 {
	  String str="";
	  for(String i : msg)
	  {
	   str= str+"\n"+i;
	  }
	  if(false)
	  {
	   Log.i("",str);
	  }
	  
	 }

	public static int getWidth(Context mContext)
	{
		  int width=0;
		  WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
		  Display display = wm.getDefaultDisplay();
		  if(Build.VERSION.SDK_INT >=13){                   
		   Point size = new Point();
		   display.getSize(size);
		   width = size.x;
		  }
		  else{
		   width = display.getWidth();  // deprecated
		  }
		  return width;
	}
		 
		
		public static int getHeight(Context mContext)
		 {
		  int height=0;
		  WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
		  Display display = wm.getDefaultDisplay();
		  if(Build.VERSION.SDK_INT >=13){               
		   Point size = new Point();
		   display.getSize(size);
		   height = size.y;
		  }else
		  {          
		   height = display.getHeight();  // deprecated
		  }
		  return height;      
		 }
	
	
	public static ProgressDialog GetProcessDialog(Activity activity)
	{
		// prepare the dialog box
		ProgressDialog dialog = new ProgressDialog(activity);
		// make the progress bar cancelable
		dialog.setCancelable(false);
		// set a message text
		dialog.setMessage("Please wait...");
		// show it
		return dialog;
	}
	
	public static ProgressDialog GetProcessDialogNew(Activity activity,String msg)
	{
		// prepare the dialog box
		ProgressDialog dialog = new ProgressDialog(activity);
		// make the progress bar cancelable
		dialog.setCancelable(false);
		// set a message text
		dialog.setMessage(msg);

		// show it
		return dialog;
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

	//Http post method

	public static HttpResponse doPost(String url, Map<String, String> kvPairs)
	throws ClientProtocolException, IOException 
	{
		// HttpClient httpclient = new DefaultHttpClient();

		DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
		HttpClient httpclient = defaultHttpClient;

		HttpPost httppost = new HttpPost(url);

		if (kvPairs != null || kvPairs.isEmpty() == false) 
		{
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(kvPairs.size());
			String k, v;
			Iterator<String> itKeys = kvPairs.keySet().iterator();

			while (itKeys.hasNext()) {
				k = itKeys.next();
				v = kvPairs.get(k);
				nameValuePairs.add(new BasicNameValuePair(k, v));
			}

			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		}

		HttpResponse response;
		response = httpclient.execute(httppost);
		Log.i("TAG", "doPost response........."+response);
		return response;
	}

	public static String getCurrentDateTimeStringGMT() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String currentDateTimeString=dateFormat.format(date);
		String currentDateTimeWithformat=Utility.changeDateTimeFormate(currentDateTimeString,"yyyy-MM-dd HH:mm:ss","yyyy-MM-dd HH:mm:ss");
		Log.i("", "onActivityResult flight currentDateTimeWithformat..." + currentDateTimeWithformat);
		Date currentDateTimeDate=Utility.convertStringIntoDate(currentDateTimeWithformat, "yyyy-MM-dd hh:mm:ss");
		Log.i("", "onActivityResult flight currentDateTimeDate..." + currentDateTimeDate);
		String gmtDateTime=Utility.getLocalTimeToGMT(currentDateTimeDate);
		Log.i("", "onActivityResult gmtDateTime..............."+gmtDateTime);
		String currentDateTimeGMT=Utility.changeDateFormate(gmtDateTime,"MM/dd/yyyy HH:mm:ss","yyyy-MM-dd HH:mm:ss");
		Log.i("", "onActivityResult currentDateTimeGMT..............."+currentDateTimeGMT);
		return currentDateTimeGMT;
	}

	public static String changeDateTimeFormate(String inputDate,String inputFormat,String outFormate) {

		String time24 =null;
		try {
			//String now = new SimpleDateFormat("hh:mm aa").format(new java.util.Date().getTime());
			System.out.println("onActivityResult time in 12 hour format : " + inputDate);
			SimpleDateFormat inFormat = new SimpleDateFormat(inputFormat);
			SimpleDateFormat outFormat = new SimpleDateFormat(outFormate);
			time24 = outFormat.format(inFormat.parse(inputDate));
			System.out.println("onActivityResult time in 24 hour format : " + time24);
		} catch (Exception e) {
			System.out.println("Exception : " + e.getMessage());
		}
		return time24;

	}

	public static Date convertStringIntoDate(String dateString,String inputFormat) {
		SimpleDateFormat formatter = new SimpleDateFormat(inputFormat);
		//String dateInString = "7-Jun-2013";
		System.out.println("dateString......."+dateString);
		Date date=null;


		try {
			date = formatter.parse(dateString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(date);
		System.out.println(formatter.format(date));


		return date;
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
			String formattedDate = writeFormat.format( date );
		}
		return writeFormat.format( date );

	}

	public static String getLocalTimeToGMT(Date localTime) {
		//Date will return local time in Java  
		//Date localTime = new Date(); 

		//creating DateFormat for converting time from local timezone to GMT
		DateFormat converter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

		//getting GMT timezone, you can get any timezone e.g. UTC
		converter.setTimeZone(TimeZone.getTimeZone("GMT"));

		System.out.println("local time : " + localTime);;
		System.out.println("time in GMT : " + converter.format(localTime));
		return converter.format(localTime);
		//Read more: http://javarevisited.blogspot.com/2012/04/how-to-convert-local-time-to-gmt-in.html#ixzz2i5QriBRI
	}


	//-------------------------------------------------------------------------------------
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


	//------------------------------------------------------------------------------		
	public String makeHttpRequest(String url, String method,
			List<NameValuePair> params) {

		InputStream is = null;
		// Making HTTP request
		try {

			// check for request method
			if(method == "POST"){
				// request method is POST
				// defaultHttpClient
				DefaultHttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(url);
				httpPost.setEntity(new UrlEncodedFormEntity(params));
				System.out.println("--------Orignal URL-------"+params);

				HttpResponse httpResponse = httpClient.execute(httpPost);
				HttpEntity httpEntity = httpResponse.getEntity();
				is = httpEntity.getContent();

			}
			else if(method == "GET")
			{
				// request method is GET
				HttpParams httpParameters = new BasicHttpParams();
				HttpConnectionParams.setConnectionTimeout(httpParameters, 20000);
				HttpConnectionParams.setSoTimeout(httpParameters, 20000);
				DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
				//  DefaultHttpClient httpClient = new DefaultHttpClient();
				String paramString = URLEncodedUtils.format(params, "utf-8");
				System.out.println("--------Orignal URL-------"+params);
				System.out.println("***paramString***"+paramString);
				url += "?" + paramString;
				System.out.println("***url***"+url);
				HttpGet httpGet = new HttpGet(url);

				HttpResponse httpResponse = httpClient.execute(httpGet);
				HttpEntity httpEntity = httpResponse.getEntity();
				is = httpEntity.getContent();
				Log.e("is^",is.toString());
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
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			//logDebug("makeHttpRequest  resposns  "+sb.toString());
			response=sb.toString();
			//  json = sb.toString();
		} catch (Exception e) 
		{
			Log.e("Buffer Error", "Error converting result " + e.toString());
		}

		return response;

	}



	public static boolean isNetworkAvailable(Context context) 
	{  

		ConnectivityManager connectivity  = null;  
		boolean isNetworkAvail = false;

		try
		{
			connectivity = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

			if (connectivity != null) 
			{       
				NetworkInfo[] info = connectivity.getAllNetworkInfo();

				if (info != null)
				{   
					for (int i = 0; i < info.length; i++) 
					{
						if (info[i].getState() == NetworkInfo.State.CONNECTED) 
						{  

							return true;
						}
					}
				}
			}
			return false;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if(connectivity !=null)
			{
				connectivity = null;
			}
		}
		return isNetworkAvail;
	}

	public static void ShowAlert(String msg,Context context)
	{
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
		
		// set title
		alertDialogBuilder.setTitle("Note:");
		
		// set dialog message
		alertDialogBuilder
			.setMessage(msg)
			.setCancelable(false)
			
			.setNegativeButton("OK",new DialogInterface.OnClickListener() 
			{
				public void onClick(DialogInterface dialog,int id)
				{
					//closing the application
					dialog.dismiss();
				}
			});
		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();
		// show it
		alertDialog.show();
		
	}
	
	public String getCurrentGmtTime()
    {
       String curentdateTime=null;
       
     SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.getDefault());
     curentdateTime = sdf.format(new Date());
       
       return curentdateTime;
    }

			
			  public static File getSdCardPath()
			   {
		    	
			    File sdCardPath=null;
			    sdCardPath=Environment.getExternalStorageDirectory();
			    Log.i("SD CARD PATH",""+ sdCardPath);
			    Log.i("SD CARD PATH",""+ sdCardPath);
			    return sdCardPath;
			   }
			  
			//---------------------------delete non empty directory------------------------------
			   
			   public static boolean deleteNon_EmptyDir(File dir) 
			   {  

			    if (dir.isDirectory()) 
			    {

			     String[] children = dir.list();

			     if (children!=null&&children.length>0) 
			     {

			      for (int i=0; i<children.length; i++)
			      {
			       boolean success = deleteNon_EmptyDir(new File(dir, children[i]));

			       if (!success)
			       {
			        return false;
			       }
			      }
			     }

			    }
			    return dir.delete();
			   }
			   
			   //------------------------------ To create file---------------------------	   
				 
			   public static File createFile(String directoryname,String filename)
			   {
				   
				File createdirectoty,createdFileName;  
			    //Log.i(TAG, "createFile");
			    createdirectoty = new File(Environment.getExternalStorageDirectory() + "/"+directoryname); 
			    //Log.i(TAG, "createFile  createdirectoty "+createdirectoty);
			    if (!createdirectoty.exists())
			    {     //Log.i(TAG, "createFile directory is not created yet");
			    createdirectoty.mkdir();
			    //System.out.println("now signed directory is created succes is "+success);
			    createdFileName = new File(createdirectoty, filename);
			    //Log.i(TAG, "createFile createdFileNamet  "+createdFileName);

			    }
			    else 
			    {    
			     //System.out.println("createFile directory is created already ");
			     //Log.i(TAG, "createFile  directory is created already ");
			     createdFileName = new File(createdirectoty, filename);
			     System.out.println("my signed file is from else block  is  "+createdFileName);
			     ////Log.i(TAG, "createFile createdFileNamet  "+createdFileName);
			     // Do something else on failure 
			    }
			    return createdFileName;
			   }
	
	

}
