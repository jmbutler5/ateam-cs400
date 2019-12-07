package application;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;


/**
 * Filename:   Graph.java
 * Project:    p4
 * Authors:    
 * 
 * Directed and unweighted graph implementation using adjacency matrix
 */

public class Graph implements GraphADT {
	
	private final int INITIALSIZE = 5;
	private boolean[][] matrix;
	private ArrayList<String> vertices;
	private int matrixSize;
	//private String vertex;
	private int edgeCount;
	private int vertexCount; 
	/*
	 * Default no-argument constructor
	 */ 
	public Graph() {
		matrix = new boolean[INITIALSIZE][INITIALSIZE];
		matrixSize = INITIALSIZE;
		vertices = new ArrayList<String>();
		edgeCount = 0;
		vertexCount = 0;
		
	}

	/**
     * Add new vertex to the graph.
     *
     * If vertex is null or already exists,
     * method ends without adding a vertex or 
     * throwing an exception.
     * 
     * Valid argument conditions:
     * 1. vertex is non-null
     * 2. vertex is not already in the graph 
     */
	public void addVertex(String vertex) {
		if(vertex == null)
			return;
		// Check existing vertex list for duplicate
		if(vertices.contains(vertex))
			return;
		// Create new matrix of size 2x vertexCount every time matrix is more
		// than half full and copy values from old matrix.
		if(vertices.size() + 1 > 0.5 * matrixSize) {
			matrixSize = 2*matrixSize;
			boolean[][] matrixOld = matrix;
			matrix = new boolean[matrixSize][matrixSize];
			for(int i = 0; i < matrixOld.length; i++) {
				for(int j=0; j < matrixOld[0].length; j++) {
					matrix[i][j] = matrixOld[i][j];
				}
			}
		}

		vertexCount++;
		vertices.add(vertex);
		return;
	}

	/**
     * Remove a vertex and all associated 
     * edges from the graph.
     * 
     * If vertex is null or does not exist,
     * method ends without removing a vertex, edges, 
     * or throwing an exception.
     * 
     * Valid argument conditions:
     * 1. vertex is non-null
     * 2. vertex is not already in the graph 
     */
	public void removeVertex(String vertex) {
		if(vertex == null)
			return;
		int v = vertices.indexOf(vertex);
		if (v == -1)
			return;
		
		for(int i = 0; i < matrix[v].length; i++) {
			// Set all outgoing edges to false 
			matrix[v][i] = false;
			// set all incoming edges to false
			matrix[i][v] = false;
		}
		// Set vertex to null to effectively remove it from the list while
		// preserving the adjacency matrix. 
		// To improve spatial efficiency, could be replaced with a placeholder
		// which is searched for when adding a new vertex.
		vertices.set(v, null);
		vertexCount --;
		
	}

	/**
     * Add the edge from vertex1 to vertex2
     * to this graph.  (edge is directed and unweighted)
     * If either vertex does not exist,
     * add vertex, and add edge, no exception is thrown.
     * If the edge exists in the graph,
     * no edge is added and no exception is thrown.
     * 
     * Valid argument conditions:
     * 1. neither vertex is null
     * 2. both vertices are in the graph 
     * 3. the edge is not in the graph
	 */
	public void addEdge(String vertex1, String vertex2) {
		if(vertex1 == null || vertex2 == null)
			return;
		//Find locations of vertices in ArrayList, and add them to the adjacency
		//matrix according to their index
		int v1 = vertices.indexOf(vertex1);
		if(v1 == -1) {
			addVertex(vertex1);
			v1 = vertices.indexOf(vertex1);
		}
		int v2 = vertices.indexOf(vertex2);
		if(v2 == -1) {
			addVertex(vertex2);
			v2 = vertices.indexOf(vertex2);
		}
		matrix[v1][v2] = true;
		edgeCount ++;
	}
	
	/**
     * Remove the edge from vertex1 to vertex2
     * from this graph.  (edge is directed and unweighted)
     * If either vertex does not exist,
     * or if an edge from vertex1 to vertex2 does not exist,
     * no edge is removed and no exception is thrown.
     * 
     * Valid argument conditions:
     * 1. neither vertex is null
     * 2. both vertices are in the graph 
     * 3. the edge from vertex1 to vertex2 is in the graph
     */
	public void removeEdge(String vertex1, String vertex2) {
		if(vertex1 == null || vertex2 == null)
			return;
		//Find locations of vertices in ArrayList, and add them to the adjacency
		//matrix according to their index
		int v1 = vertices.indexOf(vertex1);
		int v2 = vertices.indexOf(vertex2);
		if (v1 == -1 || v2 == -1)
			return;
		matrix[v1][v2] = false;
		edgeCount --;
	}	

	/**
     * Returns a Set that contains all the vertices
     * 
	 */
	public Set<String> getAllVertices() {
		Set<String> verticeSet =  new HashSet<String>();
		vertices.forEach((n) -> verticeSet.add(n));
		return verticeSet;
	}

	/**
     * Get all the neighbor (adjacent) vertices of a vertex
     *
	 */
	public List<String> getAdjacentVerticesOf(String vertex) {
		List<String> adjVertices = new ArrayList<String>();
		// Finds column of vertex, then searches matrix for any edges
		int v = vertices.indexOf(vertex);
		for(int i = 0; i < matrix[v].length; i++) {
			if(matrix[v][i] == true)
				// If edge exists, add vertex from corresponding location in 
				// vertices array list
				adjVertices.add(vertices.get(i));
		}
		return adjVertices;
	}
	
	/**
     * Returns the number of edges in this graph.
     */
    public int size() {
        return edgeCount;
    }

	/**
     * Returns the number of vertices in this graph.
     */
	public int order() {
        return vertexCount;
    }
}
