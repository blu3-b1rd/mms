package com.mms.app;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mms.MMSActivity;
import com.mms.MMSApplication;
import com.mms.app.util.LoginType;
import com.mms.model.MMSResponse;
import com.mms.model.MMSUser;
import com.mms.request.CreateUserRequest;
import com.mms.request.MMSAsyncRequest;

public class RegisterActivity extends MMSActivity {

	private TextView nameField;
	private TextView birthdayField;
	private Spinner genderField;
	private TextView usernameField;
	private TextView emailField;
	private TextView passwordField;
	private TextView confirmPasswordField;

	private final String TAG_DATE_PICKER_DIALOG = "date_picker_dialog";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.overridePendingTransition(R.anim.push_right_in,
				R.anim.push_right_out);
		this.setContentView(R.layout.activity_register);
		this.init();
	}
	
	private void init(){
		this.nameField = (TextView) this.findViewById(R.id.name_field);
		this.birthdayField = (Button) this.findViewById(R.id.birthday_field);
		this.genderField = (Spinner) this.findViewById(R.id.gender_field);
		this.usernameField = (TextView) this.findViewById(R.id.username_field);
		this.emailField = (TextView) this.findViewById(R.id.email_field);
		this.passwordField = (TextView) this.findViewById(R.id.password_field);
		this.confirmPasswordField = (TextView) this.findViewById(R.id.password_conf_field);
	}

	public void onDateClicked(View view) {
		DialogFragment datePicker = new DatePickerFragment();
		datePicker.show(this.getSupportFragmentManager(),
				TAG_DATE_PICKER_DIALOG);
	}

	public void onRegisterClicked(View view) {
		if (this.validateData()) {
			this.findViewById(R.id.button_register).setEnabled(false);
			this.findViewById(R.id.register_form_container).setVisibility(
					View.GONE);
			this.findViewById(R.id.progress).setVisibility(View.VISIBLE);
			new MMSAsyncRequest(this).execute(new CreateUserRequest(
					this.nameField.getText().toString(),
					this.emailField.getText().toString(),
					this.usernameField.getText().toString(),
					this.genderField.getSelectedItem().toString(),
					LoginType.EMAIL,
					this.birthdayField.getText().toString(),
					this.passwordField.getText().toString()));
		} else {
			Toast.makeText(this, "Verify your info", Toast.LENGTH_SHORT).show();
		}
	}
	
	@Override
	protected void onRequestFailed() {
		super.onRequestFailed();
		this.findViewById(R.id.button_register).setEnabled(true);
		this.findViewById(R.id.register_form_container).setVisibility(
				View.VISIBLE);
		this.findViewById(R.id.progress).setVisibility(View.GONE);
	}
	
	@Override
	protected void onSuccessfulResponse(MMSResponse response) {
		((MMSApplication) this.getApplication()).setUser((MMSUser) response.getContent());
		Toast.makeText(this, "Register Successful", Toast.LENGTH_SHORT).show();
		this.finish();
	}
	
	@Override
	protected void onErrorResponse(MMSResponse response) {
		super.onErrorResponse(response);
		this.findViewById(R.id.button_register).setEnabled(true);
		this.findViewById(R.id.register_form_container).setVisibility(
				View.VISIBLE);
		this.findViewById(R.id.progress).setVisibility(View.GONE);
	}

	private boolean validateData() {
		boolean flag = true;
		
		if(this.nameField.getText().toString().isEmpty()){
			this.highlight(this.nameField);
			flag = false;
		}
		if(this.usernameField.getText().toString().isEmpty()){
			this.highlight(this.usernameField);
			flag = false;
		}
		if(this.emailField.getText().toString().isEmpty()){
			this.highlight(this.emailField);
			flag = false;
		}
		if(this.passwordField.getText().toString().isEmpty()){
			this.highlight(this.passwordField);
			flag = false;
		}
		if(this.confirmPasswordField.getText().toString().isEmpty()){
			this.highlight(this.confirmPasswordField);
			flag = false;
		}
		if(!this.passwordField.getText().toString()
				.equals(this.confirmPasswordField.getText().toString())){
			this.highlight(this.passwordField);
			this.highlight(this.confirmPasswordField);
			flag = false;
		}
		
		return flag;
	}

	private void highlight(View view) {
		view.setBackgroundColor(Color.parseColor("#11DD1E2F"));
	}

	public static class DatePickerFragment extends DialogFragment implements
			DatePickerDialog.OnDateSetListener {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			final Calendar c = Calendar.getInstance();
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH);
			int day = c.get(Calendar.DAY_OF_MONTH);

			return new DatePickerDialog(getActivity(), this, year, month, day);
		}

		public void onDateSet(DatePicker view, int year, int month, int day) {
			
		}
	}

}
