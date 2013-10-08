package com.mms.app.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailLoader.ErrorReason;
import com.google.android.youtube.player.YouTubeThumbnailLoader.OnThumbnailLoadedListener;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.google.android.youtube.player.YouTubeThumbnailView.OnInitializedListener;
import com.mms.MMSConfig;
import com.mms.app.R;

public class YoutubeVideosFragment extends Fragment implements OnInitializedListener,
		OnThumbnailLoadedListener {

	private ProgressBar progress;
	private String playlistId = "PL14C2E03D5EE75AA1";
	private YouTubeThumbnailLoader thumbnailLoader;
	private YouTubeThumbnailView thumbnailView;
	private List<YouTubeVideo> videos;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_youtube_videos, container,
				false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		this.videos = new ArrayList<YoutubeVideosFragment.YouTubeVideo>();

		this.progress = (ProgressBar) view.findViewById(R.id.progress);
		this.progress.setVisibility(View.VISIBLE);

		this.thumbnailView = new YouTubeThumbnailView(this.getActivity());
		this.thumbnailView.initialize(MMSConfig.YOUTUBE_KEY, this);
	}

	@Override
	public void onInitializationFailure(YouTubeThumbnailView arg0,
			YouTubeInitializationResult arg1) {
	}

	@Override
	public void onInitializationSuccess(YouTubeThumbnailView thumbnailView,
			YouTubeThumbnailLoader thumbnailLoader) {
		this.thumbnailLoader = thumbnailLoader;
		this.thumbnailLoader.setOnThumbnailLoadedListener(this);
		this.thumbnailLoader.setPlaylist(this.playlistId);
	}

	@Override
	public void onThumbnailError(YouTubeThumbnailView arg0, ErrorReason arg1) {
	}

	@Override
	public void onThumbnailLoaded(YouTubeThumbnailView thumbnailView, String videoId) {
		do {
			
			YouTubeVideo video = new YouTubeVideo();
			video.videoId = videoId;
			video.drawable = thumbnailView.getDrawable();
			this.videos.add(video);
			this.thumbnailLoader.next();
			
		} while (this.thumbnailLoader.hasNext());
		
		this.progress.setVisibility(View.GONE);
		((ListView) this.getView().findViewById(R.id.videos_list))
				.setAdapter(new YouTubeVideosAdapter(this.getActivity()));
	}

	public class YouTubeVideo {
		public String videoId;
		public Drawable drawable;
	}

	public class YouTubeVideosAdapter extends ArrayAdapter<YouTubeVideo> {

		public YouTubeVideosAdapter(Context context) {
			super(context, 0, videos);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			convertView = LayoutInflater.from(this.getContext()).inflate(
					R.layout.list_item_video, parent, false);
			((ImageView) convertView.findViewById(R.id.video_preview))
					.setImageDrawable(videos.get(position).drawable);
			return super.getView(position, convertView, parent);
		}

		@Override
		public int getCount() {
			return videos.size();
		}

	}

}
