package graph;

import java.util.ArrayList;

public class RectangleNode extends Node {
	private ArrayList<NormalEdge> normalEdges;
	private ArrayList<ArrowEdge> arrowEdges;
	String label;
	public RectangleNode(String name,String label) {
		super(name);
		normalEdges = new ArrayList<NormalEdge>();
		arrowEdges = new ArrayList<ArrowEdge>();
		this.label = label;
	}

	@Override
	public void addEdge(Edge edge) {
		if(edge != null){
			if(edge.getClass() == NormalEdge.class){
				if(edge.getSource() == this || edge.getTarget() == this){
					this.normalEdges.add((NormalEdge) edge);
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


	public ArrayList<ArrowEdge> getArrowEdges() {
		return arrowEdges;
	}

	public String getLabel(){
		return this.label;
	}
}
