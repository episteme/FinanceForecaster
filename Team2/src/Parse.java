import java.net.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.io.*;

import org.jsoup.Jsoup;
import org.w3c.dom.Document;

import com.alchemyapi.api.AlchemyAPI;
import com.alchemyapi.api.AlchemyAPI_KeywordParams;

public class Parse implements Runnable {

	private Document doc; // Stores the response from Alchemy
	private Document sentdoc;
	private LinkedList<Topic> topics = new LinkedList<Topic>();
	private String sector;
	private String urlCache;
	private int uid;


	Parse(String sector) {
		this.sector = sector;
		urlCache = null;
	}

	public void run() {
		uid = 0;
		while (true) {
			try {
				System.out.println("Starting search");
				URL newsURL = new URL("http://www.google.com/search?q=" + sector + "&tbm=nws&tbs=sbd:1");
				URLConnection uc = newsURL.openConnection();
				// Need to pretend we are a browser so that google responds
				uc.setRequestProperty
				( "User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)" );
				BufferedReader in = new BufferedReader(
						new InputStreamReader(
								uc.getInputStream()));
				String inputLine;

				// Grabs all the links and stuffs them into theURLS
				// Grabs all the titles and stuffs them into theTitles
				// Title appears between "> and </a></h3>
				// URL appears between <a href="/url?q= and &amp;
				LinkedList<String> theURLS = new LinkedList<String>();
				LinkedList<String> theTitles = new LinkedList<String>();
				// while we have a line to read
				while ((inputLine = in.readLine()) != null) {
					
					int startIndex = inputLine.indexOf("<h3 class=\"r\"><a href=\"/url?q=");
					while(startIndex != -1)
					{
						inputLine = inputLine.substring(startIndex + 30);
						String urlTemp = inputLine.substring(0,inputLine.indexOf("&amp;sa=U&amp;"));
						urlTemp = URLDecoder.decode(urlTemp, "UTF-8");
						theURLS.add(urlTemp);
						inputLine = inputLine.substring(inputLine.indexOf("\">")+2);
						String titleTemp = inputLine.substring(0,inputLine.indexOf("</a></h3>"));
						titleTemp = Jsoup.parse(titleTemp).text();
						theTitles.add(titleTemp);
						startIndex = inputLine.indexOf("<h3 class=\"r\"><a href=\"/url?q=");
					}
				}

				// close input stream
				in.close();

				// Create a list of articles and titles
				// Is there a guarantee that these match up?
				LinkedList<Article> articles = new LinkedList<Article>();
				for (int i = 0; i < theURLS.size(); i++){
					articles.add(new Article(theURLS.get(i),theTitles.get(i),new Date()));
				}

				
				boolean urlCheck = false;
				LinkedList<Article> newArticles = new LinkedList<Article>();
				if (articles.size() > 0) {
					for (Article art : articles) {
						if (art.getURL().equals(urlCache))
							urlCheck = true;
						if (!urlCheck)
							newArticles.add(art);
					}
				}

				String APIkey = "fbde73712800960605177cdcf8cc5ade6ebd15a5";

				for (Article art : newArticles) {
					//System.out.println(art.getURL());
					art.printMe();
					AlchemyAPI alchemyObj = AlchemyAPI.GetInstanceFromString(APIkey);
					AlchemyAPI_KeywordParams params = new AlchemyAPI_KeywordParams();
					params.setKeywordExtractMode("strict");
					params.setMaxRetrieve(10);
					params.setSentiment(true);
					try {
						doc = alchemyObj.URLGetRankedKeywords(art.getURL(), params);
						sentdoc = alchemyObj.URLGetTextSentiment(art.getURL());
						
						// Convert output to String
						String alchemyOutput = NewsAnalyst.getStringFromDocument(doc);
				        String alchemyOutputSent = NewsAnalyst.getStringFromDocument(sentdoc);
						alchemyOutput = NewsAnalyst.removeKeywordXML(alchemyOutput);
						alchemyOutput = Jsoup.parse(alchemyOutput).text();
						String[] result = alchemyOutput.split(";");
				        alchemyOutputSent = alchemyOutputSent.substring(alchemyOutputSent.indexOf("<score>"));
				        alchemyOutputSent = alchemyOutputSent.substring(7,alchemyOutputSent.lastIndexOf("</score>"));
				        double sentiment = Double.parseDouble(alchemyOutputSent);
						// Add words to list
						LinkedList<String> newWords = new LinkedList<String>();
						LinkedList<Double> newRels = new LinkedList<Double>();
						if (result.length < 6)
							continue;
						for (int i = 0; i < result.length; i += 3){
							newWords.addLast(result[i]);
							newRels.addLast(Double.parseDouble(result[i+1]));
						}
						boolean isNewTopic = true;
						double overlap;
						// Check for overlap in existing topics
						// Current check is if at least 3 words 
						for (Topic t : topics) {
							overlap = 0;
							for (int i = 0; i < newWords.size(); i += 1) {
								if (t.containsWord(newWords.get(i))) {
									overlap = overlap + newRels.get(i) + t.getRel(newWords.get(i));
									System.out.println("Word overlap found: " + newWords.get(i));
								}
							}
							if (overlap >= Math.round((t.getWords().size()/3))) {
								isNewTopic = false;
								t.addArticle(art,sentiment);
								// initial word merging, adds together
								for (int i = 0; i < result.length; i += 3)
									t.addWord(result[i], Double.parseDouble(result[i+1]), Double.parseDouble(result[i+2]));
								t.addSentiment(sentiment);
								System.out.println("Topic overlap found");
							}
						}
						if (isNewTopic) {
							Topic nextTopic = new Topic(art,uid,sentiment);
							uid++;
							for (int i = 0; i < result.length; i += 3)
								nextTopic.addWord(result[i], Double.parseDouble(result[i+1]), Double.parseDouble(result[i+2]));
							nextTopic.printWordData();
							topics.add(nextTopic);
						}
					} catch (Exception e) {
						e.printStackTrace();
						System.out.println("URL parsed incorrectly");
					}
				}
				// Output information on topics
				for (Topic t : topics) {
					System.out.println("Topic has " + (t.getArticles().size()) + " articles");
					System.out.println("Topic has " + t.artsLastHour() + " articles in the last hour");
					System.out.println(t.getRecentTitle());
					Iterator<Article> iterator = t.getArticles().iterator();  
					while (iterator.hasNext()) {
						Article nextart = iterator.next();
						DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
						String date = dateFormat.format(nextart.getDate());
						System.out.println(nextart.getURL() + " @ " + date);
					} 
					// Comment this out to test printTopWords
					// t.printWordData();
					t.printTopWords();
				}
				if (articles.size() != 0)
					urlCache = articles.get(0).getURL();
				System.out.println("Waiting before rerunning");
				Thread.sleep(16000);
			} catch (Exception e){
				e.printStackTrace();
				System.out.println(e);
			}
		}
	}

	public LinkedList<Topic> getTopics() {
		return topics;
	}

	public String getSector() {
		return sector;
	}
}