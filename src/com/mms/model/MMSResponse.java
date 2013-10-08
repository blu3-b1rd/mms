package com.mms.model;

import org.json.JSONObject;


public class MMSResponse implements MMSModel {

	private static final long serialVersionUID = 3869653641088303772L;
	
	private int status;
	private String message;
	private String type;
	private MMSModel content;

	private MMSResponse(){
		this.content = null;
	}
	
	public int getStatus() {
		return status;
	}

	private void setStatus(int status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	private void setMessage(String message) {
		this.message = message;
	}

	public String getType() {
		return type;
	}

	private void setType(String type) {
		this.type = type;
	}

	public MMSModel getContent() {
		return content;
	}

	private void setContent(MMSModel content) {
		this.content = content;
	}

	public static class Builder extends MMSBuilder {

		private final String TAG_STATUS = "status";
		private final String TAG_MESSAGE = "message";
		private final String TAG_TYPE = "type";
		private final String TAG_CONTENT = "content";
		
		public Builder(String modelType) {
			super(modelType);
		}
		
		@Override
		public MMSModel build(String input) throws Exception {
			JSONObject jsonResponse = new JSONObject(input);
			MMSResponse response = new MMSResponse();
			
			response.setStatus(jsonResponse.getInt(TAG_STATUS));
			response.setMessage(jsonResponse.getString(TAG_MESSAGE));
			response.setType(jsonResponse.getString(TAG_TYPE));
			if(response.getStatus() == 200){
				response.setContent(MMSModelFactory.getFactory()
						.getModel(response.getType(), jsonResponse.getString(TAG_CONTENT)));
			}
			
			return response;
		}

	}

}
