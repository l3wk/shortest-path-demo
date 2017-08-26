package io.github.l3wk.spd.graph;

import java.util.Objects;

public class Edge {

	private Integer id;
	private Integer sourceId;
	private Integer destinationId;
	private int weight;
	
	public Edge(Integer id, Integer sourceId, Integer destinationId, int weight) {
		
		this.id = id;
		this.sourceId = sourceId;
		this.destinationId = destinationId;
		this.weight = weight;
	}
	
	public Integer getId() {
		return id;
	}
	
	public Integer getSourceId() {
		return sourceId;
	}
	
	public Integer getDestinationId() {
		return destinationId;
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
