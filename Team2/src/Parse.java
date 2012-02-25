import java.net.*;
import java.util.LinkedList;
import java.io.*;

import org.w3c.dom.Document;

import com.alchemyapi.api.AlchemyAPI;
import com.alchemyapi.api.AlchemyAPI_KeywordParams;

public class Parse {
	
	static Document doc;
	
    public static void main(String[] args) throws Exception {
    
        URL newsURL = new URL("http://www.google.co.uk/search?q=oil&tbm=nws&tbs=sbd:1");
        URLConnection uc = newsURL.openConnection();
        uc.setRequestProperty
        ( "User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)" );
        BufferedReader in = new BufferedReader(
                                new InputStreamReader(
                                uc.getInputStream()));
        String inputLine;
        
        // Grabs all the links and stuffs them into theURLS
        LinkedList<String> theURLS = new LinkedList<String>();
        while ((inputLine = in.readLine()) != null) {
        	if (inputLine.indexOf(" minute") != -1 ||
        			inputLine.indexOf("></span>Shopping</a>") != -1) {
        		int linkPos = inputLine.indexOf("<a href=\"/url?q=http:");
        		if (linkPos == -1)
        			break;
        		String noFront = inputLine.substring(linkPos + 16, inputLine.length());
        		int endLink = noFront.indexOf("&amp;sa");
        		String theURL = noFront.substring(0, endLink);
                theURLS.add(theURL);
        	}
        }
        in.close();
        
        String APIkey = "fbde73712800960605177cdcf8cc5ade6ebd15a5";
        
        for (String URL : theURLS) {
        	System.out.println(URL);
            AlchemyAPI alchemyObj = AlchemyAPI.GetInstanceFromString(APIkey);
            AlchemyAPI_KeywordParams params = new AlchemyAPI_KeywordParams();
            params.setKeywordExtractMode("strict");
            params.setMaxRetrieve(10);
            try {
            	doc = alchemyObj.URLGetRankedKeywords(URL, params);
            	// Convert output to String
            	String alchemyOutput = NewsAnalyst.getStringFromDocument(doc);
            	
            	alchemyOutput = NewsAnalyst.removeKeywordXML(alchemyOutput);

            	String[] result = alchemyOutput.split(";");

            	for (String s: result) {
            		System.out.println(s);
            	}
            } catch (Exception e) {}
        }
    }
}