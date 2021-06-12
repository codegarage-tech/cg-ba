package com.rc.ac.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.rc.ac.R;
import com.rc.ac.activity.HomeActivity;
import com.rc.ac.base.BaseFragment;
import com.rc.ac.enumeration.AppointmentType;
import com.rc.ac.model.Appointment;
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
import com.reversecoder.library.util.AllSettingsManager;

import org.parceler.Parcels;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.ymex.popup.controller.AlertController;
import cn.ymex.popup.dialog.PopupDialog;
import io.armcha.ribble.presentation.navigationview.NavigationId;
import retrofit2.Call;
import retrofit2.Response;

public class AddAppointmentFragment extends BaseFragment {

    private EditText etAppointmentDate, etSerialNo, etTime, etFromDate, etToDate, etAppointmentWith, etRemarks;
    private Button btnSave, btnUpdate, btnDelete;
    private Appointment mAppointment;
    private AppointmentType mAppointmentType;
    private User mUser;
    private String appointmentDate = "", serialNo = "", dTime = "", appointmentWith = "", remarks = "";
    private boolean isUpdated = false, isDeleted = false;
    private int dateValue;

    //Background task
    private APIInterface mApiInterface;
    private DoAppointmentTask addAppointmentTask;

    @Override
    public int initFragmentLayout() {
        return R.layout.fragment_add_appointment;

    }

    @Override
    public void initFragmentBundleData(Bundle bundle) {
        if (bundle != null) {
            String mParcelableAppointmentType = bundle.getString(AllConstants.INTENT_KEY_APPOINTMENT_TYPE);
            if (!AllSettingsManager.isNullOrEmpty(mParcelableAppointmentType)) {
                mAppointmentType = AppointmentType.valueOf(mParcelableAppointmentType);
                Logger.d(TAG, TAG + " >>> " + "mAppointmentType: " + mParcelableAppointmentType + "");

                if (mAppointmentType == AppointmentType.DETAIL) {
                    Parcelable mParcelableAppointment = bundle.getParcelable(AllConstants.INTENT_KEY_APPOINTMENT);
                    if (mParcelableAppointment != null) {
                        mAppointment = Parcels.unwrap(mParcelableAppointment);
                        Logger.d(TAG, TAG + " >>> " + "mParcelableAppointment: " + mAppointment.toString());
                    }
                }
            }
        }
    }

    @Override
    public void initFragmentViews(View parentView) {
        etAppointmentDate = (EditText) parentView.findViewById(R.id.et_appointment_date);
        etSerialNo = (EditText) parentView.findViewById(R.id.et_serial_no);
        etTime = (EditText) parentView.findViewById(R.id.et_time);
        etFromDate = (EditText) parentView.findViewById(R.id.et_from_date);
        etToDate = (EditText) parentView.findViewById(R.id.et_to_date);
        etAppointmentWith = (EditText) parentView.findViewById(R.id.et_appointment_with);
        etRemarks = (EditText) parentView.findViewById(R.id.et_remarks);
        btnSave = (Button) parentView.findViewById(R.id.btn_save);
        btnUpdate = (Button) parentView.findViewById(R.id.btn_update);
        btnDelete = (Button) parentView.findViewById(R.id.btn_delete);
    }

    @Override
    public void initFragmentViewsData() {
        mApiInterface = APIClient.getClient(getActivity()).create(APIInterface.class);

        if (mAppointmentType == AppointmentType.DETAIL) {
            btnSave.setVisibility(View.GONE);
            btnUpdate.setVisibility(View.VISIBLE);
            btnDelete.setVisibility(View.VISIBLE);
            setData();

            ((HomeActivity) getActivity()).setToolBarTitle(getString(R.string.txt_title_appointment_detail));
            ((HomeActivity) getActivity()).setLeftMenu(false);
        }
    }

    private void setData() {
        if (mAppointment != null) {
            String createdDate = AppUtil.formatDateFromstring("yyyy-MM-dd hh:mm:ss.SSS", "dd/MM/yyyy ", AppUtil.optStringNullCheckValue(mAppointment.getaDate()));
            etAppointmentDate.setText(createdDate);
            etSerialNo.setText(AppUtil.optStringNullCheckValue(mAppointment.getaNo()));

            etTime.setText(AppUtil.optStringNullCheckValue(mAppointment.getaTime()));
            etAppointmentWith.setText(AppUtil.optStringNullCheckValue(mAppointment.getaWith()));
            etRemarks.setText(AppUtil.optStringNullCheckValue(mAppointment.getRemarks()));
        }
    }

