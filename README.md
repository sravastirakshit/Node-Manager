# Node-Manager
A module to manage nodes in a tree structure

# Problem Statement
We have a root node (only one) and several children nodes, each one with its own children as well. It's a tree-based structure. Something like:     
We need two HTTP APIs that will serve the two basic operations:
1) Get all children nodes of a given node (the given node can be anyone in the tree structure).
2) Change the parent node of a given node (the given node can be anyone in the tree structure).

They need to answer quickly, even with tons of nodes. Also,we can't afford to lose this information, so some sort of persistence is required. 

Each node should have the following info:
1) node identification, 2) who is the parent node , 3) who is the root node 4) the height of the node. 

# Solution
The NodeManagerApp, a SpringBoot application, has been created to fetch any node's information of a Tree and also to manipulate any node's position.
In order to persist data, a H2 In-Memory database is used. Application uses Spring JPA to manage the entities and transactions. 
* It is advisable to use the REST Endpoints to create, update and delete nodes since the application code maintains the entities and their relations.
* There is a data.sql file, which creates the root node during application start up.  
 
The application contains below APIs (Exposed via Swagger at : http://localhost:8080/swagger-ui.html/)
* PUT /api/node -> This API is responsible for creating a node 
* POST /api/node -> This API is responsible for manipulating any node in the structure, Solution for #2 problem statement
* GET /api/nodes -> To fetch all node of the Tree structure
* GET /api/node/{id} -> To fetch any node by a node id (ID, Parent, Root, Height)
* GET /api/node/{id}/children -> To fetch all nested children node of a particular node, Solution for #1 problem statement
*  DELETE /api/node/{id} -> This API is responsible for deleting a node and its nested children if any

# Deployment
* Build the application from pom.xml using 'mvn package'. Run standalone using 'java -jar nodeManager.jar'
* For running in Docker Compose : Open Docker terminal, go to project directory and run command 'docker-compose up'

