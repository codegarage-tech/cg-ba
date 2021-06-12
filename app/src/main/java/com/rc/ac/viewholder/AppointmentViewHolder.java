package com.rc.ac.viewholder;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.rc.ac.R;
import com.rc.ac.enumeration.AppointmentType;
import com.rc.ac.fragment.AddAppointmentFragment;
import com.rc.ac.fragment.AppointmentsFragment;
import com.rc.ac.model.Appointment;
import com.rc.ac.util.AllConstants;
import com.rc.ac.util.AppUtil;
import com.rc.ac.util.Logger;
import com.reversecoder.library.event.OnSingleClickListener;

import org.parceler.Parcels;

import static com.rc.ac.util.AllConstants.INTENT_KEY_APPOINTMENT_TYPE;


/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class AppointmentViewHolder extends BaseViewHolder<Appointment> {

    private TextView tvSerialNo, tvTime, tvDate, tvDescription, tvRemarks;
    private LinearLayout linAppointment;
    private AppointmentsFragment fragment;
    private String TAG = "AppointmentViewHolder";

    public AppointmentViewHolder(ViewGroup parent, AppointmentsFragment appointmentsFragment) {
        super(parent, R.layout.row_appointment);
        this.fragment = appointmentsFragment;

        tvSerialNo = (TextView) $(R.id.tv_serial_no);
        tvTime = (TextView) $(R.id.tv_time);
        tvDate = (TextView) $(R.id.tv_date);
        tvDescription = (TextView) $(R.id.tv_description);
        tvRemarks = (TextView) $(R.id.tv_remarks);
        linAppointment = (LinearLayout) $(R.id.lin_appointment);
    }

    @Override
    public void setData(final Appointment data) {
       // String date = AppUtil.optStringNullCheckValue(data.getaDate()).substring(0, 10);

        String createdDate = AppUtil.formatDateFromstring("yyyy-MM-dd hh:mm:ss.SSS", "dd MMM ",  AppUtil.optStringNullCheckValue(data.getaDate()));
        tvSerialNo.setText(data.getaNo());
        tvTime.setText(data.getaTime());
        tvDate.setText(createdDate);
        tvDescription.setText(data.getaWith());
        tvRemarks.setText(data.getRemarks());

        Logger.d(TAG, TAG + "Appointment>>>>" + data.toString() + "");

        linAppointment.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {

                AddAppointmentFragment addAppointmentFragment = new AddAppointmentFragment();
                Bundle args = new Bundle();
                args.putParcelable(AllConstants.INTENT_KEY_APPOINTMENT, Parcels.wrap(data));
                args.putString(INTENT_KEY_APPOINTMENT_TYPE, AppointmentType.DETAIL.name());
                addAppointmentFragment.setArguments(args);

                FragmentManager fragmentManager = ((AppCompatActivity) getContext()).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.container, addAppointmentFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
    }
}