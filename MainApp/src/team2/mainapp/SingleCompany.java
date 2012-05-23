package team2.mainapp;

import java.util.ArrayList;

import team2.mainapp.ViewPagerAdapter.GetDataTask;

import android.R.drawable;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AnalogClock;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TableLayout;
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
	ArrayList<TopicLink> topicLinks;

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
		}else{
			
			double sentiment = thiscompany.getSentiment();
			Double green = sentiment * 7500;
			if(green < 0)
				green = 0d;
			Double red = sentiment * -750;
			if(red < 0)
				red = 0d;
			Log.d("Red",Double.toString(red));
			Log.d("Green",Double.toString(green));
			Integer igreen = (int) Math.round(green);
			Integer ired = (int) Math.round(red);
			if(ired > 125)
				ired = 140;
			if(igreen > 125)
				igreen = 125;
			

		TextView textView1 = (TextView) findViewById(R.id.part1text1);
		TextView textView2 = (TextView) findViewById(R.id.part1text2);
		TextView textView8 = (TextView) findViewById(R.id.textView8);
		TextView textView9 = (TextView) findViewById(R.id.textView9);
		TextView textView10 = (TextView) findViewById(R.id.textView10);
		Button textView11 = (Button) findViewById(R.id.financeLink);
		LinearLayout ll = (LinearLayout) findViewById(R.id.companyHeader);
		
		
		ll.setBackgroundColor((Color.rgb(ired, igreen, 0)));


		textView1.setText(thiscompany.getName());
		textView2.setText("Relevant to " + thiscompany.getArticles() + " topics");
		
		if(thiscompany.isTraded()){
		
		String htmlColor;
		if(thiscompany.getStockChange() < 0)
			htmlColor = "<font color=#BB0000>";
		else
			htmlColor = "<font color=#00BB00>";
		
		textView9.setText(Double.toString(thiscompany.getStockPrice()));
		textView10.setText(Html.fromHtml(htmlColor + Math.abs(thiscompany.getStockChange()) + "</font>"));
		

		textView11.setTag(thiscompany.getUrl());
		textView11.setText("Go to Google Finance Page");
		}else{
			textView9.setText("N/A");
			textView10.setVisibility(View.GONE);
			textView11.setVisibility(View.GONE);
		}
		
		ImageView imageView = (ImageView) findViewById(R.id.imageView1);

		
		double sent = thiscompany.getSentiment();
		String sentimentString;
		if(sent > 0){
			sentimentString = "positive";
			imageView.setImageResource(team2.mainapp.R.drawable.arrow_up);
		}
		else{
			sentimentString = "negative";
			imageView.setImageResource(team2.mainapp.R.drawable.arrow_down);
		}

		textView8.setText("Overall outlook: " + sentimentString);
		
		topicLinks = new ArrayList<TopicLink>();

		TableLayout listView1 = (TableLayout) findViewById(R.id.list1);
		
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
		
		listView1.addView(getRowHeader(listView1));

		int i = 0;
		for(TopicLink tl : topicLinks)
		{
			listView1.addView(getView(i,listView1));
			i++;
		}

		}
	}
	



	public void financeClickHandler(View view) {
		Button tv = (Button) view;
		Log.d("tag",tv.getTag().toString());
		Uri uriUrl = Uri.parse("http://www.google.com/finance?q=" + (String) tv.getTag().toString());
		Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);  
		startActivity(launchBrowser);
	}

	public void topicClickHandler(View view) {
		TextView tv = (TextView) view.findViewById(R.id.uid);
		TextView tv2 = (TextView) view.findViewById(R.id.sector);
		Intent myIntent = new Intent(this, SingleTopic.class);
		myIntent.putExtra("EXTRA_UID", tv.getText());
		myIntent.putExtra("SECTOR", tv2.getText());
		startActivity(myIntent);
	}

	public void onPause(){
		super.onPause();
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
		default:
			break;
		}

		return true;
	}
	

	public View getRowHeader(TableLayout parent) {
		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.tableheader2, parent, false);


		return rowView;
	}
	
	public View getView(int position,  ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.comptoplist, parent, false);

		CompanyLink thisCL = topicLinks.get(position).getCl();
		Topic thistopic = topicLinks.get(position).getT();
		Company thiscompany = topicLinks.get(position).getC();

		TextView textView1 = (TextView) rowView.findViewById(R.id.textView1);
		textView1.setText(thistopic.getTitle());
		TextView textView2 = (TextView) rowView.findViewById(R.id.textView2);

		String s = "Aggregated from " + thistopic.getArts() + " source";
		if((thistopic.getArts() != 1))
			s += "s";
		s += " since " + thistopic.getDate();
		textView2.setText(s);

		ImageView imageView = (ImageView) rowView.findViewById(R.id.imageView1);


		double sent = thisCL.getSentiment();
		String sentiment;
		if(sent > 0){
			sentiment = "positive";
			imageView.setImageResource(team2.mainapp.R.drawable.arrow_up);
		}
		else{
			sentiment = "negative";
			imageView.setImageResource(team2.mainapp.R.drawable.arrow_down);
		}

		TextView textView3 = (TextView) rowView.findViewById(R.id.textView3);
		textView3.setText("Outlook based on this topic is " + sentiment);

		TextView textView4 = (TextView) rowView.findViewById(R.id.uid);
		TextView textView5 = (TextView) rowView.findViewById(R.id.sector);

		textView4.setText(Integer.toString(thistopic.getUid()));
		textView5.setText(thistopic.getSector());

		Log.d("COMPANYTOPICADAPTER","WORKING");

		return rowView;
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