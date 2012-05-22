import java.util.LinkedList;

public class CompanyList extends LinkedList<Company>  {

	private static final long serialVersionUID = -2879891466110522574L;

	CompanyList()
	{
		super();
	}
	
	Company findCompany(String name) {
		Company found = null;
		for  (Company comp : this)
		{
			if (comp.getName().toLowerCase().equals(name.toLowerCase()))
				return comp;
		}
		return found;
	}
}