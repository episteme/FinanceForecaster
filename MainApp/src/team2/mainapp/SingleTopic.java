package team2.mainapp;

import java.util.ArrayList;

import team2.mainapp.ViewPagerAdapter.GetDataTask;

import android.R.drawable;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AnalogClock;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class SingleTopic extends Activity {

	ImageButton hide;
	ImageButton star;
	int uid;
	String sectorName;
	boolean hidestate;
	boolean starstate;
	Topic thistopic;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.singletopic);

		uid = Integer.parseInt(getIntent().getStringExtra("EXTRA_UID"));
		sectorName = getIntent().getStringExtra("SECTOR");
		
		Log.d("uidsec",Integer.toString(uid));
		Log.d("uidsec",sectorName);

		GlobalState gState = (GlobalState) getApplication();
		for (Sector sector : gState.getAllSectors()) {
			if(!sector.getName().equals(sectorName))
				continue;
			for (Topic topic : sector.getTopicData()) {
				if (topic.getUid() == uid) {
					thistopic = topic;
					break;
				}
			}
		}
		
		hide = (ImageButton) findViewById(R.id.hide);
		star = (ImageButton) findViewById(R.id.star);
		hidestate = false;
		starstate = false;

		switch(thistopic.getState()){
			case -1: hide.setImageResource(team2.mainapp.R.drawable.ic_menu_revert);hidestate=true;break;
			case 1: star.setImageResource(drawable.btn_star_big_on);starstate=true;break;
		}
		
		double sent = thistopic.getSentiment();
		Log.d("Sentiment",Double.toString(sent));
		
		sent *=2;
		if(sent > 1)
			sent = 1;
		if(sent < -1)
			sent = -1;
				
		ArrayList<KeyWord> mListItems = new ArrayList<KeyWord>();

		KeywordAdapter adapter1 = new KeywordAdapter(this, mListItems);

		ListView listView1 = (ListView) findViewById(R.id.list1);

		listView1.setAdapter(adapter1);

		mListItems.addAll(thistopic.getKeyWords());

		ArrayList<String[]> nListItems = new ArrayList<String[]>();

		TitleAdapter adapter2 = new TitleAdapter(this, nListItems);

		ListView listView2 = (ListView) findViewById(R.id.list2);

		listView2.setAdapter(adapter2);

		nListItems.addAll(thistopic.getArticle());
	}
	
	public void starClickHandler(View view) {
		GlobalState gState = (GlobalState) getApplication();
		if(starstate){
			star.setImageResource(drawable.btn_star_big_off);
			starstate = false;
			thistopic.setState(0);
			gState.getAllSectors().get(2).removeTopic(thistopic);
		}else{
			hide.setImageResource(drawable.ic_menu_delete);
			gState.getAllSectors().get(2).addTopic(thistopic);
			star.setImageResource(drawable.btn_star_big_on);	
			thistopic.setState(1);
			hidestate = false;
			starstate = true;
		}
	}
	
	public void hideClickHandler(View view) {
		GlobalState gState = (GlobalState) getApplication();
		if(hidestate){
			hide.setImageResource(drawable.ic_menu_delete);
			hidestate = false;
		}else{
			hide.setImageResource(team2.mainapp.R.drawable.ic_menu_revert);
			star.setImageResource(drawable.btn_star_big_off);		
			starstate = false;
			hidestate = true;
			thistopic.setState(0);
			gState.getAllSectors().get(2).removeTopic(thistopic);
		}
	}

	public void titleClickHandler(View view) {
		TextView tv = (TextView) view.findViewById(R.id.textView2);
		Uri uriUrl = Uri.parse((String) tv.getText());
		Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);  
		startActivity(launchBrowser);
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.prefsmenu, menu);
		return true;
	}
	
	public void onPause(){
		super.onPause();
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.home:
			Intent myIntent2 = new Intent(this, Homepage.class);
			//			myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
			myIntent2.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NO_ANIMATION);
			startActivity(myIntent2);

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

}
