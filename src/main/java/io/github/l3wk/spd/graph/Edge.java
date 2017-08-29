package io.github.l3wk.spd.graph;

import java.util.Objects;

public class Edge {

	private Integer id;
	private Integer sourceId;
	private Integer targetId;
	private int weight;
	
	public Edge(Integer id, Integer sourceId, Integer targetId, int weight) {
		
		this.id = id;
		this.sourceId = sourceId;
		this.targetId = targetId;
		this.weight = weight;
	}
	
	public Integer getId() {
		return id;
	}
	
	public Integer getSourceId() {
		return sourceId;
	}
	
	public Integer getTargetId() {
		return targetId;
	}
	
	public int getWeight() {
		return weight;
	}
	
	public void setWeight(int weight) {
		this.weight = weight;
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
