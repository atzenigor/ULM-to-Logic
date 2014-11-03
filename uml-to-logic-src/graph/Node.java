package graph;

public abstract class Node {
	private String name;
	
	public Node(String name){
		this.name = name;
	}
	public String getName(){ 
		return this.name;
	}
	public abstract void addEdge(Edge edge);
}
