package dk.nodemanager.entity;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "NODE")

public class Node {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "nodeSeq")
	@SequenceGenerator(name="nodeSeq", sequenceName = "NODE_SEQUENCE", allocationSize = 1)
	@Column(name = "NODE_ID")
	private Long nodeID;

	@Column(name = "NODE_NAME", nullable = false)
	private String nodeName;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PARENT_NODE_ID")
	private Node parentNode;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "NODE_CHILDREN")
	private List<Node> childNodeList = new LinkedList<Node>();

	@ManyToOne
	@JoinColumn(name = "ROOT_NODE_ID")
	private Node rootNode;

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

	public Node getParentNode() {
		return parentNode;
	}

	public void setParentNode(Node parentNode) {
		this.parentNode = parentNode;
	}

	public Node getRootNode() {
		return rootNode;
	}

	public void setRootNode(Node rootNode) {
		this.rootNode = rootNode;
	}

	public List<Node> getChildNodeList() {
		return childNodeList;
	}

	public void setChildNodeList(List<Node> childNodeList) {
		this.childNodeList = childNodeList;
	}
}