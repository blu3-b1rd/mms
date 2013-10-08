package com.mms.app;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.android.tabcarousel.CarouselContainer;
import com.android.tabcarousel.CarouselPagerAdapter;
import com.mms.MMSApplication;
import com.mms.MMSConfig;
import com.mms.app.fragment.ConcertsListFragment;
import com.mms.app.fragment.NewsFragment;
import com.mms.app.util.DepthPageTransformer;

public class NewsActivity extends ActionBarActivity {

	private ViewPager carouselPager;
	private CarouselContainer carousel;
	private DrawerLayout drawerLayout;
	private ActionBarDrawerToggle drawerToggle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(savedInstanceState == null){
			this.overridePendingTransition(R.anim.fade_in, 0);
		}
		this.setContentView(R.layout.activity_news);
		this.configure();
	}
	
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		this.drawerToggle.syncState();
	}

	private void configure() {
		this.configureActionBar();
		
		this.carousel = (CarouselContainer) this
				.findViewById(R.id.carousel_header);
		this.carouselPager = (ViewPager) this.findViewById(R.id.carousel_pager);
		this.configureCarousel();

		this.drawerLayout = (DrawerLayout) this.findViewById(R.id.drawer_layout);
		this.drawerToggle = new ActionBarDrawerToggle(this, this.drawerLayout,
				R.drawable.ic_drawer, 0, 0){
			@Override
			public void onDrawerClosed(View drawerView) {
				supportInvalidateOptionsMenu();
			}
			
			@Override
			public void onDrawerOpened(View drawerView) {
				supportInvalidateOptionsMenu();
			}
		};
		this.drawerLayout.setDrawerListener(this.drawerToggle);
	}
	
	private void configureActionBar(){
		ActionBar actionbar = this.getSupportActionBar();
		actionbar.setDisplayHomeAsUpEnabled(true);
		actionbar.setHomeButtonEnabled(true);
		actionbar.setTitle("News");
		actionbar.setSubtitle(MMSConfig.ARTIST_ALIAS);
	}
	
	private void configureCarousel() {
		this.carousel.setUsesDualTabs(true);
		
		this.carousel.setLabel(CarouselContainer.TAB_INDEX_FIRST, "News");
		this.carousel.setImageResource(CarouselContainer.TAB_INDEX_FIRST,
				R.drawable.band);

		this.carousel.setLabel(CarouselContainer.TAB_INDEX_SECOND, "Events");
		this.carousel.setImageResource(CarouselContainer.TAB_INDEX_SECOND,
				R.drawable.concerts);

		this.carouselPager.setPageMargin(10);
		this.carouselPager.setPageMarginDrawable(android.R.color.transparent);
		this.carouselPager.setPageTransformer(true, new DepthPageTransformer());
		this.carouselPager.setOnPageChangeListener(new CarouselPagerAdapter(
				this.carouselPager, this.carousel));
		this.carouselPager.setAdapter(new NewsPagerAdapter(
				this.getSupportFragmentManager()));
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = this.getMenuInflater();
		inflater.inflate(R.menu.home, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(this.drawerToggle.onOptionsItemSelected(item)){
			return true;
		}
		
		switch(item.getItemId()){
		case R.id.menu_logout:
			((MMSApplication) this.getApplication()).logout(this);
			return true;
		default:	
			return super.onOptionsItemSelected(item);
		}
	}

	private class NewsPagerAdapter extends FragmentPagerAdapter {
		private List<Fragment> newsFragments = new ArrayList<Fragment>();

		public NewsPagerAdapter(FragmentManager fm) {
			super(fm);
			this.newsFragments.add(new NewsFragment());
			this.newsFragments.add(new ConcertsListFragment());
		}

		@Override
		public Fragment getItem(int position) {
			return this.newsFragments.get(position);
		}

		@Override
		public int getCount() {
			return this.newsFragments.size();
		}
	}

}
