package com.mms.app.util;

import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.Activity;

import com.mms.app.DiscographyActivity;
import com.mms.app.GallerySectionActivity;
import com.mms.app.R;

public enum HomeSection {
	
	//ME(R.drawable.ic_action_me, "Me", MeSectionActivity.class),
	DISCOGRAPHY(R.drawable.ic_action_discography, "Discography", DiscographyActivity.class),
	GALLERY(R.drawable.ic_action_gallery, "Gallery", GallerySectionActivity.class),
	ABOUT(R.drawable.ic_action_about, "About", GallerySectionActivity.class);
	//YOUTUBE(R.drawable.ic_action_youtube, "Our Videos", YoutubeVideosFragment.class);
	
	private String fragmentTag;
	private int iconId;
	private String name;
	private Class<? extends Activity> activityClass;
	
	@SuppressLint("DefaultLocale")
	private HomeSection(int iconId, String name, Class<? extends Activity> activityClass){
		this.iconId = iconId;
		this.name = name;
		this.activityClass = activityClass;
		this.fragmentTag = this.toString().toLowerCase(Locale.ENGLISH) + "_fragment";
	}

	public String getFragmentTag(){
		return this.fragmentTag;
	}
	
	public int getIconId(){
		return this.iconId;
	}
	
	public String getName(){
		return this.name;
	}
	
	public Class<? extends Activity> getActivityClass(){
		return this.activityClass;
	}
	
}