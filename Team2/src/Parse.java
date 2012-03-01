import java.net.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.io.*;

import org.w3c.dom.Document;

import com.alchemyapi.api.AlchemyAPI;
import com.alchemyapi.api.AlchemyAPI_KeywordParams;

public class Parse implements Runnable {

	private Document doc;
	private LinkedList<Topic> topics = new LinkedList<Topic>();
	private String sector;
	private String urlCache;


	Parse(String sector) {
		this.sector = sector;
		urlCache = null;
	}

	public void run() {
		while(true){
			try{
				System.out.println("Starting search");
				URL newsURL = new URL("http://www.google.co.uk/search?q=" + sector + "&tbm=nws&tbs=sbd:1");
				URLConnection uc = newsURL.openConnection();
				uc.setRequestProperty
				( "User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)" );
				BufferedReader in = new BufferedReader(
						new InputStreamReader(
								uc.getInputStream()));
				String inputLine;

				// Grabs all the links and stuffs them into theURLS
				LinkedList<String> theURLS = new LinkedList<String>();
				LinkedList<String> theTitles = new LinkedList<String>();
				while ((inputLine = in.readLine()) != null) {
					if (inputLine.indexOf(" minute") != -1 ||
							inputLine.indexOf("></span>Shopping</a>") != -1) {


						int linkPos = inputLine.indexOf("<a href=\"/url?q=http:");
						int titleEndPos = inputLine.indexOf("</a></h3><br>");
						if (linkPos == -1 || titleEndPos == -1)
							break;
						String noFront = inputLine.substring(linkPos + 16, inputLine.length());
						int endLink = noFront.indexOf("&amp;sa");
						String theURL = noFront.substring(0, endLink);
						theURL = URLDecoder.decode(theURL, "UTF-8");
						theURLS.add(theURL);
						String titleMed = inputLine.substring(0,titleEndPos);
						int titleStartPos = titleMed.lastIndexOf("\">");
						String title = titleMed.substring(titleStartPos + 2);
						title = title.replace("<b>...</b>","");
						title = title.replace("</b>","");
						title = title.replace("<b>","");
						theTitles.add(title);
					}
				}


				// close input stream
				in.close();

				LinkedList<Article> articles = new LinkedList<Article>();
				for(int i = 0; i < theURLS.size(); i++){
					articles.add(new Article(theURLS.get(i),theTitles.get(i),new Date()));
				}

				boolean urlCheck = false;

				LinkedList<Article> newArticles = new LinkedList<Article>();
				if(articles.size() > 0){
					for(Article art : articles) {
						if(art.getURL().equals(urlCache))
							urlCheck = true;
						if(!urlCheck)
							newArticles.add(art);
					}
				}

				String APIkey = "fbde73712800960605177cdcf8cc5ade6ebd15a5";

				for (Article art : newArticles) {
					System.out.println(art.getURL());
					AlchemyAPI alchemyObj = AlchemyAPI.GetInstanceFromString(APIkey);
					AlchemyAPI_KeywordParams params = new AlchemyAPI_KeywordParams();
					params.setKeywordExtractMode("strict");
					params.setMaxRetrieve(10);
					try {
						doc = alchemyObj.URLGetRankedKeywords(art.getURL(), params);
						// Convert output to String
						String alchemyOutput = NewsAnalyst.getStringFromDocument(doc);
						alchemyOutput = NewsAnalyst.removeKeywordXML(alchemyOutput);
						String[] result = alchemyOutput.split(";");
						// Add words to list
						LinkedList<String> newWords = new LinkedList<String>();
						for (int i = 0; i < result.length; i += 2) {
							newWords.addLast(result[i]);
						}
						boolean isNewTopic = true;
						int overlap;
						// Check for overlap in existing topics
						// Current check is if at least 3 words 
						for (Topic t : topics) {
							overlap = 0;
							for (int i = 0; i <= 3; i += 1) {
								if (t.containsWord(newWords.get(i))) {
									overlap++;
									System.out.println("Word overlap found: " + newWords.get(i));
								}
							}
							if (overlap >= 3) {
								isNewTopic = false;
								t.addArticle(art);
								// initial word merging, adds together
								for (int i = 0; i < result.length; i += 2) {
									t.addWord(result[i], Double.parseDouble(result[i+1]));
								}
								System.out.println("Topic overlap found");
							}
						}
						if (isNewTopic) {
							Topic nextTopic = new Topic(art);
							for (int i = 0; i < result.length; i += 2) {
								nextTopic.addWord(result[i], Double.parseDouble(result[i+1]));
							}
							nextTopic.printWords();
							topics.add(nextTopic);
						}
					} catch (Exception e) {
						System.out.println("URL parsed incorrectly");
					}
				}
				// Output information on topics
				for (Topic t : topics) {
					System.out.println("Topic has " + (t.getArticles().size()) + " articles");
					System.out.println(t.getRecentTitle());
					Iterator<Article> iterator = t.getArticles().iterator();  
					while (iterator.hasNext()) {
						Article nextart = iterator.next();
						DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
						String date = dateFormat.format(nextart.getDate());
						System.out.println(nextart.getURL() + " @ " + date);
					} 
					t.printWords();
				}
				if(articles.size() != 0)
					urlCache = articles.get(0).getURL();
				System.out.println("Waiting before rerunning");
				Thread.sleep(10000);
			}catch (Exception e){
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