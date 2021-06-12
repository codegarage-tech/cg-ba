package com.rc.ac.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.rc.ac.fragment.AppointmentsFragment;
import com.rc.ac.model.Appointment;
import com.rc.ac.util.Logger;
import com.rc.ac.viewholder.AppointmentViewHolder;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;


/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class AppointmentListAdapter extends RecyclerArrayAdapter<Appointment> {

    private static final int VIEW_TYPE_REGULAR = 1;
    AppointmentsFragment fragment;

    public AppointmentListAdapter(Context context, AppointmentsFragment appointmentsFragment) {
        super(context);
        this.fragment = appointmentsFragment;

    }

    @Override
    public int getViewType(int position) {
        return VIEW_TYPE_REGULAR;
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_REGULAR:
                return new AppointmentViewHolder(parent, fragment);
            default:
                throw new InvalidParameterException();
        }
    }

    public void updateItem(Appointment appointment) {
        int position = getItemPosition(appointment);

        if (position != -1) {
            List<Appointment> appointments = new ArrayList<>();
            appointments.addAll(getAllData());
            appointments.remove(position);
            appointments.add(position, appointment);

            removeAll();
            addAll(appointments);
            notifyDataSetChanged();
        }
    }

    public void deleteItem(Appointment appointment) {
        int position = getItemPosition(appointment);

        if (position != -1) {
            List<Appointment> appointments = new ArrayList<>();
            appointments.addAll(getAllData());
            appointments.remove(position);

            removeAll();
            addAll(appointments);
            notifyDataSetChanged();
        }
    }

    private int getItemPosition(Appointment appointment) {
        List<Appointment> appointments = getAllData();
        for (int i = 0; i < appointments.size(); i++) {
            if (appointments.get(i).getId().equalsIgnoreCase(appointment.getId())) {
                return i;
            }
        }
        return -1;
    }

    public boolean isInSameDate(Appointment appointment) {
        List<Appointment> appointments = getAllData();
        Logger.d("isInSameDate", "isInSameDate(upd): "+ appointment.toString());
        for (int i = 0; i < appointments.size(); i++) {
            if (appointments.get(i).getId().equalsIgnoreCase(appointment.getId())) {
                Logger.d("isInSameDate", "isInSameDate(pre): "+ appointments.get(i).toString());
                if(appointments.get(i).getaDate().equalsIgnoreCase(appointment.getaDate())){
                    return true;
                }
            }
        }
        return false;
    }
}