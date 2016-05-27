package com.eserviss.passenger.main;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.egnyt.eserviss.R;
import com.google.gson.Gson;
import com.threembed.utilities.SessionManager;
import com.threembed.utilities.Utility;
import com.threembed.utilities.VariableConstants;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Payal on 4/19/2016.
 */
public class PromocodeApply extends Activity {
    EditText text;
    private ProgressDialog dialogL;
    SessionManager session;

    String promoResponse="";
    String message ="";
    TextView submit;
    RelativeLayout back_per;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.promocode);
         session=new SessionManager(PromocodeApply.this);
        Typeface  roboto_condensed = Typeface.createFromAsset(getAssets(),"fonts/BebasNeue.otf");
        back_per= (RelativeLayout) findViewById(R.id.back_per);
        text = (EditText) findViewById(R.id.et_promoCode);
        submit = (TextView) findViewById(R.id.apply_promo);
        text.setTypeface(roboto_condensed);
        submit.setTypeface(roboto_condensed);


        submit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Utility.printLog("user msg="+text.getText().toString().trim());
                if(text.getText().toString().trim().equals(""))
                {
                    Utility.ShowAlert(getResources().getString(R.string.pls_enter_promo_code), PromocodeApply.this);
                }
                else
                {
                    getPromoCode();
                }
            }
        });
        back_per.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PromocodeApply.this.finish();
            }
        });


    }



    private void getPromoCode()
    {
        RequestQueue volleyRequest = Volley.newRequestQueue(PromocodeApply.this);

        dialogL=ProgressDialog.show(PromocodeApply.this,"Verifying Promo Code",null);
        dialogL.setCanceledOnTouchOutside(false);

     // StringRequest myReq = new StringRequest(Request.Method.POST,"http://app.eserviss.com/Taxi/services.php/applyPromo",
             StringRequest myReq = new StringRequest(Request.Method.POST,VariableConstants.BASE_URL+"applyPromo",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        dialogL.dismiss();
                        try {
                            JSONObject jsonObject= new JSONObject(response);
                             message = jsonObject.getString("message");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        promoResponse = response;
                        Utility.printLog("Success of getting user Info"+response);
                        Toast.makeText(PromocodeApply.this, message, Toast.LENGTH_LONG).show();
                       PromocodeApply.this.finish();
                        System.out.println("PROMO RESPONSE"+promoResponse);
                        //dialogL.dismiss();

                    }
                }, 	new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error)
            {
                dialogL.dismiss();
                Toast.makeText(PromocodeApply.this, "System error in getting user Info please retry", Toast.LENGTH_LONG).show();
                Utility.printLog("Error for volley");
                System.out.println("PROMO RESPONSE1"+error);

            }
        }){
            protected HashMap<String,String> getParams() throws com.android.volley.AuthFailureError {
                HashMap<String,String> kvPair = new HashMap<String, String>();

                SessionManager session=new SessionManager(PromocodeApply.this);

                Utility utility=new Utility();
                String curenttime=utility.getCurrentGmtTime();
                Log.i("","dataandTime "+curenttime);

                kvPair.put("ent_sess_token",session.getSessionToken() );
                kvPair.put("ent_dev_id",session.getDeviceId());
                kvPair.put("ent_promo_code",text.getText().toString().trim());


                Log.i("","aaa="+session.getSessionToken());
                Log.i("","aaa="+session.getDeviceId());
                Log.i("","aaa="+curenttime);

                return kvPair;
            };

        };

        volleyRequest.add(myReq);
    }

}
