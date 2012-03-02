package team2.mainapp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class MainAppActivity extends Activity {

	static String s;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle icicle) {


		super.onCreate(icicle);

		setContentView(R.layout.main);
		Log.d("Debug", "onCreate");		

		Runnable runnable = new Runnable() {
			public void run() {
				String date = "1212/12/12 12:12:12";
				while (true) {
					try {
						Log.d("Debug", "Thread1");
						Thread.sleep(10000);
						Date dateType = new Date();
						s = TCPClient.go(date);
						DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/d HH:mm:ss");
						date = dateFormat.format(dateType);
						Log.d("Debug", "Finished?");
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		new Thread(runnable).start();

		Runnable runnable2 = new Runnable() {
			public void run(){
				while (true) {
					try {
						Log.d("Debug", "Thread2");
						Thread.sleep(10000);
					}
					catch (Exception e) {}
					if (s == null)
						continue;
					Log.d("Debug", s);
					s = null;
				}
			}
		};
		new Thread(runnable2).start();

	Log.d("On Create", "onCreate finished");
	}
}