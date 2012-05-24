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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.ToggleButton;

public class Preferences extends Activity {
	SeekBar sb1;
	SeekBar sb2;
	Spinner spinner;
	Switch switche;
	static int numstate;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.preferences);
		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		sb1 = (SeekBar)findViewById( R.id.seekBar1 );
		sb2 = (SeekBar) findViewById( R.id.seekBar2 );
		switche = (Switch) findViewById( R.id.switch1 );
		

	    spinner = (Spinner) findViewById(R.id.spinner1);
	    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
	            this, R.array.freq, R.layout.spinner_layout2);
	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    spinner.setAdapter(adapter);
	    spinner.setOnItemSelectedListener(new MyOnItemSelectedListener());

		GlobalState gState = (GlobalState) getApplication();

		sb1.setProgress(gState.getAllSectors().get(0).getThreshold());
		sb2.setProgress(gState.getAllSectors().get(1).getThreshold());
		spinner.setSelection(gState.getFrequency());
		numstate = gState.getFrequency();
		switche.setChecked(gState.isOn());
	}
	
	public class MyOnItemSelectedListener implements OnItemSelectedListener {

	    public void onItemSelected(AdapterView<?> parent,
	        View view, int pos, long id) {
	    	numstate = pos;
	    }

	    public void onNothingSelected(AdapterView parent) {
	      // Do nothing.
	    }
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
		gState.setFrequency(numstate);
		if(switche.isChecked())
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

