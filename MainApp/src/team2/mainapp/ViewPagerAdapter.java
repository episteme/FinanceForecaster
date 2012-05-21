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

public class ViewPagerAdapter extends PagerAdapter
implements TitleProvider
{
	ArrayList<PagerPackage> packages;
	PullToRefreshListView v;
	String category;
	private static String[] titles = new String[] {"Oil", "Technology", "Starred" };

	private final Context context;

	public ViewPagerAdapter( Context context )
	{
		this.context = context;
		packages = new ArrayList<PagerPackage>();
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

		ArrayList<Topic> mListItems = new ArrayList<Topic>();

		TopicListAdapter adapter = new TopicListAdapter(context,
				mListItems);

		v.setAdapter(adapter);

		((PullToRefreshListView) v).setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				// Do work to refresh the list here.
				GetDataTask task = new GetDataTask();
				task.execute();
			}
		});

		packages.add(new PagerPackage(mListItems, v, position, titles[position]));
		GetDataTask task = new GetDataTask();
		task.execute();
		return v;
	}

	public class GetDataTask extends AsyncTask<Void, Void, Void> {
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
			for(PagerPackage pp : packages){
				Log.d("package","package");
				pp.getmListItems().clear();
				
				for(Sector topicsector : gState.getAllSectors()){
					if(topicsector.getName().equals(pp.getCategory().toLowerCase())){
						java.util.Collections.sort(topicsector.getTopicData());
						for (Topic topic : topicsector.getTopicData()) {
							if(topic.getArtsLastHour() == 0)
								break;
							pp.getmListItems().add(topic);
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
		for(PagerPackage pp : packages){
			if(pp.getPosition() == position){
				packages.remove(pp);
				break;}
		}
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

class PagerPackage {
	ArrayList<Topic> mListItems;
	PullToRefreshListView view;
	int position;
	String category;
	
	PagerPackage(ArrayList<Topic> m, PullToRefreshListView v, int p, String cat){
		mListItems = m;
		view = v;
		position = p;
		category = cat;
	}

	public ArrayList<Topic> getmListItems() {
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