package com.mms.app.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mms.MMSApplication;
import com.mms.app.SplashActivity;
import com.mms.model.MMSResponse;
import com.mms.model.MMSUser;
import com.mms.request.GetUserRequest;
import com.mms.request.MMSAsyncRequest;
import com.mms.request.MMSAsyncRequest.OnMMSRequestFinishedListener;
import com.mms.app.R;
import com.mms.app.util.LoginType;

public class LoginFragment extends Fragment implements OnClickListener, OnMMSRequestFinishedListener {
	
	private EditText emailField;
	private EditText passwordField;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_login, container, false);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		this.emailField = (EditText) view.findViewById(R.id.email_field);
		this.passwordField = (EditText) view.findViewById(R.id.password_field);
		((Button) view.findViewById(R.id.button_show_login)).setOnClickListener(this);
		((Button) view.findViewById(R.id.button_login)).setOnClickListener(this);
		((Button) view.findViewById(R.id.button_register)).setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_show_login:
			this.onShowLoginClicked();
			break;
		case R.id.button_login:
			this.onLoginClicked();
			break;
		case R.id.button_register:
			this.onRegisterClicked();
			break;
		default:
			break;
		}
	}
	
	private void onShowLoginClicked(){
		this.getView().findViewById(R.id.button_show_login).setVisibility(View.GONE);
		this.getView().findViewById(R.id.button_login).setVisibility(View.VISIBLE);
		this.getView().findViewById(R.id.form_login).setVisibility(View.VISIBLE);
	}
	
	private void onLoginClicked(){
		if(this.validate()){
			this.getView().findViewById(R.id.login_content).setVisibility(View.GONE);
			this.getView().findViewById(R.id.progress).setVisibility(View.VISIBLE);
			new MMSAsyncRequest(this).execute(new GetUserRequest(LoginType.EMAIL, 
					this.emailField.getText().toString(),
					this.passwordField.getText().toString()));
		} else {
			Toast.makeText(this.getActivity(), "Incorrect credentials", Toast.LENGTH_SHORT).show();
		}
	}
	
	private boolean validate(){
		boolean flag = true;
		
		if(this.emailField.getText().toString().isEmpty()){
			this.highlight(this.emailField);
			flag = false;
		}
		
		if(this.passwordField.getText().toString().isEmpty()){
			this.highlight(this.passwordField);
			flag = false;
		}
		
		return flag;
	}
	
	private void highlight(View view){
		view.setBackgroundColor(Color.parseColor("#55DD1E2F"));
	}
	
	private void onRegisterClicked(){
		Message.obtain(new Handler((Handler.Callback) this.getActivity()),
				SplashActivity.MESSAGE_SHOW_REGISTER).sendToTarget();
	}

	@Override
	public void onMMSRequestFinished(MMSResponse response) {
		if(response == null){
			Toast.makeText(this.getActivity(), "Connection Error", Toast.LENGTH_SHORT).show();
			this.getView().findViewById(R.id.progress).setVisibility(View.GONE);
			this.getView().findViewById(R.id.login_content).setVisibility(View.VISIBLE);
		} else if(response.getStatus() == 200){
			((MMSApplication) this.getActivity().getApplication()).setUser((MMSUser) response.getContent());
			Message.obtain(new Handler((Handler.Callback) this.getActivity()),
					SplashActivity.MESSAGE_LOGGED).sendToTarget();
		} else {
			Toast.makeText(this.getActivity(), response.getMessage(), Toast.LENGTH_SHORT).show();
			this.getView().findViewById(R.id.progress).setVisibility(View.GONE);
			this.getView().findViewById(R.id.login_content).setVisibility(View.VISIBLE);
		}
	}
	
}
