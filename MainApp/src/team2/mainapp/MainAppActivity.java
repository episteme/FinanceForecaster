package team2.mainapp;
import team2.mainapp.R.drawable;
import team2.mainapp.ViewPagerAdapter.GetDataTask;
import team2.mainapp.ViewPagerAdapter.GetDataTask2;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ActionBar.OnNavigationListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

public class MainAppActivity extends Activity {
	static String s;
	static String sectors;
	static int incrementing;
	static boolean started;
	static ViewPagerAdapter adapter2;
	int ready;
	static ViewPager pager;
	Menu menu;
	MenuItem refresh;
	Handler handler;
	static Thread mRefreshChecker;
	ActionBar actionBar;
	/** Called when the activity is first created. */

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		adapter2 = new ViewPagerAdapter( this );
		pager =
				(ViewPager)findViewById( R.id.viewpager );
		TitlePageIndicator indicator =
				(TitlePageIndicator)findViewById( R.id.indicator );
		pager.setAdapter( adapter2 );
		indicator.setViewPager( pager );
		
		GlobalState gState = (GlobalState) getApplication();
		int position = gState.getPosition();
		if(position > 3)
			position = 3;
		boolean starPosition = getIntent().getBooleanExtra("STAR",false);
		if(starPosition)
			position = 3;
		Log.d("Position",Integer.toString(position));
		Log.d("starPosition",Boolean.toString(starPosition));
		pager.setCurrentItem(position);
		handler = new Handler();
		
		actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		
		SpinnerAdapter mSpinnerAdapter = ArrayAdapter.createFromResource(actionBar.getThemedContext(), R.array.home_list,
				R.layout.spinner_layout);
		
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
				case 1:check=false;break;
				case 0:myIntent.setClass(getBaseContext(), Homepage.class);break;
				case 2:myIntent.setClass(getBaseContext(), CompanyList.class);break;
				}

				if(check){
					myIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NO_ANIMATION);
					finish();
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
			pager.setCurrentItem(gState.getPosition());
			actionBar.setSelectedNavigationItem(1);
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
			star.setImageResource(drawable.star_empty);
			starstate = 0;
		}else{
			star.setImageResource(drawable.star_yellow);		
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
						gState.getAllSectors().get(3).addTopic(topic);
					}
					else{
						Log.d("setstate","0");
						topic.setState(0);
						gState.getAllSectors().get(3).removeTopic(topic);
					}
					break;
				}
			}
		}
		GetDataTask2 task = adapter2.new GetDataTask2();
		task.execute();
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menutwo, menu);
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
		case android.R.id.home:
			finish();
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
