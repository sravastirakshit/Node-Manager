package dk.nodemanager.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Stack;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dk.nodemanager.entity.Node;
import dk.nodemanager.exception.NodeManagerException;
import dk.nodemanager.repository.NodeRepository;
import dk.nodemanager.vm.NodeDetailsVM;
import dk.nodemanager.vm.NodeVM;

@Service
public class NodeServices {
	
	@Autowired
	NodeRepository nodeRepository;

	private static final Logger logger = LogManager.getLogger(NodeServices.class);

	/********* Get all nodes 
	 * @throws NodeManagerException *********/
	public List<NodeDetailsVM> getAllNodes() throws NodeManagerException {
		logger.info("Get all children of a given node id : START:  " + LocalDateTime.now());
		Iterable<Node> allNodes = nodeRepository.findAll();
		if(allNodes!=null) {
			List<NodeDetailsVM> NodeVms = new ArrayList<NodeDetailsVM>();
			allNodes.forEach(n -> NodeVms.add(createNodeVm(n)));
			logger.info("Get all children of a given node id : END:  " + LocalDateTime.now());
			return NodeVms;
		}else {
			throw new NodeManagerException("No Nodes found");
		}		
	}
	
	/********* Get a node by ID 
	 * @throws NodeManagerException *********/
	public NodeDetailsVM getNodeById(Long nodeID) throws NodeManagerException {
		logger.info("Get a given node id : START:  " + LocalDateTime.now());
		Optional<Node> optionalNode = nodeRepository.findById(nodeID);
		if (optionalNode.isPresent()) {
			return createNodeVm(optionalNode.get());
		}else
		{
			throw new NodeManagerException("Node cannot be found");
		}
		
		
	}
	
	/********* Get all children of a given node id 
	 * @throws NodeManagerException *********/
	public List<NodeDetailsVM> getNodeChildren(Long nodeID) throws NodeManagerException {

		logger.info("Get all children of a given node id : START:  " + LocalDateTime.now());
		List<NodeDetailsVM> responseList = new ArrayList<>();
		Optional<Node> optionalNode = nodeRepository.findById(nodeID); // fetching node details of the given node
		if(optionalNode.isPresent()) {
			List<Node> nodeList = getNodesAsFlatList(optionalNode.get()); // arranging all nodes including their children, in a flat list
			nodeList.forEach(n -> responseList.add(createNodeVm(n))); // converting all nodes including their children, to view manager objects
			NodeDetailsVM NodeVm = getNestedNodeDetails(responseList, nodeID); // fetching all nested node of the given node id
			logger.info("Get all children of a given node id : END:  " + LocalDateTime.now());
			return NodeVm.getChildren();
		}else {
			throw new NodeManagerException("Node cannot be found");
		}
	}
	
	/*** Update a given node id 
	 * @throws NodeManagerException ***/
	public NodeDetailsVM updateNode(NodeVM nvm) throws NodeManagerException {
		logger.info("Update Node : START:  " + LocalDateTime.now());
		
		Optional<Node> sourceNodeOptional = nodeRepository.findById(nvm.getNodeID());
		Optional<Node> destParentNodeOptional = nodeRepository.findById(nvm.getParentNodeID());

		/******Validations******/
		if(!sourceNodeOptional.isPresent()) {
			throw new NodeManagerException("Node cannot be found");
		}

		if(!destParentNodeOptional.isPresent()) {
			throw new NodeManagerException("New Parent Node cannot be found");
		}
		
		Node sourceNode = sourceNodeOptional.get();
		Node destParentNode = destParentNodeOptional.get();

		if(sourceNode.getParentNode() == null) {
			throw new NodeManagerException("Root node cannot be moved");
		}
		
		if((sourceNode.getParentNode().getNodeID().intValue() != destParentNode.getNodeID().intValue()) && isDestParentAChild(destParentNode,sourceNode.getNodeID().intValue())) {
			throw new NodeManagerException("The destination parent is a child of source node");
		}

		//Save now
		sourceNode.setNodeName(nvm.getNodeName());
		sourceNode.setParentNode(destParentNode);
		nodeRepository.save(sourceNode);
		destParentNode.getChildNodeList().add(sourceNode);
		nodeRepository.save(destParentNode);

		NodeDetailsVM vm = createNodeVm(sourceNode);

		logger.info("Update Node : END:  " + LocalDateTime.now());
		return vm;
	}
	


