package io.github.l3wk.spd.search;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

import org.junit.Test;

import io.github.l3wk.spd.graph.Edge;
import io.github.l3wk.spd.graph.Graph;
import io.github.l3wk.spd.graph.Vertex;

public class DijkstraSearchServiceTestCase {
	
	@Test
	public void testFindShortestPath_nullGraph() {
		
		SearchService service = new DijkstraSearchService(null);
		
		assertTrue(service.findShortestPath(buildVertex(1), buildVertex(2)).isEmpty());
	}
	
	@Test
	public void testFindShortestPath_nullSource() {
		
		SearchService service = new DijkstraSearchService(buildGraph());
		
		assertTrue(service.findShortestPath(null, buildVertex(1)).isEmpty());
	}
	
	@Test
	public void testFindShortestPath_nullTarget() {
		
		SearchService service = new DijkstraSearchService(buildGraph());
		
		assertTrue(service.findShortestPath(buildVertex(1), null).isEmpty());
	}
	
	@Test
	public void testFindShortestPath_noPathBetweenVertexes() {
			
		List<Vertex> v = Arrays
				.asList(buildVertex(1), 
						buildVertex(2));
		
		SearchService service = new DijkstraSearchService(buildGraph(v));
		
		assertTrue(service.findShortestPath(v.get(0), v.get(1)).isEmpty());
	}
	
	@Test
	public void testFindShortestPath_pathContainsInfiniteLoop() {
		
		List<Edge> e = Arrays.asList(buildEdge(1, 1, 1));
		
		List<Vertex> v = Arrays
				.asList(buildVertex(1, Arrays.asList(1)), 
						buildVertex(2));
		
		SearchService service = new DijkstraSearchService(new Graph(v, e));
		
		assertTrue(service.findShortestPath(v.get(0), v.get(1)).isEmpty());
	}
	
	@Test
	public void testFindShortestPath_singlePath() {
		
		// Graph contains one path from v0 to v1 that contains one edge:
		//
		// v0--(1)--v1
		//
		// We expect the search to return this path (v0 -> v1).
		
		List<Edge> e = Arrays.asList(buildEdge(1, 1, 2));
		
		List<Vertex> v = Arrays
				.asList(buildVertex(1, Arrays.asList(1)), 
						buildVertex(2));
		
		SearchService service = new DijkstraSearchService(new Graph(v, e));
		
		assertPath(service.findShortestPath(v.get(0), v.get(1)), Arrays.asList(v.get(0), v.get(1)));
	}
	
	@Test
	public void testFindShortestPath_multiplePathsWithSameLengthsAndSameWeights() {
		
		// Graph contains two paths from v0 to v3, each with two edges that have the same weight:
		//
		//   -(1)--v1--(1)-
		//  /              \
		// v0              v3
		//  \              /
		//   -(1)--v2--(1)-
		//
		// We expect the search to return the first path (v0 -> v1 -> v3) (either path is valid).
		
		List<Edge> e = Arrays
				.asList(buildEdge(1, 1, 2), 
						buildEdge(2, 1, 3), 
						buildEdge(3, 2, 4), 
						buildEdge(4, 3, 4));
		
		List<Vertex> v = Arrays
				.asList(buildVertex(1, Arrays.asList(1, 2)), 
						buildVertex(2, Arrays.asList(3)), 
						buildVertex(3, Arrays.asList(4)), 
						buildVertex(4));
		
		SearchService service = new DijkstraSearchService(new Graph(v, e));
		
		assertPath(service.findShortestPath(v.get(0), v.get(3)), Arrays.asList(v.get(0), v.get(1), v.get(3)));
	}
	
	@Test
	public void testFindShortestPath_multiplePathsWithSameLengthsButDifferentWeights() {
		
		// Graph contains two paths from v0 to v3, each with two edges, however one path (v0 -> v1 - v3) 
		// has greater total edge weight than the other:
		//
		//   -(1)--v1--(2)-
		//  /              \
		// v0              v3
		//  \              /
		//   -(1)--v2--(1)-
		//
		// We expect the search to return the path with lowest overall weight (v0 -> v2 -> v3).
		
		List<Edge> e = Arrays
				.asList(buildEdge(1, 1, 2), 
						buildEdge(2, 1, 3), 
						buildEdge(3, 2, 4, 2), 
						buildEdge(4, 3, 4));
		
		List<Vertex> v = Arrays
				.asList(buildVertex(1, Arrays.asList(1, 2)), 
						buildVertex(2, Arrays.asList(3)), 
						buildVertex(3, Arrays.asList(4)), 
						buildVertex(4));
		
		SearchService service = new DijkstraSearchService(new Graph(v, e));
		
		assertPath(service.findShortestPath(v.get(0), v.get(3)), Arrays.asList(v.get(0), v.get(2), v.get(3)));
	}
	
