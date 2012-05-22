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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class SingleCompany extends Activity {

	ImageButton hide;
	ImageButton star;
	String companyName;
	boolean hidestate;
	boolean starstate;
	Topic thistopic;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.singlecompany);

		companyName = getIntent().getStringExtra("company");

		Log.d("company",companyName);


		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		Company thiscompany = null;

		GlobalState gState = (GlobalState) getApplication();
		loop3:
			for (Sector sector : gState.getAllSectors()) {
				for (Company company : sector.getCompData()) {
					if (company.getName().equals(companyName)) {
						thiscompany = company;
						break loop3;
					}
				}
			}

		if(thiscompany == null){
			Toast toast = Toast.makeText(this, "This company is old and no longer stored", 3000);
			toast.show();
			Log.d("FINISH","FINISH");
			finish();
		}

		TextView textView1 = (TextView) findViewById(R.id.part1text1);
		TextView textView2 = (TextView) findViewById(R.id.part1text2);
		TextView textView3 = (TextView) findViewById(R.id.part1text3);
		ImageView imageView = (ImageView) findViewById(R.id.imageView1);

		textView1.setText(thiscompany.getName());
		textView2.setText("Mentioned in " + thiscompany.getArticles() + " articles.");

		if(thiscompany.isTraded()){

			String htmlColor;
			if(thiscompany.getStockChange() < 0)
				htmlColor = "<font color=#BB0000>";
			else
				htmlColor = "<font color=#00BB00>";

			textView3.setText(Html.fromHtml("Current Stock Value : " + thiscompany.getStockPrice() + " : " 
					+ htmlColor + thiscompany.getStockChange() + "</font>"));
		}else{
			textView3.setVisibility(View.GONE);
		}

		double sent = thiscompany.getSentiment();
		if(sent > 0){
			imageView.setImageResource(team2.mainapp.R.drawable.arrow_up);
		}
		else{
			imageView.setImageResource(team2.mainapp.R.drawable.arrow_down);
		}

		ArrayList<TopicLink> topicLinks = new ArrayList<TopicLink>();

		CompanyTopicAdapter adapter1 = new CompanyTopicAdapter(this, topicLinks);

		ListView listView1 = (ListView) findViewById(R.id.list1);

		listView1.setAdapter(adapter1);
		
		for(Sector sector : gState.getAllSectors()){
			if(sector.getName().equals((thiscompany.getSector().toLowerCase())))
			{
				Log.d("Working","WORKINGFUASLDAS");
				for(Topic topic : sector.getTopicData())
				{
					for(CompanyLink cl : topic.getCompanyLinks())
					{
						if(cl.getCompany().equals(thiscompany.getName())){
							Log.d("topicadded",topic.getTitle());
							topicLinks.add(new TopicLink(cl,topic,thiscompany));
						}
					}
				}
			}
		}

	}

	public void topicClickHandler(View view) {
		TextView tv = (TextView) view.findViewById(R.id.uid);
		TextView tv2 = (TextView) view.findViewById(R.id.sector);
		Intent myIntent = new Intent(this, SingleTopic.class);
		myIntent.putExtra("EXTRA_UID", tv.getText());
		myIntent.putExtra("SECTOR", tv2.getText());
		startActivity(myIntent);
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

class TopicLink{
	CompanyLink cl;
	Topic t;
	Company c;

	TopicLink(CompanyLink cl, Topic t, Company c)
	{
		this.cl = cl;
		this.t = t;
		this.c = c;
	}

	public CompanyLink getCl() {
		return cl;
	}

	public Topic getT() {
		return t;
	}

	public Company getC() {
		return c;
	}
}