package io.github.l3wk.spd.search;

import java.util.Stack;

import io.github.l3wk.spd.graph.Vertex;

public interface SearchService {

	public Stack<Vertex> findShortestPath(Vertex source, Vertex target);
}
