package team2.mainapp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import team2.mainapp.PullToRefreshListView.OnRefreshListener;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

public class GooglePageAdapter extends PagerAdapter
implements TitleProvider
{
	ArrayList<ArrayList<String[]>> mListItems;
	PullToRefreshListView v;
	String category;
	ArrayList<PullToRefreshListView> vl;
	private static String[] titles = new String[] {"Oil", "Technology" };

	private final Context context;

	public GooglePageAdapter( Context context )
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

		GoogleArrayAdapter adapter = new GoogleArrayAdapter(context,
				mListItems.get(position));

		v.setAdapter(adapter);

		((PullToRefreshListView) v).setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				// Do work to refresh the list here.
				GetDataTask2 task = new GetDataTask2();
				task.execute();
			}
		});
		vl.add(v);

		return v;
	}

	public class GetDataTask2 extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			GlobalState gState = (GlobalState) ((Activity) context).getApplication();
			while(gState.getReady() != true || mListItems.size() == 0)
			{
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void x) {
			GlobalState gState = (GlobalState) ((Activity) context).getApplication();
			for(ArrayList<String[]> list : mListItems)
				list.clear();
			int i = 0;
			// Go through the allTopics data structure, pasting title & date
			for (Sector topicsector : gState.getAllSectors()) {
				for (GoogleStory googStory : topicsector.getGoogStories()) {
					String[] allInfo = new String[5];
					allInfo[0] = googStory.getTitle();
					allInfo[1] = googStory.getTimestamp();
					allInfo[2] = googStory.printKeyWords();
					allInfo[3] = Double.toString(googStory.getSentiment());
					allInfo[4] = googStory.getLink();
					mListItems.get(i).add(allInfo);
				}
				// Complete the refresh
				((PullToRefreshListView) vl.get(i)).onRefreshComplete();
//				DateFormat dateFormat = new SimpleDateFormat("HH:mm");
//				((PullToRefreshListView) vl.get(i)).setLastUpdated(dateFormat.format(new Date()));
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