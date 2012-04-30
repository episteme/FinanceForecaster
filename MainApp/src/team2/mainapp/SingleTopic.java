package team2.mainapp;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SingleTopic extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.singletopic);
		
		int uid = Integer.parseInt(getIntent().getStringExtra("EXTRA_UID"));
		String sectorName = getIntent().getStringExtra("SECTOR");
		
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
		
		ArrayList<KeyWord> mListItems = new ArrayList<KeyWord>();

  		KeywordAdapter adapter1 = new KeywordAdapter(this, mListItems);
  		
  		ListView listView1 = (ListView) findViewById(R.id.list1);

		listView1.setAdapter(adapter1);
		
		mListItems.addAll(thistopic.getKeyWords());
		
		ArrayList<String[]> nListItems = new ArrayList<String[]>();

  		TitleAdapter adapter2 = new TitleAdapter(this, nListItems);
  		
  		ListView listView2 = (ListView) findViewById(R.id.list2);

		listView2.setAdapter(adapter2);
		
		nListItems.addAll(thistopic.getTitleUrl());

		
	}
	
	public void titleClickHandler(View view) {
		TextView tv = (TextView) view.findViewById(R.id.textView2);
		Uri uriUrl = Uri.parse((String) tv.getText());
	    Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);  
	    startActivity(launchBrowser);
	}
	
}
