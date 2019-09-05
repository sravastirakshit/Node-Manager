package dk.nodemanager.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import dk.nodemanager.exception.NodeManagerException;
import dk.nodemanager.services.NodeServices;
import dk.nodemanager.vm.NodeDetailsVM;
import dk.nodemanager.vm.NodeVM;

@Controller
@RequestMapping("/api")
public class NodeServiceController {
	@Autowired
	NodeServices nodeService;

	/** Get all Nodes * @throws NodeManagerException **/
	@GetMapping(value = "/nodes")
	public ResponseEntity<List<NodeDetailsVM>> getAllNodes() throws NodeManagerException {
		return new ResponseEntity<>(nodeService.getAllNodes(), HttpStatus.OK);
	}
		
	/** Get a Node * @throws NodeManagerException **/
	@GetMapping(value = "/node/{id}")
	public ResponseEntity<NodeDetailsVM> getNodeById(@PathVariable ("id") Long id) throws NodeManagerException {
		return new ResponseEntity<>(nodeService.getNodeById(id), HttpStatus.OK);
	}
	
	/**  Get all children of a given node id * @throws NodeManagerException **/
	@GetMapping(value = "/node/{id}/children")
	public ResponseEntity<List<NodeDetailsVM>> getNodeChildren(@PathVariable("id") Long id) throws NodeManagerException {
		return new ResponseEntity<>(nodeService.getNodeChildren(id), HttpStatus.OK);
	}

	/** Update parent node of a given node id * @throws NodeManagerException **/
	@PostMapping(value = "/node")
	public ResponseEntity<NodeDetailsVM> updateNode(@RequestBody NodeVM nvm) throws NodeManagerException {
		return new ResponseEntity<>(nodeService.updateNode(nvm), HttpStatus.OK);
	}
	
	/**  Create node * @throws NodeManagerException **/
	@PutMapping(value = "/node")
	public ResponseEntity<NodeDetailsVM> createNode(@RequestBody NodeVM nvm) throws NodeManagerException {
		return new ResponseEntity<>(nodeService.createNode(nvm), HttpStatus.OK);
	}
		
	/*** Delete a node * @throws NodeManagerException ***/
	@DeleteMapping(value = "/node/{id}")
	public ResponseEntity<?> deleteNode(@PathVariable("id") Long id) throws NodeManagerException {
		nodeService.deleteNode(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}