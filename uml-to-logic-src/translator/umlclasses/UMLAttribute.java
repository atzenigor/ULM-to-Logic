package translator.umlclasses;

import java.util.ArrayList;
import translator.ITranslator;

public class UMLAttribute implements ITranslator {
	private UMLClass umlClass;
	private String name;
	private String multiplicity_min; // null if absent
	private String multiplicity_max; // null if absent
	private String domain;

	public UMLAttribute(String name, String domain, String multiplicity_min,
			String multiplicity_max, UMLClass umlClass) {
		this.name = name;
		this.multiplicity_min = multiplicity_min;
		this.multiplicity_max = multiplicity_max;
		this.domain = domain;
		this.umlClass = umlClass;
	}

	public void setUmlClass(UMLClass umlClass) {
		this.umlClass = umlClass;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setMultiplicity_min(String multiplicity_min) {
		this.multiplicity_min = multiplicity_min;
	}

	public void setMultiplicity_max(String multiplicity_max) {
		this.multiplicity_max = multiplicity_max;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	protected UMLClass getUMLClass() {
		return this.umlClass;
	}

	@Override
	public String toString() {
		return "UMLAttribute [umlClass=" + umlClass + ", name=" + name
				+ ", multiplicity_min=" + multiplicity_min
				+ ", multiplicity_max=" + multiplicity_max + ", domain="
				+ domain + "]";
	}

	public ArrayList<String> translateFOL() {
		ArrayList<String> res = new ArrayList<String>();
		res.add("ATTRIBUTE FOL");
		res.add("∀ x,y." + name + "(x,y) ⊃ " + umlClass.getName() + "(x) ∧ "
				+ domain + "(y)");

		if (multiplicity_min != null && multiplicity_max != null) {

			String a1 = "∀ x." + umlClass.getName() + "(x) ⊃ ("
					+ multiplicity_min + " ≤ #{y | " + name + "(x,y)})";

			String c1 = "∀ x." + umlClass.getName() + "(x) ⊃ (#{y | " + name
					+ "(x,y)} ≤ " + multiplicity_max + ")";

			String d1 = "∀ x." + umlClass.getName() + "(x) ⊃ ("
					+ multiplicity_min + " ≤ #{y | " + name + "(x,y)} ≤ "
					+ multiplicity_max + ")";

			if (!multiplicity_min.equals("0") && multiplicity_max.equals("*")) {
				res.add(a1);
			} else if (multiplicity_min.equals("0")
					&& multiplicity_max.equals("*")) {

			} else if (!multiplicity_min.equals("0")
					&& !multiplicity_max.equals("*")) {
				res.add(d1);
			} else if (multiplicity_min.equals("0")
					&& !multiplicity_max.equals("*")) {
				res.add(c1);
			}

		}

		res.add("\n");

		return res;
	}

	public ArrayList<String> translateDL() {
		ArrayList<String> res2 = new ArrayList<String>();
		res2.add("ATTRIBUTE DL");
		res2.add("∃" + name + " ⊑ " + umlClass.getName());
		res2.add("∃" + name + "ˉ ⊑ " + domain );


		if (multiplicity_min != null && multiplicity_max != null) {

			String a =  umlClass.getName() + " ⊑ (≥"
					+ multiplicity_min + name + ")";

			String b = umlClass.getName() + " ⊑ (≤" 
					+ multiplicity_max + name + ")";

			String c =  umlClass.getName() + " ⊑ (≥"
					+ multiplicity_min + name + ") ⊓ (≤" 
					+ multiplicity_max + name + ")";

			if (!multiplicity_min.equals("0") && multiplicity_max.equals("*")) {
				res2.add(a);
			} else if (multiplicity_min.equals("0")
					&& multiplicity_max.equals("*")) {

			} else if (multiplicity_min.equals("0")
					&& !multiplicity_max.equals("*")) {
              res2.add(b);
			
			} else if (!multiplicity_min.equals("0")
					&& !multiplicity_max.equals("*")) {
				res2.add(c);
			}
			
		}

		res2.add("\n");

		return res2;
	}
}

