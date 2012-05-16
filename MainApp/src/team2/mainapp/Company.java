package team2.mainapp;


public class Company {

	String name;
	double sentiment;
	double relevance;
	int articles;
	double stockPrice;
	double stockChange;
	private boolean traded;

	public Company(String name, String sentiment, String relevance, String articles, String stockPrice, String stockChange, String traded) {
		this.name = name;
		this.sentiment = Double.parseDouble(sentiment);
		this.relevance = Double.parseDouble(relevance);
		this.articles = Integer.parseInt(articles);
		this.stockPrice = Double.parseDouble(stockPrice);
		this.stockChange = Double.parseDouble(stockChange);
		this.traded = Boolean.parseBoolean(traded);
	}

	public String getName() {
		return name;
	}

	public double getSentiment() {
		return sentiment;
	}

	public double getRelevance() {
		return relevance;
	}

	public int getArticles() {
		return articles;
	}

	public double getStockPrice() {
		return stockPrice;
	}

	public double getStockChange() {
		return stockChange;
	}

	public boolean isTraded() {
		return traded;
	}
}