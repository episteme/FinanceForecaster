package team2.mainapp;

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
 
        Runnable runnable = new Runnable () {
        	public void run() {
        		while (true) {
        			try {
						Thread.sleep(10000);
						s = TCPClient.go();
						Log.d("Debug", "Finished?");
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
        		}
        	}
        };
        new Thread(runnable).start();
    }
    
}