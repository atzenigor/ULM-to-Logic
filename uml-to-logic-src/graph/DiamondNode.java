package graph;

import java.util.ArrayList;

public class DiamondNode extends Node {
	private ArrayList<NormalEdge> normalEdges;
	private ArrayList<ArchEdge> archEdges;
	private ArrayList<ArrowEdge> arrowEdges;
	
	public DiamondNode(String name) {
		super(name);
		normalEdges = new ArrayList<NormalEdge>();
		archEdges = new ArrayList<ArchEdge>();
		arrowEdges = new ArrayList<ArrowEdge>();
	}

	@Override
	public void addEdge(Edge edge) {
		if(edge != null){
			if(edge.getClass() == NormalEdge.class){
				if(edge.getSource() == this || edge.getTarget() == this){
					this.normalEdges.add((NormalEdge) edge);
				}
			}
			if(edge.getClass() == ArchEdge.class){
				if(edge.getSource() == this || edge.getTarget() == this){
					this.archEdges.add((ArchEdge) edge);
				}
			}
			if(edge.getClass() == ArrowEdge.class){
				if(edge.getSource() == this || edge.getTarget() == this){
					this.arrowEdges.add((ArrowEdge) edge);
				}
			}
		}
	}

	public ArrayList<NormalEdge> getNormalEdges() {
		return normalEdges;
	}

	public ArrayList<ArchEdge> getArchEdges() {
		return archEdges;
	}

	public ArrayList<ArrowEdge> getArrowEdges() {
		return arrowEdges;
	}

}