	@Test
	public void testFindShortestPath_multiplePathsWithDifferentLengthsAndEqualOverallWeight() {
		
		// Graph contains two paths from v0 to v2, one with one edge, the other with two; the
		// overall weight of each path is the same:
		//
		//   -(1)--v1--(1)-
		//  /              \
		// v10             v2
		//  \              /
		//   ------(2)-----
		//
		// We expect the search to return the path with fewest edges (v0 -> v2).
		
		List<Edge> e = Arrays
				.asList(buildEdge(1, 1, 2), 
						buildEdge(2, 1, 3, 2), 
						buildEdge(3, 2, 3));
		
		List<Vertex> v = Arrays
				.asList(buildVertex(1, Arrays.asList(1, 2)), 
						buildVertex(2, Arrays.asList(3)), 
						buildVertex(3));
		
		SearchService service = new DijkstraSearchService(new Graph(v, e));
		
		assertPath(service.findShortestPath(v.get(0), v.get(2)), Arrays.asList(v.get(0), v.get(2)));
	}
	
	@Test
	public void testFindShortestPath_multiplePathsWithDifferentLengthsAndShortestPathHasLowestWeight() {
		
		// Graph contains two paths from v0 to v2, one with one edge, the other with two; each 
		// edge has the same weight:
		//
		//   -(1)--v1--(1)-
		//  /              \
		// v0              v2
		//  \              /
		//   ------(1)-----
		//
		// We expect the search to return the path with fewest edges and lowest overall weight (v0 -> v2).
		
		List<Edge> e = Arrays
				.asList(buildEdge(1, 1, 2), 
						buildEdge(2, 1, 3), 
						buildEdge(3, 2, 3));
		
		List<Vertex> v = Arrays
				.asList(buildVertex(1, Arrays.asList(1, 2)), 
						buildVertex(2, Arrays.asList(3)), 
						buildVertex(3));
		
		SearchService service = new DijkstraSearchService(new Graph(v, e));
		
		assertPath(service.findShortestPath(v.get(0), v.get(2)), Arrays.asList(v.get(0), v.get(2)));
	}
	
	@Test
	public void testFindShortestPath_multiplePathsWithDifferentLengthsAndLongestPathHasLowestWeight() {
		
		// Graph contains two paths from v0 to v2, one with one edge, the other with two; the path
		// with a single edge has the greatest weight:
		//
		//   -(1)--v1--(1)-
		//  /              \
		// v0              v2
		//  \              /
		//   ------(3)-----
		//
		// We expect the search to return the path with the most edges but lowest overall weight (v0 -> v1 -> v2).
		
		List<Edge> e = Arrays
				.asList(buildEdge(1, 1, 2), 
						buildEdge(2, 1, 3, 3), 
						buildEdge(3, 2, 3));
		
		List<Vertex> v = Arrays
				.asList(buildVertex(1, Arrays.asList(1, 2)), 
						buildVertex(2, Arrays.asList(3)), 
						buildVertex(3));
		
		SearchService service = new DijkstraSearchService(new Graph(v, e));
		
		assertPath(service.findShortestPath(v.get(0), v.get(2)), Arrays.asList(v.get(0), v.get(1), v.get(2)));
	}
	
	@Test
	public void testFindShortestPath_multiplePathsIncludingInfiniteLoop() {
		
		// Graph contains three paths of different lengths from v0 to v5, one of which terminates in an
		// infinite loop:
		//
		//   -(1)--v1--(1)--v4--(1)-
		//  /                       \
		// v0-(1)--v2--@            v5
		//  \                       /
		//   -----(1)--v3--(1)------
		//
		// We expect the search to avoid being stuck inside the loop and return the shortest path (v0 -> v3 -> v5).
		
		List<Edge> e = Arrays
				.asList(buildEdge(1, 1, 2), 
						buildEdge(2, 1, 3), 
						buildEdge(3, 1, 4), 
						buildEdge(4, 2, 5), 
						buildEdge(5, 3, 3), 
						buildEdge(6, 4, 6), 
						buildEdge(7, 5, 6));
		
		List<Vertex> v = Arrays
				.asList(buildVertex(1, Arrays.asList(1, 2, 3)), 
						buildVertex(2, Arrays.asList(4)), 
						buildVertex(3, Arrays.asList(5)), 
						buildVertex(4, Arrays.asList(6)), 
						buildVertex(5, Arrays.asList(7)), 
						buildVertex(6));
		
		SearchService service = new DijkstraSearchService(new Graph(v, e));
		
		assertPath(service.findShortestPath(v.get(0), v.get(5)), Arrays.asList(v.get(0), v.get(3), v.get(5)));
	}
	
	private Edge buildEdge(Integer id, Integer sourceId, Integer destinationId) {
		
		return buildEdge(id, sourceId, destinationId, 1);
	}
	
	private Edge buildEdge(Integer id, Integer sourceId, Integer destinationId, int weight) {
		
		return new Edge(id, sourceId, destinationId, weight);
	}

	private Vertex buildVertex(Integer id) {
		
		return buildVertex(id, Collections.<Integer>emptyList());
	}
	
	private Vertex buildVertex(Integer id, List<Integer> edgeIds) {
		
		return new Vertex(id, edgeIds);
	}
	
	private Graph buildGraph() {
		
		return buildGraph(Collections.<Vertex>emptyList());
	}
	
	private Graph buildGraph(List<Vertex> vertexes) {
		
		return new Graph(vertexes, Collections.<Edge>emptyList());
	}
	
	private void assertPath(Stack<Vertex> path, List<Vertex> expectedPath) {
		
		assertFalse(path.isEmpty());
		assertEquals(path.size(), expectedPath.size());
		
		expectedPath.forEach(vertex -> assertEquals(vertex, path.pop()));
	}
}
