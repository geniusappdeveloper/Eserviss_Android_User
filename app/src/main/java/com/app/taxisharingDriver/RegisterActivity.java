package com.app.taxisharingDriver;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.NameValuePair;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.taxisharingDriver.pojo.CarType;
import com.app.taxisharingDriver.pojo.CarTypeDetail;
import com.app.taxisharingDriver.pojo.CompanyType;
import com.app.taxisharingDriver.pojo.CompanyTypeDetail;
import com.app.taxisharingDriver.pojo.UploadImgeResponse;
import com.app.taxisharingDriver.response.EmailValidateResponse;
import com.app.taxisharingDriver.response.SignupResponse;
import com.app.taxisharingDriver.utility.ConnectionDetector;
import com.app.taxisharingDriver.utility.LocationUtil;
import com.app.taxisharingDriver.utility.NetworkConnection;
import com.app.taxisharingDriver.utility.NetworkNotifier;
import com.app.taxisharingDriver.utility.SessionManager;
import com.app.taxisharingDriver.utility.Utility;
import com.app.taxisharingDriver.utility.VariableConstants;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.gson.Gson;

import eu.janmuller.android.simplecropimage.CropImage;

/**
 * @author admin-pc
 *
 */

public class RegisterActivity extends FragmentActivity implements OnClickListener,NetworkNotifier,OnTouchListener,OnFocusChangeListener,OnCheckedChangeListener
{
	public String TAG = "RegisterActivity";
	private EditText firstNmameEditext;
	private EditText lastNameEditext;
	private RelativeLayout network_bar;
	private TextView network_text;
	private EditText mobileNumberEditText;
	private EditText emailIdEditText;
	private EditText passwordEditext;
	private ImageView profile_image;
	private TextView accepttermandconditiontextview;
	private Button registerButton;
	private int up=0,low=0,no=0,spl=0,xtra=0,len=0,points=0,max=8;
	private char c;
	private boolean isDialoueShowing;
	private boolean mandantfieldcheck;
	private int [] editTextArray={0,0,0,0,0,0,0};
	private android.widget.CheckBox remembercheckbox;
	private Animation animation;
	private boolean isPictureTaken;
	private boolean iscompanyFound;
	private boolean isPushTokenFound;
	private final int REQUEST_CODE_GALLERY      = 0x1;
	private final int REQUEST_CODE_TAKE_PICTURE = 0x2;
	private final int REQUEST_CODE_CROP_IMAGE   = 0x3;
	private File mFileTemp;
	TextView mDisplay;
	GoogleCloudMessaging gcm;
	AtomicInteger msgId = new AtomicInteger();
	SharedPreferences prefs;
	Context context;

	String regid;
	// Context context;
	private ConnectionDetector cd;
	private String deviceid;

	private LocationUtil locationUtil;
	
	public static final String EXTRA_MESSAGE = "message";
	public static final String PROPERTY_REG_ID = "registration_id";
	private static final String PROPERTY_APP_VERSION = "appVersion";
	private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	

	/**
	 * Substitute you own sender ID here. This is the project number you got
	 * from the API Console, as described in "Getting Started."
	 */
	 String SENDER_ID = VariableConstants.PROJECT_ID;
	/**
	 * Tag used on log messages.
	 */

