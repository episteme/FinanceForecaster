package team2.mainapp;

import team2.mainapp.CompanyAdapter.GetDataTask2;
import team2.mainapp.R.drawable;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ActionBar.OnNavigationListener;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.SpinnerAdapter;
import android.widget.TextView;

public class CompanyList extends Activity {
	static CompanyAdapter adapter2;
	static boolean started;
	static ViewPager pager;
	Menu menu;
	static MenuItem refresh;
	Handler handler;
	static Thread mRefreshChecker;
	ActionBar actionBar;
	
	public void onCreate(Bundle savedInstanceState) {
		started = false;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.company);

		adapter2 = new CompanyAdapter( this );
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
				case 2:check=false;break;
				case 1:myIntent.setClass(getBaseContext(), MainAppActivity.class);break;
				case 0:myIntent.setClass(getBaseContext(), Homepage.class);break;
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
		GetDataTask2 task = adapter2.new GetDataTask2();
		task.execute();
		GlobalState gState = (GlobalState) getApplication();
		pager.setCurrentItem(gState.getPosition());
		refreshChecker();
		pager.setCurrentItem(gState.getPosition());
		actionBar.setSelectedNavigationItem(2);
	}
	
	public void onPause(){
		super.onPause();
		mRefreshChecker.interrupt();
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
			GetDataTask2 task = adapter2.new GetDataTask2();
			task.execute();
			break;
		case R.id.prefs:
			Intent myIntent4 = new Intent(this, Preferences.class);
			myIntent4.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NO_ANIMATION);
			startActivity(myIntent4);
			break;
		case android.R.id.home:
			finish();
			break;
		default:
			break;
		}

		return true;
	}


	
	public void companyClickHandler(View view) {
		TextView tv = (TextView) view.findViewById(R.id.name);
		Intent myIntent = new Intent(this, SingleCompany.class);
		myIntent.putExtra("company", tv.getText());
		startActivity(myIntent);
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