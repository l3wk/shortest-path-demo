package io.github.l3wk.spd.graph;

import java.util.HashMap;

public class Graph {

	private HashMap<Integer, Vertex> vertexesById;
	private HashMap<Integer, Edge> edgesById;
	
	public Graph(HashMap<Integer, Vertex> vertexesById, HashMap<Integer, Edge> edgesById) {
		
		this.vertexesById = vertexesById;
		this.edgesById = edgesById;
	}
	
	public Vertex getVertex(Integer id) {
		return vertexesById.get(id);
	}
	
	public Edge getEdge(Integer id) {
		return edgesById.get(id);
	}
}
