package application;

import java.util.List;
import java.util.Set;

public interface GraphADT<E> {
	// adds a new vertex to the graph
	void addVertex(E vertex);
	// adds an edge between vertices
	void addEdge(E vertex1, E vertex2);
	// removes the edge between vertices
	void removeEdge(E vertex1, E vertex2);
	// removes vertex and associated edges
	void removeVertex(E vertex);
	// returns all vertices in the graph
	Set<E> getAllVertecies();
	// determines if the vertex is in the graph
	boolean contains(E vertex);
	//gets all adjacent vertices of a provided vertex
	List<E> getAdjacentVerticesOf();
	// returns the total # of edges in graph
	int size();
	// returns the total # of vertices in graph
	int order();
}
