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

public class HomeViewPager extends PagerAdapter
implements TitleProvider
{
	ArrayList<HomeView> vl;
	String category;
	private static String[] titles = new String[] {"Oil", "Technology"};

	private final Context context;

	public HomeViewPager( Context context )
	{
		this.context = context;
		vl = new ArrayList<HomeView>();
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
		HomeView v = new HomeView( context , titles[position] );
		((ViewPager)pager).addView( v, 0 );
		vl.add(v);
		return v;
	}
	
	public class GetDataTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			Log.d("RFERESH","REFHSDSJD");
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
			for(HomeView view : vl)
				view.refresh();
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