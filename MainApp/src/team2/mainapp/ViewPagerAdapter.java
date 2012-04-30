package team2.mainapp;

import java.util.ArrayList;
import team2.mainapp.PullToRefreshListView.OnRefreshListener;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class ViewPagerAdapter extends PagerAdapter
implements TitleProvider
{
	ArrayList<ArrayList<String[]>> mListItems;
	PullToRefreshListView v;
	String category;
	ArrayList<PullToRefreshListView> vl;
	private static String[] titles = new String[] {"Oil", "Currency" };

	private final Context context;

	public ViewPagerAdapter( Context context )
	{
		this.context = context;
		mListItems = new ArrayList<ArrayList<String[]>>();
		vl = new ArrayList<PullToRefreshListView>();
	}

	@Override
	public String getTitle( int position )
	{
		return titles[ position ];
	}

	@Override
	public int getCount()
	{
		return titles.length;
	}

	@Override
	public Object instantiateItem(View pager, int position )
	{
		v = new PullToRefreshListView( context );
		((ViewPager)pager).addView( v, 0 );
		if(category != null)
			Log.d("superduper",category);
		category = titles[ position ];
		Log.d("dupersuper",category);
		
		mListItems.add(new ArrayList<String[]>());

		MyArrayAdapter adapter = new MyArrayAdapter(context,
				mListItems.get(position));

		v.setAdapter(adapter);

		//		String[] aa = new String[5];
		//		aa[0] = "hello";
		//		aa[1] = "ddxc";
		//		aa[2] = "ddr4";
		//		aa[3] = "31415";
		//		aa[4] = "dsd";
		//
		//		mListItems[position].add(aa);

		// Set a listener to be invoked when the list should be refreshed.
		
		((PullToRefreshListView) v).setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				// Do work to refresh the list here.
				GetDataTask task = new GetDataTask();
				task.execute();
			}
		});
		vl.add(v);
		return v;
	}

	private class GetDataTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			// Do nothing
			return null;
		}

		@Override
		protected void onPostExecute(Void x) {
			Log.d("debug",category);
			GlobalState gState = (GlobalState) ((Activity) context).getApplication();
			for(ArrayList<String[]> list : mListItems)
				list.clear();
			int i = 0;
			// Go through the allTopics data structure, pasting title & date
			for (Sector topicsector : gState.getAllSectors()) {
				java.util.Collections.sort(topicsector.getTopicData());
				for (Topic topic : topicsector.getTopicData()) {
					String[] allInfo = new String[5];
					allInfo[0] = topic.getTitle();
					allInfo[1] = Integer.toString(topic.getArtsLastHour()) + " - " + topic.getDate();
					allInfo[2] = topic.getWords();
					allInfo[3] = Integer.toString(topic.getUid());
					allInfo[4] = topicsector.getName();
					mListItems.get(i).add(allInfo);
				}
				// Complete the refresh
				((PullToRefreshListView) vl.get(i)).onRefreshComplete();
				i++;
			}


		}
	}


	@Override
	public void destroyItem( View pager, int position, Object view )
	{
		((ViewPager)pager).removeView( (View) view );
	}


	@Override
	public boolean isViewFromObject( View view, Object object )
	{
		return view.equals( object );
	}

	@Override
	public void finishUpdate( View view ) {}

	@Override
	public void restoreState( Parcelable p, ClassLoader c ) {}

	@Override
	public Parcelable saveState() {
		return null;
	}

	@Override
	public void startUpdate( View view ) {}
}