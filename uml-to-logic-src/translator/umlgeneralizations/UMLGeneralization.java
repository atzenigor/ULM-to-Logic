package translator.umlgeneralizations;

import java.util.ArrayList;

import translator.ITranslator;
//import translator.umlclasses.UMLClass;

public class UMLGeneralization implements ITranslator {
	private String fatherName;
	private ArrayList<String> childrenNames;
	private boolean disjoint;
	private boolean complete;

	public UMLGeneralization(String name, String fatherName, boolean disjoint,
			boolean complete) {
		super();
		childrenNames = new ArrayList<String>();
		this.fatherName = fatherName;
		this.disjoint = disjoint;
		this.complete = complete;
	}
	public void addChild(String name) {
		this.childrenNames.add(name);
	}

	@Override
	public ArrayList<String> translateFOL() {
		ArrayList<String> res = new ArrayList<String>();
		res.add("GENERALIZATION FOL");

		for (int i = 0; i < childrenNames.size(); i++) {
			res.add("∀ x." + childrenNames.get(i) + "(x) ⊃ " + fatherName
					+ "(x)");

		}

		res.add("");

		if (disjoint == true) {

			for (int i = 0; i < childrenNames.size(); i++) {
				for (int j = 0; j < childrenNames.size(); j++) {
					if (j > i) {
						res.add("∀ x." + childrenNames.get(i) + "(x) ⊃ ¬"
								+ childrenNames.get(j) + "(x)");
					}

				}
			}
			res.add("");

			
		}


		if (complete == true) {
			String string_complete = "∀ x." + fatherName + "(x) ⊃ ("
					+ childrenNames.get(0)+"(x)";
			for (int i = 1; i < childrenNames.size(); i++) {
				if (i == childrenNames.size() - 1) {
					string_complete = string_complete.concat(" ∨ "
							+ childrenNames.get(i)+"(x)" + ")");
				} else {
					string_complete = string_complete.concat(" ∨ "
							+ childrenNames.get(i)+"(x)");
				}
			}

			res.add(string_complete);
			
			res.add("\n");

		}
		


		return res;
	}
	

	@Override
	public String toString() {
		return "UMLGeneralization [fatherName=" + fatherName
				+ ", childrenNames=" + childrenNames + ", disjoint=" + disjoint
				+ ", complete=" + complete + "]";
	}

	@Override
	public ArrayList<String> translateDL() {
		ArrayList<String> res2 = new ArrayList<String>();
		res2.add("GENERALIZATION DL");

		for (int i = 0; i < childrenNames.size(); i++) {
			res2.add(childrenNames.get(i) + " ⊑ " + fatherName
					);

		}

		res2.add("");

		if (disjoint == true) {

			for (int i = 0; i < childrenNames.size(); i++) {
				for (int j = 0; j < childrenNames.size(); j++) {
					if (j > i) {
						res2.add(childrenNames.get(i) + " ⊑ ¬"
								+ childrenNames.get(j));
					}

				}
			}
		}

		res2.add("");

		if (complete == true) {
			String string_complete =fatherName + " ⊑ "
					+ childrenNames.get(0);
			for (int i = 1; i < childrenNames.size(); i++) {
				if (i == childrenNames.size() - 1) {
					string_complete = string_complete.concat(" ⊔ "
							+ childrenNames.get(i) );
				} else {
					string_complete = string_complete.concat(" ⊔ "
							+ childrenNames.get(i));
				}
			}

			res2.add(string_complete);
		}
		return res2;
	}
}
