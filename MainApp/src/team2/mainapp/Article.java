package team2.mainapp;
import java.util.Date;


// An Article contains a link to an article
// and the date it was picked up
public class Article {
	
	private String URL;
	private Date date;
	private String title;
	private String description;
	private String source;
	
	public String getDescription() {
		return description;
	}

	public String getSource() {
		return source;
	}

	public Article(String URL, String title, String string, String string2) {
		this.URL = URL;
		this.date = date;
		this.title = title;
		this.source = string;
		this.description = string2;
	}

	public String getTitle() {
		return title;
	}

	public String getURL() {
		return URL;
	}

	public Date getDate() {
		return date;
	}
	
	public void printMe() {
		System.out.println(title);
		System.out.println(URL);
	}

}
