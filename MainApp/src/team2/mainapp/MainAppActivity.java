package team2.mainapp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;

import team2.mainapp.PullToRefreshListView.OnRefreshListener;
import team2.mainapp.ViewPagerAdapter.GetDataTask;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ListActivity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainAppActivity extends Activity {
	static String s;
	static String sectors;
	static int incrementing;
	static boolean started;
	static ViewPagerAdapter adapter2;
	int ready;
	/** Called when the activity is first created. */

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		adapter2 = new ViewPagerAdapter( this );
		ViewPager pager =
				(ViewPager)findViewById( R.id.viewpager );
		TitlePageIndicator indicator =
				(TitlePageIndicator)findViewById( R.id.indicator );
		pager.setAdapter( adapter2 );
		indicator.setViewPager( pager );
				
		GlobalState gState = (GlobalState) getApplication();

		
	}
	
	@Override
	public void onResume(){
		super.onResume();
		if(started){
			GetDataTask task = adapter2.new GetDataTask();
			task.execute();
		}
		else{
			started = true;
		}
	}

	public void myClickHandler(View view) {
		TextView tv = (TextView) view.findViewById(R.id.uid);
		TextView tv2 = (TextView) view.findViewById(R.id.sector);
		Intent myIntent = new Intent(this, SingleTopic.class);
		myIntent.putExtra("EXTRA_UID", tv.getText());
		myIntent.putExtra("SECTOR", tv2.getText());
		startActivity(myIntent);
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.mainmenu, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menuitem1:
			Intent myIntent = new Intent(this, GoogleNews.class);
			myIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NO_ANIMATION);
			startActivity(myIntent);
			break;
		case R.id.menuitem2:
			Intent myIntent2 = new Intent(this, Preferences.class);
			myIntent2.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NO_ANIMATION);
			startActivity(myIntent2);
			break;

		default:
			break;
		}

		return true;
	}
}
