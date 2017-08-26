package io.github.l3wk.spd.graph;

import java.util.Objects;

public class Edge {

	private Integer id;
	private Vertex source;
	private Vertex destination;
	private int weight;
	
	public Edge(Integer id, Vertex source, Vertex destination, int weight) {
		
		this.id = id;
		this.source = source;
		this.destination = destination;
		this.weight = weight;
	}
	
	public Integer getId() {
		return id;
	}
	
	public Vertex getSource() {
		return source;
	}
	
	public Vertex getDestination() {
		return destination;
	}
	
	public int getWeight() {
		return weight;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(this.id);
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if (obj == null || this.getClass() != obj.getClass()) {
			return false;
		} else {
			
			Edge edge = (Edge) obj;
			
			return Objects.equals(this.id, edge.id);
		}
	}
}