	private CompanyType companyType;
	private java.util.ArrayList<CompanyTypeDetail>comapanyTypeDetails;
	private String companyTypeid;
	private CarType carType;
	private java.util.ArrayList<CarTypeDetail>carTypeDetailslist;
	private String carTypeid;
	private  ProgressDialog mdialog;
	@Override
	protected void onCreate(Bundle arg0)
	{
		super.onCreate(arg0);
		setContentView(R.layout.activity_register);
		initActionBar();
		initLayoutId();
		
		locationUtil = new LocationUtil(this, this);
		
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state))
		{
			mFileTemp = new File(Environment.getExternalStorageDirectory(), VariableConstants.TEMP_PHOTO_FILE_NAME);
		}
		else 
		{
			mFileTemp = new File(getFilesDir(),VariableConstants.TEMP_PHOTO_FILE_NAME);
		}

		checkingNetworkState();
		context = getApplicationContext();
		// Check device for Play Services APK.
		cd = new ConnectionDetector(getApplicationContext());
		if (cd.isConnectingToInternet())
		{

			if (gcm == null)
			{
				Utility.printLog("===", "gcm IS null"+gcm);
				gcm=GoogleCloudMessaging.getInstance(RegisterActivity.this);
			}
			//regid=session.getRegistrationId();
			Utility.printLog("", "BackgroundForUpdateToken login regid test ......."+regid);
			if (regid==null) 
			{
				deviceid=getDeviceId(context);
				getRegistrationId(RegisterActivity.this);
			} else {
				deviceid=getDeviceId(context);
			}

			Utility.printLog("", "doInBackground regid.........."+regid);
			Utility.printLog("===", "doInBackground deviceid"+deviceid);

		}
		else 
		{

		}

	}
	
	public static String getDeviceId(Context context)
	{
		TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		return telephonyManager.getDeviceId();

	}
	/**
	 * Check the device to make sure it has the Google Play Services APK. If
	 * it doesn't, display a dialog that allows users to download the APK from
	 * the Google Play Store or enable it in the device's system settings.
	 */
	private boolean checkPlayServices() 
	{
		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		if (resultCode != ConnectionResult.SUCCESS) 
		{
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) 
			{
				GooglePlayServicesUtil.getErrorDialog(resultCode, this,PLAY_SERVICES_RESOLUTION_REQUEST).show();
			} 
			else 
			{
				finish();
			}
			return false;
		}
		return true;
	}

	/** 
	 * Method for Profile image 
	 * Option Gallery or Camera
	 */

	private void photoChooseOption()
	{
		final Dialog deleteDialog = new Dialog(this);
		//deleteDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		deleteDialog.setTitle(getResources().getString(R.string.selectphoto));
		deleteDialog.setCanceledOnTouchOutside(false);
		deleteDialog.setContentView(R.layout.profile_setting_layout);

		((Button) (deleteDialog.findViewById(R.id.camera))).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				takePicture();
				deleteDialog.dismiss();
			}
		});

		((Button) (deleteDialog.findViewById(R.id.gallery))).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v)
			{
				openGallery();
				deleteDialog.dismiss();
			}
		});
		Button removePhoto= (Button) deleteDialog.findViewById(R.id.removephoto);
		View line=deleteDialog.findViewById(R.id.line4);
		if(isPictureTaken){
			removePhoto.setVisibility(View.VISIBLE);
			line.setVisibility(View.VISIBLE);
		}

		removePhoto.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				profile_image.setImageResource(R.drawable.signupscreen_default_profile_pic);
				isPictureTaken = false;
				deleteDialog.dismiss();
			}
		});

		((Button) (deleteDialog.findViewById(R.id.cancel))).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				deleteDialog.dismiss();
			}
		});
		deleteDialog.show();
	}

	private void openGallery() 
	{
		Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
		photoPickerIntent.setType("image/*");
		startActivityForResult(photoPickerIntent, REQUEST_CODE_GALLERY);
	}

	private void takePicture() 
	{
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		try {
			Uri mImageCaptureUri = null;
			String state = Environment.getExternalStorageState();
			if (Environment.MEDIA_MOUNTED.equals(state)) {

				mImageCaptureUri = Uri.fromFile(mFileTemp);
			}
			else 
			{
				//mImageCaptureUri = InternalStorageContentProvider.CONTENT_URI;
			}	
			intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
			intent.putExtra("return-data", true);
			startActivityForResult(intent, REQUEST_CODE_TAKE_PICTURE);
		} catch (ActivityNotFoundException e) {

			//Utility.printLog("cannot take picture"+e);
		}
	}

	private void startCropImage() 
	{	 
		Utility.printLog("Strat crop");
		Intent intent = new Intent(this, CropImage.class);
		intent.putExtra(CropImage.IMAGE_PATH, mFileTemp.getPath());
		intent.putExtra(CropImage.SCALE, true);

		intent.putExtra(CropImage.ASPECT_X, 4);
		intent.putExtra(CropImage.ASPECT_Y, 4);

		startActivityForResult(intent, REQUEST_CODE_CROP_IMAGE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{

		if (resultCode != RESULT_OK)
		{
			return;
		}

		Bitmap bitmap;

		switch (requestCode)
		{
		case REQUEST_CODE_GALLERY:

			try 
			{
				InputStream inputStream = getContentResolver().openInputStream(data.getData());
				FileOutputStream fileOutputStream = new FileOutputStream(mFileTemp);
				copyStream(inputStream, fileOutputStream);
				fileOutputStream.close();
				inputStream.close();
				startCropImage();

			} catch (Exception e) 
			{
				 Utility.printLog("Error while creating temp file"+e);
			}

			break;
		case REQUEST_CODE_TAKE_PICTURE:
			startCropImage();
			break;
		case REQUEST_CODE_CROP_IMAGE:

			String path = data.getStringExtra(CropImage.IMAGE_PATH);
			Utility.printLog("", "path fileOutputStream "+path);


			if (path == null)
			{
				return;
			}

			bitmap = BitmapFactory.decodeFile(mFileTemp.getPath());
			profile_image.setImageBitmap(bitmap);
			isPictureTaken=true;
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	public static void copyStream(InputStream input, OutputStream output)
			throws IOException 
			{
		byte[] buffer = new byte[1024];
		int bytesRead;
		while ((bytesRead = input.read(buffer)) != -1) 
		{
			output.write(buffer, 0, bytesRead);
		}
			}

	/**
	 * @return Application's version code from the {@code PackageManager}.
	 * @param context
	 */
	private static int getAppVersion(Context context) 
	{
		try 
		{
			PackageInfo packageInfo = context.getPackageManager()
					.getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionCode;
		}
		catch (NameNotFoundException e)
		{
			// should never happen
			throw new RuntimeException("Could not get package name: " + e);
		}
	}

	/**
	 * @return Application's {@code SharedPreferences}.
	 * @param context
	 */
	private SharedPreferences getGCMPreferences(Context context) 
	{
		// This sample app persists the registration ID in shared preferences, but
		// how you store the regID in your app is up to you.
		return getSharedPreferences(LoginActivity.class.getSimpleName(), Context.MODE_PRIVATE);
	}

	@SuppressLint("NewApi")
	private String getRegistrationId(Context context) 
	{
		final SharedPreferences prefs = getGCMPreferences(this);
		String registrationId = prefs.getString(PROPERTY_REG_ID, "");

		// Check if app was updated; if so, it must clear the registration ID
		// since the existing regID is not guaranteed to work with the new
		// app version.

		int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
		int currentVersion = getAppVersion(this);

		Utility.printLog("APP registered VERSION:"+registeredVersion,"APP current VERSION"+currentVersion);
		if (registeredVersion != currentVersion) 
		{
			new GCMRegistration().execute();
			return "";
		}
		else
		{
			if (registrationId.isEmpty()) 
			{
				new GCMRegistration().execute();
				return "";
			}
			else
			{
				regid = registrationId;
				Utility.printLog("APP registrationId"+regid);
			}
		}

		return regid;

	} 

	private void registerInBackground()
	{
		new GCMRegistration().execute();
	}
	private class GCMRegistration extends AsyncTask<Void, Void, Void>
	{

		@Override
		protected Void doInBackground(Void... params) {

			//String msg = "";
			//logDebug("GCMRegistration doInBackground  ");
			try {
				if (gcm == null)
				{
					gcm = GoogleCloudMessaging.getInstance(context);
					//logDebug("GCMRegistration  gcm "+gcm);
				}
				regid = gcm.register(SENDER_ID);

				String regidfoundseccessfully="getGoogleRegistrationId";


			} 
			catch (IOException ex)
			{

			}
			return null;
		
		}
		@Override
		protected void onPostExecute(Void result) 
		{	
			super.onPostExecute(result);

			if(regid==null||"".equals(regid))
			{
				Utility.printLog("There is no REGISTRATION ID");

			}
			else
			{
				Utility.printLog("REGISTRATION ID IS"+regid);
			}

		}


	}

	/**
	 * Stores the registration ID and app versionCode in the application's
	 * {@code SharedPreferences}.
	 *
	 * @param context application's context.
	 * @param regId registration ID
	 */
	private void storeRegistrationId(Context context, String regId) 
	{
		final SharedPreferences prefs = getGCMPreferences(context);
		int appVersion = getAppVersion(context);
		//  Log.i(TAG, "Saving regId on app version " + appVersion);
		//  logDebug("Saving regId on app version " + appVersion);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(PROPERTY_REG_ID, regId);
		editor.putInt(PROPERTY_APP_VERSION, appVersion);
		editor.commit();
	}
	private void getCompanyType()
	{
		Utility utility=new Utility();
		//		isfirstTimeUpdatcalled=true;
		ConnectionDetector connectionDetector=new ConnectionDetector(RegisterActivity.this);
		if (connectionDetector.isConnectingToInternet()) 
		{
			String	url = VariableConstants.GET_COMPANY_TYPE;
			final ProgressDialog mdialog;
			mdialog=Utility.GetProcessDialog(RegisterActivity.this);
			mdialog.setMessage(getResources().getString(R.string.Pleasewaitmessage));
			mdialog.show();
			mdialog.setCancelable(false);
			RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);  // this = context

			// prepare the Request
			JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
					new Response.Listener<JSONObject>()
					{
				@Override
				public void onResponse(JSONObject response) 
				{  
					mdialog.dismiss();
					mdialog.cancel();
					Utility.printLog("Response"+response);
					// display response  
					Gson gson = new Gson();
					companyType=gson.fromJson(response.toString(), CompanyType.class);
					if (Integer.parseInt(companyType.getErrFlag())==0&&Integer.parseInt(companyType.getErrNum())==21)
					{
						iscompanyFound=true;
						comapanyTypeDetails=companyType.getCompanyTypeDeataiList();
					}
					else 
					{
						iscompanyFound=false;
					}

				}
					},
					new Response.ErrorListener()
					{
						@Override
						public void onErrorResponse(VolleyError error) 
						{     iscompanyFound=true;
						mdialog.dismiss();
						mdialog.cancel();
						Utility.printLog("Error.Response", ""+error);
						}
					}
					);

			int socketTimeout = 60000;//60 seconds - change to what you want
			RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
			getRequest.setRetryPolicy(policy);
			queue.add(getRequest);

		}
		else 
		{
			utility.showDialogConfirm(RegisterActivity.this,"Alert"," working internet connection required", false).show();
			//utility.displayMessageAndExit(getActivity(),"Alert"," working internet connection required");

		}
	}


	@Override
	public boolean onOptionsItemSelected(android.view.MenuItem item) 
	{
		switch (item.getItemId()) 
		{
		case android.R.id.home:
			/*Intent intent=new Intent(RegisterActivity.this, SplahsActivity.class);
		    Bundle bundle=new Bundle();
		    bundle.putBoolean("isFirstrime", false);
		    intent.putExtras(bundle);
		    startActivity(intent);*/
	  		 finish();
	         return true;
			
		default:
			return super.onOptionsItemSelected(item);
		}

	}

	/**
	 *  Initialize Action Bar 
	 */
	@SuppressLint("NewApi")
	private void initActionBar()
	{
		android.app.ActionBar  actionBar=getActionBar();
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayUseLogoEnabled(false);
		// actionBar.setBackgroundDrawable(getResources().getDrawable());
	//	actionBar.setIcon(R.drawable.login_btn_logo);
		// actionBar.setIcon(getResources().getDrawable(R.drawable.login_screen_back_btn_off));

		actionBar.setTitle(getResources().getString(R.string.createaccount));
	}
	/**
	 *  Initialise layout views  
	 */
	private void initLayoutId()
	{
		registerButton=(Button)findViewById(R.id.registerButton);
		firstNmameEditext=(EditText)findViewById(R.id.firstNmameEditext);
		lastNameEditext=(EditText)findViewById(R.id.lastNameEditext);
		mobileNumberEditText=(EditText)findViewById(R.id.mobileNumberEditText);
		emailIdEditText=(EditText)findViewById(R.id.emailIdEditText);
		accepttermandconditiontextview=(TextView)findViewById(R.id.accepttermandconditiontextview);
		passwordEditext=(EditText)findViewById(R.id.passwordEditext);
		profile_image = (ImageView)findViewById(R.id.su_one_profile_image);
		network_bar = (RelativeLayout)findViewById(R.id.network_bar);
		network_text = (TextView)findViewById(R.id.network_text);
		profile_image.setOnClickListener(this);
		firstNmameEditext.setOnFocusChangeListener(this);
		emailIdEditText.setOnFocusChangeListener(this);
		passwordEditext.setOnFocusChangeListener(this);
		mobileNumberEditText.setOnFocusChangeListener(this);
		emailIdEditText.setOnFocusChangeListener(this);
		registerButton.setOnClickListener(this);
		remembercheckbox=(android.widget.CheckBox)findViewById(R.id.remembercheckbox);
		remembercheckbox.setOnCheckedChangeListener(this);
		accepttermandconditiontextview.setOnClickListener(this);

		firstNmameEditext.addTextChangedListener(new TextWatcher()
		{

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

			@Override
			public void afterTextChanged(Editable s)
			{
				if (firstNmameEditext.getText().toString().length() <= 0)
				{
					//firstNmameEditext.setError(getResources().getString(R.string.fieldnotempty));
					editTextArray[0]=0;
				}
				else
				{
					firstNmameEditext.setError(null);
					editTextArray[0]=1;
				}
			}

		});
		/*
		* @prashant
		* last name is not mandatory
		* */

		/*
		lastNameEditext.addTextChangedListener(new TextWatcher()
		{

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

			@Override
			public void afterTextChanged(Editable s)
			{
				if (lastNameEditext.getText().toString().length() <= 0)
				{
					lastNameEditext.setError(getResources().getString(R.string.fieldnotempty));
					editTextArray[1]=0;
				}
				else
				{
					lastNameEditext.setError(null);
					editTextArray[1]=1;
				}
			}

		});*/

		mobileNumberEditText.addTextChangedListener(new TextWatcher()
		{

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

			@Override
			public void afterTextChanged(Editable s)
			{

				try
				{
					String  value=mobileNumberEditText.getText().toString();
					if(value.length()>0)
					{
						editTextArray[1]=1;
					}
					else
					{
						//mobileNumberEditText.setError(getResources().getString(R.string.fieldnotempty));
						editTextArray[1]=0;
					}
				}
				catch(NullPointerException ex)
				{
					mobileNumberEditText.setError(getResources().getString(R.string.fieldnotempty));
					editTextArray[1]=0;
				}
			}

		});
		passwordEditext.addTextChangedListener(new TextWatcher()
		{

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

			@Override
			public void afterTextChanged(Editable s)
			{
				try
				{
					String value=passwordEditext.getText().toString();
					if(value.length()>0)
					{
						editTextArray[3]=1;
						calcStr(passwordEditext.getText().toString());

						if(points<=3)
						{
							//passwordEditext.setError(getResources().getString(R.string.passwordstrength));
							editTextArray[3]=0;
						}
						else
						{
							editTextArray[3]=1;
						}
					}
					else
					{
						//passwordEditext.setError(getResources().getString(R.string.fieldnotempty));
						editTextArray[3]=0;
					}
				}
				catch(NullPointerException ex)
				{
					//passwordEditext.setError(getResources().getString(R.string.fieldnotempty));
					editTextArray[3]=0;
				}
			}

		});
		emailIdEditText.addTextChangedListener(new TextWatcher()
		{

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

			@Override
			public void afterTextChanged(Editable s)
			{
				try
				{
					String  value = emailIdEditText.getText().toString();
					if(value.length()>0)
					{
						editTextArray[2]=1;
						if(!Utility.validateEmail(emailIdEditText.getText().toString()))
						{
							//showAlert("Invalid Email format.");
							if(!emailIdEditText.getText().toString().equals(""))
							{
								emailIdEditText.setError(getResources().getString(R.string.invalidemail));
							}else if(emailIdEditText.getText().toString().equals(""))
							{
								emailIdEditText.setError(null);
							}
							editTextArray[2]=0;
							//return false;
						}
						else
						{
							editTextArray[2]=1;
						}

					}
					else
					{
						editTextArray[2]=0;
						//emailIdEditText.setError(getResources().getString(R.string.fieldnotempty));
					}

				}
				catch(NullPointerException ex)
				{
					emailIdEditText.setError(getResources().getString(R.string.fieldnotempty));
					editTextArray[2]=0;
				}

			}

		});
	}
	@Override
	public void onClick(View v) 
	{
		
			if (v.getId() == R.id.su_one_profile_image) 
			{
				photoChooseOption();
			}
		if (v.getId()==R.id.registerButton)
		{
			//boolean flagforinputfield= checkMandatoryField();
			if (editTextArray[0] == 1 && editTextArray[1] == 1 && editTextArray[2] == 1 && editTextArray[3] == 1 && remembercheckbox.isChecked())
			{
				signupRequest();
			}
			else if (editTextArray[0] != 1)
			{
				showAlert(getResources().getString(R.string.first_name_field));
			}
			else if (editTextArray[1] != 1)
			{
				showAlert(getResources().getString(R.string.phone_no_field));
			}
			else if (editTextArray[2] != 1)
			{
				showAlert(getResources().getString(R.string.email_field));
			}
			else if (editTextArray[3] != 1)
			{
				showAlert(getResources().getString(R.string.password_field));
			}
			else {
				showAlert(getResources().getString(R.string.checkbox_field));
			}

		}
			if (v.getId() == R.id.accepttermandconditiontextview) 
			{
				starttncview();
			}

	}

	/**
	 *  Singup  request 
	 */
	private void signupRequest()
	{
		Utility utility=new Utility();
		//	isfirstTimeUpdatcalled=true;
		ConnectionDetector connectionDetector=new ConnectionDetector(RegisterActivity.this);
		if (connectionDetector.isConnectingToInternet()) 
		{
			final String firstName=firstNmameEditext.getText().toString();
			String lastName ="";
			if (!"".equals(lastNameEditext.getText().toString()))
			{
				lastName = lastNameEditext.getText().toString();
			}
			
			//final String companyType=companyTypeEditext.getText().toString();
			// final String numberofSeat=seatingCapacityEditext.getText().toString();
			final String mobileNo=mobileNumberEditText.getText().toString();
			final String emailId=emailIdEditText.getText().toString();  
			//final String taxNumber=taxNumberEditext.getText().toString();
			final String password=passwordEditext.getText().toString();
			final String deviceid=Utility.getDeviceId(RegisterActivity.this);
			String curenttime=utility.getCurrentGmtTime();
			regid = getRegistrationId(RegisterActivity.this);
			//Utility.printLog("CompanyID"+companyTypeid);
			//String currentdata[]=curenttime.split(" ");

			Utility.printLog("device id="+deviceid+" regid="+regid);
			final String mparams[]={firstName,lastName,/*companyType,*/mobileNo,emailId/*,companyTypeid*/,password,deviceid,curenttime,regid };
			String	url = VariableConstants.MASTER_SIGNUP_STEP_1;
			
			mdialog = Utility.GetProcessDialog(RegisterActivity.this);
			mdialog.setMessage(getResources().getString(R.string.signupmassege));
			mdialog.show();
			RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);  // this = context

			StringRequest postRequest = new StringRequest(Request.Method.POST, url,
					new Response.Listener<String>()
					{
				@SuppressLint("NewApi")
				@Override
				public void onResponse(String response)
				{
					Utility.printLog("signupRequest  response "+response);
					fetchData(response);
				}

				private void fetchData(String jsonResponse) 
				{
					EmailValidateResponse response = null;

					try
					{
						if (jsonResponse!=null) 
						{
							Gson gson = new Gson();
							response=gson.fromJson(jsonResponse,EmailValidateResponse.class);
						}
						else
						{
							runOnUiThread(new Runnable()
							{
								public void run() 
								{
									Toast.makeText(getApplicationContext(),"Request Timeout !!", Toast.LENGTH_LONG).show();
								}

							});
						}
						if(response!=null)
						{
							if(response.getErrFlag().equals("0"))
							{
								SignupResponse signupResponse = response.getData();
								signupResponse.getChn();
								signupResponse.getEmail();
								signupResponse.getExpiryLocal();
								signupResponse.getFlag();
								signupResponse.getJoined();
								signupResponse.getmFlg();
								signupResponse.getT();
								signupResponse.getToken();
								
								Utility.printLog("channels lis="+signupResponse.getListner()+" sub="+signupResponse.getSusbChn());
								SessionManager session = new SessionManager(RegisterActivity.this);
								session.setSubscribeChannel(signupResponse.getSusbChn());
								session.setListnerChannel(signupResponse.getListner());
								
								/*Toast.makeText(getApplicationContext(),
										"Thank you for providing your details! One of our agents will contact you in the next 24 hours to complete your registration!",
										Toast.LENGTH_LONG).show();*/
								if (isPictureTaken)
								{
									isPictureTaken=false;
									String params[]={response.getErrFlag(),response.getErrNum(),response.getErrMsg(),signupResponse.getToken()};
									new BackGroundTaskForUploadImage().execute(params);
								}
								else
								{

									if (mdialog!=null)
									{
										mdialog.dismiss();
									}
								}
								ErrorMessage(getResources().getString(R.string.messagetitle),response.getErrMsg(),true,Integer.parseInt(response.getErrFlag()),Integer.parseInt(response.getErrNum()));
								//RegisterActivity.this.finish();
							}
							/*else if (response.getErrFlag().equals("1")&& response.getErrNum().equals("11")) 
							{
								if (isPictureTaken)
								{
									isPictureTaken=false;
									String params[]={response.getErrFlag(),response.getErrNum(),response.getErrMsg()};
									new BackGroundTaskForUploadImage().execute(params);
								}
								else
								{
									ErrorMessageForPreMdNotinYourArea(getResources().getString(R.string.messagetitle), response.getErrMsg(),Integer.parseInt(response.getErrFlag()),Integer.parseInt(response.getErrNum()));
								}
							}*/
							else if(response.getErrFlag().equals("1")&&response.getErrNum().equals("2"))
							{
								if (mdialog!=null)
								{
									mdialog.dismiss();
								}
								//ErrorMessage(getResources().getString(R.string.messagetitle),response.getErrMsg(),false,1,1);
								showAlert(response.getErrMsg());
							}
							else if(response.getErrFlag().equals("1")&&response.getErrNum().equals("1"))
							{
								if (mdialog!=null)
								{
									mdialog.dismiss();
								}
								//ErrorMessage(getResources().getString(R.string.messagetitle),response.getErrMsg(),false,1,1);
								showAlert(response.getErrMsg());
							}
							else
							{
								if (mdialog!=null)
								{
									mdialog.dismiss();
								}
								showAlert(response.getErrMsg());
							}
						}
					}
					catch(Exception e)
					{
						Utility.ShowAlert("Server error!!", RegisterActivity.this);
					}
				}
					},
					
					new Response.ErrorListener()
					{
						@Override
						public void onErrorResponse(VolleyError error) 
						{
							//String errorMessage = VolleyErrorHelper.getMessage(error, RegisterActivity.this);
							//android.widget.Toast.makeText(RegisterActivity.this, errorMessage, android.widget.Toast.LENGTH_SHORT).show();
							
							mdialog.dismiss();
							mdialog.cancel();
							showAlert(getResources().getString(R.string.servererror));
						}
					}
					) 
			{    

				@Override
				protected Map<String, String> getParams()
				{ 
					Map<String, String>  params = new HashMap<String, String>(); 
					params.put("ent_first_name", mparams[0]); 
					params.put("ent_last_name", mparams[1]);
					//params.put("ent_service_type", mparams[2]);
					params.put("ent_mobile", mparams[2]);
					params.put("ent_email", mparams[3]);
					//params.put("ent_comp_id", mparams[4]);
					params.put("ent_password", mparams[4]);
					params.put("ent_dev_id", mparams[5]);
					params.put("ent_date_time", mparams[6]);
					params.put("ent_push_token", mparams[7]);
					//params.put("ent_tax_num", mparams[8]);
					params.put("ent_device_type", "2");
					
					Utility.printLog("signup Request  = "+params);
					
					return params; 
				}
			};

			int socketTimeout = 60000;//60 seconds - change to what you want
			RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
			postRequest.setRetryPolicy(policy);
			//mRequestQueue.add(request);
			queue.add(postRequest);

		}
		else 
		{
			utility.showDialogConfirm(RegisterActivity.this,"Alert"," working internet connection required", false).show();

		}



	}
	private void showAlert(String message)
	{
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setTitle(getResources().getString(R.string.note));

		// set dialog message
		alertDialogBuilder
		.setMessage(message)
		.setCancelable(false)
		.setNegativeButton(getResources().getString(R.string.okbuttontext),new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				//closing the application
				dialog.dismiss();
			}
		});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();

	}

	private boolean checkMandatoryField()
	{
		for (int i = 0; i < editTextArray.length; i++) 
		{
			switch (i)
			{
			case 0:
				if (editTextArray[i]==0)
				{
					firstNmameEditext.setError(getResources().getString(R.string.fieldnotempty));
					mandantfieldcheck =false;
				}
				else 
				{
					mandantfieldcheck = true;
				}


			case 1:
				if (editTextArray[i]==0)
				{
					//lastNameEditext.setError(getResources().getString(R.string.fieldnotempty));
					//mandantfieldcheck = false;
				}
				else 
				{
					mandantfieldcheck = true;
				}
			case 2:
				if (editTextArray[i]==0)
				{
					mobileNumberEditText.setError(getResources().getString(R.string.fieldnotempty));
					mandantfieldcheck = false;
				}
				else 
				{
					mandantfieldcheck = true;
				}
			case 3:

				if (editTextArray[i]==0)
				{
					emailIdEditText.setError(getResources().getString(R.string.fieldnotempty));
					mandantfieldcheck = false;
				}
				else
				{
					mandantfieldcheck = true;
				}

			case 4:

				if (editTextArray[i]==0)
				{
					passwordEditext.setError(getResources().getString(R.string.fieldnotempty));
					mandantfieldcheck = false;
				}
				
				else 
				{
					calcStr(passwordEditext.getText().toString());
		    		if(points<=3)
		    		{
		    			//com.flurry.android.FlurryAgent.logEvent("Password Strength : LOW ");
		    			showMessage(getResources().getString(R.string.passwordstrengthlow));
		    			return false;
		    		}
		    		else
					{
						mandantfieldcheck = true;
					}
				}


			}
		}

		if (mandantfieldcheck)
		{
			if (remembercheckbox.isChecked()) 
			{

			}
			else 
			{
				mandantfieldcheck=false;
				animation = new AlphaAnimation(1, 0); // Change alpha from fully visible to invisible
				animation.setDuration(500); // duration - half a second
				animation.setInterpolator(new LinearInterpolator()); // do not alter animation rate
				animation.setRepeatCount(Animation.INFINITE); // Repeat animation infinitely
				animation.setRepeatMode(Animation.REVERSE); // Reverse animation at the end so the button will fade back in
				// final Button btn = (Button) findViewById(R.id.your_btn);
				remembercheckbox.startAnimation(animation);
				accepttermandconditiontextview.startAnimation(animation);
			}
		}

		return mandantfieldcheck;
	}
	boolean checkMandatoryFieldMissing(){
		boolean nothingIsMissing=true;
		if(firstNmameEditext.getText().toString().equals("")){
			firstNmameEditext.setError(getResources().getString(R.string.fieldnotempty));
			nothingIsMissing=false;
		}
		//////////////////////////////////////////////////////////
		if(mobileNumberEditText.getText().toString().equals("")){
			mobileNumberEditText.setError(getResources().getString(R.string.fieldnotempty));
			nothingIsMissing=false;
		}
		//////////////////////////////////////////////////////////
		if(emailIdEditText.getText().toString().equals("")){
			emailIdEditText.setError(getResources().getString(R.string.fieldnotempty));
			nothingIsMissing=false;
		}
		else if(!Utility.validateEmail(emailIdEditText.getText().toString()))
		{
			emailIdEditText.setError(getResources().getString(R.string.invalidemail));
			editTextArray[3]=0;
			nothingIsMissing=false;
		}
		///////////////////////////////////////////////////////////////
		if(passwordEditext.getText().toString().equals("")){
			passwordEditext.setError(getResources().getString(R.string.fieldnotempty));
			nothingIsMissing=false;
		}
		else if(passwordEditext.getText().length()>0){
			calcStr(passwordEditext.getText().toString());
			if(points<=3)
			{
				showMessage(getResources().getString(R.string.passwordstrengthlow));
				nothingIsMissing=false;
			}
		}
		if (!remembercheckbox.isChecked()) {
			nothingIsMissing = false;
			animation = new AlphaAnimation(1, 0); // Change alpha from fully visible to invisible
			animation.setDuration(500); // duration - half a second
			animation.setInterpolator(new LinearInterpolator()); // do not alter animation rate
			animation.setRepeatCount(Animation.INFINITE); // Repeat animation infinitely
			animation.setRepeatMode(Animation.REVERSE); // Reverse animation at the end so the button will fade back in
			// final Button btn = (Button) findViewById(R.id.your_btn);
			remembercheckbox.startAnimation(animation);
			accepttermandconditiontextview.startAnimation(animation);
		}
		return nothingIsMissing;
	}



	private void showMessage(CharSequence text)
    {
    	Context context = getApplicationContext();
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(context, text, duration);
		toast.show();    	
    }
	
	private void starttncview()
	{
		//com.flurry.android.FlurryAgent.logEvent("ClickOnAcceptTermandCondition");
		Intent intent=new Intent(RegisterActivity.this, TermandCondtion.class);
		startActivity(intent);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) 
	{
		return true;
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) 
	{
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		String value=null;

	}
	@Override
	protected void onResume() 
	{
		super.onResume();
	}
	
	@Override
	protected void onStart() 
	{
		super.onStart();
		locationUtil.connectGoogleApiClient();
	}
	
	@Override
	protected void onStop() 
	{
		super.onStop();
		locationUtil.disconnectGoogleApiClient();
	}
	
	private void calcStr(String pass)
	{
		len=pass.length();
		if(len==0)
		{
			//showMessage("Invalid Input ");
			return;
		}
		if(len<=5) points++;
		else
			if(len<=10) points+=2;
			else
				points+=3;
		for(int i=0;i<len;i++)
		{
			c=pass.charAt(i);
			if(c >='a' && c<='z') { if(low==0) points++;	low=1;} 
			else
			{
				if(c >='A' && c<='Z') {if(up==0) points++;	up=1;}
				else
				{
					if(c >='0' && c<='9') {if(no==0) 	points++;	no=1;}
					else
					{
						if(c == '_' || c == '@') { if(spl==0) 	points+=1;	spl=1;}
						else
						{
							if(xtra==0)	points+=2;
							xtra=1;

						}
					}
				}	
			}
		}

		//		if(points<=3) showMessage("Password Strength : LOW ");
		//		else
		//			if(points<=6) showMessage("Password Strength : MEDIUM ");
		//			else
		//				if(points<=9) showMessage("Password Strength : HIGH ");
		//		


		//points=0;len=0;up=0;low=0;no=0;xtra=0;spl=0;


	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
	{
		//remembercheckbox.startAnimation(animation);
		// accepttermandconditiontextview.startAnimation(animation);
		remembercheckbox.clearAnimation();
		accepttermandconditiontextview.clearAnimation();

	}

	private class  BackGroundTaskForUploadImage extends AsyncTask<String, Void, String>
	{
		private long chunkLength=1024*1024;
		private long totalBytesRead=0;
		private long bytesRemaining ;
		private long FILE_SIZE ;
		private String fileName;
		UploadImgeResponse response;
		private List<NameValuePair>uploadNameValuePairList;
		File mFile;
		private String errorNumb;
		private String errorflag;
		private String errorMessage;
		private String userSession;
		private int value;
		@Override
		protected String doInBackground(String... params) 
		{

			//com.flurry.android.FlurryAgent.logEvent("Profile Image uploading statrted ");
			errorflag=params[0];
			errorNumb=params[1];
			errorMessage=params[2];
			userSession = params[3];
			
			FileInputStream fin = null;
			totalBytesRead = 0;
			bytesRemaining=0;
			Utility utility=new Utility();
			String currentDate=utility.getCurrentGmtTime();
			//mFile=new File("/sdcard"+"/SneekPeek", "picture"+".jpg");
			String state = Environment.getExternalStorageState();
			if (Environment.MEDIA_MOUNTED.equals(state)) 
			{
				mFile= new File(Environment.getExternalStorageDirectory(), VariableConstants.TEMP_PHOTO_FILE_NAME);
			}
			else 
			{
				mFile= new File(getFilesDir(),VariableConstants.TEMP_PHOTO_FILE_NAME);
			}

			String temp=Utility.getCurrentDateTimeStringGMT();	
			temp=new String(temp.trim().replace(" ", "20"));
			temp=new String(temp.trim().replace(":", "20"));
			temp=new String(temp.trim().replace("-", "20"));
			int randonNumber=	random(10000);
			fileName="DA"+firstNmameEditext.getText().toString()+temp+randonNumber+mFile.getName();
			fileName=fileName.replaceAll(" ", "");
			try 
			{
				fin= new FileInputStream(mFile);
			}
			catch (FileNotFoundException e2) 
			{
				e2.printStackTrace();

			}

			if (mFile.isFile()&&mFile.length()>0) 
			{
				FILE_SIZE=mFile.length();

				//double numberOffChunk=((double)FILE_SIZE/(double)chunkLength);

				value = (int) Math.ceil(FILE_SIZE/chunkLength);
				if (value<1)
				{
					value=1;	
				}
				else 
				{

				}
				//Log.d(TAG, "BackGroundTaskForUploadImage doInBackground value "+value);

			}
			////Log.i(TAG, "BackGroundTaskForUploadImage doInBackground  FILE_SIZE "+FILE_SIZE);


			while (totalBytesRead < FILE_SIZE ) 
			{
				try 
				{
					bytesRemaining = FILE_SIZE-totalBytesRead;

					if ( bytesRemaining < chunkLength ) // Remaining Data Part is Smaller Than CHUNK_SIZE
					{
						chunkLength = bytesRemaining;
					}

					byte []chunk = null;
					chunk=new byte[(int) chunkLength];

					byte fileContent[] = new byte[(int) chunkLength];
					try 
					{

						fin.read(fileContent,0,(int)chunkLength);

					} 
					catch (FileNotFoundException e1) 
					{
						e1.printStackTrace();
					} 
					catch (IOException e) 
					{
						e.printStackTrace();
					}

					System.arraycopy(fileContent, 0, chunk, 0, (int) chunkLength);
					byte[] encoded = Base64.encodeBase64(chunk);
					String encodedString = new String(encoded);
					String deviceid=Utility.getDeviceId(RegisterActivity.this);
					String [] uploadParameter={userSession,deviceid,fileName,encodedString,"1","1",""+value,currentDate};
					uploadNameValuePairList=utility.getUploadParameter(uploadParameter);
					android.util.Log.d(TAG, "BackGroundTaskForUploadImage doInBackground uploadNameValuePairList "+uploadNameValuePairList);
					totalBytesRead = totalBytesRead+chunkLength;

					Utility.printLog("AAA UploadParameter"+uploadParameter);

					String result=utility.makeHttpRequest(VariableConstants.uploadImage_url,"POST",uploadNameValuePairList);
					Utility.printLog("AAA Checking Profilepicresponse"+result);

					if(result!=null)
					{
						Utility.printLog("AAA Profilepicresponse"+result);
						Gson gson = new Gson();
						response=gson.fromJson(result, UploadImgeResponse.class);
						Utility.printLog("AAA Profilepicresponse created without error"+response);
					}

				}
				catch (Exception e) 
				{
					Utility.printLog(TAG, "BackGroundTaskForUploadImage doInBackground Exception "+e);
				}
			}
			value=0;
			return null;
		}

		int random(int a)
		{
			Random rand = new Random();
			int prob = (int) (a * rand.nextDouble());
			return prob;
		}

		@Override
		protected void onPostExecute(String result) 
		{
			super.onPostExecute(result);
		     if (mdialog!=null)
		     {
		    	 mdialog.dismiss();
		    	 mdialog.cancel();
		    	 mdialog=null;
			}

			if(response!=null)
			{
				//com.flurry.android.FlurryAgent.logEvent("Profile Image  successfully uploaded ");
				if(response.getErrFlag().equals("0"))
				{
					//Toast.makeText(getApplicationContext(), "SignUp Successfull !!", Toast.LENGTH_SHORT).show();
					//SessionManager sessionManager=new SessionManager(SignUpOne.this);
					//sessionManager.createSession();
					if (errorflag.equals("0")) 
					{
						ErrorMessage(getResources().getString(R.string.messagetitle),errorMessage,false,Integer.parseInt(errorflag),Integer.parseInt(errorNumb));
					}
					else if (errorflag.equals("1")&&errorNumb.equals("11"))
					{
						ErrorMessageForPreMdNotinYourArea(getResources().getString(R.string.messagetitle),errorMessage,Integer.parseInt(errorflag),Integer.parseInt(errorNumb));
					}

				}
				else
				{
					Toast.makeText(getApplicationContext(),response.getErrMsg(), Toast.LENGTH_SHORT).show();
				}

			}
		}
	}
	private void ErrorMessage(String title,final String message,final boolean flageforSwithchActivity,final int errorFlag,final int errornum)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setPositiveButton(getResources().getString(R.string.okbuttontext),new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				if (flageforSwithchActivity) 
				{
					Intent intent=new Intent(RegisterActivity.this, SplahsActivity.class);
					Bundle bundle=new Bundle();
					bundle.putInt(VariableConstants.LOGINERRORFLAG, errorFlag);
					bundle.putInt(VariableConstants.LOGINERRORNUM, errornum);
					bundle.putString(VariableConstants.LOGINERRORMESSAGE, message);
					intent.putExtras(bundle);
					startActivity(intent);
					finish();
				}
				else
				{
					// only show message  
					finish();
					dialog.dismiss();
				}

			}
		});
		try 
		{
			AlertDialog	 alert = builder.create();
			alert.setCancelable(false);
			alert.show();
		} catch (Exception e)
		{
			Utility.printLog("Exception  "+e);
		}
		
	}

	private void ErrorMessageForPreMdNotinYourArea(String title,final String message,final int errorFlag,final int errornum)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
		builder.setTitle(title);
		builder.setMessage(message);

		builder.setPositiveButton(getResources().getString(R.string.cancelbutton),
				new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{

				//				 Intent intent=new Intent(SignUpOne.this, MainActivityDrower.class);
				//				 Bundle bundle=new Bundle();
				//				 bundle.putInt(VariableConstants.LOGINERRORFLAG, errorFlag);
				//				 bundle.putInt(VariableConstants.LOGINERRORNUM, errornum);
				//				 bundle.putString(VariableConstants.LOGINERRORMESSAGE, message);
				//				 intent.putExtras(bundle);
				// startActivity(intent);
				dialog.dismiss();
				finish();
			}
		});

		builder.setNegativeButton(getResources().getString(R.string.email),
				new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				// Intent intent=new Intent(SignUpOne.this, MainActivityDrowerDrower.class);
				// startActivity(intent);
				finish();
				dialog.dismiss();
			}
		});
		AlertDialog	 alert = builder.create();
		alert.setCancelable(false);
		alert.show();
	}
	
	@Override
	protected void onPause() 
	{
		super.onPause();
		if (mdialog!=null)
		{
		 mdialog.dismiss();
		 mdialog.cancel();mdialog=null;
		}
	}

	
	/*public void showSettingsAlert()
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
      
        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings");
  
        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");
  
        // Setting Icon to Dialog
        //alertDialog.setIcon(R.drawable.delete);
  
        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });
  
        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) 
            {
             dialog.cancel();
             finish();
            }
        });
  
        // Showing Alert Message
        alertDialog.show();
    }*/
	
	private void checkingNetworkState()
	{
		final Handler handler = new Handler();
		handler.postDelayed(new Runnable() {

			@Override
			public void run()
			{if (!Utility.isNetworkAvailable(RegisterActivity.this))
			{
				network_bar.setVisibility(View.VISIBLE);
			}
			else if (!NetworkConnection.isConnectedFast(RegisterActivity.this)) 
			{
				network_bar.setVisibility(View.VISIBLE);
				network_text.setText(getResources().getString(R.string.lownetwork));
			}
			else{
				network_bar.setVisibility(View.GONE);
			}
			}
		} , 2000);
	}

	@Override
	public void updatedInfo(String info) 
	{
		
	}

	@Override
	public void locationUpdates(Location location) 
	{
		SessionManager sessionManager = new SessionManager(this);
		sessionManager.setDriverCurrentlat(""+location.getLatitude());
		sessionManager.setDriverCurrentLongi(""+location.getLongitude());
	}

	@Override
	public void locationFailed(String message) {
		// TODO Auto-generated method stub
		
	}
	
	
}
