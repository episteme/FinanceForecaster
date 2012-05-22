package team2.mainapp;

import java.util.ArrayList;

import team2.mainapp.ViewPagerAdapter.GetDataTask;

import android.R.drawable;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
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

		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);


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

		TextView textView1 = (TextView) findViewById(R.id.part1text2);
		TextView textView2 = (TextView) findViewById(R.id.part1text3);
		TextView textView3 = (TextView) findViewById(R.id.part2text2);
		TextView textView4 = (TextView) findViewById(R.id.part2text3);
		TextView textView5 = (TextView) findViewById(R.id.part4text1);
		TextView textView6 = (TextView) findViewById(R.id.part4text2);
		TextView textView7 = (TextView) findViewById(R.id.part4text3);
		TextView textView8 = (TextView) findViewById(R.id.part4text4);
		TextView textView9 = (TextView) findViewById(R.id.part4text5);

		textView1.setText(thistopic.getArticles().get(0).getTitle());
		textView2.setText("Aggregated from " + thistopic.getArts() +
				" sources, last seen " + thistopic.getDate());
		textView3.setText(thistopic.getArticles().get(0).getSource() + " - "
				+ Html.fromHtml(thistopic.getArticles().get(0).getDescription()));
		textView4.setText(Html.fromHtml("Keywords: " + thistopic.printKeyWords()));

		Log.d("numberofarts",Integer.toString(thistopic.getArticles().size()));

		switch(thistopic.getArticles().size()){
		case 5 : 
			textView5.setVisibility(View.VISIBLE);
			textView5.setText(thistopic.getArticles().get(4).getSource());
			textView5.setTag(thistopic.getArticles().get(4).getURL());
		case 4 : 
			textView6.setVisibility(View.VISIBLE);
			textView6.setText(thistopic.getArticles().get(3).getSource());
			textView6.setTag(thistopic.getArticles().get(3).getURL());
		case 3 : 
			textView7.setVisibility(View.VISIBLE);
			textView7.setText(thistopic.getArticles().get(2).getSource());
			textView7.setTag(thistopic.getArticles().get(2).getURL());
		case 2 : 
			textView8.setVisibility(View.VISIBLE);
			textView8.setText(thistopic.getArticles().get(1).getSource());
			textView8.setTag(thistopic.getArticles().get(1).getURL());
		case 1 : 
			textView9.setVisibility(View.VISIBLE);
			textView9.setText(thistopic.getArticles().get(0).getSource());
			textView9.setTag(thistopic.getArticles().get(0).getURL());
		}



		ArrayList<CompanyLink> companyLinks = new ArrayList<CompanyLink>();

		CompanyLinkAdapter adapter1 = new CompanyLinkAdapter(this, companyLinks);

		ListView listView1 = (ListView) findViewById(R.id.list1);

		listView1.setAdapter(adapter1);

		for(CompanyLink cl : companyLinks){
			
			loop2: for(Sector sector : gState.getAllSectors())
			{
				for(Company company : sector.getCompData())
				{
					if(company.getName().equals(cl.getCompany())){
						companyLinks.add(cl);
						break loop2;
					}
				}
			}
		}
	}

	public void titleClickHandler(View view) {
		TextView tv = (TextView) view;
		Uri uriUrl = Uri.parse((String) tv.getTag());
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

		case R.id.prefs:
			Intent myIntent4 = new Intent(this, Preferences.class);
			myIntent4.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NO_ANIMATION);
			startActivity(myIntent4);
			break;
		case android.R.id.home:
			finish();
		default:
			break;
		}

		return true;
	}

}
