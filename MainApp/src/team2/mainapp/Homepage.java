package team2.mainapp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import team2.mainapp.HomeViewPager.GetDataTask;
import team2.mainapp.R.drawable;

import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.SimpleOnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

public class Homepage extends Activity {
	static String s;
	static String sectors;
	static int incrementing;
	static boolean started;
	static HomeViewPager adapter2;
	ViewPager pager;
	int ready;
	Menu menu;
	MenuItem refresh;
	Handler handler;
	static Thread mRefreshChecker;
	ActionBar actionBar;
	/** Called when the activity is first created. */

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		adapter2 = new HomeViewPager( this );
		pager =
				(ViewPager)findViewById( R.id.viewpager );
		TitlePageIndicator indicator =
				(TitlePageIndicator)findViewById( R.id.indicator );
		pager.setAdapter( adapter2 );
		indicator.setViewPager( pager );
		GlobalState gState = (GlobalState) getApplication();
		int position = gState.getPosition();
		if(position > 2)
			position = 2;
		pager.setCurrentItem(position);
		handler = new Handler();

		SpinnerAdapter mSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.home_list,
				android.R.layout.simple_spinner_dropdown_item);

		actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		actionBar.setTitle("");


		OnNavigationListener mOnNavigationListener = new OnNavigationListener() {
			// Get the same strings provided for the drop-down's ArrayAdapter
			String[] strings = getResources().getStringArray(R.array.home_list);

			@Override
			public boolean onNavigationItemSelected(int position, long itemId) {
				Intent myIntent = new Intent();
				boolean check = true;

				switch(position) {
				case 0:check=false;break;
				case 1:myIntent.setClass(getBaseContext(), MainAppActivity.class);break;
				case 2:myIntent.setClass(getBaseContext(), GoogleNews.class);break;
				case 3:myIntent.setClass(getBaseContext(), CompanyList.class);break;
				}

				if(check){
					myIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NO_ANIMATION);
					startActivity(myIntent);
				}
				return true;
			}
		};

		actionBar.setListNavigationCallbacks(mSpinnerAdapter, mOnNavigationListener);
	}

	@Override
	public void onResume(){
		super.onResume();
		GetDataTask task = adapter2.new GetDataTask();
		task.execute();
		GlobalState gState = (GlobalState) getApplication();
		pager.setCurrentItem(gState.getPosition());
		refreshChecker();
		actionBar.setSelectedNavigationItem(0);
	}

	public void onPause(){
		super.onPause();
		mRefreshChecker.interrupt();
	}

	public void topicClickHandler(View view) {
		TextView tv = (TextView) view.findViewById(R.id.uid);
		TextView tv2 = (TextView) view.findViewById(R.id.sector);
		Intent myIntent = new Intent(this, SingleTopic.class);
		myIntent.putExtra("EXTRA_UID", tv.getText());
		myIntent.putExtra("SECTOR", tv2.getText());
		startActivity(myIntent);
	}

	public void topicStarClickHandler(View view) {
		String data = (String) view.getTag();
		String uidSector[] = data.split(";");
		ImageView star = (ImageView) view;
		int uid = Integer.parseInt(uidSector[0]);
		int starstate = Integer.parseInt(uidSector[2]);
		if(starstate == 1){
			star.setImageResource(android.R.drawable.btn_star_big_off);
			starstate = 0;
		}else{
			star.setImageResource(android.R.drawable.btn_star_big_on);		
			starstate = 1;
		}
		star.setTag(uidSector[0]+";"+uidSector[1]+";"+starstate);

		String sectorName = uidSector[1];
		GlobalState gState = (GlobalState) getApplication();
		for (Sector sector : gState.getAllSectors()) {
			if(!sector.getName().equals(sectorName))
				continue;
			for (Topic topic : sector.getTopicData()) {
				if (topic.getUid() == uid) {
					if(starstate == 1){
						Log.d("setstate","1");
						topic.setState(1);
						gState.getAllSectors().get(2).addTopic(topic);
					}
					else{
						Log.d("setstate","0");
						topic.setState(0);
						gState.getAllSectors().get(2).removeTopic(topic);
					}
					break;
				}
			}
		}
	}

	public void buttonClickHandler(View view) {
		switch(view.getId()){
		case R.id.button1:
			Intent myIntent = new Intent(this, MainAppActivity.class);
			myIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NO_ANIMATION);
			startActivity(myIntent);
			break;
		case R.id.button2:
			Intent myIntent2 = new Intent(this, CompanyList.class);
			myIntent2.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NO_ANIMATION);
			startActivity(myIntent2);
			break;
		}
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.mainmenu, menu);
		this.menu = menu;
		refresh = menu.findItem(R.id.refresh);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {		
		switch (item.getItemId()) {
		case R.id.refresh:
			GetDataTask task = adapter2.new GetDataTask();
			task.execute();
			break;
		case R.id.starred:
			Intent myIntent3 = new Intent(this, MainAppActivity.class);
			//			myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
			myIntent3.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NO_ANIMATION);
			myIntent3.putExtra("STAR", true);
			startActivity(myIntent3);
			break;
		case R.id.prefs:
			Intent myIntent4 = new Intent(this, Preferences.class);
			myIntent4.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NO_ANIMATION);
			startActivity(myIntent4);
			break;
		default:
			break;
		}

		return true;
	}

	private void refreshChecker(){
		// Do something long
		Runnable runnable = new Runnable() {
			private String s;

			@Override
			public void run() {
				while(true) {
					while(refresh == null)
					{}
					handler.post(new Runnable() {
						@Override
						public void run() {
							GlobalState gState = (GlobalState) getApplication();
							int state = (gState.getRefreshState());
							if(state == -1)
								refresh.setIcon(drawable.ic_menu_refreshr);
							else if(state == 0)
								refresh.setIcon(drawable.ic_menu_refresh);
							else if(state == 1)
								refresh.setIcon(drawable.ic_menu_refreshg);	
						}
					});
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						return;
					}
				}
			}	
		};
		mRefreshChecker = new Thread(runnable);
		mRefreshChecker.start();
	}
}

