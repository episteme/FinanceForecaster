import java.util.ArrayList;


public class FinanceTest {
	
	static ArrayList<Company> companyList;
	
	public static void main(String [] args) throws Exception {
		
		companyList = new ArrayList<Company>();
		
		String URL = "http://wallstcheatsheet.com/stocks/market-recap-j-p-morgan-bombs-facebook-ipo-valuations.html/";
		
		FinanceParse fp = new FinanceParse(URL);
		
		ArrayList<String[]> articleCompany = fp.parseData(companyList);
		
		for (int i = 0; i < companyList.size(); i++) {
			System.out.println(companyList.get(i).getName() + " " + companyList.get(i).strSent() + " " + companyList.get(i).strRel());
			companyList.get(i).updatePrice();
			companyList.get(i).updatePrice();
			if(companyList.get(i).isTraded())
				System.out.println(companyList.get(i).getStockPrice() + " " + companyList.get(i).getStockChange());
		}
		
	}

}
