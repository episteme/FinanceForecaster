	package team2.mainapp;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class GoogleNews extends Activity {
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.google);
		
		GooglePageAdapter adapter2 = new GooglePageAdapter( this );
	    ViewPager pager =
	        (ViewPager)findViewById( R.id.viewpager );
	    TitlePageIndicator indicator =
	        (TitlePageIndicator)findViewById( R.id.indicator );
	    pager.setAdapter( adapter2 );
	    indicator.setViewPager( pager );
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
//			myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
			myIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NO_ANIMATION);
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
	
	public void myClickHandler(View view) {
		TextView tv = (TextView) view.findViewById(R.id.labelr);
		Log.d("uri", (String) tv.getText());

		Uri uriUrl = Uri.parse((String) tv.getText());
	    Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);  
	    startActivity(launchBrowser);
	}
	
}
