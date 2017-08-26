package io.github.l3wk.spd.graph;

import java.util.Map;

public class Graph {

	private Map<Integer, Vertex> vertexesById;
	private Map<Integer, Edge> edgesById;
	
	public Graph(Map<Integer, Vertex> vertexesById, Map<Integer, Edge> edgesById) {
		
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
