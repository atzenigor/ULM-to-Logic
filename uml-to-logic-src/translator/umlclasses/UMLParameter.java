package translator.umlclasses;

public class UMLParameter {
	private String name;
	private String domain;
	public UMLParameter(String name, String domain) {
		this.name = name;
		this.domain = domain;
	}
	String getName() {
		return name;
	}
	String getDomain() {
		return domain;
	}
	@Override
	public String toString() {
		return "UMLParameter [name=" + name + ", domain=" + domain + "]";
	}
	
}
