package translator.umlclasses;

import java.util.ArrayList;



import translator.ITranslator;

public class UMLOperation implements ITranslator {
	private UMLClass umlClass;
	private String nome;
	private String domain;
	private ArrayList<UMLParameter> parameters;

	public UMLOperation(String nome, String domain, UMLClass umlClass) {
		super();
		parameters = new ArrayList<UMLParameter>();
		this.umlClass = umlClass;
		this.nome = nome;
		this.domain = domain;
	}

	public void addParameter(String name, String domain) {
		this.parameters.add(new UMLParameter(name, domain));
	}

	public void setUmlClass(UMLClass umlClass) {
		this.umlClass = umlClass;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	protected UMLClass getUMLClass() {
		return this.umlClass;
	}

	public ArrayList<String> translateFOL() {
		ArrayList<String> res = new ArrayList<String>();
		res.add("OPERATION FOL");

		ArrayList<String> parameter_list = new ArrayList<String>(); 
		for (Integer i = 0; i < parameters.size(); i++) {
			if (i == (parameters.size() - 1))
				parameter_list.add("p" + i.toString());
			else
				parameter_list.add("p" + i.toString() + ", ");
		}

		String par_p="";
		for (int i = 0; i < parameter_list.size(); i++) {
			par_p+=parameter_list.get(i);
		}
		par_p=par_p.replace(" ", "");
		

		for (int i = 0; i < parameter_list.size(); i++) {
			String a=parameter_list.get(i);
			a=a.replace(',', ' ');
			a=a.replace(" ", "");
			parameter_list.remove(i);
			parameter_list.add(i, a);
		}
		
		
		String parametersAND1 = "";
        for (int i = 0; i < parameters.size(); i++) {
            if (i != (parameters.size() - 1)) {
                parametersAND1 = parametersAND1 + (parameters.get(i).getDomain() + "(" + parameter_list.get(i) + ") ∧ ");
            } else {
                parametersAND1 = parametersAND1 + (parameters.get(i).getDomain() + "(" + parameter_list.get(i) + ")");
            }
        }
		

		res.add("∀ x," + par_p + ",r. " + nome + "(x,"
				+ par_p + ",r)" + " ⊃ " + parametersAND1);
		res.add("∀ x," + par_p + ",r,r'. " + nome + "(x,"
				+ par_p + ",r)" + " ∧ " + nome + "(x,"
				+ par_p + ",r') ⊃ r=r'");
		res.add("∀ x," + par_p + ",r. " + umlClass.getName() + " ∧ "
				+ nome + "(x," + par_p + ",r)" + " ⊃ " + domain);
		
		res.add("\n");
		return res;
	}

	@Override
	public String toString() {
		return "UMLOperation [umlClass=" + umlClass + ", nome=" + nome
				+ ", domain=" + domain + ", parameters=" + parameters + "]";
	}

	public ArrayList<String> translateDL() {
		 ArrayList<String> res2 = new ArrayList<String>();
		 res2.add("OPERATION DL");


	      //Ensures that the instances of f(P1,...,Pm) represent tuples
	        res2.add( nome+ " ⊑ ∃r0 ⊓ (≤1r0) ⊓ ∃r1 ⊓ (≤1r1) ⊓ ∃r2 ⊓ (≤1r2) ");
	       //Ensures that the parameters of the operation have the correct types
	            res2.add("∃r0 ⊑ " + nome);
	            res2.add("∃r1 ⊑ " + nome);
	            res2.add("∃r2 ⊑ " + nome);


			//ensures that, when the operation is applied to an instance of C, then the
	       //result is an instance of R
	           res2.add("∃r0ˉ ⊑ " + umlClass.getName());
	           res2.add("∃r1ˉ ⊑ " + nome);
	           res2.add("∃r2ˉ ⊑ " + domain);

    res2.add("\n");
	return res2;

	}

}
