package dk.nodemanager.vm;

public class NodeVM {

	private Long nodeID;
	private Long parentNodeID;
	private String nodeName;

	public Long getNodeID() {
		return nodeID;
	}

	public void setNodeID(Long nodeID) {
		this.nodeID = nodeID;
	}

	public Long getParentNodeID() {
		return parentNodeID;
	}

	public void setParentNodeID(Long parentNodeID) {
		this.parentNodeID = parentNodeID;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	@Override
	public String toString() {
		return "NodeVM [nodeID=" + nodeID + ", parentNodeID=" + parentNodeID + ", nodeName=" + nodeName + "]";
	}

}
