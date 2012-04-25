	package team2.mainapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class GoogleNews extends Activity {
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.google);
		
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.mainmenu, menu);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menuitem1:
			Toast.makeText(this, "Menu Item 1 selected", Toast.LENGTH_SHORT)
					.show();
			Intent myIntent = new Intent(this, MainAppActivity.class);
			myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
			startActivity(myIntent);
		
			break;
		case R.id.menuitem2:
			Toast.makeText(this, "Menu item 2 selected", Toast.LENGTH_SHORT)
					.show();
			break;

		default:
			break;
		}

		return true;
	}
	
}
