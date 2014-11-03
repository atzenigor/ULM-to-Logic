package graph;

public class NormalEdge extends Edge {
	private String[] labels;
	/**
	 * @param source
	 * @param target
	 * @param multMin null if it's connected with a rectangle node.
	 * @param multMax null if it's connected with a rectangle node.
	 * @param role null if it's connected with a rectangle node.
	 */
	public NormalEdge(Node source, Node target, String[] labels) {
		super(source, target);
		this.labels = labels;
	}

	public String[] getLabels(){
		return this.labels;
	}

}
