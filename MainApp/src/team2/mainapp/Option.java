package team2.mainapp;

public class Option {
	int uid;
	String sector;
	int state;
	
	Option(int uid, String sector, int state)
	{
		this.uid = uid;
		this.sector = sector;
		this.state = state;
	}

	public int getUid() {
		return uid;
	}

	public String getSector() {
		return sector;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}
	
}
