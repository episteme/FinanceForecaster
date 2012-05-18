package team2.mainapp;

import java.util.ArrayList;

import team2.mainapp.ViewPagerAdapter.GetDataTask;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AnalogClock;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class SingleTopic extends Activity {

	ToggleButton hide;
	ToggleButton star;
	Gauge ac;
	int uid;
	String sectorName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.singletopic);

		uid = Integer.parseInt(getIntent().getStringExtra("EXTRA_UID"));
		sectorName = getIntent().getStringExtra("SECTOR");
		
		Log.d("uidsec",Integer.toString(uid));
		Log.d("uidsec",sectorName);

		Topic thistopic = null;

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
		
		hide = (ToggleButton) findViewById(R.id.toggleButton1);
		star = (ToggleButton) findViewById(R.id.toggleButton2);
		ac = (Gauge) findViewById(R.id.gauge1);


		switch(thistopic.getState()){
			case -1: star.setChecked(false);hide.setChecked(true);break;
			case 0: star.setChecked(false);hide.setChecked(false);break;
			case 1: star.setChecked(true);hide.setChecked(false);break;
		}
		
		double sent = thistopic.getSentiment();
		Log.d("Sentiment",Double.toString(sent));
		
		sent *=2;
		if(sent > 1)
			sent = 1;
		if(sent < -1)
			sent = -1;
		
		ac.setHandTarget((float) sent*100);
		
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
	
	public void myClickHandler(View view) {
		switch(view.getId()){
			case R.id.toggleButton1:
				if(hide.isChecked())
					star.setChecked(false);
				break;
			case R.id.toggleButton2:
				if(star.isChecked())
					hide.setChecked(false);
				break;
		}
		Log.d("Starhide",Boolean.toString(star.isChecked()));
		Log.d("Starhide",Boolean.toString(hide.isChecked()));
	}

	public void titleClickHandler(View view) {
		TextView tv = (TextView) view.findViewById(R.id.textView2);
		Uri uriUrl = Uri.parse((String) tv.getText());
		Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);  
		startActivity(launchBrowser);
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.prefmenu, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		Log.d("Starhide",Boolean.toString(star.isChecked()));
		Log.d("Starhide",Boolean.toString(hide.isChecked()));
		GlobalState gState = (GlobalState) getApplication();
		switch (item.getItemId()) {
		case R.id.menuitem1:
			for (Sector sector : gState.getAllSectors()) {
				if(!sector.getName().equals(sectorName))
					continue;
				for (Topic topic : sector.getTopicData()) {
					if (topic.getUid() == uid) {
						if(star.isChecked()){
							topic.setState(1);
							gState.getOptions().setState(uid, sectorName, 1);
						}
						else if(hide.isChecked()){
							topic.setState(-1);
							gState.getOptions().setState(uid, sectorName, -1);

						}
						else{
							topic.setState(0);
							gState.getOptions().setState(uid, sectorName, 0);
						}
						break;
					}
				}
			}
			finish();
			break;
		default:
			break;
		}

		return true;
	}

}
