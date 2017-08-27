package io.github.l3wk.spd.search;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import io.github.l3wk.spd.graph.Graph;
import io.github.l3wk.spd.graph.Vertex;

public class DijkstraSearchService implements SearchService {
	
	// Shortest path search service using Dijkstra's algorithm.
	//
	// References:
	//	- https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm
	//	- http://www.vogella.com/tutorials/JavaAlgorithmsDijkstra/article.html

	private Graph graph;
	
	Set<Vertex> unvisited;
	
	private Map<Vertex, Integer> distances;
	private Map<Vertex, Vertex> predecessors;
	
	public DijkstraSearchService(Graph graph) {
		
		this.graph = graph;
	}
	
	@Override
	public Stack<Vertex> findShortestPath(Vertex source, Vertex target) {
		
		unvisited = new HashSet<Vertex>();
		
		distances = new HashMap<Vertex, Integer>();
		predecessors = new HashMap<Vertex, Vertex>();

		if (graph != null) {
			graph.getVertexes().forEach(vertex -> unvisited.add(vertex));
			
			distances.put(source, 0);
			
			while (!unvisited.isEmpty()) {
				
				Vertex node = getVertexWithMinimumDistance(unvisited);
				
				unvisited.remove(node);
				
				if (node.equals(target)) {
					break;	// terminate the search, we found the shortest path!
				} else {
					graph.getNeighbours(node).stream()
						.filter(neighbour -> unvisited.contains(neighbour))
						.forEach(unvisitedNeighbour -> findShortestDistance(node, unvisitedNeighbour));
				}
			}
		}
		
		return getPath(target);
	}
	
	private Vertex getVertexWithMinimumDistance(Set<Vertex> vertexes) {
		
		Vertex minimum = null;
		
		for (Vertex vertex: vertexes) {
			
			if (minimum == null) {
				minimum = vertex;
			} else {
				if (getShortestDistance(vertex) < getShortestDistance(minimum)) {
					minimum = vertex;
				}
			}
		}
		
		return minimum;
	}
	
	private Integer getShortestDistance(Vertex vertex) {
		
		Integer distance = distances.get(vertex);
		
		return distance == null ? Integer.MAX_VALUE : distance;
	}
	
	private void findShortestDistance(Vertex node, Vertex unvisitedNeighbour) {
		
		final int alternative = getShortestDistance(node) + graph.getDistanceBetweenNeighbours(node, unvisitedNeighbour);
		
		if (getShortestDistance(unvisitedNeighbour) > alternative) {
			
			distances.put(unvisitedNeighbour, alternative);
			predecessors.put(unvisitedNeighbour, node);
			
			unvisited.add(unvisitedNeighbour);
		}
	}

	private Stack<Vertex> getPath(Vertex target) {
		
		Stack<Vertex> path = new Stack<Vertex>();
		
		if (!predecessors.isEmpty()) {
			Vertex step = target;
			
			while (predecessors.get(step) != null) {
				path.push(step);
				step = predecessors.get(step);
			}
			path.push(step);
		}
		
		return path;
	}
}
