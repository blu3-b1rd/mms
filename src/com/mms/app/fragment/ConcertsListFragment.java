package com.mms.app.fragment;

import java.lang.ref.WeakReference;
import java.util.Arrays;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.tabcarousel.BackScrollManager;
import com.android.tabcarousel.CarouselContainer;
import com.mms.MMSActivity;
import com.mms.MMSApplication;
import com.mms.app.R;
import com.mms.model.MMSList;
import com.mms.model.MMSModel;
import com.mms.model.MMSResponse;
import com.mms.request.MMSAsyncRequest.OnMMSRequestFinishedListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

public class ConcertsListFragment extends ListFragment
	implements OnMMSRequestFinishedListener {
	
	private CarouselContainer carouselHeader;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		//return inflater.inflate(R.layout.fragment_concerts_list, container, false);
		Arrays.sort(MOVIES);
        final CarouselListAdapter adapter = new CarouselListAdapter(getActivity());
        for (final String movie : MOVIES) {
            adapter.add(movie);
        }

        // Bind the data
        setListAdapter(adapter);
        return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		this.carouselHeader = (CarouselContainer) this.getActivity()
				.findViewById(R.id.carousel_header);
		
		/*
		ListView listView = this.getListView();
		listView.setOnScrollListener(new BackScrollManager(this.carouselHeader, null,
				CarouselContainer.TAB_INDEX_SECOND));
		
		String cookie = MMSPreferences.loadString(MMSPreferences.COOKIE, null);
		new MMSAsyncRequest(this).execute(new GetEventsRequest(cookie));
		*/
		
        final ListView listView = getListView();
        // Attach the BackScrollManager
        listView.setOnScrollListener(new BackScrollManager(this.carouselHeader, null,
                CarouselContainer.TAB_INDEX_SECOND));
        // Register the onItemClickListener
        //listView.setOnItemClickListener(this);
        // We disable the scroll bar because it would otherwise be incorrect
        // because of the hidden
        // header
        listView.setVerticalScrollBarEnabled(false);
	}
	
	
	
	@Override
	public void onMMSRequestFinished(MMSResponse response) {
		this.getView().findViewById(R.id.progress_concerts)
			.setVisibility(View.GONE);
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
		this.getListView().setAdapter(
				new ConcertsAdapter(this.getActivity(),
						(MMSList) response.getContent()));
	}
	
	protected void onRequestFailed(){
		Toast.makeText(this.getActivity(), "Connection Failed",
				Toast.LENGTH_SHORT).show();
	}
	
	protected void onWrongCookie(){
		Toast.makeText(this.getActivity(), "Login again. Wrong Cookie",
				Toast.LENGTH_SHORT).show();
		((MMSApplication) this.getActivity().getApplication())
			.logout(this.getActivity());
	}
	
	protected void onErrorResponse(MMSResponse response) {
		Toast.makeText(this.getActivity(), response.getMessage(),
				Toast.LENGTH_SHORT).show();
	}
	
	
	
	private static final String[] MOVIES = new String[] {
        "A Separation", "Beasts of the Southern Wild", "Being John Malkovich", "Capote",
        "Cast Away", "City of God", "Cloud Atlas", "Dead Poet's Society", "Downfall",
        "Incendies", "Let Me In", "Looper", "Moneyball", "Monsieur Lazhar", "Moon",
        "My Neighbour Totoro", "Paranorman", "Boogie Nights", "Hard Eight", "Magnolia",
        "Punch Drunk Love", "The Master", "There Will Be Blood", "Sherlock Holmes",
        "Silver Linings Playbook", "Lost in Translation", "The Virgin Suicides",
        "The Bling Ring", "Spirited Away", "The Big Lebowski", "The Goods", "Anchorman",
        "Step Brothers", "Talladega Nights", "The Grey", "The Hurt Locker", "Zero Dark Thirty",
        "The Impossible", "The Lives of Others", "Troll Hunter", "United 93", "Winter's Bone",
        "This is 40", "Casa De Mi Padre", "Gone Baby Gone", "Good Will Hunting", "The Town",
        "Another Earth", "Sond of my Voice", "Alien", "Alien II", "Blad Runner", "Inception",
        "Memento", "The Prestige", "Batman", "Black Swan", "The Fountain", "Pan's Labyrinth"
	};
	
	private static final class CarouselListAdapter extends ArrayAdapter<String> {

        /**
         * The header view
         */
        private static final int ITEM_VIEW_TYPE_HEADER = 0;

        /**
         * * The data in the list.
         */
        private static final int ITEM_VIEW_TYPE_DATA = 1;

        /**
         * Number of views (TextView, CarouselHeader)
         */
        private static final int VIEW_TYPE_COUNT = 2;

        /**
         * Fake header
         */
        private final View mHeader;

        /**
         * Constructor of <code>CarouselListAdapter</code>
         * 
         * @param context The {@link Context} to use
         */
        public CarouselListAdapter(Context context) {
            super(context, 0);
            // Inflate the fake header
            mHeader = LayoutInflater.from(context).inflate(R.layout.faux_carousel, null);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Return a faux header at position 0
            if (position == 0) {
                return mHeader;
            }

            // Recycle ViewHolder's items
            ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(
                        android.R.layout.simple_list_item_1, parent, false);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            // Retieve the data, but make sure to call one less than the current
            // position to avoid counting the faux header.
            final String movies = getItem(position - 1);
            holder.mLineOne.get().setText(movies);
            return convertView;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean hasStableIds() {
            return true;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int getCount() {
            return MOVIES.length + 1;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public long getItemId(int position) {
            if (position == 0) {
                return -1;
            }
            return position - 1;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int getViewTypeCount() {
            return VIEW_TYPE_COUNT;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int getItemViewType(int position) {
            if (position == 0) {
                return ITEM_VIEW_TYPE_HEADER;
            }
            return ITEM_VIEW_TYPE_DATA;
        }
    }

    private static final class ViewHolder {

        public WeakReference<TextView> mLineOne;

        /* Constructor of <code>ViewHolder</code> */
        public ViewHolder(View view) {
            // Initialize mLineOne
            mLineOne = new WeakReference<TextView>((TextView) view.findViewById(android.R.id.text1));
        }

    }
	
	
	
	
	
	
	
	
	private static final class ConcertsAdapter extends ArrayAdapter<MMSModel> {

        private static final int ITEM_VIEW_TYPE_HEADER = 0;
        private static final int ITEM_VIEW_TYPE_DATA = 1;
        private static final int VIEW_TYPE_COUNT = 2;
        
        private final View mHeader;
        private MMSList concertsList;
        private ImageLoader imageLoader;
        private DisplayImageOptions options;
        
        private class ViewHolder {
			public ImageView newsEntryImg;
			public TextView newsEntryTitle;
			public TextView newsEntryDate;
			public TextView newsEntryContent;
		}
        
        public ConcertsAdapter(Context context, MMSList concertsList) {
            super(context, R.layout.list_item_event, concertsList);
            
            this.concertsList = concertsList;
            this.mHeader = LayoutInflater.from(context).inflate(
            		R.layout.faux_carousel, null);
            this.imageLoader = ImageLoader.getInstance();
            this.options = new DisplayImageOptions.Builder()
            	.displayer(new FadeInBitmapDisplayer(800))
            	.showStubImage(R.drawable.default_news)
            	.showImageForEmptyUri(R.drawable.default_news)
            	.showImageOnFail(R.drawable.default_news)
            	.cacheInMemory()
            	.build();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (position == 0) {
                return this.mHeader;
            }
            
            View view = convertView;
            final ViewHolder holder;
            
            if (view == null) {
            	view = LayoutInflater.from(this.getContext())
            			.inflate(R.layout.list_item_event, parent, false);
            	
            	holder = new ViewHolder();
            	view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            
            
            return view;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public int getCount() {
        	if(this.concertsList.size() == 0){
        		return 0;
        	} else {
        		return this.concertsList.size() + 1;
        	}
        }

        @Override
        public int getViewTypeCount() {
            return VIEW_TYPE_COUNT;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0) {
                return ITEM_VIEW_TYPE_HEADER;
            }
            return ITEM_VIEW_TYPE_DATA;
        }
    }
	
}
