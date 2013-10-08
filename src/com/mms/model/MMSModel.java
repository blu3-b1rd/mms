package com.mms.model;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public interface MMSModel extends Serializable {
	
	public abstract class MMSBuilder {

		// "date": "17/05/2013 04:27:08 p.m."
		private static final String API_DATE_FORMAT = "dd/MM/yyyy";
		
		protected String modelType;
		
		public MMSBuilder(String modelType){
			this.modelType = modelType.replace("list", "");
		}
		
		protected Date dateFromString(String stringDate){
			DateFormat format = new SimpleDateFormat(API_DATE_FORMAT, Locale.getDefault());
			try {
				return format.parse(stringDate);
			} catch(Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		
		public abstract MMSModel build(String input) throws Exception;
	}

}
