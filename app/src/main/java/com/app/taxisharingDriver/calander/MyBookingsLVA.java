package com.app.taxisharingDriver.calander;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.app.taxisharingDriver.R;
import com.app.taxisharingDriver.utility.VariableConstants;

import java.util.ArrayList;


/**
 * Created by PrashantSingh on 2/11/16.
 */
public class MyBookingsLVA extends ArrayAdapter<AppointmentDtlsData>
{
    private Context mContext;
    private ArrayList<AppointmentDtlsData> aptDtlsData;
    private Typeface openSansRegular;

    /*******************************************************/

    public MyBookingsLVA(Context context, int resource, ArrayList<AppointmentDtlsData> aptDtlsData)
    {
        super(context, R.layout.appointmentlistviewitem, aptDtlsData);
        this.mContext = context;
        this.aptDtlsData = aptDtlsData;
       // openSansRegular = Typeface.createFromAsset(mContext.getAssets(), "fonts/OpenSans-Regular.ttf");
    }
    /*******************************************************/

    @Override
    public int getCount()
    {
        return aptDtlsData.size();
    }
    /*******************************************************/

    @Override
    public AppointmentDtlsData getItem(int position)
    {
        return aptDtlsData.get(position);
    }
    /*******************************************************/

    private class ViewHolder
    {
        TextView  passenger_name,BookingId;
        TextView pick_location;
        TextView status;
        TextView drop_location;
        TextView amount,amount_text;
        TextView time_text;
    }
    /*******************************************************/

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        AppointmentDtlsData aptDtlsDataItem = aptDtlsData.get(position);

        ViewHolder holder;
        if(convertView==null||convertView.getTag()==null)
        {
            holder = new ViewHolder();
            // inflate the layout
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.appointment_listview_item, parent, false);

            holder.passenger_name=(TextView)convertView.findViewById(R.id.passenger_name);
            holder.amount = (TextView)convertView.findViewById(R.id.amount);
            holder.amount_text = (TextView)convertView.findViewById(R.id.amount_text);
            holder.pick_location=(TextView)convertView.findViewById(R.id.pick_loc);
            holder.drop_location=(TextView)convertView.findViewById(R.id.drop_loc);
            holder.time_text=(TextView)convertView.findViewById(R.id.time_text);
            holder.status = (TextView)convertView.findViewById(R.id.status);
            holder.BookingId = (TextView)convertView.findViewById(R.id.home_booking_id_text);

            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }


        String amountstr = VariableConstants.CURRENCY_SYMBOL+aptDtlsDataItem.getAmount();


        holder.passenger_name.setId(position);
        //holder.imageview.setId(position);
        holder.pick_location.setId(position);
        holder.drop_location.setId(position);
        //holder.petientnameandtimelayout.setId(position);
        //holder.appointmentitemmainlayout.setId(position);
        holder.time_text.setId(position);
        holder.amount.setId(position);
        holder.amount_text.setId(position);
        holder.status.setId(position);
        holder.BookingId.setId(position);

        holder.passenger_name.setText(aptDtlsDataItem.getFname());
        holder.pick_location.setText(" "+aptDtlsDataItem.getAddrLine1());
        //holder.destanceunittextview.setText(" "+aptDtlsDataItem.getDistance()+" "+mContext.getResources().getString(R.string.kmh));
        holder.time_text.setText(mContext.getResources().getString(R.string.time_text)+" "+aptDtlsDataItem.getApntTime());
        holder.drop_location.setText(" "+aptDtlsDataItem.getDropLine1());
        holder.amount.setText(amountstr);
        holder.BookingId.setText(" "+aptDtlsDataItem.getBid());
        if ("0".equals(aptDtlsDataItem.getPayStatus()))
        {
            holder.amount_text.setText("" + mContext.getResources().getString(R.string.paymentnot));
        }
        else if ("1".equals(aptDtlsDataItem.getPayStatus()))
        {
            holder.amount_text.setText("" + mContext.getResources().getString(R.string.paydone));
        }
        else if ("2".equals(aptDtlsDataItem.getPayStatus()))
        {
            holder.amount_text.setText(""+mContext.getResources().getString(R.string.dispute));
        }
        else
        {
            holder.amount_text.setText(""+mContext.getResources().getString(R.string.closed));
        }

        holder.status.setText(" "+aptDtlsDataItem.getStatus().toUpperCase());
        return convertView;
    }
    /*******************************************************/
}
