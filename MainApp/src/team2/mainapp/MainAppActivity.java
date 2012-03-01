package team2.mainapp;

import android.app.Activity;
import android.os.Bundle;

public class MainAppActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
    	 
        super.onCreate(icicle);
 
        setContentView(R.layout.main);
 
        Thread cThread = new Thread(new TCPClient());
        cThread.start();       
 
    }
}