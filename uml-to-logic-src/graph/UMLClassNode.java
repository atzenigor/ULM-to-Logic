package graph;

import java.util.ArrayList;

public class UMLClassNode extends Node {
	private ArrayList<NormalEdge> normalEdges;
	private ArrayList<ArrowEdge> arrowEdges;
	private String attributesLabel;
	private String operationsLabel;
	
	public UMLClassNode(String name,String attributesLabel,String operationsLabel) {
		super(name);
		normalEdges = new ArrayList<NormalEdge>();
		arrowEdges = new ArrayList<ArrowEdge>();
		this.attributesLabel = attributesLabel;
		this.operationsLabel = operationsLabel;
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

	public String getAttributesLabel() {
		return attributesLabel;
	}

	public String getOperationsLable() {
		return operationsLabel;
	}

	public ArrayList<NormalEdge> getNormalEdges() {
		return normalEdges;
	}

	public ArrayList<ArrowEdge> getArrowEdges() {
		return arrowEdges;
	}
}
