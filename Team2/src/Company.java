import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class Company {

	String name;
	double price;
	double sentiment;
	double relevance;
	int articles;
	double stockPrice;
	double stockChange;

	String URL;
	boolean URLfound;
	private boolean traded;

	public Company(String n, String sent, String relevance) {
		name = n.replaceAll("_", " ");
		URLfound = false;
		articles = 1;
		sentiment = Double.parseDouble(sent);
		traded = false;
	}

	public void update(String s) {
		articles++;
		double doubarts = (double) articles;
		Double newSent = Double.parseDouble(s);
		sentiment = ((sentiment * (doubarts - 1)) + newSent) / doubarts;
	}

	public void updatePrice() {
		if(!URLfound || traded){
			try{
				String query;
				if(URLfound)
					query = URL;
				else
					query = name.replace(" ","+");

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
					if (inputLine.indexOf("</title>") != -1){
						break;
					}
				}

				boolean check = false;

				if (search) {
					while ((inputLine = in.readLine()) != null) {
						if (inputLine.indexOf("<a id=rc-1 ") != -1)
							if (inputLine.indexOf("/finance?q=") != -1) {
								check = true;
								query = inputLine.substring(inputLine.indexOf("/finance?q=")+11,inputLine.indexOf("\" >"));
								break;
							}
							else
								break;
					}

					if(check){
						newsURL = new URL("http://www.google.com/finance?q=" + query);
						uc = newsURL.openConnection();
						// Need to pretend we are a browser so that google responds
						uc.setRequestProperty
						( "User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)" );
						in = new BufferedReader(
								new InputStreamReader(
										uc.getInputStream()));
					}
				}

				int importantLine = 0;

				while ((inputLine = in.readLine()) != null) {
					switch (importantLine) {
					case 1 : stockPrice = Double.parseDouble(inputLine.substring(inputLine.indexOf("\">")+2,inputLine.indexOf("</span>")).replace(",",""));
					importantLine=2;
					break;
					case 2 : importantLine=3; break;
					case 3 : importantLine=4; break;
					case 4 : stockChange = Double.parseDouble(inputLine.substring(inputLine.indexOf("_c\">")+4,inputLine.indexOf("</span>")).replace(",",""));
					importantLine=5;
					traded=true;
					break;
					case 5 : importantLine=6; break;
					case 6 : importantLine=7; break;
					case 7 : importantLine=8; break;
					case 8 : importantLine=9; break;
					case 9 : 
						if(inputLine.indexOf("<div>Pre-market") != -1)
						{
							stockPrice = Double.parseDouble(inputLine.substring(inputLine.indexOf("_el\">")+5,inputLine.indexOf("</span>")).replace(",",""));
							importantLine = 10;
						}
						else
							importantLine = 11;
						break;
					case 10 : stockChange = Double.parseDouble(inputLine.substring(inputLine.indexOf("\">")+2,inputLine.indexOf("</span>")).replace(","," ")); 
					importantLine = 11; 
					break;
					case 11 : break;
					default : if (inputLine.contains("<span class=\"pr\">"))
						importantLine = 1;
					break;
					}
					if (importantLine == 11)
						break;		
				}

				this.URL = query;
				this.URLfound = true;
			}catch (Exception e) {
				this.traded = false;
				this.URLfound = true;
			}
		}
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

	public boolean isTraded() {
		return traded;
	}

	public void setTraded(boolean traded) {
		this.traded = traded;
	}

	public double getStockPrice() {
		return stockPrice;
	}

	public double getStockChange() {
		return stockChange;
	}

	public String strRel() {
		return Double.toString(relevance);
	}

}