    @Override
    public void initFragmentActions() {
        etAppointmentDate.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                AppUtil.datePicker(etAppointmentDate.getText().toString(), etAppointmentDate, getActivity());
            }
        });

        etTime.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                AppUtil.timePicker(etTime.getText().toString(), etTime, getActivity());
            }
        });
        etFromDate.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                AppUtil.datePicker(etFromDate.getText().toString(), etFromDate, getActivity());
            }
        });
        etToDate.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                AppUtil.datePicker(etToDate.getText().toString(), etToDate, getActivity());
            }
        });

        btnSave.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                checkValidation(AppointmentType.ADD);
            }
        });

        btnUpdate.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                checkValidation(AppointmentType.EDIT);
            }
        });

        btnDelete.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                showDeleteAppointmentDialog();
            }
        });
    }

    @Override
    public void initFragmentBackPress() {
        Logger.d(TAG, TAG + ">>> add appointment backpress");
        // Send updated/deleted appointment to Appointment list
        if (mAppointmentType == AppointmentType.DETAIL) {
            ((HomeActivity) getActivity()).setToolBarTitle(NavigationId.APPOINTMENTS.getValue());
            ((HomeActivity) getActivity()).setLeftMenu(true);

            if (isUpdated || isDeleted) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                if (fragmentManager != null) {
                    List<Fragment> fragmentList = fragmentManager.getFragments();
                    if (fragmentList.size() > 0) {
                        for (Fragment fragment : fragmentList) {
                            if (fragment instanceof AppointmentsFragment) {
                                if (mAppointment != null) {
                                    String appointmentDates = AppUtil.formatDateFromstring("dd/MM/yyyy", "yyyy-MM-dd hh:mm:ss.SSS", etAppointmentDate.getText().toString());
                                    mAppointment.setaDate(appointmentDates);
                                    mAppointment.setaNo(etSerialNo.getText().toString());
                                    mAppointment.setaTime(etTime.getText().toString());
                                    mAppointment.setaWith(etAppointmentWith.getText().toString());
                                    mAppointment.setRemarks(etRemarks.getText().toString());
                                    AppointmentsFragment appointmentsFragment = (AppointmentsFragment) fragment;
                                    Logger.d("<<<<mAppointment", mAppointment + ">>>>");
                                    Logger.d("appointDates", appointmentDates + "");

                                    if (isUpdated) {
                                        appointmentsFragment.initFragmentUpdate(true, mAppointment);
                                    } else if (isDeleted) {
                                        appointmentsFragment.initFragmentUpdate(false, mAppointment);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void initFragmentUpdate(Object... object) {

    }

    @Override
    public void initFragmentOnResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dismissProgressDialog();
        if (addAppointmentTask != null && addAppointmentTask.getStatus() == AsyncTask.Status.RUNNING) {
            addAppointmentTask.cancel(true);
        }
    }

    private void getDateCompare(String str) {
        Date todayDate = Calendar.getInstance().getTime();
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String today = formatter.format(todayDate);
        dateValue = AppUtil.compareDates(today, str);
        Logger.d("<<dateValue>>>", dateValue + "");
    }

    private void checkValidation(AppointmentType appointmentType) {
        KeyboardManager.hideKeyboard(getActivity());

        //Check internet connection
        if (!NetworkManager.isConnected(getActivity())) {
            Toast.makeText(getActivity(), getResources().getString(R.string.toast_network_error), Toast.LENGTH_SHORT).show();
            return;
        }

        //Check Appointment Date
        appointmentDate = etAppointmentDate.getText().toString();
        if (AllSettingsManager.isNullOrEmpty(appointmentDate)) {
            Toast.makeText(getActivity(), getString(R.string.toast_please_input_your_appointment_date), Toast.LENGTH_SHORT).show();
            return;
        }
        getDateCompare(appointmentDate);
        //Check Serial No
        serialNo = etSerialNo.getText().toString();
        if (AllSettingsManager.isNullOrEmpty(serialNo)) {
            Toast.makeText(getActivity(), getString(R.string.toast_please_input_your_appointment_si_no), Toast.LENGTH_SHORT).show();
            return;
        }

        //Check Time
        dTime = etTime.getText().toString();
        if (AllSettingsManager.isNullOrEmpty(dTime)) {
            Toast.makeText(getActivity(), getString(R.string.toast_please_input_your_appointment_time), Toast.LENGTH_SHORT).show();
            return;
        }

        //Check Description
        appointmentWith = etAppointmentWith.getText().toString();
        if (AllSettingsManager.isNullOrEmpty(appointmentWith)) {
            Toast.makeText(getActivity(), getString(R.string.toast_please_input_your_appointment_with), Toast.LENGTH_SHORT).show();
            return;
        }

        //Check Remarks
        remarks = etRemarks.getText().toString();
        if (AllSettingsManager.isNullOrEmpty(remarks)) {
            Toast.makeText(getActivity(), getString(R.string.toast_please_input_your_appointment_remarks), Toast.LENGTH_SHORT).show();
            return;
        }

        if (dateValue == -1) {
            Toast.makeText(getActivity(), getString(R.string.toast_you_can_not_add_or_edit_appointment_of_previous_date), Toast.LENGTH_SHORT).show();
            return;

        }

        //Params User
        mUser = APIResponse.getResponseObject(SessionManager.getStringSetting(getActivity(), AllConstants.SESSION_KEY_USER), User.class);
        if (mUser != null) {
            if (addAppointmentTask != null && addAppointmentTask.getStatus() == AsyncTask.Status.RUNNING) {
                Logger.d(TAG, TAG + " >>> " + "setData: canceling asynctask");
                addAppointmentTask.cancel(true);
            }
            addAppointmentTask = new DoAppointmentTask(getActivity(), appointmentType);
            addAppointmentTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }

    /************************
     * Server communication *
     ************************/
    private class DoAppointmentTask extends AsyncTask<String, Integer, Response> {

        private Context mContext;
        private AppointmentType mApiType;

        public DoAppointmentTask(Context context, AppointmentType apiType) {
            mContext = context;
            mApiType = apiType;
        }

        @Override
        protected void onPreExecute() {
            showProgressDialog();
        }

        @Override
        protected Response doInBackground(String... params) {
            try {
                Call<APIResponse> call = null;

                switch (mApiType) {
                    case ADD:
                        Logger.d(TAG, TAG + " Param>>> " + "token: " + mUser.getUserToken()
                                + "\n" + "appointment date: " + etAppointmentDate.getText().toString()
                                + "\n" + "serial no: " + etSerialNo.getText().toString()
                                + "\n" + "time: " + etTime.getText().toString()
                                + "\n" + "appointment with: " + etAppointmentWith.getText().toString()
                                + "\n" + "remarks: " + etRemarks.getText().toString()
                        );
                        call = mApiInterface.apiAddAppointment(mUser.getUserToken(), etAppointmentDate.getText().toString(), etSerialNo.getText().toString(), etTime.getText().toString(),
                                etAppointmentWith.getText().toString(), etRemarks.getText().toString());
                        break;
                    case EDIT:
                        Logger.d(TAG, TAG + " Param>>> " + "token: " + mUser.getUserToken()
                                + "\n" + "appointment id: " + mAppointment.getId()
                                + "\n" + "appointment date: " + etAppointmentDate.getText().toString()
                                + "\n" + "serial no: " + etSerialNo.getText().toString()
                                + "\n" + "time: " + etTime.getText().toString()
                                + "\n" + "appointment with: " + etAppointmentWith.getText().toString()
                                + "\n" + "remarks: " + etRemarks.getText().toString()
                        );
                        call = mApiInterface.apiUpdateAppointment(mUser.getUserToken(), mAppointment.getId(), etAppointmentDate.getText().toString(), etSerialNo.getText().toString(),
                                etTime.getText().toString(), etAppointmentWith.getText().toString(), etRemarks.getText().toString());
                        break;
                    case DELETE:
                        Logger.d(TAG, TAG + " Param>>> " + "token: " + mUser.getUserToken()
                                + "\n" + "appointment id: " + mAppointment.getId()
                        );
                        call = mApiInterface.apiDeleteAppointment(mUser.getUserToken(), mAppointment.getId());
                        break;
                }

                Response response = call.execute();
                Logger.d(TAG, TAG + " Appointment>>> " + "response: " + response);
                if (response.isSuccessful()) {
                    return response;
                }
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
                    APIResponse data = (APIResponse) result.body();

                    if (data != null) {
                        Logger.d(TAG, TAG + " >>> " + "APIResponse(data): " + data.toString());
                        if (!TextUtils.isEmpty(data.getStatusCode()) && data.getStatusCode().equalsIgnoreCase("200")) {
                            if (mApiType == AppointmentType.EDIT) {
                                isUpdated = true;
                                isDeleted = false;
                                Toast.makeText(getActivity(), data.getMessage(), Toast.LENGTH_SHORT).show();
                            } else if (mApiType == AppointmentType.DELETE) {
                                isUpdated = false;
                                isDeleted = true;
                                Toast.makeText(getActivity(), data.getMessage(), Toast.LENGTH_SHORT).show();
                                ((HomeActivity) getActivity()).initActivityBackPress();
                            } else if (mApiType == AppointmentType.ADD) {
                                isUpdated = true;
                                isDeleted = false;
                                Toast.makeText(getActivity(), data.getMessage(), Toast.LENGTH_SHORT).show();
                                clearAll();
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

    private void clearAll() {
        etAppointmentDate.setText("");
        etSerialNo.setText("");
        etTime.setText("");
        etFromDate.setText("");
        etToDate.setText("");
        etAppointmentWith.setText("");
        etRemarks.setText("");
    }

    private void showDeleteAppointmentDialog() {
        PopupDialog.create(getActivity())
                .outsideTouchHide(false)
                .dismissTime(1000 * 5)
                .controller(AlertController.build()
                        .title(getString(R.string.dialog_delete_appointment_title) + "\n")
                        .message(getString(R.string.dialog_do_you_want_to_delete_this_appointment))
                        .clickDismiss(true)
                        .negativeButton(getString(R.string.dialog_cancel), null)
                        .positiveButton(getString(R.string.dialog_ok), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                checkValidation(AppointmentType.DELETE);
                            }
                        }))
                .show();
    }
}