	/*** Create a given node  
	 * @throws NodeManagerException ***/
	public NodeDetailsVM createNode(NodeVM nvm) throws NodeManagerException {
		logger.info("Create Node : START:  " + LocalDateTime.now());
		
		Optional<Node> parentNodeOptional = nodeRepository.findById(nvm.getParentNodeID());
		if(!parentNodeOptional.isPresent()) {
			throw new NodeManagerException("Parent Node cannot be found");
		}
		
		Node parent = parentNodeOptional.get();
		
		Node node = new Node();
		node.setNodeName(nvm.getNodeName());
		node.setParentNode(parent);
		node.setRootNode(parent.getRootNode()!=null?parent.getRootNode():parent);
		Node savedNode = nodeRepository.save(node);
		
		//Keep the parent updated with new child
		parent.getChildNodeList().add(savedNode);
		nodeRepository.save(parent);
			
		NodeDetailsVM vm = createNodeVm(node);
		logger.info("Create Node : END:  " + LocalDateTime.now());
		
		return vm;
	}
	

		/*** Delete a given node id 
	 * @throws NodeManagerException ***/
	public void deleteNode(Long id) throws NodeManagerException {
		Optional<Node> nodeOptional = nodeRepository.findById(id);
		if(!nodeOptional.isPresent()) {
			throw new NodeManagerException("Node cannot be found");
		}
		nodeRepository.delete(nodeOptional.get());
	}

	/** static method to derive height of any node **/
	private int calculateNodeHeight(Node node) {
		int height = 0;
		Stack<Node> nodes = new Stack<Node>();
		nodes.push(node);
		while(!nodes.isEmpty()) {
			node = nodes.pop();
			if (node.getParentNode() == null) { 
				break;			
			}else {
				nodes.push(node.getParentNode());
				height++;
			}
		}
		return height;
	}


	/** method to check if the destination parent has been a child in source node's hierarchy**/
	private boolean isDestParentAChild(Node destParentNode, int nodeId) {
		Stack<Node> nodes = new Stack<Node>();
		nodes.push(destParentNode);
		boolean result = false;
		while (!nodes.isEmpty()) {
			destParentNode = nodes.pop();
			if (destParentNode.getParentNode() == null) {
				result = false;
				break;
			}
			if (destParentNode.getNodeID().intValue() == nodeId) {
				result = true;
				break;
			} else {
				nodes.push(destParentNode.getParentNode());
			}
		}
		return result;
	}
	
	/** method to add all the nested nodes of a given node to a List**/
	private List<Node> getNodesAsFlatList(Node entity) {
		List<Node> listOfNodes = new ArrayList<Node>();
		if (entity != null) {
			listOfNodes.add(entity);
			for(int i = 0; i < listOfNodes.size(); ++i) {
				Node n = listOfNodes.get(i);
				List<Node> children = n.getChildNodeList();
				if (children != null) {
					for (Node child: children) {
						if (!listOfNodes.contains(child)) {
							listOfNodes.add(child);
						}
					}
				}
			}
		}
		return listOfNodes;
	}
	
	/** method to fetch all nested nodes of the given node**/
	private NodeDetailsVM getNestedNodeDetails(List<NodeDetailsVM> nodes,Long nodeId) {
		Map<Long, NodeDetailsVM> nodesMap = new HashMap<>();
		for (NodeDetailsVM node : nodes) {
			nodesMap.put(node.getNodeID(), node);
        }
		nodesMap.values().forEach(v ->
		{
			NodeDetailsVM parent;
			if(nodesMap.containsKey(v.getParentNodeID())) {
				parent = nodesMap.get(v.getParentNodeID());
				parent.addChild(v);
			}
		}
		);
        return nodesMap.get(nodeId);
	}
	
	/**method to convert a node object to nodevm object**/
	private NodeDetailsVM createNodeVm(Node node) {
		NodeDetailsVM vm = new NodeDetailsVM();
		vm.setNodeID(node.getNodeID());
		vm.setHeight(calculateNodeHeight(node));
		vm.setNodeName(node.getNodeName());
		vm.setParentNodeID(node.getParentNode()!=null?node.getParentNode().getNodeID():null);
		vm.setRootNodeID(node.getRootNode()!=null?node.getRootNode().getNodeID():null);
		return vm;
	}


}
