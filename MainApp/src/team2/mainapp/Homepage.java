package team2.mainapp;
import team2.mainapp.HomeViewPager.GetDataTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class Homepage extends Activity {
	static String s;
	static String sectors;
	static int incrementing;
	static boolean started;
	static HomeViewPager adapter2;
	int ready;
	/** Called when the activity is first created. */

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);

		adapter2 = new HomeViewPager( this );
		ViewPager pager =
				(ViewPager)findViewById( R.id.viewpager );
		TitlePageIndicator indicator =
				(TitlePageIndicator)findViewById( R.id.indicator );
		pager.setAdapter( adapter2 );
		indicator.setViewPager( pager );
	}
	

	@Override
	public void onResume(){
		super.onResume();
			GetDataTask task = adapter2.new GetDataTask();
			task.execute();
	}

	public void topicClickHandler(View view) {
		TextView tv = (TextView) view.findViewById(R.id.uid);
		TextView tv2 = (TextView) view.findViewById(R.id.sector);
		Intent myIntent = new Intent(this, SingleTopic.class);
		myIntent.putExtra("EXTRA_UID", tv.getText());
		myIntent.putExtra("SECTOR", tv2.getText());
		startActivity(myIntent);
	}
	
	public void buttonClickHandler(View view) {
		switch(view.getId()){
		case R.id.button1:
			Intent myIntent = new Intent(this, MainAppActivity.class);
			myIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NO_ANIMATION);
			startActivity(myIntent);
			break;
		case R.id.button2:
			Intent myIntent2 = new Intent(this, MainAppActivity.class);
			myIntent2.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NO_ANIMATION);
			startActivity(myIntent2);
			break;
		}
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