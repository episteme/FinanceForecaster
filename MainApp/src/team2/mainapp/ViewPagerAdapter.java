package team2.mainapp;

import java.util.ArrayList;
import team2.mainapp.PullToRefreshListView.OnRefreshListener;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

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

	public class GetDataTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			// Do nothing
			return null;
		}

		@Override
		protected void onPostExecute(Void x) {
			GlobalState gState = (GlobalState) ((Activity) context).getApplication();
			for(ArrayList<String[]> list : mListItems)
				list.clear();
			int i = 0;
			while(gState.getReady() != true)
			{
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			// Go through the allTopics data structure, pasting title & date
			for (Sector topicsector : gState.getAllSectors()) {
				java.util.Collections.sort(topicsector.getTopicData());
				int j = 0;
				for (Topic topic : topicsector.getTopicData()) {
					if(topic.getArtsLastHour() == 0)
						break;
					String[] allInfo = new String[6];
					allInfo[0] = topic.getTitle();
					allInfo[1] = Integer.toString(topic.getArtsLastHour()) + " - " + topic.getDate();
					allInfo[2] = topic.getWords();
					allInfo[3] = Integer.toString(topic.getUid());
					allInfo[4] = topicsector.getName();
					allInfo[5] = Integer.toString(topic.getState());
					mListItems.get(i).add(allInfo);
					if(j == 9)
						break;
					j++;
				}
				// Complete the refresh
				((PullToRefreshListView) vl.get(i)).onRefreshComplete();
				if(i > 10)
					break;
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