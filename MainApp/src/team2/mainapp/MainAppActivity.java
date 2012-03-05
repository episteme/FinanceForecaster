package team2.mainapp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
		ads = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, al);
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
}