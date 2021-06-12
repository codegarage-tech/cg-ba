package com.rc.ac.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.jaeger.library.StatusBarUtil;
import com.rc.ac.R;
import com.rc.ac.base.BaseActivity;
import com.rc.ac.model.ParamsAppUser;
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

import io.armcha.ribble.presentation.widget.AnimatedTextView;
import retrofit2.Call;
import retrofit2.Response;

import static com.rc.ac.util.AllConstants.SESSION_IS_USER_LOGGED_IN;
import static com.rc.ac.util.AppUtil.isDebug;


/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class LoginActivity extends BaseActivity {

    //Toolbar
    private Button btnLogin;
    private LinearLayout llLeftMenu;
    private AnimatedTextView toolbarTitle;
    private EditText edtUserName, edtUserPassword;

    //Background task
    private APIInterface mApiInterface;
    private LoginAppUserTask loginAppUserTask;

    @Override
    public String[] initActivityPermissions() {
        return new String[]{};
    }

    @Override
    public int initActivityLayout() {
        return R.layout.activity_login;
    }

    @Override
    public void initStatusBarView() {
        StatusBarUtil.setTransparent(getActivity());
    }

    @Override
    public void initNavigationBarView() {

    }

    @Override
    public void initIntentData(Bundle savedInstanceState, Intent intent) {

    }

    @Override
    public void initActivityViews() {
        llLeftMenu = (LinearLayout) findViewById(R.id.ll_left_menu);
        toolbarTitle = (AnimatedTextView) findViewById(R.id.toolbar_title);
        toolbarTitle.setAnimatedText(getString(R.string.view_welcome), 0L);
        AppUtil.applyMarqueeOnTextView(toolbarTitle);
        btnLogin = (Button) findViewById(R.id.btn_login);
        edtUserName = (EditText) findViewById(R.id.edt_username);
        edtUserPassword = (EditText) findViewById(R.id.edt_user_password);
        if (isDebug(getActivity())) {
            edtUserName.setText("cmuser");
            edtUserPassword.setText("123456");
        }
    }

    @Override
    public void initActivityViewsData(Bundle savedInstanceState) {
        mApiInterface = APIClient.getClient(getActivity()).create(APIInterface.class);

    }

    @Override
    public void initActivityActions(Bundle savedInstanceState) {
        llLeftMenu.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                initActivityBackPress();
            }
        });

        btnLogin.setOnClickListener(new OnBaseClickListener() {
            @Override
            public void OnPermissionValidation(View view) {
                KeyboardManager.hideKeyboard(getActivity());

                //Check internet connection
                if (!NetworkManager.isConnected(getActivity())) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.toast_network_error), Toast.LENGTH_SHORT).show();
                    return;
                }

                //Check username
                String userName = edtUserName.getText().toString();
                if (AllSettingsManager.isNullOrEmpty(userName)) {
                    Toast.makeText(getActivity(), getString(R.string.toast_please_input_your_username), Toast.LENGTH_SHORT).show();
                    return;
                }


                //Check password
                String password = edtUserPassword.getText().toString();
                if (AllSettingsManager.isNullOrEmpty(password)) {
                    Toast.makeText(getActivity(), getString(R.string.toast_please_input_your_password), Toast.LENGTH_SHORT).show();
                    return;
                }
                //Login User
                ParamsAppUser paramUserLogin = new ParamsAppUser(userName, password);
                Log.e("paramUserLogin", paramUserLogin.toString() + "");
                Logger.d(TAG, TAG + " >>> " + "paramUserLogin: " + paramUserLogin.toString());
                loginAppUserTask = new LoginAppUserTask(getActivity(), paramUserLogin);
                loginAppUserTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

            }
        });
    }

    @Override
    public void initActivityOnResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void initActivityBackPress() {
        finish();
    }

    @Override
    public void initActivityDestroyTasks() {
        dismissProgressDialog();
        if (loginAppUserTask != null && loginAppUserTask.getStatus() == AsyncTask.Status.RUNNING) {
            loginAppUserTask.cancel(true);
        }
    }

    @Override
    public void initActivityPermissionResult(int requestCode, String[] permissions, int[] grantResults) {

    }


    /************************
     * Server communication *
     ************************/
    private class LoginAppUserTask extends AsyncTask<String, Integer, Response> {

        Context mContext;
        ParamsAppUser mParamAppUser;

        private LoginAppUserTask(Context context, ParamsAppUser paramAppUser) {
            mContext = context;
            mParamAppUser = paramAppUser;
        }

        @Override
        protected void onPreExecute() {
            showProgressDialog();
        }

        @Override
        protected Response doInBackground(String... params) {
            try {
                Logger.d(TAG, TAG + " >>> " + "APIResponse(param): " + mParamAppUser.toString());
                Call<User> call = mApiInterface.apiUserLogin(mParamAppUser.getUsername(), mParamAppUser.getPassword());
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
                    User data = (User) result.body();

                    if (data != null) {
                        Logger.d(TAG, TAG + " >>> " + "APIResponse(data): " + data.toString());

                        if (!TextUtils.isEmpty(data.getStatusCode()) && data.getStatusCode().equalsIgnoreCase("200")) {
                            // Save data into session
                            String jsonAppUser = APIResponse.getResponseString(data);
                            Logger.d(TAG, TAG + " >>> " + "APIResponse(user)" + jsonAppUser);
                            SessionManager.setStringSetting(getActivity(), AllConstants.SESSION_KEY_USER, jsonAppUser);
                            SessionManager.setBooleanSetting(LoginActivity.this, SESSION_IS_USER_LOGGED_IN, true);

                            Intent intent = new Intent(getActivity(), HomeActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(getActivity(),  ((!TextUtils.isEmpty(data.getMessage())?data.getMessage():getActivity().getString(R.string.toast_something_went_wrong))), Toast.LENGTH_SHORT).show();
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