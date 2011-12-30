package com.rayer.SubPlurkV2;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;

import com.rayer.SubPlurkV2.fragments.EntirePlurkFragment;
import com.rayer.SubPlurkV2.fragments.TrackingFragment;
import com.rayer.SubPlurkV2.fragments.UnreadFragment;
import com.rayer.SubPlurkV2.fragments.UserDataFragment;
import com.rayer.SubPlurkV2.manager.SystemManager;
import com.viewpagerindicator.TitlePageIndicator;
import com.viewpagerindicator.TitleProvider;

public class MainActivity extends FragmentActivity {
	private ViewPager mainViewPager;
	private TitlePageIndicator mainTitle;
	
	class FragmentData {
		Class<? extends Fragment> frag;
		String title;
		FragmentData(Class<? extends Fragment> target, String inTitle) {
			frag = target;
			title = inTitle;
		}
	}

	ArrayList<FragmentData> fragmentList;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.plurk_main);
		
		SystemManager.getInst().init(this);
		
		setupExperiments();
		setupViews();
		
	}
	
	private void setupViews() {
		
		
		mainViewPager = (ViewPager) findViewById(R.id.main_pager);
		mainViewPager.setAdapter(new MainAdapter(getSupportFragmentManager()));
		mainTitle = (TitlePageIndicator) findViewById(R.id.title_indicator);
		mainTitle.setViewPager(mainViewPager);
		mainTitle.setOnPageChangeListener(new OnPageChangeListener(){

			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
				
			}

			@Override
			public void onPageSelected(int position) {
				
			}

			@Override
			public void onPageScrollStateChanged(int state) {
				
			}});
	}
	
	
	private void setupExperiments() {
		fragmentList = new ArrayList<FragmentData>();
		fragmentList.add(new FragmentData(UserDataFragment.class, "使用者資料"));
		fragmentList.add(new FragmentData(EntirePlurkFragment.class, "時間軸"));
		fragmentList.add(new FragmentData(UnreadFragment.class, "未讀/新出現"));
		fragmentList.add(new FragmentData(TrackingFragment.class, "追蹤"));
	}
	

	private class MainAdapter extends FragmentPagerAdapter implements TitleProvider {

		public MainAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public String getTitle(int position) {
			return fragmentList.get(position).title;
		}

		@Override
		public Fragment getItem(int position) {
			return Fragment.instantiate(MainActivity.this, fragmentList.get(position).frag.getName());
		}

		@Override
		public int getCount() {
			return fragmentList.size();
		}
		
	}


	//TODO 這是一個很奇怪的問題，如果不這樣做，會產生null pointer exception錯誤 @@a
	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onSaveInstanceState(android.os.Bundle)
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		try {
			super.onSaveInstanceState(outState);
		}
		catch(RuntimeException e) {
			e.printStackTrace();
		}
	}

}
