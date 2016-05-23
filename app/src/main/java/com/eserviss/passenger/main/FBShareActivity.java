package com.eserviss.passenger.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Button;
import com.flurry.android.FlurryAgent;
import com.egnyt.eserviss.R;
import com.threembed.utilities.Utility;


public class FBShareActivity extends Activity 
{
	Button btn;
	private Context context;
	public static String image_path,chooser,email_address;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		email_address=extras.getString("email_address");
		chooser = extras.getString("chooser");
		
		if(chooser.equals("facebook"))
		{
		  File _sdCard = Utility.getSdCardPath();
          Log.i("SD CARD PATH!!", ""+_sdCard);        
          
          //creating directory
          File _picDir  = new File(_sdCard, getResources().getString(R.string.imagedire));                                
          Utility.deleteNon_EmptyDir(_picDir);
          
          //create file
          File imageFile= Utility.createFile(getResources().getString(R.string.imagedire), (getResources().getString(R.string.imagefilename)+"_twit"+".jpg"));
          
          //get a bitmap object from this image
          Bitmap bm = BitmapFactory.decodeResource( getResources(), R.drawable.ic_launcher);
          OutputStream outStream = null;
          
          try {
                  outStream = new FileOutputStream(imageFile);
          } catch (FileNotFoundException e) 
          {
                  // TODO Auto-generated catch block
                  e.printStackTrace();
          }
          
          bm.compress(Bitmap.CompressFormat.PNG, 100, outStream);
          
          try {
                  outStream.flush();
          } catch (IOException e) {
                  // TODO Auto-generated catch block
                  e.printStackTrace();
          }
      
          try {
                  outStream.close();
          } catch (IOException e) 
          {
                  // TODO Auto-generated catch block
                  e.printStackTrace();
          }
                                          
          //Storing path in a variable "image_path"
          image_path=imageFile.getAbsolutePath();
          //Intent intent=new Intent(FBShareActivity.this, ConnectActivity.class);
        //  intent.putExtra("filePath",image_path);
        //  startActivity(intent);
          //Toast.makeText(getApplicationContext(), "Twiteer selected", Toast.LENGTH_SHORT).show();
          share("facebook",image_path);
		}
		
