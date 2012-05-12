import java.util.ArrayList;

import com.alchemyapi.api.AlchemyAPI;
import com.alchemyapi.api.AlchemyAPI_NamedEntityParams;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class FinanceParse {
	
	private String APIkey = "fbde73712800960605177cdcf8cc5ade6ebd15a5";
	private String URL;
	
	public FinanceParse(String URL) {
		this.URL = URL;
	}
	
	public ArrayList<String[]> parseData(ArrayList<Company> cList) throws Exception {
		
		AlchemyAPI alchemyObj = AlchemyAPI.GetInstanceFromString(APIkey);
		AlchemyAPI_NamedEntityParams entityParams = new AlchemyAPI_NamedEntityParams();
		entityParams.setSentiment(true);
		entityParams.setDisambiguate(true);
		entityParams.setMaxRetrieve(10);
		Document doc = alchemyObj.URLGetRankedNamedEntities(URL, entityParams);
		
		NodeList nList = doc.getElementsByTagName("entity");
		
		ArrayList<String[]> retList = new ArrayList<String[]>();

		for (int i = 0; i < nList.getLength(); i++) {
			Element e = (Element) nList.item(i);
			
			if (getTagValue("type", e).equals("Company")) {
				String[] s = new String[3];
				NodeList neList = e.getElementsByTagName("sentiment");
				Element e2 = (Element) neList.item(0);
				String type = getTagValue("type", e2);
				if (type.equals("neutral"))
					s[1] = "0.0";
				else
					s[1] = getTagValue("score", e2);
				
				NodeList neList2 = e.getElementsByTagName("disambiguated");
				if (neList2.getLength() == 0)
					continue;
				Element e3 = (Element) neList2.item(0);
				s[0] = getTagValue("name", e3);
				s[2] = getTagValue("relevance", e);
				retList.add(s);
				boolean found = false;
				for (int k = 0; k < cList.size(); k++) {
					if (cList.get(k).getName().equals(s[0])) {
						cList.get(k).update(s[1]);
						found = true;
					}
				}
				if (!found) {
					Company c = new Company(s[0], s[1]);
					cList.add(c);
				}
			}			
		}
		return retList;
	}
	
	private static String getTagValue(String sTag, Element eElement) {
		NodeList nlList = eElement.getElementsByTagName(sTag).item(0).getChildNodes();
		
		Node nValue = (Node) nlList.item(0);
		
		return nValue.getNodeValue();
	}

}
