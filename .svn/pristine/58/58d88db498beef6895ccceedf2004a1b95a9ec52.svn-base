package com.mms.app.fragment;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Locale;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.tabcarousel.BackScrollManager;
import com.android.tabcarousel.CarouselContainer;
import com.mms.MMSActivity;
import com.mms.MMSApplication;
import com.mms.MMSPreferences;
import com.mms.app.R;
import com.mms.app.fragment.dialog.NewsDetailsDialogFragment;
import com.mms.model.MMSList;
import com.mms.model.MMSModel;
import com.mms.model.MMSNewsEntry;
import com.mms.model.MMSResponse;
import com.mms.request.GetNewsRequest;
import com.mms.request.MMSAsyncRequest;
import com.mms.request.MMSAsyncRequest.OnMMSRequestFinishedListener;

public class NewsFragment extends ListFragment
	implements OnItemClickListener, OnMMSRequestFinishedListener {

	private static final String TAG_NEWS_DETAILS_DIALOG = "news_details_dialog";
	
    private CarouselContainer mCarousel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	return inflater.inflate(R.layout.fragment_news_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mCarousel = (CarouselContainer) this.getActivity().findViewById(R.id.carousel_header);
        
        final ListView listView = getListView();
        listView.setOnScrollListener(new BackScrollManager(mCarousel, null,
                CarouselContainer.TAB_INDEX_FIRST));
        listView.setOnItemClickListener(this);
        listView.setVerticalScrollBarEnabled(false);
        
        String cookie = MMSPreferences.loadString(MMSPreferences.COOKIE, null);
		new MMSAsyncRequest(this).execute(new GetNewsRequest(cookie));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    	if(position == 0){
    		return;
    	}
    	
    	FragmentTransaction transaction = this.getFragmentManager()
				.beginTransaction();
		Fragment prevDialog = this.getFragmentManager().findFragmentByTag(
				TAG_NEWS_DETAILS_DIALOG);

		if (prevDialog != null) {
			transaction.remove(prevDialog);
		}
		transaction.commit();

		Bundle metaData = new Bundle();
		metaData.putSerializable(NewsDetailsDialogFragment.META_ENTRY,
				(Serializable) this.getListAdapter().getItem(position - 1));

		DialogFragment dialog = new NewsDetailsDialogFragment();
		dialog.setArguments(metaData);
		dialog.show(this.getFragmentManager(), TAG_NEWS_DETAILS_DIALOG);
    }
    
    @Override
	public void onMMSRequestFinished(MMSResponse response) {
		this.getView().findViewById(R.id.progress_news)
				.setVisibility(View.GONE);
		try {
			switch (response.getStatus()) {
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
		} catch (Exception e) {
			e.printStackTrace();
			this.onRequestFailed();
		}
	}

	protected void onSuccessfulResponse(MMSResponse response) {
        CarouselListAdapter adapter = new CarouselListAdapter(
        		this.getActivity(), (MMSList) response.getContent());
        setListAdapter(adapter);
	}

	protected void onRequestFailed() {
		Toast.makeText(this.getActivity(), "Connection Failed",
				Toast.LENGTH_SHORT).show();
	}

	protected void onWrongCookie() {
		Toast.makeText(this.getActivity(), "Login again. Wrong Cookie.",
				Toast.LENGTH_SHORT).show();
		((MMSApplication) this.getActivity().getApplication()).logout(this
				.getActivity());
	}

	protected void onErrorResponse(MMSResponse response) {
		Toast.makeText(this.getActivity(), response.getMessage(),
				Toast.LENGTH_SHORT).show();
	}

    private static final class CarouselListAdapter extends ArrayAdapter<MMSModel> {

        private static final int ITEM_VIEW_TYPE_HEADER = 0;
        private static final int ITEM_VIEW_TYPE_DATA = 1;
        private static final int VIEW_TYPE_COUNT = 2;
        
        private static final String DATE_FORMAT = "dd/MM/yyyy";

        private final View mHeader;
        private MMSList newsEntries;
        private SimpleDateFormat dateFormater;

        public CarouselListAdapter(Context context, MMSList newsEntries) {
            super(context, R.layout.list_item_news_entry, newsEntries);
            this.mHeader = LayoutInflater.from(context).inflate(R.layout.faux_carousel, null, false);
            this.dateFormater = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
            this.newsEntries = newsEntries;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (position == 0) {
                return mHeader;
            }

            ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(
                        R.layout.list_item_news_entry, parent, false);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            
            MMSNewsEntry entry = (MMSNewsEntry) this.getItem(0);
            holder.lblTitle.get().setText(entry.getTitle());
            holder.lblContent.get().setText(entry.getContent());
            holder.lblDate.get().setText(this.dateFormater.format(entry
					.getDate()));
            
            return convertView;
        }

        @Override
        public int getCount() {
        	return this.newsEntries.size() + 1;
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
        
        @Override
        public MMSModel getItem(int position) {
        	return this.newsEntries.get(position);
        }
    }

    private static final class ViewHolder {

        public WeakReference<TextView> lblTitle;
        public WeakReference<TextView> lblContent;
        public WeakReference<TextView> lblDate;

        public ViewHolder(View view) {
            this.lblTitle = new WeakReference<TextView>((TextView)
            		view.findViewById(R.id.lbl_news_title));
            this.lblContent = new WeakReference<TextView>((TextView)
            		view.findViewById(R.id.lbl_news_content));
            this.lblDate = new WeakReference<TextView>((TextView)
            		view.findViewById(R.id.lbl_news_date));
        }

    }

}
