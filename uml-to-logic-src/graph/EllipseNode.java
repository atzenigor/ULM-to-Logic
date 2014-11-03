package graph;
import java.util.ArrayList;

public class EllipseNode extends Node {
	private ArrayList<ArchEdge> archEdges;
	
	public EllipseNode(String name) {
		super(name);
		archEdges = new ArrayList<ArchEdge>();
	}

	@Override
	public void addEdge(Edge edge) {
		if(edge != null){
			if(edge.getClass() == ArchEdge.class){
				if(edge.getSource() == this || edge.getTarget() == this){
					this.archEdges.add((ArchEdge) edge);
				}
			}
		}
	}

	public ArrayList<ArchEdge> getArchEdges() {
		return archEdges;
	}
	
}
