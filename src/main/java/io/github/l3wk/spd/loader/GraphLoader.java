package io.github.l3wk.spd.loader;

import java.util.stream.Stream;

import io.github.l3wk.spd.graph.Graph;

public interface GraphLoader {

	public Graph load(Stream<String> stream);
}
