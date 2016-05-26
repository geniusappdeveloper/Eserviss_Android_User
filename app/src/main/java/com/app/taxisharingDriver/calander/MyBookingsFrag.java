package com.app.taxisharingDriver.calander;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.taxisharingDriver.ApplicationController;
import com.app.taxisharingDriver.BookingHistory;
import com.app.taxisharingDriver.R;
import com.app.taxisharingDriver.SplahsActivity;
import com.app.taxisharingDriver.utility.SessionManager;
import com.app.taxisharingDriver.utility.Utility;
import com.app.taxisharingDriver.utility.VariableConstants;
import com.google.gson.Gson;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Prashant Singh on 2/11/16.
 */

public class MyBookingsFrag extends Fragment implements AdapterView.OnItemClickListener
{
    private ListView lvBookings;
    private MyBookingsLVA myBookingsLVA;
    private View header;
    private Button btnRightArrow, btnLeftArrow;
    private TextView tvMonthName;

    private MasterAppointmentResponse masterAptResponse;
    private ArrayList<MasterAptWholeMonth> wholeMonthApts;
    private ArrayList<AppointmentDtlsData> aptDtlsList;
    private ArrayList<Date> bookingDays;

    private CaldroidFragment caldroidFragment;
    private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

    private Date crntSlctdDate, prevSlctdDate;
    private int crntYear, crntMonth, crntDay;
    private int crntSlctdYear, crntSlctdMonth, crntSlctdDay;  //, crntSlctdDate;
    private int prevSlctdYear, prevSlctdMonth;  //prevSlctdDate;

    private SessionManager sessionManager;
    private Utility utility;
    private ProgressDialog pDialog;
    private String deviceId = "",currentDate = "";
    private CaldroidListener listener;
    private String monthsName [];

    private String TAG = "MyBookings: ";
    /*******************************************************/

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        utility = new Utility();
        currentDate = utility.getCurrentGmtTime();

        sessionManager = new SessionManager(getActivity());
       // masterAptResponse = new MasterAppointmentResponse();

        wholeMonthApts = new ArrayList<MasterAptWholeMonth>();
        aptDtlsList = new ArrayList<AppointmentDtlsData>();
        bookingDays = new ArrayList<Date>();

