
package com.eserviss.passenger.main;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.egnyt.eserviss.MainActivity;
import com.eserviss.passenger.eseviss.passenger.crypto.CryptUtil;
import com.eserviss.passenger.eseviss.passenger.crypto.SaltedMD5;
import com.flurry.android.FlurryAgent;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.gson.Gson;
import com.eserviss.passenger.pojo.LoginResponse;
import com.egnyt.eserviss.R;
import com.google.gson.JsonArray;
import com.threembed.utilities.AppLocationService;
import com.threembed.utilities.SessionManager;
import com.threembed.utilities.Utility;
import com.threembed.utilities.VariableConstants;

public class SigninActivity extends Activity implements OnClickListener {
    private static final String TAG = "SigninActivity";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private EditText username, password;
    private TextView forgot_password;
    private ImageButton back;
    private Button login;
    private RelativeLayout RL_Signin;
    private GoogleCloudMessaging gcm;
    private static LoginResponse response;
    private SessionManager session;
    private double currentLatitude, currentLongitude;
    private String current_city_name;
    private AppLocationService appLocationService;
    private String regid, strServerResponse, jsonErrorParsing;
    private Context context;
    private String deviceid, SENDER_ID = VariableConstants.PROJECT_ID;
    Location gpsLocation;
    Dialog fDialog;
    EditText et_verificationcode;
    String cityName = "";
    String final_Step;
    String hash = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin);
        session = new SessionManager(SigninActivity.this);
        initialize();


        appLocationService = new AppLocationService(SigninActivity.this);

        gpsLocation = appLocationService.getLocation(LocationManager.GPS_PROVIDER);

        if (gpsLocation != null) {
            currentLatitude = gpsLocation.getLatitude();
            currentLongitude = gpsLocation.getLongitude();
        } else {
            Location nwLocation = appLocationService.getLocation(LocationManager.NETWORK_PROVIDER);

            if (nwLocation != null) {
                currentLatitude = nwLocation.getLatitude();
                currentLongitude = nwLocation.getLongitude();
            }
        }

		/*Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = null;
		try {
			addresses = geocoder.getFromLocation(currentLatitude, currentLongitude, 1);
			Log.i("address","is  "+addresses.size());
			if(addresses.size()>0)
			{
				Address returnedAddress = addresses.get(0);
				for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
					Log.e("Address ","--->"+addresses);
					//	String stateName = addresses.get(0).getAddressLine(1);
					cityName = addresses.get(0).getAddressLine(2);
				}
			}*/
        //	String addreline = addresses.get(0).getAddressLine(0);


		/*} catch (IOException e) {
			e.printStackTrace();

		}*/


        if (checkPlayServices()) {
            if (gcm == null) {
                gcm = GoogleCloudMessaging.getInstance(SigninActivity.this);
            }

            SessionManager session = new SessionManager(SigninActivity.this);
            regid = session.getRegistrationId();

            if (regid == null) {
                new BackgroundForRegistrationId().execute();
            } else {
                deviceid = getDeviceId(context);
            }

            Utility.printLog("doInBackground regid.........." + regid);
            Utility.printLog("doInBackground deviceid" + deviceid);

        } else {
            Utility.printLog("No valid Google Play Services APK found.");
        }
    }


    private class BackgroundForRegistrationId extends AsyncTask<String, Void, String> {
        private ProgressDialog dialogL;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            dialogL = Utility.GetProcessDialog(SigninActivity.this);

            if (dialogL != null) {
                dialogL.show();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            SessionManager session = new SessionManager(SigninActivity.this);
            Utility.printLog("===", "Inside DOINBACKGROUNG OF BackgroundForRegistrationId");

            try {

                deviceid = getDeviceId(SigninActivity.this);
                regid = gcm.register(SENDER_ID);

                session.storeRegistrationId(regid);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            Utility.printLog("===", "test deviceid" + deviceid);
            Utility.printLog("===", "test regid" + regid);
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (dialogL != null) {
                dialogL.dismiss();
            }

            if (regid == null) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SigninActivity.this);

                // set title
                alertDialogBuilder.setTitle(getResources().getString(R.string.alert));

                // set dialog message
                alertDialogBuilder
                        .setMessage(getResources().getString(R.string.slow_internet_connection))
                        .setCancelable(false)
				/*.setPositiveButton("Refresh",new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int id) {

						}
					  })*/
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


    private void initialize() {
        username = (EditText) findViewById(R.id.user_name);
        password = (EditText) findViewById(R.id.password);
        forgot_password = (TextView) findViewById(R.id.forgot_password);
        back = (ImageButton) findViewById(R.id.login_back_btn);
        login = (Button) findViewById(R.id.login_btn);
        RL_Signin = (RelativeLayout) findViewById(R.id.rl_signin);


        //======================My Change=============================
        Typeface roboto_condensed = Typeface.createFromAsset(SigninActivity.this.getAssets(), "fonts/BebasNeue.otf");

        TextView logintxt = (TextView) findViewById(R.id.logintxt);
        login.setTypeface(roboto_condensed);
        logintxt.setTypeface(roboto_condensed);


        login.setOnClickListener(this);
        forgot_password.setOnClickListener(this);
        back.setOnClickListener(this);
        RL_Signin.setOnClickListener(this);
        username.setOnFocusChangeListener(new OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                Utility.printLog("setOnFocusChangeListener hasFocus= " + hasFocus);
                if (!hasFocus) {

                    if (username.getText().toString().trim().isEmpty()) {
                        username.setError(getResources().getString(R.string.email_empty));
                    } else if (!validateEmail(username.getText().toString().trim())) {
                        username.setError(getResources().getString(R.string.invalid_email));
                    }


                }
            }
        });


        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                username.setError(null);
            }
        });


    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.login_back_btn) {
            Intent intent = new Intent(SigninActivity.this, MainActivity.class);
            intent.putExtra("NO_ANIMATION", true);
            startActivity(intent);
            //overridePendingTransition(R.anim.mainfadein, R.anim.splashfadeout);

            finish();
            overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
        }
        if (v.getId() == R.id.rl_signin) {
            Intent intent = new Intent(SigninActivity.this, MainActivity.class);
            intent.putExtra("NO_ANIMATION", true);
            startActivity(intent);
            //overridePendingTransition(R.anim.mainfadein, R.anim.splashfadeout);

            finish();
            overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
        }
        if (v.getId() == R.id.login_btn) {


            //new PasswordCheck().execute();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(login.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);

            if (validateFields()) {

                if (Utility.isNetworkAvailable(SigninActivity.this)) {
                    if (validateEmail(username.getText().toString())) {
                       // SECURELOGIN();
                        //  new PasswordCheck().execute();
                        UserLogin();
                    } else {
                        showAlert(getResources().getString(R.string.invalid_email));

                    }
                } else {
                    showAlert(getResources().getString(R.string.network_connection_fail));
                }
            }
            return;
        }

        if (v.getId() == R.id.forgot_password) {

            geTForgotpass();
			/*Intent intent=new Intent(SigninActivity.this,ForgotPwdActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.anim_two, R.anim.anim_one);*/
            //return;
        }
    }

    private void geTForgotpass() {
        fDialog = new Dialog(SigninActivity.this);
        fDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        fDialog.setContentView(R.layout.forgotpasspopup);


        Button okButton, cancelButton;
        TextView fortext, forget_pass_Tv;
        et_verificationcode = (EditText) fDialog.findViewById(R.id.et_verificationcode);
        okButton = (Button) fDialog.findViewById(R.id.okButton);
        cancelButton = (Button) fDialog.findViewById(R.id.cancelButton);
        fortext = (TextView) fDialog.findViewById(R.id.fortext);
        forget_pass_Tv = (TextView) fDialog.findViewById(R.id.forget_pass_Tv);
        forget_pass_Tv.setVisibility(View.VISIBLE);
        fortext.setText(getResources().getString(R.string.enterthemail));

        //======================My Change=============================
        Typeface roboto_condensed = Typeface.createFromAsset(SigninActivity.this.getAssets(), "fonts/BebasNeue.otf");
        et_verificationcode.setTypeface(roboto_condensed);
        forget_pass_Tv.setTypeface(roboto_condensed);
        fortext.setTypeface(roboto_condensed);
        okButton.setTypeface(roboto_condensed);
        cancelButton.setTypeface(roboto_condensed);

        cancelButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                fDialog.dismiss();
            }
        });
        okButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v)

            {
                if (!et_verificationcode.getText().toString().isEmpty()) {
                    if (validateEmail(et_verificationcode.getText().toString())) {
                        if (Utility.isNetworkAvailable(SigninActivity.this)) {
                            new BackgroundFrgtPwd().execute();
                            fDialog.dismiss();
                        } else

                            Toast.makeText(SigninActivity.this, getResources().getString(R.string.network_connection_fail), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(SigninActivity.this, getResources().getString(R.string.enter_valid_email), Toast.LENGTH_SHORT).show();

                    }
                } else {
                    Toast.makeText(SigninActivity.this, getResources().getString(R.string.email_empty), Toast.LENGTH_SHORT).show();

                }
            }
        });
        fDialog.show();

    }

    class BackgroundFrgtPwd extends AsyncTask<String, Void, String> {

        private String TAG = null;
        ProgressDialog dialogL;
        BookAppointmentResponse response;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            dialogL = Utility.GetProcessDialogNew(SigninActivity.this, getResources().getString(R.string.please_wait));
            dialogL.setCancelable(true);
            if (dialogL != null) {
                dialogL.show();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            String url = VariableConstants.BASE_URL + "forgotPassword";
            Map<String, String> kvPairs = new HashMap<String, String>();
            kvPairs.put("ent_email", et_verificationcode.getText().toString());
            kvPairs.put("ent_user_type", "2");

            Utility.printLog("ent_email: " + et_verificationcode.getText().toString());

            HttpResponse httpResponse = null;
            try {
                httpResponse = Utility.doPost(url, kvPairs);
            } catch (ClientProtocolException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                Log.i(TAG, "doPost Exception......." + e1);
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                Log.i(TAG, "doPost Exception......." + e1);
            }

            String jsonResponse = null;
            if (httpResponse != null) {

                try {
                    jsonResponse = EntityUtils.toString(httpResponse.getEntity());
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    Log.i(TAG, "doPost Exception......." + e);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    Log.i(TAG, "doPost Exception......." + e);
                }
            }
            Utility.printLog("Login Response: ", "Fwgt_pswd Response: " + jsonResponse);
            Utility.printLog("Login Response: ", jsonResponse);


            try {
                if (jsonResponse != null) {
                    Gson gson = new Gson();
                    response = gson.fromJson(jsonResponse, BookAppointmentResponse.class);
                } else {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(SigninActivity.this, getResources().getString(R.string.requestTimeout), Toast.LENGTH_SHORT).show();
                        }

                    });
                }

            } catch (Exception e) {
                Utility.ShowAlert(getResources().getString(R.string.server_error), SigninActivity.this);
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            if (dialogL != null) {
                dialogL.dismiss();
            }

            if (response != null) {
                if (response.getErrFlag().equals("0")) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            SigninActivity.this);

                    // set title
                    alertDialogBuilder.setTitle(getResources().getString(R.string.error));

                    // set dialog message
                    alertDialogBuilder
                            .setMessage(response.getErrMsg())
                            .setCancelable(false)

                            .setNegativeButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // if this button is clicked, just close
                                    // the dialog box and do nothing
                                    dialog.cancel();
                                    finish();
                                    overridePendingTransition(R.anim.anim_three, R.anim.anim_four);
                                }
                            });

                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    // show it
                    alertDialog.show();

                } else {
                    Utility.ShowAlert(response.getErrMsg(), SigninActivity.this);
                }
            } else {
                Utility.ShowAlert(getResources().getString(R.string.server_error), SigninActivity.this);
            }
        }
    }

    //=================================My Change==========================
    private void SECURELOGIN() {

        final ProgressDialog dialogL = Utility.GetProcessDialogNew(SigninActivity.this, "Signing ...");
        dialogL.setCancelable(false);
        if (dialogL != null) {
            dialogL.show();
        }

        RequestQueue volleyRequest = Volley.newRequestQueue(SigninActivity.this);

        Map<String, String> jsonParams = new HashMap<String, String>();
        JsonObjectRequest myRequest = new JsonObjectRequest(
                Request.Method.GET,
                "https://eserviss.com/christmas/user_secured_apis.php?token=f837f625e15edf84dff132959e79e&Nsignin1=true&uid=\"+deviceid",
                new JSONObject(jsonParams),

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        dialogL.dismiss();
                        try {
                            VolleyLog.v("Response:%n %s 12345", response.toString(4));

                            Log.e("SIGNIN 1 ", response.toString());

                            String status = response.getString("status");
                            if (status.equalsIgnoreCase("success")) {
                                String result = response.getString("result");
                                byte[] bytes = result.getBytes("UTF-8");
                                //  hash=SaltedMD5.getSecurePassword("123456",bytes).toString();
                                //==========MY CHANGE======================
                                try {
                                    hash = SaltedMD5.stringtoMD5(SigninActivity.this, password.getText().toString());
                                    String step_comb = SaltedMD5.stringtoMD5(SigninActivity.this, hash + "" + result);
                                    final_Step = SaltedMD5.makeSHA1Hash(step_comb);
                                    //Toast.makeText(SigninActivity.this,step_comb,Toast.LENGTH_LONG).show();

                                } catch (NoSuchAlgorithmException e) {
                                    e.printStackTrace();
                                }
                                encryptpassword(final_Step, result);

                            } else {
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.e("Error: ", error.getMessage());
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("AUTH_EMAIL", username.getText().toString());
                return headers;
            }
        };

        volleyRequest.add(myRequest);

    }

    //=======================Sending encrypted password======================
    public void encryptpassword(final String hash_value, final String salt) {

        RequestQueue volleyRequest = Volley.newRequestQueue(SigninActivity.this);

        Map<String, String> jsonParams1 = new HashMap<String, String>();
        JsonObjectRequest req = new JsonObjectRequest(
                Request.Method.POST,
                "https://eserviss.com/christmas/user_secured_apis.php?token=f837f625e15edf84dff132959e79e&Nsignin2=true&uid=" + deviceid,


                new JSONObject(jsonParams1),

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            VolleyLog.v("ENCRYPTED PASS", response.toString(4));

                            Log.e("SIGNIN 1 for service", response.toString());
                            String status = response.getString("status");
                            if (status.equalsIgnoreCase("SUCCESS")) {
                                JSONObject jsonObject = response.getJSONObject("result");
                                String id = jsonObject.getString("id");
                                String firstName = jsonObject.getString("firstName");
                                String lastName = jsonObject.getString("lastName");
                                String gender = jsonObject.getString("gender");
                                String mobile = jsonObject.getString("mobile");
                                String profilePicture = jsonObject.getString("profilePicture");
                                String email = jsonObject.getString("email");
                                String stripeCustomerId = jsonObject.getString("stripeCustomerId");
                                String stripeCardId = jsonObject.getString("stripeCardId");
                                String valid = jsonObject.getString("valid");


                                ///Signup1
                                Signup1(hash_value, salt, firstName, lastName, email, gender, mobile);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.e("Error: ", error.getMessage());
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                Log.e("hash value", "" + hash_value);
                headers.put("AUTH_HASH", hash_value);
                headers.put("AUTH_EMAIL", username.getText().toString());
                Log.e("hash", "" + headers);

                return headers;
            }
        };
        Log.e("Response: 12345", "" + req);
        volleyRequest.add(req);
    }


    //=======================Sign up 1======================
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void Signup1(final String hash_value, final String salt, final String f_name, final String l_name, final String email,
                        final String gender, final String mobile) throws JSONException {

        RequestQueue volleyRequest = Volley.newRequestQueue(SigninActivity.this);
        StringRequest myReq = new StringRequest(Request.Method.POST, VariableConstants.BASE_URL + "slaveSignup1",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        Utility.printLog("Success of getting user login Info" + response);

                        Log.e("signup1", "responce " + response);


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(SigninActivity.this, getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
                Utility.printLog("Error for volley getLoupon info");
            }
        }) {
            protected HashMap<String, String> getParams() throws com.android.volley.AuthFailureError {
                HashMap<String, String> kvPairs = new HashMap<String, String>();


                String url = VariableConstants.BASE_URL + "slaveSignup1";

                Utility utility = new Utility();
                String curenttime = utility.getCurrentGmtTime();

                kvPairs.put("ent_first_name", f_name);
                kvPairs.put("ent_last_name", l_name);
                kvPairs.put("ent_email", email);
                kvPairs.put("ent_password", password.getText().toString());
                kvPairs.put("ent_gender", gender);
                kvPairs.put("ent_mobile", mobile);//fullMobileNumber mobileNo
                kvPairs.put("ent_latitude", String.valueOf(currentLatitude));
                kvPairs.put("ent_longitude", String.valueOf(currentLongitude));
                kvPairs.put("ent_terms_cond", "true");
                kvPairs.put("ent_pricing_cond", "true");
                kvPairs.put("ent_dev_id", deviceid);
                kvPairs.put("ent_push_token", regid);
                kvPairs.put("ent_device_type", "2");
                kvPairs.put("ent_date_time", curenttime);
                kvPairs.put("ent_city", "");
                Log.e("pairs", kvPairs.toString());


                return kvPairs;
            }


        };

/*
        JsonObjectRequest req = new JsonObjectRequest(
                Request.Method.GET,
                VariableConstants.BASE_URL + "slaveSignup1",


                new JSONObject(kvPairs),

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            VolleyLog.v("Response:%n %s 12345", response.toString(4));

                            Log.e("RESPONSE FROM SERVER", response.toString());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.e("Error: ", error.getMessage());
                    }
                }) {

            *//*@Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                Log.e("hash value", "" + hash_value);
                headers.put("AUTH_HASH", hash_value);
                headers.put("AUTH_SALT", salt);
                headers.put("AUTH_EMAIL", username.getText().toString());
                headers.put("ent_first_name", f_name);
                Log.e("hash", "" + headers);

                return headers;
            }*//*
        };*/
        Log.e("Response: 12345", "" + myReq);
        volleyRequest.add(myReq);
    }


    //=================================My Change==========================//
    private void UserLogin() {
        final ProgressDialog dialogL = Utility.GetProcessDialogNew(SigninActivity.this, "Signing in...");
        dialogL.setCancelable(false);
        if (dialogL != null) {
            dialogL.show();
        }

        RequestQueue volleyRequest = Volley.newRequestQueue(SigninActivity.this);
        StringRequest myReq = new StringRequest(Request.Method.POST, VariableConstants.BASE_URL + "slaveLogin",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        if (dialogL != null) {
                            dialogL.cancel();
                        }
                        strServerResponse = response;
                        Utility.printLog("Success of getting user login Info" + response);

                        Log.i("signin", "responce " + response);

                        getUserLoginInfo(dialogL);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (dialogL != null) {
                    dialogL.cancel();
                }
                Toast.makeText(SigninActivity.this, getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
                Utility.printLog("Error for volley getLoupon info");
            }
        }) {
            protected HashMap<String, String> getParams() throws com.android.volley.AuthFailureError {
                HashMap<String, String> kvPair = new HashMap<String, String>();

                Utility utility = new Utility();
                String curenttime = utility.getCurrentGmtTime();
                Utility.printLog("ent_email", username.getText().toString());
                Utility.printLog("ent_password", password.getText().toString());
                Utility.printLog("ent_dev_id", deviceid);
                Utility.printLog("ent_push_token", regid);
                Utility.printLog("ent_date_time " + curenttime);
				/*if(!cityName.equals(""))
				{
					kvPair.put("ent_city",cityName);
				}
				else
				{
					kvPair.put("ent_city","Bangalore");
				}*/
                kvPair.put("ent_email", username.getText().toString());
                kvPair.put("ent_password", password.getText().toString());
                kvPair.put("ent_dev_id", deviceid);
                kvPair.put("ent_push_token", regid);
                kvPair.put("ent_latitude", "" + currentLatitude);
                kvPair.put("ent_longitude", "" + currentLongitude);
                //kvPair.put("ent_latitude","13.628756");
                //kvPair.put("ent_longitude","79.419179");
                kvPair.put("ent_device_type", "2");
                kvPair.put("ent_date_time", curenttime);

                Log.i("signin", "request " + kvPair);

                return kvPair;
            }

            ;
        };
        volleyRequest.add(myReq);
    }

    private void getUserLoginInfo(ProgressDialog dialogL) {
        dialogL.dismiss();
        try {
            JSONObject jsnResponse = new JSONObject(strServerResponse);

            jsonErrorParsing = jsnResponse.getString("errFlag");

            Utility.printLog("jsonErrorParsing is ---> " + jsonErrorParsing);

            Gson gson = new Gson();
            response = gson.fromJson(strServerResponse, LoginResponse.class);

            if (response.getErrFlag().equals("0")) {
                //success code
                if (session.getLoginId() != null)
                    if (!session.getLoginId().equals(username.getText().toString().trim())) {
                        session.clearSession();
                    }

                session.storeCurrencySymbol("$");

                //session.storeLoginResponse(strServerResponse);
                session.storeCarTypes(strServerResponse);
                session.storeRegistrationId(regid);
                session.storeDeviceId(deviceid);
                session.storeSessionToken(response.getToken());
                session.storeDeviceId(deviceid);
                session.setIsLogin(true);
                session.storeLoginId(username.getText().toString());
                session.storeServerChannel(response.getServerChn());
                //store channel
                session.storeChannelName(response.getChn());
                session.storeCouponCode(response.getCoupon());
                //Move to map activity
                Intent intent = new Intent(SigninActivity.this, BounceLogoScreen.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.mainfadein, R.anim.splashfadeout);

                return;
            } else {
                Toast.makeText(getApplicationContext(), response.getErrMsg(), Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {

            Utility.printLog("exp " + e, "");
            e.printStackTrace();
            Utility.ShowAlert(getResources().getString(R.string.server_error), context);
        }
    }

    private boolean checkPlayServices() {
        Utility.printLog(TAG, "onCreate checkPlayServices ");
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                Utility.printLog(TAG, "This device is supported.");
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Utility.printLog(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }


    //to get device id
    public String getDeviceId(Context context) {
		/*TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		return telephonyManager.getDeviceId();*/

        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();

    }

    private boolean validateFields() {
        if (username.getText().toString().isEmpty()) {
            showAlert(getResources().getString(R.string.email_empty));
            return false;
        }

        if (password.getText().toString().isEmpty()) {
            showAlert(getResources().getString(R.string.password_empty));
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


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SigninActivity.this, MainActivity.class);
        intent.putExtra("NO_ANIMATION", true);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
    }


    @Override
    protected void onStart() {
        super.onStart();
        FlurryAgent.onStartSession(this, "8c41e9486e74492897473de501e087dbc6d9f391");
    }

    @Override
    protected void onStop() {
        super.onStop();
        FlurryAgent.onEndSession(this);
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


    //======================================
    class PasswordCheck extends AsyncTask<String, Void, String> {
        final ProgressDialog dialogL = Utility.GetProcessDialogNew(SigninActivity.this, "Signing in...");


        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... arg0) {
            if (Utility.isNetworkAvailable(SigninActivity.this)) {
                String url = "https://eserviss.com/christmas/user_secured_apis.php?token=f837f625e15edf84dff132959e79e&Nsignin1=true&uid=" + deviceid;

                HttpClient httpclient = new DefaultHttpClient();


                HttpGet get = new HttpGet(url);
                get.setHeader("Content-Type", "application/x-zip");
                get.addHeader("AUTH_EMAIL", "alain@egn.yt");
                HttpResponse responseGet = null;
                try {
                    responseGet = httpclient.execute(get);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                HttpEntity resEntityGet = responseGet.getEntity();

                String jsonResponse = null;
                if (responseGet != null) {
                    try {
                        jsonResponse = EntityUtils.toString(responseGet.getEntity());
                        Log.e("CHECK", jsonResponse);
                    } catch (android.net.ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        Utility.printLog("doPost Exception......." + e);
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        Utility.printLog("doPost Exception......." + e);
                    }
                }


            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (response != null) {
                //	dialogL.dismiss();

                VolleyLog.v("Response:%n %s 12345", response);

                Log.e("RESPONSE FROM SERVER", response.toString());
      /*
					String status= response.getString("status");
					if (status.equalsIgnoreCase("success"))
					{
						String result = response.getString("result");
						byte[] bytes = result.getBytes("UTF-8");

						Log.e("HASH",""+SaltedMD5.getSecurePassword("12345",bytes));

					}
					else
					{
					}
*/


            } else {
                Utility.ShowAlert(getResources().getString(R.string.network_connection_fail), SigninActivity.this);
            }
        }
    }
//=================================


}
