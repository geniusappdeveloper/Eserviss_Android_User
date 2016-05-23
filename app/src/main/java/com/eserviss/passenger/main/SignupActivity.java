package com.eserviss.passenger.main;



import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.egnyt.eserviss.MainActivity;
import com.egnyt.eserviss.R;
import com.eserviss.passenger.pojo.UploadImgeResponse;
import com.eserviss.passenger.pojo.ValidVerificationCodeResponse;
import com.eserviss.passenger.pojo.VerificationCodeResponse;
import com.eserviss.passenger.pubnu.pojo.CheckMobileNoResponse;
import com.flurry.android.FlurryAgent;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.gson.Gson;
import com.threembed.utilities.AppLocationService;
import com.threembed.utilities.InternalStorageContentProvider;
import com.threembed.utilities.SessionManager;
import com.threembed.utilities.Utility;
import com.threembed.utilities.VariableConstants;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import eu.janmuller.android.simplecropimage.CropImage;

public class SignupActivity extends Activity implements View.OnClickListener, View.OnFocusChangeListener {
	private static final String TAG = "SignupActivity";
	private EditText firstName, lastName, email, mobileNo, password, referral_code;//zipcode,confirm_password;
	private TextView Terms_Cond, signup_phone_code;
	private CheckBox chkBox;
	private ImageView profile_pic, back;
	private Button next;
	private boolean isPasswordStrength = true;
	private final int REQUEST_CODE_GALLERY = 0x1;
	private final int REQUEST_CODE_TAKE_PICTURE = 0x2;
	private final int REQUEST_CODE_CROP_IMAGE = 0x3;
	private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	private String SENDER_ID = VariableConstants.PROJECT_ID;
	public static File mFileTemp;
	private GoogleCloudMessaging gcm;
	private String regid, deviceid;
	private Context context;
	private SessionManager session;
	private boolean isEmailVaild = false;
	private double currentLatitude, currentLongitude;
	String fullMobileNumber, countryCode;
	private boolean isMobileValid = false;
	private final int selectedCountryList = 4;
	private boolean isCountrySelected = false;
	private String name;
	RadioButton male, female;// others
    ImageView flag;
	Dialog fDialog;
	EditText et_verificationcode;

