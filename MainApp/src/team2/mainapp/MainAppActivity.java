package team2.mainapp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

public class MainAppActivity extends Activity {
	private Handler handler;
	private ProgressBar progress;
	static String s;
	
	private ListView lv;

	ArrayAdapter<String> ads;
	
/** Called when the activity is first created. */

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		progress = (ProgressBar) findViewById(R.id.progressBar1);
		handler = new Handler();
		
		final ArrayList<String> al = new ArrayList<String>();
		
		al.add("penile");
		al.add("vagenda");
		
		lv = (ListView)	findViewById(R.id.listView1);
		ads = new ArrayAdapter<String>(this, R.layout.rowlayout, R.id.labelo, al);
		lv.setAdapter(ads);
		
	}

	public void startProgress(View view) {
		// Do something long
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i <= 10; i++) {
					final int value = i;
					String date = "1212/12/12 12:12:12";
					try {
						Log.d("Debug", "Thread1");
						Thread.sleep(10000);
						Date dateType = new Date();
						s = TCPClient.go(date);
						
						parseInput(s);
						
					
						DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/d HH:mm:ss");
						date = dateFormat.format(dateType);
						Log.d("Debug", s);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					handler.post(new Runnable() {
						@Override
						public void run() {
							progress.setProgress(value);
							ads.add(s);
						}
					});
				}
			}
		};
		new Thread(runnable).start();
	}
	
	LinkedList<LinkedList<Topic>> allTopics = new LinkedList<LinkedList<Topic>>(); 

	protected void parseInput(String s2) {
		String[] type = s2.split("SPLITINFO\n");
		String[] topicsectors = type[1].split("TOPSTOP\n");
		
		for (String sector : topicsectors)
		{
			LinkedList<Topic> topicSector = new LinkedList<Topic>();
			
			String[] topics = sector.split("SPECTOPS\n");
			for (String topic : topics)
			{
				
				String[] rawData = topic.split(";;\n");
				String[] links = rawData[2].split(";\n");
				String[] words = rawData[3].split(";\n");
				
				ArrayList<String> URLS = new ArrayList<String>();
				for (String link : links)
				{
					String[] bits = link.split("@");
					for(String bit : bits)
						URLS.add(bit);
				}
				
				ArrayList<KeyWord> KeyWords = new ArrayList<KeyWord>();
				for (String word : words)
				{
					String[] bits = word.split("@");
					for (int i = 0; i < bits.length; i += 2)
						KeyWords.add(new KeyWord(bits[i], bits[i+1]));
				}
				
				topicSector.add(new Topic(rawData[0],rawData[1],URLS,KeyWords));
			}
			
			allTopics.add(topicSector);
		}
		
	}
}