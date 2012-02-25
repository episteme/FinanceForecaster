import java.util.Date;


// An Article contains a link to an article
// and the date it was picked up
public class Article {
	
	private String URL;
	private Date date;
	
	public Article(String URL, Date date) {
		this.URL = URL;
		this.date = date;
	}

	public String getURL() {
		return URL;
	}

	public Date getDate() {
		return date;
	}

}