	String gender = "";
	String[] files=null;
	/**
	 * ATTENTION: This was auto-generated to implement the App Indexing API.
	 * See https://g.co/AppIndexing/AndroidStudio for more information.
	 */
	private GoogleApiClient client;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signup_one);
		session = new SessionManager(SignupActivity.this);
		initialize();

		AppLocationService appLocationService = new AppLocationService(SignupActivity.this);
		Location gpsLocation = appLocationService.getLocation(LocationManager.GPS_PROVIDER);
		if (gpsLocation != null) {
			currentLatitude = gpsLocation.getLatitude();
			currentLongitude = gpsLocation.getLongitude();
			Utility.printLog("canGetLocation lat=" + currentLatitude + " lng=" + currentLongitude);
		} else {
			Location nwLocation = appLocationService.getLocation(LocationManager.NETWORK_PROVIDER);
			if (nwLocation != null) {
				currentLatitude = nwLocation.getLatitude();
				currentLongitude = nwLocation.getLongitude();
			} else {
				showSettingsAlert();
			}
		}

		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			mFileTemp = new File(Environment.getExternalStorageDirectory(), VariableConstants.TEMP_PHOTO_FILE_NAME);
		} else {
			mFileTemp = new File(getFilesDir(), VariableConstants.TEMP_PHOTO_FILE_NAME);
		}


		if (checkPlayServices()) {
			if (gcm == null) {
				Log.i("===", "gcm IS null" + gcm);
				gcm = GoogleCloudMessaging.getInstance(SignupActivity.this);
			}
			regid = session.getRegistrationId();
			Utility.printLog("BackgroundForUpdateToken login regid test ......." + regid);
			if (regid == null) {
				new BackgroundForRegistrationId().execute();
			} else {
				deviceid = getDeviceId(context);
			}

			Utility.printLog("doInBackground regid.........." + regid);
			Log.i("===", "doInBackground deviceid" + deviceid);

		} else {
			Utility.printLog("No valid Google Play Services found.");
		}
		// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
		client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
	}

	public void showSettingsAlert() {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(SignupActivity.this);
		// Setting Dialog Title
		alertDialog.setTitle(getResources().getString(R.string.gps_settings));

		// Setting Dialog Message
		alertDialog.setMessage(getResources().getString(R.string.gps_alert_message));

		// On pressing Settings button
		alertDialog.setPositiveButton(getResources().getString(R.string.settings), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
				Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				startActivity(intent);
				SignupActivity.this.finish();
			}
		});
		// on pressing cancel button
		alertDialog.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
				SignupActivity.this.finish();
			}
		});

		// Showing Alert Message
		alertDialog.show();
	}

	private void initialize() {
		firstName = (EditText) findViewById(R.id.first_name);
		lastName = (EditText) findViewById(R.id.last_name);
		email = (EditText) findViewById(R.id.signup_email);
		mobileNo = (EditText) findViewById(R.id.signup_phone);
		referral_code = (EditText) findViewById(R.id.signup_referal_code);
		password = (EditText) findViewById(R.id.signup_password);
		//confirm_password=(EditText)findViewById(R.id.signup_confirm_password);
		chkBox = (CheckBox) findViewById(R.id.chkbox_TandC);
		profile_pic = (ImageView) findViewById(R.id.profile_pic);
		signup_phone_code = (TextView) findViewById(R.id.signup_phone_code);
		flag= (ImageView) findViewById(R.id.flag_img);
		back = (ImageView) findViewById(R.id.signup_back);
		next = (Button) findViewById(R.id.signup_next);
		male = (RadioButton) findViewById(R.id.male);
		female = (RadioButton) findViewById(R.id.female);
		//others = (RadioButton) findViewById(R.id.others);


		Terms_Cond = (TextView) findViewById(R.id.txt_TandC);

		signup_phone_code.setOnClickListener(this);
		back.setOnClickListener(this);
		next.setOnClickListener(this);
		profile_pic.setOnClickListener(this);

		password.addTextChangedListener(mTextEditorWatcher);
		password.setOnFocusChangeListener(this);
		//======================My Change=============================
		Typeface roboto_condensed = Typeface.createFromAsset(SignupActivity.this.getAssets(),"fonts/BebasNeue.otf");

		TextView registertxtv= (TextView) findViewById(R.id.registertxtv);
		TextView terms_plain_text= (TextView) findViewById(R.id.terms_plain_text);
		TextView txt_TandC= (TextView) findViewById(R.id.txt_TandC);

		registertxtv.setTypeface(roboto_condensed);
		terms_plain_text.setTypeface(roboto_condensed);
		txt_TandC.setTypeface(roboto_condensed);
		next.setTypeface(roboto_condensed);

		try {
			flag.setImageBitmap(BitmapFromAsset(SignupActivity.this,"flag_lb.png"));
			signup_phone_code.setText("+961");
		} catch (IOException e) {
			e.printStackTrace();
		}
       //======================My Change=============================
		Terms_Cond.setOnClickListener(this);


		referral_code.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {
				Utility.printLog("setOnFocusChangeListener hasFocus= " + hasFocus);
				if (!hasFocus) {

					if (!(referral_code.getText().toString().trim().isEmpty())) {
						ValidatePromoCode(referral_code.getText().toString().trim());
					}

				}
			}
		});

		mobileNo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {
				Utility.printLog("setOnFocusChangeListener hasFocus= " + hasFocus);
				if (!hasFocus) {

					if (mobileNo.getText().toString().trim().isEmpty()) {
						showAlert(getResources().getString(R.string.mobile_empty));
					} else {
					
						
	        			/*if(!contac_Status(mobileNo.getText().toString().trim()))
						{
	        				showAlert("Invalid mobileNo format.");
	        			}*/
	        			/*else
		            	{*/
						new BackgroundValidateMobileNo().execute();

						//}

					}
				}
			}
		});

		email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {
				Utility.printLog("setOnFocusChangeListener hasFocus= " + hasFocus);
				if (!hasFocus) {

					if (email.getText().toString().trim().isEmpty()) {
						showAlert(getResources().getString(R.string.email_empty));
					} else {
						if (!validateEmail(email.getText().toString().trim())) {
							showAlert(getResources().getString(R.string.invalid_email));
						} else {
							new BackgroundValidateEmail().execute();
						}
					}
				}
			}
		});

		//commented becouse validation is not needed here

	}


	public void onRadioButtonClicked(View view) {

		switch (view.getId()) {
			case R.id.male:

				gender = male.getText().toString();

				break;

			case R.id.female:

				gender = female.getText().toString();

				break;

			/*case R.id.others:

				gender = others.getText().toString();*/

			//break;


			default:
				break;
		}

	}


	private boolean checkPlayServices() {
		Log.d(TAG, "onCreate checkPlayServices ");
		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		if (resultCode != ConnectionResult.SUCCESS) {
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
				Utility.printLog("This device is supported.");
				GooglePlayServicesUtil.getErrorDialog(resultCode, this,
						PLAY_SERVICES_RESOLUTION_REQUEST).show();
			} else {
				Utility.printLog("This device is not supported.");
				finish();
			}
			return false;
		}
		return true;
	}

	/*public String getDeviceId(Context context)
	{
		TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		return telephonyManager.getDeviceId();

	}*/

	public String getDeviceId(Context context) {
		/*TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		return telephonyManager.getDeviceId();*/

		TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		return telephonyManager.getDeviceId();

	}


	@Override
	public void onClick(View v) {


		if(v.getId()==R.id.signup_phone_code)
		{
			showDialoagforcountrypicker();
		}


		if (v.getId() == R.id.signup_back) {
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					SignupActivity.this);

			// set title
			alertDialogBuilder.setTitle(getResources().getString(R.string.cancel_account_creation));

			// set dialog message
			alertDialogBuilder
					.setMessage(getResources().getString(R.string.cancel_account_creation_alert))
					.setCancelable(false)
					.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							// if this button is clicked, close
							// current activity
							Intent intent = new Intent(SignupActivity.this, MainActivity.class);
							intent.putExtra("NO_ANIMATION", true);
							startActivity(intent);
							overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
							SignupActivity.this.finish();
						}
					})
					.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							// if this button is clicked, just close
							// the dialog box and do nothing
							dialog.cancel();
						}
					});

			// create alert dialog
			AlertDialog alertDialog = alertDialogBuilder.create();

			// show it
			alertDialog.show();
		}
		if (v.getId() == R.id.signup_next) {
			boolean isValid = validateFields();


			if (isValid) {
				if (validateEmail(email.getText().toString())) {

					if (isEmailVaild) {
						/*if(isMobileValid)
					{*/


						if (!mobileNo.getText().toString().isEmpty()) {
							if (referral_code.getText().toString().trim().equals("")) {

								session.clearSession();
								session.storeCurrencySymbol("AED");
								session.storeRegistrationId(regid);
								session.storeDeviceId(deviceid);

								/*Intent intent=new Intent(SignupActivity.this,SignupPayment.class);
								intent.putExtra("REGID",regid);
								intent.putExtra("DEVICEID",deviceid);

								Utility.printLog("Before sendin deviceId"+deviceid);
								Utility.printLog("Before sendin regId"+regid);
								intent.putExtra("FIRSTNAME",firstName.getText().toString());

								if(lastName.getText()!=null)
									intent.putExtra("LASTNAME",lastName.getText().toString());

								intent.putExtra("EMAIL",email.getText().toString());
								intent.putExtra("PASSWORD",password.getText().toString());
								intent.putExtra("MOBILE",mobileNo.getText().toString());
								//intent.putExtra("CITY_NAME","La Mirada");
								//intent.putExtra("CITY_NAME",current_city_name);
								//intent.putExtra("ProfileImage", mFileTemp);
								startActivity(intent);
								SignupActivity.this.finish();*/
								/*	if(Utility.isNetworkAvailable(SignupActivity.this))
							new BackgroundTaskSignUp().execute();
						else
							Utility.ShowAlert(getResources().getString(R.string.network_connection_fail), SignupActivity.this);*/

								new BackgroundForGettingVerificationCode().execute();
								overridePendingTransition(R.anim.anim_two, R.anim.anim_one);
							} else {
								ValidatePromoCode(referral_code.getText().toString().trim());
							}
						} else {
							showAlert(getResources().getString(R.string.mobile_empty));
						}
					}
					/*else
					{
						ValidatePromoCode(referral_code.getText().toString().trim());
					}*/
				}
				/*else
					{
						showAlert(getResources().getString(R.string.mobileregistered));
					}
				}*/
				else {
					showAlert(getResources().getString(R.string.enter_valid_email));
				}
			} else {
				//showAlert(getResources().getString(R.string.email_empty));
			}
		}
		if (v.getId() == R.id.profile_pic) {
			Log.i("", "inside profile pic");
			AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(SignupActivity.this);

			// Setting Dialog Message
			alertDialog2.setMessage(getResources().getString(R.string.selecto_photo));


			// Setting Positive "Yes" Btn
			alertDialog2.setPositiveButton(getResources().getString(R.string.gallery),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							openGallery();
						}
					});

			// Setting Negative "NO" Btn
			alertDialog2.setNegativeButton(getResources().getString(R.string.camera),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							takePicture();
						}
					});

			// Showing Alert Dialog
			alertDialog2.show();

		}

		/*if(v.getId()==R.id.warning_pwd)
		{

			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SignupActivity.this);

			// set title
			alertDialogBuilder.setTitle(getResources().getString(R.string.password_strength_low));

			// set dialog message
			alertDialogBuilder
				.setMessage(getResources().getString(R.string.password_strength_low_message))
				.setCancelable(false)

				.setNegativeButton(getResources().getString(R.string.ok),new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						//closing the application
						dialog.dismiss();
					}
				});

			// create alert dialog
			AlertDialog alertDialog = alertDialogBuilder.create();

			// show it
			alertDialog.show();
		}*/

		if (v.getId() == R.id.txt_TandC) {
			Intent intent = new Intent(SignupActivity.this, TermsActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
		}

		/*if(v.getId()==R.id.signup_phone_code)
		{
			Intent intent = new Intent(SignupActivity.this,ChooseCountry.class);
			startActivityForResult(intent, selectedCountryList);
		}*/

	}


	private void showDialoagforcountrypicker()
	{
		final Dialog d = new Dialog(SignupActivity.this);
		d.setTitle("Select/Edit Country");
		d.setContentView(R.layout.custome_spinner);
		LinearLayout weightLl= (LinearLayout) d.findViewById(R.id.weightLl);
		RelativeLayout quantity_Rl= (RelativeLayout) d.findViewById(R.id.quantity_Rl);
		quantity_Rl.setVisibility(View.VISIBLE);
		weightLl.setVisibility(View.GONE);

		final NumberPicker country_numberPicker = (NumberPicker) d.findViewById(R.id.quntity_numberPicker);
		final String country[] = getResources().getStringArray(R.array.country_list);
		final  String[] rl=getResources().getStringArray(R.array.CountryCodes);
		//=========================MY Change====================================
		AssetManager assetManager = getAssets();

		try {
			 files = new String[assetManager.list("flags").length];
			files=assetManager.list("flags");
		} catch (IOException e) {
			e.printStackTrace();
		}
		//=========================MY Change====================================

		//final String flags=getAssets().

		Utility.printLog("size of country"+country.length +" code size "+rl.length);

		country_numberPicker.setMinValue(0);
		country_numberPicker.setMaxValue(country.length - 1);
		country_numberPicker.setDisplayedValues(country);

		Button cancle_btn = (Button) d.findViewById(R.id.cancle_btn);
		Button Set_btn = (Button) d.findViewById(R.id.Set_btn);

		Set_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Utility.printLog(" country  value" + String.valueOf(country_numberPicker.getValue()));
				Utility.printLog(" country index value" + rl[country_numberPicker.getValue()]);
				String code[]= rl[country_numberPicker.getValue()].split(",");
				signup_phone_code.setText(code[0]); //set the value to textview

				//=========================MY Change====================================
				for(int i=0; i<files.length;i++)
				{
					if (code[1].equalsIgnoreCase(files[i].replace(".",",").split(",")[0].split("_")[1]))
					{
						try {


							flag.setImageBitmap(BitmapFromAsset(SignupActivity.this,files[i]));
						} catch (IOException e) {
							e.printStackTrace();
						}

					}
				}


				/*try {
					flag.setImageBitmap(BitmapFromAsset(SignupActivity.this,files[country_numberPicker.getValue()].replace(".",",").split(",")[0]));
				} catch (IOException e) {
					e.printStackTrace();
				}*/
				d.dismiss();
			}
		});
		cancle_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				d.dismiss(); // dismiss the dialog
			}
		});

		d.show();


	}//End of Counrycode
