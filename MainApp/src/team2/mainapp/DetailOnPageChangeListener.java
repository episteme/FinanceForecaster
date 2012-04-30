package team2.mainapp;

import android.support.v4.view.ViewPager;

public class DetailOnPageChangeListener extends ViewPager.SimpleOnPageChangeListener {
	
	private int currentPage;
	
	@Override
	public void onPageSelected(int position) {
		currentPage = position;
	}
	
	public int getCurrentPage() {
		return currentPage;
	}

}
