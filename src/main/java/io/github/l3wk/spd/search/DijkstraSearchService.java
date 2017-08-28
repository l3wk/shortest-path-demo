package io.github.l3wk.spd.search;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

import io.github.l3wk.spd.graph.Graph;
import io.github.l3wk.spd.graph.Vertex;

public class DijkstraSearchService implements SearchService {
	
	// Shortest path search service using Dijkstra's algorithm with priority queue.
	//
	// References:
	//	- https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm
	//	- http://www.vogella.com/tutorials/JavaAlgorithmsDijkstra/article.html
	//  - https://codereview.stackexchange.com/questions/32354/implementation-of-dijkstras-algorithm

	private class VertexDistanceComparator implements Comparator<Vertex> {
		
		@Override
		public int compare(Vertex left, Vertex right) {
			
			int l = getShortestDistance(left);
			int r = getShortestDistance(right);
			
			return l == r ? 0 : (l > r ? 1 : -1);
		}
	};
	
	private static final int DEFAULT_INITIAL_QUEUE_SIZE = 10;
	
	private Graph graph;
	
	private Queue<Vertex> unvisited;
	
	private Map<Vertex, Integer> distances;
	private Map<Vertex, Vertex> predecessors;
	
	public DijkstraSearchService(Graph graph) {
		
		this.graph = graph;
	}
	
	@Override
	public Stack<Vertex> findShortestPath(Vertex source, Vertex target) {
		
		final Set<Vertex> visited = new HashSet<Vertex>();
		
		unvisited = new PriorityQueue<Vertex>(DEFAULT_INITIAL_QUEUE_SIZE, new VertexDistanceComparator());
		
		distances = new HashMap<Vertex, Integer>();
		predecessors = new HashMap<Vertex, Vertex>();

		if (graph != null && source != null) {
			
			initialiseSearch(source);
			
			while (!unvisited.isEmpty()) {
				
				Vertex node = unvisited.poll();
												
				if (visited.add(node) && node.equals(target)) {
					break;	// terminate the search, we found the shortest path!
				} else {
					graph.getNeighbours(node).stream()
						.filter(neighbour -> !visited.contains(neighbour))
						.forEach(unvisitedNeighbour -> findShortestDistance(node, unvisitedNeighbour));
				}
			}
		}
		
		return getPath(target);
	}
	
	private void initialiseSearch(Vertex source) {
		
		distances.put(source, 0);
		unvisited.add(source);
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
