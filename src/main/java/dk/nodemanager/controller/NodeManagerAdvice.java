package dk.nodemanager.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import dk.nodemanager.exception.NodeManagerException;

@ControllerAdvice
public class NodeManagerAdvice {

	@ExceptionHandler({NodeManagerException.class})
    public ResponseEntity<String> handleNotFoundException(NodeManagerException e) {
        return error(HttpStatus.BAD_REQUEST, e);
    }
	private ResponseEntity<String> error(HttpStatus status, Exception e) {
        return ResponseEntity.status(status).body(e.getMessage());
    }
}
