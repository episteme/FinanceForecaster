package team2.mainapp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import team2.mainapp.PullToRefreshListView.OnRefreshListener;
import team2.mainapp.ViewPagerAdapter.GetDataTask;

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
	ArrayList<GooglePackage> packages;
	PullToRefreshListView v;
	String category;
	private static String[] titles = new String[] {"Oil", "Technology", "Energy"};

	private final Context context;

	public GooglePageAdapter( Context context )
	{
		this.context = context;
		packages = new ArrayList<GooglePackage>();
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

		ArrayList<GoogleStory> mListItems = new ArrayList<GoogleStory>();

		GoogleArrayAdapter adapter = new GoogleArrayAdapter(context,
				mListItems);

		v.setAdapter(adapter);

		((PullToRefreshListView) v).setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				// Do work to refresh the list here.
				GetDataTask2 task = new GetDataTask2();
				task.execute();
			}
		});

		packages.add(new GooglePackage(mListItems, v, position, titles[position]));
		GetDataTask2 task = new GetDataTask2();
		task.execute();
		return v;
	}

	public class GetDataTask2 extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			GlobalState gState = (GlobalState) ((Activity) context).getApplication();
			while(gState.getReady() != true)
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
			gState.setRefreshState(0);
			for(GooglePackage pp : packages){
				Log.d("package","package");
				pp.getmListItems().clear();
				
				for(Sector topicsector : gState.getAllSectors()){
					if(topicsector.getName().equals(pp.getCategory().toLowerCase())){
						for (GoogleStory goog : topicsector.getGoogStories()) {
							pp.getmListItems().add(goog);
						}
						// Complete the refresh
						((PullToRefreshListView) pp.getView()).onRefreshComplete();
					}
				}
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

class GooglePackage {
	ArrayList<GoogleStory> mListItems;
	PullToRefreshListView view;
	int position;
	String category;
	
	GooglePackage(ArrayList<GoogleStory> m, PullToRefreshListView v, int p, String cat){
		mListItems = m;
		view = v;
		position = p;
		category = cat;
	}

	public ArrayList<GoogleStory> getmListItems() {
		return mListItems;
	}

	public String getCategory() {
		return category;
	}

	public PullToRefreshListView getView() {
		return view;
	}

	public int getPosition() {
		return position;
	}
}