package com.mms.app;

import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.mms.MMSConfig;
import com.mms.app.fragment.AlbumFragment;
import com.mms.model.MMSAlbum;
import com.mms.model.MMSUrl;

public class AlbumActivity extends ActionBarActivity {

	public static final String META_ALBUM = "meta_album";
	
	private MMSAlbum mAlbum;
	private Map<Integer, String> urlsMap;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_album);
		this.mAlbum = (MMSAlbum) this.getIntent().getExtras().getSerializable(META_ALBUM);
		this.configure();
	}
	
	private void configure(){
		ActionBar actionbar = this.getSupportActionBar();
		actionbar.setTitle(this.mAlbum.getName());
		actionbar.setSubtitle(MMSConfig.ARTIST_ALIAS);
		
		AlbumFragment albumFragment = new AlbumFragment();
		albumFragment.setArguments(this.getIntent().getExtras());
		
		FragmentTransaction ft = this.getSupportFragmentManager()
				.beginTransaction();
		ft.replace(R.id.layout_album, albumFragment).commit();
	}
	
	@SuppressLint("UseSparseArrays")
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		this.urlsMap = new HashMap<Integer, String>();
		
		for(MMSUrl mUrl : this.mAlbum.getUrls()){
			menu.add(Menu.NONE, mUrl.getId(), Menu.NONE, mUrl.getUrlType());
			this.urlsMap.put(mUrl.getId(), mUrl.getUrl());
		}
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(!this.urlsMap.containsKey(item.getItemId())){
			return super.onOptionsItemSelected(item);
		}
		
		Intent urlIntent = new Intent(Intent.ACTION_VIEW,
				Uri.parse(this.urlsMap.get(item.getItemId())));
		this.startActivity(urlIntent);
		
		return true;
	}

}
