package team2.mainapp;

import android.app.ActionBar;
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
		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
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


	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
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
		overridePendingTransition(0, 0);
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