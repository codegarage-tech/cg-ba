package com.rc.ac.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rc.ac.R;
import com.rc.ac.adapter.AppointmentListAdapter;
import com.rc.ac.base.BaseFragment;
import com.rc.ac.model.Appointment;
import com.rc.ac.model.ParamsAppointment;
import com.rc.ac.model.RespAppointment;
import com.rc.ac.model.User;
import com.rc.ac.retrofit.APIClient;
import com.rc.ac.retrofit.APIInterface;
import com.rc.ac.retrofit.APIResponse;
import com.rc.ac.util.AllConstants;
import com.rc.ac.util.AppUtil;
import com.rc.ac.util.KeyboardManager;
import com.rc.ac.util.Logger;
import com.reversecoder.library.event.OnSingleClickListener;
import com.reversecoder.library.network.NetworkManager;
import com.reversecoder.library.storage.SessionManager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Response;

import static com.rc.ac.util.AppUtil.scrollSelectedItemToTheTop;

public class AppointmentsFragment extends BaseFragment {

    private LinearLayout linSearch;
    private EditText etSearch;
    private TextView tvTodaysAppointment;
    private String today = "";
    private RecyclerView rvAppointment;
    private AppointmentListAdapter appointmentListAdapter;
    //Background task
    private APIInterface mApiInterface;
    private GetAppointmentListTask appointmentListTask;
    private User mUser;

    @Override
    public int initFragmentLayout() {
        return R.layout.fragment_appointments;
    }

    @Override
    public void initFragmentBundleData(Bundle bundle) {

    }

    @Override
    public void initFragmentViews(View parentView) {
        linSearch = (LinearLayout) parentView.findViewById(R.id.lin_search);
        etSearch = (EditText) parentView.findViewById(R.id.et_search);
        tvTodaysAppointment = (TextView) parentView.findViewById(R.id.tv_todays_appointment);
        rvAppointment = (RecyclerView) parentView.findViewById(R.id.rv_appointment);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvAppointment.setLayoutManager(layoutManager);
        rvAppointment.setNestedScrollingEnabled(false);
    }

