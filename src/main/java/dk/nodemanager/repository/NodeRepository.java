package dk.nodemanager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import dk.nodemanager.entity.Node;

import org.springframework.data.repository.CrudRepository;

@Repository
public interface NodeRepository extends CrudRepository<Node, Long> {


}
