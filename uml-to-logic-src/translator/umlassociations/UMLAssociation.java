package translator.umlassociations;

import java.util.ArrayList;

import translator.ITranslator;

public class UMLAssociation implements ITranslator {
	protected String name;
	protected String nameFirstClass;
	protected String nameSecondClass;
	protected String firstMultiplicityMax;
	protected String firstMultiplicityMin;
	protected String secondMultiplicityMax;
	protected String secondMultiplicityMin;
	protected String firstRole;
	protected String secondRole;

	public UMLAssociation() {
	};

	public void setName(String name) {
		this.name = name;
	}

	public void setNameFirstClass(String nameFirstClass) {
		this.nameFirstClass = nameFirstClass;
	}

	public void setNameSecondClass(String nameSecondClass) {
		this.nameSecondClass = nameSecondClass;
	}

	public void setFirstMultiplicityMax(String firstMultiplicityMax) {
		this.firstMultiplicityMax = firstMultiplicityMax;
	}

	public void setFirstMultiplicityMin(String firstMultiplicityMin) {
		this.firstMultiplicityMin = firstMultiplicityMin;
	}

	public void setSecondMultiplicityMax(String secondMultiplicityMax) {
		this.secondMultiplicityMax = secondMultiplicityMax;
	}

	public void setSecondMultiplicityMin(String secondMultiplicityMin) {
		this.secondMultiplicityMin = secondMultiplicityMin;
	}

	public void setFirstRole(String firstRole) {
		this.firstRole = firstRole;
	}

	public void setSecondRole(String secondRole) {
		this.secondRole = secondRole;
	}

	public String getName() {
		return name;
	}

	public String getNameFirstClass() {
		return nameFirstClass;
	}

	public String getNameSecondClass() {
		return nameSecondClass;
	}

	public String getFirstMultiplicityMax() {
		return firstMultiplicityMax;
	}

	public String getFirstMultiplicityMin() {
		return firstMultiplicityMin;
	}

	public String getSecondMultiplicityMax() {
		return secondMultiplicityMax;
	}

	public String getSecondMultiplicityMin() {
		return secondMultiplicityMin;
	}

	public String getFirstRole() {
		return firstRole;
	}

	public String getSecondRole() {
		return secondRole;
	}