    @Override
    public void initFragmentViewsData() {
        mApiInterface = APIClient.getClient(getActivity()).create(APIInterface.class);
        KeyboardManager.hideKeyboard(getActivity());
        appointmentListAdapter = new AppointmentListAdapter(getActivity(), AppointmentsFragment.this);


        Date date = Calendar.getInstance().getTime();
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        today = formatter.format(date);
        Logger.d(TAG, TAG + " >>> " + "today object = " + today);
        etSearch.setText(today);

        if (today.equalsIgnoreCase(etSearch.getText().toString())) {
            tvTodaysAppointment.setText(getActivity().getResources().getString(R.string.view_todays_appointment_zero));
        }

        //Check internet connection
        if (!NetworkManager.isConnected(getActivity())) {
            Toast.makeText(getActivity(), getResources().getString(R.string.toast_network_error), Toast.LENGTH_SHORT).show();
            return;
        }

        //Params User
        mUser = APIResponse.getResponseObject(SessionManager.getStringSetting(getActivity(), AllConstants.SESSION_KEY_USER), User.class);
        if (mUser != null) {
            ParamsAppointment paramsAppointment = new ParamsAppointment(mUser.getUserToken(), etSearch.getText().toString());
            Log.e("paramsAppointment", paramsAppointment.toString() + "");
            Logger.d(TAG, TAG + " >>> " + "paramsAppointment: " + paramsAppointment.toString());
            appointmentListTask = new GetAppointmentListTask(getActivity(), paramsAppointment);
            appointmentListTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }

    @Override
    public void initFragmentActions() {
        linSearch.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                if (mUser != null) {
                    ParamsAppointment paramsAppointment = new ParamsAppointment(mUser.getUserToken(), etSearch.getText().toString());
                    Log.e("paramsAppointment", paramsAppointment.toString() + "");
                    Logger.d(TAG, TAG + " >>> " + "paramsAppointment: " + paramsAppointment.toString());
                    appointmentListTask = new GetAppointmentListTask(getActivity(), paramsAppointment);
                    appointmentListTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }
            }
        });

        etSearch.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                AppUtil.datePicker(etSearch.getText().toString(), etSearch, getActivity());

            }
        });
    }

    @Override
    public void initFragmentBackPress() {
        getActivity().finish();
    }

    @Override
    public void initFragmentUpdate(Object... object) {
        if (object[0] != null && object[1] != null) {
            boolean isUpdated = (boolean) object[0];
            Appointment appointment = (Appointment) object[1];
            Logger.d(TAG, TAG + ">> Updated appointment: " + appointment.toString());
            Logger.d(TAG, TAG + ">> Updated status: " + isUpdated);

            // Update/delete data from appointment list
            if (isUpdated) {
                appointmentListAdapter.updateItem(appointment);
            } else {
                appointmentListAdapter.deleteItem(appointment);
            }

            // Update count after updating data
            int listDataSize = appointmentListAdapter.getAllData().size();
            if (listDataSize > 0) {
                if (today.equalsIgnoreCase(etSearch.getText().toString())) {
                    tvTodaysAppointment.setText(getActivity().getResources().getString(R.string.view_todays_appointment) + "(" + listDataSize + ")");
                } else {
                    String createdDate = AppUtil.formatDateFromstring("dd/MM/yyyy", "dd/MM/yy", etSearch.getText().toString());
                    tvTodaysAppointment.setText(createdDate + "  Appointments  " + "(" + listDataSize + ")");
                }
            } else {
                if (today.equalsIgnoreCase(etSearch.getText().toString())) {
                    tvTodaysAppointment.setText(getActivity().getResources().getString(R.string.view_todays_appointment_zero));
                } else {
                    String createdDate = AppUtil.formatDateFromstring("dd/MM/yyyy", "dd/MM/yy", etSearch.getText().toString());
                    tvTodaysAppointment.setText(createdDate + "  Appointments  " + "(0)");
                }
            }
        }
    }

    @Override
    public void initFragmentOnResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dismissProgressDialog();
        if (appointmentListTask != null && appointmentListTask.getStatus() == AsyncTask.Status.RUNNING) {
            appointmentListTask.cancel(true);
        }
    }

    /************************
     * Server communication *
     ************************/
    private class GetAppointmentListTask extends AsyncTask<String, Integer, Response> {

        Context mContext;
        ParamsAppointment mParamAppointment;

        public GetAppointmentListTask(Context context, ParamsAppointment paramsAppointment) {
            mContext = context;
            mParamAppointment = paramsAppointment;
        }

        @Override
        protected void onPreExecute() {
            showProgressDialog();
        }

        @Override
        protected Response doInBackground(String... params) {
            try {
                Logger.d(TAG, TAG + "APIResponse(param): " + "token: " + mParamAppointment.getToken()
                        + "\n" + "fromDate: " + mParamAppointment.getFromDate()
                );
                Call<RespAppointment> call = mApiInterface.apiGetAppointmentList(mParamAppointment.getToken(), mParamAppointment.getFromDate());
                Response response = call.execute();
                return response;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Response result) {
            try {
                dismissProgressDialog();

                if (result != null && result.isSuccessful()) {
                    Logger.d(TAG, TAG + " >>> " + "APIResponse(result): " + result.toString());
                    Logger.d(TAG, TAG + " >>> " + "APIResponse(response): " + result.body());
                    RespAppointment data = (RespAppointment) result.body();

                    if (data != null) {
                        Logger.d(TAG, TAG + " >>> " + "APIResponse(data): " + data.toString());

                        if (!TextUtils.isEmpty(data.getStatusCode()) && data.getStatusCode().equalsIgnoreCase("200")) {
                            if (data.getApp().size() > 0) {
                                // Set appointment into list
                                appointmentListAdapter.clear();
                                appointmentListAdapter.addAll(data.getApp());
                                rvAppointment.setAdapter(appointmentListAdapter);
                                appointmentListAdapter.notifyDataSetChanged();
                                scrollSelectedItemToTheTop(rvAppointment, 0);

                                // Set appointment count
                                if (today.equalsIgnoreCase(etSearch.getText().toString())) {
                                    tvTodaysAppointment.setText(getActivity().getResources().getString(R.string.view_todays_appointment) + "(" + data.getApp().size() + ")");
                                } else {
                                    String createdDate = AppUtil.formatDateFromstring("dd/MM/yyyy", "dd/MM/yy", etSearch.getText().toString());
                                    tvTodaysAppointment.setText(createdDate + "  Appointments  " + "(" + data.getApp().size() + ")");
                                }
                            } else {
                                // Set appointment count
                                appointmentListAdapter.clear();
                                appointmentListAdapter.notifyDataSetChanged();
                                if (today.equalsIgnoreCase(etSearch.getText().toString())) {
                                    tvTodaysAppointment.setText(getActivity().getResources().getString(R.string.view_todays_appointment_zero));
                                } else {
                                    String createdDate = AppUtil.formatDateFromstring("dd/MM/yyyy", "dd/MM/yy", etSearch.getText().toString());
                                    tvTodaysAppointment.setText(createdDate + "  Appointments  " + "(0)");
                                }
                            }
                        } else {
                            Toast.makeText(getActivity(), ((!TextUtils.isEmpty(data.getMessage()) ? data.getMessage() : getActivity().getString(R.string.toast_something_went_wrong))), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), getResources().getString(R.string.toast_no_info_found), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.toast_could_not_retrieve_info), Toast.LENGTH_SHORT).show();
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }
}