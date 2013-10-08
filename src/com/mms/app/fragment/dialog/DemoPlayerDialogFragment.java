package com.mms.app.fragment.dialog;

import android.content.DialogInterface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mms.MMSConfig;
import com.mms.app.R;
import com.mms.model.MMSTrack;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

public class DemoPlayerDialogFragment extends DialogFragment
	implements OnPreparedListener, OnCompletionListener {

	public static final String META_TRACK = "meta_track";
	public static final String META_ALBUM_NAME = "meta_album_name";
	public static final String META_ALBUM_ART = "meta_album_art";
	
	private MMSTrack mTrack;
	private String mAlbumCover;
	private String mAlbumName;
	private DisplayImageOptions options;
	private MediaPlayer mPlayer;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.news_dialog);
		
		this.mTrack = (MMSTrack) this.getArguments().getSerializable(META_TRACK);
		this.mAlbumName = this.getArguments().getString(META_ALBUM_NAME);
		this.mAlbumCover = this.getArguments().getString(META_ALBUM_ART);
		
		this.options = new DisplayImageOptions.Builder()
			.displayer(new FadeInBitmapDisplayer(800))
			.showStubImage(R.drawable.default_album_art)
			.showImageForEmptyUri(R.drawable.default_album_art)
			.showImageOnFail(R.drawable.default_album_art)
			.cacheInMemory()
			.build();
		
		this.startDemo();
	}
	
	private void startDemo(){
		this.mPlayer = new MediaPlayer();
		this.mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		this.mPlayer.setOnPreparedListener(this);
		
		try {
			String demoUrl = MMSConfig.MMS_BASE_CONTENT_URL + this.mTrack.getDemoUrl();
			this.mPlayer.setDataSource(demoUrl);
			this.mPlayer.prepareAsync();
		} catch(Exception e){
			e.printStackTrace();
			this.onDemoFailed();
		}
	}
	
	private void onDemoFailed(){
		Toast.makeText(this.getActivity(), "Failed to load demo track",
				Toast.LENGTH_SHORT).show();
		this.dismiss();
	}
	
	@Override
	public void onPrepared(MediaPlayer mp) {
		this.mPlayer.start();
		View view = this.getView();
		if(view != null){
			view.findViewById(R.id.progress).setVisibility(View.GONE);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.dialog_demo_player, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		String albumCoverUrl = MMSConfig.MMS_BASE_CONTENT_URL + this.mAlbumCover;
		ImageLoader.getInstance().displayImage(albumCoverUrl,
				(ImageView) view.findViewById(R.id.img_album_art), this.options);
		((TextView) view.findViewById(R.id.lbl_track_name))
			.setText(this.mTrack.getName());
		((TextView) view.findViewById(R.id.lbl_album_name))
			.setText(this.mAlbumName);
		((ImageButton) view.findViewById(R.id.btn_close))
			.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					DemoPlayerDialogFragment.this.dismiss();
				}
			});
		
		if(this.mPlayer != null && this.mPlayer.isPlaying()){
			view.findViewById(R.id.progress).setVisibility(View.GONE);
		} else {
			view.findViewById(R.id.progress).setVisibility(View.VISIBLE);
		}
	}
	
	@Override
	public void onCompletion(MediaPlayer mp) {
		this.dismiss();
	}
	
	@Override
	public void onDismiss(DialogInterface dialog) {
		if(this.mPlayer != null) {
			if (this.mPlayer.isPlaying()) this.mPlayer.stop();
			this.mPlayer.release();
		}
		super.onDismiss(dialog);
	}
	
}