	@Override
	public ArrayList<String> translateFOL() {
		ArrayList<String> res = new ArrayList<String>();
		res.add("ASSOCIATION FOL");

		res.add("∀ x,y." + name + "(x,y) ⊃ " + nameFirstClass + "(x) ∧ "
				+ nameSecondClass + "(y)");

		if (firstMultiplicityMin == null) {
			firstMultiplicityMin = "0";
			firstMultiplicityMax = "*";
		}
		if (secondMultiplicityMin == null) {
			secondMultiplicityMin = "0";
			secondMultiplicityMax = "*";
		}

		String a1 = "∀ x." + nameFirstClass + "(x) ⊃ (" + firstMultiplicityMin
				+ " ≤ #{y | " + name + "(x,y)})";
		String a2 = "∀ x." + nameSecondClass + "(x) ⊃ ("
				+ secondMultiplicityMin + " ≤ #{x | " + name + "(x,y)})";
		String c1 = "∀ x." + nameFirstClass + "(x) ⊃ (#{y | " + name
				+ "(x,y)} ≤ " + firstMultiplicityMax + ")";
		String c2 = "∀ x." + nameSecondClass + "(x) ⊃ (#{y | " + name
				+ "(x,y)} ≤ " + secondMultiplicityMax + ")";
		String d1 = "∀ x." + nameFirstClass + "(x) ⊃ (" + firstMultiplicityMin
				+ " ≤ #{y | " + name + "(x,y)} ≤ " + firstMultiplicityMax + ")";
		String d2 = "∀ y." + nameSecondClass + "(x) ⊃ ("
				+ secondMultiplicityMin + " ≤ #{x | " + name + "(x,y)} ≤ "
				+ secondMultiplicityMax + ")";

		if (!firstMultiplicityMin.equals("0")
				&& firstMultiplicityMax.equals("*")
				&& !secondMultiplicityMin.equals("0")
				&& secondMultiplicityMax.equals("*")) {
			res.add(a1);
			res.add(a2);
		} else if (firstMultiplicityMin.equals("0")
				&& !firstMultiplicityMax.equals("*")
				&& secondMultiplicityMin.equals("0")
				&& !secondMultiplicityMax.equals("*")) {
			res.add(c1);
			res.add(c2);
		} else if (!firstMultiplicityMin.equals("0")
				&& !firstMultiplicityMax.equals("*")
				&& !secondMultiplicityMin.equals("0")
				&& !secondMultiplicityMax.equals("*")) {
			res.add(d1);
			res.add(d2);
		} else if (!firstMultiplicityMin.equals("0")
				&& firstMultiplicityMax.equals("*")
				&& secondMultiplicityMin.equals("0")
				&& secondMultiplicityMax.equals("*")) {
			res.add(a1);
		} else if (firstMultiplicityMin.equals("0")
				&& firstMultiplicityMax.equals("*")
				&& !secondMultiplicityMin.equals("0")
				&& secondMultiplicityMax.equals("*")) {
			res.add(a2);
		} else if (!firstMultiplicityMin.equals("0")
				&& firstMultiplicityMax.equals("*")
				&& secondMultiplicityMin.equals("0")
				&& !secondMultiplicityMax.equals("*")) {
			res.add(a1);
			res.add(c2);
		} else if (firstMultiplicityMin.equals("0")
				&& !firstMultiplicityMax.equals("*")
				&& !secondMultiplicityMin.equals("0")
				&& secondMultiplicityMax.equals("*")) {
			res.add(c1);
			res.add(a2);
		} else if (!firstMultiplicityMin.equals("0")
				&& firstMultiplicityMax.equals("*")
				&& !secondMultiplicityMin.equals("0")
				&& !secondMultiplicityMax.equals("*")) {
			res.add(a1);
			res.add(d2);
		} else if (!firstMultiplicityMin.equals("0")
				&& !firstMultiplicityMax.equals("*")
				&& !secondMultiplicityMin.equals("0")
				&& secondMultiplicityMax.equals("*")) {
			res.add(d1);
			res.add(a2);
		} else if (firstMultiplicityMin.equals("0")
				&& firstMultiplicityMax.equals("*")
				&& secondMultiplicityMin.equals("0")
				&& !secondMultiplicityMax.equals("*")) {
			res.add(c2);
		} else if (firstMultiplicityMin.equals("0")
				&& !firstMultiplicityMax.equals("*")
				&& secondMultiplicityMin.equals("0")
				&& secondMultiplicityMax.equals("*")) {
			res.add(c1);
		} else if (firstMultiplicityMin.equals("0")
				&& !firstMultiplicityMax.equals("*")
				&& !secondMultiplicityMin.equals("0")
				&& !secondMultiplicityMax.equals("*")) {
			res.add(c1);
			res.add(d2);
		} else if (!firstMultiplicityMin.equals("0")
				&& !firstMultiplicityMax.equals("*")
				&& secondMultiplicityMin.equals("0")
				&& !secondMultiplicityMax.equals("*")) {
			res.add(d1);
			res.add(c2);
		} else if (firstMultiplicityMin.equals("0")
				&& firstMultiplicityMax.equals("*")
				&& !secondMultiplicityMin.equals("0")
				&& !secondMultiplicityMax.equals("*")) {
			res.add(d2);
		} else if (!firstMultiplicityMin.equals("0")
				&& !firstMultiplicityMax.equals("*")
				&& secondMultiplicityMin.equals("0")
				&& secondMultiplicityMax.equals("*")) {
			res.add(d1);
		}

		res.add("\n");

		return res;

	}

	@Override
	public String toString() {
		return "UMLAssociation [name=" + name + ", nameFirstClass="
				+ nameFirstClass + ", nameSecondClass=" + nameSecondClass
				+ ", firstMultiplicityMax=" + firstMultiplicityMax
				+ ", firstMultiplicityMin=" + firstMultiplicityMin
				+ ", secondMultiplicityMax=" + secondMultiplicityMax
				+ ", secondMultiplicityMin=" + secondMultiplicityMin
				+ ", firstRole=" + firstRole + ", secondRole=" + secondRole
				+ "]";
	}

