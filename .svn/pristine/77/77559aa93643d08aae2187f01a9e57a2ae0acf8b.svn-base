package com.mms;

import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.mms.model.MMSResponse;
import com.mms.request.MMSAsyncRequest.OnMMSRequestFinishedListener;

public abstract class MMSActivity extends FragmentActivity
	implements OnMMSRequestFinishedListener {
	
	public static final int MMS_SUCCESS = 200;
	public static final int MMS_WRONG_COOKIE = 408;
	private OnMMSSuccessfulResponseListener successListener;
	
	interface OnMMSSuccessfulResponseListener {
		public void onSuccesfulResponse(MMSResponse response);
	}
	
	public MMSActivity(){
		this.successListener = null;
	}
	
	@Override
	public void onMMSRequestFinished(MMSResponse response) {
		try {
			switch(response.getStatus()){
			case MMS_SUCCESS:
				if(this.successListener == null){
					this.onSuccessfulResponse(response);
				} else {
					this.successListener.onSuccesfulResponse(response);
				}
				break;
			case MMS_WRONG_COOKIE:
				this.onWrongCookie();
				break;
			default:
				this.onErrorResponse(response);
				break;
			}
		} catch (Exception e){
			e.printStackTrace();
			this.onRequestFailed();
		}
	}
	
	protected void onRequestFailed(){
		Toast.makeText(this, "Connection Failed", Toast.LENGTH_SHORT).show();
	}
	
	protected abstract void onSuccessfulResponse(MMSResponse response);
	
	protected void onWrongCookie(){
		Toast.makeText(this, "Login again. Wrong Cookie.", Toast.LENGTH_SHORT).show();
		((MMSApplication) this.getApplication()).logout(this);
	}
	
	protected void onErrorResponse(MMSResponse response){
		Toast.makeText(this, response.getMessage(), Toast.LENGTH_SHORT).show();
	}
	
	public void overrideMMSSuccessListener(OnMMSSuccessfulResponseListener newSuccessListener){
		this.successListener = newSuccessListener;
	}

}
