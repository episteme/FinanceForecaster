import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;


public class Company {

	String name;
	double price;
	double sentiment;
	int articles;

	String URL;
	boolean URLfound;

	public Company(String n, String sent) {
		name = n.replaceAll("_", " ");
		URLfound = false;
		articles = 1;
		sentiment = Double.parseDouble(sent);
	}

	public void update(String s) {
		articles++;
		double doubarts = (double) articles;
		Double newSent = Double.parseDouble(s);
		sentiment = ((sentiment * (doubarts - 1)) + newSent) / doubarts;
	}

	public void updatePrice() throws Exception {
		String query = name.replace(" ","+");
		URL newsURL = new URL("http://www.google.com/finance?q=" + query);
		URLConnection uc = newsURL.openConnection();
		// Need to pretend we are a browser so that google responds
		uc.setRequestProperty
		( "User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)" );
		BufferedReader in = new BufferedReader(
				new InputStreamReader(
						uc.getInputStream()));
		String inputLine;

		boolean search = false;

		while ((inputLine = in.readLine()) != null) {
			if (inputLine.indexOf("Search</title>") != -1) {
				search = true;
				break;
			}
			if (inputLine.indexOf("</title>") != -1)
				break;
		}

		if (search) {
			while ((inputLine = in.readLine()) != null) {
				if (inputLine.indexOf("<a id=rc-1 ") != -1)
					if (inputLine.indexOf("/finance?q=") != -1) {
						query = inputLine.substring(inputLine.indexOf("/finance?q="),inputLine.indexOf(" >"));
						break;
					}
					else
						break;
			}

			newsURL = new URL("http://www.google.com/" + query);
			uc = newsURL.openConnection();
			// Need to pretend we are a browser so that google responds
			uc.setRequestProperty
			( "User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)" );
			in = new BufferedReader(
					new InputStreamReader(
							uc.getInputStream()));
		}

		boolean importantLine = false;

		while ((inputLine = in.readLine()) != null) {
			if (importantLine) {
				System.out.println(inputLine.substring(inputLine.indexOf("\">")+2,inputLine.indexOf("</span>")));
				break;
			}
			if (inputLine.contains("<span class=\"pr\">"))
				importantLine = true;
		}

		this.URL = query;
		this.URLfound = true;
	}

	public String strSent() {
		return Double.toString(sentiment);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
