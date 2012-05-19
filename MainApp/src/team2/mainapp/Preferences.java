package team2.mainapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.ToggleButton;

public class Preferences extends Activity {
	SeekBar sb1;
	SeekBar sb2;
	SeekBar sb3;
	ToggleButton tb;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.preferences);

		sb1 = (SeekBar)findViewById( R.id.seekBar1 );
		sb2 = (SeekBar) findViewById( R.id.seekBar2 );
		sb3 = (SeekBar) findViewById( R.id.seekBar3);
		tb = (ToggleButton) findViewById( R.id.toggleButton1 );

		GlobalState gState = (GlobalState) getApplication();

		sb1.setProgress(gState.getAllSectors().get(0).getThreshold());
		sb2.setProgress(gState.getAllSectors().get(1).getThreshold());
		sb3.setProgress(gState.getFrequency());
		tb.setChecked(gState.isOn());
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.prefsmenu, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.home:
			Intent myIntent2 = new Intent(this, Homepage.class);
			//			myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
			myIntent2.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NO_ANIMATION);
			startActivity(myIntent2);

			break;
		case R.id.snapshot:
			Intent myIntent3 = new Intent(this, GoogleNews.class);
			//			myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
			myIntent3.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NO_ANIMATION);
			startActivity(myIntent3);

			break;
		case R.id.prefs:
			Intent myIntent4 = new Intent(this, Preferences.class);
			myIntent4.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NO_ANIMATION);
			startActivity(myIntent4);
			break;
		default:
			break;
		}

		return true;
	}

	public void onPause(){
		GlobalState gState = (GlobalState) getApplication();
		gState.getAllSectors().get(0).setThreshold(sb1.getProgress());
		gState.getAllSectors().get(1).setThreshold(sb2.getProgress());
		gState.setFrequency(sb3.getProgress());
		if(tb.getText().equals("Updates Enabled"))
			gState.setOn(true);
		else
			gState.setOn(false);
		super.onPause();
		finish();
	}

	public void resetHandler(View view) {
		GlobalState gState = (GlobalState) getApplication();
		for(Sector sector : gState.getAllSectors())
			for(Topic topic : sector.getTopicData())
				if(topic.getState() == -1)
					topic.setState(0);
	}
}