        try
        {
            crntSlctdDate = prevSlctdDate = formatter.parse(currentDate);
            String strDate [] = formatter.format(crntSlctdDate).split("-");
            crntYear = crntSlctdYear = prevSlctdYear = Integer.parseInt(strDate[0]);
            crntMonth = crntSlctdMonth = prevSlctdMonth = Integer.parseInt(strDate[1]);
            crntDay = crntSlctdDay = Integer.parseInt(strDate[2]);
        }
        catch (Exception exc)
        {
            //Toast.makeText(getActivity(), exc.toString(), Toast.LENGTH_SHORT).show();
        }
    }
    /*******************************************************/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.frag_my_bookings, null);
        initializeCalendar(savedInstanceState);
        initializeViews(inflater, view);

        /*if(getActivity().getResources().getBoolean(R.bool.is_right_to_left))
        {
            monthsName = getActivity().getResources().getStringArray(R.array.monthsName);
        }*/
        return view;
    }
    /********************************************/

    @Override
    public void onResume()
    {
        super.onResume();
    }
    /********************************************/

    /**
     * @author 3embed
     * custom method to initalize the calendar
     * @param: void
     * @return: void
     */
    private void initializeViews(LayoutInflater inflater, View view)
    {
        tvMonthName = (TextView)view.findViewById(R.id.tvMonthName);

        View header = inflater.inflate(R.layout.frag_my_bookings_header_calender, null);

        lvBookings = (ListView)view.findViewById(R.id.lvBookings);
        myBookingsLVA = new MyBookingsLVA(getActivity(), 0, aptDtlsList);

        lvBookings.addHeaderView(header);
        lvBookings.setOnItemClickListener(this);
        lvBookings.setAdapter(myBookingsLVA);
    }
    /********************************************/

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        Bundle nxtActivityBundle = new Bundle();


        nxtActivityBundle.putString("EMAIL", aptDtlsList.get(position-1).getEmail());
        nxtActivityBundle.putString("APPTDT",aptDtlsList.get(position-1).getApptDt());

        Intent intent = new Intent(getActivity(), BookingHistory.class);
        intent.putExtras(nxtActivityBundle);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
    }
    /*****************************************************/

    /**
     * @author 3embed
     * custom method to initalize the calendar
     * @param: // savedInstanceState
     * @return: void
     */
    private void initializeCalendar(Bundle savedInstanceState)
    {
        // Setup caldroid fragment (normal CaldroidFragment)
        caldroidFragment = new CaldroidFragment();

        // If Activity is created after rotation
        if (savedInstanceState != null)
        {
            caldroidFragment.restoreStatesFromKey(savedInstanceState, "CALDROID_SAVED_STATE");
            caldroidFragment.getMonthTitleTextView().setGravity(Gravity.CENTER_HORIZONTAL);
        }

        // If activity is created from fresh
        else
        {
            Bundle args = new Bundle();
            Calendar calendar = Calendar.getInstance();
            args.putInt(CaldroidFragment.MONTH, calendar.get(Calendar.MONTH) + 1);
            args.putInt(CaldroidFragment.YEAR, calendar.get(Calendar.YEAR));
            args.putBoolean(CaldroidFragment.ENABLE_SWIPE, true);
            args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, true);
            args.putBoolean(CaldroidFragment.ENABLE_CLICK_ON_DISABLED_DATES, false);
            caldroidFragment.setArguments(args);
        }
        //caldroidFragment.set

        FragmentTransaction t = getFragmentManager().beginTransaction();
        t.replace(R.id.cal_appointment_relative, caldroidFragment);
        t.commit();

        setCaldroidOnClickListener();
    }
    /********************************************/


    private void setCaldroidOnClickListener()
    {
        // Setup listener
        listener = new CaldroidListener()
        {
            @Override
            public void onCaldroidViewCreated()
            {
                if(caldroidFragment.getRightArrowButton() != null)
                {
                    btnRightArrow = caldroidFragment.getRightArrowButton();
                    btnRightArrow.setVisibility(View.GONE);
                }

                if(caldroidFragment.getMonthTitleTextView() != null)
                {
                    if(getActivity().getResources().getBoolean(R.bool.is_right_to_left))
                    {
                        caldroidFragment.getMonthTitleTextView().setVisibility(View.GONE);
                        tvMonthName.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        tvMonthName.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onSelectDate(Date date, View view)
            {
                try
                {
                    crntSlctdDate = formatter.parse(formatter.format(date));
                    //Toast.makeText(getActivity(), ""+formatter.format(crntSlctdDate), Toast.LENGTH_SHORT).show();
                    onNewDateSelection();
                }
                catch (Exception exc)
                {

                }
            }

            @Override
            public void onChangeMonth(int month, int year)
            {
                // TODO: onChange month clear previous and current selected date
                caldroidFragment.clearBackgroundResourceForDate(prevSlctdDate);
                caldroidFragment.clearTextColorForDate(prevSlctdDate);

                caldroidFragment.clearBackgroundResourceForDate(crntSlctdDate);
                caldroidFragment.clearTextColorForDate(crntSlctdDate);

                String temp[] = formatter.format(crntSlctdDate).split("-");
                crntSlctdYear = year;
                crntSlctdMonth = month;

                tvMonthName.setText(getActivity().getResources().getStringArray(R.array.monthsName)[month-1]+" "+year);
                //crntSlctdDay = Integer.parseInt(temp[2]);

                String newDate = ""; //+crntSlctdYear+"-"+crntSlctdMonth+"-"+crntSlctdDay;
                newDate = ""+crntSlctdYear+"-"+crntSlctdMonth+"-"+crntDay;

                if(year == crntYear && month == crntMonth)
                {
                    if(btnRightArrow != null)
                    {
                        btnRightArrow.setVisibility(View.GONE);
                    }
                    else
                    {
                        btnRightArrow = caldroidFragment.getRightArrowButton();
                        btnRightArrow.setVisibility(View.GONE);
                        //caldroidFragment.setEnableSwipe(false);
                    }
                }
                else if(year == crntYear && month > crntMonth)
                {
                    //caldroidFragment.moveToDate(prevSlctdDate);
                    caldroidFragment.prevMonth();
                    btnRightArrow.setVisibility(View.GONE);
                    Toast.makeText(getActivity(),
                            getActivity().getResources().getString(R.string.noFutureAptsAvailable), Toast.LENGTH_SHORT).show();
                }
                else
                {
                    btnRightArrow.setVisibility(View.VISIBLE);
                }

                try
                {
                    crntSlctdDate = formatter.parse(newDate);
                }
                catch (Exception exc)
                {

                }
                prevSlctdDate = crntSlctdDate;
                //newDate = ""+year+"-"+month;
                getMasterAppointments();
            }
        };

        // Setup Caldroid
        caldroidFragment.setCaldroidListener(listener);
    }
    /********************************************/


    /**
     * @author 3embed
     * custom method to cehck whether new selected date is of same month or a different year or month
     * //@param date: contains new selected date
     */
    private void onNewDateSelection()
    {
        String strCrntSlctdDate[] = formatter.format(crntSlctdDate).split("-");
        String strPrevDate[] = formatter.format(prevSlctdDate).split("-");

        crntSlctdYear = Integer.parseInt(strCrntSlctdDate[0]);
        crntSlctdMonth = Integer.parseInt(strCrntSlctdDate[1]);
        crntSlctdDay = Integer.parseInt(strCrntSlctdDate[2]);

        prevSlctdYear = Integer.parseInt(strPrevDate[0]);
        prevSlctdMonth = Integer.parseInt(strPrevDate[1]);
        //prevSlctdDate = Integer.parseInt(strPrevDate[2]);

        //TODO: if years are equal
        if(crntSlctdYear == prevSlctdYear)
        {
            //Toast.makeText(getActivity(), "YEARS are equal", Toast.LENGTH_SHORT).show();
            onNewDateSelectionSameYear();
        }

        //TODO: if current year is greater than previous year (basically from dec to jan of nxt year)
        else if(crntSlctdYear > prevSlctdYear)
        {
            onNewDateSelectionGreaterYear();
        }

        //TODO: if current selected year is less than previous year
        else
        {
            //if(crntSlctdYear < prevSlctdYear)
            //Toast.makeText(getActivity(), formatter.format(crntSlctdDate)+"   is less than prev year "+prevSlctdDate, Toast.LENGTH_SHORT).show();
            btnRightArrow.setVisibility(View.VISIBLE);
            caldroidFragment.prevMonth();
        }
    }
    /********************************************/

    /**
     *@uthor: 3embed
     * custom method to update caldroid if the selected year is equal to previous year
     */
    private void onNewDateSelectionSameYear()
    {
        //TODO: if months are equal just update date selection Views
        if(crntSlctdMonth == prevSlctdMonth)
        {
            //Toast.makeText(getActivity(), "MONTHS are equal", Toast.LENGTH_SHORT).show();
            //TODO: To check whether current selected day is smaller than today
            if(crntSlctdYear == crntYear && crntSlctdMonth == crntMonth)
            {
                //Toast.makeText(getActivity(), "CURRENT MONTH & YEAR", Toast.LENGTH_SHORT).show();
                btnRightArrow.setVisibility(View.GONE);

                //Toast.makeText(getActivity(), "DAY is "+crntSlctdDay, Toast.LENGTH_SHORT).show();
                if(crntSlctdDay > crntDay)
                {
                    Toast.makeText(getActivity(),
                            getActivity().getResources().getString(R.string.noFutureAptsAvailable), Toast.LENGTH_SHORT).show();
                }
                else
                {
                    //Toast.makeText(getActivity(), "Yes bookings are available.", Toast.LENGTH_LONG).show();
                    updateSelectedDate();
                }
            } else
            {
                updateSelectedDate();
            }
        }

        //TODO: if current selected month is less than the previously selected month, update the caldroid views
        else if(crntSlctdMonth < prevSlctdMonth)
        {
            btnRightArrow.setVisibility(View.VISIBLE);
            caldroidFragment.prevMonth();
        }

        //TODO: if current selected month is greater than the previously selected month, compare with current month
        // else if(crntSlctdMonth > prevSlctdMonth) => move forward
        else
        {
            if(crntSlctdYear <= crntYear)
            {
                if (crntSlctdMonth == crntMonth)
                {
                    //Toast.makeText(getActivity(), "No future bookings crntSlctdMonth == crntMonth ", Toast.LENGTH_LONG).show();
                    btnRightArrow.setVisibility(View.GONE);
                    caldroidFragment.nextMonth();
                }

                else if (crntSlctdYear < crntYear && crntSlctdMonth > crntMonth)
                {
                    //Toast.makeText(getActivity(), "No future bookings crntSlctdMonth > crntMonth ", Toast.LENGTH_LONG).show();
                    btnRightArrow.setVisibility(View.GONE);
                    caldroidFragment.nextMonth();
                }

                else if (crntSlctdMonth > crntMonth && crntSlctdYear >= crntYear)
                {
                    Toast.makeText(getActivity(),
                            getActivity().getResources().getString(R.string.noFutureAptsAvailable), Toast.LENGTH_SHORT).show();
                    btnRightArrow.setVisibility(View.GONE);
                }

                //else if (crntSlctdMonth < crntMonth)
                else
                {
                    //Toast.makeText(getActivity(), "No future bookings are available.", Toast.LENGTH_LONG).show();
                    btnRightArrow.setVisibility(View.VISIBLE);
                    caldroidFragment.nextMonth();
                }
            }
        }
    }
    /***********************************************/

    /**
     *
     */
    private void onNewDateSelectionGreaterYear()
    {
        //Toast.makeText(getActivity(), formatter.format(crntSlctdDate)+"   is greater than prev year "+prevSlctdDate, Toast.LENGTH_SHORT).show();
        if(crntSlctdYear == crntYear)
        {
            //TODO: if current selected month is greater than current month (usually wont exe bcz Jan cant be >)
            if(crntSlctdMonth > crntMonth)
            {
                Toast.makeText(getActivity(),
                        getActivity().getResources().getString(R.string.noFutureAptsAvailable), Toast.LENGTH_SHORT).show();
            }

            //TODO: if current selected month is equal to current month
            else if(crntSlctdMonth == crntMonth)
            {
                btnRightArrow.setVisibility(View.GONE);
                caldroidFragment.nextMonth();
            }

            //TODO: if current selected month is less than current month (if crnt month > Jan)
            else
            {
                btnRightArrow.setVisibility(View.VISIBLE);
                caldroidFragment.nextMonth();
            }
        }

        //TODO: if current selected year is  less than current year
        else if(crntSlctdYear < crntYear)
        {
            btnRightArrow.setVisibility(View.VISIBLE);
            caldroidFragment.nextMonth();
        }
        else
        {
            Toast.makeText(getActivity(),
                    getActivity().getResources().getString(R.string.noFutureAptsAvailable), Toast.LENGTH_SHORT).show();
        }
    }
    /***********************************************/

    /**
     * @author 3embed
     * custom method to update
     */
    private void updateSelectedDate()
    {
        if(!prevSlctdDate.equals("") && prevSlctdDate != crntSlctdDate)
        {
            if(bookingDays.contains(prevSlctdDate))
            {
                //Toast.makeText(getActivity(), "NOT contained", Toast.LENGTH_SHORT).show();
                caldroidFragment.setBackgroundResourceForDate(R.color.green, prevSlctdDate);
            }
            else
            {
                //Toast.makeText(getActivity(), "YES contained", Toast.LENGTH_SHORT).show();
                caldroidFragment.clearBackgroundResourceForDate(prevSlctdDate);
                //caldroidFragment.setTextColorForDate(R.color.black, prevSlctdDate);
            }
            caldroidFragment.setBackgroundResourceForDate(R.color.blue, crntSlctdDate);
            //caldroidFragment.setTextColorForDate(R.color.white, crntSlctdDate);
            caldroidFragment.refreshView();
            prevSlctdDate = crntSlctdDate;

            aptDtlsList.clear();

            if(bookingDays.contains(crntSlctdDate))
            {
                if (wholeMonthApts.size() > 0)
                {
                    for (MasterAptWholeMonth dateWiseApts : wholeMonthApts)
                    {
                        try
                        {
                            //Log.d(TAG, "resetCalendar: date: " + formatter.parse(dateWiseApts.getDate()));

                            if (formatter.format(crntSlctdDate).equals(formatter.format(formatter.parse(dateWiseApts.getDate()))))
                            {
                                if(dateWiseApts.getAppt().size() >0)
                                {
                                    aptDtlsList.addAll(dateWiseApts.getAppt());
                                    //Toast.makeText(getActivity(), "SIZE aptDtlsList :"+aptDtlsList.size(), Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    Toast.makeText(getActivity(),
                                            getActivity().getResources().getString(R.string.noAptsAvailable), Toast.LENGTH_SHORT).show();
                                }
                                break;
                            }
                        }
                        catch (Exception exc)
                        {
                            //Toast.makeText(getActivity(),"Exc: "+exc, Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
            else
            {
                Toast.makeText(getActivity(),
                        getActivity().getResources().getString(R.string.noAptsAvailable), Toast.LENGTH_SHORT).show();
            }
            myBookingsLVA.notifyDataSetChanged();
        }
        else
        {
            //Toast.makeText(getActivity(), "ELSE updateSelectedDate", Toast.LENGTH_SHORT).show();
        }
    }
    /********************************************/

    /**
     * @author 3embed
     * custom method for api call to get master appointments of current selected month
     * //@param crntSelectedDate: contains the current selected date
     */

    private void getMasterAppointments()
    {
        if (Utility.isNetworkAvailable(getActivity()))
        {
            deviceId = Utility.getDeviceId(getActivity());
            if (pDialog == null)
            {
                pDialog = Utility.GetProcessDialog(getActivity());
                pDialog.setCancelable(false);
            }

            pDialog.setMessage(getResources().getString(R.string.pleaseWait));
            pDialog.show();
            //********** Getting values required for getMasterAppointments services ******/

           /* RequestBody requestBody = new FormEncodingBuilder()
                    .add("ent_sess_token", sessionManager.getSessionToken())
                    .add("ent_dev_id", deviceId)
                    .add("ent_date_time", currentDate)                          //YYYY-MM-DD HH:MM:SS"
                    .add("ent_appnt_dt", ""+crntSlctdYear+"-"+crntSlctdMonth)                       //YYYY-MM"
                    .add("ent_user_type", VariableConstants.USER_TYPE)
                    .build();

            //Utility.printLog("Mybookings getMasterAppointments requestBody: " + requestBody);
            OkHttpRequest.doJsonRequest(ServiceUrls.GET_APPOINTMENTS_URL, requestBody, new OkHttpRequest.JsonRequestCallback() {
                @Override
                public void onSuccess(String result)
                {
                    if (pDialog != null)
                    {
                        pDialog.dismiss();
                        pDialog = null;
                    }
                    Utility.printLog("Mybookings getMasterAppointments result: " + result);
                    getMasterAppointmentsHandler(result);
                }

                @Override
                public void onError(String error) {
                    if (pDialog != null) {
                        pDialog.dismiss();
                        pDialog = null;
                    }
                    Toast.makeText(getActivity(), getResources().getString(R.string.oops) + " " + getResources().getString(R.string.smthWentWrong), Toast.LENGTH_SHORT).show();
                    //Utility.printLog("Mybookings getMasterAppointments onError JSON DATA Error" + error);
                }
            });*/

            String deviceid=Utility.getDeviceId(getActivity());
            String curenttime=utility.getCurrentGmtTime();
            String currentdata[]=curenttime.split(" ");
            String datestr=currentdata[0];
            //datestr = datestr.substring(0, datestr.lastIndexOf("-"));
            datestr = ""+crntSlctdYear+"-"+crntSlctdMonth;
            SessionManager sessionManager=new SessionManager(getActivity());
            String sessiontoken=sessionManager.getSessionToken();
            final String mparams[]={sessiontoken,deviceid,datestr,curenttime};
            RequestQueue queue = Volley.newRequestQueue(getActivity());  // this = context

            String  url = VariableConstants.getAppointmentDetail_url;

            StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>()
                    {
                        @Override
                        public void onResponse(String response)
                        {
                            if (pDialog!=null)
                            {
                                pDialog.dismiss();
                                pDialog.cancel();
                                //mdialog=null;
                            }
                            Utility.printLog("AAAAAAAAA WholeMonthAppointment = "+response);
                            getMasterAppointmentsHandler(response);
                        }
                    },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error)
                        {
                            Utility.printLog("Error.Response", error.toString());
                            if (pDialog!=null)
                            {
                                pDialog.dismiss();
                                pDialog.cancel();
                                //mdialog=null;
                            }
                            ErrorMessage(getResources().getString(R.string.messagetitle),getResources().getString(R.string.servererror),true);
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams()
                {
                    Map<String, String>  params = new HashMap<String, String>();
                    params.put("ent_sess_token", mparams[0]);
                    params.put("ent_dev_id", mparams[1]);
                    params.put("ent_appnt_dt",mparams[2]);
                    params.put("ent_date_time",mparams[3]);

                    return params;
                }
            };
            int socketTimeout = 60000;//60 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            postRequest.setRetryPolicy(policy);
            queue.add(postRequest);
        }
        else
        {
            utility.showDialogConfirm(getActivity(),"Alert"," working internet connection required", false).show();
        }
    }
    /********************************************************/


    public void getMasterAppointmentsHandler(String result)
    {
        Utility.printLog("MyBookings getAppointmentHandlerJsonResponse: " + result);

        int errFlag=0;
        int errNum = 0;
        String errMsg ="";

        try
        {
            Gson gson = new Gson();
            masterAptResponse = gson.fromJson(result, MasterAppointmentResponse.class);

            if(masterAptResponse != null )
            {
                errFlag = masterAptResponse.getErrFlag();
                errNum = masterAptResponse.getErrNum();
                errMsg = masterAptResponse.getErrMsg();

                if(errFlag==0 && errNum==31)
                {
                    //Utility.printLog("Mybookings response.getAppointments().size() " + masterAptResponse.getAppointments().size());
                    resetCalendar();
                }
                else
                {
                    Toast.makeText(getActivity(), masterAptResponse.getErrMsg(),Toast.LENGTH_SHORT).show();
                }
                //adapter.notifyDataSetChanged();
            }
            else if (errNum==99 && errFlag==1)
            {
                showADialogInvalidOrExpToken(getResources().getString(R.string.oops), errMsg);
            }
            else if (errNum==6 && errFlag==1)
            {
                showADialogInvalidOrExpToken(getResources().getString(R.string.oops), errMsg);
            }
            else if (errNum==7 && errFlag==1)
            {
                showADialogInvalidOrExpToken(getResources().getString(R.string.oops), errMsg);
            }
            else if (errNum==101 && errFlag==1)
            {
                showADialogInvalidOrExpToken(getResources().getString(R.string.oops), errMsg);
            }
            else
            {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

                // set title
                alertDialogBuilder.setTitle(getResources().getString(R.string.messagetitle));

                // set dialog message
                alertDialogBuilder
                        .setMessage(getResources().getString(R.string.messagetitle))
                        .setCancelable(false)

                        .setNegativeButton(getResources().getString(R.string.okbuttontext),new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog,int id)
                            {
                                // if this button is clicked, just close
                                // the dialog box and do nothing
                                dialog.dismiss();
                            }
                        });
                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();
                // show it
                alertDialog.show();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    /*****************************************************/

    /**
     * @author: 3embed
     * custom method to update the caldroid view
     * @param: void
     * @return: void
     */

    private void resetCalendar()
    {
        caldroidFragment.clearBackgroundResourceForDates(bookingDays);
        caldroidFragment.clearTextColorForDates(bookingDays);

        // Reset calendar
        caldroidFragment.clearDisableDates();
        caldroidFragment.clearSelectedDates();
        caldroidFragment.setEnableSwipe(true);
        caldroidFragment.refreshView();

        wholeMonthApts.clear();
        aptDtlsList.clear();
        bookingDays.clear();

        wholeMonthApts.addAll(masterAptResponse.getAppointments());

        //TODO: to filter the dates of current month which contains bookings and add them to date list
        //for(int i = 0; i<wholeMonthApts.size(); i++)
        Log.d(TAG, "resetCalendar: wholeMonthApts = " + wholeMonthApts.size());

        if (wholeMonthApts.size() > 0)
        {
            for (MasterAptWholeMonth dateWiseApts : wholeMonthApts)
            {
                //TODO: if bookings are available on this day set it's background as green
                if (dateWiseApts.getAppt().size() > 0)
                {
                    try
                    {
                        Date tempDate = formatter.parse(dateWiseApts.getDate());
                        bookingDays.add(tempDate);

                        //Make that date background as green, text as white
                        caldroidFragment.setBackgroundResourceForDate(R.color.green, tempDate);
                        caldroidFragment.setTextColorForDate(R.color.white, tempDate);
                    }
                    catch (Exception exc)
                    {
                        //Toast.makeText(getActivity(), "exc:  " + exc, Toast.LENGTH_LONG).show();
                    }
                }
                else
                {

                }
            }
            //Toast.makeText(getActivity(), "SIZE bookingDay: " + bookingDays.size(), Toast.LENGTH_SHORT).show();

            if(crntSlctdYear == crntYear && crntSlctdMonth == crntMonth)
            {
                caldroidFragment.setBackgroundResourceForDate(R.color.blue, crntSlctdDate);
                caldroidFragment.setTextColorForDate(R.color.black, crntSlctdDate);

                if (bookingDays.contains(crntSlctdDate))
                {
                    if (wholeMonthApts.size() > 0)
                    {
                        for (MasterAptWholeMonth dateWiseApts : wholeMonthApts)
                        {
                            try
                            {
                                //Log.d(TAG, "resetCalendar: date: " + formatter.parse(dateWiseApts.getDate()));
                                if (formatter.format(crntSlctdDate).equals(formatter.format(formatter.parse(dateWiseApts.getDate())))) {
                                    if (dateWiseApts.getAppt().size() > 0)
                                    {
                                        aptDtlsList.addAll(dateWiseApts.getAppt());
                                        //Toast.makeText(getActivity(), "SIZE aptDtlsList :"+aptDtlsList.size(), Toast.LENGTH_SHORT).show();
                                    }
                                    else
                                    {
                                        Toast.makeText(getActivity(),
                                                getActivity().getResources().getString(R.string.noAptsAvailable), Toast.LENGTH_SHORT).show();
                                    }
                                    break;
                                }
                            }
                            catch (Exception exc)
                            {
                                //Toast.makeText(getActivity(),"Exc: "+exc, Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }
                else
                {
                    Toast.makeText(getActivity(),
                            getActivity().getResources().getString(R.string.noAptsAvailable), Toast.LENGTH_SHORT).show();
                }
            }
        }
        myBookingsLVA.notifyDataSetChanged();
    }
    /********************************************/

    /**
     * @author 3embed
     * custom method to show an alert dialog for invalid or expired session token
     * @param title: String to set as alert dialog title
     * @param message: String to set as alert dialog msg
     */
    public void showADialogInvalidOrExpToken(String title, String message)
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(message);

        alertDialogBuilder.setNegativeButton(getActivity().getResources().getString(R.string.okbuttontext),
                new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        sessionManager.logoutUser();
                        getActivity().stopService(ApplicationController.getMyServiceInstance());

                        Intent LoginIntent = new Intent(getActivity(), SplahsActivity.class);
                        LoginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        getActivity().startActivity(LoginIntent);
                        getActivity().finish();
                        getActivity().overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
                        dialog.dismiss();
                    }
                });

        AlertDialog	 alertDialog = alertDialogBuilder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
    }
    /*****************************************************/

    /**
     * Save current states of the Caldroid here
     */

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        if (caldroidFragment != null)
        {
            caldroidFragment.saveStatesToKey(outState, "CALDROID_SAVED_STATE");
        }
    }
    /*****************************************************/

    private void ErrorMessage(String title,String message,final boolean flageforSwithchActivity)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title);
        builder.setMessage(message);

        builder.setPositiveButton(getResources().getString(R.string.okbuttontext),
                new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        if (flageforSwithchActivity)
                        {

                        }
                        else
                        {
                            // only show message
                        }
                        dialog.dismiss();
                    }
                });

        AlertDialog	 alert = builder.create();
        alert.setCancelable(false);
        alert.show();
    }
}
