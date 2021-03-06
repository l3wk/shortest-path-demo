package io.github.l3wk.spd.graph;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Graph {

	private Map<Integer, Edge> edgesById;
	private Map<Integer, Vertex> vertexesById;
	
	public Graph(Map<Integer, Vertex> vertexesById, Map<Integer, Edge> edgesById) {

		this.edgesById = edgesById;
		this.vertexesById = vertexesById;
	}
	
	public Graph(Collection<Vertex> vertexes, Collection<Edge> edges) {

		this.edgesById = edges.stream().collect(Collectors.toMap(Edge::getId, Function.identity()));
		this.vertexesById = vertexes.stream().collect(Collectors.toMap(Vertex::getId, Function.identity()));
	}
	
	public Edge getEdge(Integer id) {
		return edgesById.get(id);
	}
	
	public Vertex getVertex(Integer id) {
		return vertexesById.get(id);
	}
	
	public Collection<Edge> getEdges() {
		return edgesById.values();
	}
	
	public Collection<Vertex> getVertexes() {
		return vertexesById.values();
	}
	
	public boolean isEmpty() {
		
		return vertexesById.isEmpty() && edgesById.isEmpty();
	}
	
	public List<Vertex> getNeighbours(Vertex vertex) {
		
		if (vertex == null) {
			return Collections.<Vertex>emptyList();
		}
		
		return vertex.getEdgeIds().stream()
				.map(id -> edgesById.get(id))
				.map(edge -> vertexesById.get(edge.getTargetId()))
				.collect(Collectors.toList());
	}
	
	public Integer getDistanceBetweenNeighbours(Vertex source, Vertex target) {
		
		if (source == null || target == null) {
			return null;
		}
		
		Optional<Edge> connection = source.getEdgeIds().stream()
				.map(id -> edgesById.get(id))
				.filter(edge -> edge.getTargetId().equals(target.getId()))
				.findFirst();
		
		return connection.isPresent() ? connection.get().getWeight() : null;
	}
}
