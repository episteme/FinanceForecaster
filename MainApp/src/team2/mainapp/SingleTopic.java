package team2.mainapp;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ListView;

public class SingleTopic extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.singletopic);
		
		int uid = Integer.parseInt(getIntent().getStringExtra("EXTRA_UID"));
		
		Topic thistopic = null;
		
		GlobalState gState = (GlobalState) getApplication();
		for (Sector sector : gState.getAllSectors()) {
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
}
