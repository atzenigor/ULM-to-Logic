package translator.umlclasses;

import java.util.ArrayList;

import translator.ITranslator;

public class UMLClass implements ITranslator {
	protected String name;
	protected ArrayList<UMLAttribute> attributes;
	protected ArrayList<UMLOperation> operations;
	
	public UMLClass(String name){
		attributes = new ArrayList<UMLAttribute>();
		operations = new ArrayList<UMLOperation>();
		this.name = name;
	}
	
	public void addAttribute(UMLAttribute umlAttribute){
		if(umlAttribute != null && umlAttribute.getUMLClass() == this)
			this.attributes.add(umlAttribute);
	}
	public void addOperation(UMLOperation umlOperation){
		if(umlOperation != null && umlOperation.getUMLClass() == this)
			this.operations.add(umlOperation);
	}

	protected String getName(){
		return this.name;
	}
	
	
	@Override
	public String toString() {
		return "UMLClass [name=" + name + ", attributes=" + attributes
				+ ", operations=" + operations + "]";
	}

	@Override
	public ArrayList<String> translateFOL() {
		ArrayList<String> res = new ArrayList<String>();
		for(int i=0; i<attributes.size(); i++) {
			res.addAll(attributes.get(i).translateFOL());
		}
		for(int i=0; i<operations.size(); i++) {
			res.addAll(operations.get(i).translateFOL());
		}
		return res;
	}
	@Override
	public ArrayList<String> translateDL() {
		ArrayList<String> res = new ArrayList<String>();
		for(int i=0; i<attributes.size(); i++) {
			res.addAll(attributes.get(i).translateDL());
		}
		for(int i=0; i<operations.size(); i++) {
			res.addAll(operations.get(i).translateDL());
		}
		return res;
	}
}
