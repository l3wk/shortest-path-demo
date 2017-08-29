package io.github.l3wk.spd.loader;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;

import io.github.l3wk.spd.graph.Edge;
import io.github.l3wk.spd.graph.Graph;
import io.github.l3wk.spd.graph.Vertex;

public class JsonSocialNetworkGraphLoaderTestCase {

	private GraphLoader loader;
	
	@Before
	public void setUp() throws Exception {
		
		loader = new JsonSocialNetworkGraphLoader();
	}

	@Test
	public void testLoad_nullStream() {
		
		Graph graph = loader.load(null);
		
		assertNotNull(graph);
		assertTrue(graph.isEmpty());
	}
	
	@Test
	public void testLoad_emptyStream() {
		
		Graph graph = loader.load(Stream.empty());
		
		assertNotNull(graph);
		assertTrue(graph.isEmpty());
	}
	
	@Test
	public void testLoad_emptyString() {
		
		Graph graph = loader.load(Stream.of(""));
		
		assertNotNull(graph);
		assertTrue(graph.isEmpty());
	}
	
	@Test
	public void testLoad_emptyEntry() {
		
		Graph graph = loader.load(Stream.of("{}"));
		
		assertNotNull(graph);
		assertTrue(graph.isEmpty());
	}
	
	@Test
	public void testLoad_singleVertexNoEdges() {
		
		Graph graph = loader.load(Stream.of("{\"user\": 1}"));
		
		assertNotNull(graph);
		assertFalse(graph.isEmpty());
		
		List<Vertex> vertexes = new ArrayList<>(graph.getVertexes());
		
		assertEquals(1, vertexes.size());
		assertVertex(vertexes.get(0), 1);
		
		assertTrue(graph.getEdges().isEmpty());
	}
	
	@Test
	public void testLoad_singleVertexEmptyFriends() {
		
		Graph graph = loader.load(Stream.of("{\"user\": 1, \"friends\": []}"));
		
		assertNotNull(graph);
		assertFalse(graph.isEmpty());
		
		List<Vertex> vertexes = new ArrayList<>(graph.getVertexes());
		
		assertEquals(1, vertexes.size());
		assertVertex(vertexes.get(0), 1);
		
		assertTrue(graph.getEdges().isEmpty());
	}
	
	@Test
	public void testLoad_multipleVertexesNoEdges() {
		
		Graph graph = loader.load(Stream.of("{\"user\": 1}", "{\"user\": 2}"));
		
		assertNotNull(graph);
		assertFalse(graph.isEmpty());
		
		List<Vertex> vertexes = new ArrayList<>(graph.getVertexes());
		
		assertEquals(2, vertexes.size());
		assertVertex(vertexes.get(0), 1);
		assertVertex(vertexes.get(1), 2);
		
		assertTrue(graph.getEdges().isEmpty());
	}
	
	@Test
	public void testLoad_multipleVertexesSingleEdgeWithoutWeight() {
		
		Graph graph = loader.load(Stream
				.of("{\"user\": 1, \"friends\": [2]}", 
					"{\"user\": 2}"));
		
		assertNotNull(graph);
		assertFalse(graph.isEmpty());
		
		List<Vertex> vertexes = new ArrayList<>(graph.getVertexes());
		
		assertEquals(2, vertexes.size());
		assertVertex(vertexes.get(0), 1, 1);
		assertVertex(vertexes.get(1), 2);
		
		List<Edge> edges = new ArrayList<>(graph.getEdges());
		
		assertEquals(1, edges.size());
		assertEdge(edges.get(0), 1, 2, JsonSocialNetworkGraphLoader.DEFAULT_SKILL);
	}
	
	@Test
	public void testLoad_multipleVertexesSingleEdgeWithWeights() {
		
		Graph graph = loader.load(Stream
				.of("{\"user\": 1, \"friends\": [2], \"skill\": 10}", 
					"{\"user\": 2, \"skill\": 2}"));
		
		assertNotNull(graph);
		assertFalse(graph.isEmpty());
		
		List<Vertex> vertexes = new ArrayList<>(graph.getVertexes());
		
		assertEquals(2, vertexes.size());
		assertVertex(vertexes.get(0), 1, 1);
		assertVertex(vertexes.get(1), 2);
		
		List<Edge> edges = new ArrayList<>(graph.getEdges());
		
		assertEquals(1, edges.size());
		assertEdge(edges.get(0), 1, 2, 8);
	}
	
	@Test
	public void testLoad_multipleVertexesMultipleEdgesWithoutWeight() {
		
		Graph graph = loader.load(Stream
				.of("{\"user\": 1, \"friends\": [2, 3]}", 
					"{\"user\": 2, \"friends\": [3]}", 
					"{\"user\": 3}"));
		
		assertNotNull(graph);
		assertFalse(graph.isEmpty());
		
		List<Vertex> vertexes = new ArrayList<>(graph.getVertexes());
		
		assertEquals(3, vertexes.size());
		assertVertex(vertexes.get(0), 1, 2);
		assertVertex(vertexes.get(1), 2, 1);
		assertVertex(vertexes.get(2), 3);
		
		List<Edge> edges = new ArrayList<>(graph.getEdges());
		
		assertEquals(3, edges.size());
		assertEdge(edges.get(0), 1, 2, JsonSocialNetworkGraphLoader.DEFAULT_SKILL);
		assertEdge(edges.get(1), 1, 3, JsonSocialNetworkGraphLoader.DEFAULT_SKILL);
		assertEdge(edges.get(2), 2, 3, JsonSocialNetworkGraphLoader.DEFAULT_SKILL);
	}
	
	@Test
	public void testLoad_multipleVertexesMultipleEdgesWithWeights() {
		
		Graph graph = loader.load(Stream
				.of("{\"user\": 1, \"friends\": [2, 3], \"skill\": 10}", 
					"{\"user\": 2, \"friends\": [3], \"skill\": 7}", 
					"{\"user\": 3, \"skill\": 4}"));
		
		assertNotNull(graph);
		assertFalse(graph.isEmpty());
		
		List<Vertex> vertexes = new ArrayList<>(graph.getVertexes());
		
		assertEquals(3, vertexes.size());
		assertVertex(vertexes.get(0), 1, 2);
		assertVertex(vertexes.get(1), 2, 1);
		assertVertex(vertexes.get(2), 3);
		
		List<Edge> edges = new ArrayList<>(graph.getEdges());
		
		assertEquals(3, edges.size());
		assertEdge(edges.get(0), 1, 2, 3);
		assertEdge(edges.get(1), 1, 3, 6);
		assertEdge(edges.get(2), 2, 3, 6);
	}
	
	private void assertVertex(Vertex vertex, Integer expectedId) {
		
		assertVertex(vertex, expectedId, 0);
	}
	
	private void assertVertex(Vertex vertex, Integer expectedId, int expectedEdgeIdCount) {
		
		assertNotNull(vertex);
		assertEquals(expectedId, vertex.getId());
		assertEquals(expectedEdgeIdCount, vertex.getEdgeIds().size());
	}
	
	private void assertEdge(Edge edge, Integer expectedSourceId, Integer expectedTargetId, int expectedWeight) {
		
		assertNotNull(edge);
		assertNotNull(edge.getId());
		assertEquals(expectedSourceId, edge.getSourceId());
		assertEquals(expectedTargetId, edge.getTargetId());
		assertEquals(expectedWeight, edge.getWeight());
	}
}
