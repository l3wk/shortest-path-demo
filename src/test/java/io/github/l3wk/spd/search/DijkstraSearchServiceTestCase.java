package io.github.l3wk.spd.search;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;

import io.github.l3wk.spd.graph.Edge;
import io.github.l3wk.spd.graph.Graph;
import io.github.l3wk.spd.graph.Vertex;

public class DijkstraSearchServiceTestCase {
	
	private SearchService service;

	@Before
	public void setUp() throws Exception {
		
		//service = new DijkstraSearchService();
	}

	@Test
	public void testFindShortestPath_nullGraph() {
		
		service = new DijkstraSearchService(null);
		
		assertTrue(service.findShortestPath(buildVertex(1), buildVertex(2)).isEmpty());
	}
	
	@Test
	public void testFindShortestPath_nullSource() {
		
		service = new DijkstraSearchService(buildGraph());
		
		assertTrue(service.findShortestPath(null, buildVertex(1)).isEmpty());
	}
	
	@Test
	public void testFindShortestPath_nullTarget() {
		
		service = new DijkstraSearchService(buildGraph());
		
		assertTrue(service.findShortestPath(buildVertex(1), null).isEmpty());
	}
	
	@Test
	public void testFindShortestPath_pathContainsInfiniteLoop() {
		
		Edge loop = buildEdge(1, 1, 1);
		
		Vertex v1 = buildVertex(1, Arrays.asList(1));
		Vertex v2 = buildVertex(2);
		
		service = new DijkstraSearchService(buildGraph(Arrays.asList(v1, v2), Arrays.asList(loop)));
		
		assertTrue(service.findShortestPath(v1, v2).isEmpty());
	}
	
	private Edge buildEdge(Integer id, Integer sourceId, Integer destinationId) {
		
		return new Edge(id, sourceId, destinationId, 1);
	}
	
	private Graph buildGraph() {
		
		return new Graph(Collections.<Integer, Vertex>emptyMap(), Collections.<Integer, Edge>emptyMap());
	}
	
	private Graph buildGraph(List<Vertex> vertexes, List<Edge> edges) {
		
		Map<Integer, Vertex> v = vertexes.stream().collect(Collectors.toMap(Vertex::getId, Function.identity()));
		Map<Integer, Edge> e = edges.stream().collect(Collectors.toMap(Edge::getId, Function.identity()));
		
		return new Graph(v, e);
	}

	private Vertex buildVertex(Integer id) {
		
		return buildVertex(id, Collections.<Integer>emptyList());
	}
	
	private Vertex buildVertex(Integer id, List<Integer> edges) {
		
		return new Vertex(id, edges);
	}
}
