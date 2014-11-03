package translator.umlkeyconstraint;

import java.util.ArrayList;

import translator.ITranslator;

public class UMLKeyConstraint implements ITranslator {
	ArrayList<String> nameAssociations;
	String nameCommonClass;

	public UMLKeyConstraint() {
		this.nameAssociations = new ArrayList<String>();
	}

	public void addNameAssociation(String nameAssociation) {
		this.nameAssociations.add(nameAssociation);
	}

	public void setNameCommonClass(String name) {
		this.nameCommonClass = name;
	}

	@Override
	public ArrayList<String> translateFOL() {
		ArrayList<String> res = new ArrayList<String>();
		res.add("KEY CONSTRAINT FOL");
		for (int i = 0; i < nameAssociations.size(); i++) {
			res.add("∀ x,y,y'." + nameAssociations.get(i) + "(x,y) ∧ "
					+ nameAssociations.get(i) + "(x,y') ⊃ y=y'");
			res.add("∀ y,x,x'." + nameAssociations.get(i) + "(x,y) ∧ "
					+ nameAssociations.get(i) + "(x',y) ⊃ x=x'");
		}
		res.add("\n");
		return res;
	}

	@Override
	public ArrayList<String> translateDL() {

		ArrayList<String> res = new ArrayList<String>();
		res.add("KEY CONSTRAINT DL");

		String listOfAss = " ";
		for (int i = 0; i < nameAssociations.size(); i++) {
			if (i < (nameAssociations.size() - 1))
				listOfAss += nameAssociations.get(i) + ", ";
			else
				listOfAss += nameAssociations.get(i);

		}
		res.add("("+"id " + nameCommonClass +  " "+listOfAss+")");

		res.add("\n");
		return res;

	}

}
