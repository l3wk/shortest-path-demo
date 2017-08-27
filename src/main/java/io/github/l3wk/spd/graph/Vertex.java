package io.github.l3wk.spd.graph;

import java.util.List;
import java.util.Objects;

public class Vertex {

	private Integer id;
	private List<Integer> edgeIds;
	
	public Vertex(Integer id, List<Integer> edgeIds) {
		
		this.id = id;
		this.edgeIds = edgeIds;
	}
	
	public Integer getId() {
		return id;
	}
	
	public List<Integer> getEdgeIds() {
		return edgeIds;
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
			
			Vertex vertex = (Vertex) obj;
			
			return Objects.equals(this.id, vertex.id);
		}
	}
	
	@Override
	public String toString() {
		return id.toString();
	}
}
