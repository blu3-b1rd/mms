package com.mms.app;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.Toast;

import com.mms.MMSActivity;
import com.mms.MMSApplication;
import com.mms.MMSConfig;
import com.mms.MMSPreferences;
import com.mms.model.MMSGalleryItem;
import com.mms.model.MMSList;
import com.mms.model.MMSModel;
import com.mms.model.MMSResponse;
import com.mms.request.GetArtistGallery;
import com.mms.request.MMSAsyncRequest;
import com.mms.request.MMSAsyncRequest.OnMMSRequestFinishedListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

public class GallerySectionActivity extends ActionBarActivity
	implements OnMMSRequestFinishedListener {
	
	/*
	private MMSList mGallery;
	private LinearLayout galleryLayout;
	
	private DisplayImageOptions options;
	private ImageLoader imageLoader;
	*/
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_gallery);
		String cookie = MMSPreferences.loadString(MMSPreferences.COOKIE, null);
		new MMSAsyncRequest(this).execute(new GetArtistGallery(cookie));
	}

	@Override
	public void onMMSRequestFinished(MMSResponse response) {
		this.findViewById(R.id.progress).setVisibility(View.GONE);
		try {
			switch(response.getStatus()){
			case MMSActivity.MMS_SUCCESS:
				this.onSuccessfulResponse(response);
				break;
			case MMSActivity.MMS_WRONG_COOKIE:
				this.onWrongCookie();
				break;
			default:
				this.onErrorResponse(response);
				break;
			}
		} catch(Exception e) {
			e.printStackTrace();
			this.onRequestFailed();
		}
	}
	
	protected void onSuccessfulResponse(MMSResponse response){
		MMSList content = (MMSList) response.getContent();
		if(content.isEmpty()){
			this.findViewById(R.id.lbl_empty).setVisibility(View.VISIBLE);
		} else {
			this.findViewById(R.id.lbl_empty).setVisibility(View.GONE);
			this.displayGallery(content);
		}
	}
	
	private void displayGallery(MMSList gallery){
		((Gallery) this.findViewById(R.id.gallery))
			.setAdapter(new GalleryAdapter(this, gallery));
	}
	
	protected void onRequestFailed(){
		Toast.makeText(this, "Connection Failed",
				Toast.LENGTH_SHORT).show();
	}
	
	protected void onWrongCookie(){
		Toast.makeText(this, "Login again. Wrong Cookie",
				Toast.LENGTH_SHORT).show();
		((MMSApplication) this.getApplication()).logout(this);
	}
	
	protected void onErrorResponse(MMSResponse response) {
		Toast.makeText(this, response.getMessage(),
				Toast.LENGTH_SHORT).show();
	}
	
	private class GalleryAdapter extends ArrayAdapter<MMSModel> {
		
		private MMSList gallery;
		private DisplayImageOptions options;
		private ImageLoader imageLoader = ImageLoader.getInstance();
		
		public GalleryAdapter(Context context, MMSList gallery){
			super(context, R.layout.gallery_item, gallery);
			this.gallery = gallery;
			
			this.options = new DisplayImageOptions.Builder()
				.displayer(new FadeInBitmapDisplayer(800))
				.showStubImage(R.drawable.default_album_art)
				.showImageForEmptyUri(R.drawable.default_album_art)
				.showImageOnFail(R.drawable.default_album_art)
				.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
				.cacheInMemory()
				.build();
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = LayoutInflater.from(this.getContext()).inflate(
					R.layout.gallery_item, parent, false);
			
			ImageView galleryItem = (ImageView) view
					.findViewById(R.id.img_gallery_item);
			
			MMSGalleryItem item = (MMSGalleryItem) this.getItem(position);
			
			this.imageLoader.displayImage(
					MMSConfig.MMS_BASE_CONTENT_URL + item.getUrl(),
					galleryItem, this.options);
			
			return view;
		}
		
		@Override
		public int getCount() {
			return this.gallery.size();
		}
		
		@Override
		public MMSModel getItem(int position) {
			return this.gallery.get(position);
		}
		
	}
	
}
