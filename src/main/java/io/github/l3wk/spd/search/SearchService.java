package io.github.l3wk.spd.search;

import java.util.List;

import io.github.l3wk.spd.graph.Vertex;

public interface SearchService {

	public List<Vertex> findShortestPath(Vertex source, Vertex target);
}