		if(chooser.equals("twitter"))
		{
		  File _sdCard = Utility.getSdCardPath();
          Log.i("SD CARD PATH!!", ""+_sdCard);        
          
          //creating directory
          File _picDir  = new File(_sdCard, getResources().getString(R.string.imagedire));                                
          Utility.deleteNon_EmptyDir(_picDir);
          
          //create file
          File imageFile= Utility.createFile(getResources().getString(R.string.imagedire), (getResources().getString(R.string.imagefilename)+"_twit"+".jpg"));
          
          //get a bitmap object from this image
          Bitmap bm = BitmapFactory.decodeResource( getResources(), R.drawable.ic_launcher);
          OutputStream outStream = null;
          
          try {
                  outStream = new FileOutputStream(imageFile);
          } catch (FileNotFoundException e) {
                  // TODO Auto-generated catch block
                  e.printStackTrace();
          }
          
          bm.compress(Bitmap.CompressFormat.PNG, 100, outStream);
          
          try {
                  outStream.flush();
          } catch (IOException e) {
                  // TODO Auto-generated catch block
                  e.printStackTrace();
          }
      
          try {
                  outStream.close();
          } catch (IOException e) {
                  // TODO Auto-generated catch block
                  e.printStackTrace();
          }
                                          
          //Storing path in a variable "image_path"
          image_path=imageFile.getAbsolutePath();
          
          //Intent intent=new Intent(FBShareActivity.this, ConnectActivity.class);
        //  intent.putExtra("filePath",image_path);
        //  startActivity(intent);
          //Toast.makeText(getApplicationContext(), "Twiteer selected", Toast.LENGTH_SHORT).show();
          shareTwitter("twitter",image_path);
		}
		/*else
		{
			Toast.makeText(FBShareActivity.this,"Twitter app nicht gefunden", Toast.LENGTH_SHORT).show();
			finish();
		}*/
		
		
		if(chooser.equals("email"))
		{
			/*String mail=email_address;
			Intent email = new Intent(Intent.ACTION_SENDIntent.ACTION_VIEW);
			email.setType("message/rfc822");
			email.putExtra(Intent.EXTRA_EMAIL,mail);    
			email.putExtra(Intent.EXTRA_SUBJECT, "Get rewwarded by using Doodle Points!");
			email.putExtra(Intent.EXTRA_STREAM, image_path);
			email.putExtra(Intent.EXTRA_TEXT, "");
			startActivity(Intent.createChooser(email, "Choose an Email client :"));*/
			File _sdCard = Utility.getSdCardPath();
	          Log.i("SD CARD PATH!!", ""+_sdCard);        
	          
	          //creating directory
	          File _picDir  = new File(_sdCard, getResources().getString(R.string.imagedire));                                
	          Utility.deleteNon_EmptyDir(_picDir);
	          
	          //create file
	          File imageFile= Utility.createFile(getResources().getString(R.string.imagedire), (getResources().getString(R.string.imagefilename)+"_twit"+".jpg"));
	          
	          //get a bitmap object from this image
	          Bitmap bm = BitmapFactory.decodeResource( getResources(), R.drawable.ic_launcher);
	          OutputStream outStream = null;
	          
	          try {
	                  outStream = new FileOutputStream(imageFile);
	          } catch (FileNotFoundException e) {
	                  // TODO Auto-generated catch block
	                  e.printStackTrace();
	          }
	          
	          bm.compress(Bitmap.CompressFormat.PNG, 100, outStream);
	          
	          try {
	                  outStream.flush();
	          } catch (IOException e) {
	                  // TODO Auto-generated catch block
	                  e.printStackTrace();
	          }
	      
	          try {
	                  outStream.close();
	          } catch (IOException e) {
	                  // TODO Auto-generated catch block
	                  e.printStackTrace();
	          }
	                                          
	          //Storing path in a variable "image_path"
	          image_path=imageFile.getAbsolutePath();
			
	          String mail=email_address;
			Uri uri = Uri.fromFile(new File(imageFile.getAbsolutePath()));
			Intent email = new Intent(Intent.ACTION_SEND/*Intent.ACTION_VIEW*/);
			email.setType("message/rfc822");
			//email.putExtra(Intent.EXTRA_EMAIL,emailStrArray);    
			email.putExtra(Intent.EXTRA_SUBJECT,getResources().getString(R.string.app_name));
			email.putExtra(Intent.EXTRA_STREAM, uri);
			email.putExtra(Intent.EXTRA_EMAIL,mail); 
			email.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.share_message));
			startActivity(Intent.createChooser(email, "Choose an Email client :"));	
			finish();
		}
	}
	
	 /**
	    * To share photo with text on facebook
	    * @param nameApp
	    * @param imagePath
	    */
	    void share(String nameApp, String imagePath) 
	    {
	      try
	      {
	          List<Intent> targetedShareIntents = new ArrayList<Intent>();
	          Intent share = new Intent(Intent.ACTION_SEND);
	          share.setType("image/*");
	          List<ResolveInfo> resInfo = getPackageManager().queryIntentActivities(share, 0);
	          if (!resInfo.isEmpty()){
	              for (ResolveInfo info : resInfo) {
	                  Intent targetedShare = new Intent(Intent.ACTION_SEND);
	                  targetedShare.setType("image/*"); // put here your mime type
	                  if (info.activityInfo.packageName.toLowerCase().contains(nameApp) || info.activityInfo.name.toLowerCase().contains(nameApp)) {
	                      targetedShare.putExtra(Intent.EXTRA_SUBJECT, "Sample Photo");
	                     
	                      targetedShare.putExtra(Intent.EXTRA_TEXT,"This photo is created by App Name");
	                      targetedShare.putExtra(Intent.EXTRA_TITLE,"This photo is created by App Name");
	                  
	                      targetedShare.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(imagePath)));
	                      targetedShare.setPackage(info.activityInfo.packageName);
	                      targetedShareIntents.add(targetedShare);
	                  }
	              }
	              Intent chooserIntent = Intent.createChooser(targetedShareIntents.remove(0), "");
	              chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetedShareIntents.toArray(new Parcelable[]{}));
	              startActivity(chooserIntent);
	              finish();
	          }
	      }
	      catch(Exception e){
	          Log.v("VM","Exception while sending image on" + nameApp + " "+  e.getMessage());
	      }
	    }
	
	    	void shareTwitter(String nameApp, String imagePath) {
		      try
		      {
		          List<Intent> targetedShareIntents = new ArrayList<Intent>();
		          Intent share = new Intent(Intent.ACTION_SEND);
		          share.setType("image/*");
		          List<ResolveInfo> resInfo = getPackageManager().queryIntentActivities(share, 0);
		          if (!resInfo.isEmpty()){
		              for (ResolveInfo info : resInfo) 
		              {
		                  Intent targetedShare = new Intent(Intent.ACTION_SEND);
		                  targetedShare.setType("image/*"); // put here your mime type
		                  if (info.activityInfo.packageName.toLowerCase().contains(nameApp) || info.activityInfo.name.toLowerCase().contains(nameApp)) 
		                  {
		                      targetedShare.putExtra(Intent.EXTRA_SUBJECT, "Sample Photo");
		                    //  targetedShare.putExtra(Intent.EXTRA_TEXT,"This photo is created by App Name");
		                      targetedShare.putExtra(Intent.EXTRA_TEXT,getResources().getString(R.string.share_message));
		                      targetedShare.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(imagePath)));
		                      targetedShare.setPackage(info.activityInfo.packageName);
		                      targetedShareIntents.add(targetedShare);
		                  }
		              }
		              Intent chooserIntent = Intent.createChooser(targetedShareIntents.remove(0), "");
		              chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetedShareIntents.toArray(new Parcelable[]{}));
		              startActivity(chooserIntent);
		              finish();
		          }
		      }
		      catch(Exception e){
		          Log.v("VM","Exception while sending image on" + nameApp + " "+  e.getMessage());
		      }
		    }
	    
	    @Override
	    public void onBackPressed() 
	    {
	    	// TODO Auto-generated method stub
	    super.onBackPressed();
	    finish();
	    }
	    
	    
	    public static String getImagePath()
	    {
	    	return image_path;
	    }
	    
	    
	    @Override
		protected void onStart()
		{
			super.onStart();
			FlurryAgent.onStartSession(this, "8c41e9486e74492897473de501e087dbc6d9f391");
		}
		 
		@Override
		protected void onStop()
		{
			super.onStop();		
			FlurryAgent.onEndSession(this);
		}
	    
	
}


