package team2.mainapp;

import team2.mainapp.CompanyAdapter.GetDataTask2;
import team2.mainapp.R.drawable;
import android.app.Activity;
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
import android.widget.TextView;

public class CompanyList extends Activity {
	static CompanyAdapter adapter2;
	static boolean started;
	static ViewPager pager;
	Menu menu;
	static MenuItem refresh;
	Handler handler;
	static Thread mRefreshChecker;
	
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
		pager.setCurrentItem(gState.getPosition());
		handler = new Handler();
	}

	@Override
	public void onResume(){
		super.onResume();
		GetDataTask2 task = adapter2.new GetDataTask2();
		task.execute();
		GlobalState gState = (GlobalState) getApplication();
		if(gState.getRefreshState() == 1){
			gState.setRefreshState(0);
			refresh.setIcon(drawable.ic_menu_refresh);
		}
		pager.setCurrentItem(gState.getPosition());
		refreshChecker();
		pager.setCurrentItem(gState.getPosition());
	}
	
	public void onPause(){
		super.onPause();
		mRefreshChecker.interrupt();
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
			GetDataTask2 task = adapter2.new GetDataTask2();
			task.execute();
			GlobalState gState = (GlobalState) getApplication();
			if(gState.getRefreshState() == 1){
				gState.setRefreshState(0);
				refresh.setIcon(drawable.ic_menu_refresh);
			}
			refresh.setIcon(drawable.ic_menu_refresh);
			break;
		case R.id.snapshot:
			Intent myIntent3 = new Intent(this, GoogleNews.class);
			//			myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
			myIntent3.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NO_ANIMATION);
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

	public void myClickHandler(View view) {
		TextView tv = (TextView) view.findViewById(R.id.labelr);
		Log.d("uri", (String) tv.getText());

		Uri uriUrl = Uri.parse((String) tv.getText());
		Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);  
		startActivity(launchBrowser);
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
							else if(state == 1)
								refresh.setIcon(drawable.ic_menu_refreshg);	
						}
					});
					try {
						Thread.sleep(5000);
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