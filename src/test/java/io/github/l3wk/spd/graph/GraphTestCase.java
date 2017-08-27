package io.github.l3wk.spd.graph;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class GraphTestCase {

	@Test
	public void testGetNeighbours_nullVertex() {
		
		Graph graph = new Graph(Collections.<Vertex>emptyList(), Collections.<Edge>emptyList());
		
		List<Vertex> neighbours = graph.getNeighbours(null);
		
		assertNotNull(neighbours);
		assertTrue(neighbours.isEmpty());
	}

	@Test
	public void testGetNeighbours_emptyGraph() {
		
		Vertex vertex = new Vertex(1, Collections.<Integer>emptyList());
		
		Graph graph = new Graph(Collections.<Vertex>emptyList(), Collections.<Edge>emptyList());
		
		List<Vertex> neighbours = graph.getNeighbours(vertex);
		
		assertNotNull(neighbours);
		assertTrue(neighbours.isEmpty());
	}
	
	@Test
	public void testGetNeighbours_vertexWithNoNeighbours() {
		
		Vertex vertex = new Vertex(1, Collections.<Integer>emptyList());
	
		Graph graph = new Graph(Arrays.asList(vertex), Collections.<Edge>emptyList());
		
		List<Vertex> neighbours = graph.getNeighbours(vertex);
		
		assertNotNull(neighbours);
		assertTrue(neighbours.isEmpty());
	}
	
	@Test
	public void testGetNeighbours_vertexWithSingleNeighbour() {
		
		Edge e1 = new Edge(1, 1, 2, 1);
		
		Vertex v1 = new Vertex(1, Arrays.asList(1));
		Vertex v2 = new Vertex(2, Collections.<Integer>emptyList());
		
		Graph graph = new Graph(Arrays.asList(v1, v2), Arrays.asList(e1));
		
		List<Vertex> neighbours = graph.getNeighbours(v1);
		
		assertNotNull(neighbours);
		assertEquals(1, neighbours.size());
		assertTrue(neighbours.contains(v2));
	}
	
	@Test
	public void testGetDistanceBeteenNeighbours_nullSource() {
		
		Vertex v1 = new Vertex(1, Collections.<Integer>emptyList());
		
		Graph graph = new Graph(Arrays.asList(v1), Collections.<Edge>emptyList());
		
		assertNull(graph.getDistanceBetweenNeighbours(null, v1));
	}
	
	@Test
	public void testGetDistanceBeteenNeighbours_nullTargetAndSourceWithNoEdge() {
		
		Vertex v1 = new Vertex(1, Collections.<Integer>emptyList());
		
		Graph graph = new Graph(Arrays.asList(v1), Collections.<Edge>emptyList());
		
		assertNull(graph.getDistanceBetweenNeighbours(v1, null));
	}
	
	@Test
	public void testGetDistanceBetweenNeighbours_nullTargetAndSourceWithSingleEdge() {
		
		Edge e1 = new Edge(1, 1, 2, 1);
		
		Vertex v1 = new Vertex(1, Arrays.asList(1));
		Vertex v2 = new Vertex(2, Collections.<Integer>emptyList());
		
		Graph graph = new Graph(Arrays.asList(v1, v2), Arrays.asList(e1));
		
		assertNull(graph.getDistanceBetweenNeighbours(v1, null));
	}
	
	@Test
	public void testGetDistanceBetweenNeighbours_emptyGraph() {
		
		Vertex v1 = new Vertex(1, Collections.<Integer>emptyList());
		Vertex v2 = new Vertex(2, Collections.<Integer>emptyList());
		
		Graph graph = new Graph(Collections.<Vertex>emptyList(), Collections.<Edge>emptyList());
		
		assertNull(graph.getDistanceBetweenNeighbours(v1, v2));
	}
	
	@Test
	public void testGetDistanceBetweenNeighbours_noEdgeBetweenNodes() {
		
		Vertex v1 = new Vertex(1, Collections.<Integer>emptyList());
		Vertex v2 = new Vertex(2, Collections.<Integer>emptyList());
		
		Graph graph = new Graph(Arrays.asList(v1, v2), Collections.<Edge>emptyList());
		
		assertNull(graph.getDistanceBetweenNeighbours(v1, v2));
	}
	
	@Test
	public void testGetDistanceBetweenNeighbours_nodesWithSingleEdgeDefined() {
		
		Edge e1 = new Edge(1, 1, 2, 1);
		
		Vertex v1 = new Vertex(1, Arrays.asList(1));
		Vertex v2 = new Vertex(2, Collections.<Integer>emptyList());
		
		Graph graph = new Graph(Arrays.asList(v1, v2), Arrays.asList(e1));
		
		Integer distance = graph.getDistanceBetweenNeighbours(v1, v2);
		
		assertNotNull(distance);
		assertEquals(e1.getWeight(), distance.intValue());
	}
	
	@Test
	public void testGetDistanceBetweenNeighbours_nodesWithMultipleEdgesDefined() {
		
		Edge e1 = new Edge(1, 1, 2, 1);
		Edge e2 = new Edge(2, 1, 2, 2);
		
		Vertex v1 = new Vertex(1, Arrays.asList(1, 2));
		Vertex v2 = new Vertex(2, Collections.<Integer>emptyList());
		
		Graph graph = new Graph(Arrays.asList(v1, v2), Arrays.asList(e1, e2));
		
		Integer distance = graph.getDistanceBetweenNeighbours(v1, v2);
		
		assertNotNull(distance);
		assertEquals(e1.getWeight(), distance.intValue());
	}
}
