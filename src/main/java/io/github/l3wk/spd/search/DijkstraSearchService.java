package io.github.l3wk.spd.search;

import java.util.Stack;

import io.github.l3wk.spd.graph.Graph;
import io.github.l3wk.spd.graph.Vertex;

public class DijkstraSearchService implements SearchService {

	private Graph graph;
	
	public DijkstraSearchService(Graph graph) {
		
		this.graph = graph;
	}
	
	@Override
	public Stack<Vertex> findShortestPath(Vertex source, Vertex target) {
		
		return new Stack<Vertex>();
	}
}