	@Override
	public ArrayList<String> translateDL() {
		ArrayList<String> res2 = new ArrayList<String>();
		res2.add("ASSOCIATION DL");

		res2.add("∃" + name + " ⊑ " + nameFirstClass ); 
		res2.add("∃" + name + "ˉ ⊑ " + nameSecondClass ); 

				

		String a1 =  nameFirstClass + " ⊑ (≥" + firstMultiplicityMin + name +")";
		String a2 =  nameSecondClass + " ⊑ (≥" + secondMultiplicityMin + name+")";
		String c1 =  nameFirstClass + " ⊑ (≤" + firstMultiplicityMax + name+")";
		String c2 =  nameSecondClass + " ⊑ (≤" + secondMultiplicityMax + name+ ")";
		String d1 =  nameFirstClass + " ⊑ (≥" + firstMultiplicityMin
				+ name+") ⊓ (≤" + firstMultiplicityMax + name+")";
		String d2 =  nameFirstClass + " ⊑ (≥" + secondMultiplicityMin
				+name+ ") ⊓ (≤" + secondMultiplicityMax + name+")";

		if (!firstMultiplicityMin.equals("0")
				&& firstMultiplicityMax.equals("*")
				&& !secondMultiplicityMin.equals("0")
				&& secondMultiplicityMax.equals("*")) {
			res2.add(a1);
			res2.add(a2);
		} else if (firstMultiplicityMin.equals("0")
				&& !firstMultiplicityMax.equals("*")
				&& secondMultiplicityMin.equals("0")
				&& !secondMultiplicityMax.equals("*")) {
			res2.add(c1);
			res2.add(c2);
		} else if (!firstMultiplicityMin.equals("0")
				&& !firstMultiplicityMax.equals("*")
				&& !secondMultiplicityMin.equals("0")
				&& !secondMultiplicityMax.equals("*")) {
			res2.add(d1);
			res2.add(d2);
		} else if (!firstMultiplicityMin.equals("0")
				&& firstMultiplicityMax.equals("*")
				&& secondMultiplicityMin.equals("0")
				&& secondMultiplicityMax.equals("*")) {
			res2.add(a1);
		} else if (firstMultiplicityMin.equals("0")
				&& firstMultiplicityMax.equals("*")
				&& !secondMultiplicityMin.equals("0")
				&& secondMultiplicityMax.equals("*")) {
			res2.add(a2);
		} else if (!firstMultiplicityMin.equals("0")
				&& firstMultiplicityMax.equals("*")
				&& secondMultiplicityMin.equals("0")
				&& !secondMultiplicityMax.equals("*")) {
			res2.add(a1);
			res2.add(c2);
		} else if (firstMultiplicityMin.equals("0")
				&& !firstMultiplicityMax.equals("*")
				&& !secondMultiplicityMin.equals("0")
				&& secondMultiplicityMax.equals("*")) {
			res2.add(c1);
			res2.add(a2);
		} else if (!firstMultiplicityMin.equals("0")
				&& firstMultiplicityMax.equals("*")
				&& !secondMultiplicityMin.equals("0")
				&& !secondMultiplicityMax.equals("*")) {
			res2.add(a1);
			res2.add(d2);
		} else if (!firstMultiplicityMin.equals("0")
				&& !firstMultiplicityMax.equals("*")
				&& !secondMultiplicityMin.equals("0")
				&& secondMultiplicityMax.equals("*")) {
			res2.add(d1);
			res2.add(a2);
		} else if (firstMultiplicityMin.equals("0")
				&& firstMultiplicityMax.equals("*")
				&& secondMultiplicityMin.equals("0")
				&& !secondMultiplicityMax.equals("*")) {
			res2.add(c2);
		} else if (firstMultiplicityMin.equals("0")
				&& !firstMultiplicityMax.equals("*")
				&& secondMultiplicityMin.equals("0")
				&& secondMultiplicityMax.equals("*")) {
			res2.add(c1);
		} else if (firstMultiplicityMin.equals("0")
				&& !firstMultiplicityMax.equals("*")
				&& !secondMultiplicityMin.equals("0")
				&& !secondMultiplicityMax.equals("*")) {
			res2.add(c1);
			res2.add(d2);
		} else if (!firstMultiplicityMin.equals("0")
				&& !firstMultiplicityMax.equals("*")
				&& secondMultiplicityMin.equals("0")
				&& !secondMultiplicityMax.equals("*")) {
			res2.add(d1);
			res2.add(c2);
		} else if (firstMultiplicityMin.equals("0")
				&& firstMultiplicityMax.equals("*")
				&& !secondMultiplicityMin.equals("0")
				&& !secondMultiplicityMax.equals("*")) {
			res2.add(d2);
		} else if (!firstMultiplicityMin.equals("0")
				&& !firstMultiplicityMax.equals("*")
				&& secondMultiplicityMin.equals("0")
				&& secondMultiplicityMax.equals("*")) {
			res2.add(d1);
		}

		res2.add("\n");
		return res2;

	}

}