//================================= My Change=================================
	public static Bitmap BitmapFromAsset(Context c, String strName)
			throws IOException {
		Bitmap bitmap = null;
		try {

			AssetManager assetManager = c.getAssets();


			InputStream istr = assetManager.open("flags"+"/"+strName);
// InputStream istr =
			// assetManager.open(strName.split(",")[1]+"/"+strName.split(",")[0]+".png");
			bitmap = BitmapFactory.decodeStream(istr);
		} catch (Exception e) {
			Toast.makeText(c,e.toString(),Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
		return bitmap;
	}
	//================================= My Change=================================
	private void openGallery() {
		Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
		photoPickerIntent.setType("image/*");
		startActivityForResult(photoPickerIntent, REQUEST_CODE_GALLERY);
	}

	private void takePicture() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		try {
			Uri mImageCaptureUri = null;
			String state = Environment.getExternalStorageState();
			if (Environment.MEDIA_MOUNTED.equals(state)) {
				mImageCaptureUri = Uri.fromFile(mFileTemp);
			} else {
				mImageCaptureUri = InternalStorageContentProvider.CONTENT_URI;
			}
			intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
			intent.putExtra("return-data", true);
			startActivityForResult(intent, REQUEST_CODE_TAKE_PICTURE);
		} catch (ActivityNotFoundException e) {

			Log.d("", "cannot take picture", e);
		}
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK) {
			return;
		}

		Bitmap bitmap;

		switch (requestCode) {

			case REQUEST_CODE_GALLERY:

				try {

					InputStream inputStream = getContentResolver().openInputStream(data.getData());
					FileOutputStream fileOutputStream = new FileOutputStream(mFileTemp);

					Log.e("", "inputStream" + inputStream);
					Log.e("", "fileOutputStream" + fileOutputStream);

					copyStream(inputStream, fileOutputStream);
					fileOutputStream.close();
					inputStream.close();

					startCropImage();

				} catch (Exception e) {
					Log.e("", "Error while creating temp file", e);
				}

				break;
			case REQUEST_CODE_TAKE_PICTURE:

				startCropImage();
				break;
			case REQUEST_CODE_CROP_IMAGE:

				String path = data.getStringExtra(CropImage.IMAGE_PATH);
				Log.e("", "path fileOutputStream " + path);

				if (path == null) {
					return;
				}

				bitmap = BitmapFactory.decodeFile(mFileTemp.getPath());
				Utility.printLog("profile pic name getPath=" + mFileTemp.getPath());
				Utility.printLog("profile pic name1=" + mFileTemp.getName());
				profile_pic.setImageBitmap(bitmap);
				Utility.printLog("BitmapFactory file size before = " + mFileTemp.length());


				break;

		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
		int width = image.getWidth();
		int height = image.getHeight();

		float bitmapRatio = (float) width / (float) height;
		if (bitmapRatio > 0) {
			width = maxSize;
			height = (int) (width / bitmapRatio);
		} else {
			height = maxSize;
			width = (int) (height * bitmapRatio);
		}
		return Bitmap.createScaledBitmap(image, width, height, true);
	}

	public Bitmap getResizedBitmap(Bitmap image, int bitmapWidth, int bitmapHeight) {
		return Bitmap.createScaledBitmap(image, bitmapWidth, bitmapHeight, true);
	}

	public Uri getImageUri(Context inContext, Bitmap inImage) {
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		inImage.compress(Bitmap.CompressFormat.PNG, 100, bytes);
		String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
		return Uri.parse(path);
	}

	private void startCropImage() {
		Intent intent = new Intent(this, CropImage.class);
		intent.putExtra(CropImage.IMAGE_PATH, mFileTemp.getPath());
		intent.putExtra(CropImage.SCALE, true);

		intent.putExtra(CropImage.ASPECT_X, 4);
		intent.putExtra(CropImage.ASPECT_Y, 4);

		startActivityForResult(intent, REQUEST_CODE_CROP_IMAGE);
	}


	public static void copyStream(InputStream input, OutputStream output)
			throws IOException {

		byte[] buffer = new byte[1024];
		int bytesRead;
		while ((bytesRead = input.read(buffer)) != -1) {
			output.write(buffer, 0, bytesRead);
		}
	}


	private boolean validateFields() {
		if (firstName.getText().toString().isEmpty() || firstName.getText().toString().equals("")) {
			showAlert(getResources().getString(R.string.first_name_empty));
			return false;
		} else if (mobileNo.getText().toString().isEmpty()) {
			showAlert(getResources().getString(R.string.phone_num_empty));
			return false;
		} else if (email.getText().toString().isEmpty()) {
			showAlert(getResources().getString(R.string.email_empty));
			return false;
		} else if (password.getText().toString().isEmpty()) {
			showAlert(getResources().getString(R.string.password_empty));
			return false;
		} else if (!chkBox.isChecked()) {
			showAlert(getResources().getString(R.string.accept_terms_conditions));
			return false;
		}

		return true;
	}

	private void showAlert(String message) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

		// set title
		alertDialogBuilder.setTitle(getResources().getString(R.string.alert));

		// set dialog message
		alertDialogBuilder
				.setMessage(message)
				.setCancelable(false)
				.setNegativeButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						//closing the application
						dialog.dismiss();
					}
				});
		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();
		// show it
		alertDialog.show();
	}


	private final TextWatcher mTextEditorWatcher = new TextWatcher() {

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			Utility.printLog("inside onTextChanged");

			//warning_pwd.setVisibility(View.INVISIBLE);

		}

		@Override
		public void afterTextChanged(Editable s) {

		}
	};

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if (v.getId() == R.id.signup_password) {
			Utility.printLog("inside onFocusChange");
			if (!hasFocus) {
				//Utility.printLog("hasFocus: "+hasFocus);
				int strength = calcStr(password.getText().toString());

				if (strength == -1) {
					password.setError(getResources().getString(R.string.password_sould_not_empty));
					isPasswordStrength = false;
					return;
				}
				if (strength <= 3) {
					//warning_pwd.setVisibility(View.VISIBLE);

					//password.setError(getResources().getString(R.string.password_strength_low_message));
					isPasswordStrength = false;
					return;
				}
				isPasswordStrength = true;
			}
			return;
		}
	}

	public boolean validateEmail(String email) {
		boolean isValid = false;

		String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
		CharSequence inputStr = email;

		Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(inputStr);
		if (matcher.matches()) {
			isValid = true;
		}
		return isValid;
	}
	//contact validation
	   /* public boolean contac_Status(String no) 
	    {
	    	boolean isvalid = false;
	        if(no.matches("[0-9]{10}")){
	            //return true;
	            isvalid = true;
	        }
	       

	            return isvalid;
	        
	    }*/

	private int calcStr(String pass) {
		int up = 0, low = 0, no = 0, spl = 0, xtra = 0, len = 0, points = 0;
		char c;

		len = pass.length();
		if (len == 0) {
				/*showMessage("Invalid Input ");*/
			return -1;
		}
		if (len <= 5) points++;
		else if (len <= 10) points += 2;
		else
			points += 3;
		for (int i = 0; i < len; i++) {
			c = pass.charAt(i);
			if (c >= 'a' && c <= 'z') {
				if (low == 0) points++;
				low = 1;
			} else {
				if (c >= 'A' && c <= 'Z') {
					if (up == 0) points++;
					up = 1;
				} else {
					if (c >= '0' && c <= '9') {
						if (no == 0) points++;
						no = 1;
					} else {
						if (c == '_' || c == '@') {
							if (spl == 0) points += 1;
							spl = 1;
						} else {
							if (xtra == 0) points += 2;
							xtra = 1;

						}
					}
				}
			}
		}

		return points;

			/*if(points<=3) showMessage("Password Strength : LOW ");
    		else
    			if(points<=6) showMessage("Password Strength : MEDIUM ");
    			else
    				if(points<=9) showMessage("Password Strength : HIGH ");



    		points=0;len=0;up=0;low=0;no=0;xtra=0;spl=0;*/
	}


	private class BackgroundForRegistrationId extends AsyncTask<String, Void, String> {
		private ProgressDialog dialogL;


		@Override
		protected String doInBackground(String... params) {
			Log.i("===", "Inside DOINBACKGROUNG OF BackgroundForRegistrationId");

			try {

				deviceid = getDeviceId(context);
				regid = gcm.register(SENDER_ID);

				session.storeRegistrationId(regid);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Log.i("===", "test deviceid" + deviceid);
			Log.i("===", "test regid" + regid);
			return null;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			dialogL = Utility.GetProcessDialog(SignupActivity.this);
			dialogL.setCancelable(false);
			if (dialogL != null) {
				dialogL.show();
			}
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (dialogL != null) {
				dialogL.dismiss();
			}

			if (regid == null) {
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SignupActivity.this);

				// set title
				alertDialogBuilder.setTitle(getResources().getString(R.string.alert));

				// set dialog message
				alertDialogBuilder
						.setMessage(getResources().getString(R.string.network_connection_fail))
						.setCancelable(false)
						.setNegativeButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								//closing the application
								finish();
							}
						});

				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();

				// show it
				alertDialog.show();
			}
		}
	}


	class BackgroundValidateMobileNo extends AsyncTask<String, Void, String> {
		CheckMobileNoResponse mobileResponse;
		ProgressDialog dialogL;

		protected void onPreExecute() {
			super.onPreExecute();
			dialogL = Utility.GetProcessDialogNew(SignupActivity.this, "Validating Mobile No...");
			dialogL.setCancelable(true);
			if (dialogL != null) {
				dialogL.setCancelable(false);
				dialogL.show();
			}
		}

		@Override
		protected String doInBackground(String... params) {
			String url = VariableConstants.BASE_URL + "checkMobile";

			Utility utility = new Utility();
			String curenttime = utility.getCurrentGmtTime();
			Log.i("", "dateandTime " + curenttime);

			Map<String, String> kvPairs = new HashMap<String, String>();
			fullMobileNumber = signup_phone_code.getText().toString() + mobileNo.getText().toString();
			kvPairs.put("ent_mobile", fullMobileNumber);
			kvPairs.put("ent_user_type", "2");

			Utility.printLog("The kvPair values in Signup Activity is :: " + kvPairs);

			HttpResponse httpResponse = null;
			try {
				httpResponse = Utility.doPost(url, kvPairs);
			} catch (ClientProtocolException e1) {
				e1.printStackTrace();
				Utility.printLog("doPost Exception......." + e1);
			} catch (IOException e1) {
				e1.printStackTrace();
				Utility.printLog("doPost Exception......." + e1);
			}

			String jsonResponse = null;

			if (httpResponse != null) {

				try {
					jsonResponse = EntityUtils.toString(httpResponse.getEntity());
					Utility.printLog("CheckMobile Response: " + jsonResponse);
				} catch (ParseException e) {
					e.printStackTrace();
					Utility.printLog(TAG, "doPost Exception......." + e);
				} catch (IOException e) {
					e.printStackTrace();
					Utility.printLog(TAG, "doPost Exception......." + e);
				}
			}
			try {

				if (jsonResponse != null) {
					Gson gson = new Gson();
					mobileResponse = gson.fromJson(jsonResponse, CheckMobileNoResponse.class);
				} else {
					runOnUiThread(new Runnable() {
						public void run() {
							Toast.makeText(getApplicationContext(), getResources().getString(R.string.requestTimeout), Toast.LENGTH_SHORT).show();
						}
					});
				}
			} catch (Exception e) {
				Utility.ShowAlert(getResources().getString(R.string.server_error), SignupActivity.this);
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (dialogL != null) {
				dialogL.dismiss();
			}
			if (mobileResponse != null) {
				Utility.printLog("akbar" + mobileResponse.getErrFlag());
				Utility.printLog("akbar" + mobileResponse.getErrMsg());


				if (mobileResponse.getErrFlag().equals("0")) {
					//isMobileValid = true;

					session.clearSession();
					session.storeCurrencySymbol("$");
					session.storeRegistrationId(regid);
					session.storeDeviceId(deviceid);

					//showAlert(mobileResponse.getErrMsg());


						/*Intent intent=new Intent(SignupActivity.this,SignupPayment.class);
					intent.putExtra("REGID",regid);
					intent.putExtra("DEVICEID",deviceid);

					Utility.printLog("Before sendin deviceId"+deviceid);
					Utility.printLog("Before sendin regId"+regid);
					intent.putExtra("FIRSTNAME",firstName.getText().toString());

					if(lastName.getText()!=null)
						intent.putExtra("LASTNAME",lastName.getText().toString());

					intent.putExtra("EMAIL",email.getText().toString());
					intent.putExtra("PASSWORD",password.getText().toString());
					intent.putExtra("MOBILE",mobileNo.getText().toString());
					startActivity(intent);
					SignupActivity.this.finish();
					overridePendingTransition(R.anim.anim_two, R.anim.anim_one);*/

				} else {
					//isMobileValid = false;
					showAlert(mobileResponse.getErrMsg());
					mobileNo.setText(null);
				}
			} else {

				//isMobileValid = false;
				mobileNo.setText(null);
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SignupActivity.this);

				// set title
				alertDialogBuilder.setTitle(getResources().getString(R.string.alert));

				// set dialog message
				alertDialogBuilder
						.setMessage(getResources().getString(R.string.server_error))
						.setCancelable(false)

						.setNegativeButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// if this button is clicked, just close
								// the dialog box and do nothing
								finish();
							}
						});
				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();
				// show it
				alertDialog.show();


			}
		}
	}


	class BackgroundValidateEmail extends AsyncTask<String, Void, String> {
		BookAppointmentResponse response;
		ProgressDialog dialogL;

		protected void onPreExecute() {
			super.onPreExecute();
			dialogL = Utility.GetProcessDialogNew(SignupActivity.this, "Validating Email...");
			dialogL.setCancelable(true);
			if (dialogL != null) {
				dialogL.setCancelable(false);
				dialogL.show();
			}
		}

		@Override
		protected String doInBackground(String... params) {
			String url = VariableConstants.BASE_URL + "validateEmailZip";

			Utility utility = new Utility();
			String curenttime = utility.getCurrentGmtTime();
			Log.i("", "dateandTime " + curenttime);

			Map<String, String> kvPairs = new HashMap<String, String>();

			kvPairs.put("ent_email", email.getText().toString());
			//kvPairs.put("zip_code",zipcode.getText().toString());
			//kvPairs.put("zip_code","90637");
			kvPairs.put("ent_user_type", "2");
			kvPairs.put("ent_date_time", curenttime);

			HttpResponse httpResponse = null;
			try {
				httpResponse = Utility.doPost(url, kvPairs);
			} catch (ClientProtocolException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				Utility.printLog("doPost Exception......." + e1);
			} catch (IOException e1) {
				e1.printStackTrace();
				Utility.printLog("doPost Exception......." + e1);
			}

			String jsonResponse = null;
			if (httpResponse != null) {

				try {
					jsonResponse = EntityUtils.toString(httpResponse.getEntity());
					Utility.printLog("GetDoctoraround Response: ", jsonResponse);
				} catch (ParseException e) {
					e.printStackTrace();
					Utility.printLog(TAG, "doPost Exception......." + e);
				} catch (IOException e) {
					e.printStackTrace();
					Utility.printLog(TAG, "doPost Exception......." + e);
				}
			}
			try {
				if (jsonResponse != null) {
					Gson gson = new Gson();
					response = gson.fromJson(jsonResponse, BookAppointmentResponse.class);
					Log.i("", "DONE WITH GSON");
				} else {
					runOnUiThread(new Runnable() {
						public void run() {
							Toast.makeText(getApplicationContext(), getResources().getString(R.string.requestTimeout), Toast.LENGTH_SHORT).show();
						}
					});
				}
			} catch (Exception e) {
				Utility.ShowAlert(getResources().getString(R.string.server_error), SignupActivity.this);
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (dialogL != null) {
				dialogL.dismiss();
			}
			if (response != null) {
				if (response.getErrFlag().equals("0")) {
					isEmailVaild = true;

						/*session.clearSession();
					session.storeCurrencySymbol("$");
					session.storeRegistrationId(regid);
					session.storeDeviceId(deviceid);

					Intent intent=new Intent(SignupActivity.this,SignupPayment.class);
					intent.putExtra("REGID",regid);
					intent.putExtra("DEVICEID",deviceid);

					Utility.printLog("Before sendin deviceId"+deviceid);
					Utility.printLog("Before sendin regId"+regid);
					intent.putExtra("FIRSTNAME",firstName.getText().toString());

					if(lastName.getText()!=null)
						intent.putExtra("LASTNAME",lastName.getText().toString());

					intent.putExtra("EMAIL",email.getText().toString());
					intent.putExtra("PASSWORD",password.getText().toString());
					intent.putExtra("MOBILE",mobileNo.getText().toString());
					//intent.putExtra("CITY_NAME","La Mirada");
					//intent.putExtra("CITY_NAME",current_city_name);
					//intent.putExtra("ProfileImage", mFileTemp);
					startActivity(intent);
					SignupActivity.this.finish();
					overridePendingTransition(R.anim.anim_two, R.anim.anim_one);*/

				} else {
					isEmailVaild = false;
					showAlert(response.getErrMsg());
					email.setText(null);
				}
			} else {
				isEmailVaild = false;

				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SignupActivity.this);

				// set title
				alertDialogBuilder.setTitle(getResources().getString(R.string.alert));

				// set dialog message
				alertDialogBuilder
						.setMessage(getResources().getString(R.string.server_error))
						.setCancelable(false)

						.setNegativeButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// if this button is clicked, just close
								// the dialog box and do nothing
								finish();
							}
						});
				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();
				// show it
				alertDialog.show();
			}
		}
	}

	class BackgroundForGettingVerificationCode extends AsyncTask<String, Void, String> {
		VerificationCodeResponse verificationResponse;
		ProgressDialog dialogL;

		protected void onPreExecute() {
			super.onPreExecute();
			dialogL = Utility.GetProcessDialogNew(SignupActivity.this, "Wait for verification code...");
			dialogL.setCancelable(true);
			if (dialogL != null) {
				dialogL.setCancelable(false);
				dialogL.show();
			}
		}

		@Override
		protected String doInBackground(String... params) {
			String url = VariableConstants.BASE_URL + "getVerificationCode";

			Utility utility = new Utility();
			String curenttime = utility.getCurrentGmtTime();
			Log.i("", "dateandTime " + curenttime);

			fullMobileNumber = signup_phone_code.getText().toString() + mobileNo.getText().toString();

			Utility.printLog("The Value of full mobile number :: " + fullMobileNumber);

			Map<String, String> kvPairs = new HashMap<String, String>();

			kvPairs.put("ent_mobile", fullMobileNumber);
			kvPairs.put("ent_sess_token", regid);
			kvPairs.put("ent_dev_id", deviceid);
			Log.i("getVerificationCode", "request " + kvPairs);

			HttpResponse httpResponse = null;
			try {
				httpResponse = Utility.doPost(url, kvPairs);
			} catch (ClientProtocolException e1) {
				e1.printStackTrace();
				Utility.printLog("doPost Exception......." + e1);
			} catch (IOException e1) {
				e1.printStackTrace();
				Utility.printLog("doPost Exception......." + e1);
			}

			String jsonResponse = null;
			if (httpResponse != null) {

				try {
					jsonResponse = EntityUtils.toString(httpResponse.getEntity());
					Utility.printLog("getVerification code Response: " + jsonResponse);

					Log.i("getVerificationCode", "responce " + jsonResponse);
				} catch (ParseException e) {
					e.printStackTrace();
					Utility.printLog(TAG, "doPost Exception......." + e);
				} catch (IOException e) {
					e.printStackTrace();
					Utility.printLog(TAG, "doPost Exception......." + e);
				}
			}
			try {

				if (jsonResponse != null) {
					Gson gson = new Gson();
					verificationResponse = gson.fromJson(jsonResponse, VerificationCodeResponse.class);
					Utility.printLog("response" + verificationResponse);
					Log.i("", "DONE WITH GSON");
				} else {
					runOnUiThread(new Runnable() {
						public void run() {
							Toast.makeText(getApplicationContext(), getResources().getString(R.string.requestTimeout), Toast.LENGTH_SHORT).show();
						}
					});
				}
			} catch (Exception e) {
				Utility.ShowAlert(getResources().getString(R.string.server_error), SignupActivity.this);
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (dialogL != null) {
				dialogL.dismiss();
			}
			if (verificationResponse != null) {


				if (verificationResponse.getErrFlag().equals("0")) {
					isEmailVaild = true;

					session.clearSession();
					session.storeCurrencySymbol("$");
					session.storeRegistrationId(regid);
					session.storeDeviceId(deviceid);


					geTVerificationpopup();

						/*Intent intent=new Intent(SignupActivity.this,SignupVerificationActivity.class);

						intent.putExtra("REGID",regid);
						intent.putExtra("gender", gender);
						intent.putExtra("DEVICEID", deviceid);
						*//*Utility.printLog("Before sendin deviceId"+deviceid);
						Utility.printLog("Before sendin regId"+regid);*//*
						intent.putExtra("FIRSTNAME",firstName.getText().toString());
						if(lastName.getText()!=null)
							intent.putExtra("LASTNAME",lastName.getText().toString());
						intent.putExtra("EMAIL",email.getText().toString());
						intent.putExtra("PASSWORD",password.getText().toString());
						intent.putExtra("MOBILE",mobileNo.getText().toString());
						startActivity(intent);
						SignupActivity.this.finish();
						overridePendingTransition(R.anim.anim_two, R.anim.anim_one);*/

				} else {
					isEmailVaild = false;
					showAlert(verificationResponse.getErrMsg());
				}


			} else {


				isEmailVaild = false;

				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SignupActivity.this);

				// set title
				alertDialogBuilder.setTitle(getResources().getString(R.string.alert));

				// set dialog message
				alertDialogBuilder
						.setMessage(getResources().getString(R.string.server_error))
						.setCancelable(false)

						.setNegativeButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// if this button is clicked, just close
								// the dialog box and do nothing
								finish();
							}
						});
				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();
				// show it
				alertDialog.show();


			}
		}
	}

	private void geTVerificationpopup() {


		fDialog = new Dialog(SignupActivity.this);

		fDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		fDialog.setContentView(R.layout.forgotpasspopup);

		Button okButton, cancelButton;


		et_verificationcode = (EditText) fDialog.findViewById(R.id.et_verificationcode);
		//et_verificationcode.setHint("OTP");
		okButton = (Button) fDialog.findViewById(R.id.okButton);
		cancelButton = (Button) fDialog.findViewById(R.id.cancelButton);

		//======================My Change=============================
		Typeface roboto_condensed = Typeface.createFromAsset(SignupActivity.this.getAssets(),"fonts/BebasNeue.otf");
	TextView	fortext = (TextView) fDialog.findViewById(R.id.fortext);
		fortext.setTypeface(roboto_condensed);
		okButton.setTypeface(roboto_condensed);
		cancelButton.setTypeface(roboto_condensed);
		//email= (EditText) fDialog.findViewById(R.id.email);

		okButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				int count = et_verificationcode.getText().toString().length();
				if (count == 5) {
					new BackgroundForValid_VerificationCode().execute();
					fDialog.dismiss();
				} else {
					Toast.makeText(SignupActivity.this, "Please Enter OTP", Toast.LENGTH_SHORT).show();
				}
			}
		});

		cancelButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				fDialog.dismiss();
			}
		});

		et_verificationcode.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

				Utility.printLog(" inside onTextChanged  :: " + count);


				count = s.length();

				Utility.printLog(" inside onTextChanged  s.length() :: " + count);


				if (count == 5) {
					Utility.printLog("inside onTextChanged  length is 5  ::");

					new BackgroundForValid_VerificationCode().execute();
					fDialog.dismiss();

				}

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

				Utility.printLog(" inside beforeTextChanged  :: " + count);

				count = s.length();

				Utility.printLog(" inside beforeTextChanged  s.length() :: " + count);

			}

			@Override
			public void afterTextChanged(Editable s) {

				Utility.printLog(" inside afterTextChanged   :: " + s);

				Utility.printLog(" inside afterTextChanged  s.length() :: " + s.length());

			}
		});


		fDialog.show();

	}


	class BackgroundForValid_VerificationCode extends AsyncTask<String, Void, String> {
		ValidVerificationCodeResponse response;
		ProgressDialog dialogL;

		protected void onPreExecute() {
			super.onPreExecute();
			dialogL = Utility.GetProcessDialogNew(SignupActivity.this, "Validating verification code...");
			dialogL.setCancelable(true);
			if (dialogL != null) {
				dialogL.setCancelable(false);
				dialogL.show();
			}
		}

		@Override
		protected String doInBackground(String... params) {
			String url = VariableConstants.BASE_URL + "verifyPhone";

			Utility utility = new Utility();
			String curenttime = utility.getCurrentGmtTime();
			Log.i("", "dateandTime " + curenttime);

			Map<String, String> kvPairs = new HashMap<String, String>();

			kvPairs.put("ent_phone", signup_phone_code.getText().toString() + mobileNo.getText().toString());
			kvPairs.put("ent_sess_token", regid);
			kvPairs.put("ent_dev_id", deviceid);
			kvPairs.put("ent_code", et_verificationcode.getText().toString());

			Log.i("verifyPhone", "request" + kvPairs);

			Utility.printLog("The kvPair values in Valid Verification Code Activity is :: " + kvPairs);

			HttpResponse httpResponse = null;
			try {
				httpResponse = Utility.doPost(url, kvPairs);
			} catch (ClientProtocolException e1) {
				e1.printStackTrace();
				Utility.printLog("doPost Exception......." + e1);
			} catch (IOException e1) {
				e1.printStackTrace();
				Utility.printLog("doPost Exception......." + e1);
			}

			String jsonResponse = null;
			if (httpResponse != null) {

				try {
					jsonResponse = EntityUtils.toString(httpResponse.getEntity());
					Utility.printLog("Check Valid verification Code Response: " + jsonResponse);
					Log.i("verifyPhone", "responce" + jsonResponse);
				} catch (ParseException e) {
					e.printStackTrace();
					Utility.printLog("doPost Exception......." + e);
				} catch (IOException e) {
					e.printStackTrace();
					Utility.printLog("doPost Exception......." + e);
				}
			}
			try {

				if (jsonResponse != null) {
					Gson gson = new Gson();
					response = gson.fromJson(jsonResponse, ValidVerificationCodeResponse.class);
					Log.i("", "DONE WITH GSON");
				} else {
					runOnUiThread(new Runnable() {
						public void run() {
							Toast.makeText(getApplicationContext(), getResources().getString(R.string.requestTimeout), Toast.LENGTH_SHORT).show();
						}
					});
				}


			} catch (Exception e) {
				Utility.ShowAlert(getResources().getString(R.string.server_error), SignupActivity.this);
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (dialogL != null) {
				dialogL.dismiss();
			}
			if (response != null) {


				if (response.getErrFlag().equals("0")) {



				/*	Intent intent=new Intent(SignupVerificationActivity.this,SignupVerificationActivity.class);
					intent.putExtra("REGID",regid);
					intent.putExtra("DEVICEID",deviceid);

					Utility.printLog("Before sendin deviceId"+deviceid);
					Utility.printLog("Before sendin regId"+regid);
					intent.putExtra("FIRSTNAME",firstName);

					if(lastName!=null)
						intent.putExtra("LASTNAME",lastName);

					intent.putExtra("EMAIL",email);
					intent.putExtra("PASSWORD",password);
					intent.putExtra("MOBILE",mobileNo);

					startActivity(intent);
					SignupVerificationActivity.this.finish();
					overridePendingTransition(R.anim.anim_two, R.anim.anim_one);*/
					if (Utility.isNetworkAvailable(SignupActivity.this))

						new BackgroundTaskSignUp().execute();

					else
						Utility.ShowAlert(getResources().getString(R.string.network_connection_fail), SignupActivity.this);
				} else {
					showAlert(response.getErrMsg());
					et_verificationcode.setText(null);
				}


			} else {


				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SignupActivity.this);

				// set title
				alertDialogBuilder.setTitle(getResources().getString(R.string.alert));

				// set dialog message
				alertDialogBuilder
						.setMessage(getResources().getString(R.string.server_error))
						.setCancelable(false)

						.setNegativeButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// if this button is clicked, just close
								// the dialog box and do nothing
								finish();
							}
						});
				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();
				// show it
				alertDialog.show();


			}
		}
	}


	private void ValidatePromoCode(final String promoCode) {
		final ProgressDialog dialogL = Utility.GetProcessDialogNew(SignupActivity.this, "Validating Referral Code...");
		dialogL.setCancelable(true);
		if (dialogL != null) {
			dialogL.show();
		}
		RequestQueue volleyRequest = Volley.newRequestQueue(SignupActivity.this);

		StringRequest myReq = new StringRequest(Request.Method.POST, VariableConstants.BASE_URL + "verifyCode",
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						Utility.printLog("Success of getting ValidatePromoCode " + response);
						JSONObject jsnResponse;
						try {
							dialogL.dismiss();
							jsnResponse = new JSONObject(response);
							String mErrNum = jsnResponse.getString("errNum");
							Utility.printLog("jsonErrorParsing is ---> " + mErrNum);

							if (jsnResponse.getString("errFlag").equals("0")) {
								Toast.makeText(SignupActivity.this, jsnResponse.getString("errMsg"), Toast.LENGTH_LONG).show();
								session.clearSession();
								session.storeCurrencySymbol("₹");

								//session.storeCurrencySymbol(R.string.rs);


								geTVerificationpopup();

								session.storeRegistrationId(regid);
								session.storeDeviceId(deviceid);

							/*Intent intent=new Intent(SignupActivity.this,SignupVerificationActivity.class);
							intent.putExtra("REGID",regid);
							intent.putExtra("DEVICEID",deviceid);
							intent.putExtra("gender",gender);
							Utility.printLog("Before sendin regId"+regid);
							intent.putExtra("FIRSTNAME",firstName.getText().toString());

							if(lastName.getText()!=null)

								intent.putExtra("LASTNAME",lastName.getText().toString());
							intent.putExtra("EMAIL",email.getText().toString());
							intent.putExtra("PASSWORD",password.getText().toString());
							intent.putExtra("MOBILE",mobileNo.getText().toString());
							intent.putExtra("referral", promoCode);
							//intent.putExtra("CITY_NAME","La Mirada");
							//intent.putExtra("CITY_NAME",current_city_name);
							//intent.putExtra("ProfileImage", mFileTemp);

							startActivity(intent);
							SignupActivity.this.finish();
							overridePendingTransition(R.anim.anim_two, R.anim.anim_one);*/
							} else {
								referral_code.setText(null);
								Utility.ShowAlert(jsnResponse.getString("errMsg"), SignupActivity.this);
							}

						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				dialogL.dismiss();
				Toast.makeText(SignupActivity.this, getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
				Utility.printLog("Error for volley");
			}
		}) {
			protected HashMap<String, String> getParams() throws AuthFailureError {
				HashMap<String, String> kvPair = new HashMap<String, String>();

				Utility.printLog("ValidatePromoCode promoCode=" + promoCode);
				Utility.printLog("ValidatePromoCode promoCode currentLatitude =" + currentLatitude);
				Utility.printLog("ValidatePromoCode promoCode currentLongitude =" + currentLongitude);

				kvPair.put("ent_coupon", promoCode);
				kvPair.put("ent_lat", "" + currentLatitude);
				kvPair.put("ent_long", "" + currentLongitude);

				return kvPair;
			}

			;
		};
		volleyRequest.add(myReq);
	}

	@Override
	protected void onResume() {
		super.onResume();

		initialize();
			/*if( !(firstName.getText().toString().equals("")) && !(confirm_password.getText().toString().equals("")))
		{
			getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		}
		else
		{
			getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		}*/
	}


	@Override
	public void onBackPressed() {

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SignupActivity.this);

		// set title
		alertDialogBuilder.setTitle(getResources().getString(R.string.cancel_account_creation));

		// set dialog message
		alertDialogBuilder
				.setMessage(getResources().getString(R.string.cancel_account_creation_alert))
				.setCancelable(false)
				.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// if this button is clicked, close
						// current activity
						Intent intent = new Intent(SignupActivity.this, MainActivity.class);
						intent.putExtra("NO_ANIMATION", true);
						startActivity(intent);
						overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
						SignupActivity.this.finish();
					}
				})
				.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// if this button is clicked, just close
						// the dialog box and do nothing
						dialog.cancel();
					}
				});
		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();
		// show it
		alertDialog.show();
	}

	@Override
	protected void onStart() {
		super.onStart();
		// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
		//client.connect();
		FlurryAgent.onStartSession(this, "8c41e9486e74492897473de501e087dbc6d9f391");

		/*// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
		Action viewAction = Action.newAction(
				Action.TYPE_VIEW, // TODO: choose an action type.
				"Signup Page", // TODO: Define a title for the content shown.
				// TODO: If you have web page content that matches this app activity's content,
				// make sure this auto-generated web page URL is correct.
				// Otherwise, set the URL to null.
				Uri.parse("http://host/path"),
				// TODO: Make sure this auto-generated app deep link URI is correct.
				Uri.parse("android-app://com.eserviss.passenger.main/http/host/path")
		);
		AppIndex.AppIndexApi.start(client, viewAction);*/
	}

	@Override
	protected void onStop() {
		super.onStop();

		FlurryAgent.onEndSession(this);
		/*// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
		Action viewAction = Action.newAction(
				Action.TYPE_VIEW, // TODO: choose an action type.
				"Signup Page", // TODO: Define a title for the content shown.
				// TODO: If you have web page content that matches this app activity's content,
				// make sure this auto-generated web page URL is correct.
				// Otherwise, set the URL to null.
				Uri.parse("http://host/path"),
				// TODO: Make sure this auto-generated app deep link URI is correct.
				Uri.parse("android-app://com.eserviss.passenger.main/http/host/path")
		);
		AppIndex.AppIndexApi.end(client, viewAction);
		FlurryAgent.onEndSession(this);
		// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
		client.disconnect();*/
	}

	private class BackgroundTaskSignUp extends AsyncTask<String, Void, String> {
		SignUpResponse response;
		String jsonResponse;
		ProgressDialog dialogL;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialogL = Utility.GetProcessDialogNew(SignupActivity.this, getResources().getString(R.string.signing_you_up));
			dialogL.setCancelable(true);
			if (dialogL != null) {
				dialogL.setCancelable(false);
				dialogL.show();
			}
		}

		@Override
		protected String doInBackground(String... params) {
			Map<String, String> kvPairs = new HashMap<String, String>();

			String url = VariableConstants.BASE_URL + "slaveSignup";

			Utility utility = new Utility();
			String curenttime = utility.getCurrentGmtTime();

			Utility.printLog("BackgroundTaskSignUp currentLatitude=" + currentLatitude + " currentLongitude=" + currentLongitude);


			//if(access_token!=null)

			kvPairs.put("ent_first_name", firstName.getText().toString());
			kvPairs.put("ent_last_name", lastName.getText().toString());
			kvPairs.put("ent_email", email.getText().toString());
			kvPairs.put("ent_password", password.getText().toString());
			kvPairs.put("ent_mobile", mobileNo.getText().toString());
			kvPairs.put("ent_gender", gender);
			//kvPairs.put("ent_city",current_city_name);
			kvPairs.put("ent_latitude", String.valueOf(currentLatitude));
			kvPairs.put("ent_longitude", String.valueOf(currentLongitude));
			//kvPairs.put("ent_latitude","13.02882275");
			//kvPairs.put("ent_longitude","77.58966314");

			kvPairs.put("ent_terms_cond", "true");
			kvPairs.put("ent_pricing_cond", "true");
			kvPairs.put("ent_dev_id", deviceid);
			kvPairs.put("ent_push_token", regid);
			kvPairs.put("ent_device_type", "2");
			kvPairs.put("ent_date_time", curenttime);
			Utility.printLog("params" + kvPairs);
			Log.i("signup", "request " + kvPairs);
			HttpResponse httpResponse = null;
			try {
				httpResponse = Utility.doPost(url, kvPairs);
			} catch (ClientProtocolException e1) {
				e1.printStackTrace();
				Utility.printLog(TAG, "doPost Exception......." + e1);
			} catch (IOException e1) {
				e1.printStackTrace();
				Utility.printLog(TAG, "doPost Exception......." + e1);
			}

			if (httpResponse != null) {

				try {
					jsonResponse = EntityUtils.toString(httpResponse.getEntity());
					Utility.printLog("SignUp Response: " + jsonResponse);
					Log.i("signup", "Response " + jsonResponse);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Utility.printLog(TAG, "SignUp Response doPost ParseException......." + e);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Utility.printLog(TAG, "SignUp Response doPost IOException......." + e);
				}
			}

			if (jsonResponse != null) {
				Gson gson = new Gson();
				response = gson.fromJson(jsonResponse, SignUpResponse.class);
			} else {
				runOnUiThread(new Runnable() {
					public void run() {
						Toast.makeText(SignupActivity.this, getResources().getString(R.string.requestTimeout), Toast.LENGTH_SHORT).show();
					}
				});
			}

			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			Utility.printLog("SignUp Response onPostExecute" + jsonResponse);

			if (response != null) {
				if (response.getErrFlag().equals("0")) {
					SessionManager session = new SessionManager(SignupActivity.this);
					if (response.getToken() != null)
						session.storeSessionToken(response.getToken());
					session.storeRegistrationId(regid);
					session.storeLoginId(email.getText().toString());
					session.storeDeviceId(deviceid);
					session.setIsLogin(true);
					session.storeServerChannel(response.getServerChn());
					//session.storeLoginResponse(jsonResponse);
					session.storeCarTypes(jsonResponse);
					session.storeChannelName(response.getChn());
					session.storeCouponCode(response.getCoupon());

					name = firstName.getText().toString();

					if (Utility.isNetworkAvailable(SignupActivity.this)) {
						new BackGroundTaskForUploadImage().execute();
					} else {
						Utility.ShowAlert(getResources().getString(R.string.network_connection_fail), SignupActivity.this);
					}
				} else {
					Toast.makeText(SignupActivity.this, response.getErrMsg(), Toast.LENGTH_SHORT).show();
					Utility.printLog("", "PROBLEM : errorNumb : " + response.getErrNum());

					if (dialogL != null) {
						dialogL.dismiss();
					}
				}
			} else {
				Toast.makeText(SignupActivity.this, getResources().getString(R.string.server_error), Toast.LENGTH_SHORT).show();
				if (dialogL != null) {
					dialogL.dismiss();
				}
			}
		}
	}

	private class BackGroundTaskForUploadImage extends AsyncTask<String, Void, String> {
		private long chunkLength = 1000 * 1024;
		private long totalBytesRead = 0;
		private long bytesRemaining;
		private long FILE_SIZE;
		private String fileName;
		UploadImgeResponse response;
		private ProgressDialog dialogL;
		private List<NameValuePair> uploadNameValuePairList;
		File mFile;

		@Override
		protected String doInBackground(String... params) {

			FileInputStream fin = null;
			totalBytesRead = 0;
			bytesRemaining = 0;

			//mFile=new File("/sdcard"+"/SneekPeek", "picture"+".jpg");
			String state = Environment.getExternalStorageState();
			if (Environment.MEDIA_MOUNTED.equals(state)) {
				//mFile= new File(Environment.getExternalStorageDirectory(), VariableConstants.TEMP_PHOTO_FILE_NAME);
				mFile = mFileTemp;
			} else {
				//mFile= new File(getFilesDir(),VariableConstants.TEMP_PHOTO_FILE_NAME);
				mFile = mFileTemp;
			}


			String temp = Utility.getCurrentDateTimeStringGMT();
			temp = new String(temp.trim().replace(" ", "20"));
			temp = new String(temp.trim().replace(":", "20"));
			temp = new String(temp.trim().replace("-", "20"));

			fileName = "PA" + name + temp + mFile.getName();

			try {
				fin = new FileInputStream(mFile);
			} catch (FileNotFoundException e2) {
				e2.printStackTrace();
			}

			if (mFile.isFile() && mFile.length() > 0) {
				FILE_SIZE = mFile.length();
			}


			Utility.printLog(TAG, "BackGroundTaskForUploadImage doInBackground  FILE_SIZE " + FILE_SIZE);


			while (totalBytesRead < FILE_SIZE) {
				try {
					bytesRemaining = FILE_SIZE - totalBytesRead;

					if (bytesRemaining < chunkLength) // Remaining Data Part is Smaller Than CHUNK_SIZE
					{
						chunkLength = bytesRemaining;
					}

					byte[] chunk = null;
					chunk = new byte[(int) chunkLength];

					byte fileContent[] = new byte[(int) chunkLength];
					try {
						fin.read(fileContent, 0, (int) chunkLength);
					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}

					System.arraycopy(fileContent, 0, chunk, 0, (int) chunkLength);
					//byte[] encoded = Base64.encodeBase64(chunk);
					byte[] encoded = Base64.encode(chunk, Base64.NO_WRAP);
					String encodedString = new String(encoded);

					Utility.printLog("Base 64: " + encodedString);

					SessionManager session = new SessionManager(SignupActivity.this);
					String sessiontoken = session.getSessionToken();

					Utility utility = new Utility();
					String curenttime = utility.getCurrentGmtTime();
					//String dateandTime=UltilitiesDate.getLocalTime(curenttime);
					Utility.printLog("", "dataandTime " + curenttime);

					String[] uploadParameter = {sessiontoken, session.getDeviceId(), fileName, encodedString, "2", "1", "1", curenttime};

					Utility.printLog("BackGroundTaskForUploadImage params = " + uploadParameter);


					// String [] uploadParameter={sessiontoken,deviceid,fileName,encodedString,"2","1","Submit"};
					//  com.threembed.utilities.Utility utility=new com.threembed.utilities.Utility();

					uploadNameValuePairList = utility.getUploadParameter(uploadParameter);
					//String url=com.threembed.utilites.Constants.reqUrl+"sendASnap_result.php?user_session_token="+userSession+"&owner_id="+userId+"&media_name="+fileName+"&media_chunk="+encodedString+"&upload_media_submit=Upload";
					//Utility.printLog("", "url to send :"+url);

					totalBytesRead = totalBytesRead + chunkLength;

					String result = utility.makeHttpRequest(VariableConstants.BASE_URL + "uploadImage", "POST", uploadNameValuePairList);

					Utility.printLog("IMAGE UPLOAD", result);
					Utility.printLog("UPLOAD", "RESPONSE:  : " + result);

					if (result != null) {
						Gson gson = new Gson();
						response = gson.fromJson(result, UploadImgeResponse.class);
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			if (dialogL != null) {
				dialogL.dismiss();
			}

			if (response != null) {
				if (response.getErrFlag().equals("0")) {

					Toast.makeText(getApplicationContext(), getResources().getString(R.string.signup_success), Toast.LENGTH_SHORT).show();
					SessionManager session = new SessionManager(SignupActivity.this);
					session.setIsLogin(true);
					//Move to map activity
					Intent intent = new Intent(SignupActivity.this, BounceLogoScreen.class);
					startActivity(intent);
					finish();
					overridePendingTransition(R.anim.mainfadein, R.anim.splashfadeout);
				} else {
					//Utility.ShowAlert(response.getErrMsg(), SignupPayment.this);

					Toast.makeText(getApplicationContext(), getResources().getString(R.string.signup_success), Toast.LENGTH_SHORT).show();
					SessionManager session = new SessionManager(SignupActivity.this);
					session.setIsLogin(true);
					//Move to map activity
					Intent intent = new Intent(SignupActivity.this, BounceLogoScreen.class);
					startActivity(intent);
					finish();
					overridePendingTransition(R.anim.mainfadein, R.anim.splashfadeout);
				}
			} else {
				//Utility.ShowAlert(response.getErrMsg(), SignupPayment.this);

				Toast.makeText(getApplicationContext(), getResources().getString(R.string.signup_success), Toast.LENGTH_SHORT).show();
				SessionManager session = new SessionManager(SignupActivity.this);
				session.setIsLogin(true);
				//Move to map activity
				Intent intent = new Intent(SignupActivity.this, BounceLogoScreen.class);
				startActivity(intent);
				finish();
				overridePendingTransition(R.anim.mainfadein, R.anim.splashfadeout);
			}
		}
	}
}