/*public class FBShareActivity extends Activity 
{
	Button btn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.sharing_to_socialweb);
		//btn=(Button)findViewById(R.id.button1);
		
		String urlToShare = "http://doodle-points.com/images/logo.png";
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		//intent.putExtra(Intent.EXTRA_TEXT, urlToShare+"Entdecke auch du Doodle Points und staube viele Geschenke an! Doodle Points ist einen revolutionäre Treuekarte, welche alle Treuekarten in einer App vereint. Scannen, Sammeln, Einlösen! \n via Doodle Points");
		//intent.putExtra(Intent.EXTRA_TEXT, "Entdecke auch du Doodle Points und staube viele Geschenke an! Doodle Points ist einen revolutionäre Treuekarte, welche alle Treuekarten in einer App vereint. Scannen, Sammeln, Einlösen! \n via Doodle Points");
		intent.putExtra(Intent.EXTRA_TEXT, urlToShare);

		// See if official Facebook app is found
		boolean facebookAppFound = false;
		List<ResolveInfo> matches = getPackageManager().queryIntentActivities(intent, 0);
		int i=0;
		for (ResolveInfo info : matches) 
		{
	    	Utility.logdebug("", "com. is find ="+matches.get(i++));

		    if (info.activityInfo.packageName.toLowerCase().startsWith("com.facebook")) 
		    {
		    	Utility.logdebug("", "com.facebook is find");
		        intent.setPackage(info.activityInfo.packageName);
		        facebookAppFound = true;
		        break;
		    }
		    if(info.activityInfo.packageName.toLowerCase().startsWith("com.twitter"))
		    {
		    	Utility.logdebug("", "com.twitter is find");
		    }
		    
		}
		//If facebook app not found, load sharer.php in a browser
		if (!facebookAppFound) 
		{
		    String sharerUrl = "http://doodle-points.com/images/logo.png";
		    intent = new Intent(Intent.ACTION_VIEW, Uri.parse(sharerUrl));
		}
		startActivity(intent);
		finish();
		
	}
}*/

