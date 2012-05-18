package team2.mainapp;

import java.util.Date;
import java.util.LinkedList;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

public class GlobalState extends Application {

	static LinkedList<Sector> allSectors;
	static OptionList options;
	static boolean ready;
	static boolean on;
	int frequency;
	static Date lastUpdated;
	static int position;

	public void onCreate() {
		Log.d("Starting","Application");
		Intent intent = new Intent(this, Background.class);
		startService(intent);

		this.setReady(false);
		this.setOn(true);
		this.setFrequency(0);
		this.position = 0;

		this.setSectors(new LinkedList<Sector>());
		this.getAllSectors().add(new Sector("oil"));
		this.getAllSectors().add(new Sector("technology"));
		options = new OptionList();
	}

	public OptionList getOptions() {
		return options;
	}

	public int getFrequency() {
		return frequency;
	}

	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}

	public LinkedList<Sector> getAllSectors() {
		return allSectors;
	}

	public void setSectors(LinkedList<Sector> allTopics) {
		GlobalState.allSectors = allTopics;
	}

	public void setReady(boolean r){
		ready = r;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		GlobalState.position = position;
	}

	public boolean getReady(){
		return ready;
	}

	public boolean isOn() {
		return on;
	}

	public void setOn(boolean o) {
		on = o;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setUpdated(Date date) {
		lastUpdated = date;
	}
}

class OptionList extends LinkedList<Option>
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -354907508809963100L;

	OptionList(){
		super();
	}

	public Integer getState(int uid, String sector)
	{
		for(Option option : this)
		{
			if(option.getUid() == uid && option.getSector().equals(sector))
				return option.getState();
		}
		return 0;
	}

	public void setState(int uid, String sector, int state)
	{
		for(Option option : this)
		{
			if(option.getUid() == uid && option.getSector().equals(sector) && state != 0){
				option.setState(state);
				break;
			}
			else if(option.getUid() == uid && option.getSector().equals(sector)){
				this.remove(option);
				break;
			}
		}
	}

}
