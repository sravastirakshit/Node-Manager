package dk.nodemanager.vm;

import java.util.ArrayList;
import java.util.List;

public class NodeDetailsVM {

	private Long nodeID;
	private String nodeName;
	private Long parentNodeID;
	private Long rootNodeID;
	private Integer height;
	private List<NodeDetailsVM> children = new ArrayList<NodeDetailsVM>();

	public Long getNodeID() {
		return nodeID;
	}

	public void setNodeID(Long nodeID) {
		this.nodeID = nodeID;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public Long getParentNodeID() {
		return parentNodeID;
	}

	public void setParentNodeID(Long parentNodeID) {
		this.parentNodeID = parentNodeID;
	}

	public Long getRootNodeID() {
		return rootNodeID;
	}

	public void setRootNodeID(Long rootNodeID) {
		this.rootNodeID = rootNodeID;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public List<NodeDetailsVM> getChildren() {
		return children;
	}

	public void setChildren(List<NodeDetailsVM> children) {
		this.children = children;
	}

	@Override
	public String toString() {
		return "NodeDetailsVm [nodeID=" + nodeID + ", nodeName=" + nodeName + ", parentNodeID=" + parentNodeID
				+ ", rootNodeID=" + rootNodeID + ", height=" + height + ", children=" + children + "]";
	}

	/** adding all children of the node **/
	public void addChild(NodeDetailsVM child) {
		if (!this.children.contains(child) && child != null)
			this.children.add(child);
	}

}
