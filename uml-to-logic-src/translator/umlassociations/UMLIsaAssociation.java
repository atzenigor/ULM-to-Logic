package translator.umlassociations;

import java.util.ArrayList;

import translator.ITranslator;

public class UMLIsaAssociation implements ITranslator {
	private String father;
	private String child;

	public UMLIsaAssociation(String father, String child) {
		super();
		this.father = father;
		this.child = child;
	}

	protected String getFather() {
		return father;
	}

	protected String getChild() {
		return child;
	}

	void setFather(String father) {
		this.father = father;
	}

	void setChild(String child) {
		this.child = child;
	}
	

	@Override
	public String toString() {
		return "UMLIsaAssociation [father=" + father + ", child=" + child + "]";
	}

	public ArrayList<String> translateFOL() {
		ArrayList<String> res = new ArrayList<String>();
		res.add("ISA ASSOCIATION FOL");
		res.add("∀ x,y." + child + "(x,y) ⊃ " + father + "(x,y)");
		
		res.add("\n");

		return res;
	}

	public ArrayList<String> translateDL() {	
		ArrayList<String> res2 = new ArrayList<String>();
		res2.add("ISA ASSOCIATION DL");
		res2.add( father + " ⊑ " + child );
		
		res2.add("\n");

		return res2;
}
}
